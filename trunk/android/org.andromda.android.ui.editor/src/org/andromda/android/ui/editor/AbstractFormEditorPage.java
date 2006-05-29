package org.andromda.android.ui.editor;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * A base class that all pages that should be added to the
 * {@link org.andromda.android.ui.editor.AbstractMultipageSourceEditor} must subclass.
 * 
 * This class provides access to the document currently being edited in the hosting multi page source editor. It also
 * ensures a two-way synchronization between the model and the form's components.
 * 
 * @author Peter Friese
 * @since 06.01.2006
 */
public abstract class AbstractFormEditorPage
        extends FormPage
{

    /** Signals that the model is currently being updated. */
    private boolean modelUpdating;

    /**
     * This modification listener should be attached to input elements in order to automatically signal that the model
     * needs to be updated.
     */
    private KeyAdapter modificationListener;

    /**
     * Create the form page.
     * 
     * @param id the id of the page.
     * @param title the title of the page.
     */
    public AbstractFormEditorPage(final String id,
        final String title)
    {
        super(id, title);
    }

    /**
     * Create the form page.
     * 
     * @param editor the owning editor.
     * @param id the id of the page.
     * @param title the title of the page.
     */
    public AbstractFormEditorPage(final FormEditor editor,
        final String id,
        final String title)
    {
        super(editor, id, title);
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
     */
    protected void setInput(IEditorInput input)
    {
        super.setInput(input);
        IDocument document = getDocumentProvider().getDocument(input);
        setupDocument(document);
    }

    /**
     * Adds a document listener to the document so that we will be informed about changes to the document.
     * 
     * @param document the document we want to listen to.
     */
    private void setupDocument(final IDocument document)
    {
        document.addDocumentListener(new IDocumentListener()
        {
            public void documentAboutToBeChanged(final DocumentEvent event)
            {
            }

            public void documentChanged(final DocumentEvent event)
            {
                if (!modelUpdating)
                {
                    updatePage();
                }
            }

        });
    }

    /**
     * This method will be called whenever the document has changed. The method will then call
     * <code>doUpdatePage()</code>, giving subclasses the chance to perform the update of the page and its widgets
     * themselves.
     */
    private void updatePage()
    {
        doUpdatePage(getDocument());
    }

    /**
     * Subclasses must implement this method in order to update the widgets with the contents of the document.
     * 
     * @param document the document with the changes already applied.
     */
    protected abstract void doUpdatePage(IDocument document);

    /**
     * This method will be called whenever the user has changed the content of the widgets on this form. It will then
     * call <code>doUpdateModel()</code> giving subclasses the chance to perform the update of the document
     * themselves.
     */
    private void updateModel()
    {
        modelUpdating = true;
        doUpdateDocument(getDocument());
        modelUpdating = false;
    }

    /**
     * Subclasses must implement this method in order to update the model with the contents of the widgets.
     * 
     * @param document the document that has to be updated.
     */
    protected abstract void doUpdateDocument(IDocument document);

    /**
     * Retrieve the document provider of the attached editor.
     * 
     * @return the document provider of the source editor of the multi page editor this page is hosted on.
     */
    protected IDocumentProvider getDocumentProvider()
    {
        return (IDocumentProvider)getEditor().getAdapter(IDocumentProvider.class);
    }

    /**
     * Retrieve the currenty edited document.
     * 
     * @return the document currently being edited.
     */
    protected IDocument getDocument()
    {
        return getDocumentProvider().getDocument(getEditorInput());
    }

    /**
     * Lazily instantiates a key listener that can be added to the input elements of the editor. This key listener will
     * fire the model updating mechanism on the form editor page.
     * 
     * @return a key listener.
     */
    protected KeyListener getKeyListener()
    {
        if (modificationListener == null)
        {
            modificationListener = new KeyAdapter()
            {
                public void keyReleased(final KeyEvent e)
                {
                    AbstractFormEditorPage.this.updateModel();
                }
            };
        }
        return modificationListener;
    }

}
