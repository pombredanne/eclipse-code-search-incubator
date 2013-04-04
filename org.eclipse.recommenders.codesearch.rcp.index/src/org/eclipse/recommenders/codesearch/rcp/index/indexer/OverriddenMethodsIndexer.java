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
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.corext.dom.Bindings;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;

import com.google.common.base.Optional;

@SuppressWarnings("restriction")
public class OverriddenMethodsIndexer implements IClassIndexer {
    @Override
    public void indexType(final Document document, final TypeDeclaration type) {
        final ASTVisitor visitor = new ASTVisitor() {
            @Override
            public boolean visit(final MethodDeclaration node) {
                final IMethodBinding b = node.resolveBinding();
                setOverriddenMethodName(document, b);
                return false;
            }
        };

        type.accept(visitor);
    }

    private void setOverriddenMethodName(final Document document, final IMethodBinding b) {
        if (b == null) {
            return;
        }
        final IMethodBinding overriddenBinding = Bindings.findOverriddenMethod(b, true);
        final Optional<String> opt = BindingHelper.getIdentifier(overriddenBinding);
        if (opt.isPresent()) {
        	CodeIndexer.addFieldToDocument(document, Fields.OVERRIDDEN_METHODS, opt.get());
        }
    }
}
