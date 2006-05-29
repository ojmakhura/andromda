package org.andromda.android.ui.internal.configuration.editor.cartridge;

import org.andromda.android.core.model.IModelChangedEvent;
import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelSectionPart;
import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
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
 *
 * @author Peter Friese
 * @since 08.12.2005
 */
public class CartridgeConfigurationDetailsSection
        extends AbstractAndromdaModelSectionPart
        implements IPartSelectionListener
{

    /** The visual style of the section. */
    private static final int SECTION_STYLE = Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
            | Section.EXPANDED;

    /** The composite hosted by this section. This composite contains the real edit components. */
    private CartridgeConfigurationDetailsComposite cartridgeConfigurationDetailsComposite;

    private NamespacePropertyContainer namespacePropertyContainer;

    /**
     * Creates a new CartridgeConfigurationDetailsSection.
     *
     * @param page
     */
    public CartridgeConfigurationDetailsSection(final AbstractModelFormPage page)
    {
        super(page, SECTION_STYLE);
    }

    /**
     * Creates a new CartridgeConfigurationDetailsSection.
     *
     * @param parent
     * @param page
     */
    public CartridgeConfigurationDetailsSection(final Composite parent,
        final AbstractModelFormPage page)
    {
        super(parent, page, SECTION_STYLE);
    }

    /**
     * {@inheritDoc}
     */
    public void initialize(final IManagedForm form)
    {
        super.initialize(form);

        // set up the section
        getSection().setText("Cartridge configuration");
        getSection().setDescription("Configure the cartridge.");

        // insert model details composite
        cartridgeConfigurationDetailsComposite = new CartridgeConfigurationDetailsComposite(this, SWT.NONE);
        getSection().setClient(cartridgeConfigurationDetailsComposite);
        getSection().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#refresh()
     */
    public void refresh()
    {
        super.refresh();
        cartridgeConfigurationDetailsComposite.setNamespacePropertyContainer(namespacePropertyContainer);
    }

    /**
     * {@inheritDoc}
     */
    public void commit(final boolean onSave)
    {
        super.commit(onSave);
    }

    /**
     * {@inheritDoc}
     */
    public void selectionChanged(final IFormPart part,
        final ISelection selection)
    {
        if (selection instanceof IStructuredSelection)
        {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            Object firstElement = structuredSelection.getFirstElement();
            if (firstElement instanceof NamespacePropertyContainer)
            {
                namespacePropertyContainer = (NamespacePropertyContainer)firstElement;
                refresh();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void modelChanged(final IModelChangedEvent event)
    {
        refresh();
    }

}
