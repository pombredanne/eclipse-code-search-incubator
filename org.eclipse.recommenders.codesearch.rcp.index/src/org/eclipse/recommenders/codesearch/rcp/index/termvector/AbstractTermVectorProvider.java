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

package org.eclipse.recommenders.codesearch.rcp.index.termvector;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class AbstractTermVectorProvider implements ITermVectorProvider {
    private boolean done = false;
    private List<String> termVector = null;

    public boolean isDone() {
        return done;
    }

    protected void setDone(final boolean done) {
        this.done = done;
    }

    public List<String> getTermVector() {
        return termVector;
    }

    protected void setTermVector(final List<String> termVector) {
        this.termVector = termVector;
    }

    @Override
    public List<String> getDisjunctTermVector() {
        return getTermVector();
    }

    protected abstract String[] getFields();

    @Override
    public void load(final ITermVectorConsumable consumable, final Map<Integer, Object> argumentsMap) {
        final Set<String> result = Sets.newHashSet();

        final Set<String> types = consumable.getTermVector(getFields());
        result.addAll(types);

        setTermVector(Lists.newArrayList(result));
        setDone(true);
    }

    @Override
    public boolean doneLoading() {
        return isDone();
    }
}
