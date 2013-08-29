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
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IMethodIndexer;

import com.google.common.base.Optional;

/**
 * Indexes a different view on method parameters.
 * 
 * Example: public void testReturnNames(List l, String s) { }
 * 
 * is indexed as Ljava/util/List;Ljava/lang/String;
 * 
 */
public class ParameterTypesStructuralIndexer implements IMethodIndexer {

    @Override
    public void indexMethod(Document document, MethodDeclaration method) {

        String s = "";

        for (final SingleVariableDeclaration var : (List<SingleVariableDeclaration>) method.parameters()) {
            final Optional<String> opt = BindingHelper.getIdentifier(var.getType());
            if (opt.isPresent()) {
                s += opt.get() + ";";
            }
        }

        if (!s.equals(""))
            CodeIndexer.addFieldToDocument(document, Fields.PARAMETER_TYPES_STRUCTURAL, s);
    }

}
