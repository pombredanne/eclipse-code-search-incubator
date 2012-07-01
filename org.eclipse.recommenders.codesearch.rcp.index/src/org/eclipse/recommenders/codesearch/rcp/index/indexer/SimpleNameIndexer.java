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

import java.util.List;

import org.apache.lucene.document.Document;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IFieldIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IMethodIndexer;

public class SimpleNameIndexer implements IMethodIndexer, IClassIndexer, IFieldIndexer {

    @Override
    public void indexMethod(final Document document, final MethodDeclaration method) {
    	CodeIndexer.addFieldToDocument(document, Fields.SIMPLE_NAME, method.getName().getIdentifier());
    }

    @Override
    public void indexType(final Document document, final TypeDeclaration type) {
    	CodeIndexer.addFieldToDocument(document, Fields.SIMPLE_NAME, type.getName().getIdentifier());
    }

    @Override
    public void indexField(final Document document, final FieldDeclaration field) {
        for (final VariableDeclarationFragment f : (List<VariableDeclarationFragment>) field.fragments()) {
            final SimpleName name = f.getName();
            CodeIndexer.addFieldToDocument(document, Fields.SIMPLE_NAME, name.getIdentifier());
        }

    }
}
