package org.andromda.metafacades.uml14;

import org.omg.uml.foundation.datatypes.Expression;

import java.util.Collection;


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

    public boolean isPrimitive()
    {
        final String type = getType().getFullyQualifiedName();
        return  "int".equals(type) ||
                "float".equals(type) ||
                "double".equals(type) ||
                "short".equals(type) ||
                "long".equals(type) ||
                "byte".equals(type) ||
                "char".equals(type) ||
                "boolean".equals(type);
    }

    public boolean isCollection()
    {
        try
        {
            Class parameterClass = Class.forName(getType().getFullyQualifiedName());
            Class collectionClass = Class.forName(Collection.class.getName());
            return collectionClass.isAssignableFrom(parameterClass);
        }
        catch(Exception exception)
        {
            return false;
        }
    }

    public boolean isArray()
    {
        try
        {
            return Class.forName(getType().getFullyQualifiedName()).isArray();
        }
        catch(Exception exception)
        {
            return false;
        }
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
