package org.andromda.translation.testsuite;

/**
 * Represents a ExpressionText object loaded into an TranslatorTestConfig
 * object.
 * 
 * @author Chad Brandon
 */
public class ExpressionTest
{

    private String to;
    private String from;

    /**
     * Gets the from translation.
     * 
     * @return String
     */
    public String getFrom()
    {
        final String methodName = "ExpressionTest.getFrom";
        if (this.to == null)
        {
            throw new TranslationTestProcessorException(methodName
                + " - from can not be null");
        }
        return from;
    }

    /**
     * Set the from translation.
     * 
     * @param from the expression from which translation occurs.
     */
    public void setFrom(String from)
    {
        this.from = from;
    }

    /**
     * Gets the translation to which the translation should match.
     * 
     * @return String
     */
    public String getTo()
    {
        final String methodName = "ExpressionTest.getTo";
        if (this.to == null)
        {
            throw new TranslationTestProcessorException(methodName
                + " - to can not be null");
        }
        return to;
    }

    /**
     * Sets the translation to which the translation should match after the
     * translation of the 'from' property occurs.
     * 
     * @param to
     */
    public void setTo(String to)
    {
        this.to = to;
    }

}