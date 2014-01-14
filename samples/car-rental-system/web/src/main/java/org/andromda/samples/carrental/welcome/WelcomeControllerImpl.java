package org.andromda.samples.carrental.welcome;


/**
 * @see org.andromda.samples.carrental.welcome.WelcomeController
 */
public class WelcomeControllerImpl
    extends WelcomeController
{

	/**
	 * @see org.andromda.samples.carrental.welcome.WelcomeController#getWelcomeMessage()
	 */
	@Override
	public String getWelcomeMessage() throws Throwable {
		final String welcomeMessage = "Welcome to the Andromda Car Rental Sample Application.\n"
				+ '\n'
				+ "This is the replacement Car Rental Application using the BPM4Struts cartridge."
				+ "This is work in progress."
				+ "It is basically a skeleton application."
				+ "Over time the functions in this application will be implemented.";

		return welcomeMessage;
	}
}
