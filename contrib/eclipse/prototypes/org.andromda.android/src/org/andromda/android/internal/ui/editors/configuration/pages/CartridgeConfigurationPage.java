/**
 *
 */
package org.andromda.android.internal.ui.editors.configuration.pages;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 *
 * @author Peter Friese
 * @since 28.05.2005
 */
public class CartridgeConfigurationPage
        extends FormPage
{

    private CartridgeConfigurationMasterDetailsBlock cartridgeConfigurationMasterDetailsBlock;

    public CartridgeConfigurationPage(String id, String title)
    {
        super(id, title);
    }

    public CartridgeConfigurationPage(FormEditor editor, String id, String title)
    {
        super(editor, id, title);
        cartridgeConfigurationMasterDetailsBlock = new CartridgeConfigurationMasterDetailsBlock();
    }

    protected void createFormContent(IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Configure Cartridges");
        Composite body = form.getBody();
        body.setLayout(new GridLayout());
        toolkit.paintBordersFor(body);

        cartridgeConfigurationMasterDetailsBlock.createContent(managedForm);
    }

}
