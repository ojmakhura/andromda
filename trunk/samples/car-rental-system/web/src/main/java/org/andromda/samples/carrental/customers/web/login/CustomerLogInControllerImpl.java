// license-header java merge-point
package org.andromda.samples.carrental.customers.web.login;


/**
 * @see org.andromda.samples.carrental.customers.web.login.CustomerLogInController
 */
public class CustomerLogInControllerImpl extends CustomerLogInController
{

	/**
	 * @see org.andromda.samples.carrental.customers.web.login.CustomerLogInController#authenticateAsCustomer(org.andromda.samples.carrental.customers.web.login.AuthenticateAsCustomerForm)
	 */
	@Override
	public String authenticateAsCustomer(AuthenticateAsCustomerForm form)
			throws Throwable {
		// this property receives a default value, just to have the application running on dummy data
        form.setPassword("password-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setCustomerNo("customerNo-test");
        return "success";
	}
}