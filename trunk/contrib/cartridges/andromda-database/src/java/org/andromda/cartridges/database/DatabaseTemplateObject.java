package org.andromda.cartridges.database;

import org.andromda.core.common.ClassUtils;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This template object helps with model to DDL transformation.
 * 
 * @author Chad Brandon
 */
public class DatabaseTemplateObject
{

    private static Logger logger = Logger
        .getLogger(DatabaseTemplateObject.class);

    private Short maxSqlNameLength = null;

    public DatabaseTemplateObject()
    {
        //initialize converters we're using since we don't want to default to 0
        ConvertUtils.register(new LongConverter(null), java.lang.Long.class);
        ConvertUtils.register(
            new IntegerConverter(null),
            java.lang.Integer.class);
        ConvertUtils.register(new ShortConverter(null), java.lang.Short.class);
    }

    /**
     * <p>
     * Trims the passed in value to the name maximum length (if one has been set
     * in the JdbcTypeMappings XML configuration.
     * </p>
     * If no maximum length has been set then this method does nothing.
     * 
     * @param name the name length to check and trim if necessary
     * @return String the string to be used as SQL type
     */
    public String ensureMaxNameLength(String name)
    {
        if (StringUtils.isNotEmpty(name) && this.getMaxSqlNameLength() != null)
        {
            int max = this.getMaxSqlNameLength().intValue();
            if (name.length() > max)
            {
                name = name.substring(0, max);
            }
        }
        return name;
    }

    /**
     * Sets the generated DDL name maximum length. This is so names don't get to
     * long, for example, Oracle names can't be longer than 30 characters so the
     * maxSqlNameLength would be set to 30.
     * 
     * @param maxSqlNameLength the maximum length an name can be.
     */
    public void setMaxSqlNameLength(String maxSqlNameLength)
    {
        if (maxSqlNameLength != null)
        {
            this.maxSqlNameLength = Short.valueOf(maxSqlNameLength);
        }
    }

    /**
     * Get the value of the current maximum length that a SQL name may be
     * 
     * @return Integer - the current maximum length that a SQL name can be.
     */
    private Short getMaxSqlNameLength()
    {
        return this.maxSqlNameLength;
    }

    /**
     * Instantiates an Object from the initial value of an attribute and returns
     * its value.
     * 
     * @param attribute the attribute to retrieve the initial value from.
     * @return Object the initial value.
     */
    public Object getInitialValue(AttributeFacade attribute)
    {
        Object initialValue = null;
        if (attribute != null)
        {
            final String methodName = "DatbaseTemplateObject.getInitialValue";
            String exp = attribute.getDefaultValue();
            if (StringUtils.isNotEmpty(exp))
            {
                ClassifierFacade type = attribute.getType();
                if (type != null)
                {
                    String typeString = attribute.getType()
                        .getFullyQualifiedName();
                    if (StringUtils.isNotEmpty(typeString))
                    {
                        try
                        {
                            Class typeClass = ClassUtils.loadClass(typeString);

                            initialValue = StringUtils.trimToEmpty(exp);
                            if (StringUtils.isNotEmpty(initialValue.toString()))
                            {
                                initialValue = ConvertUtils.convert(
                                    exp,
                                    typeClass);
                            }
                        }
                        catch (Exception ex)
                        {
                            String errMsg = "Error performing " + methodName;
                            logger.error(errMsg, ex);
                            throw new MetafacadeException(errMsg, ex);
                        }
                    }
                }
            }
        }
        return initialValue;
    }

}