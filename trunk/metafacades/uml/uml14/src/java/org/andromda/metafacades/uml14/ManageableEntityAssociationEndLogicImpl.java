package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.Entity;

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
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getCrudName()
     */
    protected java.lang.String handleGetCrudName()
    {
        return getName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getCrudGetterName()
     */
    protected java.lang.String handleGetCrudGetterName()
    {
        return getGetterName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getCrudSetterName()
     */
    protected java.lang.String handleGetCrudSetterName()
    {
        return getSetterName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getCrudType()
     */
    protected java.lang.Object handleGetCrudType()
    {
        ClassifierFacade crudType = null;

        final ClassifierFacade classifierFacade = getType();
        if (classifierFacade instanceof Entity)
        {
            final Entity entity = (Entity)classifierFacade;
            final Collection identifiers = entity.getIdentifiers();

            if (!identifiers.isEmpty())
            {
                final AttributeFacade identifier = (AttributeFacade)identifiers.iterator().next();
                crudType = identifier.getType();
            }
        }

        return crudType;
    }

}