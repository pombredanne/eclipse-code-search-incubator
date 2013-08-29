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

package org.eclipse.recommenders.codesearch.rcp.index.indexer.utils;

import java.io.File;

import com.google.common.base.Optional;

public interface IIndexInformationProvider {
    Optional<Long> getLastIndexed(final File location);

    void setLastIndexed(final File location, final Long lastIndexed);
}
