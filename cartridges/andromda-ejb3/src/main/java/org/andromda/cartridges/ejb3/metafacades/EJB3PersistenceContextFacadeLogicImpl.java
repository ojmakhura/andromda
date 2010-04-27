package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Profile;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3PersistenceContextFacade.
 *
 * @see EJB3PersistenceContextFacade
 */
public class EJB3PersistenceContextFacadeLogicImpl
    extends EJB3PersistenceContextFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EJB3PersistenceContextFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3PersistenceContextFacade#getUnitName()
     */
    @Override
    protected String handleGetUnitName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_UNIT_NAME);
    }

    /**
     * @see EJB3PersistenceContextFacade#getContextType()
     */
    @Override
    protected String handleGetContextType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_TYPE);
    }

    /**
     * @see EJB3PersistenceContextFacadeLogic#handleGetDatasource()
     */
    @Override
    protected String handleGetDatasource()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_PERSISTENCE_CONTEXT_DATASOURCE);
    }
}
