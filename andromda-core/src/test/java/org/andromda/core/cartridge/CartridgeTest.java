package org.andromda.core.cartridge;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import junit.framework.TestCase;
import org.andromda.core.cartridge.template.ModelElement;
import org.andromda.core.cartridge.template.Template;
import org.andromda.core.cartridge.template.Type;
import org.andromda.core.common.ComponentContainer;
import org.andromda.core.common.TemplateObject;
import org.andromda.core.namespace.NamespaceComponents;
import org.andromda.core.profile.Profile;

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
        NamespaceComponents.instance().discover();
        Collection<Cartridge> cartridges = ComponentContainer.instance().findComponentsOfType(Cartridge.class);
        assertNotNull(cartridges);
        this.cartridge = cartridges.iterator().next();
        Profile.instance().setNamespace(this.cartridge.getNamespace());
        this.cartridge.initialize();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown()
        throws Exception
    {
        this.cartridge = null;
    }

    /**
     * 
     */
    public void testGetNamespace()
    {
        assertEquals(
            "test",
            this.cartridge.getNamespace());
    }

    /**
     * 
     */
    public void testGetResources()
    {
        Collection<Resource> resources = this.cartridge.getResources();
        assertNotNull(resources);
        assertEquals(
            3,
            resources.size());

        // first template
        final Iterator<Resource> templateIterator = resources.iterator();
        Resource resource = templateIterator.next();
        assertTrue(resource.isLastModifiedCheck());
        
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
        Collection<ModelElement> modelElements = template.getSupportedModeElements().getModelElements();
        assertNotNull(modelElements);
        assertEquals(
            1,
            modelElements.size());
        ModelElement element = modelElements.iterator().next();
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
        assertEquals("$viewType.equals('jsp')", template.getOutputCondition());
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
        element = modelElements.iterator().next();
        assertNull(element.getVariable());
        assertNull(element.getStereotype());
        
        final Collection<Type> types = element.getTypes();
        assertNotNull(types);
        assertEquals(1, types.size());
        final Type type = types.iterator().next();
        final Collection properties = type.getProperties();
        assertEquals(2, properties.size());
        final Iterator propertyIterator = properties.iterator();
        Type.Property property1 = (Type.Property)propertyIterator.next();
        assertEquals("propertyOne", property1.getName());
        assertEquals("", property1.getValue());
        Type.Property property2 = (Type.Property)propertyIterator.next();
        assertEquals("propertyThree", property2.getName());
        assertEquals("Contents", property2.getValue());
        
        final Map<String, String> conditions = cartridge.getConditions();
        assertEquals(1, conditions.size());
        final String expressionName = conditions.keySet().iterator().next();
        assertEquals("viewTypeIsJsp", expressionName);
        final String expressionValue = conditions.get(expressionName);
        assertEquals("$viewType.equalsIgnoreCase('jsp')", expressionValue);
    }

    /**
     * 
     */
    public void testGetPropertyReferences()
    {
        String[] propertyRefs = this.cartridge.getPropertyReferences();
        assertNotNull(propertyRefs);
        assertEquals(
            2,
            propertyRefs.length);

        String propertyReferenceOne = "propertyReferenceOne";
        String propertyReferenceTwo = "propertyReferenceTwo";

        assertEquals(propertyReferenceOne, propertyRefs[0]);
        assertEquals(propertyReferenceTwo, propertyRefs[1]);
    }

    /**
     * 
     */
    public void testGetTemplateObjects()
    {
        final Collection<TemplateObject> templateObjects = this.cartridge.getTemplateObjects();
        assertNotNull(templateObjects);
        assertEquals(
            1,
            templateObjects.size());
        TemplateObject templateObject = templateObjects.iterator().next();
        assertEquals("utils", templateObject.getName());
        assertEquals("test",templateObject.getNamespace());
        CartridgeTemplateObject object = (CartridgeTemplateObject)templateObject.getObject();
        assertNotNull(object);
        assertEquals("3", object.getDefinitionOne());
        
    }

    /**
     * 
     */
    public void testGetContents()
    {
        Collection<String> contents = this.cartridge.getContents();
        assertNotNull(contents);

        //    TODO wrong Test, new Integer() are always not same!
        // make sure there's more than 0 contents listed
//        assertNotSame(
//            new Integer(0),
//            new Integer(contents.size()));
    }
}
