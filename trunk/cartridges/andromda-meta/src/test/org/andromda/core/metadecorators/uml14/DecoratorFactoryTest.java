package org.andromda.core.metadecorators.uml14;

import junit.framework.TestCase;

import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;

public class DecoratorFactoryTest extends TestCase
{
    /**
     * Constructor for DecoratorFactoryTest.
     * @param arg0
     */
    public DecoratorFactoryTest(String arg0)
    {
        super(arg0);
    }

    public void testRegisterDecoratorClass()
    {
        DecoratorFactory df = DecoratorFactory.getInstance();
        assertEquals(0, df.getNamespaceCount());
        
        df.setActiveNamespace("core");
        assertEquals("core", df.getActiveNamespace());
        assertEquals(1, df.getNamespaceCount());
        assertEquals(0, df.getDecoratorCount());

        df.registerDecoratorClass(Classifier.class.getName(), null, SampleClassifierDecorator.class.getName());
        assertEquals(1, df.getDecoratorCount());
        df.registerDecoratorClass(Attribute.class.getName(), null, SampleAttributeDecorator.class.getName());
        assertEquals(2, df.getDecoratorCount());
        
        String decoratorClassName = df.lookupDecoratorClass(Classifier.class.getName(), null);
        assertEquals(SampleClassifierDecorator.class.getName(), decoratorClassName);
        
        // even if stereotype is given, the normal decorator class should be returned
        decoratorClassName = df.lookupDecoratorClass(Classifier.class.getName(), "Entity");
        assertEquals(SampleClassifierDecorator.class.getName(), decoratorClassName);
        
        decoratorClassName = df.lookupDecoratorClass(Attribute.class.getName(), null);
        assertEquals(SampleAttributeDecorator.class.getName(), decoratorClassName);
        
        // even if stereotype is given, the normal decorator class should be returned
        decoratorClassName = df.lookupDecoratorClass(Attribute.class.getName(), "PrimaryKey");
        assertEquals(SampleAttributeDecorator.class.getName(), decoratorClassName);

        // test negative case
        decoratorClassName = df.lookupDecoratorClass("unregistered.class.Name", "PrimaryKey");
        assertNull(decoratorClassName);
        


        df.setActiveNamespace("hibernate");
        assertEquals(2, df.getNamespaceCount());
        assertEquals("hibernate", df.getActiveNamespace());
        
        df.registerDecoratorClass(Classifier.class.getName(), "Entity", SampleEntityDecorator.class.getName());
        assertEquals(1, df.getDecoratorCount());

        // if stereotype is not given, the core's classifier decorator should be returned
        decoratorClassName = df.lookupDecoratorClass(Classifier.class.getName(), null);
        assertEquals(SampleClassifierDecorator.class.getName(), decoratorClassName);
        
        // now, if stereotype is given, the decorator for entity classes should be returned
        decoratorClassName = df.lookupDecoratorClass(Classifier.class.getName(), "Entity");
        assertEquals(SampleEntityDecorator.class.getName(), decoratorClassName);



        df.setActiveNamespace("ejb");
        assertEquals(3, df.getNamespaceCount());
        assertEquals("ejb", df.getActiveNamespace());
        
        df.registerDecoratorClass(Classifier.class.getName(), "Entity", SampleEntityBeanDecorator.class.getName());
        assertEquals(1, df.getDecoratorCount());

        // now, if stereotype is given, the decorator for entity beans should be returned
        decoratorClassName = df.lookupDecoratorClass(Classifier.class.getName(), "Entity");
        assertEquals(SampleEntityBeanDecorator.class.getName(), decoratorClassName);



        df.setActiveNamespace("hibernate");
        assertEquals(3, df.getNamespaceCount());
        assertEquals("hibernate", df.getActiveNamespace());
        
        // now, if stereotype is given, the decorator for hibernate's entity classes should be returned again
        // without interfering with the other registration for the stereotype "Entity"
        decoratorClassName = df.lookupDecoratorClass(Classifier.class.getName(), "Entity");
        assertEquals(SampleEntityDecorator.class.getName(), decoratorClassName);
    }

}
