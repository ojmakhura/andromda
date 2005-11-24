package org.andromda.android.ui.internal.configuration.editor;

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
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 *
 * @author Peter Friese
 * @since 07.11.2005
 */
public class GenericAndroidDetailsPage
        extends AbstractFormPart
        implements IDetailsPage
{

    private FormToolkit toolkit;

    private Composite propertiesComposite;

    private Property[] properties;

    private Namespace namespace;

    public void createContents(Composite parent)
    {
        toolkit = getManagedForm().getToolkit();
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginTop = 5;
        gridLayout.marginHeight = 0;
        parent.setLayout(gridLayout);

        final Section cartridgeNameCartridgeSection = toolkit.createSection(parent, ExpandableComposite.EXPANDED
                | ExpandableComposite.TITLE_BAR);
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
            for (int i = 0; i < properties.length; i++)
            {
                Property property = properties[i];
                String name = property.getName();
                boolean required = property.getRequired();
                String req = "";
                if (required)
                {
                    req = "*";
                }

                final Label label = toolkit.createLabel(propertiesComposite, name + req + ":", SWT.NONE);
                label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));

                final Text valueText = toolkit.createText(propertiesComposite, null, SWT.NONE);
                valueText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
                valueText.setText(getPropertyValue(name));
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

    private void setPropertyValue(String propertyName, String value)
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
        if (found == null) {
            found = namespace.getProperties().addNewProperty();
            found.setName(propertyName);
        }
        found.setStringValue(value);

    }

    private void update()
    {
        fillPropertiesComposite();
    }

    public void selectionChanged(IFormPart part,
        ISelection selection)
    {
        IStructuredSelection structuredSelection = (IStructuredSelection)selection;
        Object element = structuredSelection.getFirstElement();
        if (element instanceof NamespacePropertyContainer)
        {
            NamespacePropertyContainer namespacePropertyContainer = (NamespacePropertyContainer)element;
            PropertyGroup propertyGroup = namespacePropertyContainer.getPropertyGroup();
            properties = propertyGroup.getPropertyArray();
            namespace = namespacePropertyContainer.getNamespace();
        }
        update();
    }

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
