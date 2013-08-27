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
 
package org.eclipse.recommenders.test.codesearch.rcp.indexer

import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.recommenders.codesearch.rcp.index.Fields
import org.eclipse.recommenders.codesearch.rcp.index.indexer.AllDeclaredFieldNamesIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.AnnotationsIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DeclaredFieldNamesIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DeclaredFieldTypesIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DeclaringTypeIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DocumentTypeIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.FieldsReadIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.FieldsWrittenIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.FullTextIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.InstanceOfIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ModifiersIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ProjectNameIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.QualifiedNameIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.ResourcePathIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.SimpleNameIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.TimestampIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.UsedFieldsInFinallyIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.UsedFieldsInTryIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.UsedMethodsIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.UsedTypesIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.visitor.CompilationUnitVisitor
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture
import org.junit.Test

import static org.eclipse.recommenders.test.codesearch.rcp.indexer.TestBase.*
import org.junit.Ignore

class TestGeneralScenarios extends TestBase {

	@Test
	def void testDocumentCounts(){
		val code = '''
		public class MyClass {
		} 
		'''
		
		 exercise(code, new SimpleNameIndexer())
		assertNumDocs( 1)
	}

	@Test
	def void testDocumentCounts02(){
		val code = '''
		public class MyClass {
			public void test() {
			}
		} 
		'''
		
		 exercise(code, new SimpleNameIndexer())
		assertNumDocs( 2)
	}

	@Test
	def void testDocumentCounts03(){
		val code = '''
		import java.util.List;
		import java.util.Map;
		
		public class MyClass {	
			Map map;
			public List test() {
				return null;
			}
		}
		'''
		
		 exercise(code, new SimpleNameIndexer())
		assertNumDocs( 3)
	}
	
	@Test
	def void testFriendlyNameIndexer(){
		val code = '''
		public class MyClass {
		} 
		'''
		
		 exercise(code, new SimpleNameIndexer())
		
		assertField( l(newArrayList(s(Fields::SIMPLE_NAME, "MyClass"))))
	}
	
	@Test
	def void testFriendlyNameIndexer02(){
		val code = '''
		public class MyClass {
			public void test() {}
		} 
		'''
		
		 exercise(code, new SimpleNameIndexer())
		
		assertField( l(newArrayList(s(Fields::SIMPLE_NAME, "test"))))
	}
	
	@Test
	def void testFriendlyNameIndexer03(){
		val code = '''
		import java.util.Map;
		public class MyClass {
			Map map;
		} 
		'''
		
		 exercise(code, new SimpleNameIndexer())
		
		assertField( l(newArrayList(s(Fields::SIMPLE_NAME, "map"))))
	}
	
	@Test
	def void testFullyQualifiedNameIndexer(){
		val code = '''
		public class MyClass {
		} 
		'''
		
		 exercise(code, new QualifiedNameIndexer())
		
		assertField( l(newArrayList(s(Fields::QUALIFIED_NAME, "LMyClass"))))
	}
	
	@Ignore
	@Test
	def void testFullyQualifiedNameIndexer04(){
		val code = '''
		package org.test;
		
		public class MyClass {
		} 
		'''
		
		 exercise(code, new QualifiedNameIndexer())
		
		assertField( l(newArrayList(s(Fields::QUALIFIED_NAME, "Lorg/test/MyClass"))))
	}
	
	@Test
	def void testFullyQualifiedNameIndexer02(){
		val code = '''
		public class MyClass {
			public void test() {}
		} 
		'''
		
		 exercise(code, new QualifiedNameIndexer())
		
		assertField( l(newArrayList(
			s(Fields::QUALIFIED_NAME, "LMyClass.test()V")
		)))
	}
	
	@Test
	def void testFullyQualifiedNameIndexer03(){
		val code = '''
		import java.util.Map;
		public class MyClass {
			Map mapInstance;
		} 
		'''
		
		 exercise(code, new QualifiedNameIndexer())
		
		assertField( l(newArrayList(s(Fields::QUALIFIED_NAME, "LMyClass.mapInstance"))))
	}
	
