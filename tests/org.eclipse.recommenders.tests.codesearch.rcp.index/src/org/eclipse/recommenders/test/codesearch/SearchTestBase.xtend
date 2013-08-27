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
 
package org.eclipse.recommenders.test.codesearch

import org.eclipse.recommenders.test.codesearch.rcp.indexer.TestBase
import org.junit.Assert

class SearchTestBase extends TestBase {
	
	def void assertQueryResultGtZero(CharSequence query) {
				
		var result = search.lenientSearch(query.toString)
		Assert::assertTrue('''Query [«query»] yielded no results'''.toString, result.docs.totalHits > 0)
	}
}