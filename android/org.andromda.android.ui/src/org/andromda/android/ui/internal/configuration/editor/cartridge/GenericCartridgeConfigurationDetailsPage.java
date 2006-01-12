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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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

    /** This listener listens to modifications of the textfields. */
    private ModifyListener modifyListener = new ModifyListener()
    {
        public void modifyText(final ModifyEvent e)
        {
            Widget w = e.widget;
            if (w instanceof Text)
            {
                Text textField = (Text)w;
                String value = textField.getText();
                String name = (String)textField.getData("PROPERTYNAME");
                setPropertyValue(name, value);
                System.out.println("Modify");
                markDirty();
            }
        }
    };

    /** This listener listens to selection events on the "ignore" checkbuttons. */
    private SelectionListener selectionListener = new SelectionAdapter()
    {
        public void widgetSelected(final SelectionEvent e)
        {
            Widget w = e.widget;
            if (w instanceof Button)
            {
                Button checkbox = (Button)w;
                boolean selected = checkbox.getSelection();
                String name = (String)checkbox.getData("PROPERTYNAME");
                setIgnoreProperty(name, selected);
                System.out.println("Modify");
                markDirty();
            }
        }
    };

    /**
     * {@inheritDoc}
     */
    public void createContents(final Composite parent)
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
        cartridgeNameCartridgeSection.setLayoutData(gridData);
        cartridgeNameCartridgeSection.setText("<Namespace> - <Group> namespace properties");

        propertiesComposite = toolkit.createComposite(cartridgeNameCartridgeSection, SWT.NONE);
        propertiesComposite.setLayout(new GridLayout(3, false));
        toolkit.paintBordersFor(propertiesComposite);
        cartridgeNameCartridgeSection.setClient(propertiesComposite);

        createPropertiesFields();
    }

    /**
     * Updates the contents of all fields.
     */
    private void updatePropertiesFields()
    {
        Control[] children = propertiesComposite.getChildren();
        for (int i = 0; i < children.length; i++)
        {
            Control control = children[i];
            if (control instanceof Text)
            {
                Text textField = (Text)control;
                String propertyName = (String)textField.getData("PROPERTYNAME");
                org.andromda.core.configuration.PropertyDocument.Property property = findProperty(propertyName);
                String value = property.getStringValue();
                
                textField.removeModifyListener(modifyListener);
                textField.setText(value);
                textField.addModifyListener(modifyListener);
            }
            else if (control instanceof Button)
            {
                Button checkbox = (Button)control;
                String propertyName = (String)checkbox.getData("PROPERTYNAME");
                org.andromda.core.configuration.PropertyDocument.Property property = findProperty(propertyName);
                boolean ignore = property.getIgnore();
                
                checkbox.removeSelectionListener(selectionListener);
                checkbox.setSelection(ignore);
                checkbox.addSelectionListener(selectionListener);
            }
        }
    }

    /**
     * Danymically populate the properties composite. For each property in the selected namspace property group, 
     * a label / edit field combination will be created.
     */
    private void createPropertiesFields()
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
                // valueText.setText(getPropertyValue(name));
                valueText.setToolTipText(documentation);
                valueText.setData("PROPERTYNAME", name);
                valueText.addModifyListener(modifyListener);

                final Button ignoreCheckBox = toolkit.createButton(propertiesComposite, null, SWT.CHECK);
                ignoreCheckBox.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
                ignoreCheckBox.setData("PROPERTYNAME", name);
                ignoreCheckBox.setToolTipText("Ignore this property.");
                ignoreCheckBox.addSelectionListener(selectionListener);
            }
        }
    }

    /**
     * Reads the value of the given property from the currently selected namespace.
     * 
     * @param propertyName The name of the property to retrieve.
     * @return The value of the property.
     */
    private String getPropertyValue(final String propertyName)
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
    private void setPropertyValue(final String propertyName,
        final String value)
    {
        org.andromda.core.configuration.PropertyDocument.Property property = findProperty(propertyName);
        if (property != null)
        {
            property.setStringValue(value);
        }
    }

    /**
     * Sets the "ignore" value of the selected property.
     * 
     * @param propertyName The name of the property.
     * @param ignore Whether to ignore the property.
     */
    private void setIgnoreProperty(final String propertyName,
        final boolean ignore)
    {
        org.andromda.core.configuration.PropertyDocument.Property property = findProperty(propertyName);
        if (property != null)
        {
            if (ignore)
            {
                property.setIgnore(ignore);
            }
            else
            {
                property.unsetIgnore();
            }
        }

    }

    /**
     * Finds a property by its name.
     * 
     * @param propertyName  The name of the property to look up.
     * @return The property, if it exists. Null otherwise.
     */
    private org.andromda.core.configuration.PropertyDocument.Property findProperty(final String propertyName)
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
        return found;
    }

    /**
     * Update the GUI.
     */
    private void update()
    {
        createPropertiesFields();
        updatePropertiesFields();
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
            propertyGroup = namespacePropertyContainer.getPropertyGroup();
            properties = propertyGroup.getPropertyArray();
            namespace = namespacePropertyContainer.getNamespace();
        }
        update();
    }

    /**
     * {@inheritDoc}
     */
    public void refresh()
    {
        super.refresh();
        update();
    }

    /**
     * {@inheritDoc}
     */
    public void commit(final boolean onSave)
    {
        super.commit(onSave);
        System.out.println("Commit");
    }

}
