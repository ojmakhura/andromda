package org.andromda.cartridges.interfaces.test;

import junit.framework.TestCase;

import org.andromda.cartridges.interfaces.CartridgeXmlParser;
import org.andromda.cartridges.interfaces.ICartridgeDescriptor;

/**
 * Tests a sample cartridge implementation just to see if the cartridge
 * interface classes work OK.
 * 
 * @see org.andromda.cartridges.interfaces.test.AndroMDATestCartridge
 * @since 01.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>

 */
public class AndroMDATestCartridgeTest extends TestCase
{
    private AndroMDATestCartridge fCartridge;

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
        fCartridge = new AndroMDATestCartridge();
        fCartridge.setDescriptor(new CartridgeXmlParser().parse(getClass().getResourceAsStream("SampleCartridgeDescriptor.xml")));
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        fCartridge = null;
    }

    public void testGetCapabilities()
    {
        ICartridgeDescriptor icc = fCartridge.getDescriptor();
        assertNotNull(icc);

        assertEquals("andromda-ejb", icc.getCartridgeName());
        
        assertNotNull(icc.getProperties());
        assertEquals(icc.getProperties().get("persistence"), "ejb");

        assertNotNull(icc.getSupportedStereotypes());
        assertTrue(icc.getSupportedStereotypes().contains("EntityBean"));
        assertTrue(
            icc.getSupportedStereotypes().contains("StatelessSessionBean"));
        assertTrue(
            icc.getSupportedStereotypes().contains("StatefulSessionBean"));

        assertNotNull(icc.getOutlets());
        assertTrue(icc.getOutlets().contains("beans"));
        assertTrue(icc.getOutlets().contains("impls"));

        assertEquals(
            "TemplateConfiguration: EntityBean EntityBean.vsl {0}/{1}Bean.java beans true",
            icc.getTemplateConfigurations().get(0).toString());
    }

}
