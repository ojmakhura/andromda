package org.andromda.core.cartridge;

import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.andromda.core.common.PluginDiscoverer;
import org.andromda.core.common.XmlObjectFactory;

/**
 * Implements the JUnit test suit for 
 * <code>org.andromda.core.cartridge.Cartridge</code>
 * 
 * @see org.andromda.core.cartridge.Cartridge
 * 
 * @since 01.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon

 */
public class CartridgeTest extends TestCase
{
    private Cartridge cartridge;

    /**
     * Constructor for AndroMDATestCartridgeTest.
     * @param arg0
     */
    public CartridgeTest(String arg0)
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
        Collection cartridges = 
            PluginDiscoverer.instance().findPlugins(
                Cartridge.class);
        assertNotNull(cartridges);
        this.cartridge = (Cartridge)cartridges.iterator().next();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        this.cartridge = null;
    }
    
    public void testGetName() 
    {
        assertEquals("andromda-sample", this.cartridge.getName());
    }
    
    public void testGetTemplates() 
    {
        assertNotNull(this.cartridge.getTemplateConfigurations());
        assertEquals(2, this.cartridge.getTemplateConfigurations().size());
    }

    public void testGetPropertyReferences() 
    {
        Map propertyRefs = this.cartridge.getPropertyReferences();
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
        assertNotNull(this.cartridge.getType());
        assertEquals("cartridge", this.cartridge.getType());
    }
    
    public void testGetTemplateObjects() 
    {
        assertNotNull(this.cartridge.getTemplateObjects());
        assertEquals(1, this.cartridge.getTemplateObjects().size());
    }
}
