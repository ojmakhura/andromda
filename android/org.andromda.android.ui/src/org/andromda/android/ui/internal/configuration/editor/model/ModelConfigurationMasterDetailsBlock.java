package org.andromda.android.ui.internal.configuration.editor.model;

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
public class ModelConfigurationMasterDetailsBlock
        extends AbstractMasterDetailsBlock
{

    private ModelDetailsPage modelDetailsPage;

    public ModelConfigurationMasterDetailsBlock(AbstractModelFormPage parentPage)
    {
        super(parentPage);
        modelDetailsPage = new ModelDetailsPage();
    }

    protected void createMasterPart(final IManagedForm managedForm,
        Composite parent)
    {
        ModelConfigurationMasterSection modelConfigurationMasterSection = new ModelConfigurationMasterSection(parent,
                getParentPage());
        managedForm.addPart(modelConfigurationMasterSection);
    }

    protected void registerPages(DetailsPart detailsPart)
    {
        detailsPart.setPageProvider(new IDetailsPageProvider()
        {

            public Object getPageKey(Object object)
            {
                return object;
            }

            public IDetailsPage getPage(Object key)
            {
                if (key instanceof Model)
                {
                    Model model = (Model)key;
                    return modelDetailsPage;
                }
                else
                {
                    return null;
                }
            }

        });
    }

    protected void createToolBarActions(IManagedForm managedForm)
    {
    }

}
