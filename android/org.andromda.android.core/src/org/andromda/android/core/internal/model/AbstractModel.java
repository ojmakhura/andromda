package org.andromda.android.core.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.andromda.android.core.model.IModel;
import org.andromda.android.core.model.IModelChangeProvider;
import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.core.model.IModelChangedListener;

/**
 * This abstract base class provides methods for dealing with model changes.
 * 
 * @author Peter Friese
 * @since 21.12.2005
 */
public class AbstractModel
        implements IModel, IModelChangeProvider
{

    /** Parties that are interested in changes that happen to this model. */
    private Collection listeners;

    /**
     * Creates a new model.
     */
    public AbstractModel()
    {
        super();
        listeners = Collections.synchronizedList(new ArrayList());
    }

    /**
     * @see org.andromda.android.core.model.IModelChangeProvider#addModelChangedListener(org.andromda.android.core.model.IModelChangedListener)
     */
    public void addModelChangedListener(IModelChangedListener listener)
    {
        listeners.add(listener);
    }

    /**
     * @see org.andromda.android.core.model.IModelChangeProvider#fireModelChanged(org.andromda.android.core.model.IModelChangedEvent)
     */
    public void fireModelChanged(IModelChangedEvent event)
    {
        IModelChangedListener[] list = (IModelChangedListener[])listeners.toArray(new IModelChangedListener[listeners
                .size()]);
        for (int i = 0; i < list.length; i++)
        {
            IModelChangedListener listener = list[i];
            listener.modelChanged(event);
        }
    }

    /**
     * @see org.andromda.android.core.model.IModelChangeProvider#fireModelObjectChanged(java.lang.Object,
     *      java.lang.String, java.lang.Object, java.lang.Object)
     */
    public void fireModelObjectChanged(Object object,
        String property,
        Object oldValue,
        Object newValue)
    {
        fireModelChanged(new ModelChangedEvent(this, object, property, oldValue, newValue));
    }

    /**
     * @see org.andromda.android.core.model.IModelChangeProvider#removeModelChangedListener(org.andromda.android.core.model.IModelChangedListener)
     */
    public void removeModelChangedListener(IModelChangedListener listener)
    {
        listeners.remove(listener);
    }

}
