package org.andromda.samples.carrental.inventory;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class CarAccessoryTypeBeanImpl 
       extends CarAccessoryTypeBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class CarAccessoryTypeBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("CarAccessoryTypeBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("CarAccessoryTypeBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "CarAccessoryTypeBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("CarAccessoryTypeBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("CarAccessoryTypeBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("CarAccessoryTypeBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("CarAccessoryTypeBean.ejbActivate...");
    }
}
