package org.andromda.cartridges.database;

import org.andromda.core.profile.Profile;
import org.andromda.metafacades.uml.UMLProfile;

public final class DatabaseProfile extends UMLProfile
{
    /**
     * The Profile instance from which we retrieve the mapped profile names.
     */
    private static final Profile profile = Profile.instance();

    /**
     * Sets the dummy data load for testing using a database with meaningless data.
     */
    public static final String TAGGEDVALUE_DUMMYLOAD_SIZE = profile.get("DATABASE_DUMMYLOAD_SIZE");
    public static final int DUMMY_LOAD_SIZE_DEFAULT = 20;
}
