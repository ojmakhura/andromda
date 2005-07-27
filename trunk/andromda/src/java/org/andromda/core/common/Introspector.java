package org.andromda.core.common;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;


/**
 * A simple class providing the ability to manipulate properties on java bean objects.
 *
 * @author Chad Brandon
 */
public final class Introspector
{
    /**
     * The shared instance.
     */
    private static Introspector instance = null;

    /**
     * Gets the shared instance.
     *
     * @return the shared introspector instance.
     */
    public static final Introspector instance()
    {
        if (instance == null)
        {
            instance = new Introspector();
        }
        return instance;
    }

    /**
     * <p> Indicates whether or not the given <code>object</code> contains a
     * valid property with the given <code>name</code> and <code>value</code>.
     * </p>
     * <p>
     * A valid property means the following:
     * <ul>
     * <li>It exists on the object</li>
     * <li>It is not null on the object</li>
     * <li>If its a boolean value, then it evaluates to <code>true</code></li>
     * <li>If value is not null, then the property matches the given </code>.value</code></li>
     * </ul>
     * All other possibilities return <code>false</code>
     * </p>
     *
     * @param object the object to test for the valid property.
     * @param name the name of the propery for which to test.
     * @param value the value to evaluate against.
     * @return true/false
     */
    public boolean containsValidProperty(
        final Object object,
        final String name,
        final String value)
    {
        boolean valid = false;

        try
        {
            final Object propertyValue = this.getProperty(
                    object,
                    name);
            valid = propertyValue != null;

            // if valid is still true, and the propertyValue
            // is not null
            if (valid)
            {
                // if it's a collection then we check to see if the
                // collection is not empty
                if (propertyValue instanceof Collection)
                {
                    valid = !((Collection)propertyValue).isEmpty();
                }
                else
                {
                    final String valueAsString = String.valueOf(propertyValue);
                    if (StringUtils.isNotEmpty(value))
                    {
                        valid = valueAsString.equals(value);
                    }
                    else if (propertyValue instanceof Boolean)
                    {
                        valid = Boolean.valueOf(valueAsString).booleanValue();
                    }
                }
            }
        }
        catch (final Throwable throwable)
        {
            valid = false;
        }
        return valid;
    }

    /**
     * Sets the property having the given <code>name</code> on the <code>object</code>
     * with the given <code>value</code>.
     *
     * @param object the object on which to set the property.
     * @param name the name of the property to populate.
     * @param value the value to give the property.
     */
    public void setProperty(
        final Object object,
        final String name,
        final Object value)
    {
        this.setNestedProperty(
            object,
            name,
            value);
    }

    /**
     * The delimiter used for seperating nested properties.
     */
    private static final char NESTED_DELIMITER = '.';

    /**
     * Attempts to set the nested property with the given
     * name of the given object.
     * @param object the object on which to populate the property.
     * @param name the name of the object.
     * @param value the value to populate.
     */
    private final void setNestedProperty(
        final Object object,
        String name,
        final Object value)
    {
        if (object != null && name != null && name.length() > 0)
        {
            final int dotIndex = name.indexOf(NESTED_DELIMITER);
            if (dotIndex >= name.length())
            {
                throw new IntrospectorException("Invalid property call --> '" + name + "'");
            }
            String[] names = name.split("\\" + NESTED_DELIMITER);
            Object objectToPopulate = object;
            for (int ctr = 0; ctr < names.length; ctr++)
            {
                name = names[ctr];
                if (ctr == names.length - 1)
                {
                    break;
                }
                objectToPopulate = this.internalGetProperty(
                        objectToPopulate,
                        name);
            }
            this.internalSetProperty(
                objectToPopulate,
                name,
                value);
        }
    }

