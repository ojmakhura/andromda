package org.andromda.samples.carrental.contracts;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;



public abstract class AccidentDocBeanImpl 
       extends AccidentDocBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class AccidentDocBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("AccidentDocBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("AccidentDocBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "AccidentDocBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("AccidentDocBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("AccidentDocBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("AccidentDocBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("AccidentDocBean.ejbActivate...");
    }
}
