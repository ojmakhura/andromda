package org.andromda.samples.carrental.contracts;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.andromda.samples.carrental.customers.Customer;
import org.andromda.samples.carrental.customers.CustomerFactory;
import org.andromda.samples.carrental.inventory.InventoryException;
import org.andromda.samples.carrental.inventory.InventoryService;
import org.andromda.samples.carrental.inventory.InventoryServiceHome;

import net.sf.hibernate.HibernateException;

public class ContractServiceBeanImpl
    extends ContractServiceBean
    implements javax.ejb.SessionBean
{
    // concrete business methods that were declared
    // abstract in class ContractServiceBean ...

    protected java.util.Collection handleSearchForReservationsOfCustomer(
        net.sf.hibernate.Session sess,
        java.lang.String customerId)
        throws ContractException
    {
        try
        {
            ArrayList results = new ArrayList();

            Collection reservations =
                ReservationFactory.findByCustomer(sess, customerId);
            for (Iterator it = reservations.iterator(); it.hasNext();)
            {
                Reservation r = (Reservation) it.next();
                results.add(getReservationData(r));
            }
            return results;
        }
        catch (HibernateException e)
        {
            return new ArrayList();
        }
        catch (SQLException e)
        {
            throw new EJBException(e);
        }
    }

    /**
     * Extracts the data from a Reservation object.
     * @param r the Reservation
     * @return ReservationData the data
     */
    private ReservationData getReservationData(Reservation r)
    {
        return new ReservationData(
            r.getId(),
            r.getReservationDate(),
            r.getComfortClass());
    }

    protected void handleReserve(
        net.sf.hibernate.Session sess,
        java.lang.String customerId,
        java.lang.String comfortClass,
        java.util.Date reservationDate)
        throws ContractException
    {
        try
        {
            Customer customer =
                CustomerFactory.findByPrimaryKey(sess, customerId);

            InventoryServiceHome ish = getInventoryServiceHome();
            InventoryService ms = ish.create();
            Collection comfortableCars =
                ms.searchCarByComfortClass(comfortClass);

            Collection reservations =
                ReservationFactory.findByComfortClassAndDate(
                    sess,
                    comfortClass,
                    reservationDate);

            if (comfortableCars.size() <= reservations.size())
            {
                throw new NotEnoughAvailableException(
                    comfortClass,
                    reservationDate);
            }

            // Reservation is legal, so let's do it!
            Reservation res =
                ReservationFactory.create(reservationDate, comfortClass);
            res.setCustomer(customer);
            sess.save(res);

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
        catch (CreateException e)
        {
            throw new EJBException(e);
        }
        catch (HibernateException e)
        {
            throw new EJBException(e);
        }
        catch (SQLException e)
        {
            throw new EJBException(e);
        }
        catch (NamingException e)
        {
            throw new EJBException(e);
        }
        catch (RemoveException e)
        {
            throw new EJBException(e);
        }
    }

    // ---------- the usual session bean stuff... ------------

    public void setSessionContext(javax.ejb.SessionContext ctx)
    {
        //Log.trace("ContractServiceBean.setSessionContext...");
        super.setSessionContext(ctx);
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
