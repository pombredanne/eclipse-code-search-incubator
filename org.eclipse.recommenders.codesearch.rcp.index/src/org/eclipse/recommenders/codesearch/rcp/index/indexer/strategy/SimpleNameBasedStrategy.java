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

import java.util.Map;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;

import com.google.common.collect.Maps;

/**
 * Simple non dynamic version that uses a hard-coded information set for some
 * predefined fields
 */
public class SimpleNameBasedStrategy implements IFieldIndexingStrategy {

    private Map<String, Store> storeMap = Maps.newHashMap();
    private Map<String, Index> indexMap = Maps.newHashMap();

    private Object helperObj = null;

    public SimpleNameBasedStrategy() {
        // Extend by filling the two maps...

        // We read these fields from source code
        storeMap.put(Fields.TYPE, Store.YES);
        storeMap.put(Fields.TIMESTAMP, Store.YES);
        storeMap.put(Fields.VARIABLE_NAME, Store.YES);
        storeMap.put(Fields.QUALIFIED_NAME, Store.YES);
        storeMap.put(Fields.DECLARING_TYPE, Store.YES);
        storeMap.put(Fields.DECLARING_METHOD, Store.YES);
        storeMap.put(Fields.JAVA_ELEMENT_HANDLE, Store.YES);
        storeMap.put(Fields.PARAMETER_TYPES_STRUCTURAL, Store.YES);
        storeMap.put(Fields.USED_AS_PARAMETER_IN_METHODS, Store.YES);

        // We won't search for these fields
        indexMap.put(Fields.TIMESTAMP, Index.NO);
        indexMap.put(Fields.JAVA_ELEMENT_HANDLE, Index.NO);
    }

    @Override
    public Store getStore(String fieldName) {
        return getValueOrDefault(fieldName, storeMap, Store.NO);
    }

    @Override
    public Index getIndex(String fieldName) {
        return getValueOrDefault(fieldName, indexMap, Index.ANALYZED);
    }

    @SuppressWarnings("unchecked")
    private <T> T getValueOrDefault(String fieldName, Map<String, T> map, T defaultValue) {
        helperObj = map.get(fieldName);

        if (helperObj != null)
            return (T) helperObj;

        return defaultValue;
    }
}
