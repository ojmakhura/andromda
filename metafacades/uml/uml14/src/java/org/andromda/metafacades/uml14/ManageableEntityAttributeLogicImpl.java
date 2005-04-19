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
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#getCrudGetterName()
     */
    protected java.lang.String handleGetCrudGetterName()
    {
        return getGetterName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#getCrudName()
     */
    protected java.lang.String handleGetCrudName()
    {
        return getName();
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#getCrudSetterName()
     */
    protected java.lang.String handleGetCrudSetterName()
    {
        return getSetterName();
    }

}