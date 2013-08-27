package org.eclipse.recommenders.test.codesearch.rcp.indexer;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DeclaringMethodIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DocumentTypeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableDefinitionIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableNameIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableParameterUsageIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableTargetUsageIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableTypeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IVarUsageIndexer;
import org.eclipse.recommenders.test.codesearch.rcp.indexer.TestBase;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.junit.Test;

@SuppressWarnings("all")
public class TestVariableUsageScenarios extends TestBase {
  @Test
  public void testDeclaringMethodIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("String s;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    DeclaringMethodIndexer _declaringMethodIndexer = new DeclaringMethodIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _declaringMethodIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.DECLARING_METHOD, "LMyClass.testMethod()V");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableNameIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("String s;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableNameIndexer _variableNameIndexer = new VariableNameIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableNameIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_NAME, "s");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableNameIndexer02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("String s, a;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableNameIndexer _variableNameIndexer = new VariableNameIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableNameIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_NAME, "a");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
    String _s_2 = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_3 = this.s(Fields.VARIABLE_NAME, "s");
    ArrayList<String> _newArrayList_2 = CollectionLiterals.<String>newArrayList(_s_2, _s_3);
    List<String> _l_1 = this.l(((String[])Conversions.unwrapArray(_newArrayList_2, String.class)));
    this.assertField(_l_1);
  }
  
  @Test
  public void testVariableNameAsParameterIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod(String s) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableNameIndexer _variableNameIndexer = new VariableNameIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableNameIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_NAME, "s");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableTypeIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("String s;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableTypeIndexer _variableTypeIndexer = new VariableTypeIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableTypeIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_TYPE, "Ljava/lang/String");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableTypeIndexer02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("String s, a;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableTypeIndexer _variableTypeIndexer = new VariableTypeIndexer();
    VariableNameIndexer _variableNameIndexer = new VariableNameIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableTypeIndexer, _variableNameIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_NAME, "s");
    String _s_2 = this.s(Fields.VARIABLE_TYPE, "Ljava/lang/String");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1, _s_2);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
    String _s_3 = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_4 = this.s(Fields.VARIABLE_NAME, "a");
    String _s_5 = this.s(Fields.VARIABLE_TYPE, "Ljava/lang/String");
    ArrayList<String> _newArrayList_2 = CollectionLiterals.<String>newArrayList(_s_3, _s_4, _s_5);
    List<String> _l_1 = this.l(((String[])Conversions.unwrapArray(_newArrayList_2, String.class)));
    this.assertField(_l_1);
  }
  
  @Test
  public void testVariableDefinitionIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("String s;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableDefinitionIndexer _variableDefinitionIndexer = new VariableDefinitionIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableDefinitionIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_DEFINITION, Fields.DEFINITION_UNINITIALIZED);
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableDefinitionIndexer02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod(String s) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableDefinitionIndexer _variableDefinitionIndexer = new VariableDefinitionIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableDefinitionIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_DEFINITION, Fields.DEFINITION_PARAMETER);
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableDefinitionIndexer03() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("Object o = new Object();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableDefinitionIndexer _variableDefinitionIndexer = new VariableDefinitionIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableDefinitionIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_DEFINITION, Fields.DEFINITION_INSTANCE_CREATION);
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableDefinitionIndexer04() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("Object o = null;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableDefinitionIndexer _variableDefinitionIndexer = new VariableDefinitionIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableDefinitionIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_DEFINITION, Fields.DEFINITION_NULLLITERAL);
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableDefinitionIndexer05() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("String s1 = \"\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("String s2 = s1.toString();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableDefinitionIndexer _variableDefinitionIndexer = new VariableDefinitionIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableDefinitionIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_DEFINITION, Fields.DEFINITION_ASSIGNMENT);
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableDefinitionIndexer07() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("int s2 = 1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableDefinitionIndexer _variableDefinitionIndexer = new VariableDefinitionIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableDefinitionIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_DEFINITION, Fields.DEFINITION_ASSIGNMENT);
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableDefinitionIndexer06() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("String s1 = \"\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableDefinitionIndexer _variableDefinitionIndexer = new VariableDefinitionIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableDefinitionIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.VARIABLE_DEFINITION, Fields.DEFINITION_ASSIGNMENT);
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableUsedAsParameterIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("String s = null;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("System.out.println(s);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableParameterUsageIndexer _variableParameterUsageIndexer = new VariableParameterUsageIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableParameterUsageIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.USED_AS_PARAMETER_IN_METHODS, "Ljava/io/PrintStream.println(Ljava/lang/String;)V|System.out");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableUsedAsParameterIndexer02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("String s1 = null;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("String s2 = null;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("s2.compareTo(s1);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableParameterUsageIndexer _variableParameterUsageIndexer = new VariableParameterUsageIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableParameterUsageIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.USED_AS_PARAMETER_IN_METHODS, "Ljava/lang/String.compareTo(Ljava/lang/String;)I|s2");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testVariableUsedAsTargetIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClass {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void testMethod() {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("String s = null;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("s.toString();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    VariableTargetUsageIndexer _variableTargetUsageIndexer = new VariableTargetUsageIndexer();
    ArrayList<IVarUsageIndexer> _newArrayList = CollectionLiterals.<IVarUsageIndexer>newArrayList(_documentTypeIndexer, _variableTargetUsageIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_VARUSAGE);
    String _s_1 = this.s(Fields.USED_AS_TAGET_FOR_METHODS, "Ljava/lang/String.toString()Ljava/lang/String;");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
}
