package org.andromda.core.translation.library;

import java.util.HashMap;
import java.util.Map;

import org.andromda.core.common.BasePlugin;
import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.translation.Translator;

/**
 * The AndroMDA Translation Library implementation of the Plugin. Library
 * instances are configured from
 * <code>META-INF/andromda-translation-library.xml</code> files discovered on
 * the classpath.
 * 
 * @author Chad Brandon
 */
public class Library
    extends BasePlugin
{

    private Map libraryTranslations = new HashMap();

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
    public void addLibraryTranslation(LibraryTranslation libraryTranslation)
    {
        final String methodName = "Library.addLibraryTranslation";
        ExceptionUtils.checkNull(
            methodName,
            "libraryTranslation",
            libraryTranslation);
        libraryTranslation.setLibrary(this);
        this.libraryTranslations.put(
            libraryTranslation.getName(),
            libraryTranslation);
    }

    /**
     * Gets the LibraryTranslation instances (keyed by name) which are part of
     * this Library.
     * 
     * @return Map
     */
    public Map getLibraryTranslations()
    {
        return this.libraryTranslations;
    }

    /**
     * Retrieves the LibraryTranslation with the specified name.
     * 
     * @param name
     * @return LibraryTranslation the LibraryTranslation corresponding to the
     *         <code>name</code>.
     */
    public LibraryTranslation getLibraryTranslation(String name)
    {
        final String methodName = "Library.getLibraryTranslation";
        ExceptionUtils.checkEmpty(methodName, "name", name);
        return (LibraryTranslation)this.libraryTranslations.get(name);
    }

    /**
     * Sets the <cod>translatorClass</code> that will perform the translation
     * processing.
     * 
     * @param translatorClass the Class for the Translator implementation.
     */
    public void setTranslator(String translatorClass)
    {
        final String methodName = "Library.setTranslator";
        try
        {
            ComponentContainer.instance().registerDefaultComponent(
                Translator.class,
                ClassUtils.loadClass(translatorClass));
        }
        catch (Throwable th)
        {
            String errMsg = "Error performing " + methodName;
            logger.error(errMsg, th);
            throw new LibraryException(errMsg, th);
        }
    }

    /**
     * @see org.andromda.core.common.BasePlugin#populateTemplateContext(java.util.Map)
     */
    public void populateTemplateContext(Map templateContext)
    {
        super.populateTemplateContext(templateContext);
    }

    /**
     * @see org.andromda.core.common.Plugin#getType()
     */
    public String getType()
    {
        return "translation-library";
    }
}