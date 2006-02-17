package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityAttributeFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityAttributeFacade
 */
public class EJB3ManageableEntityAttributeFacadeLogicImpl
    extends EJB3ManageableEntityAttributeFacadeLogic
{
    /**
     * The property that stores the defuult temporal type for date based attributes
     */
    public static final String ENTITY_DEFAULT_TEMPORAL_TYPE = "entityDefaultTemporalType";
    
    public EJB3ManageableEntityAttributeFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityAttributeFacadeLogic#handleGetTemporalType()
     */
    protected String handleGetTemporalType()
    {
        String temporalType = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_TEMPORAL_TYPE);
        if (StringUtils.isBlank(temporalType))
        {
            temporalType = String.valueOf(this.getConfiguredProperty(ENTITY_DEFAULT_TEMPORAL_TYPE));
        }
        return temporalType;
    }
}