package org.andromda.metafacades.uml14;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EnumerationFacade.
 * 
 * @see org.andromda.metafacades.uml.EnumerationFacade
 */
public class EnumerationFacadeLogicImpl
    extends EnumerationFacadeLogic
    implements org.andromda.metafacades.uml.EnumerationFacade
{
    // ---------------- constructor -------------------------------

    public EnumerationFacadeLogicImpl(
        org.omg.uml.foundation.core.Classifier metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiterals()
     */
    protected java.util.Collection handleGetLiterals()
    {
        return getAttributes();
    }
}