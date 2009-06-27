package org.andromda.cartridges.support.webservice.client;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.schema.Schema;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.Base64;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utilities for Axis2 clients.
 *
 * @author Chad Brandon
 */
public class Axis2ClientUtils
{
    /**
     * Gets the appropriate OM element for the given method
     * @param definition the WSDL definition.
     * @param method the method corresponding to the OMElement to create.
     * @param arguments the arguments to pass to the method.
     * @param typeMapper the {@link TypeMapper} instance to use for converting types.
     * @return the constructed OMElement
     */
    public static OMElement getOperationOMElement(
        final Definition definition,
        final Method method,
        final Object[] arguments,
        final TypeMapper typeMapper)
    {
        final Class serviceClass = method.getDeclaringClass();
        OMElement operationOMElement = null;
        try
        {
            final String operationName = method.getName();
            final String serviceName = serviceClass.getSimpleName();
            final Element operationElement = getElementByAttribute(
                    definition,
                    NAME,
                    operationName);
            final Schema operationSchema = Axis2ClientUtils.getElementSchemaByAttribute(
                    definition,
                    NAME,
                    operationName);
            if (operationSchema != null)
            {
                final OMFactory factory = OMAbstractFactory.getOMFactory();

                // - collect all the namespaces
                final Map<String, OMNamespace> namespaces = new HashMap<String, OMNamespace>();
                final OMNamespace xsiNamespace = factory.createOMNamespace(XSI_NS, XSI_PREFIX);
                namespaces.put(XSI_PREFIX, xsiNamespace);

                final Collection<Schema> schemas = getSchemas(definition);
                for (final Schema schema : schemas)
                {
                    final String namespace = Axis2ClientUtils.getTargetNamespace(schema);
                    final String prefix = getNamespacePrefix(
                            definition,
                            namespace);
                    namespaces.put(prefix, factory.createOMNamespace(namespace, prefix));
                }

                operationOMElement =
                    getOMElement(
                        definition,
                        operationSchema,
                        null,
                        null,
                        operationName,
                        factory,
                        namespaces,
                        typeMapper);
                if (operationElement == null)
                {
                    throw new RuntimeException("No operation with name '" + operationName + "' can be found on service: " +
                        serviceName);
                }
                final List argumentElements = Axis2ClientUtils.getElementsWithAttribute(
                        operationElement,
                        TYPE);
                final Class[] parameterTypes = method.getParameterTypes();
                if (argumentElements.size() != arguments.length)
                {
                    throw new RuntimeException("Operation: " + operationName + " takes " + parameterTypes.length +
                        " argument(s), and 'arguments' only contains: " + arguments.length);
                }

                // - declare all the namespaces
                operationOMElement.declareNamespace(xsiNamespace);
                for (final OMNamespace namespace : namespaces.values())
                {
                    operationOMElement.declareNamespace(namespace);
                }

                // - add the argument children
                for (int ctr = 0; ctr < argumentElements.size(); ctr++)
                {
                    final Element argument = (Element)argumentElements.get(ctr);
                    final String argumentName = getAttributeValue(
                            argument,
                            NAME);
                    final OMElement element = getOMElement(
                            definition,
                            operationSchema,
                            argument,
                            arguments[ctr],
                            argumentName,
                            factory,
                            namespaces,
                            typeMapper);
                    operationOMElement.addChild(element);
                }
            }
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception);
        }
        return operationOMElement;
    }

    /**
     * Constructs and OMElement from the given bean
     *
     * @param definition the WSDL definition
     * @param schema the current schema from which to retrieve the om element.
     * @param componentElement, the current componentElemnet of the WSDL definition.
     * @param bean the bean to introspect
     * @param elementName the name of the element to construct.
     * @param the factory used for element construction.
     * @param namespaces all available namespaces.
     * @param typeMapper the {@link TypeMapper} instance to use for converting types.
     */
    public static OMElement getOMElement(
        final Definition definition,
        final Schema schema,
        final Element componentElement,
        final Object bean,
        final String elementName,
        final OMFactory factory,
        final Map<String, OMNamespace> namespaces,
        final TypeMapper typeMapper)
    {
        return getOMElement(
            definition,
            schema,
            componentElement,
            bean,
            elementName,
            factory,
            namespaces,
            typeMapper,
            new ArrayList<Object>());
    }

    /**
     * Constructs and OMElement from the given bean
     *
     * @param definition the WSDL definition
     * @param schema the current schema from which to retrieve the om element.
     * @param componentElement, the current componentElemnet of the WSDL definition.
     * @param bean the bean to introspect
     * @param elementName the name of the element
     * @param factory the OM factory instance used to create the element.
     * @param namespaces all available namespaces.
     * @param typeMapper the {@link TypeMapper} instance to use for converting types.
     * @param evaluatingBeans the collection in which to keep the beans that are evaluating in order
     *        to prevent endless recursion.
     */
    private static OMElement getOMElement(
        final Definition definition,
        Schema schema,
        final Element componentElement,
        final Object bean,
        final String elementName,
        final OMFactory factory,
        final Map<String, OMNamespace> namespaces,
        final TypeMapper typeMapper,
        final Collection<Object> evaluatingBeans)
    {
        final String componentElementName = componentElement != null ? componentElement.getAttribute(NAME) : null;
        if (StringUtils.isNotBlank(componentElementName))
        {
            final Schema currentSchema = Axis2ClientUtils.getElementSchemaByAttribute(
                    definition,
                    NAME,
                    componentElementName);
            if (currentSchema != null)
            {
                schema = currentSchema;
            }
        }

        OMNamespace omNamespace = null;
        if (isQualified(schema))
        {
            final String namespace = Axis2ClientUtils.getTargetNamespace(schema);
            final String namespacePrefix = getNamespacePrefix(
                    definition,
                    namespace);
            omNamespace = namespaces.get(namespacePrefix);
        }

        final OMElement omElement =
            factory.createOMElement(
                elementName,
                omNamespace);
        if (bean != null && evaluatingBeans != null && !evaluatingBeans.contains(bean))
        {
            evaluatingBeans.add(bean);
            if (isSimpleType(bean, typeMapper))
            {
                omElement.addChild(factory.createOMText(typeMapper.getStringValue(bean)));
            }
            else if (bean instanceof byte[])
            {
                omElement.addChild(factory.createOMText(Base64.encode((byte[])bean)));
            }
            else
            {
                final Element currentComponentElement =
                    Axis2ClientUtils.getElementByAttribute(
                        definition,
                        NAME,
                        bean.getClass().getSimpleName());
                final Class beanType = bean.getClass();
                if (beanType.isArray())
                {
                    final Element arrayElement = Axis2ClientUtils.getRequiredElementByAttribute(
                            definition,
                            componentElement,
                            NAME,
                            elementName);
                    final Element arrayTypeElement =
                        Axis2ClientUtils.getElementByAttribute(
                            definition,
                            NAME,
                            stripPrefix(arrayElement.getAttribute(TYPE)));
                    final String arrayComponentName = Axis2ClientUtils.getAttributeValueFromChildElement(
                            arrayTypeElement,
                            NAME,
                            0);
                    for (int ctr = 0; ctr < Array.getLength(bean); ctr++)
                    {
                        omElement.addChild(
                            getOMElement(
                                definition,
                                schema,
                                currentComponentElement,
                                Array.get(
                                    bean,
                                    ctr),
                                arrayComponentName,
                                factory,
                                namespaces,
                                typeMapper,
                                evaluatingBeans));
                    }
                }
                else
                {


                    final String attributeValue = omNamespace != null ?
                        omNamespace.getPrefix() + NS_SEPARATOR + beanType.getSimpleName() : beanType.getSimpleName();
                    // - add the xsi:type attribute for complex types
                    omElement.addAttribute(TYPE, attributeValue, namespaces.get(XSI_PREFIX));
                }
                try
                {
                    final java.util.Map properties = PropertyUtils.describe(bean);
                    for (final Iterator iterator = properties.keySet().iterator(); iterator.hasNext();)
                    {
                        final String name = (String)iterator.next();
                        if (!CLASS.equals(name))
                        {
                            final Object value = properties.get(name);
                            if (value != null)
                            {
                                omElement.addChild(
                                    getOMElement(
                                        definition,
                                        schema,
                                        currentComponentElement,
                                        value,
                                        name,
                                        factory,
                                        namespaces,
                                        typeMapper,
                                        evaluatingBeans));
                            }
                        }
                    }
                }
                catch (final Throwable throwable)
                {
                    throw new RuntimeException(throwable);
                }
            }
            evaluatingBeans.remove(bean);
        }
        return omElement;
    }

    private static final String ELEMENT_FORM_DEFAULT = "elementFormDefault";

    /**
     * Indicates whether or not a xml document is qualified.
     */
    private static final String QUALIFIED = "qualified";

    /**
     * The attribute that stores the target namespace.
     */
    private static final String TARGET_NAMESPACE = "targetNamespace";

    /**
     * The schema instance namespace.
     */
    private static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

    /**
     * The prefix for the schema instance namespace.
     */
    private static final String XSI_PREFIX = "xsi";

    /**
     * Used to seperate a namespace prefix and name in a QName.
     */
    private static final String NS_SEPARATOR = ":";

    /**
     * Indicates whether or not the schema is qualified.
     *
     * @param schema the schema to check.
     * @return true/false
     */
    private static boolean isQualified(Schema schema)
    {
        boolean isQualified = false;
        if (schema != null)
        {
            final String qualified = Axis2ClientUtils.getAttributeValue(
                schema.getElement(),
                ELEMENT_FORM_DEFAULT);
            isQualified = QUALIFIED.equalsIgnoreCase(qualified);
        }
        return isQualified;
    }

    private static String getTargetNamespace(Schema schema)
    {
        return Axis2ClientUtils.getAttributeValue(
            schema.getElement(),
            TARGET_NAMESPACE);
    }

    private static String stripPrefix(String name)
    {
        return name.replaceAll(
            ".*:",
            "");
    }

    private static String getNamespacePrefix(
        final Definition definition,
        final String namespace)
    {
        String prefix = null;
        final Map namespaces = definition.getNamespaces();
        for (final Iterator iterator = namespaces.entrySet().iterator(); iterator.hasNext();)
        {
            final Map.Entry entry = (Map.Entry)iterator.next();
            if (entry.getValue().equals(namespace))
            {
                prefix = (String)entry.getKey();
            }
        }
        return prefix;
    }

    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String BASE = "base";

    private static Element getRequiredElementByAttribute(
        final Definition definition,
        final Element container,
        final String attribute,
        final String value)
    {
        Element element = null;
        if (container != null)
        {
            element = Axis2ClientUtils.getElementByAttribute(
                container,
                attribute,
                value);
        }
        // - try finding the element on an inherited type
        if (element == null)
        {
            final String xsiTypeName = Axis2ClientUtils.getAttributeValueFromChildElement(container, BASE, 0);
            final String typeName = getLocalName(xsiTypeName);
            element = Axis2ClientUtils.getElementByAttribute(
                Axis2ClientUtils.getElementByAttribute(definition, NAME, typeName),
                attribute,
                value);
        }
        if (element == null)
        {
            throw new RuntimeException("'" + value + "' was not found on element '" + container.getAttribute(NAME) + "'");
        }
        return element;
    }

    private static Element getElementByAttribute(
        final Definition definition,
        final String attribute,
        final String value)
    {
        Element element = null;
        if (value != null)
        {
            final Types types = definition.getTypes();
            final Collection elements = types.getExtensibilityElements();
            for (final Iterator iterator = elements.iterator(); iterator.hasNext();)
            {
                final Object object = iterator.next();
                if (object instanceof Schema)
                {
                    final Schema schema = (Schema)object;
                    element =
                        getElementByAttribute(
                            schema.getElement(),
                            attribute,
                            value);
                    if (element != null)
                    {
                        break;
                    }
                }
            }
        }
        return element;
    }

    private static Collection<Schema> getSchemas(final Definition definition)
    {
        final Collection<Schema> schemas = new ArrayList<Schema>();
        final Types types = definition.getTypes();
        final Collection elements = types.getExtensibilityElements();
        for (final Iterator iterator = elements.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof Schema)
            {
                schemas.add((Schema)object);
            }
        }
        return schemas;
    }

    private static Element getElementByAttribute(
        final Schema schema,
        final String attribute,
        final String value)
    {
        return getElementByAttribute(
            schema.getElement(),
            attribute,
            value);
    }

    /**
     * Gets the schema that owns the element that has the given attribute with the given value.
     *
     * @param definition the WSDL definition.
     * @param attribute the name of the attribute to find.
     * @param value the value of the attribute.
     * @return the schema
     */
    private static Schema getElementSchemaByAttribute(
        final Definition definition,
        final String attribute,
        final String value)
    {
        final Types types = definition.getTypes();
        final Collection elements = types.getExtensibilityElements();
        Schema schema = null;
        for (final Iterator iterator = elements.iterator(); iterator.hasNext();)
        {
            final Object object = iterator.next();
            if (object instanceof Schema)
            {
                final Element element = getElementByAttribute(
                        (Schema)object,
                        attribute,
                        value);
                if (element != null)
                {
                    schema = (Schema)object;
                    break;
                }
            }
        }
        return schema;
    }

    private static Element getElementByAttribute(
        final Element element,
        final String attribute,
        final String value)
    {
        Element found = null;
        if (element != null && value != null)
        {
            final String foundAttribute = element.getAttribute(attribute);
            if (StringUtils.isNotBlank(value) && value.equals(foundAttribute))
            {
                found = element;
            }
            else
            {
                final NodeList nodes = element.getChildNodes();
                for (int ctr = 0; ctr < nodes.getLength(); ctr++)
                {
                    final Node node = nodes.item(ctr);
                    if (node instanceof Element)
                    {
                        found =
                            getElementByAttribute(
                                (Element)node,
                                attribute,
                                value);
                        if (found != null)
                        {
                            break;
                        }
                    }
                }
            }
        }
        return found;
    }

    private static List<Element> getElementsWithAttribute(
        final Element element,
        final String attribute)
    {
        final List<Element> found = new ArrayList<Element>();
        if (element != null)
        {
            final String foundAttribute = element.getAttribute(attribute);
            if (StringUtils.isNotBlank(foundAttribute))
            {
                found.add(element);
            }
            final NodeList nodes = element.getChildNodes();
            for (int ctr = 0; ctr < nodes.getLength(); ctr++)
            {
                final Node node = nodes.item(ctr);
                if (node instanceof Element)
                {
                    found.addAll(getElementsWithAttribute(
                            (Element)node,
                            attribute));
                }
            }
        }
        return found;
    }

    private static String getAttributeValue(
        final Element element,
        final String attribute)
    {
        String value = null;
        Element found = getElementWithAttribute(
                element,
                attribute);
        if (found != null)
        {
            value = found.getAttribute(attribute);
        }
        return value;
    }

    private static String getAttributeValueFromChildElement(
        final Element element,
        final String attribute,
        int childIndex)
    {
        String value = null;
        if (element != null)
        {
            int elementIndex = 0;
            for (int ctr = 0; ctr < element.getChildNodes().getLength(); ctr++)
            {
                final Node node = element.getChildNodes().item(ctr);
                if (node instanceof Element)
                {
                    if (elementIndex == childIndex)
                    {
                        value =
                            getAttributeValue(
                                (Element)node,
                                attribute);
                    }
                    elementIndex++;
                }
            }
        }
        return value;
    }

    /**
     * Finds the first child element of the given <code>element</code> that has an attribute
     * matching the name of <code>attribute</code>.
     *
     * @param element the element to search.
     * @param attribute the name of the attribute to get.
     * @return the found element or null if not found.
     */
    private static Element getElementWithAttribute(
        final Element element,
        final String attribute)
    {
        Element found = null;
        if (element != null)
        {
            final NodeList nodes = element.getChildNodes();
            final String foundAttribute = element.getAttribute(attribute);
            if (StringUtils.isNotBlank(foundAttribute))
            {
                found = element;
            }
            if (found == null)
            {
                for (int ctr = 0; ctr < nodes.getLength(); ctr++)
                {
                    final Node node = nodes.item(ctr);
                    if (node instanceof Element)
                    {
                        found =
                            getElementWithAttribute(
                                (Element)node,
                                attribute);
                    }
                }
            }
        }
        return found;
    }

    private static final String CLASS = "class";

    /**
     * Deserializes the given <code>element</code> to the given <code>type</code>.
     *
     * @param element the XML OMElement
     * @param type the java type.
     * @param typeMapper the "object creator" used to construct objects from given classes.
     * @return the deserialized object.
     * @throws Exception
     */
    public static Object deserialize(
        OMElement element,
        Class type,
        final TypeMapper typeMapper) throws Exception
    {
        Object bean = null;
        if (typeMapper.isSimpleType(type))
        {
            bean = getSimpleTypeObject(type, element, typeMapper);
        }
        else if (type == byte[].class)
        {
            bean = Base64.decode(element.getText());
        }
        else
        {
            if (type.isArray())
            {
                final Collection<Object> elements = new ArrayList<Object>();
                for (final Iterator iterator = element.getChildElements(); iterator.hasNext();)
                {
                    OMElement omElement = (OMElement)iterator.next();
                    elements.add(deserialize(
                            omElement,
                            type.getComponentType(),
                            typeMapper));
                }
                bean =
                    elements.toArray((Object[])Array.newInstance(
                            type.getComponentType(),
                            0));
            }
            else
            {
                try
                {
                    type = getAppropriateType(element, type);
                    bean = typeMapper.getObject(type);
                    final java.beans.PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(type);
                    for (int ctr = 0; ctr < descriptors.length; ctr++)
                    {
                        final java.beans.PropertyDescriptor descriptor = descriptors[ctr];
                        final String name = descriptor.getName();
                        if (!CLASS.equals(name))
                        {
                            final OMElement propertyElement = findElementByName(
                                    element,
                                    name);

                            if (propertyElement != null)
                            {
                                PropertyUtils.setProperty(
                                    bean,
                                    name,
                                    deserialize(
                                        propertyElement,
                                        descriptor.getPropertyType(),
                                        typeMapper));
                            }
                        }
                    }
                }
                catch (final Throwable throwable)
                {
                    throw new RuntimeException(throwable);
                }
            }
        }
        return bean;
    }

    /**
     * Gets the object given the type, element and typeMapper.
     * @param type the type of object to get.
     * @param element the elemtn that has the value to populate the object with.
     * @param typeMapper the mapper used for retriving the value if it can't be found
     *        by the simple type mapper.
     * @return the object
     */
    private static Object getSimpleTypeObject(Class type,
        OMElement element, TypeMapper typeMapper)
    {
        Object object = org.apache.axis2.databinding.typemapping.SimpleTypeMapper.getSimpleTypeObject(
            type,
            element);
        if (object == null && element != null)
        {
            object = typeMapper.getObject(type, element.getText());
        }
        return object;
    }

    /**
     * The java package separator character.
     */
    private static final String PACKAGE_SEPARATOR = ".";

    /**
     * The xsi:type Qname.
     */
    private static final QName XSI_TYPE_QNAME = new QName(XSI_NS, TYPE);

    /**
     * Gets the appropriate type from checking the xsi:type (if present).  Currently
     * this just assumes any types in a hierarchy are in the same package.
     *
     * @param element the element from which to retrieve the type.
     * @param type the current type.
     * @return the appropriate type.
     * @throws ClassNotFoundException
     */
    private static Class getAppropriateType(final OMElement element, Class type) throws ClassNotFoundException
    {
        final String xsiTypeName = element.getAttributeValue(XSI_TYPE_QNAME);
        if (xsiTypeName != null)
        {
            final String typeName = getLocalName(xsiTypeName);
            if (!typeName.equals(type.getSimpleName()))
            {
                // TODO: need to handle types that aren't in the same package (we should look up the
                // mapped class here instead of assuming the same package)
                type = Thread.currentThread().getContextClassLoader().loadClass(
                    type.getPackage().getName() + PACKAGE_SEPARATOR + typeName);
            }
        }
        return type;
    }

    /**
     * Strips the prefix from a type name in the given form: prefix:localName
     * to get the local name.
     *
     * @param typeName the type name with an optional prefix
     * @return the local name.
     */
    private static String getLocalName(final String typeName)
    {
        String localName;
        String[] names = typeName.split(NS_SEPARATOR);
        if (names.length > 1)
        {
            localName = names[1];
        }
        else
        {
            localName = names[0];
        }
        return localName;
    }

    /**
     * Finds an element having the given <code>name</code> from the child elements on the given
     * <code>element</code>.
     *
     * @param element the element to search.
     * @param name the name of the element to find.
     * @return the found element or null if one couldn't be found.
     */
    private static OMElement findElementByName(
        final OMElement element,
        final String name)
    {
        OMElement found = null;
        for (final Iterator iterator = element.getChildElements(); iterator.hasNext();)
        {
            final OMElement child = (OMElement)iterator.next();
            if (child.getLocalName().equals(name))
            {
                found = child;
                break;
            }
        }
        return found;
    }

    /**
     * First delegate to the Axis2 simple type mapper, if that says
     * its simple, it is, otherwise check to see if the type is an enumeration (typesafe
     * or Java5 version).
     *
     * @param bean the bean to check.
     * @return true/false
     */
    private static boolean isSimpleType(final Object bean, final TypeMapper typeMapper)
    {
        return typeMapper.isSimpleType(bean != null ? bean.getClass() : null);
    }
}