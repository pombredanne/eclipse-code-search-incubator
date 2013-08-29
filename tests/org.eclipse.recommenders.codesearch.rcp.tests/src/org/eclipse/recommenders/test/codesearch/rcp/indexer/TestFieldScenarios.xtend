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
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DocumentTypeIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.FieldTypeIndexer
import org.junit.Test

class TestFieldScenarios extends TestBase {

    @Test
    def void testFieldTypeIndexer() {
        val code = '''
            import java.util.List;
            public class MyClass {
            	List x;
            }
        '''

        exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FieldTypeIndexer())))

        assertField(
            l(
                newArrayList(
                    s(Fields::TYPE, Fields::TYPE_FIELD),
                    s(Fields::FIELD_TYPE, "Ljava/util/List")
                )))
    }
}
