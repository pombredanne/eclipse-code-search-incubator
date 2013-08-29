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
package org.eclipse.recommenders.test.codesearch.rcp.indexer;

import static org.eclipse.recommenders.utils.Throws.throwUnhandledException;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.strategy.IFieldIndexingStrategy;
import org.eclipse.recommenders.codesearch.rcp.index.searcher.CodeSearcher;
import org.eclipse.recommenders.internal.codesearch.rcp.CodesearchIndexModule;

public class LuceneInMemoryFixture {

    public Version lversion;
    public RAMDirectory ldirectory;
    public IndexWriter lwriter;
    public Analyzer lanalyzer;
    public IndexReader lreader;
    private QueryParser lqueryParser;
    private IndexSearcher lsearcher;
    public CodeSearcher searcher;
    public CodeIndexer index;

    public LuceneInMemoryFixture() {
        try {
            final CodesearchIndexModule factory = new CodesearchIndexModule();
            lversion = factory.version();
            lanalyzer = factory.analyzer();
            ldirectory = new RAMDirectory();
            lwriter = factory.indexWriter(lversion, lanalyzer, ldirectory);
            lreader = factory.indexReader(lwriter);

            lqueryParser = factory.queryParser(lversion, lanalyzer);
            lsearcher = new IndexSearcher(lreader);
            searcher = new CodeSearcher(lsearcher, lreader, lqueryParser);
            index = new CodeIndexer(lwriter, searcher);

            CodeIndexer.setIndexingFieldInformationProvider(new TestFieldIndexingStrategy());

        } catch (final IOException e) {
            throwUnhandledException(e);
        }
    }

    private class TestFieldIndexingStrategy implements IFieldIndexingStrategy {
        @Override
        public Store getStore(String fieldName) {
            return Store.YES;
        }

        @Override
        public Index getIndex(String fieldName) {
            return Index.ANALYZED;
        }
    }
}
