package org.andromda.metafacades.uml14;




/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationLiteralFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationLiteralFacade
 */
public class EnumerationLiteralFacadeLogicImpl
       extends EnumerationLiteralFacadeLogic
       implements org.andromda.metafacades.uml.EnumerationLiteralFacade
{
    // ---------------- constructor -------------------------------

    public EnumerationLiteralFacadeLogicImpl (org.omg.uml.foundation.core.EnumerationLiteral metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getEnumeration()
     */
    protected java.lang.Object handleGetEnumeration()
    {
        return metaObject.getEnumeration();
    }

}
