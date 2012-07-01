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

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public abstract class MethodCallVisitor extends ASTVisitor {
    @Override
    public boolean visit(final MethodInvocation node) {
        final IMethodBinding b = node.resolveMethodBinding();
        handleMethodCall(b);
        return true;
    }

    @Override
    public boolean visit(final ClassInstanceCreation node) {
        final IMethodBinding b = node.resolveConstructorBinding();
        handleMethodCall(b);
        return true;
    }

    @Override
    public boolean visit(final SuperConstructorInvocation node) {
        final IMethodBinding b = node.resolveConstructorBinding();
        handleMethodCall(b);
        return true;
    }

    @Override
    public boolean visit(final ConstructorInvocation node) {
        final IMethodBinding b = node.resolveConstructorBinding();
        handleMethodCall(b);
        return true;
    }

    @Override
    public boolean visit(final SuperMethodInvocation node) {
        final IMethodBinding b = node.resolveMethodBinding();
        handleMethodCall(b);
        return true;
    }

    protected void handleMethodCall(final IMethodBinding methodBinding) {
        // Let callers overwrite and handle methodBinding
    }
}
