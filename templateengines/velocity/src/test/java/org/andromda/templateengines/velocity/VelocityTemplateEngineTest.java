package org.andromda.templateengines.velocity;

import org.andromda.core.configuration.Namespace;
import org.andromda.core.configuration.NamespaceProperties;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.configuration.Property;
import org.andromda.core.templateengine.TemplateEngineException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Tests the direct interpretation of a string by VelocityTemplateEngine.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 * @author Michail Plushnikov
 */
public class VelocityTemplateEngineTest
{
    @Test
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
     * Creates an instance of templatengine
     * @return instance of tempalte engine
     * @throws Exception
     */
    private VelocityTemplateEngine createEngine() throws Exception
    {
        Property mergeMappings = new Property();
        URL mergeMappingsUri = VelocityTemplateEngineTest.class.getResource("/merge-mappings.xml");
        assertNotNull(mergeMappingsUri);
        mergeMappings.setName(NamespaceProperties.MERGE_MAPPINGS_URI);
        mergeMappings.setValue(mergeMappingsUri.toString());

        final String namespaceName = "test-namespace";
        final Namespace namespace = new Namespace();
        namespace.setName(namespaceName);
        namespace.addProperty(mergeMappings);
        Namespaces.instance().addNamespace(namespace);
        final VelocityTemplateEngine engine = new VelocityTemplateEngine();
        engine.initialize(namespaceName);
        return engine;
    }

    /**
     * Tests the dynamic template merging.
     *
     * @throws Exception
     */
    @Test
    public void testTemplateMerging()
            throws Exception
    {
        StringWriter writer = new StringWriter();
        String path = "merge-test.vsl";

        final Map<String, Object> context = new HashMap<String, Object>();
        context.put("contextObject", "aValue");

        final VelocityTemplateEngine engine = createEngine();
        engine.processTemplate(path, context, writer);
        assertEquals("<test>merged value aValue</test>", writer.toString());
    }

    @Test
    public void testVelocityEscapeToolHash()
            throws Exception
    {
        final VelocityTemplateEngine engine = createEngine();
        assertEquals("Test#Test", engine.getEvaluatedExpression("Test${esc.hash}Test", null));
    }

    @Test
    public void testVelocityEscapeToolJavadoc()
            throws Exception
    {
        final VelocityTemplateEngine engine = createEngine();
        assertEquals("@see package.name.Clasname#methodname(package.name.class var)",
                engine.getEvaluatedExpression("@see package.name.Clasname${esc.hash}methodname(package.name.class var)",
                        null));
    }

    @Test(expected= TemplateEngineException.class)
    public void testVelocityNonEscapeToolJavadoc()
            throws Exception
    {
        final VelocityTemplateEngine engine = createEngine();
        assertEquals("@see package.name.Clasname#methodname(package.name.class var)",
                engine.getEvaluatedExpression("@see package.name.Clasname#methodname(package.name.class var)", null));
    }

    @Test
    public void testVelocityEscapeToolDollar()
            throws Exception
    {
        final VelocityTemplateEngine engine = createEngine();
        assertEquals("Test$Test", engine.getEvaluatedExpression("Test${esc.dollar}Test", null));
    }
}