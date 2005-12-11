package org.andromda.android.ui.internal.editor;

import org.andromda.android.core.internal.AndroidModelManager;
import org.andromda.android.core.project.IAndroidProject;
import org.andromda.android.ui.configuration.editor.ConfigurationEditor;
import org.andromda.core.configuration.AndromdaDocument;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormPage;

/**
 *
 * @author Peter Friese
 * @since 11.12.2005
 */
public abstract class BaseMasterDetailsBlock
        extends MasterDetailsBlock
{

    private FormPage parentPage = null;
    
    /**
     * @param parentPage The parentPage to set.
     */
    public void setParentPage(FormPage parentPage)
    {
        this.parentPage = parentPage;
    }
    
    /**
     * @return Returns the parentPage.
     */
    public FormPage getParentPage()
    {
        return parentPage;
    }

    /**
     * @return
     */
    protected AndromdaDocument getAndromdaDocument()
    {
        ConfigurationEditor editor = (ConfigurationEditor)getParentPage().getEditor();
        return editor.getDocument();
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
