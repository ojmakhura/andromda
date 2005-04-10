// license-header java merge-point
package org.andromda.samples.carrental.contracts;



/**
 * @see org.andromda.samples.carrental.contracts.ContractServiceBean
 */
public class ContractServiceBeanImpl extends ContractServiceBean
{
    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#searchForReservationsOfCustomer(java.lang.String)
     */
    public java.util.Collection searchForReservationsOfCustomer(java.lang.String customerId)
            throws org.andromda.samples.carrental.contracts.ContractException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#reserve(java.lang.String, java.lang.String,
            *      java.util.Date)
     */
    public void reserve(java.lang.String customerId, java.lang.String comfortClass, java.util.Date reservationDate)
            throws org.andromda.samples.carrental.contracts.ContractException
    {
        //TODO: put your implementation here.
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#handOutReservation(java.lang.String)
     */
    public java.lang.String handOutReservation(java.lang.String idReservation)
            throws org.andromda.samples.carrental.contracts.ContractException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#returnCar(java.lang.String)
     */
    public java.lang.String returnCar(java.lang.String idContract)
            throws org.andromda.samples.carrental.contracts.ContractException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#searchForContractsOfCustomer(java.lang.String)
     */
    public java.util.Collection searchForContractsOfCustomer(java.lang.String idCustomer)
            throws org.andromda.samples.carrental.contracts.ContractException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.ContractService#registerAccident(java.lang.String)
     */
    public java.lang.String registerAccident(java.lang.String idContract)
            throws org.andromda.samples.carrental.contracts.ContractException
    {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

}