    /**
     * Attempts to retrieve the property with the given <code>name</code> on the <code>object</code>.
     *
     * @param object the object to which the property belongs.
     * @param the name of the property
     * @return the value of the property.
     */
    public final Object getProperty(
        final Object object,
        final String name)
    {
        Object result = null;

        try
        {
            result = this.getNestedProperty(
                    object,
                    name);
        }
        catch (final IntrospectorException throwable)
        {
            // Dont catch our own exceptions.
            // Otherwise get Exception/Cause chain which
            // can hide the original exception.
            throw throwable;
        }
        catch (Throwable throwable)
        {
            throwable = ExceptionUtils.getRootCause(throwable);

            // If cause is an IntrospectorException re-throw that exception
            // rather than creating a new one.
            if (throwable instanceof IntrospectorException)
            {
                throw (IntrospectorException)throwable;
            }
            throw new IntrospectorException(throwable);
        }
        return result;
    }

    /**
     * Gets a nested property, that is it gets the properties
     * seperated by '.'.
     *
     * @param object the object from which to retrieve the nested property.
     * @param name the name of the property
     * @return the property value or null if one couldn't be retrieved.
     */
    private final Object getNestedProperty(
        final Object object,
        final String name)
    {
        Object property = null;
        if (object != null && name != null && name.length() > 0)
        {
            int dotIndex = name.indexOf(NESTED_DELIMITER);
            if (dotIndex == -1)
            {
                property = this.internalGetProperty(
                        object,
                        name);
            }
            else
            {
                if (dotIndex >= name.length())
                {
                    throw new IntrospectorException("Invalid property call --> '" + name + "'");
                }
                final Object nextInstance = internalGetProperty(
                        object,
                        name.substring(
                            0,
                            dotIndex));
                property = getNestedProperty(
                        nextInstance,
                        name.substring(dotIndex + 1));
            }
        }
        return property;
    }

    /**
     * Cache for a class's write methods.
     */
    private final Map writeMethodsCache = new HashMap();

    /**
     * Gets the writable method for the property.
     *
     * @param object the object from which to retrieve the property method.
     * @param name the name of the property.
     * @return the property method or null if one wasn't found.
     */
    private final Method getWriteMethod(
        final Object object,
        final String name)
    {
        Method writeMethod = null;
        final Class objectClass = object.getClass();
        Map classWriteMethods = (Map)this.writeMethodsCache.get(objectClass);
        if (classWriteMethods == null)
        {
            classWriteMethods = new HashMap();
        }
        else
        {
            writeMethod = (Method)classWriteMethods.get(name);
        }
        if (writeMethod == null)
        {
            final PropertyDescriptor descriptor = this.getPropertyDescriptor(
                    object.getClass(),
                    name);
            writeMethod = descriptor != null ? descriptor.getWriteMethod() : null;
            if (writeMethod != null)
            {
                classWriteMethods.put(
                    name,
                    writeMethod);
                this.writeMethodsCache.put(
                    objectClass,
                    classWriteMethods);
            }
        }
        return writeMethod;
    }

    /**
     * Indicates if the <code>object</code> has a property that
     * is <em>readable</em> with the given <code>name</code>.
     *
     * @param object the object to check.
     * @param name the property to check for.
     */
    public boolean isReadable(
        final Object object,
        final String name)
    {
        return this.getReadMethod(
            object,
            name) != null;
    }

    /**
     * Indicates if the <code>object</code> has a property that
     * is <em>writable</em> with the given <code>name</code>.
     *
     * @param object the object to check.
     * @param name the property to check for.
     */
    public boolean isWritable(
        final Object object,
        final String name)
    {
        return this.getWriteMethod(
            object,
            name) != null;
    }

    /**
     * Cache for a class's read methods.
     */
    private final Map readMethodsCache = new HashMap();

