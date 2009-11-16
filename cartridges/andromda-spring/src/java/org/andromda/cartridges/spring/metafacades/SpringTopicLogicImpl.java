package org.andromda.cartridges.spring.metafacades;

import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringTopic.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringTopic
 */
public class SpringTopicLogicImpl
    extends SpringTopicLogic
{

    /**
     * Public constructor for SpringTopicLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringTopic
     */
    public SpringTopicLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @return lowerCamelCaseName(this.getName())
     * @see org.andromda.cartridges.spring.metafacades.SpringTopic#getBeanName()
     */
    protected String handleGetBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @return lowerCamelCaseName(this.getTemplatePattern().replaceAll("\\{0\\}", getName()))
     * @see org.andromda.cartridges.spring.metafacades.SpringTopic#getTemplateBeanName()
     */
    protected String handleGetTemplateBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getTemplatePattern().replaceAll(
            "\\{0\\}",
            this.getName()));
    }

    /**
     * @return "get" + StringUtilsHelper.capitalize(this.getTemplateBeanName())
     * @see org.andromda.cartridges.spring.metafacades.SpringTopic#getTemplateGetterName()
     */
    protected String handleGetTemplateGetterName()
    {
        return "get" + StringUtils.capitalize(this.getTemplateBeanName());
    }

    /**
     * @return "set" + StringUtilsHelper.capitalize(this.getTemplateBeanName())
     * @see org.andromda.cartridges.spring.metafacades.SpringTopic#getTemplateSetterName()
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