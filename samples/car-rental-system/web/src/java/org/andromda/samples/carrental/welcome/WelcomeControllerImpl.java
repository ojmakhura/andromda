package org.andromda.samples.carrental.welcome;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;

/**
 * @see org.andromda.samples.carrental.welcome.WelcomeController
 */
public class WelcomeControllerImpl extends WelcomeController
{
    /**
     * @see org.andromda.samples.carrental.welcome.WelcomeController#getWelcomeMessage(org.apache.struts.action.ActionMapping, org.andromda.samples.carrental.welcome.GetWelcomeMessageForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final java.lang.String getWelcomeMessage(ActionMapping mapping, org.andromda.samples.carrental.welcome.GetWelcomeMessageForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        WelcomeForm form2 = (WelcomeForm) form;
        StringBuffer welcomeMessage = new StringBuffer();
        // TODO Make NLSable
        welcomeMessage.append( "Welcome to the Andromda Car Rental Sample Application.\n");
        welcomeMessage.append( "\n");
        welcomeMessage.append( "This is the replacement Car Rental Application using the BPM4Struts cartridge.");
        welcomeMessage.append( "This is work in progress.");
        welcomeMessage.append( "It is basically a skeleton application.");
        welcomeMessage.append( "Over time the functions in this application will be implemented.");
        form2.setWelcomeMessage( welcomeMessage.toString() );
        return "";
    }


}
