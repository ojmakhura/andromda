// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: SessionBeanBase.vsl in andromda-ejb3-cartridge.
//
package org.andromda.howto2.rental;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.annotation.ejb.Clustered;

/**
 * Autogenerated EJB session bean base class RentalServiceBean.
 *
 *
 */
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Remote({RentalServiceRemote.class})
@Clustered
public abstract class RentalServiceBase
    implements RentalServiceRemote
{
    // ------ Session Context Injection ------

    @Resource
    protected SessionContext context;

    // ------ Persistence Context Definitions --------

    /**
     * Inject persistence context howtomodelcaching
     */
    @PersistenceContext(unitName = "howtomodelcaching")
    protected EntityManager emanager;

    // ------ DAO Injection Definitions --------

    /**
     * Inject DAO CarDao
     */
    @EJB
    private CarDao carDao;

    /**
     * Inject DAO PersonDao
     */
    @EJB
    private PersonDao personDao;

    // --------------- Constructors ---------------

    public RentalServiceBase()
    {
        super();
    }

    // ------ DAO Getters --------

    /**
     * Get the injected DAO CarDao
     */
    protected CarDao getCarDao()
    {
        return this.carDao;
    }

    /**
     * Get the injected DAO PersonDao
     */
    protected PersonDao getPersonDao()
    {
        return this.personDao;
    }

    // -------- Business Methods  --------------

    /**
     *
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List getAllCars()
        throws RentalException
    {
        try
        {
            return this.handleGetAllCars();
        }
        catch (RentalException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            throw new RentalServiceException(
                "Error performing 'org.andromda.howto2.rental.RentalService.getAllCars()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAllCars()}
     */
    protected abstract List handleGetAllCars()
        throws Exception;

    /**
     *
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List getCustomersByName(String name)
        throws RentalException
    {
        if (name == null)
        {
            throw new IllegalArgumentException(
                "org.andromda.howto2.rental.RentalServiceBean.getCustomersByName(String name) - 'name' can not be null");
        }
        try
        {
            return this.handleGetCustomersByName(name);
        }
        catch (RentalException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            throw new RentalServiceException(
                "Error performing 'org.andromda.howto2.rental.RentalService.getCustomersByName(String name)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getCustomersByName(String)}
     */
    protected abstract List handleGetCustomersByName(String name)
        throws Exception;


    // -------- Lifecycle Callbacks --------------
}
