package org.andromda.cartridges.support.webservice.client;

import java.lang.reflect.Method;

import org.apache.axis2.databinding.typemapping.SimpleTypeMapper;

/**
 * The default {@link TypeMapper} implementation.
 *
 * @author Chad Brandon
 */
public class DefaultTypeMapper
    implements TypeMapper
{

    public Object getObject(Class type)
    {
        Object object = null;
        if (type != null)
        {
            try
            {
                object = type.newInstance();
            }
            catch (Exception exception)
            {
                throw new TypeMapperException(exception);
            }
        }
        return object;
    }


    private static final String VALUE_OF = "valueOf";

    @SuppressWarnings("unchecked")
    public Object getObject(Class type, String value)
    {
        Object object = null;
        if (type != null && value != null)
        {
            try
            {
                if (type.isEnum())
                {
                    object = type.getMethod(
                        VALUE_OF, new Class[]{java.lang.String.class}).invoke(
                            type, value);
                }
                else
                {
                    final Method fromMethod = getEnumerationFromMethod(type);
                    if (fromMethod != null)
                    {
                        object = fromMethod.invoke(type, new Object[]{value});
                    }
                }
            }
            catch (Exception exception)
            {
                throw new TypeMapperException(exception);
            }
        }
        return object;
    }

    public String getStringValue(Object object)
    {
        return SimpleTypeMapper.getStringValue(object);
    }

    public boolean isSimpleType(Class type)
    {
        return java.util.Calendar.class.isAssignableFrom(type) ||
            java.util.Date.class.isAssignableFrom(type) ||
            SimpleTypeMapper.isSimpleType(type) ||
            isEnumeration(type);
    }

    /**
     * Indicates whether or not the given type represents an enumeration.
     *
     * @param type the type to check.
     * @return true/false
     */
    private boolean isEnumeration(final Class type)
    {
        return isEnumeration(type, getEnumerationFromMethod(type));
    }

    /**
     * Indicates whether or not the given type represents an enumeration by checking
     * whether the type is an actual "enum" class or the "fromMethod" is not null.
     *
     * @param type the type to check.
     * @param the "from" method used to construct a typesafe enumeration from it's simple type.
     * @return true/false
     */
    private boolean isEnumeration(final Class type, final Method fromMethod)
    {
        boolean enumeration = false;
        if (type != null)
        {
            enumeration = type.isEnum();
            if (!enumeration)
            {
                enumeration = fromMethod != null;
            }
        }
        return enumeration;
    }

    private static final String FROM = "from";

    /**
     * Gets the "from" method for a type safe enumeration.
     *
     * @param type the type.
     * @return the "from" method (i.e. fromString, etc).
     */
    private Method getEnumerationFromMethod(final Class type)
    {
        Method fromMethod = null;
        if (type != null)
        {
            // - check for the typesafe enum pattern
            for (final Method method : type.getMethods())
            {
                if (method.getName().startsWith(FROM))
                {
                    final Class[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1)
                    {
                        final Class parameterType = parameterTypes[0];
                        if (method.getName().equals(FROM + parameterType.getSimpleName()))
                        {
                            fromMethod = method;
                            break;
                        }
                    }
                }
            }
        }
        return fromMethod;
    }

}
