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
 
package org.eclipse.recommenders.test.codesearch.rcp.searcher

import org.eclipse.recommenders.codesearch.rcp.index.indexer.DocumentTypeIndexer
import org.junit.Test
import org.eclipse.recommenders.codesearch.rcp.index.Fields
import org.eclipse.recommenders.codesearch.rcp.index.indexer.SimpleNameIndexer
import org.eclipse.recommenders.test.codesearch.SearchTestBase

class SearchTest extends SearchTestBase {
	    
	@Test
	def void testSimpleSearch(){
		val code = '''
		public class MyClass {
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new SimpleNameIndexer())))
		
		var query = '''«Fields::TYPE»:«Fields::TYPE_CLASS» AND «Fields::SIMPLE_NAME»:MyClass'''

		assertQueryResultGtZero(query)
	}   
	@Test
	def void testSimpleSearchLowerCase(){
		val code = '''
		public class MyClass {
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new SimpleNameIndexer())))
		
		var query = '''«Fields::TYPE»:«Fields::TYPE_CLASS» AND «Fields::SIMPLE_NAME»:myclass'''

		assertQueryResultGtZero(query)
	}
}