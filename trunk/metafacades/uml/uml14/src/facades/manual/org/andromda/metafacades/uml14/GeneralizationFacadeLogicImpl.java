package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GeneralizationFacade
 */
public class GeneralizationFacadeLogicImpl
       extends GeneralizationFacadeLogic
       implements org.andromda.metafacades.uml.GeneralizationFacade
{
    // ---------------- constructor -------------------------------
    
    public GeneralizationFacadeLogicImpl (org.omg.uml.foundation.core.Generalization metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }
    // ------------- relations ------------------
    
	/**
	 * @see org.andromda.metafacades.uml.GeneralizationFacade#getChild()
	 */
    public java.lang.Object handleGetChild()
    {
        return metaObject.getChild();
    }

	/**
	 * @see org.andromda.metafacades.uml.GeneralizationFacade#getParent()
	 */
    public java.lang.Object handleGetParent()
    {
        return metaObject.getParent();
    }

}
