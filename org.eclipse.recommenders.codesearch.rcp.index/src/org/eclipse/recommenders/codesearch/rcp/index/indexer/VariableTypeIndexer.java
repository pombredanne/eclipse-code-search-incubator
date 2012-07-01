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

package org.eclipse.recommenders.codesearch.rcp.index.indexer;

import org.apache.lucene.document.Document;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IVarUsageIndexer;

import com.google.common.base.Optional;

public class VariableTypeIndexer implements IVarUsageIndexer {

    @Override
    public void indexVarUsage(final Document document, final MethodDeclaration method, final SimpleName name) {
        final ITypeBinding type = name.resolveTypeBinding();
        final Optional<String> opt = BindingHelper.getIdentifier(type);
        if (opt.isPresent()) {
        	CodeIndexer.addFieldToDocument(document, Fields.VARIABLE_TYPE, (opt.get()));
        }
    }
}
