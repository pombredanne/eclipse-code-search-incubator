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
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;

import com.google.common.base.Optional;

public class DeclaredMethodsIndexer implements IClassIndexer {

    @Override
    public void indexType(final Document document, final TypeDeclaration type) {
        final ASTVisitor visitor = new ASTVisitor() {
            @Override
            public boolean visit(final MethodDeclaration node) {
                final Optional<String> opt = BindingHelper.getIdentifier(node);
                if (opt.isPresent()) {
                	CodeIndexer.addFieldToDocument(document, Fields.DECLARED_METHODS, opt.get());
                }
                return false;
            }
        };

        type.accept(visitor);
    }
}
