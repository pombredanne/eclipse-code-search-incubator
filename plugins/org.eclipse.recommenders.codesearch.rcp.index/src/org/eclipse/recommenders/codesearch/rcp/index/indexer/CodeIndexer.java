/**
 * Copyright (c) 2012 Tobias Boehm.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Tobias Boehm - initial API and implementation.
 *    Kavith Thiranga - Refactorings to support new Recommenders API
 */

package org.eclipse.recommenders.codesearch.rcp.index.indexer;

import static org.eclipse.recommenders.codesearch.rcp.index.searcher.CodeSearcher.prepareSearchTerm;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.document.SetBasedFieldSelector;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.recommenders.codesearch.rcp.index.Fields;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.interfaces.IIndexer;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.strategy.IFieldIndexingStrategy;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.strategy.SimpleNameBasedStrategy;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.utils.IIndexInformationProvider;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.utils.IndexInformationCache;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.visitor.CompilationUnitVisitor;
import org.eclipse.recommenders.codesearch.rcp.index.searcher.CodeSearcher;
import org.eclipse.recommenders.codesearch.rcp.index.wiring.CodesearchIndexPlugin;
import org.eclipse.recommenders.rcp.utils.LoggingUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class CodeIndexer implements ICompilationUnitIndexer {

    private static boolean verbose = false; // XXX: Always set me back to false
                                            // please...
    
    private static final List<IIndexer> defaultIndexer = getDefaultIndexer();

    private static IFieldIndexingStrategy indexingFieldInfoProvider = new SimpleNameBasedStrategy();

    public static void setVerbose(boolean value) {
        verbose = value;
    }

    public static void setIndexingFieldInformationProvider(IFieldIndexingStrategy newIindexingFieldInfoProvider) {
        indexingFieldInfoProvider = newIindexingFieldInfoProvider;
    }

    public static void addFieldToDocument(final Document document, final String fieldName, final int fieldValue) {
        addFieldToDocument(document, fieldName, String.valueOf(fieldValue));
    }

    public static void addFieldToDocument(final Document document, final String fieldName, final String fieldValue) {
        addInternal(document, fieldName, fieldValue, indexingFieldInfoProvider.getStore(fieldName),
                indexingFieldInfoProvider.getIndex(fieldName));
    }

    private static void addInternal(final Document document, final String fieldName, final String fieldValue,
            Field.Store store, Field.Index index) {
        if (fieldValue == null)
            return;

        if (verbose) {
            // XXX: Replace with better logging
            System.out.println(String.format("Indexed field [%30s]=[%60s]", fieldName, fieldValue));
        }

        final Field field = new Field(fieldName, fieldValue, store, index);

        document.add(field);
    }

    private final IndexWriter writer;
    private final CodeSearcher searcher;
    private final List<IIndexer> tmpIndexerCollection = Lists.newArrayList();
    private final IIndexInformationProvider indexInformationProvider;

    @Inject
    public CodeIndexer(final IndexWriter writer, final CodeSearcher searcher) throws IOException {
        this.writer = writer;
        this.searcher = searcher;
        indexInformationProvider = new IndexInformationCache();
    }

    @Override
    public void index(final CompilationUnit cu) throws IOException {
        index(cu, new CodeIndexerDefaultConfigBean());
    }

    @Override
    public void index(final CompilationUnit cu, CodeIndexerConfigBean settings) throws IOException {
        index(cu, settings, defaultIndexer);
    }

    @Override
    public void index(final CompilationUnit cu, final IIndexer indexer) throws IOException {
        tmpIndexerCollection.clear();
        tmpIndexerCollection.add(indexer);
        index(cu, tmpIndexerCollection);
    }


    @Override
    public void index(final CompilationUnit cu, final List<IIndexer> indexer) throws IOException {
        index(cu, new CodeIndexerDefaultConfigBean(), indexer);
    }
    
    private void index(final CompilationUnit cu, CodeIndexerConfigBean settings, final List<IIndexer> indexer) throws IOException {
        if(settings.isDeleteDocumentFirst()) {
            delete(cu);
        }
        
        final CompilationUnitVisitor visitor = new CompilationUnitVisitor(this, settings);
        visitor.addIndexer(indexer);
        
        try {
            cu.accept(visitor);
        } catch (final Exception e) {
            LoggingUtils.logError(e, CodesearchIndexPlugin.getDefault(), "Exception while indexing %s", ResourcePathIndexer.getFile(cu));
        }
        
        // add to internal cache
        indexInformationProvider.setLastIndexed(ResourcePathIndexer.getFile(cu), TimestampIndexer.getTime());
    }
    
    @Override
    public long lastIndexed(final File location) {
        Optional<Long> lastIndexed = indexInformationProvider.getLastIndexed(location);

        if (lastIndexed.isPresent()) {
            return lastIndexed.get();
        } else {
            lastIndexed = lastIndexedInternal(location);
        }

        if (lastIndexed.isPresent()) {
            indexInformationProvider.setLastIndexed(location, lastIndexed.get());
            return lastIndexed.get();
        }

        // last update 1.1.1970 ;)
        return 0;
    }

    private Optional<Long> lastIndexedInternal(final File location) {
        try {
            final Query query = new TermQuery(prepareSearchTerm(Fields.RESOURCE_PATH,
                    ResourcePathIndexer.getPath(location)));
            final FieldSelector selector = new SetBasedFieldSelector(Sets.newHashSet(Fields.TIMESTAMP),
                    Sets.<String>newHashSet());
            final List<Document> docs = searcher.search(query, selector, 1);
            if (docs.size() > 0) {
                return getMinTimestamp(docs);
            }
        } catch (final Exception e) {
            LoggingUtils.logError(e, CodesearchIndexPlugin.getDefault(), "failed to fetch last indexed timestamp for CU from code-search index.");
        }

        return Optional.absent();
    }

    private Optional<Long> getMinTimestamp(final List<Document> documents) {
        long min = Long.MAX_VALUE;

        for (final Document doc : documents) {
            final String timestampString = doc.get(Fields.TIMESTAMP);

            try {
                final Long timestampValue = Long.parseLong(timestampString);
                if (min > timestampValue) {
                    min = timestampValue;
                }
            } catch (final Exception ex) {
            }
        }

        if (min == Long.MAX_VALUE) {
            return Optional.absent();
        }

        return Optional.of(min);
    }

    public void delete(final File location) throws IOException {
        delete(prepareSearchTerm(Fields.RESOURCE_PATH, ResourcePathIndexer.getPath(location)));
    }

    public void delete(final Term term) throws IOException {
        if (term == null || term.text() == null) {
            return;
        }

        final Query q = new TermQuery(prepareSearchTerm(term.field(), term.text().toLowerCase()));
        writer.deleteDocuments(q);
    }

    @Override
    public void delete(final CompilationUnit cu) throws IOException {
        final String cuPath = ResourcePathIndexer.getPath(cu);
        
        delete(prepareSearchTerm(Fields.RESOURCE_PATH, cuPath));
    }

    public void commit() {
        try {
            writer.commit();
        } catch (final Exception e) {
            LoggingUtils.logError(e, CodesearchIndexPlugin.getDefault(), "failed to commit latest changes to code-search index.");
        }
    }

    public void compact(final boolean wait) {
        try {
            writer.forceMerge(10, true);
            writer.commit();
        } catch (final Exception e) {
            LoggingUtils.logError(e, CodesearchIndexPlugin.getDefault(), "failed to compact code-search index.");
        }
    }

    /**
     * Adds a document to the index without previously checking and
     * deleting old versions of the "same" document in the index.
     */
    public void addDocument(final Document d) throws IOException {
        writer.addDocument(d);
    }

    @Override
    public void truncateIndex() {
        try {
            writer.deleteAll();
            writer.commit();
        } catch (final IOException e) {
            LoggingUtils.logError(e, CodesearchIndexPlugin.getDefault(), "failed to truncate code-search index.");
        }
    }

    public void printStats() {
        // XXX: Mach was
        // try {
        // Activator.logInfo("Stat - Docs in Index: " +
        // // m_writer.numDocs());
        // } catch (final IOException e) {
        // // XXX: Activator.logError(e);
        // }
    }

    @Override
    public void close() {
        try {
            commit();

            writer.close();
        } catch (final Exception ex) {
            LoggingUtils.logError(ex, CodesearchIndexPlugin.getDefault(), "failed to close code-search index.");
        }
    }

    public void addDocuments(final List<Document> docs) throws IOException {
        for (final Document doc : docs) {
            addDocument(doc);
        }
    }
    
    public static List<IIndexer> getDefaultIndexer() {

        final List<IIndexer> list = Lists.newArrayList();

        list.add(new AllDeclaredFieldNamesIndexer());
        list.add(new AllDeclaredMethodNamesIndexer());
        list.add(new AllExtendedTypesIndexer());
        list.add(new AllImplementedInterfacesIndexer());
        list.add(new AnnotationsIndexer());
        list.add(new CaughtTypeIndexer());
        list.add(new CheckedExceptionsIndexer());
        list.add(new DeclaredFieldNamesIndexer());
        list.add(new DeclaredFieldTypesIndexer());
        list.add(new DeclaredMethodNamesIndexer());
        list.add(new DeclaredMethodsIndexer());
        list.add(new DeclaringMethodIndexer());
        list.add(new DeclaringTypeIndexer());
        list.add(new DocumentTypeIndexer());
        list.add(new ExtendedTypeIndexer());
        list.add(new FieldsReadIndexer());
        list.add(new FieldsWrittenIndexer());
        list.add(new FieldTypeIndexer());
        list.add(new SimpleNameIndexer());
        list.add(new FullTextIndexer());
        list.add(new FullTextIndexer2());
        list.add(new QualifiedNameIndexer());
        list.add(new ImplementedInterfacesIndexer());
        list.add(new InstanceOfIndexer());
        list.add(new ModifiersIndexer());
        list.add(new OverriddenMethodsIndexer());
        list.add(new ParameterCountIndexer());
        list.add(new ParameterTypesIndexer());
        list.add(new ParameterTypesStructuralIndexer());
        list.add(new ProjectNameIndexer());
        list.add(new ResourcePathIndexer());
        list.add(new ReturnTypeIndexer());
        // list.add(new ReturnVariableExpressionIndexer());
        list.add(new TimestampIndexer());
        list.add(new UsedFieldsInFinallyIndexer());
        list.add(new UsedFieldsInTryIndexer());
        list.add(new UsedMethodsIndexer());
        list.add(new UsedMethodsInFinallyIndexer());
        list.add(new UsedMethodsInTryIndexer());
        list.add(new UsedTypesIndexer());
        list.add(new UsedTypesInFinallyIndexer());
        list.add(new UsedTypesInTryIndexer());
        list.add(new VariableDefinitionIndexer());
        list.add(new VariableNameIndexer());
        list.add(new VariableParameterUsageIndexer());
        list.add(new VariableTargetUsageIndexer());
        list.add(new VariableTypeIndexer());
        list.add(new JavaElementHandleIndexer());
        return list;
    }
}
