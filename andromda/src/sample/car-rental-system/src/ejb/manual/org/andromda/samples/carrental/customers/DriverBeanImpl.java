package org.andromda.samples.carrental.customers;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class DriverBeanImpl 
       extends DriverBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class DriverBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("DriverBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("DriverBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "DriverBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("DriverBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("DriverBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("DriverBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("DriverBean.ejbActivate...");
    }
}
