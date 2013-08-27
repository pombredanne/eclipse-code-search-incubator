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

package org.eclipse.recommenders.codesearch.rcp.index.termvector;

import org.eclipse.recommenders.codesearch.rcp.index.Fields;

public class JavaMethodProvider extends AbstractTermVectorProvider {

    @Override
    protected String[] getFields() {
        return new String[] { Fields.USED_METHODS, Fields.USED_METHODS_IN_FINALLY, Fields.USED_METHODS_IN_TRY,
                Fields.OVERRIDDEN_METHODS, Fields.USED_AS_TAGET_FOR_METHODS, Fields.USED_AS_PARAMETER_IN_METHODS,
                Fields.DECLARED_METHODS
        // ...
        };
    }
}
