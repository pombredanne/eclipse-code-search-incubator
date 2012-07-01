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
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;

public class AllDeclaredFieldNamesIndexer extends DeclaredFieldNamesIndexer implements IClassIndexer {

    @Override
    public void indexType(final Document document, final TypeDeclaration type) {
        ITypeBinding superclass = type.resolveBinding();

        for (; superclass != null; superclass = superclass.getSuperclass()) {
            addFields(document, superclass);
        }
    }

    private void addFields(final Document document, final ITypeBinding type) {
        if (type == null) {
            return;
        }

        for (final IVariableBinding field : type.getDeclaredFields()) {
        	CodeIndexer.addFieldToDocument(document, Fields.ALL_DECLARED_FIELD_NAMES, field.getName());
        }
    }

}
