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
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IFieldIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IMethodIndexer;

public class ModifiersIndexer implements IClassIndexer, IMethodIndexer, IFieldIndexer {

    @Override
    public void indexField(final Document document, final FieldDeclaration field) {
        addFields(document, field.getModifiers());
    }

    @Override
    public void indexMethod(final Document document, final MethodDeclaration method) {
        addFields(document, method.getModifiers());
    }

    @Override
    public void indexType(final Document document, final TypeDeclaration type) {
        addFields(document, type.getModifiers());
    }

    private void addFields(final Document document, final int flags) {
        if (Modifier.isPublic(flags)) {
            addField(document, Fields.MODIFIER_PUBLIC);
        }
        if (Modifier.isFinal(flags)) {
            addField(document, Fields.MODIFIER_FINAL);
        }
        if (Modifier.isPrivate(flags)) {
            addField(document, Fields.MODIFIER_PRIVATE);
        }
        if (Modifier.isProtected(flags)) {
            addField(document, Fields.MODIFIER_PROTECTED);
        }
        if (Modifier.isStatic(flags)) {
            addField(document, Fields.MODIFIER_STATIC);
        }
        if (Modifier.isAbstract(flags)) {
            addField(document, Fields.MODIFIER_ABSTRACT);
        }
    }

    private void addField(final Document document, final String modifier) {
    	CodeIndexer.addFieldToDocument(document, Fields.MODIFIERS, modifier);
    }
}
