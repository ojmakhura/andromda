package org.andromda.core.metadecorators.uml14;



/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Parameter
 *
 *
 */
public class ParameterDecoratorImpl extends ParameterDecorator
{
    // ---------------- constructor -------------------------------
    
    public ParameterDecoratorImpl (org.omg.uml.foundation.core.Parameter metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ParameterDecorator ...

    // ------------- relations ------------------
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ParameterDecorator#handleGetType()
     */
    public org.omg.uml.foundation.core.ModelElement handleGetType()
    {
        return metaObject.getType();
    }

    // ------------------------------------------------------------

}
