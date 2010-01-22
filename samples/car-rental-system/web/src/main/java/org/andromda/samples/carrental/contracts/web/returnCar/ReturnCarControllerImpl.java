// license-header java merge-point
package org.andromda.samples.carrental.contracts.web.returnCar;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarController
 */
public class ReturnCarControllerImpl extends ReturnCarController
{
    /**
     * @see org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarController#returnCar(org.apache.struts.action.ActionMapping, ReturnCarForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String returnCar(ActionMapping mapping, ReturnCarForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setId("id-test");
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarController#searchForContractsOfCustomer(org.apache.struts.action.ActionMapping, SearchForContractsOfCustomerForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void searchForContractsOfCustomer(ActionMapping mapping, SearchForContractsOfCustomerForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // populating the table with a dummy list
        form.setContract(contractDummyList);
    }

    /**
     * @see org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarController#hadAccident(org.apache.struts.action.ActionMapping, HadAccidentForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String hadAccident(ActionMapping mapping, HadAccidentForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setAccidentHappend(false);
        // this property receives a default value, just to have the application running on dummy data
        form.setId("id-test");
        return null;
    }

    /**
     * This dummy variable is used to populate the "contract" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection contractDummyList =
        Arrays.asList(new ContractDummy("contractNo-1", "signed-1", "id-1"),
            new ContractDummy("contractNo-2", "signed-2", "id-2"),
            new ContractDummy("contractNo-3", "signed-3", "id-3"),
            new ContractDummy("contractNo-4", "signed-4", "id-4"),
            new ContractDummy("contractNo-5", "signed-5", "id-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class ContractDummy implements Serializable
    {
        private String contractNo = null;
        private String signed = null;
        private String id = null;

        public ContractDummy(String contractNo, String signed, String id)
        {
            this.contractNo = contractNo;
            this.signed = signed;
            this.id = id;
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
        
        public void setId(String id)
        {
            this.id = id;
        }

        public String getId()
        {
            return this.id;
        }
        
    }
}
