package org.andromda.core.translation.library;

import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.TemplateObject;
import org.andromda.core.namespace.NamespaceComponents;


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
        NamespaceComponents.instance().discover();
        Collection librarys = ComponentContainer.instance().findComponentsOfType(Library.class);
        assertNotNull(librarys);
        this.library = (Library)librarys.iterator().next();
        this.library.initialize();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown()
        throws Exception
    {
        this.library = null;
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
        String[] propertyRefs = this.library.getPropertyReferences();
        assertNotNull(propertyRefs);
        assertEquals(
            2,
            propertyRefs.length);

        String propertyReferenceOne = "propertyReferenceOne";
        String propertyReferenceTwo = "propertyReferenceTwo";

        assertEquals(propertyReferenceOne, propertyRefs[0]);
        assertEquals(propertyReferenceTwo, propertyRefs[1]);
    }

    public void testGetTemplateObjects()
    {
        final Collection templateObjects = this.library.getTemplateObjects();
        assertNotNull(templateObjects);
        assertEquals(
            1,
            templateObjects.size());
        TemplateObject templateObject = ((TemplateObject)templateObjects.iterator().next());
        assertEquals("utils", templateObject.getName());
        assertEquals("test",templateObject.getNamespace());
        LibraryTemplateObject object = (LibraryTemplateObject)templateObject.getObject();
        assertNotNull(object);
        assertEquals("3", object.getDefinitionOne());
    }
}