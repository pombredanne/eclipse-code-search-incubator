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

import java.io.IOException;
import java.util.LinkedList;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public abstract class TermSplitTokenizer extends TokenFilter {

    private LinkedList<Token> tokens = new LinkedList<Token>();

    private TermAttribute termAtt;
    private OffsetAttribute offsetAtt;
    private PositionIncrementAttribute posAtt;

    public TermSplitTokenizer(TokenStream in) {
        super(in);

        termAtt = (TermAttribute) addAttribute(TermAttribute.class);
        offsetAtt = (OffsetAttribute) addAttribute(OffsetAttribute.class);
        posAtt = (PositionIncrementAttribute) addAttribute(PositionIncrementAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!tokens.isEmpty()) {
            applyToken(tokens.removeFirst());
        } else if (input.incrementToken()) {
            splitIntoTokens();

            if (!tokens.isEmpty()) {
                if (!returnOriginalTerm())
                    applyToken(tokens.removeFirst());
            }
        } else {
            return false;
        }

        return true;
    }

    private void splitIntoTokens() {
        String term = termAtt.term();
        String[] termParts = splitTerm(term);

        if (termParts.length > 1) {
            int termPos = offsetAtt.startOffset();

            for (int i = 0; i < termParts.length; i++) {
                String termPart = termParts[i];
                int termPartPos = termPos + term.indexOf(termPart);
                int termPartEndPos = termPartPos + termPart.length();

                Token newToken = new Token(termPart, termPartPos, termPartEndPos);
                newToken.setPositionIncrement(0);

                tokens.add(newToken);
            }
        }
    }

    private void applyToken(Token token) {
        termAtt.setTermBuffer(token.termBuffer(), 0, token.termLength());
        posAtt.setPositionIncrement(token.getPositionIncrement());
        offsetAtt.setOffset(token.startOffset(), token.endOffset());
    }

    protected boolean returnOriginalTerm() {
        return false;
    }

    public abstract String[] splitTerm(String term);
}
