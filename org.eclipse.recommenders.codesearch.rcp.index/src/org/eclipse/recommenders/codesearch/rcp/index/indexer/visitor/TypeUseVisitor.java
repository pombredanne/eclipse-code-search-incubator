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
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.AstHelper;
import org.eclipse.recommenders.utils.names.ITypeName;
import org.eclipse.recommenders.utils.rcp.ast.BindingUtils;

public abstract class TypeUseVisitor extends ASTVisitor {

    @Override
    public boolean visit(final SimpleType node) {
    	try {
        final ITypeBinding b = node.resolveBinding();
        handleTypeUseInternal(b);
    	} catch(NullPointerException ex) {
    		//Ignore, might be thrown in DefaultBindingResolver:1542
    	}
    	
        return false;
    }

    @Override
    public boolean visit(final QualifiedType node) {
        final ITypeBinding b = node.resolveBinding();
        handleTypeUseInternal(b);

        return false;
    }

    @Override
    public boolean visit(final SimpleName node) {
        final IBinding b = node.resolveBinding();
        if (b instanceof ITypeBinding) {
            handleTypeUseInternal((ITypeBinding) b);
        } else if (b instanceof IVariableBinding) {
            final IVariableBinding var = (IVariableBinding) b;
            handleTypeUseInternal(var.getType());
        }

        return false;
    }

    @Override
    public boolean visit(final FieldDeclaration node) {
        final ITypeBinding typeBinding = node.getType().resolveBinding();
        handleTypeUseInternal(typeBinding);
        return false;
    }

    @Override
    public boolean visit(final MethodInvocation node) {
        return false;
    }

    @Override
    public boolean visit(final ClassInstanceCreation node) {
        return false;
    }

    @Override
    public boolean visit(final SuperConstructorInvocation node) {
        return false;
    }

    @Override
    public boolean visit(final ConstructorInvocation node) {
        return false;
    }

    @Override
    public boolean visit(final SuperMethodInvocation node) {
        return false;
    }

    @Override
    public boolean visit(final TryStatement node) {
        return true;
    };

    private void handleTypeUseInternal(final ITypeBinding typeBinding) {
        final ITypeName typeName = BindingUtils.toTypeName(typeBinding).orNull();
        if (!AstHelper.isPrimitiveOrArrayOrNullOrObjectOrString(typeName)) {
            handleTypeUse(typeBinding);
        }
    }

    protected void handleTypeUse(final ITypeBinding typeBinding) {
        // Let callers override and handle typeBinding
    }
}
