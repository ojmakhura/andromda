package org.andromda.android.core.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.andromda.android.core.model.IEditorModel;
import org.andromda.android.core.model.IModelChangeProvider;
import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.core.model.IModelChangedListener;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

/**
 * This abstract base class provides methods for dealing with model changes.
 * 
 * @author Peter Friese
 * @since 21.12.2005
 */
public abstract class AbstractEditorModel
        implements IEditorModel, IModelChangeProvider
{

    /** Parties that are interested in changes that happen to this model. */
    private Collection listeners;
    
    /** The editor document. */
    private final IDocument document;
    
    /** This document listener take care of updating the model whenever the document has changed. */
    private IDocumentListener documentListener = new IDocumentListener() {
        
        public void documentAboutToBeChanged(final DocumentEvent event)
        {
            // do nothing
        }

        public void documentChanged(final DocumentEvent event)
        {
            parseDocument(event.getDocument());
            fireModelChanged(null);
        }

    };    

    /**
     * Creates a new model.
     * 
     * @param document the editor document to be wrapped.
     */
    public AbstractEditorModel(final IDocument document)
    {
        this.document = document;
        subscribeToDocument(document);
        parseDocument(document);
        listeners = Collections.synchronizedList(new ArrayList());
    }

    /**
     * {@inheritDoc}
     */
    public void addModelChangedListener(final IModelChangedListener listener)
    {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void fireModelChanged(final IModelChangedEvent event)
    {
        IModelChangedListener[] list = (IModelChangedListener[])listeners.toArray(new IModelChangedListener[listeners
                .size()]);
        for (int i = 0; i < list.length; i++)
        {
            IModelChangedListener listener = list[i];
            listener.modelChanged(event);
        }
        
        document.removeDocumentListener(documentListener);
        updateDocument();
        document.addDocumentListener(documentListener);
    }

    /**
     * {@inheritDoc}
     */
    public void fireModelObjectChanged(final Object object,
        final String property,
        final Object oldValue,
        final Object newValue)
    {
        fireModelChanged(new ModelChangedEvent(this, object, property, oldValue, newValue));
    }

    /**
     * {@inheritDoc}
     */
    public void removeModelChangedListener(final IModelChangedListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Subscribes to change events on the <code>document</code>.
     *
     * @param document the document we want to subscribe to.
     */
    protected void subscribeToDocument(final IDocument document)
    {
        document.addDocumentListener(documentListener);
    }

    /**
     * @return Returns the document.
     */
    protected IDocument getDocument()
    {
        return document;
    }
    
    /**
     * Parses the document and updates the model.
     *
     * @param document the document to parse.
     */
    protected abstract void parseDocument(IDocument document);
    
    /**
     * Updates the editor document with the contents of the model. Subclasses have to implement this method.
     */
    protected abstract void updateDocument();
    
}
