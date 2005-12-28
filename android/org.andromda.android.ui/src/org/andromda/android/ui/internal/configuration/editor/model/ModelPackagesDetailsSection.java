package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelSectionPart;
import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
import org.andromda.core.configuration.ModelDocument.Model;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IPartSelectionListener;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This section contains GUI elements to configure which model packages wil be considered during code generation.
 *
 * @author Peter Friese
 * @since 22.12.2005
 */
public class ModelPackagesDetailsSection
        extends AbstractAndromdaModelSectionPart
        implements IPartSelectionListener
{

    /** The visual style of the section. */
    private static final int SECTION_STYLE = Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
            | Section.EXPANDED;

    /** The composite hosted by this section. This composite contains the real edit components. */
    private ModelPackagesDetailsComposite modelPackagesDetailsComposite;

    /**
     * @param page
     */
    public ModelPackagesDetailsSection(AbstractModelFormPage page)
    {
        super(page, SECTION_STYLE);
    }

    /**
     * @param parent
     * @param page
     */
    public ModelPackagesDetailsSection(Composite parent,
        AbstractModelFormPage page)
    {
        super(parent, page, SECTION_STYLE);
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
     */
    public void initialize(IManagedForm form)
    {
        super.initialize(form);

        // set up the section
        getSection().setText("Model packages");
        getSection().setDescription("Define which packages take part in the generation process.");

        // insert model details composite
        modelPackagesDetailsComposite = new ModelPackagesDetailsComposite(this, SWT.NONE);
        getSection().setClient(modelPackagesDetailsComposite);
        getSection().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#refresh()
     */
    public void refresh()
    {
        super.refresh();
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
        super.commit(onSave);
    }

    /**
     * @see org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse.ui.forms.IFormPart,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IFormPart part,
        ISelection selection)
    {
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            Object firstElement = structuredSelection.getFirstElement();
            if (firstElement instanceof Model)
            {
                Model model = (Model)firstElement;
                modelPackagesDetailsComposite.setModel(model);
            }
        }
    }

}
