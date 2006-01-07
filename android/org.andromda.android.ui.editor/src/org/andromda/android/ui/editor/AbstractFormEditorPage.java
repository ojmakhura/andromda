package org.andromda.android.ui.editor;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
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

    private boolean modelUpdating;

    private KeyAdapter modificationListener;

    private FocusAdapter focusListener;

    /**
     * Create the form page
     *
     * @param id
     * @param title
     */
    public AbstractFormEditorPage(String id,
        String title)
    {
        super(id, title);
    }

    /**
     * Create the form page
     *
     * @param editor
     * @param id
     * @param title
     */
    public AbstractFormEditorPage(FormEditor editor,
        String id,
        String title)
    {
        super(editor, id, title);
    }

    /**
     * Create contents of the form
     *
     * @param managedForm
     */
    protected void createFormContent(IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Empty FormPage");
        Composite body = form.getBody();
        body.setLayout(new GridLayout());
        toolkit.paintBordersFor(body);
    }

    /**
     * @see org.eclipse.ui.forms.editor.FormPage#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    public void init(IEditorSite site,
        IEditorInput input)
    {
        super.init(site, input);
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
    private void setupDocument(IDocument document)
    {
        document.addDocumentListener(new IDocumentListener()
        {
            public void documentAboutToBeChanged(DocumentEvent event)
            {
            }

            public void documentChanged(DocumentEvent event)
            {
                if (!modelUpdating)
                {
                    updateWidgets();
                }
            }

        });
    }

    /**
     * This method will be called whenever the document has changed. The method will then call <code>doUpdateWidgets()</code>,
     * giving subclasses the chance to perform the update of the widgets themselves.
     */
    private void updateWidgets()
    {
        doUpdateWidgets(getDocument());
    }

    /**
     * Subclasses must implement this method in order to update the widgets with the contents of the document.
     *
     * @param document the document with the changes already applied.
     */
    protected abstract void doUpdateWidgets(IDocument document);

    /**
     * This method will be called whenever the user has changed the content of the widgets on this form. It will then
     * call <code>doUpdateModel()</code> giving subclasses the chance to perform the update of the document themselves.
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
     * @return te document currently being edited.
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
                public void keyReleased(KeyEvent e)
                {
                    AbstractFormEditorPage.this.updateModel();
                }
            };
        }
        return modificationListener;
    }

    protected FocusListener getFocusListener()
    {
        if (focusListener == null)
        {
            focusListener = new FocusAdapter()
            {
                public void focusLost(FocusEvent e)
                {
                }
            };
        }
        return focusListener;
    }

}
