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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.google.common.base.Optional;

public abstract class AbstractFieldsAccessIndexer {
    public enum AssignmentSide {
        RIGHT, LEFT
    }

    protected void addFields(final Document document, final ASTNode node, final String fieldName) {
        addFields(document, node, fieldName, null);
    }

    protected void addFields(final Document document, final ASTNode node, final String fieldName,
            final AssignmentSide side) {
        final ASTVisitor visitor = new ASTVisitor() {
            @Override
            public boolean visit(final QualifiedName node) {
                if (side == null || (parentIsAssignment(node) && currentNodeCorrectSideOfAssignment(node, side))
                        || side.equals(AssignmentSide.RIGHT)) {
                    final SimpleName simpleName = node.getName();

                    final Optional<TypeDeclaration> opt = AstHelper.getDeclaringType(node);
                    if (!opt.isPresent()) {
                        return false;
                    }
                    final Optional<String> optId = BindingHelper.getIdentifier(opt.get());
                    if (!optId.isPresent()) {
                        return false;
                    }
                    final String result = String.format("%1$s.%2$s", optId.get(), simpleName);

                    CodeIndexer.addFieldToDocument(document, fieldName, result);
                }

                return false;
            }
        };

        node.accept(visitor);
    }

    private boolean parentIsAssignment(final ASTNode node) {
        return node.getParent() != null && node.getParent() instanceof Assignment;
    }

    private boolean currentNodeCorrectSideOfAssignment(final ASTNode node, final AssignmentSide side) {
        if (side.equals(AssignmentSide.LEFT)) {
            return ((Assignment) node.getParent()).getLeftHandSide() == node;
        } else if (side.equals(AssignmentSide.RIGHT)) {
            return ((Assignment) node.getParent()).getRightHandSide() == node;
        } else {
            // What "else"?
            return false;
        }
    }
}
