package org.andromda.metafacades.uml14;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.metafacade.MetafacadeFactoryException;
import org.andromda.metafacades.uml.DependencyFacade;
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
     * The <code>uppercase</code> role name mask.
     */
    private static final String MASK_UPPERCASE = "uppercase";

    /**
     * The <code>lowercase</code> role name mask.
     */
    private static final String MASK_LOWERCASE = "lowercase";

    /**
     * The <code>camelcase</code> role name mask.
     */
    private static final String MASK_CAMELCASE = "camelcase";

    /**
     * The <code>nospace</code> role name mask.
     */
    private static final String MASK_NOSPACE = "nospace";

    /**
     * The <code>none</code> role name mask.
     */
    private static final String MASK_NONE = "none";

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
            String mask = null;

            try
            {
                mask = StringUtils.trimToEmpty(String.valueOf(
                        this.getConfiguredProperty(UMLMetafacadeProperties.ROLE_NAME_MASK) ));
            }
            catch (MetafacadeFactoryException mffe)
            {
                // if the property has not been registered use the default value
                mask = MASK_NONE;
            }

            if (!mask.equalsIgnoreCase(MASK_NONE))
            {
                if (mask.equalsIgnoreCase(MASK_UPPERCASE))
                {
                    name = name.toUpperCase();
                }
                else if (mask.equalsIgnoreCase(MASK_LOWERCASE))
                {
                    name = name.toLowerCase();
                }
                else if (mask.equalsIgnoreCase(MASK_CAMELCASE))
                {
                    name = StringUtilsHelper.upperCamelCaseName(name
                        .toLowerCase());
                }
                else if (mask.equalsIgnoreCase(MASK_NOSPACE))
                {
                    name = StringUtils.deleteWhitespace(name);
                }
            }
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml14.RoleFacade#isEnabled()
     */
    protected boolean handleIsEnabled()
    {
        return CollectionUtils.find(
            this.getSourceDependencies(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    DependencyFacade dependency = (DependencyFacade)object;
                    Object target = dependency.getTargetElement();
                    return target instanceof ServiceFacade
                        || target instanceof ServiceOperationFacade;
                }
            }) != null;

    }
}
