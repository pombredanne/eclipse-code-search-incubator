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

package org.eclipse.recommenders.codesearch.rcp.index.indexer.utils;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CompilationUnitHelper {

    private static ASTParser parser;

    static {
        parser = ASTParser.newParser(AST.JLS4);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
    }

    public static CompilationUnit parse(final ICompilationUnit unit) {
        parser.setSource(unit);
        return (CompilationUnit) parser.createAST(null); // parse
    }

    public static CompilationUnit parse(final IClassFile type) {
        parser.setSource(type);
        return (CompilationUnit) parser.createAST(null); // parse
    }

    public static CompilationUnit parse(final char[] source, final String unitName, final IJavaProject project) {
        parser = ASTParser.newParser(AST.JLS4);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setProject(project);
        parser.setUnitName(unitName);
        parser.setSource(source);
        return (CompilationUnit) parser.createAST(null); // parse
    }
}
