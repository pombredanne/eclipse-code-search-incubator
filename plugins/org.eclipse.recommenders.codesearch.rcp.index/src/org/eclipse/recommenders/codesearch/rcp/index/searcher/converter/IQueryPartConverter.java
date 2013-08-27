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

package org.eclipse.recommenders.codesearch.rcp.index.searcher.converter;

public interface IQueryPartConverter {
    /**
     * Converts the queryPart into the string used in the final query
     * 
     * @param queryPart
     * @return
     */
    String convertFrom(String queryPart);

    /**
     * Converts the queryPart into the string displayed in content assist
     * 
     * @param queryPart
     * @return
     */
    String convertTo(String queryPart);
}
