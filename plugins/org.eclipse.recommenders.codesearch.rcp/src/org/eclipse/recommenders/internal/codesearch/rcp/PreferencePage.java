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

package org.eclipse.recommenders.internal.codesearch.rcp;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexer;
import org.eclipse.recommenders.injection.InjectionService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    
    private static final String P_KEEP_IN_SYNC = "recommenders.codesearch.index.keep_in_sync";

    @Override
    protected void createFieldEditors() {
        final Composite parent = getFieldEditorParent();
        addField(new BooleanFieldEditor(P_KEEP_IN_SYNC, "Keep index in sync with workspace.", parent));
        final Label label = new Label(parent, SWT.WRAP | SWT.BORDER);
        final GridData data = GridDataFactory.fillDefaults().grab(true, true).span(2, 1).hint(100, 300).create();
        data.horizontalSpan = 2;
        data.widthHint = 200;
        data.grabExcessHorizontalSpace = true;
        label.setLayoutData(data);

        label.setText("Note that building the initial index is a long running operation. "
                + "Its duration depends on workspace size and number project dependencies and "
                + "may take up to 30 minutes.\n\nThis operation can be canceled at any time; "
                + "Indexing operation starts from the last sucessfully indexed project.");
    }

    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(CodesearchIndexPlugin.getDefault().getPreferenceStore());
    }

    public static boolean isActive() {
        final CodesearchIndexPlugin plugin = CodesearchIndexPlugin.getDefault();
        final IPreferenceStore store = plugin.getPreferenceStore();
        final boolean res = store.getBoolean(P_KEEP_IN_SYNC);
        return res;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {       
        super.propertyChange(event);
        
        if(event.getSource() instanceof BooleanFieldEditor && 
                ((BooleanFieldEditor) event.getSource()).getPreferenceName().equals(P_KEEP_IN_SYNC)){
            
            if((Boolean) event.getNewValue()){

              boolean confirmed = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), 
                                    "Warning - Long running operation", 
                                    "Note that building the initial index is a long running operation. "
                                    + "Press OK to start indexing now.\nOtherwise it will be started on next restart."); 
              IndexUpdateService service = InjectionService.getInstance().getInjector()
                      .getInstance(IndexUpdateService.class);
              
              if(confirmed){
                  service.reindexWorkspace();
              }
            }
        }
    }
    
    
}
