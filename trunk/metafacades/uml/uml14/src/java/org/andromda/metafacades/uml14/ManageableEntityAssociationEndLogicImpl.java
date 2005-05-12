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

    public ManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getManageableIdentifier()
     */
    protected Object handleGetManageableIdentifier()
    {
        AttributeFacade manageableIdentifier = null;

        final ClassifierFacade classifierFacade = getType();
        if (classifierFacade instanceof Entity)
        {
            final Entity entity = (Entity)classifierFacade;
            final Collection identifiers = entity.getIdentifiers();

            if (!identifiers.isEmpty())
            {
                manageableIdentifier = (AttributeFacade)identifiers.iterator().next();
            }
        }

        return manageableIdentifier;
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#isDisplay()
     */
    protected boolean handleIsDisplay()
    {
        // we always display association ends
        return true;
    }
}