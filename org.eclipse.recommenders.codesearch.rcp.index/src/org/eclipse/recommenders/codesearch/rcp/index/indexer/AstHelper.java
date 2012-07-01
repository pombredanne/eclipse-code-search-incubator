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

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.utils.names.ITypeName;
import org.eclipse.recommenders.utils.names.VmTypeName;

import com.google.common.base.Optional;

public class AstHelper {
    public static boolean isPrimitiveOrArrayOrNullOrObjectOrString(final ITypeName type) {
        return isNullOrArrayType(type) || type.isPrimitiveType() || type == VmTypeName.OBJECT
                || type == VmTypeName.STRING;
    }

    public static boolean isNullOrArrayType(final ITypeName type) {
        return type == null || type.isArrayType();
    }

    // protected void debugOut(String msg, String origin) {
    // // Activator.logInfo("%1$s [%2$s]: %3$s (%4$s)", getDocumentType(),
    // getName(), msg, origin);
    // }

    public static IProject getProject(ASTNode node) {
        while (node != null && !(node instanceof CompilationUnit)) {
            node = node.getParent();
        }
        final ITypeRoot root = ((CompilationUnit) node).getTypeRoot();
        if (root == null) {
            final IJavaProject property = (IJavaProject) node.getProperty("project");
            return property.getProject();
        }
        return root.getJavaProject().getProject();
    }

    public static Optional<TypeDeclaration> getDeclaringType(ASTNode node) {
        for (; node != null; node = node.getParent()) {
            if (node instanceof TypeDeclaration) {
                return of((TypeDeclaration) node);
            }
        }

        return absent();
    }

    public static Optional<MethodDeclaration> getDeclaringMethod(ASTNode node) {
        for (; node != null; node = node.getParent()) {
            if (node instanceof MethodDeclaration) {
                return of((MethodDeclaration) node);
            }
        }
        return absent();
    }

    public static boolean returnsVoid(final MethodDeclaration method) {
        return method != null && isVoid(method.getReturnType2());
    }

    public static boolean isVoid(final Type type) {
        return type != null && type.toString().equals("void");
    }
}
