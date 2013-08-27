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

import java.util.List;
import java.util.Map;

public interface ITermVectorProvider {
    List<String> getDisjunctTermVector();

    void load(final ITermVectorConsumable consumable, final Map<Integer, Object> argumentsMap);

    boolean doneLoading();
}
