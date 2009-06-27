package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ManageableEntity;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ManageableEntityAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd
 */
public class ManageableEntityAssociationEndLogicImpl
    extends ManageableEntityAssociationEndLogic
{
    public ManageableEntityAssociationEndLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getManageableIdentifier()
     */
    protected Object handleGetManageableIdentifier()
    {
        AttributeFacade manageableIdentifier = null;

        final ClassifierFacade classifierFacade = this.getType();
        if (classifierFacade instanceof ManageableEntity)
        {
            final ManageableEntity entity = (ManageableEntity)classifierFacade;
            manageableIdentifier = entity.getManageableIdentifier();
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