package org.andromda.cartridges.database;

import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.core.common.Profile;

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

    /**
     * The name to display instead of the name modeled on the entity.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_TABLE_DISPLAYNAME =
            profile.get("DATABASE_CONSOLE_TABLE_DISPLAYNAME");

    /**
     * The name to use when exporting as another table's foreign key.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_TABLE_DISPLAYCOLUMN =
            profile.get("DATABASE_CONSOLE_TABLE_DISPLAYCOLUMN");

    /**
     * The number of records to show on the same page.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_TABLE_PAGESIZE =
            profile.get("DATABASE_CONSOLE_TABLE_PAGESIZE");

    /**
     * The maximum number of records to retrieve from the database.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_TABLE_MAXLISTSIZE =
            profile.get("DATABASE_CONSOLE_TABLE_MAXLISTSIZE");

    /**
     * Allows the table to be exported into different formats.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_TABLE_EXPORTABLE =
            profile.get("DATABASE_CONSOLE_TABLE_EXPORTABLE");

    /**
     * Allows data to be inserted into the table.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_TABLE_INSERTABLE =
            profile.get("DATABASE_CONSOLE_TABLE_INSERTABLE");

    /**
     * The name to display instead of the entity attribute's name.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_DISPLAYNAME =
            profile.get("DATABASE_CONSOLE_COLUMN_DISPLAYNAME");

    /**
     * Whether or not foreign keys should be resolved so that they can be presented
     * into a drop-down combobox, instead of an empty textfield.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_RESOLVEFK =
            profile.get("DATABASE_CONSOLE_COLUMN_RESOLVEFK");

    /**
     * Whether or not to hide this column from the user.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_HIDE =
            profile.get("DATABASE_CONSOLE_COLUMN_HIDE");

    /**
     * Whether or not it should be possible to sort records based on this column.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_SORTABLE =
            profile.get("DATABASE_CONSOLE_COLUMN_SORTABLE");

    /**
     * Whether or not this column is updateable.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_UPDATEABLE =
            profile.get("DATABASE_CONSOLE_COLUMN_UPDATEABLE");

    /**
     * Whether or not this column should be allowed in exported format.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_EXPORTABLE =
            profile.get("DATABASE_CONSOLE_COLUMN_EXPORTABLE");

    /**
     * The maximum number of characters to display from the column's value, truncated values
     * will have a trailing ellipsis (...).
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_SIZE =
            profile.get("DATABASE_CONSOLE_COLUMN_SIZE");

    /**
     * A comma-separated list of values to which values in this column are restricted.
     */
    public static final String TAGGEDVALUE_DATABASE_CONSOLE_COLUMN_VALUES =
            profile.get("DATABASE_CONSOLE_COLUMN_VALUES");

}
