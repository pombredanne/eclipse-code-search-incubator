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
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IMethodIndexer;

import com.google.common.base.Optional;

public class ReturnTypeIndexer implements IMethodIndexer {

    @Override
    public void indexMethod(final Document document, final MethodDeclaration method) {
        Type returnType = method.getReturnType2();

        addReturnsVoidField(document, returnType);
        addReturnsTypeField(document, returnType);
    }

    private void addReturnsVoidField(final Document document, Type returnType) {
        if (AstHelper.isVoid(returnType)) {
        	CodeIndexer.addFieldToDocument(document, Fields.RETURN_TYPE, "Lvoid");
        }
    }

    private void addReturnsTypeField(final Document document, Type returnType) {
        final Optional<String> opt = BindingHelper.getIdentifier(returnType);
        if (opt.isPresent() && !opt.get().equals("V")) {
            // Ignore "V" which would be the case for void return types
        	CodeIndexer.addFieldToDocument(document, Fields.RETURN_TYPE, opt.get());
        }
    }
}
