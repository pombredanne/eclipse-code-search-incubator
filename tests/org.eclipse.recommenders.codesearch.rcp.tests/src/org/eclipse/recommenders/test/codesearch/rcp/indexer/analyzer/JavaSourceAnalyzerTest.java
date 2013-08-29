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

import org.junit.Test;

public class JavaSourceAnalyzerTest extends AnalysisTestBase {
    @Test
    public void testLowerCase() {
        testAnalyzer("StringReader", new String[] { "stringreader" });
    }

    @Test
    public void testMethodName() {
        testAnalyzer("public String toString() { return \"\";}", new String[] { "to", "string" });
    }

    @Test
    public void testPackageNames() {
        testAnalyzer("java.io.StringReader", new String[] { "java", "io", "stringreader" });
    }

    @Test
    public void testCamelCase() {
        testAnalyzer("someCamelCaseString", new String[] { "some", "camel", "case", "string" });
    }

    @Test
    public void testCamelCase02() {
        testAnalyzer("SomeCamelCaseString", new String[] { "some", "camel", "case", "string" });
    }
}
