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

public class DotSplitTokenizer extends TermSplitTokenizer {

    public DotSplitTokenizer(TokenStream in) {
        super(in);
    }

    @Override
    public String[] splitTerm(String term) {
        String[] parts = term.split("[.]");
        return parts;
    }

    @Override
    protected boolean returnOriginalTerm()
    {
        return true;
    }
}
