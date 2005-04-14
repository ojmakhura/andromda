package org.andromda.metafacades.uml14;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.EntityAttribute;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntityIdentifier.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityIdentifier
 */
public class ManageableEntityAssociationEndLogicImpl
        extends ManageableEntityAssociationEndLogic
{
    // ---------------- constructor -------------------------------

    public ManageableEntityAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    protected String handleGetForeignName(EntityAttribute attribute)
    {
        String foreignName = getName();

        if (attribute != null)
        {
            foreignName += StringUtilsHelper.upperCamelCaseName(attribute.getName());
        }

        return foreignName;
    }

    protected String handleGetForeignGetterName(EntityAttribute attribute)
    {
        return "get" + StringUtilsHelper.upperCamelCaseName(getForeignName(attribute));
    }

    protected String handleGetForeignSetterName(EntityAttribute attribute)
    {
        return "set" + StringUtilsHelper.upperCamelCaseName(getForeignName(attribute));
    }
}