/**
 * Copyright (c) 2012 Tobias Boehm.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Marcel Bruch - initial API and implementation.
 *    Tobias Boehm - implementation
 */

package org.eclipse.recommenders.codesearch.rcp.index.searcher;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.google.common.collect.Lists;

public class SearchResult {

    private boolean allDocumentsLoaded = false;
    private List<Document> loadedDocs;

    public final Query query;
    public final TopDocs docs;
    public final IndexSearcher searcher;

    public SearchResult(final Query query, final TopDocs docs, final IndexSearcher searcher) {
        super();
        this.query = query;
        this.docs = docs;
        this.searcher = searcher;
    }

    public ScoreDoc[] scoreDocs() {
        return docs.scoreDocs;
    }

    public Document scoreDoc(final int index) throws CorruptIndexException, IOException {
        final ScoreDoc scoreDoc = docs.scoreDocs[index];
        return searcher.doc(scoreDoc.doc);
    }

    public List<Document> getAllDocuments() throws CorruptIndexException, IOException {
        if (allDocumentsLoaded)
            return loadedDocs;

        loadedDocs = Lists.newArrayList();
        for (int i = 0; i < docs.totalHits; i++) {
            loadedDocs.add(searcher.doc(i));
        }

        allDocumentsLoaded = true;

        return loadedDocs;
    }
}
