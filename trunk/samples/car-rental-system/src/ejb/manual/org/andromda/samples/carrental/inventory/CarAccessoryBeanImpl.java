package org.andromda.samples.carrental.inventory;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class CarAccessoryBeanImpl 
       extends CarAccessoryBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class CarAccessoryBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("CarAccessoryBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("CarAccessoryBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "CarAccessoryBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("CarAccessoryBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("CarAccessoryBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("CarAccessoryBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("CarAccessoryBean.ejbActivate...");
    }
}
