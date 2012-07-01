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

package org.eclipse.recommenders.codesearch.rcp.index.indexer.visitor;

import java.util.List;

import org.apache.lucene.document.Document;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IVarUsageIndexer;

import com.google.common.collect.Lists;

public class VarUsageVisitor {

    private List<IIndexer> indexer;
    private MethodDeclaration method;
    private final List<Document> documents = Lists.newArrayList();

    public VarUsageVisitor(List<IIndexer> indexer) {
        this.indexer = indexer;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void visit(MethodDeclaration method) {
        this.method = method;

        method.accept(visitor);
    }

    private ASTVisitor visitor = new ASTVisitor() {

        public boolean visit(SingleVariableDeclaration node) {
            process(method, node.getName());

            return true;
        };

        public boolean visit(VariableDeclarationFragment node) {
            process(method, node.getName());

            return true;
        };
    };

    private void process(MethodDeclaration method, SimpleName name) {

        final Document varUsageDocument = new Document();

        for (IIndexer i : indexer) {
            if (i instanceof IVarUsageIndexer) {
                ((IVarUsageIndexer) i).indexVarUsage(varUsageDocument, method, name);
            }
        }

        documents.add(varUsageDocument);
    }
}
