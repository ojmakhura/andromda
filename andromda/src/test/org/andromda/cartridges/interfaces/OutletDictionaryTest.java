package org.andromda.cartridges.interfaces;

import java.io.File;

import org.andromda.cartridges.interfaces.OutletDictionary;

import junit.framework.TestCase;

/**
 * @since 02.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class OutletDictionaryTest extends TestCase
{
    private OutletDictionary fOutletDictionary;

    /**
     * Constructor for OutletDictionaryTest.
     * @param arg0
     */
    public OutletDictionaryTest(String arg0)
    {
        super(arg0);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        fOutletDictionary = new OutletDictionary();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        fOutletDictionary = null;
    }

    public void testAddOutletMapping() 
    {
        fOutletDictionary.addOutletMapping("ejb", "beans", new File("mybeansdir"));
        assertEquals(new File("mybeansdir"), fOutletDictionary.lookupOutlet("ejb", "beans"));
    }
}
