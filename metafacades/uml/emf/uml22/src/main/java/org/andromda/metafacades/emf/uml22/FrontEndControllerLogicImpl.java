package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndSessionObject;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.web.MetafacadeWebGlobals;
import org.andromda.metafacades.uml.web.MetafacadeWebUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.UseCase;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.FrontEndController.
 *
 * @see org.andromda.metafacades.uml.FrontEndController
 */
public class FrontEndControllerLogicImpl
    extends FrontEndControllerLogic
{
    private static final long serialVersionUID = -8765076432370916838L;

    /**
     * The logger instance.
    private static final Logger LOGGER = Logger.getLogger(FrontEndControllerLogicImpl.class);
     */

    /**
     * @param metaObject
     * @param context
     */
    public FrontEndControllerLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getServiceReferences()
     */
    @Override
    protected List<DependencyFacade> handleGetServiceReferences()
    {
        return new FilteredCollection(this.getSourceDependencies())
        {
            private static final long serialVersionUID = 34L;
            @Override
            public boolean evaluate(final Object object)
            {
                return ((DependencyFacade)object).getTargetElement() instanceof Service;
            }
        };
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getUseCase()
     */
    @Override
    protected Element handleGetUseCase()
    {
        Element owner = (Classifier)this.metaObject;
        while (!(owner == null || owner instanceof UseCase))
        {
            owner = owner.getOwner();
        }
        return owner;
    }

    /**
     * @see org.andromda.metafacades.uml.FrontEndController#getDeferringActions()
     */
    @Override
    protected List<FrontEndAction> handleGetDeferringActions()
    {
        final Collection<FrontEndAction> deferringActions = new LinkedHashSet<FrontEndAction>();

        for (final OperationFacade operation : this.getOperations())
        {
            final FrontEndControllerOperation controllerOperation = (FrontEndControllerOperation)operation;
            deferringActions.addAll(controllerOperation.getDeferringActions());
        }
        return new ArrayList<FrontEndAction>(deferringActions);
    }

    @Override
    protected String handleGetImplementationName() {
        final String pattern = Objects.toString(
            this.getConfiguredProperty(MetafacadeWebGlobals.CONTROLLER_IMPLEMENTATION_PATTERN), "");
        return pattern.replaceFirst("\\{0\\}", StringUtils.capitalize(this.getName()));
    }

    @Override
    protected String handleGetFullyQualifiedImplementationName() {
        final StringBuilder fullyQualifiedName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append(packageName + '.');
        }
        return fullyQualifiedName.append(this.getImplementationName()).toString();
    }

    @Override
    protected String handleGetFullyQualifiedImplementationPath() {
        return this.getFullyQualifiedImplementationName().replace('.', '/');
    }

    @Override
    protected String handleGetBeanName() {
        return StringUtilsHelper.lowerCamelCaseName(this.getName());
    }

    @Override
    protected String handleGetControllerSerialVersionUID() {
        final StringBuilder buffer = new StringBuilder();
       buffer.append(this.getFullyQualifiedImplementationName());
       addSerialUIDData(buffer);
       return MetafacadeWebUtils.calcSerialVersionUID(buffer);
    }

    private void addSerialUIDData(StringBuilder buffer)
    {
        for (final FrontEndAction action : this.getUseCase().getActions())
        {
            buffer.append(action.getName());
        }
    }

    @Override
    protected Collection handleGetAllServices() {
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

    @Override
    protected Collection handleGetAllowedOperations() {
        List<OperationFacade> operations = new ArrayList<OperationFacade>(this.getOperations());
        CollectionUtils.filter(
            operations,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean valid = FrontEndControllerOperation.class.isAssignableFrom(object.getClass());
                    if (valid)
                    {
                        valid = ((FrontEndControllerOperation)object).isExposed();
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

    @Override
    protected Collection handleGetSessionObjectReferences() {
        final List<DependencyFacade> references = new ArrayList<DependencyFacade>(this.getSourceDependencies());
        for (final Iterator<DependencyFacade> iterator = references.iterator(); iterator.hasNext();)
        {
            final ModelElementFacade targetElement = (iterator.next()).getTargetElement();
            if (!(targetElement instanceof FrontEndSessionObject))
            {
                iterator.remove();
            }
        }
        return references;
    }

    @Override
    protected Collection handleGetServicesPackagesReferences() {
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
    protected String handleGetPath() {
                
        return UMLMetafacadeUtils.getPath(this);
    }

    @Override
    protected String handleGetRestPath() {
                
        return UMLMetafacadeUtils.getRestPath(this);
    }

    @Override
    protected String handleGetFilename() {
        return UMLMetafacadeUtils.getFilename(this);
    }

    @Override
    protected String handleGetTargetUrl() {
        return UMLMetafacadeUtils.getTargetUrl(this);
    }

    @Override
    protected Collection handleGetAllowedRoles() {
        return UMLMetafacadeUtils.getAllowedRoles(this);
    }

    // TODO: We may want to override getPackageName here, since in uml2
    // statemachine and usecase are "packages".
    // We may return the getUseCase package name
    // For now, in ModelElement, we are handling this case.
}
