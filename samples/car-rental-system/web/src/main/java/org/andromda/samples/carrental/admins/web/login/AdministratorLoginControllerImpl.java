// license-header java merge-point
package org.andromda.samples.carrental.admins.web.login;


/**
 * @see org.andromda.samples.carrental.admins.web.login.AdministratorLoginController
 */
public class AdministratorLoginControllerImpl extends AdministratorLoginController
{
	
	/**
	 * @see org.andromda.samples.carrental.admins.web.login.AdministratorLoginController#authenticateAsAdministrator(org.andromda.samples.carrental.admins.web.login.AuthenticateAsAdministratorForm)
	 */
	@Override
	public String authenticateAsAdministrator(
			AuthenticateAsAdministratorForm form) throws Throwable {
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setPassword("password-test");
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setAccountNo("accountNo-test");
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setName("name-test");
		return "succeed";
	}
}
