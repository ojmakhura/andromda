package org.andromda.samples.carrental.inventory;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class CarBeanImpl 
       extends CarBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class CarBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("CarBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("CarBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "CarBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("CarBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("CarBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("CarBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("CarBean.ejbActivate...");
    }
}
