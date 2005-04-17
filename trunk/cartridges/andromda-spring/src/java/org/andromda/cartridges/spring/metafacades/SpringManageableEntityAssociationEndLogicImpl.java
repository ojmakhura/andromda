package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd
 */
public class SpringManageableEntityAssociationEndLogicImpl
    extends SpringManageableEntityAssociationEndLogic
{
    // ---------------- constructor -------------------------------

    public SpringManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected java.lang.String handleGetDaoName()
    {
        return getName() + "Dao";
    }

    protected java.lang.String handleGetDaoReferenceName()
    {
        String referenceName = null;

        final ClassifierFacade type = getType();
        if (type != null)
        {
            final char[] name = getName().toCharArray();
            if (name.length > 0)
            {
                name[0] = Character.toLowerCase(name[0]);
            }
            referenceName = new String(name) + "Dao";
        }

        return referenceName;
    }

    protected java.lang.String handleGetDaoGetterName()
    {
        return getGetterName() + "Dao";
    }

    protected java.lang.String handleGetDaoSetterName()
    {
        return getSetterName() + "Dao";
    }

}