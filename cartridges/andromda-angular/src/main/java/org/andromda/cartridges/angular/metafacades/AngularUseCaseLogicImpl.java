// license-header java merge-point
//
// Generated by: MetafacadeLogicImpl.vsl in andromda-meta-cartridge.
package org.andromda.cartridges.angular.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import org.andromda.cartridges.angular.AngularGlobals;
import org.andromda.cartridges.angular.AngularProfile;
import org.andromda.cartridges.angular.AngularUtils;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndFinalState;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.IncludeFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents a Angular use case.
 * MetafacadeLogic implementation for AngularUseCase.
 *
 * @see AngularUseCase
 */
public class AngularUseCaseLogicImpl
    extends AngularUseCaseLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * Public constructor for AngularUseCaseLogicImpl
     * @see AngularUseCase
     */
    public AngularUseCaseLogicImpl (Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return actionPath
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCase#getPath()
     */
    @Override
    public String getPath()
    {
        // String actionPath = null;
        // final FrontEndActivityGraph graph = this.getActivityGraph();
        // if (graph != null)
        // {
        //     final AngularAction action = (AngularAction)graph.getInitialAction();
        //     if (action != null)
        //     {
        //         actionPath = action.getPath();
        //     }
        // }
        return "view/" + super.getPath();
    }

    /**
     * Collects all attribute messages into the given Map.
     *
     * @param messages the Map in which messages are collected.
     * @param attributes the attributes to collect the messages from.
     * @param resolvingTypes used to prevent endless recursion.
     */
    private void collectAttributeMessages(Map<String,String> messages, Collection attributes,
        final Collection<ClassifierFacade> resolvingTypes)
    {
        if (attributes != null && !attributes.isEmpty())
        {
            for (final Iterator iterator = attributes.iterator(); iterator.hasNext(); )
            {
                final AngularAttribute attribute = (AngularAttribute)iterator.next();
                messages.put(
                    attribute.getMessageKey(),
                    attribute.getMessageValue());
                // - lets go another level for nested attributes
                this.collectTypeMessages(messages, attribute.getType(), resolvingTypes);
            }
        }
    }

    /**
     * Collects all association end messages into the given Map.
     *
     * @param messages the Map in which messages are collected.
     * @param associationEnds the association ends to collect the messages from.
     * @param resolvingTypes used to prevent endless recursion.
     */
    private void collectAssociationEndMessages(Map<String,String> messages, Collection associationEnds,
        final Collection<ClassifierFacade> resolvingTypes)
    {
        if (associationEnds != null && !associationEnds.isEmpty())
        {
            for (final Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
            {
                final AssociationEndFacade end = (AssociationEndFacade)iterator.next();
                this.collectTypeMessages(messages, end.getType(), resolvingTypes);
            }
        }
    }

    private void collectTypeMessages(Map<String,String> messages, ClassifierFacade type,
         final Collection<ClassifierFacade> resolvingTypes)
    {
        if (type != null)
        {
            if (!resolvingTypes.contains(type))
            {
                resolvingTypes.add(type);
                if (type.isArrayType())
                {
                    type = type.getNonArray();
                }
                //check again, since the type can be changed
                if (!resolvingTypes.contains(type))
                {
                    this.collectAttributeMessages(messages, type.getAttributes(), resolvingTypes);
                    this.collectAssociationEndMessages(messages, type.getNavigableConnectingEnds(), resolvingTypes);
                }
            }
            resolvingTypes.remove(type);
        }
    }

    /**
     * @return navigationRules
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCase#getNavigationRules()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Collection<Object> handleGetNavigationRules()
    {
        final Map<String, Object> rules = new LinkedHashMap<String, Object>();
        for (final FrontEndView feView : this.getViews())
        {
            final AngularView view = (AngularView)feView;
            rules.put(view.getFromOutcome(), view);
            for (final Iterator forwardIterator = view.getForwards().iterator(); forwardIterator.hasNext();)
            {
                final Object forward = forwardIterator.next();
                String name;
                if (forward instanceof AngularForward)
                {
                    name = ((AngularForward)forward).getFromOutcome();
                }
                else
                {
                    name = ((AngularAction)forward).getFromOutcome();
                }
                rules.put(name, forward);
            }
        }
        return rules.values();
    }

    /**
     * @return navigationChildren
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCase#getNavigationChildren()
     */
    @Override
    protected Collection<UseCaseFacade> handleGetNavigationChildren()
    {
        return CollectionUtils.collect(getIncludes(), new Transformer()
        {
            public Object transform(Object object)
            {
                final IncludeFacade include = (IncludeFacade)object;
                return include.getAddition();
            }
        });
    }

    private static boolean isParent(final AngularUseCase useCase1, final AngularUseCase useCase2)
    {
        return CollectionUtils.exists(useCase2.getIncludes(), new Predicate()
        {
            public boolean evaluate(Object object)
            {
                final IncludeFacade include = (IncludeFacade)object;
                return include.getAddition().equals(useCase1);
            }
        });
    }

    /**
     * @return navigationParents
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCase#getNavigationParents()
     */
    @Override
    protected Collection<FrontEndUseCase> handleGetNavigationParents()
    {
        final AngularUseCase theUseCase = this;
        return CollectionUtils.select(getAllUseCases(),new Predicate() {
            @SuppressWarnings("synthetic-access")
            public boolean evaluate(Object o)
            {
                final AngularUseCase useCase = (AngularUseCase)o;
                if (theUseCase.equals(useCase))
                {
                    return false;
                }
                return isParent(theUseCase, useCase);
            }
        });
    }

    /**
     * @return actionRoles
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCase#getActionRoles()
     */
    @Override
    protected String handleGetActionRoles()
    {
        final StringBuilder rolesBuffer = new StringBuilder();
        boolean first = true;
        for (final Role role : this.getRoles())
        {
            if (first)
            {
                first = false;
            }
            else
            {
                rolesBuffer.append(',');
            }
            rolesBuffer.append(role.getName());
        }
        return rolesBuffer.toString();
    }

    /**
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCaseLogic#handleGetPreferences()
     */
    @Override
    protected Object handleGetPreferences()
    {
        AngularPortletPreferences preferences = null;
        final Collection<DependencyFacade> dependencies = this.getSourceDependencies();
        if (dependencies != null && !dependencies.isEmpty())
        {
            for (final DependencyFacade dependency : dependencies)
            {
                final Object target = dependency.getTargetElement();
                if (dependency.getTargetElement() instanceof AngularPortletPreferences)
                {
                    preferences = (AngularPortletPreferences)target;
                    break;
                }
            }
        }
        return preferences;
    }

    /**
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCaseLogic#handleGetPortletEditForwardName()
     */
    @Override
    protected String handleGetPortletEditForwardName()
    {
        return this.getWebResourceName() + "-portlet-edit";
    }

    /**
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCaseLogic#handleGetPortletEditPath()
     */
    @Override
    protected String handleGetPortletEditPath()
    {
        return this.getPathRoot() + "/" + this.getPortletEditForwardName();
    }

    /**
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCaseLogic#handleGetPortletHelpForwardName()
     */
    @Override
    protected String handleGetPortletHelpForwardName()
    {
        return this.getWebResourceName() + "-portlet-help";
    }

    /**
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCaseLogic#handleGetPortletHelpPath()
     */
    @Override
    protected String handleGetPortletHelpPath()
    {
        return this.getPathRoot() + "/" + this.getPortletHelpForwardName();
    }

    /**
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCaseLogic#handleGetPortletViewForwardName()
     */
    @Override
    protected String handleGetPortletViewForwardName()
    {
        return this.getWebResourceName() + "-portlet-view";
    }

    private String getWebResourceName()
    {
        return AngularUtils.toWebResourceName(this.getName());
    }

    /**
     * @see org.andromda.cartridges.angular.metafacades.AngularUseCaseLogic#handleGetPortletViewPath()
     */
    @Override
    protected String handleGetPortletViewPath()
    {
        return this.getPath();
    }

    private void getMenuItems(AngularUseCase useCase, HashSet<ModelElementFacade> imports) {

        for(FrontEndView view : useCase.getViews()) {
            imports.add(view);
        }
    }

    @Override
    protected Collection<ModelElementFacade> handleGetImports() {

        HashSet<ModelElementFacade> imports = new HashSet<>();

        return imports;
    }

    @Override
    protected String handleGetRouterPath() {
        String path = StringUtils.strip(((String) this.findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_PATH)));
        if (StringUtils.isBlank(path)) {
            path = this.getPathRoot();
        }

        while(path.startsWith("/")) { /// remove leading '/'s
            path = path.substring(1);
        }

        return path;
    }

    @Override
    protected String handleGetModuleFilePath() {
        return "view" + this.getPathRoot() + "/" + this.getModuleFileName();
    }

    @Override
    protected String handleGetModuleFileName() {
        return MetafacadeWebUtils.toWebResourceName(this.getStoreName()) + ".module";
    }

    @Override
    protected String handleGetRoutingModuleFileName() {
        return MetafacadeWebUtils.toWebResourceName(this.getStoreName()) + "-routing.module";
    }

    @Override
    protected String handleGetRoutingModuleFilePath() {
        return "view" + this.getPathRoot() + "/" + this.getRoutingModuleFileName();
    }

    @Override
    protected String handleGetModuleName() {
        return getStoreName() + "Module";
    }

    @Override
    protected String handleGetRoutingModuleName() {
        return getStoreName() + "RoutingModule";
    }

    @Override
    protected String handleGetComponentName() {
        
        if(this.isViewHasNameOfUseCase()) {
            return this.getActionClassName() + "UC";
        } else {
            return this.getActionClassName();
        }
    }

    @Override
    protected String handleGetSelectorName() {
        return AngularUtils.toWebResourceName(this.getComponentName());
    }

    @Override
    protected String handleGetStoreName() {
        String name = this.getSubstitutionName();
        return StringUtils.isBlank(name) ? getActionClassName() : name;
    }
}