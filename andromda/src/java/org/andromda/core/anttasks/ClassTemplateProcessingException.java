package org.andromda.core.anttasks;

import java.lang.reflect.Method;


/**
 * <p>This exception is thrown when something goes wrong during the
 * procesing of a VelocityTemplateEngine template script.</p>
 * 
 * @author Matthias Bohlen
 */
public final class ClassTemplateProcessingException
    extends Exception
{

    /**
     * Constructor for ClassTemplateProcessingException.
     */
    public ClassTemplateProcessingException() {
        super();
    }

    /**
     * Constructor for ClassTemplateProcessingException.
     * @param message
     */
    public ClassTemplateProcessingException(String message) {
        super(message);
    }

    /**
     * Constructor for ClassTemplateProcessingException.
     * @param message
     * @param cause
     */
    public ClassTemplateProcessingException(String message, Throwable cause) {
        super(message + ": " + cause.getMessage());
        myInitCause(cause);
    }

    private void myInitCause(Throwable cause) {
        if (null != initCauseMethod) {
            try {
                initCauseMethod.invoke(this, new Object[] { cause });
            } catch (Exception ex) {
                // We're probably running in a pre-1.4 JRE
                // Ignore the exception
            }
        }
    }

    private static Method initCauseMethod = null;

    static {
        try {
            Class myClass = ClassTemplateProcessingException.class;
            initCauseMethod = myClass.getMethod("initCause", new Class[] { Throwable.class });
        } catch (Exception ex) {
            // We're probably running in a pre-1.4 JRE
            // Ignore the exception
        }
    }
}
