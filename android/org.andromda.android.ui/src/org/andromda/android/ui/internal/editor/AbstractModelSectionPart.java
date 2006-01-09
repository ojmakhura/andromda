package org.andromda.android.ui.internal.editor;

import org.andromda.android.core.model.IEditorModel;
import org.andromda.android.core.model.IModelChangeProvider;
import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.core.model.IModelChangedListener;
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
public abstract class AbstractModelSectionPart
        extends SectionPart
        implements IModelChangedListener
{

    /** The editor page owning this section. */
    private final AbstractModelFormPage page;

    /** The model change provider this section is subscribed to. */
    private IModelChangeProvider modelChangeProvider;

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
        subscribeToModelChanges();

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
     * @see org.eclipse.ui.forms.AbstractFormPart#dispose()
     */
    public void dispose()
    {
        if (modelChangeProvider != null)
        {
            modelChangeProvider.removeModelChangedListener(this);
        }
        super.dispose();
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

    /**
     * @return The model being edited.
     */
    public IEditorModel getEditorModel()
    {
        AbstractModelFormEditor modelFormEditor = getModelFormEditor();
        if (modelFormEditor != null)
        {
            return modelFormEditor.getEditorModel();
        }
        return null;
    }

    /**
     * Subscribe to model change events.
     */
    protected void subscribeToModelChanges()
    {
        if (getEditorModel() instanceof IModelChangeProvider)
        {
            modelChangeProvider = (IModelChangeProvider)getEditorModel();
            modelChangeProvider.addModelChangedListener(this);
        }
    }

    /**
     * @see org.andromda.android.core.model.IModelChangedListener#modelChanged(org.andromda.android.core.model.IModelChangedEvent)
     */
    public abstract void modelChanged(IModelChangedEvent event);

    /**
    *
    */
   protected void publishChangeEvent()
   {
       if (getEditorModel() instanceof IModelChangeProvider)
       {
           IModelChangeProvider provider = (IModelChangeProvider)getEditorModel();
           provider.fireModelChanged(null);
       }
   }

}
