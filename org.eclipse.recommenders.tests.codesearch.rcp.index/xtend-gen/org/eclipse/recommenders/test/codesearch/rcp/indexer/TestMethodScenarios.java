package org.eclipse.recommenders.test.codesearch.rcp.indexer;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CheckedExceptionsIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ParameterCountIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ParameterTypesIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ParameterTypesStructuralIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ReturnTypeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ReturnVariableExpressionIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.test.codesearch.rcp.indexer.TestBase;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.junit.Test;

@SuppressWarnings("all")
public class TestMethodScenarios extends TestBase {
  @Test
  public void testReturnTypeIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public List testReturnSomething() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return null;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ReturnTypeIndexer _returnTypeIndexer = new ReturnTypeIndexer();
    this.exercise(code, _returnTypeIndexer);
    String _s = this.s(Fields.RETURN_TYPE, "Ljava/util/List");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testReturnTypeVoidIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testReturnSomething() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ReturnTypeIndexer _returnTypeIndexer = new ReturnTypeIndexer();
    this.exercise(code, _returnTypeIndexer);
    String _s = this.s(Fields.RETURN_TYPE, "Lvoid");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
    String _s_1 = this.s(Fields.RETURN_TYPE, "V");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s_1);
    List<String> _l_1 = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertNotField(_l_1);
  }
  
  @Test
  public void testParameterTypesIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testSomething(List a) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ParameterTypesIndexer _parameterTypesIndexer = new ParameterTypesIndexer();
    this.exercise(code, _parameterTypesIndexer);
    String _s = this.s(Fields.PARAMETER_TYPES, "Ljava/util/List");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testParameterCountIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testSomething(List a) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ParameterCountIndexer _parameterCountIndexer = new ParameterCountIndexer();
    this.exercise(code, _parameterCountIndexer);
    String _s = this.s(Fields.PARAMETER_COUNT, "1");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testParameterCountIndexer02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testSomething(List a, String b) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ParameterCountIndexer _parameterCountIndexer = new ParameterCountIndexer();
    this.exercise(code, _parameterCountIndexer);
    String _s = this.s(Fields.PARAMETER_COUNT, "2");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testReturnVariableExpressionsIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public List testReturnNames() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("List names = null;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return names;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ReturnVariableExpressionIndexer _returnVariableExpressionIndexer = new ReturnVariableExpressionIndexer();
    this.exercise(code, _returnVariableExpressionIndexer);
    String _s = this.s(Fields.RETURN_VARIABLE_EXPRESSIONS, "names");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testReturnVariableExpressionsIndexer02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public List testReturnNames(boolean value) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("List names1 = null;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("List names2 = null;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if(value) { return names1; }");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("else { return names2;}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ReturnVariableExpressionIndexer _returnVariableExpressionIndexer = new ReturnVariableExpressionIndexer();
    this.exercise(code, _returnVariableExpressionIndexer);
    String _s = this.s(Fields.RETURN_VARIABLE_EXPRESSIONS, "names1");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
    String _s_1 = this.s(Fields.RETURN_VARIABLE_EXPRESSIONS, "names2");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s_1);
    List<String> _l_1 = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l_1);
  }
  
  @Test
  public void testReturnVariableExpressionsIndexer03() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public List testReturnNames() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return null;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ReturnVariableExpressionIndexer _returnVariableExpressionIndexer = new ReturnVariableExpressionIndexer();
    this.exercise(code, _returnVariableExpressionIndexer);
    String _s = this.s(Fields.RETURN_VARIABLE_EXPRESSIONS, "null");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testCheckedExceptionsIndexer01() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.io.IOException;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public List method() throws IOException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    CheckedExceptionsIndexer _checkedExceptionsIndexer = new CheckedExceptionsIndexer();
    ArrayList<CheckedExceptionsIndexer> _newArrayList = CollectionLiterals.<CheckedExceptionsIndexer>newArrayList(_checkedExceptionsIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.CHECKED_EXCEPTIONS, "Ljava/io/IOException");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testParameterTypesStructuralIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.List;");
    _builder.newLine();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testReturnNames(List l, String s) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    ParameterTypesStructuralIndexer _parameterTypesStructuralIndexer = new ParameterTypesStructuralIndexer();
    this.exercise(code, _parameterTypesStructuralIndexer);
    String _s = this.s(Fields.PARAMETER_TYPES_STRUCTURAL, "Ljava/util/List;Ljava/lang/String;");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
  }
}
