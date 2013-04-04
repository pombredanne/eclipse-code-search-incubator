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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

public class LowerCaseKeywordAnalyzer extends Analyzer {

    private Version luceneVersion = null;

    public LowerCaseKeywordAnalyzer(Version luceneVersion) {
        this.luceneVersion = luceneVersion;
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream tokenStream = new KeywordTokenizer(reader);
        tokenStream = new LowerCaseFilter(luceneVersion, tokenStream);
        return tokenStream;
    }
}
