package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.ManageableEntity;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntityAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd
 * @author Bob Fields
 */
public class ManageableEntityAssociationEndLogicImpl
    extends ManageableEntityAssociationEndLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ManageableEntityAssociationEndLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#getManageableIdentifier()
     */
    @Override
    protected EntityAttribute handleGetManageableIdentifier()
    {
        AttributeFacade manageableIdentifier = null;

        final ClassifierFacade classifierFacade = this.getType();
        if (classifierFacade instanceof ManageableEntity)
        {
            final ManageableEntity entity = (ManageableEntity)classifierFacade;
            manageableIdentifier = entity.getManageableIdentifier();
        }

        return (EntityAttribute)manageableIdentifier;
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAssociationEnd#isDisplay()
     */
    @Override
    protected boolean handleIsDisplay()
    {
        // we always display association ends
        return true;
    }
}