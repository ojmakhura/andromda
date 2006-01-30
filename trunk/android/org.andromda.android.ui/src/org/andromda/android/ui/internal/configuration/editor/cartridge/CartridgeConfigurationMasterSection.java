package org.andromda.android.ui.internal.configuration.editor.cartridge;

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
public class CartridgeConfigurationMasterSection
        extends AbstractAndromdaModelSectionPart
{

    /** The visual style of the section. */
    private static final int SECTION_STYLE = Section.DESCRIPTION | Section.TITLE_BAR;

    private CartridgeConfigurationMasterComposite cartridgeConfigurationMasterComposite;

    /**
     * Creates a new CartridgeConfigurationMasterSection.
     *
     * @param parent
     * @param page
     */
    public CartridgeConfigurationMasterSection(final Composite parent,
        final AbstractModelFormPage page)
    {
        super(parent, page, SECTION_STYLE);
    }

    /**
     * Creates a new CartridgeConfigurationMasterSection.
     *
     * @param page
     */
    public CartridgeConfigurationMasterSection(final AbstractModelFormPage page)
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
        getSection().setText("Cartridges");
        getSection().setDescription("Configure the Cartridge(s) to be used by AndroMDA.");
        getSection().marginHeight = 5;
        getSection().marginWidth = 5;

        // add cartridges master composite
        cartridgeConfigurationMasterComposite = new CartridgeConfigurationMasterComposite(this, SWT.NONE);
        getSection().setClient(cartridgeConfigurationMasterComposite);
        getSection().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
    }

    /**
     * {@inheritDoc}
     */
    public void refresh()
    {
        super.refresh();
        AndromdaDocument andromdaDocument = getAndromdaDocument();
        cartridgeConfigurationMasterComposite.setAndroMDADocument(andromdaDocument);
    }

    /**
     * {@inheritDoc}
     */
    public void modelChanged(final IModelChangedEvent event)
    {
        refresh();
    }

}
