package org.andromda.samples.carrental.admins.web.login;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.admins.web.login.AdministratorLoginController
 */
public class AdministratorLoginControllerImpl
        extends AdministratorLoginController
{
    /**
     * @see org.andromda.samples.carrental.admins.web.login.AdministratorLoginController#authenticateAsAdministrator(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.admins.web.login.AuthenticateAsAdministratorForm,
            *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String authenticateAsAdministrator(ActionMapping mapping,
                                                              org.andromda.samples.carrental.admins.web.login.AuthenticateAsAdministratorForm form,
                                                              HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        // TODO add logic to authenticate
        
        String result = "fail";
        boolean accountOK = form.getAccountNo().equals("999999");
        boolean passwordOK = form.getPassword().equals("admin");
        boolean nameOK = form.getName().equals("admin");
        if (accountOK && passwordOK && nameOK)
        {
            result = "success";
        }
        return result;
    }

}
