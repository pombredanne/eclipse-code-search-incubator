package org.eclipse.recommenders.test.codesearch.rcp.searcher;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DocumentTypeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.SimpleNameIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.test.codesearch.SearchTestBase;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.junit.Test;

@SuppressWarnings("all")
public class SearchTest extends SearchTestBase {
  @Test
  public void testSimpleSearch() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    SimpleNameIndexer _simpleNameIndexer = new SimpleNameIndexer();
    ArrayList<IIndexer> _newArrayList = CollectionLiterals.<IIndexer>newArrayList(_documentTypeIndexer, _simpleNameIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(Fields.TYPE, "");
    _builder_1.append(":");
    _builder_1.append(Fields.TYPE_CLASS, "");
    _builder_1.append(" AND ");
    _builder_1.append(Fields.SIMPLE_NAME, "");
    _builder_1.append(":MyClass");
    CharSequence query = _builder_1;
    this.assertQueryResultGtZero(query);
  }
  
  @Test
  public void testSimpleSearchLowerCase() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    SimpleNameIndexer _simpleNameIndexer = new SimpleNameIndexer();
    ArrayList<IIndexer> _newArrayList = CollectionLiterals.<IIndexer>newArrayList(_documentTypeIndexer, _simpleNameIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(Fields.TYPE, "");
    _builder_1.append(":");
    _builder_1.append(Fields.TYPE_CLASS, "");
    _builder_1.append(" AND ");
    _builder_1.append(Fields.SIMPLE_NAME, "");
    _builder_1.append(":myclass");
    CharSequence query = _builder_1;
    this.assertQueryResultGtZero(query);
  }
}
