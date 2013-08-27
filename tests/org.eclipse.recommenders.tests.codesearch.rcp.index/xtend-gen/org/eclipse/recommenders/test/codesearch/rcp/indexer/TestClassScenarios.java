package org.eclipse.recommenders.test.codesearch.rcp.indexer;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.AllDeclaredMethodNamesIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.AllExtendedTypesIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.AllImplementedInterfacesIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DeclaredMethodNamesIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DeclaredMethodsIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DocumentTypeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ExtendedTypeIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ImplementedInterfacesIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.OverriddenMethodsIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IClassIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.test.codesearch.rcp.indexer.TestBase;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.junit.Test;

@SuppressWarnings("all")
public class TestClassScenarios extends TestBase {
  @Test
  public void testDeclaredMethodsIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class MyClassXyZ {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void test() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void foo() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DeclaredMethodsIndexer _declaredMethodsIndexer = new DeclaredMethodsIndexer();
    this.exercise(code, _declaredMethodsIndexer);
    String _s = this.s(Fields.DECLARED_METHODS, "LMyClassXyZ.test()V");
    String _s_1 = this.s(Fields.DECLARED_METHODS, "LMyClassXyZ.foo()V");
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testOverriddenMethodsIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class Clazz {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String toString() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return super.toString();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    OverriddenMethodsIndexer _overriddenMethodsIndexer = new OverriddenMethodsIndexer();
    ArrayList<IClassIndexer> _newArrayList = CollectionLiterals.<IClassIndexer>newArrayList(_documentTypeIndexer, _overriddenMethodsIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_CLASS);
    String _s_1 = this.s(Fields.OVERRIDDEN_METHODS, "Ljava/lang/Object.toString()Ljava/lang/String;");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testExtendedTypeIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.io.IOException;");
    _builder.newLine();
    _builder.append("public class MyException extends IOException {");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    ExtendedTypeIndexer _extendedTypeIndexer = new ExtendedTypeIndexer();
    ArrayList<IClassIndexer> _newArrayList = CollectionLiterals.<IClassIndexer>newArrayList(_documentTypeIndexer, _extendedTypeIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_CLASS);
    String _s_1 = this.s(Fields.EXTENDED_TYPE, "Ljava/io/IOException");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testAllExtendedTypesIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.io.IOException;");
    _builder.newLine();
    _builder.append("public class MyException extends IOException {");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    AllExtendedTypesIndexer _allExtendedTypesIndexer = new AllExtendedTypesIndexer();
    ArrayList<IClassIndexer> _newArrayList = CollectionLiterals.<IClassIndexer>newArrayList(_documentTypeIndexer, _allExtendedTypesIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_CLASS);
    String _s_1 = this.s(Fields.ALL_EXTENDED_TYPES, "Ljava/io/IOException");
    String _s_2 = this.s(Fields.ALL_EXTENDED_TYPES, "Ljava/lang/Exception");
    String _s_3 = this.s(Fields.ALL_EXTENDED_TYPES, "Ljava/lang/Throwable");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1, _s_2, _s_3);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testImplementedInterfacesIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.io.Externalizable;");
    _builder.newLine();
    _builder.append("public class MyInterface implements Externalizable {");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    ImplementedInterfacesIndexer _implementedInterfacesIndexer = new ImplementedInterfacesIndexer();
    ArrayList<IClassIndexer> _newArrayList = CollectionLiterals.<IClassIndexer>newArrayList(_documentTypeIndexer, _implementedInterfacesIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_CLASS);
    String _s_1 = this.s(Fields.IMPLEMENTED_TYPES, "Ljava/io/Externalizable");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testAllImplementedInterfacesIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.io.Externalizable;");
    _builder.newLine();
    _builder.append("public class MyInterface implements Externalizable {");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    AllImplementedInterfacesIndexer _allImplementedInterfacesIndexer = new AllImplementedInterfacesIndexer();
    ArrayList<IClassIndexer> _newArrayList = CollectionLiterals.<IClassIndexer>newArrayList(_documentTypeIndexer, _allImplementedInterfacesIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_CLASS);
    String _s_1 = this.s(Fields.ALL_IMPLEMENTED_TYPES, "Ljava/io/Externalizable");
    String _s_2 = this.s(Fields.ALL_IMPLEMENTED_TYPES, "Ljava/io/Serializable");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1, _s_2);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testDeclaredMethodNamesIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public class Clazz {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void method01() {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private void method02() {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected void method03() {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    DeclaredMethodNamesIndexer _declaredMethodNamesIndexer = new DeclaredMethodNamesIndexer();
    ArrayList<IClassIndexer> _newArrayList = CollectionLiterals.<IClassIndexer>newArrayList(_documentTypeIndexer, _declaredMethodNamesIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_CLASS);
    String _s_1 = this.s(Fields.DECLARED_METHODS_NAMES, "method01");
    String _s_2 = this.s(Fields.DECLARED_METHODS_NAMES, "method02");
    String _s_3 = this.s(Fields.DECLARED_METHODS_NAMES, "method03");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1, _s_2, _s_3);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testAllMethodNamesIndexer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.io.IOException;");
    _builder.newLine();
    _builder.append("public class MyOtherException extends IOException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void method01() {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private void method02() {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected void method03() {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    AllDeclaredMethodNamesIndexer _allDeclaredMethodNamesIndexer = new AllDeclaredMethodNamesIndexer();
    ArrayList<IClassIndexer> _newArrayList = CollectionLiterals.<IClassIndexer>newArrayList(_documentTypeIndexer, _allDeclaredMethodNamesIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_CLASS);
    String _s_1 = this.s(Fields.ALL_DECLARED_METHOD_NAMES, "method01");
    String _s_2 = this.s(Fields.ALL_DECLARED_METHOD_NAMES, "method02");
    String _s_3 = this.s(Fields.ALL_DECLARED_METHOD_NAMES, "method03");
    String _s_4 = this.s(Fields.ALL_DECLARED_METHOD_NAMES, "getMessage");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1, _s_2, _s_3, _s_4);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertField(_l);
  }
  
  @Test
  public void testAllMethodNamesIndexer02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.io.IOException;");
    _builder.newLine();
    _builder.append("public class MyOtherException extends IOException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void method01() {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private void method02() {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected void method03() {}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    final CharSequence code = _builder;
    DocumentTypeIndexer _documentTypeIndexer = new DocumentTypeIndexer();
    DeclaredMethodNamesIndexer _declaredMethodNamesIndexer = new DeclaredMethodNamesIndexer();
    ArrayList<IClassIndexer> _newArrayList = CollectionLiterals.<IClassIndexer>newArrayList(_documentTypeIndexer, _declaredMethodNamesIndexer);
    List<IIndexer> _i = this.i(((IIndexer[])Conversions.unwrapArray(_newArrayList, IIndexer.class)));
    this.exercise(code, _i);
    String _s = this.s(Fields.TYPE, Fields.TYPE_CLASS);
    String _s_1 = this.s(Fields.ALL_DECLARED_METHOD_NAMES, "getMessage");
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList(_s, _s_1);
    List<String> _l = this.l(((String[])Conversions.unwrapArray(_newArrayList_1, String.class)));
    this.assertNotField(_l);
  }
}
