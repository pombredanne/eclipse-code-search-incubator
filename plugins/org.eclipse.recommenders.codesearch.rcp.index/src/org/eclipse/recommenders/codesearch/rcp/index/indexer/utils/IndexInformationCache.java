/**
 * Copyright (c) 2012 Tobias Boehm.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Tobias Boehm - initial API and implementation.
 *    Kavith Thiranga - Refactorings to support new Guava API
 */

package org.eclipse.recommenders.codesearch.rcp.index.indexer.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class IndexInformationCache implements IIndexInformationProvider {

    private final Cache<File, Long> cache = CacheBuilder.newBuilder().maximumSize(3000)
            .expireAfterWrite(1, TimeUnit.MINUTES).build();

    @Override
    public Optional<Long> getLastIndexed(final File location) {
        if (cache.asMap().containsKey(location)) {
            return Optional.of(cache.getIfPresent(location));
        }

        return Optional.absent();
    }

    @Override
    public void setLastIndexed(final File location, final Long lastIndexed) {
        cache.put(location, lastIndexed);
    }
}
