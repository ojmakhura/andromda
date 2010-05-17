package org.andromda.core.templateengine;

import java.io.Writer;

import java.util.List;
import java.util.Map;


/**
 * Just a mock template engine implementation
 * for testing purposes.
 *
 * @author Chad Brandon
 */
public class MockTemplateEngine
    implements TemplateEngine
{
    /**
     * @see org.andromda.core.templateengine.TemplateEngine#initialize(String)
     */
    public void initialize(String namespace)
        throws Exception
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#processTemplate(String, Map, Writer)
     */
    public void processTemplate(
        String templateFile,
        Map<String, Object> templateObjects,
        Writer output)
        throws Exception
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#shutdown()
     */
    public void shutdown()
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getMacroLibraries()
     */
    public List<String> getMacroLibraries()
    {
        return null;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#addMacroLibrary(String)
     */
    public void addMacroLibrary(String macroLibrary)
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#setMergeLocation(String)
     */
    public void setMergeLocation(String mergeLocation)
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#getEvaluatedExpression(String, Map)
     */
    public String getEvaluatedExpression(String expression, Map<String, Object> templateObjects)
    {
        return null;
    }
}