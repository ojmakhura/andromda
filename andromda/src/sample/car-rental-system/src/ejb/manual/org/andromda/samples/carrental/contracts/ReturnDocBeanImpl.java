package org.andromda.samples.carrental.contracts;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class ReturnDocBeanImpl 
       extends ReturnDocBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class ReturnDocBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("ReturnDocBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("ReturnDocBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "ReturnDocBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("ReturnDocBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("ReturnDocBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("ReturnDocBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("ReturnDocBean.ejbActivate...");
    }
}
