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

package org.eclipse.recommenders.codesearch.rcp.index;

public class Fields {
	public final static String TYPE_CLASS = "type";
	public final static String TYPE_METHOD = "method";
	public final static String TYPE_FIELD = "field";
	public final static String TYPE_TRYCATCH = "trycatch";
	public final static String TYPE_VARUSAGE = "varusage";
	
	public static final String MODIFIER_PUBLIC = "public";
	public static final String MODIFIER_FINAL = "final";
	public static final String MODIFIER_PRIVATE = "private";
	public static final String MODIFIER_PROTECTED = "protected";
	public static final String MODIFIER_STATIC = "static";
	public static final String MODIFIER_ABSTRACT = "abstract";
	
	public static final String DEFINITION_PARAMETER = "parameter";
	public static final String DEFINITION_NULLLITERAL = "nullLiteral";
	public static final String DEFINITION_ASSIGNMENT = "assignment";
	public static final String DEFINITION_INSTANCE_CREATION = "instanceCreation";
	public static final String DEFINITION_UNINITIALIZED = "uninitialized";
	
	/**
     * Java handle used to open a given java element in an editor.
     */
    public static final String JAVA_ELEMENT_HANDLE = "Handle";
    
	// SimpleField
	/** The unique represantation of the entity. Abbreviated as \cquote{FQN}<br /><br />
		Can be applied to: type, method, field, tryCatch
	*/
	//Generated - please modify in source file
	public final static String QUALIFIED_NAME = "FullyQualifiedName";
	/** Simple version of the entity's name<br /><br />
		Can be applied to: type, method, field, tryCatch
	*/
	//Generated - please modify in source file
	public final static String SIMPLE_NAME = "FriendlyName";
	/** Expression returned by a method, e.g, "return true;"<br /><br />
		Can be applied to: method
	*/
	//Generated - please modify in source file
	public final static String RETURN_VARIABLE_EXPRESSIONS = "ReturnVariableExpressions";
	/** Simple versions of all declared methods within, across the entire hierarchy<br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String ALL_DECLARED_METHOD_NAMES = "AllDeclaredMethodNames";
	/** Simple versions of all declared methods within<br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String DECLARED_METHODS_NAMES = "DeclaredMethodNames";
	/** Simple names of all declared fields<br /><br />
		Can be applied to: type, method, tryCatch
	*/
	//Generated - please modify in source file
	public final static String DECLARED_FIELD_NAMES = "DeclaredFieldNames";
	/** Simple names of all declared fields, across the entire hierarchy<br /><br />
		Can be applied to: type, method, tryCatch
	*/
	//Generated - please modify in source file
	public final static String ALL_DECLARED_FIELD_NAMES = "AllDeclaredFieldNames";
	/** Full text representation of the entire entity<br /><br />
		Can be applied to: type, method, field, tryCatch
	*/
	//Generated - please modify in source file
	public final static String FULL_TEXT = "FullText";
	/** The FQNs of the fields read within the entity. E.g, \cvalue{SomeType.someField}<br /><br />
		Can be applied to: type, method, tryCatch
	*/
	//Generated - please modify in source file
	public final static String FIELDS_READ = "FieldsRead";
	/** The FQNs of the fields written within the entity. E.g, \cvalue{SomeType.someField}<br /><br />
		Can be applied to: method, tryCatch
	*/
	//Generated - please modify in source file
	public final static String FIELDS_WRITTEN = "FieldsWritten";
	/** The FQNs of the fields used within the finally-block. E.g, \cvalue{SomeType.someField}<br /><br />
		Can be applied to: tryCatch
	*/
	//Generated - please modify in source file
	public final static String USED_FIELDS_IN_FINALLY = "UsedFieldsInFinally";
	/** The FQNs of the fields used within the try-block. E.g, \cvalue{SomeType.someField}<br /><br />
		Can be applied to: tryCatch
	*/
	//Generated - please modify in source file
	public final static String USED_FIELDS_IN_TRY = "UsedFieldsInTry";
	/** Variable name of a variable usage<br /><br />
		Can be applied to: varusage
	*/
	//Generated - please modify in source file
	public final static String VARIABLE_NAME = "VariableName";
	/** Textual representation of a methods parameter list used \textbf{internally} by MethodPatternQL (see \ref{sec:MethodPatternQL})<br /><br />
		Can be applied to: method
	*/
	//Generated - please modify in source file
	public final static String PARAMETER_TYPES_STRUCTURAL = "ParameterTypesStructural";
	/** <br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String ANNOTATIONS = "Annotations";
	
	
	// TypeField
	/** FQNs of types (interfaces) implemented by the entity<br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String IMPLEMENTED_TYPES = "ImplementedTypes";
	/** FQN of the type the class has extended<br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String EXTENDED_TYPE = "ExtendedTypes";
	/** Contains FQNs of all types used within the entity<br /><br />
		Can be applied to: type, method, field, tryCatch
	*/
	//Generated - please modify in source file
	public final static String USED_TYPES = "UsedTypes";
	/** Contains FQNs of all types used within the try-block<br /><br />
		Can be applied to: tryCatch
	*/
	//Generated - please modify in source file
	public final static String USED_TYPES_IN_TRY = "UsedTypesInTry";
	/** Contains FQNs of all types used within the finally-block<br /><br />
		Can be applied to: tryCatch
	*/
	//Generated - please modify in source file
	public final static String USED_TYPES_IN_FINALLY = "UsedTypesInFinally";
	/** Contains the FQNs of all types used in the parameter list of the method<br /><br />
		Can be applied to: method
	*/
	//Generated - please modify in source file
	public final static String PARAMETER_TYPES = "ParameterTypes";
	/** Contains the FQN of type returned by the method<br /><br />
		Can be applied to: method
	*/
	//Generated - please modify in source file
	public final static String RETURN_TYPE = "ReturnType";
	/** Contains the FQNs of all types used in \cquote{instanceof} checks within the entity<br /><br />
		Can be applied to: type, method, tryCatch
	*/
	//Generated - please modify in source file
	public final static String INSTANCEOF_TYPES = "InstanceofTypes";
	/** FQNs of types (interfaces) implemented by the entity across the entire hierarchy<br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String ALL_IMPLEMENTED_TYPES = "AllImplementedTypes";
	/** FQNs of the types the class has extended across the entire hierarchy<br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String ALL_EXTENDED_TYPES = "AllExtendedTypes";
	/** FQN of the field's type<br /><br />
		Can be applied to: field
	*/
	//Generated - please modify in source file
	public final static String FIELD_TYPE = "FieldType";
	/** FQN of the catch-block's caught type<br /><br />
		Can be applied to: tryCatch
	*/
	//Generated - please modify in source file
	public final static String CAUGHT_TYPE = "CaughtType";
	/** Contains FQNs of the types declared within the entity<br /><br />
		Can be applied to: type, method, tryCatch
	*/
	//Generated - please modify in source file
	public final static String DECLARED_FIELD_TYPES = "DeclaredFieldTypes";
	/** The FQN of the entity's declaring type, e.g., a methods class or a catch-blocks method<br /><br />
		Can be applied to: method, field, tryCatch
	*/
	//Generated - please modify in source file
	public final static String DECLARING_TYPE = "DeclaringType";
	/** The FQN of the used variable's type<br /><br />
		Can be applied to: varusage
	*/
	//Generated - please modify in source file
	public final static String VARIABLE_TYPE = "VariableType";
	/** The FQN of the checked exceptions possibly thrown by a method<br /><br />
		Can be applied to: method
	*/
	//Generated - please modify in source file
	public final static String CHECKED_EXCEPTIONS = "CheckedExceptions";
	
	
	// MethodField
	/** <br /><br />
		Can be applied to: type, method, tryCatch
	*/
	//Generated - please modify in source file
	public final static String USED_METHODS = "UsedMethods";
	/** <br /><br />
		Can be applied to: tryCatch
	*/
	//Generated - please modify in source file
	public final static String USED_METHODS_IN_TRY = "UsedMethodsInTry";
	/** <br /><br />
		Can be applied to: tryCatch
	*/
	//Generated - please modify in source file
	public final static String USED_METHODS_IN_FINALLY = "UsedMethodsInFinally";
	/** <br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String OVERRIDDEN_METHODS = "OverriddenMethods";
	/** <br /><br />
		Can be applied to: type
	*/
	//Generated - please modify in source file
	public final static String DECLARED_METHODS = "DeclaredMethods";
	/** <br /><br />
		Can be applied to: varusage
	*/
	//Generated - please modify in source file
	public final static String DECLARING_METHOD = "DeclaringMethod";
	/** <br /><br />
		Can be applied to: varusage
	*/
	//Generated - please modify in source file
	public final static String USED_AS_PARAMETER_IN_METHODS = "UsedAsParameterInMethods";
	/** <br /><br />
		Can be applied to: varusage
	*/
	//Generated - please modify in source file
	public final static String USED_AS_TAGET_FOR_METHODS = "UsedAsTargetForMethods";
	
	
	// FilePathField
	/** Local file path to the current entity's source code file<br /><br />
		Can be applied to: type, method, field, tryCatch
	*/
	//Generated - please modify in source file
	public final static String RESOURCE_PATH = "ResourcePath";
	
	
	// NumberField
	/** Number of parameters that are defined in a method's parameter list<br /><br />
		Can be applied to: method
	*/
	//Generated - please modify in source file
	public final static String PARAMETER_COUNT = "ParameterCount";
	/** Timestamp of the document's entity's last indexing time. Measured in milliseconds since January 1, 1970<br /><br />
		Can be applied to: type, method, field, tryCatch
	*/
	//Generated - please modify in source file
	public final static String TIMESTAMP = "Timestamp";
	
	
	// ModifierField
	/** Modifiers that are defined for the entity<br /><br />
		Can be applied to: type, method, field
	*/
	//Generated - please modify in source file
	public final static String MODIFIERS = "Modifiers";
	
	
	// DocumentTypeField
	/** Defines the type of the entity represented by the document.<br /><br />
		Can be applied to: type, method, field, tryCatch, varusage
	*/
	//Generated - please modify in source file
	public final static String TYPE = "Type";
	
	
	// ProjectNameField
	/** Name of the project the entity's source code file is a part of<br /><br />
		Can be applied to: type, method, field, tryCatch
	*/
	//Generated - please modify in source file
	public final static String PROJECT_NAME = "ProjectName";
	
	
	// DefinitionType
	/** The way a variable of a variable usage has been declared<br /><br />
		Can be applied to: varusage
	*/
	//Generated - please modify in source file
	public final static String VARIABLE_DEFINITION = "VariableDefinition";
	
	
}
