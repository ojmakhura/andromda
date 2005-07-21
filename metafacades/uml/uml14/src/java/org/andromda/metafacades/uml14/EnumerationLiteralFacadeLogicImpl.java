package org.andromda.metafacades.uml14;

import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationLiteralFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationLiteralFacade
 */
public class EnumerationLiteralFacadeLogicImpl
    extends EnumerationLiteralFacadeLogic
{
    public EnumerationLiteralFacadeLogicImpl(
        org.omg.uml.foundation.core.EnumerationLiteral metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetName()
     */
    protected String handleGetName()
    {
        return StringUtilsHelper.separate(
            super.handleGetName(),
            "_").toUpperCase();
    }

    /**
     * @see org.andromda.metafacades.uml14.EnumerationLiteralFacade#getDefinitionOne()
     */
    protected String handleGetValue()
    {
        return StringUtils.trimToEmpty(super.handleGetName());
    }
}