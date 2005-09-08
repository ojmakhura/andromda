package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.metafacades.JSFControllerLogic;
import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFController.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFController
 */
public class JSFControllerLogicImpl
    extends JSFControllerLogic
{

    public JSFControllerLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getImplementationName()
     */
    protected java.lang.String handleGetImplementationName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.CONTROLLER_IMPLEMENTATION_PATTERN));
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getFullyQualifiedImplementationName()
     */
    protected java.lang.String handleGetFullyQualifiedImplementationName()
    {
        final StringBuffer fullyQualifiedName = new StringBuffer();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(this.getImplementationName()).toString();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getFullyQualifiedImplementationPath()
     */
    protected String handleGetFullyQualifiedImplementationPath()
    {
        return this.getFullyQualifiedImplementationName().replace('.', '/');
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getBeanName()
     */
    protected String handleGetBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }
}