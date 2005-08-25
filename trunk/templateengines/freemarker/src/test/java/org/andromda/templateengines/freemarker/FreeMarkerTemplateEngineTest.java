package org.andromda.templateengines.freemarker;

import java.io.StringReader;
import java.io.StringWriter;

import java.util.HashMap;

import freemarker.template.Configuration;
import freemarker.template.Template;

import junit.framework.TestCase;


/**
 * Tests the direct interpretation of a string by FreeMarkerTemplateEngine.
 */
public class FreeMarkerTemplateEngineTest
    extends TestCase
{
    public FreeMarkerTemplateEngineTest(String name)
    {
        super(name);
    }

    public void testDirectFreeMarker()
        throws Exception
    {
        StringWriter writer = new StringWriter();

        // - create the template
        Template template = new Template("strTemplate", new StringReader("${test1}${test2}"), new Configuration());

        HashMap templateObjects = new HashMap();

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