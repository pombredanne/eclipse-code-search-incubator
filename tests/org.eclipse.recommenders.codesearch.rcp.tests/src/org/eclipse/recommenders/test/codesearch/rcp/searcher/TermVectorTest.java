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
import java.util.Set;

import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.test.codesearch.AbstractTestIndex;
import org.junit.Assert;
import org.junit.Test;

public class TermVectorTest extends AbstractTestIndex {

    @Test
    public void testSimpleTermVector() throws IOException {
        newIndex().newDoc().addField(Fields.USED_TYPES, "testTermVector01").finish();

        final Set<String> vec = getSearchIndexer().getTermVector(new String[] { Fields.USED_TYPES });

        Assert.assertEquals(1, vec.size());
        Assert.assertTrue(vec.contains("testtermvector01"));
    }

}
