/**
 * Copyright (c) 2012 Tobias Boehm.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Marcel Bruch - initial API and implementation.
 */

package org.eclipse.recommenders.codesearch.rcp.index.apidoc;

import org.apache.lucene.document.Document;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class Selection {

    public final MethodDeclaration method;
    public final IJavaElement handle;
    public final String varname;
    public final Document doc;
    public final Exception exception;

    public Selection(final IJavaElement handle, final MethodDeclaration method, final String varname, final Document doc) {
        this(handle, method, varname, doc, null);
    }

    public Selection(final Exception e) {
        this(null, null, null, null, e);
    }

    protected Selection(final IJavaElement handle, final MethodDeclaration method, final String varname, final Document doc, final Exception e) {
        this.handle = handle;
        this.method = method;
        this.varname = varname;
        this.doc = doc;
        this.exception = e;
    }

    public boolean isError() {
        return exception != null;
    }

    IMethod element() {
        return (IMethod) method.resolveBinding().getJavaElement();
    }    
    
}
