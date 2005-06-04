package org.andromda.core.translation.library;

import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.andromda.core.common.PluginDiscoverer;
import org.andromda.core.common.XmlObjectFactory;


/**
 * Implements the JUnit test suit for
 * <code>org.andromda.core.translation.library.Library</code>
 *
 * @see org.andromda.core.library.LibraryTest
 * @author Chad Brandon
 */
public class LibraryTest
    extends TestCase
{
    private Library library;

    /**
     * Constructor for LibraryTest.
     *
     * @param name
     */
    public LibraryTest(String name)
    {
        super(name);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        // set validation off since the parser used by JUnit
        // doesn't seem to support schema validation
        XmlObjectFactory.setDefaultValidating(false);
        PluginDiscoverer.instance().discoverPlugins();
        Collection librarys = PluginDiscoverer.instance().findPlugins(Library.class);
        assertNotNull(librarys);
        this.library = (Library)librarys.iterator().next();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown()
        throws Exception
    {
        this.library = null;
    }

    public void testGetName()
    {
        assertEquals(
            "test-translation-library",
            this.library.getName());
    }

    public void testGetLibraryTranslations()
    {
        Map libraryTranslations = this.library.getLibraryTranslations();
        assertNotNull(libraryTranslations);
        assertEquals(
            2,
            libraryTranslations.size());
        LibraryTranslation translationOne = (LibraryTranslation)libraryTranslations.get("TranslationOne");
        assertNotNull(translationOne);
        assertNotNull(translationOne.getTemplate());
        assertEquals(
            "translations/test/TranslationOne.vsl",
            translationOne.getTemplate());
        assertEquals(
            "element",
            translationOne.getVariable());
        assertNotNull(translationOne.getTranslator());
        assertEquals(
            "org.andromda.core.translation.library.TestTranslator",
            translationOne.getTranslator().getClass().getName());
        LibraryTranslation translationTwo = (LibraryTranslation)libraryTranslations.get("TranslationTwo");
        assertNotNull(translationTwo);
        assertNull(translationTwo.getTemplate());
        assertNull(translationTwo.getVariable());
        assertNotNull(translationTwo.getTranslator());
        assertEquals(
            "org.andromda.core.translation.library.TestSubTranslator",
            translationTwo.getTranslator().getClass().getName());
    }

    public void testGetPropertyReferences()
    {
        Map propertyRefs = this.library.getPropertyReferences();
        assertNotNull(propertyRefs);
        assertEquals(
            2,
            propertyRefs.size());

        String propertyReferenceOne = "propertyReferenceWithDefault";
        String propertyReferenceTwo = "propertyReferenceNoDefault";

        assertTrue(propertyRefs.containsKey(propertyReferenceOne));
        assertTrue(propertyRefs.containsKey(propertyReferenceTwo));

        assertEquals(
            "aDefaultValue",
            propertyRefs.get(propertyReferenceOne));
        assertEquals(
            null,
            propertyRefs.get(propertyReferenceTwo));
    }

    public void testGetType()
    {
        assertNotNull(this.library.getType());
        assertEquals(
            "translation-library",
            this.library.getType());
    }

    public void testGetTemplateObjects()
    {
        assertNotNull(this.library.getTemplateObjects());
        assertEquals(
            1,
            this.library.getTemplateObjects().size());
    }
}