package org.andromda.metafacades.uml14;

import org.andromda.core.common.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EnumerationLiteralFacade.
 * 
 * @see org.andromda.metafacades.uml.EnumerationLiteralFacade
 */
public class EnumerationLiteralFacadeLogicImpl
    extends EnumerationLiteralFacadeLogic
    implements org.andromda.metafacades.uml.EnumerationLiteralFacade
{
    // ---------------- constructor -------------------------------

    public EnumerationLiteralFacadeLogicImpl(
        org.omg.uml.foundation.core.EnumerationLiteral metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        return StringUtilsHelper.separate(super.getName(), "_").toUpperCase();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getEnumeration()
     */
    protected java.lang.Object handleGetEnumeration()
    {
        return metaObject.getEnumeration();
    }

    /**
     * @see org.andromda.metafacades.uml14.EnumerationLiteralFacadeLogic#handleGetValue()
     */
    protected String handleGetValue()
    {
        return StringUtils.trimToEmpty(super.getName());
    }
}