package org.andromda.cartridges.mgmt.test;

import java.util.Iterator;

import org.andromda.cartridges.interfaces.DefaultAndroMDACartridge;
import org.andromda.cartridges.interfaces.DefaultCartridgeDescriptor;
import org.andromda.cartridges.interfaces.IAndroMDACartridge;
import org.andromda.cartridges.mgmt.CartridgeDictionary;

import junit.framework.TestCase;

/**
 * Test for the methods in the CartridgeDictionary class.
 * 
 * @see org.andromda.cartridges.mgmt.CartridgeDictionary
 * @since 01.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class CartridgeDictionaryTest extends TestCase
{
    private CartridgeDictionary fDictionary;

    /**
     * Constructor for CartridgeDictionaryTest.
     * @param arg0
     */
    public CartridgeDictionaryTest(String arg0)
    {
        super(arg0);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        fDictionary = new CartridgeDictionary();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        fDictionary = null;
    }

    final public void testAddCartridge()
    {
        DefaultAndroMDACartridge c1 = new DefaultAndroMDACartridge();
        DefaultCartridgeDescriptor cd = new DefaultCartridgeDescriptor();
        cd.setCartridgeName("c1");
        c1.setDescriptor(cd);        
        
        DefaultAndroMDACartridge c2 = new DefaultAndroMDACartridge();
        DefaultCartridgeDescriptor cd2 = new DefaultCartridgeDescriptor();
        cd2.setCartridgeName("c2");
        c2.setDescriptor(cd2);        

        fDictionary.addCartridge("s1", c1);
        fDictionary.addCartridge("s1", c2);
        fDictionary.addCartridge("s2", c2);
        
        assertEquals (2, fDictionary.lookupCartridges("s1").size());
        Iterator it = fDictionary.lookupCartridges("s1").iterator();
        assertEquals ("c1", ((IAndroMDACartridge)it.next()).getDescriptor().getCartridgeName());
        assertEquals ("c2", ((IAndroMDACartridge)it.next()).getDescriptor().getCartridgeName());
        
        assertEquals (1, fDictionary.lookupCartridges("s2").size());
        assertNull (fDictionary.lookupCartridges("s999"));
    }
}
