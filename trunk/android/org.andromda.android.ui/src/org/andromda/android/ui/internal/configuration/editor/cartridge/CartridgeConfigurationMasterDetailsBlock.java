package org.andromda.android.ui.internal.configuration.editor.cartridge;

import org.andromda.android.ui.internal.editor.AbstractMasterDetailsBlock;
import org.andromda.android.ui.internal.editor.AbstractModelFormPage;
import org.andromda.core.configuration.ModelDocument.Model;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;

/**
 *
 * @author Peter Friese
 * @since 11.12.2005
 */
public class CartridgeConfigurationMasterDetailsBlock
        extends AbstractMasterDetailsBlock
{

    private CartridgeConfigurationDetailsPage cartridgeDetailsPage;

    /**
     * Creates a new ModelConfigurationMasterDetailsBlock.
     *
     * @param parentPage
     */
    public CartridgeConfigurationMasterDetailsBlock(final AbstractModelFormPage parentPage)
    {
        super(parentPage);
        cartridgeDetailsPage = new CartridgeConfigurationDetailsPage();
    }

    /**
     * {@inheritDoc}
     */
    protected void createMasterPart(final IManagedForm managedForm,
        final Composite parent)
    {
        CartridgeConfigurationMasterSection cartridgeConfigurationMasterSection = new CartridgeConfigurationMasterSection(
                parent, getParentPage());
        managedForm.addPart(cartridgeConfigurationMasterSection);
    }

    /**
     * {@inheritDoc}
     */
    protected void registerPages(final DetailsPart detailsPart)
    {
        detailsPart.setPageProvider(new IDetailsPageProvider()
        {

            public Object getPageKey(Object object)
            {
                return object;
            }

            public IDetailsPage getPage(Object key)
            {
                if (key instanceof NamespacePropertyContainer)
                {
                    NamespacePropertyContainer namespacePropertyContainer = (NamespacePropertyContainer)key;
                    return cartridgeDetailsPage;
                }
                else
                {
                    return null;
                }
            }

        });
    }

    /**
     * {@inheritDoc}
     */
    protected void createToolBarActions(final IManagedForm managedForm)
    {
    }

}
