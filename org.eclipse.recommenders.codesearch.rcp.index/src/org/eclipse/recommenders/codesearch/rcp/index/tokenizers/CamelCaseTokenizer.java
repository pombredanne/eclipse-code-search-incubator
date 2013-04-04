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

package org.eclipse.recommenders.codesearch.rcp.index.tokenizers;

import org.apache.lucene.analysis.TokenStream;

public class CamelCaseTokenizer extends TermSplitTokenizer {

    public CamelCaseTokenizer(TokenStream in) {
        super(in);
    }

    @Override
    public String[] splitTerm(String term) {

        String newWord = term.replaceAll("([A-Z][a-z])", "_$1").replaceAll("([a-z])([A-Z])", "$1_$2");

        return newWord.split("[_]");
    }

    @Override
    protected boolean returnOriginalTerm() {
        return true;
    }
}
