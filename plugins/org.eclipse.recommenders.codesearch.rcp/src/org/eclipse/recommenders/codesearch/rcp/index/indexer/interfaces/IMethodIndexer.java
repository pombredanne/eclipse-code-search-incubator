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

package org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces;

import org.apache.lucene.document.Document;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public interface IMethodIndexer extends IIndexer {
    void indexMethod(final Document document, final MethodDeclaration method);
}
