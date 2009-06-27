package org.andromda.cartridges.spring.metafacades;

import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringQueue.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringQueue
 */
public class SpringQueueLogicImpl
    extends SpringQueueLogic
{

    public SpringQueueLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue#getBeanName()
     */
    protected java.lang.String handleGetBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue#getTemplateBeanName()
     */
    protected String handleGetTemplateBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getTemplatePattern().replaceAll(
            "\\{0\\}",
            this.getName()));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue#getTemplateGetterName()
     */
    protected String handleGetTemplateGetterName()
    {
        return "get" + StringUtilsHelper.capitalize(this.getTemplateBeanName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue#getTemplateSetterName()
     */
    protected String handleGetTemplateSetterName()
    {
        return "set" + StringUtilsHelper.capitalize(this.getTemplateBeanName());
    }
    
    /**
     * Gets the value of the {@link SpringGlobals#JMS_DESTINATION_TEMPLATE_PATTERN}
     *
     * @return the template name pattern.
     */
    private String getTemplatePattern()
    {
        return String.valueOf(this.getConfiguredProperty(SpringGlobals.JMS_DESTINATION_TEMPLATE_PATTERN));
    }
}