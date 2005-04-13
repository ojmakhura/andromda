package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.ServiceOperation;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.Role.
 *
 * @see org.andromda.metafacades.uml.Role
 */
public class RoleLogicImpl
        extends RoleLogic
{
    // ---------------- constructor -------------------------------

    public RoleLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetName()
     */
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
            String mask = StringUtils.trimToEmpty(String.valueOf(
                    this.getConfiguredProperty(UMLMetafacadeProperties.ROLE_NAME_MASK)));
            name = NameMasker.mask(name, mask);
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.Role#isReferencesPresent()
     */
    protected boolean handleIsReferencesPresent()
    {
        final Collection allSourceDependencies = new HashSet(this.getSourceDependencies());
        for (GeneralizableElementFacade parent = this.getGeneralization();
             parent != null; parent = parent.getGeneralization())
        {
            allSourceDependencies.addAll(parent.getSourceDependencies());
        }
        Object test = CollectionUtils.find(allSourceDependencies, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency = (DependencyFacade)object;
                Object target = dependency.getTargetElement();
                return target instanceof Service || target instanceof ServiceOperation;
            }
        });

        return test != null;
    }

}