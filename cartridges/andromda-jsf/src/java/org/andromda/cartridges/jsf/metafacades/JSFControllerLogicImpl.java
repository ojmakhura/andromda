package org.andromda.cartridges.jsf.metafacades;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
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

    /**
     * Public constructor for JSFControllerLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.jsf.metafacades.JSFController
     */
    public JSFControllerLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @return ImplementationName
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.CONTROLLER_IMPLEMENTATION_PATTERN));
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @return FullyQualifiedImplementationName
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getFullyQualifiedImplementationName()
     */
    protected String handleGetFullyQualifiedImplementationName()
    {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName).append('.');
        }
        return fullyQualifiedName.append(this.getImplementationName()).toString();
    }

    /**
     * @return FullyQualifiedImplementationPath
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getFullyQualifiedImplementationPath()
     */
    protected String handleGetFullyQualifiedImplementationPath()
    {
        return this.getFullyQualifiedImplementationName().replace('.', '/');
    }

    /**
     * @return BeanName
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getBeanName()
     */
    protected String handleGetBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @return SessionObjectReferences
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getSessionObjectReferences()
     */
    protected List handleGetSessionObjectReferences()
    {
        final List references = new ArrayList(this.getSourceDependencies());
        for (final Iterator iterator = references.iterator(); iterator.hasNext();)
        {
            final ModelElementFacade targetElement = ((DependencyFacade)iterator.next()).getTargetElement();
            if (!(targetElement instanceof JSFSessionObject))
            {
                iterator.remove();   
            }
        }
        return references;
    }
}