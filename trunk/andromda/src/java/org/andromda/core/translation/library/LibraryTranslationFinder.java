package org.andromda.core.translation.library;

import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Finds LibraryTranslations by code>translation</code> (i.e. library and
 * name).
 * 
 * @author Chad Brandon
 */
public class LibraryTranslationFinder
{

    private static Logger logger = Logger
        .getLogger(LibraryTranslationFinder.class);
    
    protected static Map libraryTranslations = new HashMap();

    /**
     * Finds the library with the specified libraryName.
     * 
     * @param libraryName
     * @return Library - returns the Library found or null if none is found.
     */
    protected static Library findLibrary(String libraryName)
    {
        return (Library)ComponentContainer.instance()
            .findComponent(libraryName);
    }

    /**
     * Finds the LibraryTranslation with the specified translationName.
     * 
     * @param translation the name of the translation to find.
     * @return LibraryTranslation returns the LibraryTranslation found or null
     *         if none is found.
     */
    public static LibraryTranslation findLibraryTranslation(String translation)
    {
        final String methodName = "LibraryTranslation.findLibraryTranslation";
        ExceptionUtils.checkEmpty(methodName, "translation", translation);

        LibraryTranslation libraryTranslation = (LibraryTranslation)libraryTranslations
            .get(translation);

        if (libraryTranslation == null)
        {
            char libSeparator = '.';
            int index = translation.indexOf(libSeparator);
            if (index == -1)
            {
                throw new IllegalArgumentException(
                    methodName
                        + " -  libraryTranslation '"
                        + translation
                        + "' must contain the character '"
                        + libSeparator
                        + "' in order to seperate the library name from the translation"
                        + " name (must be in the form: <library name>.<translation name>)");
            }
            String libraryName = translation.substring(0, index);
            Library library = findLibrary(libraryName);
            int translationLength = translation.length();

            String translationName = translation.substring(
                index + 1,
                translationLength);

            if (library != null)
            {
                libraryTranslation = library
                    .getLibraryTranslation(translationName);
                if (libraryTranslation == null)
                {
                    logger.error("ERROR! no translation '" + translationName
                        + "' found within library --> '" + libraryName + "'");
                }
            }
        }
        return libraryTranslation;
    }

}