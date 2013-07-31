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

package org.eclipse.recommenders.codesearch.rcp.index.indexer;

import org.apache.lucene.document.Document;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;
import org.eclipse.recommenders.utils.names.ITypeName;
import org.eclipse.recommenders.rcp.utils.AstBindings;

import com.google.common.base.Optional;

public class AllImplementedInterfacesIndexer extends ImplementedInterfacesIndexer implements IClassIndexer {

    @Override
    public void indexType(final Document document, final TypeDeclaration type) {

        final ITypeBinding b = type.resolveBinding();
        if (b == null) {
            return;
        }
        for (final ITypeBinding interface_ : b.getInterfaces()) {

            ITypeBinding superclass = interface_;

            for (; superclass != null;) {
                final Optional<ITypeName> opt = AstBindings.toTypeName(superclass);

                if (opt.isPresent()) {
                	CodeIndexer.addFieldToDocument(document, Fields.ALL_IMPLEMENTED_TYPES, opt.get().getIdentifier());
                }

                superclass = superclass.getInterfaces().length > 0 ? superclass.getInterfaces()[0] : null;
            }
        }
    }
}
