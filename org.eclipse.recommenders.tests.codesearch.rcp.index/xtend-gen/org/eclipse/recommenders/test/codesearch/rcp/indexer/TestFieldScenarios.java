package org.eclipse.recommenders.test.codesearch.rcp.indexer;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DocumentTypeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.FieldTypeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IFieldIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.test.codesearch.rcp.indexer.TestBase;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.junit.Test;

@SuppressWarnings("all")
public class TestFieldScenarios extends TestBase {
  @Test
  public void testFieldTypeIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("List x;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    FieldTypeIndexer _fieldTypeIndexer = new FieldTypeIndexer();
    ArrayList<IFieldIndexer> _newArrayList = CollectionLiterals.<IFieldIndexer>newArrayList(_documentTypeIndexer, _fieldTypeIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_FIELD);
    String _s_1 = this.s(Fields.FIELD_TYPE, "Ljava/util/List");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
}
