package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.datatypes.Expression;
import org.andromda.core.common.StringUtilsHelper;


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

    /**
     * This method is overridden to make sure the parameter name will result in uncompilable Java code.
     */
    public String getName()
    {
        return StringUtilsHelper.toJavaMethodName(super.getName());
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