    /**
     * Gets the readable method for the property.
     *
     * @param object the object from which to retrieve the property method.
     * @param name the name of the property.
     * @return the property method or null if one wasn't found.
     */
    private final Method getReadMethod(
        final Object object,
        final String name)
    {
        Method readMethod = null;
        final Class objectClass = object.getClass();
        Map classWriteMethods = (Map)this.readMethodsCache.get(objectClass);
        if (classWriteMethods == null)
        {
            classWriteMethods = new HashMap();
        }
        else
        {
            readMethod = (Method)classWriteMethods.get(name);
        }
        if (readMethod == null)
        {
            final PropertyDescriptor descriptor = this.getPropertyDescriptor(
                    object.getClass(),
                    name);
            readMethod = descriptor != null ? descriptor.getReadMethod() : null;
            if (readMethod != null)
            {
                classWriteMethods.put(
                    name,
                    readMethod);
                this.readMethodsCache.put(
                    objectClass,
                    classWriteMethods);
            }
        }
        return readMethod;
    }

    /**
     * The cache of property descriptors.
     */
    private final Map propertyDescriptorsCache = new HashMap();

    /**
     * Retrives the property descriptor for the given type and name of
     * the property.
     *
     * @param type the Class of which we'll attempt to retrieve the property
     * @param name the name of the property.
     * @return the found property descriptor
     */
    private final PropertyDescriptor getPropertyDescriptor(
        final Class type,
        final String name)
    {
        PropertyDescriptor propertyDescriptor = null;
        Map classPropertyDescriptors = (Map)this.propertyDescriptorsCache.get(type);
        if (classPropertyDescriptors == null)
        {
            classPropertyDescriptors = new HashMap();
        }
        else
        {
            propertyDescriptor = (PropertyDescriptor)classPropertyDescriptors.get(name);
        }
        if (propertyDescriptor == null)
        {
            try
            {
                final PropertyDescriptor[] descriptors =
                    java.beans.Introspector.getBeanInfo(type).getPropertyDescriptors();
                final int descriptorNumber = descriptors.length;
                for (int ctr = 0; ctr < descriptorNumber; ctr++)
                {
                    final PropertyDescriptor descriptor = descriptors[ctr];
                    if (descriptor.getName().equals(name))
                    {
                        propertyDescriptor = descriptor;
                        break;
                    }
                }
                if (propertyDescriptor == null && name.indexOf(NESTED_DELIMITER) != -1)
                {
                    int dotIndex = name.indexOf(NESTED_DELIMITER);
                    if (dotIndex >= name.length())
                    {
                        throw new IntrospectorException("Invalid property call --> '" + name + "'");
                    }
                    final PropertyDescriptor nextInstance =
                        this.getPropertyDescriptor(
                            type,
                            name.substring(
                                0,
                                dotIndex));
                    propertyDescriptor =
                        this.getPropertyDescriptor(
                            nextInstance.getPropertyType(),
                            name.substring(dotIndex + 1));
                }
            }
            catch (final java.beans.IntrospectionException exception)
            {
                throw new IntrospectorException(exception);
            }
            classPropertyDescriptors.put(
                name,
                propertyDescriptor);
            this.propertyDescriptorsCache.put(
                type,
                classPropertyDescriptors);
        }
        return propertyDescriptor;
    }

    /**
     * Prevents stack-over-flows by storing the objects that
     * are currently being evaluted within {@link #internalGetProperty(Object, String)}.
     */
    private final Map evaluatingObjects = new HashMap();

    /**
     * Attempts to get the value of the property with <code>name</code> on the
     * given <code>object</code> (throws an exception if the property
     * is not readable on the object).
     *
     * @param object the object from which to retrieve the property.
     * @param name the name of the property
     * @return the resulting property value
     * @throws Exception if an error occurs during introspection.
     */
    private final Object internalGetProperty(
        final Object object,
        final String name)
    {
        Object property = null;

        // - prevent stack-over-flows by checking to make sure
        //   we aren't entering any circular evalutions
        final Object value = this.evaluatingObjects.get(object);
        if (value == null || !value.equals(name))
        {
            this.evaluatingObjects.put(
                object,
                name);
            if (object != null || name != null || name.length() > 0)
            {
                final Method method = this.getReadMethod(
                        object,
                        name);
                if (method == null)
                {
                    throw new IntrospectorException("No readable property named '" + name + "', exists on object '" +
                        object + "'");
                }
                try
                {
                    property = method.invoke(
                            object,
                            (Object[])null);
                }
                catch (final Throwable throwable)
                {
                    throw new IntrospectorException(throwable);
                }
            }
            this.evaluatingObjects.remove(object);
        }
        return property;
    }

