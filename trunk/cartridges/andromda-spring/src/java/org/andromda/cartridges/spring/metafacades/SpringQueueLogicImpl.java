package org.andromda.cartridges.spring.metafacades;

import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringQueue.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringQueue
 */
public class SpringQueueLogicImpl
    extends SpringQueueLogic
{

    /**
     * Public constructor for SpringQueueLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue
     */
    public SpringQueueLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return StringUtilsHelper.lowerCamelCaseName(this.getName())
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue#getBeanName()
     */
    protected String handleGetBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @return lowerCamelCaseName(this.getTemplatePattern().replaceAll("\\{0\\}", getName()))
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue#getTemplateBeanName()
     */
    protected String handleGetTemplateBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getTemplatePattern().replaceAll(
            "\\{0\\}",
            this.getName()));
    }

    /**
     * @return "get" + StringUtilsHelper.capitalize(this.getTemplateBeanName())
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue#getTemplateGetterName()
     */
    protected String handleGetTemplateGetterName()
    {
        return "get" + StringUtils.capitalize(this.getTemplateBeanName());
    }

    /**
     * @return "set" + StringUtilsHelper.capitalize(this.getTemplateBeanName())
     * @see org.andromda.cartridges.spring.metafacades.SpringQueue#getTemplateSetterName()
     */
    protected String handleGetTemplateSetterName()
    {
        return "set" + StringUtils.capitalize(this.getTemplateBeanName());
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