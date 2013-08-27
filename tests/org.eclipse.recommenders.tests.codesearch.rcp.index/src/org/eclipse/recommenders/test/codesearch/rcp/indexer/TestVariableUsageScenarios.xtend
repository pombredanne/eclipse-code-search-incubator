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

import org.junit.Test
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DocumentTypeIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.DeclaringMethodIndexer
import org.eclipse.recommenders.codesearch.rcp.index.Fields
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableNameIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableTypeIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableDefinitionIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableParameterUsageIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.VariableTargetUsageIndexer

class TestVariableUsageScenarios extends TestBase {
	
	@Test
	def void testDeclaringMethodIndexer(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				String s;
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new DeclaringMethodIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::DECLARING_METHOD, "LMyClass.testMethod()V")
		)))		
	}
	
	@Test
	def void testVariableNameIndexer(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				String s;
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableNameIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_NAME, "s")
		)))		
	}
	
	@Test
	def void testVariableNameIndexer02(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				String s, a;
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableNameIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_NAME, "a")
		)))		
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_NAME, "s")
		)))		
	}
	
	@Test
	def void testVariableNameAsParameterIndexer(){
		val code = '''
		public class MyClass {
			public void testMethod(String s) {
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableNameIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_NAME, "s")
		)))		
	}
	
	@Test
	def void testVariableTypeIndexer(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				String s;
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableTypeIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_TYPE, "Ljava/lang/String")
		)))		
	}
	
	@Test
	def void testVariableTypeIndexer02(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				String s, a;
			}
		}
		'''
		 
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableTypeIndexer(), new VariableNameIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_NAME, "s"),
			s(Fields::VARIABLE_TYPE, "Ljava/lang/String")
		)))		
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_NAME, "a"),
			s(Fields::VARIABLE_TYPE, "Ljava/lang/String")
		)))		
	}
	
	@Test
	def void testVariableDefinitionIndexer(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				String s;
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableDefinitionIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_DEFINITION, Fields::DEFINITION_UNINITIALIZED)
		)))		
	}
	
	@Test
	def void testVariableDefinitionIndexer02(){
		val code = '''
		public class MyClass {
			public void testMethod(String s) {
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableDefinitionIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_DEFINITION, Fields::DEFINITION_PARAMETER)
		)))		
	}
	
	@Test
	def void testVariableDefinitionIndexer03(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				Object o = new Object();
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableDefinitionIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_DEFINITION, Fields::DEFINITION_INSTANCE_CREATION)
		)))		
	}
	
	@Test
	def void testVariableDefinitionIndexer04(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				Object o = null;
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableDefinitionIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_DEFINITION, Fields::DEFINITION_NULLLITERAL)
		)))		
	}
	
	@Test
	def void testVariableDefinitionIndexer05(){
		val code = '''
		public class MyClass {
			String s1 = "";
			public void testMethod() {
				String s2 = s1.toString();
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableDefinitionIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_DEFINITION, Fields::DEFINITION_ASSIGNMENT)
		)))		
	}
	
	@Test
	def void testVariableDefinitionIndexer07(){
		val code = '''
		public class MyClass {
			public void testMethod() {
				int s2 = 1;
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableDefinitionIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_DEFINITION, Fields::DEFINITION_ASSIGNMENT)
		)))		
	}
	
	@Test
	def void testVariableDefinitionIndexer06(){
		val code = '''
		public class MyClass {
			public void testMethod() {
			String s1 = "";
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableDefinitionIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::VARIABLE_DEFINITION, Fields::DEFINITION_ASSIGNMENT)
		)))		
	}
	
	@Test
	def void testVariableUsedAsParameterIndexer(){
		val code = '''
		public class MyClass {
			public void testMethod() {
		        String s = null;
		        System.out.println(s);
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableParameterUsageIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::USED_AS_PARAMETER_IN_METHODS, "Ljava/io/PrintStream.println(Ljava/lang/String;)V|System.out")
		)))		
	}
	
	@Test
	def void testVariableUsedAsParameterIndexer02(){
		val code = '''
		public class MyClass {
			public void testMethod() {
		        String s1 = null;
		        String s2 = null;
		        s2.compareTo(s1);
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableParameterUsageIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::USED_AS_PARAMETER_IN_METHODS, "Ljava/lang/String.compareTo(Ljava/lang/String;)I|s2")
		)))		
	}
	
	@Test
	def void testVariableUsedAsTargetIndexer(){
		val code = '''
		public class MyClass {
			public void testMethod() {
		        String s = null;
				s.toString();
			}
		}
		'''
		
		exercise(code, i(newArrayList(new DocumentTypeIndexer(), new VariableTargetUsageIndexer())))
		
		assertField(l(newArrayList(
			s(Fields::TYPE, Fields::TYPE_VARUSAGE),
			s(Fields::USED_AS_TAGET_FOR_METHODS, "Ljava/lang/String.toString()Ljava/lang/String;")
		)))		
	}
}