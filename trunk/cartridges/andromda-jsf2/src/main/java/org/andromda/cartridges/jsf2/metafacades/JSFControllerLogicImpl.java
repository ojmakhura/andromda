package org.andromda.cartridges.jsf2.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.andromda.cartridges.jsf2.JSFGlobals;
import org.andromda.cartridges.jsf2.JSFUtils;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.Service;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf2.metafacades.JSFController.
 *
 * @see org.andromda.cartridges.jsf2.metafacades.JSFController
 */
public class JSFControllerLogicImpl
    extends JSFControllerLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFControllerLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return implementationName
     * @see org.andromda.cartridges.jsf2.metafacades.JSFController#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        final String pattern = ObjectUtils.toString(
            this.getConfiguredProperty(JSFGlobals.CONTROLLER_IMPLEMENTATION_PATTERN));
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @return fullyQualifiedImplementationName
     * @see org.andromda.cartridges.jsf2.metafacades.JSFController#getFullyQualifiedImplementationName()
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
     * @see org.andromda.cartridges.jsf2.metafacades.JSFController#getFullyQualifiedImplementationPath()
     */
    protected String handleGetFullyQualifiedImplementationPath()
    {
        return this.getFullyQualifiedImplementationName().replace('.', '/');
    }

    /**
     * @return StringUtilsHelper.lowerCamelCaseName(this.getName())
     * @see org.andromda.cartridges.jsf2.metafacades.JSFController#getBeanName()
     */
    protected String handleGetBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @return references
     * @see org.andromda.cartridges.jsf2.metafacades.JSFController#getSessionObjectReferences()
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
     * @see org.andromda.cartridges.jsf2.metafacades.JSFController#getControllerSerialVersionUID()
     */
    protected String handleGetControllerSerialVersionUID()
    {
       final StringBuilder buffer = new StringBuilder();
       buffer.append(this.getFullyQualifiedImplementationName());
       addSerialUIDData(buffer);
       return JSFUtils.calcSerialVersionUID(buffer);
    }

    private void addSerialUIDData(StringBuilder buffer)
    {
        for (final FrontEndAction action : this.getUseCase().getActions())
        {
            buffer.append(action.getName());
        }
    }

    /**
     * @see org.andromda.cartridges.jsf2.metafacades.JSFController#getAllServices()
     */
    @Override
    protected Collection<Service> handleGetAllServices() {
        final Set<Service> allServices=new HashSet<Service>();
        for(final DependencyFacade dependency: this.getServiceReferences())
        {
            allServices.add((Service)dependency.getTargetElement());
        }
        for(final DependencyFacade dependency: this.getServicesPackagesReferences())
        {
            final PackageFacade pack=(PackageFacade)dependency.getTargetElement();
            for(final ClassifierFacade clazz: pack.getClasses())
            {
                if(clazz instanceof Service)
                {
                    allServices.add((Service)clazz);
                }
            }
        }
        for(final FrontEndAction action: getUseCase().getActions())
        {
            for(final FrontEndActionState as: action.getActionStates())
            {
                for(final OperationFacade operation: as.getServiceCalls())
                {
                    allServices.add((Service)operation.getOwner());
                }
            }
        }
        return allServices;
    }

    /**
     * @see org.andromda.cartridges.jsf2.metafacades.JSFController#getServicesPackagesReferences()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected List<PackageFacade> handleGetServicesPackagesReferences() {
        return (List<PackageFacade>)new FilteredCollection(this.getSourceDependencies())
        {
            private static final long serialVersionUID = 134L;
            @Override
            public boolean evaluate(final Object object)
            {
                return ((DependencyFacade)object).getTargetElement() instanceof PackageFacade;
            }
        };
    }
}
