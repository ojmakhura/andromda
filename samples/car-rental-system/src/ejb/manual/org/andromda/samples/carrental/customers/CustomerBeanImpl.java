package org.andromda.samples.carrental.customers;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class CustomerBeanImpl 
       extends CustomerBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class CustomerBean ...

    public void addDriver (org.andromda.samples.carrental.customers.DriverLocal driver) {
        // TODO: put your implementation here.
    }



    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("CustomerBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("CustomerBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "CustomerBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("CustomerBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("CustomerBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("CustomerBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("CustomerBean.ejbActivate...");
    }
}
