package org.andromda.core.metafacade;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Stores the validation messages that are stored up to be output at the end
 * model processing.
 * 
 * @author Chad Brandon
 */
public class ModelValidationMessage
{
    /**
     * Constructs a new instance of MetafacadeValidationMessage taking the
     * <code>metafacadeClass</code> the <code>modelElementName</code> and
     * <code>message</code>.
     * 
     * @param metafacadeClass the Class of the metafacade being validated.
     * @param modelElementName the name of the model element being validated.
     * @param message the message to to communitate about the validation.
     */
    public ModelValidationMessage(
        Class metafacadeClass,
        String modelElementName,
        String message)
    {
        final String constructorName = "MetafacadeValidationMessage";
        ExceptionUtils.checkNull(
            constructorName,
            "metafacadeClass",
            metafacadeClass);
        ExceptionUtils.checkEmpty(constructorName, "message", message);
        this.metafacadeClass = metafacadeClass;
        this.modelElementName = modelElementName;
        this.message = message;
    }

    private Class metafacadeClass;
    private String modelElementName;
    private String message;

    /**
     * @return Returns the message.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @return Returns the metafacadeClass.
     */
    public Class getMetafacadeClass()
    {
        return metafacadeClass;
    }

    /**
     * @return Returns the modelElementName.
     */
    public String getModelElementName()
    {
        return modelElementName;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer toString = new StringBuffer(metafacadeClass.getName());
        toString.append("[");
        toString.append(modelElementName);
        toString.append("]");
        toString.append(":");
        toString.append(this.message);
        return toString.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return StringUtils.trimToEmpty(this.message).hashCode()
            + StringUtils.trimToEmpty(this.getModelElementName()).hashCode()
            + this.getMetafacadeClass().hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object)
    {
        boolean equals = object != null
            && ModelValidationMessage.class == object.getClass();
        if (equals)
        {
            ModelValidationMessage message = (ModelValidationMessage)object;
            equals = message.getMetafacadeClass() == this.getMetafacadeClass();
            if (equals)
            {
                equals = StringUtils
                    .trimToEmpty(message.getModelElementName())
                    .equals(StringUtils.trimToEmpty(this.getModelElementName()));
                if (equals)
                {
                    equals = StringUtils.trimToEmpty(message.getMessage())
                        .equals(StringUtils.trimToEmpty(this.getMessage()));
                }
            }
        }
        return equals;
    }
}