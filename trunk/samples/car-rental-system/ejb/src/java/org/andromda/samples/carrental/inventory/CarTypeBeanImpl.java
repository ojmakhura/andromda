package org.andromda.samples.carrental.inventory;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class CarTypeBeanImpl 
       extends CarTypeBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class CarTypeBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("CarTypeBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("CarTypeBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "CarTypeBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("CarTypeBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("CarTypeBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("CarTypeBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("CarTypeBean.ejbActivate...");
    }
}
