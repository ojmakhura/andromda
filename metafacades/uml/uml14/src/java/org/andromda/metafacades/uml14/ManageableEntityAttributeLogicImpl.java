package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntityAttribute.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAttribute
 */
public class ManageableEntityAttributeLogicImpl
    extends ManageableEntityAttributeLogic
{
    // ---------------- constructor -------------------------------

    public ManageableEntityAttributeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#getManageableGetterName()
     */
    protected java.lang.String handleGetManageableGetterName()
    {
        return getGetterName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#getManageableName()
     */
    protected java.lang.String handleGetManageableName()
    {
        return getName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#getManageableSetterName()
     */
    protected java.lang.String handleGetManageableSetterName()
    {
        return getSetterName();
    }

}