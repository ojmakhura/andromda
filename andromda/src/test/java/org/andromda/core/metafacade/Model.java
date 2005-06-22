package org.andromda.core.metafacade;

import java.util.Collection;

import org.andromda.core.configuration.ModelPackages;


/**
 * @author Chad Brandon
 */
public class Model
    implements ModelAccessFacade
{
    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setModel(java.lang.Object)
     */
    public void setModel(Object model)
    {
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModel()
     */
    public Object getModel()
    {
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getName(java.lang.Object)
     */
    public String getName(Object modelElement)
    {
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getPackageName(java.lang.Object)
     */
    public String getPackageName(Object modelElement)
    {
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getStereotypeNames(java.lang.Object)
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#findByStereotype(java.lang.String)
     */
    public Collection findByStereotype(String stereotype)
    {
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModelElements()
     */
    public Collection getModelElements()
    {
        return null;
    }

    public void setPackageFilter(ModelPackages modelPackages)
    {
    }
}