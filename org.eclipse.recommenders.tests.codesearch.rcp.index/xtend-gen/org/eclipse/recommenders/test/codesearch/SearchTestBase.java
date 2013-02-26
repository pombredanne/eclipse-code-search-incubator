package org.eclipse.recommenders.test.codesearch;

import org.eclipse.recommenders.codesearch.rcp.index.searcher.SearchResult;
import org.eclipse.recommenders.test.codesearch.rcp.indexer.TestBase;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Assert;

@SuppressWarnings("all")
public class SearchTestBase extends TestBase {
  public void assertQueryResultGtZero(final CharSequence query) {
    try {
      String _string = query.toString();
      SearchResult result = this.search.lenientSearch(_string);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Query [");
      _builder.append(query, "");
      _builder.append("] yielded no results");
      String _string_1 = _builder.toString();
      boolean _greaterThan = (result.docs.totalHits > 0);
      Assert.assertTrue(_string_1, _greaterThan);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
