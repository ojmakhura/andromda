package org.andromda.templateengines.velocity;

import junit.framework.TestCase;

import org.andromda.core.common.Namespace;
import org.andromda.core.common.NamespaceProperties;
import org.andromda.core.common.Namespaces;
import org.andromda.core.common.Property;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Tests the direct interpretation of a string by VelocityTemplateEngine.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public class VelocityTemplateEngineTest
    extends TestCase
{
    public VelocityTemplateEngineTest(String arg0)
    {
        super(arg0);
    }

    public void testDirectVelocity()
        throws Exception
    {
        StringWriter writer = new StringWriter();

        VelocityEngine engine = new VelocityEngine();
        engine.init();

        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("test1", "@test1@");
        velocityContext.put("test2", "@test2@");

        assertTrue(engine.evaluate(velocityContext, writer, "mylogtag", "$test1$test2"));
        assertEquals(
            "@test1@@test2@",
            writer.getBuffer().toString());
    }
    
    /**
     * Tests the dynamic template merging.
     * 
     * @throws Exception
     */
    public void testTemplateMerging()
        throws Exception
    {
        final String packagePath = this.getClass().getPackage().getName().replace('.', '/');
        Property mergeMappings = new Property();
        URL mergeMappingsUri = VelocityTemplateEngineTest.class.getResource(
            "/" + packagePath + "/merge-mappings.xml");
        System.out.println("package path: " + packagePath + "/merge-mappings.xml");
        assertNotNull(mergeMappingsUri);
        mergeMappings.setName(NamespaceProperties.MERGE_MAPPINGS_URI);
        mergeMappings.setValue(mergeMappingsUri.toString());
        final String namespaceName = "test-namespace";
        final Namespace namespace = new Namespace();
        namespace.setName(namespaceName);
        namespace.addProperty(mergeMappings);
        Namespaces.instance().addNamespace(namespace);
        final VelocityTemplateEngine engine = new VelocityTemplateEngine();
        engine.init(namespaceName);
        StringWriter writer = new StringWriter();
        String path = packagePath + "/merge-test.vsl";
        
        
        final Map context = new HashMap();
        context.put("contextObject", "aValue");
        engine.processTemplate(path, context, writer);
        assertEquals("<test>merged value aValue</test>", writer.toString());
    }
}