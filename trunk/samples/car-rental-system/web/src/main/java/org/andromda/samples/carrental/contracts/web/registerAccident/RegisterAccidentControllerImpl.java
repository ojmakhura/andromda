// license-header java merge-point
package org.andromda.samples.carrental.contracts.web.registerAccident;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.contracts.web.registerAccident.RegisterAccidentController
 */
public class RegisterAccidentControllerImpl extends RegisterAccidentController
{
    /**
     * @see org.andromda.samples.carrental.contracts.web.registerAccident.RegisterAccidentController#searchForContractsOfCustomer(org.apache.struts.action.ActionMapping, org.andromda.samples.carrental.contracts.web.registerAccident.SearchForContractsOfCustomerForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void searchForContractsOfCustomer(ActionMapping mapping, org.andromda.samples.carrental.contracts.web.registerAccident.SearchForContractsOfCustomerForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // populating the table with a dummy list
        form.setContracts(contractsDummyList);
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.registerAccident.RegisterAccidentController#registerAccident(org.apache.struts.action.ActionMapping, org.andromda.samples.carrental.contracts.web.registerAccident.RegisterAccidentForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String registerAccident(ActionMapping mapping, org.andromda.samples.carrental.contracts.web.registerAccident.RegisterAccidentForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setIdContract("idContract-test");
        return null;
    }

    /**
     * This dummy variable is used to populate the "contracts" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final java.util.Collection contractsDummyList =
        java.util.Arrays.asList( new Object[] {
            new ContractsDummy("contractNo-1", "signed-1", "idContract-1"),
            new ContractsDummy("contractNo-2", "signed-2", "idContract-2"),
            new ContractsDummy("contractNo-3", "signed-3", "idContract-3"),
            new ContractsDummy("contractNo-4", "signed-4", "idContract-4"),
            new ContractsDummy("contractNo-5", "signed-5", "idContract-5")
        } );

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class ContractsDummy implements java.io.Serializable
    {
        private String contractNo = null;
        private String signed = null;
        private String idContract = null;

        public ContractsDummy(String contractNo, String signed, String idContract)
        {
            this.contractNo = contractNo;
            this.signed = signed;
            this.idContract = idContract;
        }
        
        public void setContractNo(String contractNo)
        {
            this.contractNo = contractNo;
        }

        public String getContractNo()
        {
            return this.contractNo;
        }
        
        public void setSigned(String signed)
        {
            this.signed = signed;
        }

        public String getSigned()
        {
            return this.signed;
        }
        
        public void setIdContract(String idContract)
        {
            this.idContract = idContract;
        }

        public String getIdContract()
        {
            return this.idContract;
        }
        
    }
}