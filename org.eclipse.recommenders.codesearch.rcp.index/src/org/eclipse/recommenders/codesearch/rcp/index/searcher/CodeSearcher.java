/**
 * Copyright (c) 2012 Tobias Boehm.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Tobias Boehm - initial API and implementation.
 */

package org.eclipse.recommenders.codesearch.rcp.index.searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.eclipse.recommenders.codesearch.rcp.index.termvector.ITermVectorConsumable;
import org.eclipse.recommenders.utils.Checks;
import org.eclipse.recommenders.utils.rcp.internal.RecommendersUtilsPlugin;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CodeSearcher implements ITermVectorConsumable {
    private final QueryParser parser;
    private IndexSearcher searcher;
    private IndexReader reader;

    @Inject
    public CodeSearcher(final IndexSearcher searcher, final IndexReader reader, final QueryParser parser)
            throws IOException {
        this.parser = parser;
        this.searcher = searcher;
        this.reader = reader;
    }

    @VisibleForTesting
    public List<Document> getDocuments() throws IOException {
        final MatchAllDocsQuery allDocsQuery = new MatchAllDocsQuery();
        return search(allDocsQuery, null);
    }

    public List<Document> search(final String queryString) throws IOException, ParseException {
        return search(queryString, null);
    }

    public List<Document> search(final String queryString, final FieldSelector selector) throws IOException,
            ParseException {
        final Query query = parser.parse(queryString);
        return search(query, selector);
    }

    public List<Document> search(final Query query, final FieldSelector selector) throws IOException {
        renewReader();
        return search(query, selector, reader.numDocs() + 1);
    }

    public List<Document> search(final Query query, final FieldSelector selector, final int maxHits) throws IOException {
        final SearchResult searchResult = lenientSearch(query, maxHits);
        final List<Document> result = toList(searchResult.scoreDocs(), selector);
        return result;
    }

    /**
     * caller is responsible for closing the searcher
     * 
     * @throws ParseException
     */
    public SearchResult lenientSearch(final String queryString) throws IOException, ParseException {
        renewReader();
        final Query query = parser.parse(queryString);
        final TopDocs docs = searcher.search(query, reader.numDocs() + 1);
        return new SearchResult(query, docs, searcher);
    }

    /**
     * caller is responsible for closing the searcher
     */
    public SearchResult lenientSearch(final Query query) throws IOException {
        renewReader();
        
        final TopDocs docs = searcher.search(query, reader.numDocs() + 1);
        return new SearchResult(query, docs, searcher);
    }

    /**
     * caller is responsible for closing the searcher
     */
    public SearchResult lenientSearch(final Query query, final int maxHits) throws IOException {
        Checks.ensureIsGreaterOrEqualTo(maxHits, 1, "max hits must be greater zero");
        renewReader();
        
        final TopDocs docs = searcher.search(query, maxHits);
        return new SearchResult(query, docs, searcher);
    }

    @Override
    public Set<String> getTermVector(final String[] fieldNames) {
        renewReader();
        final Set<String> result = Sets.newHashSet();
        for (final String field : fieldNames) {
            try {
                final String[] values = FieldCache.DEFAULT.getStringIndex(reader, field).lookup;

                result.addAll(Lists.newArrayList(values));
            } catch (final IOException e) {
                RecommendersUtilsPlugin.logError(e, "Exception during reopening of index reader");
            }
        }
        result.remove(null);
        return result;
    }

    private void renewReader() {
        try {
            final IndexReader newReader = IndexReader.openIfChanged(reader);
            if (newReader != null) {
                // reader was reopened
                reader.close();
                searcher.close();
                reader = newReader;
                searcher = new IndexSearcher(reader);
            }
        } catch (final Exception e) {
            RecommendersUtilsPlugin.logError(e, "Exception during reopening of index reader");
        }
    }

    private List<Document> toList(final ScoreDoc[] scoreDocs, final FieldSelector selector)
            throws CorruptIndexException, IOException {
        final List<Document> result = new ArrayList<Document>(scoreDocs.length);
        for (final ScoreDoc doc : scoreDocs) {
            if (selector != null) {
                result.add(searcher.doc(doc.doc, selector));
            } else {
                result.add(searcher.doc(doc.doc));
            }
        }
        return result;
    }

    public Document getDocument(int docId) throws CorruptIndexException, IOException {
        return searcher.doc(docId);
    }

    public QueryParser getParser() {
        return parser;
    }

    public int getDocCount(final Term t) throws IOException {
        return reader.docFreq(t);
    }

    public static Term prepareSearchTerm(String field, String value) {
        return new Term(field, value.toLowerCase());
    }
}
