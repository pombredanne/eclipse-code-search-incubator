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

public class DotNotationTypeConverter implements IQueryPartConverter {

    @Override
    public String convertFrom(final String queryPart) {
        String tmp = queryPart;

        // If user entered a non-qualified type (e.g. IWorkbench) then prepend a wildcard operator automatically
        if(!tmp.startsWith("*")) {
	        if( !(tmp.contains(".") || tmp.contains("/")) && !tmp.startsWith("L*")) {
	        	tmp = "*/" + tmp;
	        }
        }
        
        if (!tmp.startsWith("L")) {
            tmp = "L" + tmp;
        }

        tmp = tmp.replace(".", "/");

        return tmp;
    }

    @Override
    public String convertTo(final String queryPart) {
        String tmp = queryPart;

        if (tmp.startsWith("L")) {
            tmp = tmp.substring(1);
        }
        
        if(tmp.startsWith("*")) {
            tmp = tmp.substring(1);
        }

        tmp = tmp.replace("/", ".");

        return tmp;
    }

}
