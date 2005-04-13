package org.andromda.samples.carrental.admins.web.admin;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController
 */
public class AdminAdminControllerImpl
        extends AdminAdminController
{
    /**
     * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController#updateAdministrator(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.admins.web.admin.UpdateAdministratorForm, javax.servlet.http.HttpServletRequest,
            *      javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String updateAdministrator(ActionMapping mapping,
                                                      org.andromda.samples.carrental.admins.web.admin.UpdateAdministratorForm form,
                                                      HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setAccountNo("accountNo-test");
        form.setName("name-test");
        form.setEmail("email-test");
        return null;
    }

    /**
     * @see org.andromda.samples.carrental.admins.web.admin.AdminAdminController#addAdministrator(org.apache.struts.action.ActionMapping,
            *      org.andromda.samples.carrental.admins.web.admin.AddAdministratorForm, javax.servlet.http.HttpServletRequest,
            *      javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String addAdministrator(ActionMapping mapping,
                                                   org.andromda.samples.carrental.admins.web.admin.AddAdministratorForm form,
                                                   HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setAccountNo("accountNo-test");
        form.setName("name-test");
        form.setEmail("email-test");
        return null;
    }

}
