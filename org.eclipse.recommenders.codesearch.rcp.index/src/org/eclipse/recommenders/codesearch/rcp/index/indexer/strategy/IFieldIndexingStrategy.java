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

package org.eclipse.recommenders.codesearch.rcp.index.indexer.strategy;

import org.apache.lucene.document.Field;

/**
 * Information interface that indicates whether a certain field should be
 * stores/analyzed
 */
public interface IFieldIndexingStrategy {
    public Field.Store getStore(String fieldName);

    public Field.Index getIndex(String fieldName);
}
