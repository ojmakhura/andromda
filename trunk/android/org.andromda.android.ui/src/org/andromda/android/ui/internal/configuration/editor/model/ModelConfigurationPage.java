package org.andromda.android.ui.internal.configuration.editor.model;

import org.andromda.android.ui.internal.configuration.editor.AbstractAndromdaModelFormPage;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This is the editor page containing the master/details part for editing the model properties.
 *
 * @author Peter Friese
 * @since 08.11.2005
 */
public class ModelConfigurationPage
        extends AbstractAndromdaModelFormPage
{

    /** The page id. */
    public static final String PAGE_ID = "models";

    private ModelConfigurationMasterDetailsBlock modelConfigurationMasterDetailsBlock;

    /**
     * Creates a new model configuration page.
     *
     * @param editor The editor hosting this page.
     * @param id The unique page identifier.
     * @param title The title of the page.
     */
    public ModelConfigurationPage(FormEditor editor,
        String id,
        String title)
    {
        super(editor, id, title);
        modelConfigurationMasterDetailsBlock = new ModelConfigurationMasterDetailsBlock(this);
    }

    /**
     * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
     */
    protected void createFormContent(IManagedForm managedForm)
    {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        form.setText("Model Configuration");
        Composite body = form.getBody();

        GridLayout gridLayout = new GridLayout();
        body.setLayout(gridLayout);
        toolkit.paintBordersFor(body);

        modelConfigurationMasterDetailsBlock.createContent(managedForm);
    }
    
}
