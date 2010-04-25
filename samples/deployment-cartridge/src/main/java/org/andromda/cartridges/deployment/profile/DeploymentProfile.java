package org.andromda.cartridges.deployment.profile;

import org.andromda.core.profile.Profile;

/**
 * Represents stereotypes and tagged values to be used with the deployment
 * cartridge.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class DeploymentProfile
{

    private static final Profile profile = Profile.instance();

    /* ----------------- Stereotypes -------------------- */

    public static final String STEREOTYPE_MANIFEST = profile.get("MANIFEST");
    public static final String STEREOTYPE_WRAPS = profile.get("WRAPS");
}
