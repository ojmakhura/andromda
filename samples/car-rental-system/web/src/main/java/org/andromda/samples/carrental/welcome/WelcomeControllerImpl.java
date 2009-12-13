package org.andromda.samples.carrental.welcome;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.welcome.WelcomeController
 */
public class WelcomeControllerImpl
    extends WelcomeController
{
    /**
     * @see org.andromda.samples.carrental.welcome.WelcomeController#getWelcomeMessage(org.apache.struts.action.ActionMapping,
     *      org.andromda.samples.carrental.welcome.GetWelcomeMessageForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String getWelcomeMessage(
        ActionMapping mapping,
        org.andromda.samples.carrental.welcome.GetWelcomeMessageForm form,
        HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        final String welcomeMessage = "Welcome to the Andromda Car Rental Sample Application.\n"
            + '\n'
            + "This is the replacement Car Rental Application using the BPM4Struts cartridge."
            + "This is work in progress." + "It is basically a skeleton application."
            + "Over time the functions in this application will be implemented.";

        return welcomeMessage;
    }
}