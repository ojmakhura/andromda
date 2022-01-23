package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.andromda.cartridges.thymeleaf.ThymeleafGlobals;
import org.andromda.cartridges.thymeleaf.ThymeleafUtils;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController
 */
public class ThymeleafControllerLogicImpl
    extends ThymeleafControllerLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafControllerLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return implementationName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        final String pattern = Objects.toString(
            this.getConfiguredProperty(ThymeleafGlobals.CONTROLLER_IMPLEMENTATION_PATTERN), "");
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    /**
     * @return fullyQualifiedImplementationName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController#getFullyQualifiedImplementationName()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController#getFullyQualifiedImplementationPath()
     */
    protected String handleGetFullyQualifiedImplementationPath()
    {
        return this.getFullyQualifiedImplementationName().replace('.', '/');
    }

    /**
     * @return StringUtilsHelper.lowerCamelCaseName(this.getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController#getBeanName()
     */
    protected String handleGetBeanName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    /**
     * @return references
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController#getSessionObjectReferences()
     */
    protected List<DependencyFacade> handleGetSessionObjectReferences()
    {
        final List<DependencyFacade> references = new ArrayList<DependencyFacade>(this.getSourceDependencies());
        for (final Iterator<DependencyFacade> iterator = references.iterator(); iterator.hasNext();)
        {
            final ModelElementFacade targetElement = (iterator.next()).getTargetElement();
            if (!(targetElement instanceof ThymeleafSessionObject))
            {
                iterator.remove();
            }
        }
        return references;
    }

    /**
     * @return controllerSerialVersionUID
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController#getControllerSerialVersionUID()
     */
    protected String handleGetControllerSerialVersionUID()
    {
       final StringBuilder buffer = new StringBuilder();
       buffer.append(this.getFullyQualifiedImplementationName());
       addSerialUIDData(buffer);
       return ThymeleafUtils.calcSerialVersionUID(buffer);
    }

    private void addSerialUIDData(StringBuilder buffer)
    {
        for (final FrontEndAction action : this.getUseCase().getActions())
        {
            buffer.append(action.getName());
        }
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController#getAllServices()
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
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafController#getServicesPackagesReferences()
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

    @Override
    protected boolean handleIsRestAtom() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected int handleGetJaxwsCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    private static final String EMPTY_STRING = "";
    private static final String DEFAULT = "default";

    @Override
    protected String handleGetRestMethod() {
        
        String method = (String)this.findTaggedValue(ThymeleafGlobals.REST_HTTP_METHOD);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(method) || method.equals(DEFAULT))
        {
            method = EMPTY_STRING;
        }
        return method;
    }

    @Override
    protected String handleGetRestRetention() {
        String retention = (String)this.findTaggedValue(ThymeleafGlobals.REST_RETENTION);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(retention) || retention.equals(DEFAULT))
        {
            retention = EMPTY_STRING;
        }
        return retention;
    }

    @Override
    protected String handleGetRestTarget() {
        String target = (String)this.findTaggedValue(ThymeleafGlobals.REST_TARGET);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(target) || target.equals(DEFAULT))
        {
            target = EMPTY_STRING;
        }
        return target;
    }

    @Override
    protected String handleGetRestProvider() {
        String provider = (String)this.findTaggedValue(ThymeleafGlobals.REST_PROVIDER);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(provider) || provider.equals(DEFAULT))
        {
            provider = EMPTY_STRING;
        }
        return provider;
    }

    @Override
    protected String handleGetRestProduces() {
        return ThymeleafControllerOperationLogicImpl.translateMediaType((String)this.findTaggedValue(ThymeleafGlobals.REST_PRODUCES));
    }

    @Override
    protected String handleGetRestConsumes() {
        String consumes = (String)this.findTaggedValue(ThymeleafGlobals.REST_CONSUMES);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(consumes) || consumes.equals(DEFAULT))
        {
            consumes = EMPTY_STRING;
        }
        else
        {
            consumes = ThymeleafControllerOperationLogicImpl.translateMediaType(consumes);
        }
        return consumes;
    }

    /**
     * The property defining if the web service XML should be validated against the wsdl/xsd schema.
     */
    private static final String PROPERTY_SCHEMA_VALIDATION = "schemaValidation";
    private static final String BOOLEAN_FALSE = "false";
    private static final String BOOLEAN_TRUE = "true";

    @Override
    protected String handleGetRestCacheType() {
        String cacheType = (String)this.findTaggedValue(ThymeleafGlobals.CACHE_TYPE);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(cacheType) || cacheType.equals(DEFAULT))
        {
            cacheType = EMPTY_STRING;
        }
        return cacheType;
    }

    @Override
    protected int handleGetRestCount() {
        int restCount = 0;
        String rest = (String)this.findTaggedValue(ThymeleafGlobals.REST);
        for (ThymeleafControllerOperation operation : this.getAllowedOperations())
        {
            if (StringUtils.isNotBlank(rest) && (operation.isRest() || rest.equals(BOOLEAN_TRUE)))
            {
                restCount++;
            }
        }
        return restCount;
    }

    @Override
    protected Collection<String> handleGetRestContexts() {
        List<String> contexts = new ArrayList<String>();
        String context = (String)this.findTaggedValue(ThymeleafGlobals.REST_CONTEXT);
        if (!(this.getRestCount()>0) || StringUtils.isBlank(context) || context.equals(DEFAULT))
        {
            context = EMPTY_STRING;
        }
        else
        {
            // Parse comma/pipe/semicolon delimited elements into ArrayList
            String[] parsed = StringUtils.split(context, ",;|");
            for (int i=0; i<parsed.length; i++)
            {
                contexts.add(parsed[i]);
            }
        }
        return contexts;
    }

    private static final String SLASH = "/";
    private static final String QUOTE = "\"";

    @Override
    protected String handleGetRestPath() {
        String path = (String)this.findTaggedValue(ThymeleafGlobals.REST_PATH);
        if (StringUtils.isBlank(path))
        {
            path = EMPTY_STRING;
        }

        if (!(this.getRestCount()>0) || StringUtils.isBlank(path) || path.equals(DEFAULT))
        {
            path = ThymeleafUtils.toWebResourceName(this.getName());
        }
        else
        {
            if (!path.startsWith(QUOTE))
            {
                path = path;
            }
            if (!path.endsWith(QUOTE) || path.length()<2)
            {
                path = path;
            }
        
            if(!path.startsWith(SLASH)) {
                path = SLASH + path;
            }
            
            if(path.endsWith(SLASH)) {
                path = path.substring(0, path.length() - 1);
            }
        }
        return path;
    }

    @Override
    protected Collection handleGetAllowedOperations() {
        List<OperationFacade> operations = new ArrayList<OperationFacade>(this.getOperations());
        CollectionUtils.filter(
            operations,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = ThymeleafControllerOperation.class.isAssignableFrom(object.getClass());
                    if (valid)
                    {
                        valid = ((ThymeleafControllerOperation)object).isExposed();
                    }
                    return valid;
                }
            });
        // if (this.getWSDLOperationSortMode().equals(OPERATION_SORT_MODE_NAME))
        // {
        //     Collections.sort(
        //         operations,
        //         new OperationNameComparator());
        // }
        return operations;
    }
}
