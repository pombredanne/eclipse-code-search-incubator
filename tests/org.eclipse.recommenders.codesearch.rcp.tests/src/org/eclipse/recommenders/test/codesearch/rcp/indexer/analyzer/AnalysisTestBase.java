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
package org.eclipse.recommenders.test.codesearch.rcp.indexer.analyzer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.analyzer.JavaSourceCodeAnalyzer;

import com.google.common.collect.Lists;

public abstract class AnalysisTestBase {

    protected void testAnalyzer(Analyzer a, String text, String[] expected) {
        List<String> result = parseKeywords(a, null, text);

        for (String s : expected) {
            assertTrue("Result must contain [" + s + "]", result.contains(s));
        }
    }

    protected void testAnalyzer(String text, String[] expected) {
        testAnalyzer(new JavaSourceCodeAnalyzer(), text, expected);
    }

    private List<String> parseKeywords(Analyzer analyzer, String field, String keywords) {

        List<String> result = Lists.newArrayList();
        TokenStream stream = analyzer.tokenStream(field, new StringReader(keywords));

        try {
            while (stream.incrementToken()) {
                result.add(stream.getAttribute(TermAttribute.class).term());
            }
            stream.close();
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
        }

        return result;
    }
}
