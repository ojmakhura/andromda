package org.andromda.core.metadecorators.uml14;

import org.andromda.core.common.StringUtilsHelper;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Attribute
 *
 *
 */
public class AttributeDecoratorImpl extends AttributeDecorator
{
    // ---------------- constructor -------------------------------

    public AttributeDecoratorImpl(
        org.omg.uml.foundation.core.Attribute metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class AttributeDecorator ...

    public java.lang.String getGetterName()
    {
        return "get"
            + StringUtilsHelper.upperCaseFirstLetter(metaObject.getName());
    }

    public java.lang.String getSetterName()
    {
        return "set"
            + StringUtilsHelper.upperCaseFirstLetter(metaObject.getName());
    }

    // ------------- relations ------------------

}
