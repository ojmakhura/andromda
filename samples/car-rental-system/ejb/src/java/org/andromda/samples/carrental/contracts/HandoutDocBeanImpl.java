package org.andromda.samples.carrental.contracts;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class HandoutDocBeanImpl 
       extends HandoutDocBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class HandoutDocBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("HandoutDocBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("HandoutDocBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "HandoutDocBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("HandoutDocBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("HandoutDocBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("HandoutDocBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("HandoutDocBean.ejbActivate...");
    }
}
