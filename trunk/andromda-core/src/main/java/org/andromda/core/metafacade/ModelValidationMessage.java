package org.andromda.core.metafacade;

import java.io.Serializable;

import java.util.List;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Stores the validation messages that are collected during model validation.
 *
 * @author Chad Brandon
 */
public class ModelValidationMessage
    implements Serializable
{
    /**
     * Constructs a new instance of MetafacadeValidationMessage taking a
     * <code>metafacade</code> instance and a <code>message</code>
     * indicating what has been violated.
     * 
     * @param metafacade the metafacade being validated.
     * @param message the message to to communitate about the validation.
     */
    public ModelValidationMessage(
        final MetafacadeBase metafacade,
        final String message)
    {
        this(metafacade, null, message);
    }
    
    /**
     * Constructs a new instance of MetafacadeValidationMessage taking a
     * <code>metafacade</code> instance the <code>name</code> of the
     * validation constraint and the actual <code>message</code> text indicating
     * what has been violated.
     * 
     * @param metafacade the metafacade being validated.
     * @param name the name of the model element being validated.
     * @param message the message to to communitate about the validation.
     */
    public ModelValidationMessage(
        final MetafacadeBase metafacade,
        final String name,
        final String message)
    {
        ExceptionUtils.checkNull("metafacade", metafacade);
        ExceptionUtils.checkEmpty("message", message);
        this.metafacade = metafacade;
        this.name = name;
        this.message = message;
    }
    
    /**
     * Stores the actual name of the constraint (if there is one).
     */
    private String name;
    
    /**
     * Gets the name of the validation constraint.
     * 
     * @return the constraint name.
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Stores the actual message text.
     */
    private String message;

    /**
     * Gets the actual message text.
     * 
     * @return Returns the message.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Stores the actual metafacade to which this validation message applies.
     */
    private MetafacadeBase metafacade;

    /**
     * Stores the metafacade name which is only constructed the very first time.
     */
    private String metafacadeName = null;

    /**
     * Gets the name of the metafacade to which this validation message applies.
     *
     * @return Returns the metafacade.
     */
    public String getMetafacadeName()
    {
        if (this.metafacadeName == null)
        {
            final String seperator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
            final StringBuffer name = new StringBuffer();
            for (
                MetafacadeBase metafacade = this.metafacade; metafacade != null;
                metafacade = (MetafacadeBase)metafacade.getValidationOwner())
            {
                if (StringUtils.isNotBlank(metafacade.getValidationName()))
                {
                    String validationName = metafacade.getValidationName();
                    if (metafacade.getValidationOwner() != null)
                    {
                        // remove package if we have an owner
                        validationName = validationName.replaceAll(".*" + seperator, "");
                    }
                    if (name.length()>0)
                    {
                        name.insert(0, seperator);
                    }
                    name.insert(0, validationName);
                }
            }
            this.metafacadeName = name.toString();
        }
        return metafacadeName;
    }

    /**
     * Stores the metafacade class displayed within the message, this is only retrieved the very first time.
     */
    private Class metafacadeClass = null;

    /**
     * Gets the class of the metafacade to which this validation message applies.
     *
     * @return the metafacade Class.
     */
    public Class getMetafacadeClass()
    {
        if (metafacadeClass == null)
        {
            this.metafacadeClass = this.metafacade.getClass();
            final List interfaces = ClassUtils.getAllInterfaces(this.metafacade.getClass());
            if (interfaces != null && !interfaces.isEmpty())
            {
                this.metafacadeClass = (Class)interfaces.iterator().next();
            }
        }
        return this.metafacadeClass;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        final StringBuffer toString = new StringBuffer();
        toString.append('[');
        toString.append(this.getMetafacadeName());
        toString.append(']');
        toString.append(':');
        toString.append(this.message);
        return toString.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return this.toString().hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object)
    {
        boolean equals = object != null && ModelValidationMessage.class == object.getClass();
        if (equals)
        {
            final ModelValidationMessage message = (ModelValidationMessage)object;
            equals = message.toString().equals(this.toString());
        }
        return equals;
    }
}