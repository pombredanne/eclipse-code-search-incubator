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

import static org.eclipse.recommenders.utils.Checks.ensureIsNotNull;

import java.io.File;
import java.net.URI;

import org.apache.lucene.document.Document;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IFieldIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IMethodIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.ITryCatchBlockIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IVarUsageIndexer;

import com.google.common.annotations.VisibleForTesting;

public class ResourcePathIndexer implements IClassIndexer, IMethodIndexer,
        ITryCatchBlockIndexer, IFieldIndexer, IVarUsageIndexer {

    public static String getPath(final IJavaElement e) {
        return getPath(getFile(e));
    }

    public static File getFile(final IJavaElement e) {
        final IResource resource = e.getResource();
        final IPath path;
        if (resource == null) {
            path = e.getPath();
        } else {
            path = resource.getRawLocation();
            if (path == null) {
                final URI uri = resource.getLocationURI();
                if (uri != null) {
                    return new File(uri);
                }
            }
        }
        return path.toFile();
    }

    public static String getPath(final CompilationUnit cu) {
        return getPath(getFile(cu));
    }

    public static File getFile(final CompilationUnit cu) {
        final ITypeRoot root = cu.getTypeRoot();
        if (root == null) {
            // this is a special treatment for cus created from source code w/o
            // a IClassFile or ICompilationUnit
            return ensureIsNotNull((File) cu.getProperty("location"));
        }
        return getFile(root);
    }

    public static String getPath(final File file) {
        return file.getAbsolutePath();
    }

    @VisibleForTesting
    public static String escape(final String path) {
        String fileName = path;
        fileName = fileName.replaceAll("\\\\", "\\\\\\\\");
        fileName = fileName.replaceAll(":", "\\\\:");
        fileName = fileName.replaceAll("-", "\\\\-");
        return fileName;
    }

    @Override
    public void indexField(final Document document, final FieldDeclaration field) {
        addField(document, field);
    }

    @Override
    public void indexMethod(final Document document, final MethodDeclaration method) {
        addField(document, method);
    }

    @Override
    public void indexType(final Document document, final TypeDeclaration type) {
        addField(document, type);
    }

    @Override
    public void indexTryCatchBlock(final Document document, final TryStatement tryStatement,
            final CatchClause catchClause) {

        addField(document, tryStatement);
    }

    @Override
    public void indexVarUsage(final Document document, final MethodDeclaration method, final SimpleName name) {
        addField(document, method);
    }

    private void addField(final Document document, final ASTNode node) {
        final CompilationUnit cu = (CompilationUnit) node.getRoot();
        final File f = getFile(cu);
        CodeIndexer.addFieldToDocument(document, Fields.RESOURCE_PATH, f.getAbsolutePath());
    }
}
