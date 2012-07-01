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

package org.eclipse.recommenders.codesearch.rcp.index.wiring;

import org.eclipse.recommenders.codesearch.rcp.index.indexer.CodeIndexer;
import org.eclipse.recommenders.injection.InjectionService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class CodesearchIndexPlugin extends AbstractUIPlugin {

    private static CodesearchIndexPlugin INSTANCE;

    public static CodesearchIndexPlugin getDefault() {
        return INSTANCE;
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        INSTANCE = this;
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        super.stop(context);
        final CodeIndexer instance = InjectionService.getInstance().getInjector()
                .getInstance(CodeIndexer.class);
        instance.close();
    }
}
