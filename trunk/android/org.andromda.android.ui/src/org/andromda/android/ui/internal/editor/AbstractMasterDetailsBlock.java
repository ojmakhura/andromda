package org.andromda.android.ui.internal.editor;

import org.andromda.android.core.internal.AndroidModelManager;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.ui.configuration.editor.ConfigurationEditor;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.forms.MasterDetailsBlock;

/**
 *
 * @author Peter Friese
 * @since 11.12.2005
 */
public abstract class AbstractMasterDetailsBlock
        extends MasterDetailsBlock
{

    private AbstractModelFormPage parentPage = null;
    
    /**
     * 
     */
    public AbstractMasterDetailsBlock(AbstractModelFormPage parentPage)
    {
        super();
        this.parentPage = parentPage;
    }
    
    /**
     * @return Returns the parentPage.
     */
    public AbstractModelFormPage getParentPage()
    {
        return parentPage;
    }
    
    /**
     * @return
     */
    public IModel getModel()
    {
        return getParentPage().getModel();
    }

    /**
     * @return
     */
    protected IAndroidProject getAndroidProject()
    {
        ConfigurationEditor editor = (ConfigurationEditor)getParentPage().getEditor();
        IResource adapter = (IResource)editor.getEditorInput().getAdapter(IResource.class);
        return AndroidModelManager.getInstance().getAndroidModel().getAndroidProject(adapter);
    }

}
