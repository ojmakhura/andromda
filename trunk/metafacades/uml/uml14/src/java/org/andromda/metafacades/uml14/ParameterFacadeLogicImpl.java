package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.datatypes.Expression;


/**
 * Metaclass facade implementation.
 */
public class ParameterFacadeLogicImpl
       extends ParameterFacadeLogic
       implements org.andromda.metafacades.uml.ParameterFacade
{
    // ---------------- constructor -------------------------------

    public ParameterFacadeLogicImpl (org.omg.uml.foundation.core.Parameter metaObject, String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ParameterDecorator ...
    
    public String handleGetDefaultValue()
    {
        final Expression expression = metaObject.getDefaultValue();
        return (expression == null) ? "" : expression.getBody();
    }


    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getType()
     */
    protected Object handleGetType()
    {
        return metaObject.getType();
    }

    // ------------------------------------------------------------

}