	@Test
	def void testDocumentTypeIndexerClassOnly(){
		val code = '''
		public class MyClass {
		} 
		'''
		
		 exercise(code, new DocumentTypeIndexer())
		
		assertField( l(newArrayList(s(Fields::TYPE, Fields::TYPE_CLASS))))
	}
	
	@Test
	def void testDocumentTypeIndexerClassAndField(){
		val code = '''
		import java.util.Map;
		public class MyClass {
			Map map;
		} 
		'''
		
		 exercise(code, new DocumentTypeIndexer())
		
		assertField( l(newArrayList(s(Fields::TYPE, Fields::TYPE_CLASS))))
		assertField( l(newArrayList(s(Fields::TYPE, Fields::TYPE_FIELD))))
	}
	
	@Test
	def void testDocumentTypeIndexerClassAndMethod(){
		val code = '''
		public class MyClass {
			public void test() {
			}
		} 
		'''
		
		 exercise(code, new DocumentTypeIndexer())
		
		assertField( l(newArrayList(s(Fields::TYPE, Fields::TYPE_CLASS))))
		assertField( l(newArrayList(s(Fields::TYPE, Fields::TYPE_METHOD))))
	}
	
	@Test
	def void testDocumentTypeIndexerClassMethodAndTryCatch(){
		val code = '''
		import java.util.Map;
		
		public class MyClass {	
			public void test() {
				try {
					Map map;
					if(map != null) { throw new Exception(); }
				} catch(Exception ex) {
				}
				return null;
			}
		}
		'''
		
		 exercise(code, new DocumentTypeIndexer())
		
		assertField( l(newArrayList(s(Fields::TYPE, Fields::TYPE_CLASS))))
		assertField( l(newArrayList(s(Fields::TYPE, Fields::TYPE_METHOD))))
		assertField( l(newArrayList(s(Fields::TYPE, Fields::TYPE_TRYCATCH))))
	}
	
	@Test
	def void testUsedTypesIndexer(){
		val code = '''
		import java.util.List;
		import java.util.Map;
		
		public class MyClass {	
			Map map;
		}
		'''
		
		 exercise(code, new UsedTypesIndexer())
		
		assertField( l(newArrayList(
			s(Fields::USED_TYPES, "Ljava/util/Map")
		)))
	}
	
