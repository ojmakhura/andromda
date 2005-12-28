package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelSectionPart;
import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
import org.andromda.core.configuration.AndromdaDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 *
 * @author Peter Friese
 * @since 14.12.2005
 */
public class ModelConfigurationMasterSection
        extends AbstractAndromdaModelSectionPart
{

    /** The visual style of the section. */
    private static final int SECTION_STYLE = Section.DESCRIPTION | Section.TITLE_BAR;

    private ModelConfigurationMasterComposite modelConfigurationMasterComposite;

    /**
     * @param parent
     * @param page
     */
    public ModelConfigurationMasterSection(Composite parent,
        AbstractModelFormPage page)
    {
        super(parent, page, SECTION_STYLE);
    }

    /**
     * @param page
     */
    public ModelConfigurationMasterSection(AbstractModelFormPage page)
    {
        super(page, SECTION_STYLE);
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
     */
    public void initialize(IManagedForm form)
    {
        super.initialize(form);

        // set up section
        getSection().setText("Models");
        getSection().setDescription("Configure the model(s) to be processed by AndroMDA.");
        getSection().marginHeight = 5;
        getSection().marginWidth = 5;

        // add model master section
        modelConfigurationMasterComposite = new ModelConfigurationMasterComposite(this, SWT.NONE);
        getSection().setClient(modelConfigurationMasterComposite);
        getSection().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#refresh()
     */
    public void refresh()
    {
        super.refresh();
        AndromdaDocument andromdaDocument = getAndromdaDocument();
        modelConfigurationMasterComposite.setAndroMDADocument(andromdaDocument);
    }

}
