package org.andromda.android.ui.internal.configuration.editor.model;

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
 * @since 08.11.2005
 */
public class ModelConfigurationPage
        extends FormPage
{

    public static final String PAGE_ID = "models";

    private ModelConfigurationMasterDetailsBlock modelConfigurationMasterDetailsBlock;

    public ModelConfigurationPage(FormEditor editor,
        String id,
        String title)
    {
        super(editor, id, title);
        modelConfigurationMasterDetailsBlock = new ModelConfigurationMasterDetailsBlock(this);
    }

    protected void createFormContent(IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Model Configuration");
        Composite body = form.getBody();
        final GridLayout gridLayout = new GridLayout();
        body.setLayout(gridLayout);
        toolkit.paintBordersFor(body);

        modelConfigurationMasterDetailsBlock.createContent(managedForm);
    }

}
