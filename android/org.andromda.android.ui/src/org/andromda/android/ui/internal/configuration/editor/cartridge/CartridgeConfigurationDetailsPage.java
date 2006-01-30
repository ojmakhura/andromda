package org.andromda.android.ui.internal.configuration.editor.cartridge;

import org.andromda.android.ui.internal.editor.AbstractModelDetailsPage;
import org.andromda.core.configuration.ModelDocument.Model;
import org.andromda.core.configuration.NamespaceDocument.Namespace;
import org.andromda.core.namespace.PropertyDocument.Property;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * This details page contains sections for configuring the cartridge selected in the master part.
 *
 * @author Peter Friese
 * @since 17.01.2006
 */
public class CartridgeConfigurationDetailsPage
        extends AbstractModelDetailsPage
        implements IDetailsPage
{

    /**
     * {@inheritDoc}
     */
    public void createContents(final Composite parent)
    {
        FormToolkit toolkit = getManagedForm().getToolkit();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);

        CartridgeConfigurationDetailsSection cartridgeConfigurationDetailsSection = new CartridgeConfigurationDetailsSection(
                parent, getPage());
        getManagedForm().addPart(cartridgeConfigurationDetailsSection);
    }

    /**
     * {@inheritDoc}
     */
    public void dispose()
    {
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDirty()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void commit(final boolean onSave)
    {
    }

    /**
     * {@inheritDoc}
     */
    public boolean setFormInput(final Object input)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void setFocus()
    {
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStale()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void refresh()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void selectionChanged(final IFormPart part,
        final ISelection selection)
    {
        IStructuredSelection structuredSelection = (IStructuredSelection)selection;
        Object element = structuredSelection.getFirstElement();
        if (element instanceof NamespacePropertyContainer)
        {
            NamespacePropertyContainer namespacePropertyContainer = (NamespacePropertyContainer)element;
            PropertyGroup propertyGroup = namespacePropertyContainer.getPropertyGroup();
            Property[] properties = propertyGroup.getPropertyArray();
            Namespace namespace = namespacePropertyContainer.getNamespace();

        }
    }

}
