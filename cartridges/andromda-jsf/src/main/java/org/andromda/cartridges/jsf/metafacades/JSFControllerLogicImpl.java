package org.andromda.cartridges.jsf.metafacades;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FrontEndAction;
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
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFControllerLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @return implementationName
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.CONTROLLER_IMPLEMENTATION_PATTERN));
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @return fullyQualifiedImplementationName
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getFullyQualifiedImplementationName()
     */
    protected String handleGetFullyQualifiedImplementationName()
    {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(this.getImplementationName()).toString();
    }

    /**
     * @return getFullyQualifiedImplementationName().replace('.', '/')
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getFullyQualifiedImplementationPath()
     */
    protected String handleGetFullyQualifiedImplementationPath()
    {
        return this.getFullyQualifiedImplementationName().replace('.', '/');
    }

    /**
     * @return StringUtilsHelper.lowerCamelCaseName(this.getName())
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getBeanName()
     */
    protected String handleGetBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @return references
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getSessionObjectReferences()
     */
    protected List<DependencyFacade> handleGetSessionObjectReferences()
    {
        final List<DependencyFacade> references = new ArrayList<DependencyFacade>(this.getSourceDependencies());
        for (final Iterator<DependencyFacade> iterator = references.iterator(); iterator.hasNext();)
        {
            final ModelElementFacade targetElement = (iterator.next()).getTargetElement();
            if (!(targetElement instanceof JSFSessionObject))
            {
                iterator.remove();   
            }
        }
        return references;
    }
    
    /**
     * @return controllerSerialVersionUID
     * @see org.andromda.cartridges.jsf.metafacades.JSFController#getControllerSerialVersionUID()
     */
    protected String handleGetControllerSerialVersionUID()
    {
           final StringBuilder buffer = new StringBuilder();
           buffer.append(this.getFullyQualifiedImplementationName());
           addSerialUIDData(buffer);
           return JSFUtils.calcSerialVersionUID(buffer);
    }

    private void addSerialUIDData(StringBuilder buffer){
        for (final FrontEndAction action : this.getUseCase().getActions())
        {
            buffer.append(action.getName());
        }
    }
}