package org.andromda.samples.carrental.contracts;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.samples.carrental.inventory.InventoryException;
import org.andromda.samples.carrental.inventory.InventoryService;
import org.andromda.samples.carrental.inventory.InventoryServiceHome;
import org.andromda.samples.carrental.customers.CustomerLocal;
import org.andromda.samples.carrental.customers.CustomerLocalHome;

public class ContractServiceBeanImpl
    extends ContractServiceBean
    implements SessionBean
{
    // concrete business methods that were declared
    // abstract in class ContractServiceBean ...

    /**
     * @see org.andromda.samples.carrental.contracts.ContractServiceBean#searchForReservationsOfCustomer(String)
     */
    public java.util.Collection searchForReservationsOfCustomer(
        java.lang.String customerId)
        throws ContractException
    {
        try
        {
            ArrayList results = new ArrayList();

            ReservationLocalHome rlh = getReservationLocalHome();

            Collection reservations = rlh.findByCustomer(customerId);
            for (Iterator it = reservations.iterator(); it.hasNext();)
            {
                ReservationLocal r = (ReservationLocal) it.next();
                results.add(r.getReservationData());
            }

            return results;
        }
        catch (NamingException e)
        {
            throw new EJBException(e);
        }
        catch (FinderException e)
        {
            throw new EJBException(e);
        }
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractServiceBean#reserve(String, String, Date)
     */
    public void reserve(
        java.lang.String customerId,
        java.lang.String comfortClass,
        java.util.Date reservationDate)
        throws ContractException
    {
        try
        {
            CustomerLocalHome clh = this.getCustomerLocalHome();
            CustomerLocal customer = clh.findByPrimaryKey(customerId);

            InventoryServiceHome ish = getInventoryServiceHome();
            InventoryService ms = ish.create();
            Collection comfortableCars =
                ms.searchCarByComfortClass(comfortClass);

            ReservationLocalHome rlh = getReservationLocalHome();
            Collection reservations =
                rlh.findByComfortClasseAndDate(comfortClass, reservationDate);

            if (comfortableCars.size() <= reservations.size())
            {
                throw new NotEnoughAvailableException(
                    comfortClass,
                    reservationDate);
            }

            // Reservation is legal, so let's do it!
            ReservationLocal res = rlh.create(reservationDate, comfortClass);
            res.setCustomer(customer);

            // Service-Bean not used any more
            ms.remove();
        }
        catch (InventoryException e)
        {
            throw new NotEnoughAvailableException(
                comfortClass,
                reservationDate);
        }
        catch (RemoteException e)
        {
            throw new EJBException(e);
        }
        catch (NamingException e)
        {
            throw new EJBException(e);
        }
        catch (FinderException e)
        {
            throw new EJBException(e);
        }
        catch (CreateException e)
        {
            throw new EJBException(e);
        }
        catch (RemoveException e)
        {
            throw new EJBException(e);
        }
    }

    // ---------- the usual session bean stuff... ------------

    private SessionContext context;

    public void setSessionContext(SessionContext ctx)
    {
        //Log.trace("ContractServiceBean.setSessionContext...");
        context = ctx;
    }

    public void ejbRemove()
    {
        //Log.trace(
        //    "ContractServiceBean.ejbRemove...");
    }

    public void ejbPassivate()
    {
        //Log.trace("ContractServiceBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("ContractServiceBean.ejbActivate...");
    }
}
