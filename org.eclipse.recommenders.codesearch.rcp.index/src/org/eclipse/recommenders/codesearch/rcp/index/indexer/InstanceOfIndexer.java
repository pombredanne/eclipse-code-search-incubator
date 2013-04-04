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
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IMethodIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.ITryCatchBlockIndexer;

import com.google.common.base.Optional;

public class InstanceOfIndexer implements IMethodIndexer, ITryCatchBlockIndexer, IClassIndexer {

    @Override
    public void indexType(final Document document, final TypeDeclaration type) {
        final InstanceOfVisitor visitor = new InstanceOfVisitor(document);
        type.accept(visitor);
    }

    @Override
    public void indexTryCatchBlock(final Document document, final TryStatement tryStatement, final CatchClause catchClause) {
        final InstanceOfVisitor visitor = new InstanceOfVisitor(document);
        catchClause.accept(visitor);
    }

    @Override
    public void indexMethod(final Document document, final MethodDeclaration method) {
        final InstanceOfVisitor visitor = new InstanceOfVisitor(document);
        method.accept(visitor);
    }

    private class InstanceOfVisitor extends ASTVisitor {
        private final Document document;

        public InstanceOfVisitor(final Document document) {
            this.document = document;
        }

        @Override
        public boolean visit(final InstanceofExpression node) {

            final Optional<String> opt = BindingHelper.getIdentifier(node.getRightOperand());
            if (opt.isPresent()) {
            	CodeIndexer.addFieldToDocument(document, Fields.INSTANCEOF_TYPES, opt.get());
            }

            return false;
        }
    };
}
