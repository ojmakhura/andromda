package org.andromda.core.cartridge;

import junit.framework.TestCase;

import org.andromda.core.common.XmlObjectFactory;

/**
 * Tests a sample cartridge implementation just to see if the cartridge
 * interface classes work OK.
 * 
 * @see org.andromda.core.cartridge.AndroMDATestCartridge
 * @since 01.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>

 */
public class AndroMDATestCartridgeTest extends TestCase
{
    private Cartridge testCartridge;

    /**
     * Constructor for AndroMDATestCartridgeTest.
     * @param arg0
     */
    public AndroMDATestCartridgeTest(String arg0)
    {
        super(arg0);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        // set validation off since the parser used by JUnit
        // don't seem to support schema validation
        XmlObjectFactory.setDefaultValidating(false);
        this.testCartridge = Cartridge.getInstance(
            this.getClass().getResource("SampleCartridgeDescriptor.xml"));
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        testCartridge = null;
    }

    public void testGetCapabilities()
    {
        assertNotNull(testCartridge);

        assertEquals("andromda-ejb", testCartridge.getName());

        assertEquals(2, testCartridge.getTemplateConfigurations().size());
    }
}
