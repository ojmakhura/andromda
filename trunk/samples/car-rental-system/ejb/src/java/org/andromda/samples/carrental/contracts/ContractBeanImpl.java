package org.andromda.samples.carrental.contracts;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class ContractBeanImpl 
       extends ContractBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class ContractBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("ContractBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("ContractBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "ContractBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("ContractBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("ContractBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("ContractBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("ContractBean.ejbActivate...");
    }
}
