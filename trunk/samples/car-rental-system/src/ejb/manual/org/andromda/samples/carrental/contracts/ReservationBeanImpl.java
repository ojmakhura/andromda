package org.andromda.samples.carrental.contracts;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


public abstract class ReservationBeanImpl 
       extends ReservationBean
       implements EntityBean
{
    // concrete business methods that were declared
    // abstract in class ReservationBean ...


    // ------------ and the rest of the EJB stuff ---------------

    private EntityContext context;

    public void setEntityContext(EntityContext ctx)
    {
        //Log.trace("ReservationBean.setEntityContext...");
        context = ctx;
    }

    public void unsetEntityContext()
    {
        //Log.trace("ReservationBean.unsetEntityContext...");
        context = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException
    {
        //Log.trace(
        //    "ReservationBean.ejbRemove...");
    }

    public void ejbLoad()
    {
        //Log.trace("ReservationBean.ejbLoad...");
    }

    public void ejbStore()
    {
        //Log.trace("ReservationBean.ejbStore...");
    }

    public void ejbPassivate()
    {
        //Log.trace("ReservationBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("ReservationBean.ejbActivate...");
    }
}
