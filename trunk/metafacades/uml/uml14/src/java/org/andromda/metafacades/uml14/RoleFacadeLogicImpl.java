package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.HashSet;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ServiceFacade;
import org.andromda.metafacades.uml.ServiceOperationFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.RoleFacade.
 * 
 * @see org.andromda.metafacades.uml.RoleFacade
 */
public class RoleFacadeLogicImpl
    extends RoleFacadeLogic
{
    // ---------------- constructor -------------------------------

    public RoleFacadeLogicImpl(
        Object metaObject,
        String context)
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
            String mask = StringUtils
                .trimToEmpty(String
                    .valueOf(this
                        .getConfiguredProperty(UMLMetafacadeProperties.ROLE_NAME_MASK)));
            name = MetafacadeUtils.getMaskedName(name, mask);
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.RoleFacade#isReferencesPresent()
     */
    protected boolean handleIsReferencesPresent()
    {
        final Collection allSourceDependencies = new HashSet(this
            .getSourceDependencies());
        for (GeneralizableElementFacade parent = this.getGeneralization(); parent != null; parent = parent
            .getGeneralization())
        {
            allSourceDependencies.addAll(parent.getSourceDependencies());
        }
        Object test = CollectionUtils.find(
            allSourceDependencies,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    Object target = dependency.getTargetElement();
                    return target instanceof ServiceFacade
                        || target instanceof ServiceOperationFacade;
                }
            });

        return test != null;
    }

}