	@Test
	def void testUsedTypesIndexer02(){
		val code = '''
		import java.util.List;
		import java.util.Map;
		
		public class MyClass {	
			Map map;
			public List test() {
				return null;
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new UsedTypesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::USED_TYPES, "Ljava/util/Map")
		)))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::USED_TYPES, "Ljava/util/List")
		)))
	}
	
	@Test
	def void testUsedTypesIndexer03(){
		val code = '''
		import java.util.Map;
		
		public class MyClass {	
			public void test() {
				try {
				} catch(Exception ex) {
					Map map;
				}
				return null;
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new UsedTypesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::USED_TYPES, "Ljava/util/Map")
		)))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::USED_TYPES, "Ljava/util/Map")
		)))
	}
	
	@Test
	def void testUsedTypesIndexerNoPrimitivesStringObjectEtc(){
		val code = '''
		import java.util.Map;
		
		public class MyClass {	
			String f;
			Object o1;
			public void test() {
				String g;
				Object o2;
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new UsedTypesIndexer())))
		
		assertNotField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::USED_TYPES, "Ljava/lang/String")
		)))
		
		assertNotField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::USED_TYPES, "Ljava/lang/String")
		)))
		assertNotField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::USED_TYPES, "Ljava/lang/Object")
		)))
		
		assertNotField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::USED_TYPES, "Ljava/lang/Object")
		)))
	}
	
	@Test
	def void testUsedMethodsIndexer(){
		val code = '''		
		public class MyClass {
			public List test() {
				String s = "";
				s.concat("test");
			}
		}
		'''
		
		exercise(code, i(newArrayList(new UsedMethodsIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::USED_METHODS, "Ljava/lang/String.concat(Ljava/lang/String;)Ljava/lang/String;")
		)))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::USED_METHODS, "Ljava/lang/String.concat(Ljava/lang/String;)Ljava/lang/String;")
		)))
	}
	
	@Test
	def void testUsedMethodsIndexer02(){
		val code = '''
		import java.util.Map;
		public class MyClass {	
			public List test() {
				String s = "";
				try {
				} catch(Exception ex) {
					s.concat("test");
				}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new UsedMethodsIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::USED_METHODS, "Ljava/lang/String.concat(Ljava/lang/String;)Ljava/lang/String;")
		)))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::USED_METHODS, "Ljava/lang/String.concat(Ljava/lang/String;)Ljava/lang/String;")
		)))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::USED_METHODS, "Ljava/lang/String.concat(Ljava/lang/String;)Ljava/lang/String;")
		)))
	}
	
	@Test
	def void testDeclaringTypeIndexerMethod(){
		val code = '''
		public class MyClass {
			public void foo() {
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new SimpleNameIndexer(), new DeclaringTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::SIMPLE_NAME, "foo"),
			s(Fields::DECLARING_TYPE, "LMyClass")
		)))		
	}
	
	@Test
	def void testDeclaringTypeIndexerField(){
		val code = '''
		public class MyClass {
			Map map;
		}
		'''
		
		 exercise(code, i(newArrayList(new SimpleNameIndexer(), new DeclaringTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::SIMPLE_NAME, "map"),
			s(Fields::DECLARING_TYPE, "LMyClass")
		)))		
	}
	
	@Test
	def void testDeclaringTypeIndexerType(){
		val code = '''
		public class MyClass {
			public class SubClass {
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new SimpleNameIndexer(), new DeclaringTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::SIMPLE_NAME, "SubClass"),
			s(Fields::DECLARING_TYPE, "LMyClass")
		)))		
	}
	
	@Test
	def void testDeclaringTypeIndexerVarUsage(){
		val code = '''
		public class MyClass {
			public void testMethod123() {
				String s;
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new DeclaringTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::DECLARING_TYPE, "LMyClass")
		)))		
	}
	
	@Test
	def void testDeclaringTypeIndexerTryCatch(){
		val code = '''
		public class MyClass {
			public void testMethod123() {
				try{} catch(Exception ex){}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new DeclaringTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::DECLARING_TYPE, "LMyClass")
		)))		
	}
	
	@Test
	def void testProjectNameIndexer(){
		val code = '''
		public class MyClass {
		}
		'''
		
		 exercise(code, i(newArrayList(new ProjectNameIndexer(), new DocumentTypeIndexer())), "projectName")
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::PROJECT_NAME, "projectName")
		)))		
	}
	
	@Test
	def void testProjectNameIndexer02(){
		val code = '''
		public class MyClass {
			public void myMethod() {
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new ProjectNameIndexer(), new DocumentTypeIndexer())), "projectName")
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::PROJECT_NAME, "projectName")
		)))		
	}
	
	@Test
	def void testProjectNameIndexer03(){
		val code = '''
		public class MyClass {
			MyClass test;
		}
		'''
		
		 exercise(code, i(newArrayList(new ProjectNameIndexer(), new DocumentTypeIndexer())), "projectName")
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_FIELD),
			s(Fields::PROJECT_NAME, "projectName")
		)))		
	}
	
	@Test
	def void testProjectNameIndexer04(){
		val code = '''
		public class MyClass {
			public void myMethod() {
				try {}
				catch(Exception ex) {}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new ProjectNameIndexer(), new DocumentTypeIndexer())), "projectName")
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::PROJECT_NAME, "projectName")
		)))		
	}
	
	@Test
	def void testResourcePathIndexer(){
		val code = '''
		public class MyClass {
		}
		'''
		
		val fixture = new JavaProjectFixture(ResourcesPlugin::getWorkspace(),"projectName")
		val struct = fixture.createFileAndParseWithMarkers(code.toString)
		val cu = struct.first;
        var cuParsed = parse(cu);

	
        var visitor = new CompilationUnitVisitor(f.index);
        visitor.addIndexer(i(newArrayList(new ResourcePathIndexer(), new DocumentTypeIndexer())));
        
        cuParsed.accept(visitor)
       	f.index.commit
        		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::RESOURCE_PATH, ResourcePathIndexer::getPath(getCompilationUnitFromAstNode(cuParsed)))
		)))		
	}
	
	@Test
	def void testResourcePathIndexer02(){
		val code = '''
		public class MyClass {
			public void myMethod() {
			}
		}
		'''
		
		val fixture = new JavaProjectFixture(ResourcesPlugin::getWorkspace(),"projectName")
		val struct = fixture.createFileAndParseWithMarkers(code.toString)
		val cu = struct.first;
        var cuParsed = parse(cu);

		
        var visitor = new CompilationUnitVisitor(f.index);
        visitor.addIndexer(i(newArrayList(new ResourcePathIndexer(), new DocumentTypeIndexer())));
        
        cuParsed.accept(visitor)
        f.index.commit
        		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::RESOURCE_PATH, ResourcePathIndexer::getPath(getCompilationUnitFromAstNode(cuParsed)))
		)))		
	}
	
	@Test
	def void testResourcePathIndexer03(){
		val code = '''
		public class MyClass {
			MyClass test;
		}
		'''
		
		val fixture = new JavaProjectFixture(ResourcesPlugin::getWorkspace(),"projectName")
		val struct = fixture.createFileAndParseWithMarkers(code.toString)
		val cu = struct.first;
        var cuParsed = parse(cu);

        
		
        var visitor = new CompilationUnitVisitor(f.index);
        visitor.addIndexer(i(newArrayList(new ResourcePathIndexer(), new DocumentTypeIndexer())));
        
        cuParsed.accept(visitor)
        f.index.commit
        		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_FIELD),
			s(Fields::RESOURCE_PATH, ResourcePathIndexer::getPath(getCompilationUnitFromAstNode(cuParsed)))
		)))		
	}
	
	@Test
	def void testResourcePathIndexer04(){
		val code = '''
		public class MyClass {
			public void myMethod() {
				try {}
				catch(Exception ex) {}
			}
		}
		'''
		
		val fixture = new JavaProjectFixture(ResourcesPlugin::getWorkspace(),"projectName")
		val struct = fixture.createFileAndParseWithMarkers(code.toString)
		val cu = struct.first;
        var cuParsed = parse(cu);

        var visitor = new CompilationUnitVisitor(f.index);
        visitor.addIndexer(i(newArrayList(new ResourcePathIndexer(), new DocumentTypeIndexer())));
        
        cuParsed.accept(visitor)
        f.index.commit
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::RESOURCE_PATH, ResourcePathIndexer::getPath(getCompilationUnitFromAstNode(cuParsed))
			)
		)))			
	}
	
	@Test
	def void testResourcePathIndexer05(){
		val code = '''
		public class MyClass {
			public void myMethod() {
				String a = "";
			}
		}
		'''
		
		val fixture = new JavaProjectFixture(ResourcesPlugin::getWorkspace(),"projectName")
		val struct = fixture.createFileAndParseWithMarkers(code.toString)
		val cu = struct.first;
        var cuParsed = parse(cu);

		
        var visitor = new CompilationUnitVisitor(f.index);
        visitor.addIndexer(i(newArrayList(new ResourcePathIndexer(), new DocumentTypeIndexer())));
        
        cuParsed.accept(visitor)
		f.index.commit
		        		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::RESOURCE_PATH, ResourcePathIndexer::getPath(getCompilationUnitFromAstNode(cuParsed)))
		)))		
	}
	
	@Test
	def void testModifiersIndexerClass(){
		val code = '''
		public class MyClass {
		}
		'''
		
		 exercise(code, i(newArrayList(new ModifiersIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::MODIFIERS, "public")
		)))		
	}
	
	@Test
	def void testModifiersIndexerClass02(){
		val code = '''
		public abstract class MyClass {
		}
		'''
		
		 exercise(code, i(newArrayList(new ModifiersIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::MODIFIERS, Fields::MODIFIER_PUBLIC),
			s(Fields::MODIFIERS, Fields::MODIFIER_ABSTRACT)
		)))		
	}
	
	@Test
	def void testModifiersIndexerMethod(){
		val code = '''
		public class MyClass {
			public void doSomethingNow123413() {}
		}
		'''
		
		 exercise(code, i(newArrayList(new ModifiersIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::MODIFIERS, Fields::MODIFIER_PUBLIC)
		)))		
	}
	
	@Test
	def void testModifiersIndexerMethod02(){
		val code = '''
		public abstract class MyClass {
			public static void doSomethingNow123413() {}
		}
		'''
		
		 exercise(code, i(newArrayList(new ModifiersIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::MODIFIERS, Fields::MODIFIER_PUBLIC),
			s(Fields::MODIFIERS, Fields::MODIFIER_STATIC)
		)))		
	}
	
	@Test
	def void testModifiersIndexerField(){
		val code = '''
		import java.util.Map;
		public class MyClass {
			private Map map;
		}
		'''
		
		 exercise(code, i(newArrayList(new ModifiersIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_FIELD),
			s(Fields::MODIFIERS, Fields::MODIFIER_PRIVATE)
		)))		
	}
	
	@Test
	def void testModifiersIndexerField02(){
		val code = '''
		import java.util.Map;
		public final class MyClass {
			protected static Map map;
		}
		'''
		
		 exercise(code, i(newArrayList(new ModifiersIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_FIELD),
			s(Fields::MODIFIERS, Fields::MODIFIER_PROTECTED),
			s(Fields::MODIFIERS, Fields::MODIFIER_STATIC)
		)))		
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::MODIFIERS, Fields::MODIFIER_FINAL)
		)))	
	}
	
	@Test
	def void testDeclaredFieldNamesClass(){
		val code = '''
		import java.util.Map;
		public final class MyClass {
			Map map;
		}
		'''
		
		 exercise(code, i(newArrayList(new DeclaredFieldNamesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::DECLARED_FIELD_NAMES, "map")
		)))	
	}
	
	@Test
	def void testDeclaredFieldNamesClassWithInitalizer(){
		val code = '''
		import java.util.Map;
		public final class MyClass {
			Map map = new HashMap();
		}
		'''
		
		 exercise(code, i(newArrayList(new DeclaredFieldNamesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::DECLARED_FIELD_NAMES, "map")
		)))	
	}
	
	@Test
	def void testDeclaredFieldNamesMethod(){
		val code = '''
		import java.util.Map;
		public final class MyClass {
			void doSomethingElse() {
				Map map;
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DeclaredFieldNamesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::DECLARED_FIELD_NAMES, "map")
		)))	
	}
	
	@Test
	def void testDeclaredFieldNamesTryCatch(){
		val code = '''
		import java.util.Map;
		public final class MyClass {
			void doSomethingElse() {
				try {}
				catch(Exception ex) { Map map; }
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DeclaredFieldNamesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::DECLARED_FIELD_NAMES, "map")
		)))	
	}
	
	@Test
	def void testDeclaredFieldTypesClass(){
		val code = '''
		import java.util.Map;
		public final class MyClass {
			Map map;
		}
		'''
		
		 exercise(code, i(newArrayList(new DeclaredFieldTypesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::DECLARED_FIELD_TYPES, "Ljava/util/Map")
		)))	
	}
	
	@Test
	def void testDeclaredFieldTypesMethod(){
		val code = '''
		import java.util.Map;
		public final class MyClass {
			void doSomethingElse() {
				Map map;
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DeclaredFieldTypesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::DECLARED_FIELD_TYPES, "Ljava/util/Map")
		)))	
	}
	
	@Test
	def void testDeclaredFieldTypesTry(){
		val code = '''
		import java.util.Map;
		public final class MyClass {
			void doSomethingElse() {
				try {}
				catch(Exception ex) { Map map; }
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DeclaredFieldTypesIndexer(), new DocumentTypeIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::DECLARED_FIELD_TYPES, "Ljava/util/Map")
		)))	
	}
	
	@Test
	def void testAllFieldNamesIndexerClass(){
		val code = '''
		import java.util.Map;
		import java.io.IOException;
		public class MyOtherException extends IOException {
			private Map theMapyMap;
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new AllDeclaredFieldNamesIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::ALL_DECLARED_FIELD_NAMES, "theMapyMap")
		)))
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::ALL_DECLARED_FIELD_NAMES, "serialVersionUID")
		)))
	}
	
//	@Test
//	def void testAllFieldNamesIndexerMethod(){
//		val code = '''
//		import java.util.Map;
//		public class MyOtherException extends IOException {
//			void doThisAndThat() {
//				Map theMapyMap;
//			}
//		}
//		'''
//		
//		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new AllDeclaredFieldNamesIndexer())))
//		
//		assertField( l(newArrayList(
//			s(Fields::TYPE, Fields::TYPE_METHOD),
//			s(Fields::ALL_DECLARED_FIELD_NAMES, "theMapyMap")
//		)))
//	}
//	
//	@Test
//	def void testAllFieldNamesIndexerMethod02(){
//		val code = '''
//		import java.util.Map;
//		public class MyOtherException extends IOException {
//			private Map theMapyMap;
//			void doThisAndThat() {
//				Map someOtherMap;
//			}
//		}
//		'''
//		
//		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new AllDeclaredFieldNamesIndexer())))
//		
//		assertField( l(newArrayList(
//			s(Fields::TYPE, Fields::TYPE_METHOD),
//			s(Fields::ALL_DECLARED_FIELD_NAMES, "someOtherMap")
//		)))
//		assertNotField( l(newArrayList(
//			s(Fields::TYPE, Fields::TYPE_METHOD),
//			s(Fields::ALL_DECLARED_FIELD_NAMES, "theMapyMap")
//		)))
//	}
//	
//	@Test
//	def void testAllFieldNamesIndexerTryCatch(){
//		val code = '''
//		import java.util.Map;
//		public class MyOtherException extends IOException {
//			private Map theMapyMap;
//			void doThisAndThat() {
//				try {}
//				catch(Exception ex) {
//					Map someOtherMap;
//				}
//			}
//		}
//		'''
//		
//		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new AllDeclaredFieldNamesIndexer())))
//		
//		assertField( l(newArrayList(
//			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
//			s(Fields::ALL_DECLARED_FIELD_NAMES, "someOtherMap")
//		)))
//	}
	
	@Test
	def void testAllFieldNamesIndexerClass02(){
		val code = '''
		import java.util.Map;
		import java.io.IOException;
		public class MyOtherException extends IOException {
			private Map theMapyMap;
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new AllDeclaredFieldNamesIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::ALL_DECLARED_FIELD_NAMES, "theMapyMap"),
			s(Fields::ALL_DECLARED_FIELD_NAMES, "stackTrace")
		)))
	}
	
	@Test
	def void testFullTextIndexerClass(){
		val code = '''
		import java.io.IOException;
		public class MOtherException extends IOException {
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FullTextIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::FULL_TEXT, '''public class MOtherException extends IOException {
}'''.toString)
		)))
	}
	
	@Test
	def void testFullTextIndexerMethod(){
		val code = '''
		import java.io.IOException;
		public class MyTinyException extends IOException {
			public static void theEasiestMethodEver() {
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FullTextIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::FULL_TEXT, '''public static void theEasiestMethodEver(){
}'''.toString)
		)))
	}
	
	@Test
	def void testFullTextIndexerTryCatch(){
		val code = '''
		import java.io.IOException;
		public class MyRandomException extends IOException {
			public static void theWorstMethodEver() {
				try {}
				catch(Exception ex) {
					//This is a comment
				}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FullTextIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::FULL_TEXT, '''catch (Exception ex) {
}'''.toString)
		)))
	}
	
	@Test
	def void testFullTextIndexerField(){
		val code = '''
		import java.util.Map;
		import java.io.IOException;
		public class MyOtherOtherException extends IOException {
			Map theWorldMap;
			public static void theBestMethodEver() {
				try {}
				catch(Exception ex) {
					//This is a comment
				}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FullTextIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_FIELD),
			s(Fields::FULL_TEXT, '''Map theWorldMap;'''.toString)
		)))
	}
	
	@Test
	def void testFieldsReadIndexerMethod(){
		val code = '''
		import java.util.Map;
		import java.io.IOException;
		public class testFieldsReadIndexerMethod extends IOException {
			public Map theWorldMap;
			public static void theBestMethodEver() {
				MyOtherOtherException m;
				Object o = m.theWorldMap;
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FieldsReadIndexer(), new FieldsWrittenIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::FIELDS_READ, "LtestFieldsReadIndexerMethod.theWorldMap")
		)))
		
		assertNotField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::FIELDS_WRITTEN, "LtestFieldsReadIndexerMethod.someObject")
		)))
	}
	
	@Test
	def void testFieldsReadIndexerClass(){
		val code = '''
		import java.io.IOException;
		public class testFieldsReadIndexerClass {
			public Object someObject = null;
			public Testclass ob = new Testclass();
			public Object anObject = ob.someObject;
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FieldsReadIndexer(), new FieldsWrittenIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::FIELDS_READ, "LtestFieldsReadIndexerClass.someObject")
		)))
		
//		assertNotField( l(newArrayList(
//			s(Fields::TYPE, Fields::TYPE_CLASS),
//			s(Fields::FIELDS_WRITTEN, "LTestclass.someObject")
//		)))
	}
	
	@Test
	def void testFieldsReadIndexerTryCatch(){
		val code = '''
		import java.io.IOException;
		public class testFieldsReadIndexerTryCatch extends IOException {
			public Object someObject = null;
			public Object theWorldMap = (new Testclass()).someObject;
			public static void theBestMethodEver() {
				try {
				} catch(Exception ex) {
					Testclass c = new Testclass();
					Object myObject = c.someObject;
				}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FieldsReadIndexer(), new FieldsWrittenIndexer())))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::FIELDS_READ, "LtestFieldsReadIndexerTryCatch.someObject")
		)))
		
