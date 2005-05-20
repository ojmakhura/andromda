package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ManageableEntityAttribute.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAttribute
 */
public class ManageableEntityAttributeLogicImpl
        extends ManageableEntityAttributeLogic
{

    public ManageableEntityAttributeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#isDisplay()
     */
    protected boolean handleIsDisplay()
    {
        boolean display = true;

        // only identifiers might be hidden
        if (isIdentifier())
        {
            final String displayStrategy = StringUtils.trimToNull((String)getConfiguredProperty(
                    UMLMetafacadeProperties.MANAGEABLE_ID_DISPLAY_STRATEGY));
            // never display identifiers
            if ("never".equalsIgnoreCase(displayStrategy))
            {
                display = false;
            }
            // always display identifiers
            else if ("always".equalsIgnoreCase(displayStrategy))
            {
                display = true;
            }
            // only display identifiers when explicitely modeled
            else // if ("auto".equalsIgnoreCase(displayStrategy))
            {
                display = ((Entity)getOwner()).isUsingAssignedIdentifier();
            }
        }

        return display;
    }

    protected boolean handleIsManageableGetterAvailable()
    {
        return getType().isBlobType();
    }
}