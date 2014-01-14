// license-header java merge-point
package org.andromda.samples.carrental.contracts.web.returnCar;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

/**
 * @see org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarController
 */
public class ReturnCarControllerImpl extends ReturnCarController {
	
	/**
	 * @see org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarController#returnCar(org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarForm)
	 */
	@Override
	public String returnCar(ReturnCarForm form) throws Throwable {
		// this property receives a default value, just to have the application
		form.setId("id-test");
		return null;
	}

	/**
	 * @see org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarController#searchForContractsOfCustomer(org.andromda.samples.carrental.contracts.web.returnCar.SearchForContractsOfCustomerForm)
	 */
	@Override
	public void searchForContractsOfCustomer(
			SearchForContractsOfCustomerForm form) throws Throwable {
		// populating the table with a dummy list
		form.setContract(contractDummyList);
	}

	/**
	 * @see org.andromda.samples.carrental.contracts.web.returnCar.ReturnCarController#hadAccident(org.andromda.samples.carrental.contracts.web.returnCar.HadAccidentForm)
	 */
	@Override
	public String hadAccident(HadAccidentForm form) throws Throwable {
		 // this property receives a default value, just to have the application
		 form.setAccidentHappend(false);
		 // this property receives a default value, just to have the application
		 form.setId("id-test");
		 return "no";
	}
	
	/**
	 * This dummy variable is used to populate the "contract" table. You may
	 * delete it when you add you own code in this controller.
	 */
	private static final Collection contractDummyList = Arrays.asList(
			new ContractDummy("contractNo-1", "signed-1", "id-1"),
			new ContractDummy("contractNo-2", "signed-2", "id-2"),
			new ContractDummy("contractNo-3", "signed-3", "id-3"),
			new ContractDummy("contractNo-4", "signed-4", "id-4"),
			new ContractDummy("contractNo-5", "signed-5", "id-5"));

	/**
	 * This inner class is used in the dummy implementation in order to get the
	 * web application running without any manual programming. You may delete
	 * this class when you add you own code in this controller.
	 */
	public static final class ContractDummy implements Serializable {
		private static final long serialVersionUID = 5398913098977575130L;

		private String contractNo = null;
		private String signed = null;
		private String id = null;

		public ContractDummy(String contractNo, String signed, String id) {
			this.contractNo = contractNo;
			this.signed = signed;
			this.id = id;
		}

		public void setContractNo(String contractNo) {
			this.contractNo = contractNo;
		}

		public String getContractNo() {
			return this.contractNo;
		}

		public void setSigned(String signed) {
			this.signed = signed;
		}

		public String getSigned() {
			return this.signed;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}
	}

	
}
