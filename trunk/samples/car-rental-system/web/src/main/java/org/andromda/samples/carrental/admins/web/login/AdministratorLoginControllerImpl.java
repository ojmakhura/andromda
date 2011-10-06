// license-header java merge-point
package org.andromda.samples.carrental.admins.web.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.admins.web.login.AdministratorLoginController
 */
public class AdministratorLoginControllerImpl extends AdministratorLoginController
{
    /**
     * @see org.andromda.samples.carrental.admins.web.login.AdministratorLoginController#authenticateAsAdministrator(org.apache.struts.action.ActionMapping, AuthenticateAsAdministratorForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final String authenticateAsAdministrator(ActionMapping mapping, AuthenticateAsAdministratorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setPassword("password-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setAccountNo("accountNo-test");
        // this property receives a default value, just to have the application running on dummy data
        form.setName("name-test");
        return null;
    }
}
