package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.omg.uml.foundation.core.Generalization;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GeneralizableElementFacade
 */
public class GeneralizableElementFacadeLogicImpl
       extends GeneralizableElementFacadeLogic
       implements org.andromda.metafacades.uml.GeneralizableElementFacade
{
    // ---------------- constructor -------------------------------
    
    public GeneralizableElementFacadeLogicImpl (org.omg.uml.foundation.core.GeneralizableElement metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class GeneralizableElementFacade ...

	/**
	 * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllGeneralizations()()
	 */
    public java.util.Collection handleGetAllGeneralizations() 
    {
    	Collection generalizations = new ArrayList();
        for (GeneralizableElementFacade element = this.getGeneralization();
             element != null;
             element = element.getGeneralization()) {
        	generalizations.add(element);
        }
        return generalizations;
    }

    // ------------- relations ------------------
    
	/**
	 * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralization()
	 */
    public java.lang.Object handleGetGeneralization()
    {
        Collection generalizations = metaObject.getGeneralization();
        if (generalizations == null)
        {
            return null;
        }
        Iterator i = generalizations.iterator();

        if (i.hasNext())
        {
            Generalization generalization = (Generalization) i.next();
            return generalization.getParent();
        }

        return null;
    }

    protected Collection handleGetGeneralizations()
    {
        return metaObject.getGeneralization();
    }
}
