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

public class CodeIndexerConfigBean {
	final private boolean deleteDocumentFirst;
	final private float documentBoost;
	
	public CodeIndexerConfigBean(boolean deleteDocumentFirst, float documentBoost) {
		this.deleteDocumentFirst = deleteDocumentFirst;
		this.documentBoost = documentBoost;
	}
	
	public boolean isDeleteDocumentFirst() {
		return deleteDocumentFirst;
	}

	public float getDocumentBoost() {
		return documentBoost;
	}
}
