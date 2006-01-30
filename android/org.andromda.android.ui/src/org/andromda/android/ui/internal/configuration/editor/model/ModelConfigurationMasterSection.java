package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.core.model.IModelChangedEvent;
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
    public ModelConfigurationMasterSection(final Composite parent,
        final AbstractModelFormPage page)
    {
        super(parent, page, SECTION_STYLE);
    }

    /**
     * @param page
     */
    public ModelConfigurationMasterSection(final AbstractModelFormPage page)
    {
        super(page, SECTION_STYLE);
    }

    /**
     * {@inheritDoc}
     */
    public void initialize(final IManagedForm form)
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
     * {@inheritDoc}
     */
    public void refresh()
    {
        super.refresh();
        AndromdaDocument andromdaDocument = getAndromdaDocument();
        modelConfigurationMasterComposite.setAndroMDADocument(andromdaDocument);
    }

    /**
     * {@inheritDoc}
     */
    public void modelChanged(final IModelChangedEvent event)
    {
        refresh();
    }

}
