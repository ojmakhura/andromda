package org.andromda.samples.carrental.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

public class InventoryServiceBeanImpl
    extends InventoryServiceBean
    implements SessionBean
{
    // concrete business methods that were declared
    // abstract in class InventoryServiceBean ...

    /**
     * @see org.andromda.samples.carrental.inventory.InventoryServiceBean#createCarType(CarTypeData)
     */
    public java.lang.String createCarType(CarTypeData typeData)
        throws InventoryException
    {
        try
        {
            CarTypeLocalHome cth = getCarTypeLocalHome();
            return cth.create(typeData).getId();
        }
        catch (NamingException e)
        {
            throw new EJBException(e);
        }
        catch (CreateException e)
        {
            throw new EJBException(e);
        }
    }

    /**
     * @see org.andromda.samples.carrental.inventory.InventoryServiceBean#createCar(CarData, String)
     */
    public java.lang.String createCar(
        CarData carData,
        java.lang.String carTypeId)
        throws InventoryException
    {
        try
        {
            CarTypeLocalHome ctlh = getCarTypeLocalHome();
            CarTypeLocal ctl = ctlh.findByPrimaryKey(carTypeId);

            CarLocalHome clh = getCarLocalHome();
            CarLocal cl = clh.create(carData);

            cl.setType(ctl);
            return cl.getId();
        }
        catch (NamingException e)
        {
            throw new EJBException(e);
        }
        catch (CreateException e)
        {
            throw new EJBException(e);
        }
        catch (FinderException e)
        {
            throw new EJBException(e);
        }
    }

    /**
     * @see de.mbohlen.uml2ejb.samples.carrental.inventory.InventoryServiceBean#searchCarByComfortClass(String)
     */
    public java.util.Collection searchCarByComfortClass(
        java.lang.String comfortClass)
        throws InventoryException
    {
        try
        {
            CarLocalHome clh = getCarLocalHome();
            Collection cl = clh.findByComfortClass(comfortClass);
            ArrayList result = new ArrayList();
            for (Iterator iter = cl.iterator(); iter.hasNext();)
            {
                CarLocal element = (CarLocal) iter.next();
                result.add(element.getCarData());
            }
            return result;
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
    * @see de.mbohlen.uml2ejb.samples.carrental.inventory.InventoryServiceBean#searchAllCarTypes()
    */
    public Collection searchAllCarTypes() throws InventoryException
    {
        try
        {
            CarTypeLocalHome cth = getCarTypeLocalHome();
            Collection carTypes = cth.findAll();

            ArrayList result = new ArrayList();
            for (Iterator iter = carTypes.iterator(); iter.hasNext();)
            {
                CarTypeLocal element = (CarTypeLocal) iter.next();
                result.add(element.getCarTypeData());
            }
            return result;
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
     * @see de.mbohlen.uml2ejb.samples.carrental.inventory.InventoryServiceBean#searchAllCars()
     */
    public Collection searchAllCars() throws InventoryException
    {
        try
        {
            CarLocalHome clh = getCarLocalHome();
            Collection cl = clh.findAll();
            ArrayList result = new ArrayList();
            for (Iterator iter = cl.iterator(); iter.hasNext();)
            {
                CarLocal element = (CarLocal) iter.next();
                result.add(
                    new CarAndTypeData(
                        element.getCarData(),
                        element.getType().getCarTypeData()));
            }
            return result;
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

    // ---------- the usual session bean stuff... ------------

    private SessionContext context;

    public void setSessionContext(SessionContext ctx)
    {
        //Log.trace("InventoryServiceBean.setSessionContext...");
        context = ctx;
    }

    public void ejbRemove()
    {
        //Log.trace(
        //    "InventoryServiceBean.ejbRemove...");
    }

    public void ejbPassivate()
    {
        //Log.trace("InventoryServiceBean.ejbPassivate...");
    }

    public void ejbActivate()
    {
        //Log.trace("InventoryServiceBean.ejbActivate...");
    }

}
