package org.andromda.android.core.internal.settings;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Set up default preferences for Android.
 * 
 * @author Peter Friese
 * @since 06.12.2005
 */
public class PreferenceInitializer
        extends AbstractPreferenceInitializer
{

    /** Default location of andromda.xml (relative to project root) */
    private static final String DEFAULT_CONFIGURATION_LOCATION = "mda/src/main/config/andromda.xml";

    /**
     * {@inheritDoc}
     */
    public void initializeDefaultPreferences()
    {
        AndroidSettingsAccess.instance().setDefaultConfigurationLocation(DEFAULT_CONFIGURATION_LOCATION);
    }

}
