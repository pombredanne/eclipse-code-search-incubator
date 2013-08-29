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

import java.util.List
import org.apache.lucene.document.Document
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer
import org.eclipse.recommenders.codesearch.rcp.index.indexer.visitor.CompilationUnitVisitor
import org.eclipse.recommenders.codesearch.rcp.index.searcher.CodeSearcher
import org.eclipse.recommenders.internal.codesearch.rcp.IndexUpdateService
import org.eclipse.recommenders.tests.jdt.JavaProjectFixture
import org.junit.Ignore

import static junit.framework.Assert.*

@Ignore("to make maven happy: All files that start or end with Test are executed per default. If no tests are found the build is failed...")
class TestBase extends AbstractTestBase {

    public LuceneInMemoryFixture f = new LuceneInMemoryFixture()
    public CodeIndexer index = f.index
    public CodeSearcher search = f.searcher

    def static parse(ICompilationUnit unit) {
        var parser = ASTParser::newParser(AST::JLS3);
        parser.setKind(ASTParser::K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);

        parser.createAST(null);
    }

    def assertNumDocs(int expectedNum) {
        var numDocs = search.getDocuments.size

        assertTrue('''The number of documents is not correct. Is [«numDocs»] but should be [«expectedNum»]'''.toString,
            numDocs.equals(expectedNum))
    }

    def assertField(List<String> expected) {

        for (document : search.getDocuments) {
            var foundInDocument = true

            for (exp : expected) {
                var found = false

                for (field : document.fields) {
                    if (s(field.name, field.stringValue).equals(exp)) {
                        found = true;
                    }
                }

                if (!found) {
                    foundInDocument = false
                }
            }

            if (foundInDocument) {
                return true
            }
        }

        assertTrue(
            '''There was no document with «expected»
		
		Documents present:
		«getAllDocsAsString(search.getDocuments)»'''.toString, false)

        return false
    }

    def assertFieldStartsWith(List<String> expected) {
        for (document : search.getDocuments) {
            var foundInDocument = true

            for (exp : expected) {
                var found = false

                for (field : document.fields) {
                    if (s(field.name, field.stringValue).startsWith(exp)) {
                        found = true;
                    }
                }

                if (!found) {
                    foundInDocument = false
                }
            }

            if (foundInDocument) {
                return true
            }
        }

        assertTrue(
            '''There was no document with (startswith) «expected»
		
		Documents present:
		«getAllDocsAsString(search.getDocuments)»'''.toString, false)
        return false
    }

    def getAllDocsAsString(List<Document> docs) {

        var docsFound = '''''';
        for (document : search.getDocuments) {
            docsFound = '''«docsFound»
    		«document.toString»'''
        }

        return docsFound
    }

    def assertNotField(List<String> expected) {
        for (document : search.getDocuments) {
            var foundInDocument = true

            for (exp : expected) {
                var found = false

                for (field : document.fields) {
                    if (s(field.name, field.stringValue).equals(exp)) {
                        found = true;
                    }
                }

                if (!found) {
                    foundInDocument = false
                }
            }

            if (foundInDocument) {
                assertTrue('''There was a document with «expected»'''.toString, false)
            }
        }
    }

    def s(String name, String value) {
        return '''«name»=«value»'''.toString
    }

    def exercise(CharSequence code, List<IIndexer> indexer) {
        exercise(code, indexer, "test")
    }

    def exercise(CharSequence code, List<IIndexer> indexer, String projectName) {
        exercise(code, indexer, projectName, "MyClass.java")
    }

    def exercise(CharSequence code, List<IIndexer> indexer, String projectName, String fileName) {
        exercise(code, null, null, indexer, projectName, fileName)
    }

    def exercise(CharSequence code1, CharSequence code2, CharSequence code3, List<IIndexer> indexer, String projectName,
        String fileName) {
        IndexUpdateService::setBackgroundIndexerActive(false) // To prevent workspace events from being processed

        //CodeIndexer::setVerbose(true)
        val fixture = new JavaProjectFixture(ResourcesPlugin::getWorkspace(), projectName)
        val struct = fixture.createFileAndParseWithMarkers(code1.toString)
        val cu = struct.first;
        var cuParsed = parse(cu);

        var visitor = new CompilationUnitVisitor(index);
        visitor.addIndexer(indexer);

        cuParsed.accept(visitor)
        index.commit

        return index
    }

    def exercise(CharSequence code, IIndexer indexer) {
        exercise(code, i(newArrayList(indexer)))
    }

    //	def c(String [] items) {
    //		return StringUtils::concatWith(", ", items)
    //	}
    def l(String[] items) {
        return items as List<String>
    }

    def i(IIndexer[] items) {
        return items as List<IIndexer>
    }
}
