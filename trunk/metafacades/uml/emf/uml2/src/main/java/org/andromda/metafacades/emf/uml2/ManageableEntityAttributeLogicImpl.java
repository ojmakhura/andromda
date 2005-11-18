package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntityAttribute.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAttribute
 */
public class ManageableEntityAttributeLogicImpl
    extends ManageableEntityAttributeLogic
{
    public ManageableEntityAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#isDisplay()
     */
    protected boolean handleIsDisplay()
    {
        // TODO: put your implementation here.
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#isManageableGetterAvailable()
     */
    protected boolean handleIsManageableGetterAvailable()
    {
        // TODO: put your implementation here.
        return false;
    }
}