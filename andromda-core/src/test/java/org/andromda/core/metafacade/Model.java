package org.andromda.core.metafacade;

import java.util.Collection;
import org.andromda.core.configuration.Filters;

/**
 * @author Chad Brandon
 */
public class Model
    implements ModelAccessFacade
{
    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setModel(Object)
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
     * @see org.andromda.core.metafacade.ModelAccessFacade#getName(Object)
     */
    public String getName(Object modelElement)
    {
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getPackageName(Object)
     */
    public String getPackageName(Object modelElement)
    {
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getStereotypeNames(Object)
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        return null;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#findByStereotype(String)
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

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setPackageFilter(org.andromda.core.configuration.Filters)
     */
    public void setPackageFilter(Filters modelPackages)
    {
    }
}