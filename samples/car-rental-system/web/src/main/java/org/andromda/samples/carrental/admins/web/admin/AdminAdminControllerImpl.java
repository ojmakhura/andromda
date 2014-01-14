// license-header java merge-point
package org.andromda.samples.carrental.admins.web.admin;


/**
 * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController
 */
public class AdminAdminControllerImpl extends AdminAdminController
{

	/**
	 * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController#updateAdministrator(org.andromda.samples.carrental.admins.web.admin.UpdateAdministratorForm)
	 */
	@Override
	public String updateAdministrator(UpdateAdministratorForm form)
			throws Throwable {
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setAccountNo("accountNo-test");
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setEmail("email-test");
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setName("name-test");
		return "succeed";
	}

	/**
	 * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController#addAdministrator(org.andromda.samples.carrental.admins.web.admin.AddAdministratorForm)
	 */
	@Override
	public String addAdministrator(AddAdministratorForm form) throws Throwable {
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setAccountNo("accountNo-test");
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setEmail("email-test");
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setName("name-test");
		return "succeed";
	}
}
