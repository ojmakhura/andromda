package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.EntityAttribute;

import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntityAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd
 */
public class ManageableEntityAssociationEndLogicImpl
    extends ManageableEntityAssociationEndLogic
{
    // ---------------- constructor -------------------------------

    public ManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getManageableName()
     */
    protected java.lang.String handleGetManageableName()
    {
        return getName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getManageableGetterName()
     */
    protected java.lang.String handleGetManageableGetterName()
    {
        return getGetterName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getManageableSetterName()
     */
    protected java.lang.String handleGetManageableSetterName()
    {
        return getSetterName();
    }

    protected Object handleGetManageableIdentifier()
    {
        EntityAttribute manageableIdentifier = null;

        final ClassifierFacade classifierFacade = getType();
        if (classifierFacade instanceof Entity)
        {
            final Entity entity = (Entity)classifierFacade;
            final Collection identifiers = entity.getIdentifiers();

            if (!identifiers.isEmpty())
            {
                manageableIdentifier = (EntityAttribute)identifiers.iterator().next();
            }
        }

        return manageableIdentifier;
    }
}