//		assertNotField( l(newArrayList(
//			s(Fields::TYPE, Fields::TYPE_CLASS),
//			s(Fields::FIELDS_WRITTEN, "LTestclass.someObject")
//		)))
	}
	
	@Test
	def void testFieldsWrittenIndexerMethod(){
		val code = '''
		import java.util.Map;
		import java.io.IOException;
		public class MyOtherOtherException extends IOException {
			public Map theWorldMap;
			public static void theBestMethodEver() {
				MyOtherOtherException m = null;
				m.theWorldMap = null;
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new FieldsReadIndexer(), new FieldsWrittenIndexer())))
		
//		assertNotField( l(newArrayList(
//			s(Fields::TYPE, Fields::TYPE_METHOD),
//			s(Fields::FIELDS_READ, "LMyOtherOtherException.theWorldMap")
//		)))
		
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::FIELDS_WRITTEN, "LMyOtherOtherException.theWorldMap")
		)))
	}
	
	@Test
	def void testUsedFieldsInTryIndexer(){
		val code = '''
		import java.util.Map;
		import java.io.IOException;
		public class MyOtherOtherException extends IOException {
			public Map theWorldMap;
			public static void theBestMethodEver() {
				try {
					MyOtherOtherException m = null;
					m.theWorldMap = null;
				} catch(Exception ex) {
				} finally {
				}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new UsedFieldsInTryIndexer())))
				
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::USED_FIELDS_IN_TRY, "LMyOtherOtherException.theWorldMap")
		)))
	}
	
	@Test
	def void testUsedFieldsInFinallyIndexer(){
		val code = '''
		import java.util.Map;
		import java.io.IOException;
		public class MyOtherOtherException extends IOException {
			public Map theWorldMap;
			public static void theBestMethodEver() {
				try {
				} catch(Exception ex) {
				} finally {
					MyOtherOtherException m = null;
					m.theWorldMap = null;
				}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new UsedFieldsInFinallyIndexer())))
				
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::USED_FIELDS_IN_FINALLY, "LMyOtherOtherException.theWorldMap")
		)))
	}
	
	@Test
	def void testAnnotationIndexer(){
		val code = '''
		@Deprecated
		public class MyAnnotatedClass {
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new AnnotationsIndexer())))
				
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::ANNOTATIONS, "Ljava/lang/Deprecated")
		)))
	}
	
	@Test
	def void testAnnotationIndexer02(){
		val code = '''
		@SuppressWarnings({"unchecked", "rawtypes"})
		public class MyAnnotatedClass {
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new AnnotationsIndexer())))
				
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::ANNOTATIONS, "Ljava/lang/SuppressWarnings")
		)))
			
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::ANNOTATIONS, "Ljava/lang/SuppressWarnings:unchecked")
		)))
			
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::ANNOTATIONS, "Ljava/lang/SuppressWarnings:rawtypes")
		)))
	}
	
	@Test
	def void testAnnotationIndexer03(){
		val code = '''
		import java.util.List;
		public class MyAnnotatedClass {
			@SuppressWarnings("rawtypes")
			public static String printLabel(List l) {
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new AnnotationsIndexer())))
				
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::ANNOTATIONS, "Ljava/lang/SuppressWarnings")
		)))
			
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::ANNOTATIONS, "Ljava/lang/SuppressWarnings:rawtypes")
		)))
	}
	
	@Test
	def void testInstanceOfIndexerClass(){
		val code = '''
		public class MyInstanceOfClass {
			public void operation() {
				Object a = new String();
				
				if(a instanceof Exception) {
					//Somethin's fishy
				} 
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new InstanceOfIndexer())))
				
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::INSTANCEOF_TYPES, "Ljava/lang/Exception")
		)))
	}
	
	@Test
	def void testInstanceOfIndexerMethod(){
		val code = '''
		public class MyInstanceOfClass {
			public void operation() {
				Object a = new String();
				
				if(a instanceof Exception) {
					//Somethin's fishy
				} 
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new InstanceOfIndexer())))
				
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::INSTANCEOF_TYPES, "Ljava/lang/Exception")
		)))
	}
	
	@Test
	def void testInstanceOfIndexerTryCatch(){
		val code = '''
		public class MyInstanceOfClass {
			public void operation() {
				Object a = new String();
				
				try {
				}
				catch(Exception ex) {
					if(a instanceof Exception) {
						//Somethin's fishy
					} 
				}
			}
		}
		'''
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new InstanceOfIndexer())))
				
		assertField( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::INSTANCEOF_TYPES, "Ljava/lang/Exception")
		)))
	}
	
	@Test
	def void testTimestampIndexer() {
		val code = '''
		public class MyInstanceOfClass {
		}
		'''
		
        TimestampIndexer::updateCurrentTimestamp();
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new TimestampIndexer())))
				
		assertFieldStartsWith( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_CLASS),
			s(Fields::TIMESTAMP, TimestampIndexer::getTimeString().substring(0, 8)) //This test obviously will fail from time to time
		)))
	}
	
	@Test
	def void testTimestampIndexer02() {
		val code = '''
		public class MyInstanceOfClass {
			public void operation() {
			}
		}
		'''
		
        TimestampIndexer::updateCurrentTimestamp();
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new TimestampIndexer())))
				
		assertFieldStartsWith( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_METHOD),
			s(Fields::TIMESTAMP, TimestampIndexer::getTimeString().substring(0, 8)) //This test obviously will fail from time to time
		)))
	}
	
	@Test
	def void testTimestampIndexer03() {
		val code = '''
		public class MyInstanceOfClass {
			private String s;
		}
		'''
		
        TimestampIndexer::updateCurrentTimestamp();
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new TimestampIndexer())))
				
		assertFieldStartsWith( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_FIELD),
			s(Fields::TIMESTAMP, TimestampIndexer::getTimeString().substring(0, 8)) //This test obviously will fail from time to time
		)))
	}
	
	@Test
	def void testTimestampIndexer04() {
		val code = '''
		public class MyInstanceOfClass {
			public void operation() {
				try {
				}
				catch(Exception ex) {
				}
			}
		}
		'''
		
        TimestampIndexer::updateCurrentTimestamp();
		
		 exercise(code, i(newArrayList(new DocumentTypeIndexer(), new TimestampIndexer())))
				
		assertFieldStartsWith( l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_TRYCATCH),
			s(Fields::TIMESTAMP, TimestampIndexer::getTimeString().substring(0, 8)) //This test obviously will fail from time to time
		)))
	}
}