package org.andromda.samples.carrental.admins;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

public abstract class AdministratorBeanImpl 
       extends AdministratorBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class AdministratorBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("AdministratorBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("AdministratorBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "AdministratorBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("AdministratorBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("AdministratorBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("AdministratorBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("AdministratorBean.ejbActivate...");
    }
}
