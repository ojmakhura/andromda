package org.andromda.metafacades.emf.uml22;

import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ManageableEntityAttribute.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAttribute
 */
public class ManageableEntityAttributeLogicImpl
    extends ManageableEntityAttributeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ManageableEntityAttributeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#isDisplay()
     */
    @Override
    protected boolean handleIsDisplay()
    {
        boolean display = true;

        // only identifiers might be hidden
        if (this.isIdentifier())
        {
            final String displayStrategy =
                StringUtils.trimToNull(
                    (String)this.getConfiguredProperty(UMLMetafacadeProperties.MANAGEABLE_ID_DISPLAY_STRATEGY));

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

            // only display identifiers when explicitly modeled
            else// if ("auto".equalsIgnoreCase(displayStrategy))
            {
                display = ((Entity)this.getOwner()).isUsingAssignedIdentifier();
            }
        }

        return display;
    }

    /**
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#isManageableGetterAvailable()
     */
    @Override
    protected boolean handleIsManageableGetterAvailable()
    {
        return this.handleGetType() != null && this.getType().isBlobType();
    }
}
