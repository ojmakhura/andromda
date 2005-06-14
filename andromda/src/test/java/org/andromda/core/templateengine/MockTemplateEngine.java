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
     * @see org.andromda.core.templateengine.TemplateEngine#initialize(java.lang.String)
     */
    public void initialize(String namespace)
        throws Exception
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#processTemplate(java.lang.String, java.util.Map, java.io.Writer)
     */
    public void processTemplate(
        String templateFile,
        Map templateObjects,
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
    public List getMacroLibraries()
    {
        return null;
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#addMacroLibrary(java.lang.String)
     */
    public void addMacroLibrary(String macroLibrary)
    {
    }

    /**
     * @see org.andromda.core.templateengine.TemplateEngine#setMergeLocation(java.lang.String)
     */
    public void setMergeLocation(String mergeLocation)
    {
    }

    /**

     * @see org.andromda.core.templateengine.TemplateEngine#getEvaluatedExpression(java.lang.String)
     */
    public String getEvaluatedExpression(String expression)
    {
        return null;
    }
}