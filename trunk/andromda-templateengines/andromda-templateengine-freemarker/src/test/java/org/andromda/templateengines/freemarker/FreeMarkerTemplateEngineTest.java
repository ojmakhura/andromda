package org.andromda.templateengines.freemarker;

import java.io.StringReader;
import java.io.StringWriter;

import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Tests the direct interpretation of a string by FreeMarkerTemplateEngine.
 */
public class FreeMarkerTemplateEngineTest    
{
    /**
     * @throws Exception
     */
    @Test
    public void testDirectFreeMarker()
        throws Exception
    {
        StringWriter writer = new StringWriter();

        // - create the template
        Template template = new Template("strTemplate", new StringReader("${test1}${test2}"), new Configuration());

        Map<String, Object> templateObjects = new HashMap<String, Object>();

        templateObjects.put(
            "test1",
            "@test1@");
        templateObjects.put(
            "test2",
            "@test2@");

        template.process(
            templateObjects,
            writer);
        assertEquals(
            "@test1@@test2@",
            writer.getBuffer().toString());
    }
}