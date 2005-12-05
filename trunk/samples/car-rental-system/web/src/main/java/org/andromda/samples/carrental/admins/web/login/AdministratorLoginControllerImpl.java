// license-header java merge-point
package org.andromda.samples.carrental.admins.web.login;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.admins.web.login.AdministratorLoginController
 */
public class AdministratorLoginControllerImpl extends AdministratorLoginController
{
    /**
     * @see org.andromda.samples.carrental.admins.web.login.AdministratorLoginController#authenticateAsAdministrator(org.apache.struts.action.ActionMapping, org.andromda.samples.carrental.admins.web.login.AuthenticateAsAdministratorForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String authenticateAsAdministrator(ActionMapping mapping, org.andromda.samples.carrental.admins.web.login.AuthenticateAsAdministratorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
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