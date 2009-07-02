package org.andromda.core.translation.library;

import java.util.LinkedHashMap;
import java.util.Map;

import org.andromda.core.common.BasePlugin;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.Translator;


/**
 * The AndroMDA Translation Library implementation of the Plugin. Library instances are configured from
 * <code>META-INF/andromda-translation-library.xml</code> files discovered on the classpath.
 *
 * @author Chad Brandon
 */
public class Library
    extends BasePlugin
{
    private final Map<String, LibraryTranslation> libraryTranslations = new LinkedHashMap<String, LibraryTranslation>();

    /**
     * The default Library constructor.
     */
    public Library()
    {
        super();
    }

    /**
     * Adds a new LibraryTranslation.
     *
     * @param libraryTranslation
     */
    public void addLibraryTranslation(final LibraryTranslation libraryTranslation)
    {
        ExceptionUtils.checkNull("libraryTranslation", libraryTranslation);
        libraryTranslation.setLibrary(this);
        this.libraryTranslations.put(
            libraryTranslation.getName(),
            libraryTranslation);
    }

    /**
     * Gets the LibraryTranslation instances (keyed by name) which are part of this Library.
     *
     * @return Map
     */
    public Map<String, LibraryTranslation> getLibraryTranslations()
    {
        return this.libraryTranslations;
    }

    /**
     * Retrieves the LibraryTranslation with the specified name.
     *
     * @param name
     * @return LibraryTranslation the LibraryTranslation corresponding to the <code>name</code>.
     */
    public LibraryTranslation getLibraryTranslation(final String name)
    {
        ExceptionUtils.checkEmpty("name", name);
        return this.libraryTranslations.get(name);
    }

    /**
     * Sets the <cod>translatorClass</code> that will perform the translation processing.
     *
     * @param translatorClass the Class for the Translator implementation.
     */
    public void setTranslator(final String translatorClass)
    {
        try
        {
            ComponentContainer.instance().registerDefaultComponent(
                Translator.class,
                ClassUtils.loadClass(translatorClass));
        }
        catch (final Throwable throwable)
        {
            throw new LibraryException(throwable);
        }
    }

    /**
     * @see org.andromda.core.common.BasePlugin#populateTemplateContext(java.util.Map)
     */
    public void populateTemplateContext(final Map<String, Object> templateContext)
    {
        super.populateTemplateContext(templateContext);
    }
}