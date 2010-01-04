// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.samples.carrental.contracts;

import java.util.Collection;
import java.util.Date;

/**
 * @see org.andromda.samples.carrental.contracts.ContractService
 */
public class ContractServiceImpl
    extends ContractServiceBase
{

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#searchForReservationsOfCustomer(String)
     */
    protected Collection handleSearchForReservationsOfCustomer(String customerId)
        throws Exception
    {
        //@todo implement protected Collection handleSearchForReservationsOfCustomer(String customerId)
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#reserve(String, String, Date)
     */
    protected void handleReserve(String customerId, String comfortClass, Date reservationDate)
        throws Exception
    {
        //@todo implement protected void handleReserve(String customerId, String comfortClass, Date reservationDate)
        throw new UnsupportedOperationException("org.andromda.samples.carrental.contracts.ContractService.handleReserve(String customerId, String comfortClass, Date reservationDate) Not implemented!");
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#handOutReservation(String)
     */
    protected String handleHandOutReservation(String idReservation)
        throws Exception
    {
        //@todo implement protected String handleHandOutReservation(String idReservation)
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#returnCar(String)
     */
    protected String handleReturnCar(String idContract)
        throws Exception
    {
        //@todo implement protected String handleReturnCar(String idContract)
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#searchForContractsOfCustomer(String)
     */
    protected Collection handleSearchForContractsOfCustomer(String idCustomer)
        throws Exception
    {
        //@todo implement protected Collection handleSearchForContractsOfCustomer(String idCustomer)
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#registerAccident(String)
     */
    protected String handleRegisterAccident(String idContract)
        throws Exception
    {
        //@todo implement protected String handleRegisterAccident(String idContract)
        return null;
    }

}
