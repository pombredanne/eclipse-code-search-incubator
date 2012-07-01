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

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ResourcePathIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.TimestampIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.utils.CompilationUnitHelper;
import org.eclipse.recommenders.test.codesearch.AbstractTestIndex;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class LastIndexedTimestampTest extends AbstractTestIndex {

    @Test
    public void testLastUpdatedGtZero() throws Exception {

        final ICompilationUnit icu = getSampleICompilationUnit();

        final IPath p = Path.fromPortableString(ResourcePathIndexer.getPath(CompilationUnitHelper.parse(icu)));
        final File location = p.toFile();

        newIndex();
        final CodeIndexer index = getIndexer();

        final List<IIndexer> indexer = Lists.newArrayList();
        indexer.add(new ResourcePathIndexer());
        indexer.add(new TimestampIndexer());

        final CompilationUnit cu = CompilationUnitHelper.parse(icu);

        TimestampIndexer.updateCurrentTimestamp();

        index.index(cu, indexer);
        index.commit();

        final long lastUpdated = index.lastIndexed(location);

        Assert.assertTrue(lastUpdated > 0);
    }
}
