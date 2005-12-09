package org.andromda.android.ui.internal.configuration.editor.cartridge;

import org.andromda.android.core.util.XmlUtils;
import org.andromda.core.configuration.NamespaceDocument.Namespace;
import org.andromda.core.namespace.PropertyDocument.Property;
import org.andromda.core.namespace.PropertyGroupDocument.PropertyGroup;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * This {@link org.eclipse.ui.forms.IDetailsPage} displays the namespace properties for the selected namespace property
 * group. The user can edit these properties.
 * 
 * @author Peter Friese
 * @since 07.11.2005
 */
public class GenericCartridgeConfigurationDetailsPage
        extends AbstractFormPart
        implements IDetailsPage
{

    /** The section containing the list of properties being edited. */
    private Section cartridgeNameCartridgeSection;

    /** The composite containing the list of properties. */
    private Composite propertiesComposite;

    /** The form toolkit. */
    private FormToolkit toolkit;

    /** The namespace being edited. */
    private Namespace namespace;

    /** The property group being edited. */
    private PropertyGroup propertyGroup;

    /** This array contains all properties of the selected propertygroup. */
    private Property[] properties;

    /**
     * @see org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
     */
    public void createContents(Composite parent)
    {
        toolkit = getManagedForm().getToolkit();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 5;
        gridLayout.marginHeight = 0;
        parent.setLayout(gridLayout);

        cartridgeNameCartridgeSection = toolkit.createSection(parent, Section.DESCRIPTION | Section.EXPANDED
                | Section.TITLE_BAR);
        cartridgeNameCartridgeSection.setDescription("Edit the namespace settings to configure the cartridges.");
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData.heightHint = 541;
        gridData.widthHint = 564;
        cartridgeNameCartridgeSection.setLayoutData(gridData);
        cartridgeNameCartridgeSection.setText("<Namespace> - <Group> namespace properties");

        propertiesComposite = toolkit.createComposite(cartridgeNameCartridgeSection, SWT.NONE);
        final GridLayout gridLayout_1 = new GridLayout();
        gridLayout_1.numColumns = 2;
        propertiesComposite.setLayout(gridLayout_1);
        toolkit.paintBordersFor(propertiesComposite);
        cartridgeNameCartridgeSection.setClient(propertiesComposite);

        fillPropertiesComposite();
    }

    /**
     * Danymically populate the properties composite. For each property in the selected namspace property group, a label /
     * edit field combination will be created.
     */
    private void fillPropertiesComposite()
    {
        // dispose of old controls
        Control[] children = propertiesComposite.getChildren();
        for (int i = 0; i < children.length; i++)
        {
            Control control = children[i];
            control.dispose();
        }

        if (properties != null)
        {

            String namespaceName = namespace.getName();
            String propertyGroupName = propertyGroup.getName();
            cartridgeNameCartridgeSection.setText(namespaceName + "/" + propertyGroupName + " properties");

            String propertyGroupDocumentation = XmlUtils.getTextValueFromElement(propertyGroup.getDocumentation());
            if (propertyGroupDocumentation != null)
            {
                cartridgeNameCartridgeSection.setDescription(propertyGroupDocumentation);
            }
            else
            {
                cartridgeNameCartridgeSection.setDescription("Configure the " + namespaceName + " namespace.");
            }

            for (int i = 0; i < properties.length; i++)
            {
                Property property = properties[i];
                String name = property.getName();
                String documentation = XmlUtils.getTextValueFromElement(property.getDocumentation());
                boolean required = property.getRequired();
                String req = "";
                if (required)
                {
                    req = "*";
                }

                final Label label = toolkit.createLabel(propertiesComposite, name + req + ":", SWT.NONE);
                label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
                label.setToolTipText(documentation);

                final Text valueText = toolkit.createText(propertiesComposite, null, SWT.NONE);
                valueText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
                valueText.setText(getPropertyValue(name));
                valueText.setToolTipText(documentation);
                valueText.setData("PROPERTYNAME", name);
                valueText.addModifyListener(new ModifyListener()
                {
                    public void modifyText(ModifyEvent e)
                    {
                        Widget w = e.widget;
                        if (w instanceof Text)
                        {
                            Text textField = (Text)w;
                            String value = textField.getText();
                            String name = (String)textField.getData("PROPERTYNAME");
                            setPropertyValue(name, value);
                        }
                        System.out.println("Modify");
                        markDirty();
                    }
                });
            }
        }
    }

    /**
     * Reads the value of the given property from the currently selected namespace.
     * 
     * @param propertyName The name of the property to retrieve.
     * @return The value of the property.
     */
    private String getPropertyValue(String propertyName)
    {
        org.andromda.core.configuration.PropertyDocument.Property[] propertyArray = namespace.getProperties()
                .getPropertyArray();
        for (int i = 0; i < propertyArray.length; i++)
        {
            org.andromda.core.configuration.PropertyDocument.Property property = propertyArray[i];
            if (propertyName.equalsIgnoreCase(property.getName()))
            {
                return property.getStringValue();
            }
        }
        return "";
    }

    /**
     * Sets the value of the given property in the selected namespace.
     * 
     * @param propertyName The name of the property.
     * @param value The new value.
     */
    private void setPropertyValue(String propertyName,
        String value)
    {
        org.andromda.core.configuration.PropertyDocument.Property found = null;

        org.andromda.core.configuration.PropertyDocument.Property[] propertyArray = namespace.getProperties()
                .getPropertyArray();
        for (int i = 0; i < propertyArray.length; i++)
        {
            org.andromda.core.configuration.PropertyDocument.Property property = propertyArray[i];
            if (propertyName.equalsIgnoreCase(property.getName()))
            {
                found = property;
                break;
            }
        }
        if (found == null)
        {
            found = namespace.getProperties().addNewProperty();
            found.setName(propertyName);
        }
        found.setStringValue(value);

    }

    /**
     * Update the GUI.
     */
    private void update()
    {
        fillPropertiesComposite();
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
        if (element instanceof NamespacePropertyContainer)
        {
            NamespacePropertyContainer namespacePropertyContainer = (NamespacePropertyContainer)element;
            propertyGroup = namespacePropertyContainer.getPropertyGroup();
            properties = propertyGroup.getPropertyArray();
            namespace = namespacePropertyContainer.getNamespace();
        }
        update();
    }

    /**
     * @see org.eclipse.ui.forms.IFormPart#refresh()
     */
    public void refresh()
    {
        super.refresh();
        update();
    }

    /**
     * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
     */
    public void commit(boolean onSave)
    {
        super.commit(onSave);
        System.out.println("Commit");
    }

}
