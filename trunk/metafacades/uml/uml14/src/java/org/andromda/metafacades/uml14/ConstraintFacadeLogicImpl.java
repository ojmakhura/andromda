package org.andromda.metafacades.uml14;

import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ConstraintFacade.
 *
 * @see org.andromda.metafacades.uml.ConstraintFacade
 */
public class ConstraintFacadeLogicImpl
       extends ConstraintFacadeLogic
       implements org.andromda.metafacades.uml.ConstraintFacade
{
    // ---------------- constructor -------------------------------
    
    public ConstraintFacadeLogicImpl (org.omg.uml.foundation.core.Constraint metaObject, java.lang.String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ConstraintFacade ...

	/**
	 * @see org.andromda.metafacades.uml.ConstraintFacade#getBody()
	 */
    public java.lang.String getBody() 
    {   
        return this.metaObject.getBody().getBody();
    }

    // ------------- relations ------------------
    
    /**
     * @see org.andromda.metafacades.uml.ConstraintFacade#getContextElement()
     */
    public java.lang.Object handleGetContextElement()
    {
        Object element = null;
        List elements = this.metaObject.getConstrainedElement();
        if (elements != null && !elements.isEmpty()) {
            element = elements.get(0);
        }
        return element;
    }

}
