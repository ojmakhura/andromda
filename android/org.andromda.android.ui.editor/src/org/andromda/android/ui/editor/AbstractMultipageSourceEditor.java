package org.andromda.android.ui.editor;

import org.andromda.android.ui.EditorUIPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.osgi.framework.Bundle;

/**
 * Abstract forms editor with a source page.
 *
 * @author Peter Friese
 * @since 05.01.2006
 */
public abstract class AbstractMultipageSourceEditor
        extends FormEditor
{

    /** The source editor. */
    private TextEditor editor;

    /** The page index of the source editor. */
    private int sourcePageIndex;

    /**
     * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
     */
    protected void addPages()
    {
        try
        {
            addSourcePage();
            addFormPages();
        }
        catch (PartInitException e)
        {
            EditorUIPlugin.log(e);
        }
    }

    /**
     * @throws PartInitException if one or more pages could not be added.
     */
    protected abstract void addFormPages() throws PartInitException;

    /**
     * @see org.eclipse.ui.forms.editor.FormEditor#addPage(org.eclipse.ui.forms.editor.IFormPage)
     */
    public int addPage(final IFormPage page) throws PartInitException
    {
        int index = getPageCount() - 1;
        addPage(index, page);
        return index;
    }

    /**
     * Creates the source page.
     *
     * @throws PartInitException in case the source page could not be initialized correctly
     */
    private void addSourcePage() throws PartInitException
    {
        sourcePageIndex = addPage(getSourceEditor(), getEditorInput());
        setPageText(sourcePageIndex, "Source");

        getSourceEditor().setInput(getEditorInput());
    }

    /**
     * @return the source editor
     */
    private TextEditor getSourceEditor()
    {
        if (editor == null)
        {
            editor = instantiateSourceEditor();
        }
        return editor;
    }

    /**
     * Finds out whether the XML source editor plug-in is available.
     *
     * @return <code>true</code> if the XML editor plug-in is abailable, <code>false</code> if not.
     */
    private boolean isXMLEditorAvailable()
    {
        Bundle bundle = Platform.getBundle("org.eclipse.wst.sse.ui");
        return bundle != null;
    }

    /**
     * Instantiates the source editor. If the XML source editor is available, it will be instantiated. If no XML source
     * editor is available, the default TextEditor will be instantiated.
     *
     * @return either the XML source editor or the TextEditor.
     */
    protected TextEditor instantiateSourceEditor()
    {
        TextEditor result = null;
        if (isXMLEditorAvailable())
        {
            result = instantiateXMLEditor();
        }

        if (result == null)
        {
            result = new TextEditor();
        }

        return result;
    }

    /**
     * Instantiates the XML source editor. This is done using reflection in order to avoid importing the needed type. If
     * the type cannot be found, <code>null</code> will bed returned.
     *
     * @return an instance of {@link org.eclipse.wst.sse.ui.StructuredTextEditor}
     */
    protected TextEditor instantiateXMLEditor()
    {
        TextEditor result = null;
        try
        {
            Class clazz = Class.forName("org.eclipse.wst.sse.ui.StructuredTextEditor");
            result = (TextEditor)clazz.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            EditorUIPlugin.log(e);
        }
        catch (InstantiationException e)
        {
            EditorUIPlugin.log(e);
        }
        catch (IllegalAccessException e)
        {
            EditorUIPlugin.log(e);
        }
        return result;
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(final IProgressMonitor monitor)
    {
        getSourceEditor().doSave(monitor);
        editorDirtyStateChanged();
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public void doSaveAs()
    {
        getSourceEditor().doSaveAs();
        editorDirtyStateChanged();
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed()
    {
        return getSourceEditor().isSaveAsAllowed();
    }

    /**
     * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
     */
    public Object getAdapter(final Class adapter)
    {
        // get the document provider by asking the source editor
        if (IDocumentProvider.class.equals(adapter))
        {
            TextEditor sourceEditor = getSourceEditor();
            if (sourceEditor != null)
            {
                return sourceEditor.getDocumentProvider();
            }
            return null;
        }

        // get the document by asking the source editor
        if (IDocument.class.equals(adapter))
        {

            IDocumentProvider documentProvider = (IDocumentProvider)getAdapter(IDocumentProvider.class);
            if (documentProvider != null)
            {
                return documentProvider.getDocument(getEditorInput());
            }
        }
        return super.getAdapter(adapter);
    }

    /**
     * Retrieves the document.
     *
     * @return the document being edited.
     */
    public IDocument getDocument()
    {
        return (IDocument)getAdapter(IDocument.class);
    }

}
