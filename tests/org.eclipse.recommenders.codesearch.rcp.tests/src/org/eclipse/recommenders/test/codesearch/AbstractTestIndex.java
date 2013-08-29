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
package org.eclipse.recommenders.test.codesearch;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.utils.CompilationUnitHelper;
import org.eclipse.recommenders.codesearch.rcp.index.searcher.CodeSearcher;
import org.eclipse.recommenders.internal.codesearch.rcp.IndexUpdateService;
import org.eclipse.recommenders.test.codesearch.rcp.indexer.LuceneInMemoryFixture;
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture;
import org.eclipse.recommenders.utils.Pair;

import com.google.common.collect.Lists;

public class AbstractTestIndex {

    private CodeIndexer index;
    private CodeSearcher searcher;
    private final List<Document> docs = Lists.newArrayList();
    private Document currentDoc = null;
    private final String TestClassName = "MyInstanceOfClass";

    public AbstractTestIndex() {
        IndexUpdateService.setBackgroundIndexerActive(false);
        // To prevent workspace events from being processed
    }

    public AbstractTestIndex newIndex() throws IOException {
        final LuceneInMemoryFixture f = new LuceneInMemoryFixture();
        index = f.index;
        searcher = f.searcher;
        return this;
    }

    public String getTestClassFileName() {
        return TestClassName + ".java";
    }

    public CodeIndexer getIndexer() {
        return index;
    }

    public CodeSearcher getSearchIndexer() throws IOException {
        return searcher;
    }

    public AbstractTestIndex newDocs() {
        docs.clear();
        return this;
    }

    public AbstractTestIndex newDoc() {
        if (currentDoc != null) {
            docs.add(currentDoc);
        }

        currentDoc = new Document();

        return this;
    }

    public AbstractTestIndex addField(final String fieldName, final String fieldValue) {
        if (currentDoc == null) {
            currentDoc = new Document();
        }

        CodeIndexer.addFieldToDocument(currentDoc, fieldName, fieldValue);

        return this;
    }

    public AbstractTestIndex finish() throws IOException {
        if (currentDoc != null) {
            docs.add(currentDoc);
        }

        currentDoc = null;

        index.addDocuments(docs);

        index.commit();

        newDocs();

        return this;
    }

    protected CompilationUnit getSampleCompilationUnit() throws Exception {

        final JavaProjectFixture fixture = new JavaProjectFixture(ResourcesPlugin.getWorkspace(), "testProject");

        final Pair<ICompilationUnit, Set<Integer>> struct = fixture.createFileAndParseWithMarkers("public class "
                + TestClassName + " {}");

        final CompilationUnit cu = CompilationUnitHelper.parse(struct.getFirst());

        return cu;
    }

    protected ICompilationUnit getSampleICompilationUnit() throws Exception {

        final JavaProjectFixture fixture = new JavaProjectFixture(ResourcesPlugin.getWorkspace(), "testProject");

        final Pair<ICompilationUnit, Set<Integer>> struct = fixture.createFileAndParseWithMarkers("public class "
                + TestClassName + " {}");

        final ICompilationUnit cu = struct.getFirst();

        return cu;
    }
}
