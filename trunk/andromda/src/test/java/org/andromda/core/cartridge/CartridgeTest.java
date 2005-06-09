package org.andromda.core.cartridge;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.andromda.core.cartridge.template.ModelElement;
import org.andromda.core.cartridge.template.Template;
import org.andromda.core.cartridge.template.Type;
import org.andromda.core.common.PluginDiscoverer;
import org.andromda.core.common.XmlObjectFactory;


/**
 * Implements the JUnit test suit for
 * {@link org.andromda.core.cartridge.Cartridge}
 *
 * @see org.andromda.core.cartridge.Cartridge
 * @since 01.04.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class CartridgeTest
    extends TestCase
{
    private Cartridge cartridge;

    /**
     * Constructor for AndroMDATestCartridgeTest.
     *
     * @param name
     */
    public CartridgeTest(String name)
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
        Collection cartridges = PluginDiscoverer.instance().findPlugins(Cartridge.class);
        assertNotNull(cartridges);
        this.cartridge = (Cartridge)cartridges.iterator().next();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown()
        throws Exception
    {
        this.cartridge = null;
    }

    public void testGetName()
    {
        assertEquals(
            "andromda-test-cartridge",
            this.cartridge.getName());
    }

    public void testGetResources()
    {
        Collection resources = this.cartridge.getResources();
        assertNotNull(resources);
        assertEquals(
            2,
            resources.size());

        // first template
        final Iterator templateIterator = resources.iterator();
        Template template = (Template)templateIterator.next();
        assertEquals(
            "EntityBean.vsl",
            template.getPath());
        assertEquals(
            "{0}/{1}Bean.java",
            template.getOutputPattern());
        assertEquals(
            "beans",
            template.getOutlet());
        assertTrue(template.isOverwrite());
        assertNotNull(template.getSupportedModeElements());
        assertEquals(
            "entity",
            template.getSupportedModeElements().getVariable());
        Collection modelElements = template.getSupportedModeElements().getModelElements();
        assertNotNull(modelElements);
        assertEquals(
            1,
            modelElements.size());
        ModelElement element = (ModelElement)modelElements.iterator().next();
        assertEquals(
            "Entity",
            element.getStereotype());

        // second template
        template = (Template)templateIterator.next();
        assertEquals(
            "templates/webservice/axis/server-config.wsdd.vsl",
            template.getPath());
        assertEquals(
            "WEB-INF/server-config.wsdd",
            template.getOutputPattern());
        assertEquals(
            "axis-configuration",
            template.getOutlet());
        assertTrue(template.isOverwrite());
        assertTrue(template.isOutputToSingleFile());
        assertFalse(template.isOutputOnEmptyElements());
        assertNotNull(template.getSupportedModeElements());
        assertEquals(
            "services",
            template.getSupportedModeElements().getVariable());
        modelElements = template.getSupportedModeElements().getModelElements();
        assertNotNull(modelElements);
        assertEquals(
            1,
            modelElements.size());
        element = (ModelElement)modelElements.iterator().next();
        assertNull(element.getVariable());
        assertNull(element.getStereotype());
        
        final Collection types = element.getTypes();
        assertNotNull(types);
        assertEquals(1, types.size());
        final Type type = (Type)types.iterator().next();
        final Collection properties = type.getProperties();
        assertEquals(3, properties.size());
        final Iterator propertyIterator = properties.iterator();
        Type.Property property1 = (Type.Property)propertyIterator.next();
        assertEquals("propertyOne", property1.getName());
        assertEquals("", property1.getValue());
        Type.Property property2 = (Type.Property)propertyIterator.next();
        assertEquals("propertyTwo", property2.getName());
        assertEquals("Attribute", property2.getValue());
        Type.Property property3 = (Type.Property)propertyIterator.next();
        assertEquals("propertyThree", property3.getName());
        assertEquals("Contents", property3.getValue());
    }

    public void testGetPropertyReferences()
    {
        Map propertyRefs = this.cartridge.getPropertyReferences();
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
        assertNotNull(this.cartridge.getType());
        assertEquals(
            "cartridge",
            this.cartridge.getType());
    }

    public void testGetTemplateObjects()
    {
        assertNotNull(this.cartridge.getTemplateObjects());
        assertEquals(
            1,
            this.cartridge.getTemplateObjects().size());
    }

    public void testGetContents()
    {
        Collection contents = this.cartridge.getContents();
        assertNotNull(contents);

        // make sure there's more than 0 contents listed
        TestCase.assertNotSame(
            new Integer(0),
            new Integer(contents.size()));
    }
}