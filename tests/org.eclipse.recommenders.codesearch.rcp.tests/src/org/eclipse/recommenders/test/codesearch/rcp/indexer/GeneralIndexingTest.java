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

import junit.framework.Assert;

import org.apache.lucene.index.Term;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ResourcePathIndexer;
import org.eclipse.recommenders.test.codesearch.AbstractTestIndex;
import org.junit.Test;

public class GeneralIndexingTest extends AbstractTestIndex {

    @Test
    public void testDeleteByTerm() throws Exception {

        final CompilationUnit cu = getSampleCompilationUnit();

        newIndex();
        final CodeIndexer index = getIndexer();

        index.index(cu, new ResourcePathIndexer());
        index.commit();

        Assert.assertEquals(1, getSearchIndexer().getDocuments().size());
        final Term term = new Term(Fields.RESOURCE_PATH, ResourcePathIndexer.getPath(cu));
        index.delete(term);
        index.commit();
        Assert.assertEquals(0, getSearchIndexer().getDocuments().size());
    }
}
