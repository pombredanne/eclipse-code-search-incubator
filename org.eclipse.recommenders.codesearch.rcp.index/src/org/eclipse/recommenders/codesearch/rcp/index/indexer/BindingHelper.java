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

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.recommenders.utils.names.IMethodName;
import org.eclipse.recommenders.utils.names.ITypeName;
import org.eclipse.recommenders.rcp.utils.AstBindings;

import com.google.common.base.Optional;

public final class BindingHelper {

    public static Optional<String> getIdentifier(final MethodDeclaration node) {
        final IMethodBinding b = node.resolveBinding();
        return getIdentifier(b);
    }

    public static Optional<String> getIdentifier(final FieldDeclaration node) {
        final ITypeBinding b = node.getType().resolveBinding();
        return getIdentifier(b);
    }

    public static Optional<String> getIdentifier(final Optional<TypeDeclaration> node) {
        return getIdentifier(node.orNull());
    }

    public static Optional<String> getIdentifier(final Type node) {
        if (node == null) {
            return absent();
        }
        final ITypeBinding b = node.resolveBinding();
        return getIdentifier(b);
    }

    public static Optional<String> getIdentifier(final TypeDeclaration node) {
        if (node == null) {
            return absent();
        }
        final ITypeBinding b = node.resolveBinding();
        return getIdentifier(b);
    }

    public static Optional<String> getIdentifier(final VariableDeclarationStatement node) {
    	try {
        final ITypeBinding b = node.getType().resolveBinding();
        	return getIdentifier(b);
    	} catch(NullPointerException ex) {
    		//Ignore, might be thrown in DefaultBindingResolver:1542
    		return absent();
    	}
    }

    public static Optional<String> getIdentifier(final ITypeBinding b) {
        if (b == null) {
            return absent();
        }
        final Optional<ITypeName> opt = AstBindings.toTypeName(b);
        if (!opt.isPresent()) {
            return absent();
        }
        return of(opt.get().getIdentifier());
    }

    public static Optional<String> getTypeIdentifier(final SimpleName n) {
        return getIdentifier(n.resolveTypeBinding());
    }

    public static Optional<String> getIdentifier(final MethodInvocation m) {
        return getIdentifier(m.resolveMethodBinding());
    }

    public static Optional<String> getIdentifier(final IMethodBinding b) {
        if (b == null) {
            return absent();
        }
        final Optional<IMethodName> opt = AstBindings.toMethodName(b);
        if (!opt.isPresent()) {
            return absent();
        }
        return of(opt.get().getIdentifier());
    }
}
