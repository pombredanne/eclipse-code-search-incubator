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

package org.eclipse.recommenders.codesearch.rcp.index.indexer.analyzer;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.tokenizers.CamelCaseTokenizer;
import org.eclipse.recommenders.codesearch.rcp.index.tokenizers.DotSplitTokenizer;

public class JavaSourceCodeAnalyzer extends Analyzer {

    private Set<Object> javaSourceCodeStopSet;

    public JavaSourceCodeAnalyzer() {
        super();

        javaSourceCodeStopSet = StopFilter.makeStopSet(Version.LUCENE_35, new String[] { Fields.MODIFIER_ABSTRACT,
                Fields.MODIFIER_FINAL, Fields.MODIFIER_PRIVATE, Fields.MODIFIER_PROTECTED, Fields.MODIFIER_PUBLIC,
                Fields.MODIFIER_STATIC, "interface", "implements", "extends", "null", "new", "switch", "case",
                "default", "synchronized", "do", "if", "else", "break", "continue", "this", "assert", "for",
                "instanceof", "transient", "void", "finally", "catch", "return", "throws", "throw", "class", "while",
                "import", "package", "true", "false" });
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        final Version version = Version.LUCENE_35;

        TokenStream resultTokenStream = new StandardTokenizer(version, reader);
        resultTokenStream = new StopFilter(version, resultTokenStream, javaSourceCodeStopSet);
//        resultTokenStream = new WordSplitTokenizer(resultTokenStream);
        resultTokenStream = new DotSplitTokenizer(resultTokenStream);
        resultTokenStream = new CamelCaseTokenizer(resultTokenStream);
        resultTokenStream = new LowerCaseFilter(version, resultTokenStream);

        return resultTokenStream;
    }
}
