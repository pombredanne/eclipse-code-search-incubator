/**
 * Copyright (c) 2012 Tobias Boehm.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Marcel Bruch - initial API and implementation.
 *    Kavith Thiranga - Refactorings to support new Recommenders API
 */

package org.eclipse.recommenders.codesearch.rcp.index.apidoc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.document.Document;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.ui.SharedASTProvider;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.searcher.SearchResult;
import org.eclipse.recommenders.internal.codesearch.rcp.CodesearchIndexPlugin;
import org.eclipse.recommenders.rcp.JavaElementResolver;
import org.eclipse.recommenders.rcp.utils.ASTNodeUtils;
import org.eclipse.recommenders.rcp.utils.Logs;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

final class ContentProvider implements ILazyContentProvider {

    private final ExecutorService s = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime
            .getRuntime().availableProcessors(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(20),

    new ThreadFactoryBuilder().setPriority(Thread.MIN_PRIORITY).build());
    public static MethodDeclaration MDEMPTY;
    static {
        final AST ast = AST.newAST(AST.JLS4);
        MDEMPTY = ast.newMethodDeclaration();
        MDEMPTY.setName(ast.newSimpleName("not_found"));
        MDEMPTY.setBody(ast.newBlock());     
    }

    private TableViewer viewer;
    private final SearchResult searchResults;

    ContentProvider(final SearchResult searchResults, final JavaElementResolver jdtResolver) {
        this.searchResults = searchResults;
    }

    @Override
    public void dispose() {
        s.shutdown();
    }

    @Override
    public void updateElement(final int index) {
        try {
            s.submit(new Runnable() {
                private IMethod jdtMethod;
                private IType jdtType;
                private MethodDeclaration astMethod;
                private IJavaElement element;

                @Override
                public void run() {
                    try {
                        final Document doc = searchResults.scoreDoc(index);
                        if (!findHandle(doc)) {
                            final IllegalStateException e = new IllegalStateException("Could not find handle "
                                    + doc.get(Fields.JAVA_ELEMENT_HANDLE));
                            updateIndex(new Selection(e), index);
                            return;
                        }
                        // There can be a selection without an enclosing method. eg. annotation, extended types, etc.
                        if (!findAstMethod() && !findJdtType()) {
                            updateIndex(new Selection(element, MDEMPTY, "", doc), index);
                            return;
                        }
                        updateIndex(new Selection(element, astMethod, doc.get(Fields.VARIABLE_NAME), doc), index);
                    } catch (final Exception e) {
                        updateIndex(new Selection(e), index);
                    }
                }

                private boolean findHandle(final Document doc) {
                    final String handle = doc.get(Fields.JAVA_ELEMENT_HANDLE);
                    element = JavaCore.create(handle);
                    return element != null;
                }

                private boolean findJdtMethod() {
                    jdtMethod = (IMethod) element.getAncestor(IJavaElement.METHOD);
                    return jdtMethod != null;
                }
                
                private boolean findJdtType(){
                    jdtType = (IType) element.getAncestor(IJavaElement.TYPE);
                    return jdtType != null;
                }
                
                private boolean findAstMethod() {
                    try {
                        if(!findJdtMethod()){
                            return false;
                        }
                        final ITypeRoot cu = jdtMethod.getTypeRoot();
                        if (cu == null) {
                            return false;
                        }
                        final CompilationUnit ast = SharedASTProvider.getAST(cu, SharedASTProvider.WAIT_YES, null);
                        if (ast == null) {
                            return false;
                        }

                        // caused NPEs:
                        // ASTNodeSearchUtil.getMethodDeclarationNode(jdtMethod,
                        // ast);
                        astMethod = ASTNodeUtils.find(ast, jdtMethod).orNull();
                    } catch (final Exception e) {
                        Logs.logError(e, CodesearchIndexPlugin.getDefault(), "failed to find declaring method %s",
                                jdtMethod);
                    }
                    return astMethod != null;
                }
            });
        } catch (final RejectedExecutionException e) {
            updateIndex(new Selection(new RuntimeException(
                    "Too many rendering requests at once. Select this item again to refresh.")), index);
            // the user was just scrolling to fast...
            // to prevent ui freezes we ignore too many requests...
        }
    }

    private void updateIndex(final Selection s, final int index) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final Table table = viewer.getTable();
                if (table.isDisposed()) {
                    return;
                }
                viewer.replace(s, index);
            }
        });
    }

    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        this.viewer = (TableViewer) viewer;
    }
}
