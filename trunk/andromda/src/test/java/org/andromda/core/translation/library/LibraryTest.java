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
public class LibraryTest extends TestCase
{
    private Library library;

    /**
     * Constructor for AndroMDATestLibraryTest.
     * @param arg0
     */
    public LibraryTest(String arg0)
    {
        super(arg0);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        // set validation off since the parser used by JUnit
        // doesn't seem to support schema validation
        XmlObjectFactory.setDefaultValidating(false);
        PluginDiscoverer.instance().discoverPlugins();
        Collection librarys = 
            PluginDiscoverer.instance().findPlugins(
                Library.class);
        assertNotNull(librarys);
        this.library = (Library)librarys.iterator().next();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        this.library = null;
    }
    
    public void testGetName() 
    {
        assertEquals("test-translation-library", this.library.getName());
    }
    
    public void testGetLibraryTranslations() 
    {
        assertNotNull(this.library.getLibraryTranslations());
        assertEquals(2, this.library.getLibraryTranslations().size());
    }

    public void testGetPropertyReferences() 
    {
        Map propertyRefs = this.library.getPropertyReferences();
        assertNotNull(propertyRefs);
        assertEquals(2, propertyRefs.size());
        
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
        assertEquals("translation-library", this.library.getType());
    }
    
    public void testGetTemplateObjects() 
    {
        assertNotNull(this.library.getTemplateObjects());
        assertEquals(1, this.library.getTemplateObjects().size());
    }
}