    /**
     * Attempts to sets the value of the property with <code>name</code> on the
     * given <code>object</code> (throws an exception if the property
     * is not writable on the object).
     *
     * @param object the object from which to retrieve the property.
     * @param name the name of the property to set.
     * @param value the value of the property to set.
     * @throws Exception if an error occurs during introspection.
     */
    private final void internalSetProperty(
        final Object object,
        final String name,
        Object value)
    {
        if (object != null || name != null || name.length() > 0)
        {
            Class expectedType = null;
            if (value != null)
            {
                final PropertyDescriptor descriptor = this.getPropertyDescriptor(
                        object.getClass(),
                        name);
                if (descriptor != null)
                {
                    expectedType = this.getPropertyDescriptor(
                            object.getClass(),
                            name).getPropertyType();
                    value = this.convert(
                            value,
                            expectedType);
                }
            }
            final Method method = this.getWriteMethod(
                    object,
                    name);
            if (method == null)
            {
                throw new IntrospectorException("No writeable property named '" + name + "', exists on object '" +
                    object + "'");
            }
            try
            {
                method.invoke(
                    object,
                    new Object[] {value});
            }
            catch (final Throwable throwable)
            {
                throw new IntrospectorException(throwable);
            }
        }
    }

    /**
     * The prefix of the 'valueOf' method available on wrapper classes.
     */
    private static final String VALUE_OF_METHOD_NAME = "valueOf";

    /**
     * Attempts to convert the <code>object</code> to the <code>expectedType</code>.
     *
     * @param object the object to convert.
     * @param expectedType the type to which it should be converted.
     * @return the converted object
     */
    private final Object convert(
        Object object,
        Class expectedType)
    {
        try
        {
            if (expectedType == String.class)
            {
                object = object.toString();
            }
            else
            {
                if (expectedType.isPrimitive())
                {
                    expectedType = (Class)primitiveWrappers.get(expectedType);
                }
                Method valueOfMethod = null;
                try
                {
                    valueOfMethod =
                        expectedType.getDeclaredMethod(
                            VALUE_OF_METHOD_NAME,
                            new Class[] {object.getClass()});
                }
                catch (final NoSuchMethodException exception)
                {
                    throw new IntrospectorException("Could not convert '" + object + "' to type '" +
                        expectedType.getName() + "'");
                }
                object = valueOfMethod.invoke(
                        expectedType,
                        new Object[] {object});
            }
        }
        catch (final Throwable throwable)
        {
            throw new IntrospectorException(throwable);
        }
        return object;
    }

    /**
     * Shuts this instance down and reclaims
     * any resouces used by this instance.
     */
    public void shutdown()
    {
        this.propertyDescriptorsCache.clear();
        this.writeMethodsCache.clear();
        this.readMethodsCache.clear();
        this.evaluatingObjects.clear();
        instance = null;
    }

    /**
     * Stores each primitive and its associated wrapper class.
     */
    private static final Map primitiveWrappers = new HashMap();

    /**
     * Initialize the primitiveWrappers.
     */
    static
    {
        primitiveWrappers.put(
            boolean.class,
            Boolean.class);
        primitiveWrappers.put(
            int.class,
            Integer.class);
        primitiveWrappers.put(
            long.class,
            Long.class);
        primitiveWrappers.put(
            short.class,
            Short.class);
        primitiveWrappers.put(
            byte.class,
            Byte.class);
        primitiveWrappers.put(
            float.class,
            Float.class);
        primitiveWrappers.put(
            double.class,
            Double.class);
        primitiveWrappers.put(
            char.class,
            Character.class);
    }
}