package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.ui.internal.editor.AbstractModelDetailsPage;
import org.andromda.core.configuration.ModelDocument.Model;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 *
 * @author Peter Friese
 * @since 11.12.2005
 */
public class ModelDetailsPage
        extends AbstractModelDetailsPage
        implements IDetailsPage
{

    private Model model;

    public void createContents(Composite parent)
    {
        FormToolkit toolkit = getManagedForm().getToolkit();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);

        ModelDetailsSection detailsSection = new ModelDetailsSection(parent, getPage());
        getManagedForm().addPart(detailsSection);

    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#dispose()
     */
    public void dispose()
    {
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#isDirty()
     */
    public boolean isDirty()
    {
        return false;
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
     */
    public boolean setFormInput(Object input)
    {
        return false;
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#setFocus()
     */
    public void setFocus()
    {
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#isStale()
     */
    public boolean isStale()
    {
        return false;
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#refresh()
     */
    public void refresh()
    {
    }

    /**
     * @see org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse.ui.forms.IFormPart,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IFormPart part,
        ISelection selection)
    {
        IStructuredSelection structuredSelection = (IStructuredSelection)selection;
        Object element = structuredSelection.getFirstElement();
        if (element instanceof Model)
        {
            model = (Model)element;
        }
        update();
    }

    /**
     *
     */
    private void update()
    {
        boolean lastModifiedCheck = model.getLastModifiedCheck();
//        lastModifiedCheckButton.setSelection(lastModifiedCheck);
    }
}
