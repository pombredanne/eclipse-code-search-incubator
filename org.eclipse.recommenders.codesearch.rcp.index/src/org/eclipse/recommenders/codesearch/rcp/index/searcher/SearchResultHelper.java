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

package org.eclipse.recommenders.codesearch.rcp.index.searcher;

import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SearchResultHelper {
    public static TopDocs getIntersection(List<TopDocs> validScoreDocs, IndexSearcher searcher) {
        if (validScoreDocs.size() == 0)
            return new TopDocs(0, new ScoreDoc[0], 0);

        TopDocs result = validScoreDocs.get(0);

        for (int i = 1; i < validScoreDocs.size(); i++) {
            result = getIntersection(result, validScoreDocs.get(i), searcher);
        }

        return getUniqueDocsByFieldName(result, searcher, Fields.DECLARING_METHOD);
    }

    public static TopDocs getIntersection(TopDocs t1, TopDocs t2, IndexSearcher searcher) {
        List<ScoreDoc> list = Lists.newArrayList();

        for (ScoreDoc d1 : t1.scoreDocs) {
            for (ScoreDoc d2 : t2.scoreDocs) {
                if (areEqual(d1, d2, searcher, Fields.DECLARING_METHOD)) {
                    list.add(d1);
                    break;
                }
            }
        }

        return asTopDocs(list);
    }

    public static TopDocs asTopDocs(List<ScoreDoc> scoreDocs) {
        ScoreDoc[] resultDocs = new ScoreDoc[scoreDocs.size()];
        return new TopDocs(scoreDocs.size(), scoreDocs.toArray(resultDocs), 0);
    }

    public static TopDocs asTopDocs(ScoreDoc[] scoreDocs) {
        return new TopDocs(scoreDocs.length, scoreDocs, 0);
    }

    private static boolean areEqual(ScoreDoc d1, ScoreDoc d2, IndexSearcher searcher, String fieldName) {
        try {
            Document doc1 = searcher.doc(d1.doc);
            Document doc2 = searcher.doc(d2.doc);

            return doc1.get(fieldName) != null && doc2.get(fieldName) != null
                    && doc1.get(fieldName).equals(doc2.get(fieldName));
        } catch (Exception ex) {
            return false;
        }
    }

    private static TopDocs getUniqueDocsByFieldName(TopDocs t1, IndexSearcher searcher, String fieldName) {
        Map<String, ScoreDoc> docs = Maps.newHashMap();

        for (ScoreDoc d1 : t1.scoreDocs) {
            try {
                Document doc1 = searcher.doc(d1.doc);

                // This is a naive way. We ignore scores etc.
                docs.put(doc1.get(fieldName), d1);
            } catch (Exception e) {
                // Nothing to do since doc has no method name anyway
            }
        }

        ScoreDoc[] resultDocs = new ScoreDoc[docs.size()];
        return new TopDocs(docs.size(), docs.values().toArray(resultDocs), 0);
    }
}
