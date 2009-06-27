package org.andromda.maven.plugin.andromdapp.script;

/**
 * An exception thrown when an unexpected exception occurs during
 * execution of the ScriptClassGenerator.
 * 
 * @author Chad Brandon
 */
public class ScriptClassGeneratorException
    extends RuntimeException
{
    /**
     * Constructor for ScriptClassGeneratorException.
     *
     * @param message the exception message.
     * @param parent the parent exception.
     */
    public ScriptClassGeneratorException(
        String message,
        Throwable parent)
    {
        super(message, parent);
    }

    /**
     * Constructor for ScriptClassGeneratorException.
     *
     * @param message the exception message
     */
    public ScriptClassGeneratorException(String message)
    {
        super(message);
    }
    
    /**
     * Constructor for ScriptClassGeneratorException.
     *
     * @param parent the parent exception
     */
    public ScriptClassGeneratorException(Throwable parent)
    {
        super(parent);
    }
}
