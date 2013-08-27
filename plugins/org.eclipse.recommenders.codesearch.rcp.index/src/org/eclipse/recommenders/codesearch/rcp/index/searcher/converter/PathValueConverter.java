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

public class PathValueConverter implements IQueryPartConverter {

    @Override
    public String convertFrom(final String queryPart) {
        String result = queryPart;

        // result = result.replaceFirst("/", ":");
        // result = result.replace(":", ":\\");
        // result = result.replace("/", "\\");
        result = result.replace(":", "\\:");

        return result;
    }

    @Override
    public String convertTo(final String queryPart) {
        String result = queryPart;

        // result = result.replace("\\", "/");
        // result = result.replace(":", "\\:");
        result = result.replace("\\:", ":");

        return result;
    }

}
