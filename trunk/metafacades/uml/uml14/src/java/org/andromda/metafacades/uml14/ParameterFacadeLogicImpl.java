package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.datatypes.Expression;


/**
 *
 *
 * Metaclass facade implementation.
 *
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

    public String getDefaultValue()
    {
        final Expression expression = metaObject.getDefaultValue();
        return (expression == null) ? "" : expression.getBody();
    }


    // ------------- relations ------------------

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ParameterDecorator#handleGetType()
     */
    protected Object handleGetType()
    {
        return metaObject.getType();
    }

    // ------------------------------------------------------------

}
