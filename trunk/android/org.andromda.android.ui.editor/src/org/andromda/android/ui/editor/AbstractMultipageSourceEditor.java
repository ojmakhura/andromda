package org.andromda.android.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * Abstract forms editor with a source page.
 *
 * @author Peter Friese
 * @since 05.01.2006
 */
public abstract class AbstractMultipageSourceEditor
        extends FormEditor
{

    private TextEditor editor;

    private int sourcePageIndex;

    /**
     *
     */
    public AbstractMultipageSourceEditor()
    {
        super();
    }

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
            // TODO: handle exception
        }
    }

    /**
     * @throws PartInitException
     *
     */
    protected abstract void addFormPages() throws PartInitException;

    public int addPage(IFormPage page) throws PartInitException
    {
        int index = getPageCount() - 1;
        addPage(index, page);
        return index;
    }

    /**
     * Creates the source page.
     *
     * @throws PartInitException
     */
    private void addSourcePage() throws PartInitException
    {
        sourcePageIndex = addPage(getSourceEditor(), getEditorInput());
        setPageText(sourcePageIndex, "Source");

        getSourceEditor().setInput(getEditorInput());
    }

    /**
     * @return
     */
    private TextEditor getSourceEditor()
    {
        if (editor == null)
        {
            editor = instantiateSourceEditor();
        }
        return editor;
    }

    protected TextEditor instantiateSourceEditor()
    {
        return new TextEditor();
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor)
    {
        getSourceEditor().doSave(monitor);
    }

    /**
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public void doSaveAs()
    {
        getSourceEditor().doSaveAs();
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
    public Object getAdapter(Class adapter)
    {
        if (IDocumentProvider.class.equals(adapter))
        {
            return getSourceEditor().getDocumentProvider();
        }
        return super.getAdapter(adapter);
    }

}
