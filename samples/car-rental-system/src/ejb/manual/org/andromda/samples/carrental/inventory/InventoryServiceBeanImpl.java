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
            return cth
                .create(
                    typeData.getManufacturer(),
                    typeData.getIdentifier(),
                    typeData.getOrderNo(),
                    typeData.getComfortClass())
                .getId();
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
            CarType ctl = ctlh.findByPrimaryKey(carTypeId);

            CarLocalHome clh = getCarLocalHome();
            Car cl = clh.create(carData.getRegistrationNo(), carData.getInventoryNo());

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
     * @see org.andromda.samples.carrental.inventory.InventoryServiceBean#searchCarByComfortClass(String)
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
                Car element = (Car) iter.next();
                result.add(getCarData(element));
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
     * Extracts the data out of a Car object.
     * @param c the Car
     * @return CarData
     */
    private CarData getCarData(Car c)
    {
        return new CarData(c.getId(), c.getRegistrationNo(), c.getInventoryNo());
    }

    /**
    * @see org.andromda.samples.carrental.inventory.InventoryServiceBean#searchAllCarTypes()
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
                CarType element = (CarType) iter.next();
                result.add(getCarTypeData(element));
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
     * @see org.andromda.samples.carrental.inventory.InventoryServiceBean#searchAllCars()
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
                Car element = (Car) iter.next();
                result.add(
                    new CarAndTypeData(
                        getCarData(element),
                        getCarTypeData(element.getType())));
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
     * Extracts the data from a CarType object.
     * @param ct the CarType
     * @return CarTypeData the data
     */
    private CarTypeData getCarTypeData(CarType ct)
    {
        return new CarTypeData(ct.getId(), ct.getManufacturer(), ct.getIdentifier(), ct.getOrderNo(), ct.getComfortClass());
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
