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
package org.eclipse.recommenders.test.codesearch.rcp.indexer

import org.eclipse.recommenders.codesearch.rcp.index.Fields
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ParameterCountIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ParameterTypesIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ReturnTypeIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ReturnVariableExpressionIndexer
import org.junit.Test
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CheckedExceptionsIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ParameterTypesStructuralIndexer

class TestMethodScenarios extends TestBase {

    @Test
    def void testReturnTypeIndexer() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	public List testReturnSomething() {
            		return null;
            	}
            }
        '''

        exercise(code, new ReturnTypeIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::RETURN_TYPE, "Ljava/util/List")
                )))
    }

    @Test
    def void testReturnTypeVoidIndexer() {
        val code = '''
            public class MyClass {
            	public void testReturnSomething() {
            	}
            }
        '''

        exercise(code, new ReturnTypeIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::RETURN_TYPE, "Lvoid")
                )))

        assertNotField(
            l(
                newArrayList(
                    s(Fields::RETURN_TYPE, "V")
                )))
    }

    @Test
    def void testParameterTypesIndexer() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	public void testSomething(List a) {
            	}
            }
        '''

        exercise(code, new ParameterTypesIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::PARAMETER_TYPES, "Ljava/util/List")
                )))
    }

    @Test
    def void testParameterCountIndexer() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	public void testSomething(List a) {
            	}
            }
        '''

        exercise(code, new ParameterCountIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::PARAMETER_COUNT, "1")
                )))
    }

    @Test
    def void testParameterCountIndexer02() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	public void testSomething(List a, String b) {
            	}
            }
        '''

        exercise(code, new ParameterCountIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::PARAMETER_COUNT, "2")
                )))
    }

    @Test
    def void testReturnVariableExpressionsIndexer() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	public List testReturnNames() {
            		List names = null;
            		return names;
            	}
            }
        '''

        exercise(code, new ReturnVariableExpressionIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::RETURN_VARIABLE_EXPRESSIONS, "names")
                )))
    }

    @Test
    def void testReturnVariableExpressionsIndexer02() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	public List testReturnNames(boolean value) {
            		List names1 = null;
            		List names2 = null;
            		
            		if(value) { return names1; }
            		else { return names2;}
            	}
            }
        '''

        exercise(code, new ReturnVariableExpressionIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::RETURN_VARIABLE_EXPRESSIONS, "names1")
                )))

        assertField(
            l(
                newArrayList(
                    s(Fields::RETURN_VARIABLE_EXPRESSIONS, "names2")
                )))
    }

    @Test
    def void testReturnVariableExpressionsIndexer03() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	public List testReturnNames() {
            		return null;
            	}
            }
        '''

        exercise(code, new ReturnVariableExpressionIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::RETURN_VARIABLE_EXPRESSIONS, "null")
                )))
    }

    @Test
    def void testCheckedExceptionsIndexer01() {
        val code = '''
            import java.io.IOException;
            public class MyClass {
            	public List method() throws IOException {
            	}
            }
        '''

        exercise(code, i(newArrayList(new CheckedExceptionsIndexer())))

        assertField(
            l(
                newArrayList(
                    s(Fields::CHECKED_EXCEPTIONS, "Ljava/io/IOException")
                )))
    }

    @Test
    def void testParameterTypesStructuralIndexer() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	public void testReturnNames(List l, String s) {
            	}
            }
        '''

        exercise(code, new ParameterTypesStructuralIndexer())

        assertField(
            l(
                newArrayList(
                    s(Fields::PARAMETER_TYPES_STRUCTURAL, "Ljava/util/List;Ljava/lang/String;")
                )))
    }
}
