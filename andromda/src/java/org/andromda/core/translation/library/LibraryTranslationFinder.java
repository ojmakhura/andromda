package org.andromda.core.translation.library;

import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.apache.log4j.Logger;


/**
 * Finds LibraryTranslations by code>translation</code> (i.e. library and name).
 *
 * @author Chad Brandon
 */
public class LibraryTranslationFinder
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(LibraryTranslationFinder.class);

    /**
     * Stores the found library translations.
     */
    protected static final Map libraryTranslations = new HashMap();

    /**
     * Finds the library with the specified libraryName.
     *
     * @param libraryName
     * @return the Library found or null if none is found.
     */
    protected static final Library findLibrary(final String libraryName)
    {
        return (Library)ComponentContainer.instance().findComponentByNamespace(libraryName, Library.class);
    }

    /**
     * Finds the LibraryTranslation with the specified translationName.
     *
     * @param translation the name of the translation to find.
     * @return the LibraryTranslation found or null if none is found.
     */
    public static LibraryTranslation findLibraryTranslation(final String translation)
    {
        ExceptionUtils.checkEmpty("translation", translation);

        LibraryTranslation libraryTranslation = (LibraryTranslation)libraryTranslations.get(translation);

        if (libraryTranslation == null)
        {
            char libSeparator = '.';
            int index = translation.indexOf(libSeparator);
            if (index == -1)
            {
                throw new IllegalArgumentException(
                    "libraryTranslation '" + translation + "' must contain the character '" +
                    libSeparator + "' in order to seperate the library name from the translation" +
                    " name (must be in the form: <library name>.<translation name>)");
            }
            final String libraryName = translation.substring(0, index);
            final Library library = findLibrary(libraryName);
            final int translationLength = translation.length();

            final String translationName = translation.substring(index + 1, translationLength);

            if (library != null)
            {
                libraryTranslation = library.getLibraryTranslation(translationName);
                if (libraryTranslation == null)
                {
                    logger.error(
                        "ERROR! no translation '" + translationName + "' found within library --> '" + libraryName +
                        "'");
                }
            }
        }
        return libraryTranslation;
    }
}