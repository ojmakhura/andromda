package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.ServiceOperation;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.Role.
 *
 * @see org.andromda.metafacades.uml.Role
 */
public class RoleLogicImpl
    extends RoleLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public RoleLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.ModelElementFacadeLogic#handleGetName()
     */
    @Override
    public String handleGetName()
    {
        String name;
        Object value = this.findTaggedValue(UMLProfile.TAGGEDVALUE_ROLE_NAME);
        if (value != null)
        {
            name = StringUtils.trimToEmpty(String.valueOf(value));
        }
        else
        {
            name = super.handleGetName();
            String mask =
                StringUtils.trimToEmpty(
                    String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ROLE_NAME_MASK)));
            name = NameMasker.mask(
                    name,
                    mask);
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.Role#isReferencesPresent()
     */
    @Override
    protected boolean handleIsReferencesPresent()
    {
        final Collection<DependencyFacade> allSourceDependencies = new LinkedHashSet<DependencyFacade>(this.getSourceDependencies());
        for (GeneralizableElementFacade parent = this.getGeneralization(); parent != null;
            parent = parent.getGeneralization())
        {
            allSourceDependencies.addAll(parent.getSourceDependencies());
        }
        boolean present =
            CollectionUtils.find(
                allSourceDependencies,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        DependencyFacade dependency = (DependencyFacade)object;
                        Object target = dependency.getTargetElement();
                        return target instanceof Service || target instanceof ServiceOperation;
                    }
                }) != null;

        // - if no references on any services, try the FrontEndUseCases
        if (!present)
        {
            final Collection<AssociationEndFacade> associationEnds = this.getAssociationEnds();
            for (final Iterator<AssociationEndFacade> iterator = associationEnds.iterator(); iterator.hasNext() && !present;)
            {
                final AssociationEndFacade associationEnd = iterator.next();
                present = associationEnd.getOtherEnd().getType() instanceof FrontEndUseCase;
            }

            // - a generalized role is still a role, and therefore is associated
            // with the FrontEndUseCase
            if (!present)
            {
                present = !this.getGeneralizedActors().isEmpty();
            }
        }

        return present;
    }
}
