package org.andromda.android.ui.internal.editor;

import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;

/**
 * 
 * @author Peter Friese
 * @since 16.12.2005
 */
public abstract class AbstractModelDetailsPage
        implements IDetailsPage
{

    private IManagedForm managedForm;

    public void initialize(IManagedForm managedForm)
    {
        this.managedForm = managedForm;
    }

    /**
     * @return Returns the managedForm.
     */
    public IManagedForm getManagedForm()
    {
        return managedForm;
    }
    
    /**
     * @return
     */
    public AbstractModelFormPage getPage()
    {
        return (AbstractModelFormPage)managedForm.getContainer();
    }
    

}
