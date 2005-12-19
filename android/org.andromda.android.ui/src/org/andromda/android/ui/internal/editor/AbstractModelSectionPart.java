package org.andromda.android.ui.internal.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Section;

/**
 * A basic section part hat features easy access to the editor and the hosting editor page.
 * 
 * @author Peter Friese
 * @since 09.12.2005
 */
public class AbstractModelSectionPart
        extends SectionPart
{

    /** The editor page owning this section. */
    private final AbstractModelFormPage page;

    /**
     * Creates a new section part.
     * 
     * @param parent The parent composite.
     * @param page The page that hosts this section.
     * @param style The SWT style for the section.
     */
    public AbstractModelSectionPart(Composite parent,
        AbstractModelFormPage page,
        int style)
    {
        super(parent, page.getManagedForm().getToolkit(), style);
        this.page = page;

    }

    /**
     * Creates a new section part.
     * 
     * @param parent The parent composite.
     * @param page The page that hosts this section.
     */
    public AbstractModelSectionPart(Composite parent,
        AbstractModelFormPage page)
    {
        this(parent, page, Section.DESCRIPTION | Section.TITLE_BAR);
    }

    /**
     * Creates a new section part.
     * 
     * @param page The page that hosts this section.
     * @param style The SWT style of the section.
     */
    public AbstractModelSectionPart(AbstractModelFormPage page,
        int style)
    {
        this(page.getManagedForm().getForm().getBody(), page, style);
    }

    /**
     * Creates a new section part.
     * 
     * @param page The page that hosts this section.
     */
    public AbstractModelSectionPart(AbstractModelFormPage page)
    {
        this(page.getManagedForm().getForm().getBody(), page);
    }

    /**
     * @return Returns the page.
     */
    protected AbstractModelFormPage getPage()
    {
        return page;
    }

    /**
     * @return The editor.
     */
    public FormEditor getEditor()
    {
        return getPage().getEditor();
    }

    /**
     * @return The model form editor.
     */
    public AbstractModelFormEditor getModelFormEditor()
    {
        return (AbstractModelFormEditor)getEditor();
    }

    /**
     * This method returns the project in which the currently edited element is located in.
     * 
     * @return The project that "owns" the edited resource.
     */
    public IProject getProject()
    {
        Object container = getManagedForm().getContainer();
        if (container instanceof FormPage)
        {
            FormPage formPage = (FormPage)container;
            FormEditor editor = formPage.getEditor();
            IEditorInput editorInput = editor.getEditorInput();
            if (editorInput instanceof IFileEditorInput)
            {
                IFileEditorInput fileEditorInput = (IFileEditorInput)editorInput;
                IFile file = fileEditorInput.getFile();
                return file.getProject();
            }
        }
        else if (container instanceof IWorkbenchPage)
        {
            IWorkbenchPage workbenchPage = (IWorkbenchPage)container;
            ISelection selection = workbenchPage.getSelection();
            if (selection instanceof IStructuredSelection)
            {
                IStructuredSelection structuredSelection = (IStructuredSelection)selection;
                Object element = structuredSelection.getFirstElement();
            }
            // TODO finish this branch!
        }
        return null;
    }

}
