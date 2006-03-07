package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Profile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3PersistenceContextFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3PersistenceContextFacade
 */
public class EJB3PersistenceContextFacadeLogicImpl
    extends EJB3PersistenceContextFacadeLogic
{

    public EJB3PersistenceContextFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3PersistenceContextFacade#getUnitName()
     */
    protected java.lang.String handleGetUnitName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_UNIT_NAME);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3PersistenceContextFacade#getContextType()
     */
    protected java.lang.String handleGetContextType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_TYPE);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3PersistenceContextFacadeLogic#handleGetDatasource()
     */
    protected String handleGetDatasource()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_DATASOURCE);
    }

}