package org.andromda.samples.carrental.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

public class InventoryServiceBeanImpl
    extends InventoryServiceBean
    implements javax.ejb.SessionBean
{
    // concrete business methods that were declared
    // abstract in class InventoryServiceBean ...

    protected java.lang.String handleCreateCarType(
        net.sf.hibernate.Session sess,
        org.andromda.samples.carrental.inventory.CarTypeData typeData)
        throws InventoryException
    {
        try
        {
            CarType ct =
                CarTypeFactory.create(
                    typeData.getManufacturer(),
                    typeData.getIdentifier(),
                    typeData.getOrderNo(),
                    typeData.getComfortClass());
            return (String) sess.save(ct);
        }
        catch (Exception e)
        {
            throw new EJBException(e);
        }
    }

    protected java.lang.String handleCreateCar(
        net.sf.hibernate.Session sess,
        org.andromda.samples.carrental.inventory.CarData carData,
        java.lang.String carTypeId)
        throws InventoryException
    {
        try
        {
            CarType ctl = CarTypeFactory.findByPrimaryKey(sess, carTypeId);

            Car cl =
                CarFactory.create(
                    carData.getRegistrationNo(),
                    carData.getInventoryNo());

            cl.setType(ctl);
            return (String) sess.save(cl);
        }
        catch (Exception e)
        {
            throw new EJBException(e);
        }
    }

    protected java.util.Collection handleSearchCarByComfortClass(
        net.sf.hibernate.Session sess,
        java.lang.String comfortClass)
        throws InventoryException
    {
        try
        {
            Collection cl =
                CarFactory.findByComfortClass(sess, comfortClass);
            ArrayList result = new ArrayList();
            for (Iterator iter = cl.iterator(); iter.hasNext();)
            {
                Car element = (Car) iter.next();
                result.add(getCarData(element));
            }
            return result;
        }
        catch (Exception e)
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
        return new CarData(
            c.getId(),
            c.getRegistrationNo(),
            c.getInventoryNo());
    }

    protected java.util.Collection handleSearchAllCarTypes(
        net.sf.hibernate.Session sess)
        throws InventoryException
    {
        try
        {
            Collection carTypes = CarTypeFactory.findAll(sess);

            ArrayList result = new ArrayList();
            for (Iterator iter = carTypes.iterator(); iter.hasNext();)
            {
                CarType element = (CarType) iter.next();
                result.add(getCarTypeData(element));
            }
            return result;
        }
        catch (Exception e)
        {
            throw new EJBException(e);
        }
    }

    protected java.util.Collection handleSearchAllCars(
        net.sf.hibernate.Session sess)
        throws InventoryException
    {
        try
        {
            Collection cl = CarFactory.findAll(sess);
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
        catch (Exception e)
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
        return new CarTypeData(
            ct.getId(),
            ct.getManufacturer(),
            ct.getIdentifier(),
            ct.getOrderNo(),
            ct.getComfortClass());
    }

    // ---------- the usual session bean stuff... ------------

    public void setSessionContext(javax.ejb.SessionContext ctx)
    {
        //Log.trace("InventoryServiceBean.setSessionContext...");
        super.setSessionContext(ctx);
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
