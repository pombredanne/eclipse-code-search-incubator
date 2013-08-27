/**
 * Copyright (c) 2012 Tobias Boehm.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Tobias Boehm - initial API and implementation.
 *    Kavith Thiranga - Refactorings to support new Recommenders API
 */

package org.eclipse.recommenders.codesearch.rcp.index.indexer.visitor;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.document.Document;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexerConfigBean;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexerDefaultConfigBean;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IFieldIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IMethodIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.ITryCatchBlockIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.wiring.CodesearchIndexPlugin;
import org.eclipse.recommenders.rcp.utils.LoggingUtils;

import com.google.common.collect.Lists;

public class CompilationUnitVisitor extends ASTVisitor {

    private CodeIndexer index = null;
    private final List<IIndexer> indexer;
    private CodeIndexerConfigBean settings;

    public void addIndexer(final IIndexer indexer) {
        this.indexer.add(indexer);
    }

    public void addIndexer(final Collection<IIndexer> indexer) {
        this.indexer.addAll(indexer);
    }

    public CompilationUnitVisitor(final CodeIndexer index) {
        this(index, new CodeIndexerDefaultConfigBean());
    }
    
    public CompilationUnitVisitor(final CodeIndexer index, final CodeIndexerConfigBean settings) {
        this.index = index;
        this.indexer = Lists.newArrayList();
        this.settings = settings;
    }

    @Override
    public boolean visit(final TypeDeclaration node) {
        final Document document = new Document();

        for (final IIndexer i : indexer) {
            if (i instanceof IClassIndexer) {
                ((IClassIndexer) i).indexType(document, node);
            }
        }

        addDocument(document);
        return true;
    }

    @Override
    public boolean visit(final MethodDeclaration node) {
        final Document methodDocument = new Document();

        for (final IIndexer i : indexer) {
            if (i instanceof IMethodIndexer) {
                ((IMethodIndexer) i).indexMethod(methodDocument, node);
            }
        }

        if (methodDocument.getFields().size() > 0) {
            addDocument(methodDocument);
        }

        // For each method declaration we use another visitor to learn about
        // variable usage

        final VarUsageVisitor varUsageVisitor = new VarUsageVisitor(indexer);
        varUsageVisitor.visit(node);

        for (final Document d : varUsageVisitor.getDocuments()) {
            if (d.getFields().size() > 0) {
                addDocument(d);
            }
        }

        return true;
    }

    @Override
    public boolean visit(final FieldDeclaration node) {
        final Document document = new Document();

        for (final IIndexer i : indexer) {
            if (i instanceof IFieldIndexer) {
                ((IFieldIndexer) i).indexField(document, node);
            }
        }

        if (document.getFields().size() > 0) {
            addDocument(document);
        }
        return false;
    }

    @Override
    public boolean visit(final TryStatement node) {
        final Document document = new Document();

        final TryStatement tryStatement = node;

        @SuppressWarnings("unchecked")
        final List<CatchClause> catchClauses = tryStatement.catchClauses();

        for (final CatchClause catchClause : catchClauses) {
            for (final IIndexer i : indexer) {
                if (i instanceof ITryCatchBlockIndexer) {
                    ((ITryCatchBlockIndexer) i).indexTryCatchBlock(document, tryStatement, catchClause);
                }
            }
        }

        if (document.getFields().size() > 0) {
            addDocument(document);
        }

        return false;
    }

    private void addDocument(final Document document) {

        try {
            document.setBoost(settings.getDocumentBoost());
            index.addDocument(document);
        } catch (final IOException e) {
            LoggingUtils.logError(e, CodesearchIndexPlugin.getDefault(), "Exception during document save.");
        }
    }
}
