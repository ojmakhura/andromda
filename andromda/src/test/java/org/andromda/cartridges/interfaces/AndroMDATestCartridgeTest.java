package org.andromda.cartridges.interfaces;

import org.andromda.core.common.XmlObjectFactory;

import junit.framework.TestCase;

/**
 * Tests a sample cartridge implementation just to see if the cartridge
 * interface classes work OK.
 * 
 * @see org.andromda.cartridges.interfaces.AndroMDATestCartridge
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
        // set validation off since the parser's used by JUnit
        // don't seem to support schema validation
        XmlObjectFactory.setDefaultValidating(false);
        fCartridge.setDescriptor(DefaultCartridgeDescriptor.getInstance(
            this.getClass().getResource("SampleCartridgeDescriptor.xml")));
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
        CartridgeDescriptor icc = fCartridge.getDescriptor();
        assertNotNull(icc);

        assertEquals("andromda-ejb", icc.getCartridgeName());
        
        assertNotNull(icc.getProperties());
        assertEquals(icc.getProperties().get("persistence"), "ejb");

        assertEquals(
            "TemplateConfiguration: EntityBean EntityBean.vsl {0}/{1}Bean.java beans true true",
            icc.getTemplateConfigurations().get(0).toString());
            
        assertEquals(
            "TemplateConfiguration: EntityBean EntityBeanImpl.vsl {0}/{1}BeanImpl.java impls false true",
            icc.getTemplateConfigurations().get(1).toString());

        assertEquals(
            "TemplateConfiguration: EntityBean EntityBeanCMP.vsl {0}/{1}BeanCMP.java beans true false",
            icc.getTemplateConfigurations().get(2).toString());
    }

}
