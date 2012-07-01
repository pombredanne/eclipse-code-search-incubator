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
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;

import com.google.common.base.Optional;

public class ExtendedTypeIndexer implements IClassIndexer {

    @Override
    public void indexType(final Document document, final TypeDeclaration type) {
        final ITypeBinding clazz = type.resolveBinding();
        if (clazz != null) {
            addAnalyzedExtendedTypeField(document, clazz, Fields.EXTENDED_TYPE);
        }
    }

    protected void addAnalyzedExtendedTypeField(final Document document, final ITypeBinding typeBinding,
            final String fieldName) {
        final ITypeBinding superclass = typeBinding.getSuperclass();
        final Optional<String> opt = BindingHelper.getIdentifier(superclass);
        if (opt.isPresent()) {
        	CodeIndexer.addFieldToDocument(document, fieldName, opt.get());
        }
    }

}
