package org.andromda.samples.carrental.contracts;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class PartnerInAccidentBeanImpl 
       extends PartnerInAccidentBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class PartnerInAccidentBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("PartnerInAccidentBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("PartnerInAccidentBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "PartnerInAccidentBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("PartnerInAccidentBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("PartnerInAccidentBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("PartnerInAccidentBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("PartnerInAccidentBean.ejbActivate...");
    }
}
