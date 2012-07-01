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

package org.eclipse.recommenders.test.codesearch.rcp.searcher;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.searcher.CodeSearcher;
import org.eclipse.recommenders.test.codesearch.AbstractTestIndex;
import org.junit.Assert;
import org.junit.Test;

public class CodeSearcherTest extends AbstractTestIndex {

    @Test
    public void testSimpleSearchReturnsSomething() throws IOException, ParseException {
        newIndex().newDoc().addField(Fields.TYPE, Fields.TYPE_CLASS).finish();

        final CodeSearcher sut = getSearchIndexer();

        final List<Document> result = sut.search(String.format("%s:%s", Fields.TYPE, Fields.TYPE_CLASS));

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testGetAllDocuments() throws IOException {
        newIndex().newDoc().newDoc().newDoc().finish();

        final List<Document> docs = getSearchIndexer().getDocuments();

        Assert.assertNotNull(docs);
        Assert.assertEquals(3, docs.size());
    }
}
