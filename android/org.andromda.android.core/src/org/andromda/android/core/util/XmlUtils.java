package org.andromda.android.core.util;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

/**
 * Contains utilities for dealing with XML.
 *
 * @author Chad Brandon
 * @author Peter Friese
 */
public class XmlUtils
{

    /**
     * Adds the <code>value</code> to the body of the <code>xmlObject</code> 's first element.
     *
     * @param object the XmlObject instance for which we'll add the value.
     * @param value the new value to add.
     */
    static void addTextValueToElement(XmlObject object,
        Object value)
    {
        final String methodName = "XmlUtils.addTextValueToFirstElement";
        if (object == null)
        {
            throw new IllegalArgumentException(methodName + " object can not be null");
        }
        XmlCursor cursor = object.newCursor();
        cursor.toFirstChild();
        if (value != null)
        {
            cursor.setTextValue(String.valueOf(value));
        }
        else
        {
            cursor.setTextValue(null);
        }
        cursor.dispose();
    }

    /**
     * Gets the text value (if any) from the body of the first element contained within the XmlObject
     * <code>object</code> instance.
     *
     * @param object the XmlObject instance for which we'll add the value.
     * @return the text value as a String
     */
    public static String getTextValueFromElement(XmlObject object)
    {
        String value = null;
        if (object != null)
        {
            XmlCursor cursor = object.newCursor();
            cursor.toFirstChild();
            value = cursor.getTextValue();
            // remove all excess whitespace
            if (StringUtils.isNotEmpty(value))
            {
                // remove tabs
                value = value.replaceAll("\t", "");
                // remove newline characters
                value = value.replaceAll("\n", " ");
                value = value.replaceAll("    ", "").trim();
            }
            cursor.dispose();
        }
        return value;
    }

    /**
     * Gets the element from the passed in XmlObject instance.
     *
     * @param object the XmlObject instance from which to retrieve the element.
     * @param name the element name to retrieve.
     * @return the element (or <code>null</code> if the element doesn't exist
     */
    static XmlObject getNestedElement(XmlObject object,
        String name)
    {
        final String methodName = "XmlUtils.getTextValueFromElement";
        if (object == null)
        {
            throw new IllegalArgumentException(methodName + " object can not be null");
        }
        XmlObject element = null;
        XmlObject[] results = selectElements(object, name);
        if (results != null && results.length > 0)
        {
            element = results[0];
        }
        return element;
    }

    /**
     * Selects elements from the given <code>object</code> instance and using the given <code>path</code>.
     *
     * @param object the XmlObject instance from which the elements will be selected
     * @param path the path to use, should <strong>NOT </strong> start with any slashes.
     * @return the found XmlObject instances
     */
    static XmlObject[] selectElements(XmlObject object,
        String path)
    {
        final String methodName = "XmlUtils.selectPath";
        if (object == null)
        {
            throw new IllegalArgumentException(methodName + " 'object' can not be null");
        }
        if (path == null)
        {
            throw new IllegalArgumentException(methodName + " 'path' can not be null");
        }
        path = path.replaceAll("/", "/e:");
        // put back the attribute path to what it should be
        path = path.replaceAll("/e:@", "/@");

        String query = "declare namespace e='" + DEFAULT_QNAME_URI + "' " + ".//e:" + path;
        return object.selectPath(query);
    }

    /**
     * Gets the attribute value (if any) from the body of the first element contained within the XmlObject
     * <code>object</code> instance.
     *
     * @param object the XmlObject instance for which we'll add the value.
     * @param name the name of the attribute.
     * @return the text value as a String
     */
    static String getAttributeValueFromElement(XmlObject object,
        String name)
    {
        String value = null;
        if (object != null)
        {
            XmlCursor cursor = object.newCursor();
            cursor.toFirstChild();
            value = cursor.getAttributeText(getQName(name));
            if (value == null)
            {
                value = cursor.getAttributeText(getQName(":" + name));
            }
            cursor.dispose();
        }
        return value;
    }

    /**
     * Adds an attribute with the given <code>name</code> having the given <code>value</code> to
     * <code>anyType</code> element, but does <strong>NOT </strong> add the default namespace if no namespace is given
     * in the <code>name</code>.
     *
     * @param anyType the XmlObject instance to which the attribute will be added.
     * @param value the new value to add.
     */
    static void addAttributeNoNamespace(XmlObject anyType,
        String name,
        Object value)
    {
        addAttribute(anyType, ":" + name, value);
    }

    /**
     * Adds an attribute with the given <code>name</code> having the given <code>value</code> to
     * <code>anyType</code> element.
     *
     * @param anyType the XmlObject instance to which the attribute will be added.
     * @param value the new value to add.
     */
    static void addAttribute(XmlObject anyType,
        String name,
        Object value)
    {
        final String methodName = "XmlUtils.addAttribute";
        if (anyType == null)
        {
            throw new IllegalArgumentException(methodName + " anyType can not be null");
        }
        XmlCursor cursor = anyType.newCursor();
        cursor.toFirstContentToken();

        QName qName = qName = getQName(name);
        if (value != null)
        {
            cursor.insertAttributeWithValue(qName, String.valueOf(value));
        }
        else
        {
            cursor.insertAttributeWithValue(qName, null);
        }
        cursor.dispose();
    }

    /**
     * Adds an element with the given <code>name</code> having the given <code>value</code> to <code>anyType</code>
     * element.
     *
     * @param anyType the XmlObject instance to which the element will be added.
     * @param value the new value to add.
     */
    static void addElement(XmlObject anyType,
        String name,
        Object value)
    {
        final String methodName = "XmlUtils.addElement";
        if (anyType == null)
        {
            throw new IllegalArgumentException(methodName + " anyType can not be null");
        }
        XmlCursor cursor = anyType.newCursor();
        cursor.toFirstContentToken();

        QName qName = getQName(name);
        if (value != null)
        {
            cursor.insertElementWithText(qName, String.valueOf(value));
        }
        else
        {
            cursor.insertElementWithText(qName, null);
        }
        cursor.dispose();
    }

    /**
     * Adds an element with the given <code>name</code> to the <code>anyType</code> element.
     *
     * @param anyType the XmlObject instance to which the element will be added.
     * @param value the new value to add.
     */
    static void addElement(XmlObject anyType,
        String name)
    {
        final String methodName = "XmlUtils.addElement";
        if (anyType == null)
        {
            throw new IllegalArgumentException(methodName + " anyType can not be null");
        }
        XmlCursor cursor = anyType.newCursor();
        cursor.toFirstContentToken();
        QName qName = getQName(name);
        cursor.insertElement(qName);
        cursor.dispose();
    }

    /**
     * If a URI isn't specified when adding an element, this one is used.
     */
    private static final String DEFAULT_QNAME_URI = "urn:hl7-org:v3";

    /**
     * Constructs a QName instance from the given <code>name</code> by seperating the URI from the actual name.
     *
     * @param name the name to create the QName instance from.
     * @return the new QName (or null if name was null).
     */
    private static QName getQName(String name)
    {
        QName qName = null;
        if (StringUtils.isNotEmpty(name))
        {
            int lastColonIndex = name.lastIndexOf(':');
            String uri = null;
            if (lastColonIndex != -1)
            {
                uri = name.substring(0, lastColonIndex);
                name = name.substring(lastColonIndex + 1, name.length());
            }
            else
            {
                uri = DEFAULT_QNAME_URI;
            }

            if (StringUtils.isNotEmpty(uri))
            {
                qName = new QName(uri, name);
            }
            else
            {
                qName = new QName(name);
            }
        }
        return qName;
    }

    /**
     * Adds the given <code>text</code> to the element found having the specified <code>elementName</code>.
     *
     * @param anyType the any type element.
     * @param elementName the element name.
     * @param text the text to add
     */
    static void addCharactersToElement(XmlObject object,
        String text)
    {
        final String methodName = "XmlUtils.addTextToElement";
        if (object == null)
        {
            throw new IllegalArgumentException(methodName + " object can not be null");
        }
        XmlCursor cursor = object.newCursor();
        cursor.insertChars(text);
        cursor.dispose();
    }

    /**
     * Adds an attribute with the given <code>name</code> and <code>value</code> to the first element found with the
     * given <code>elementName</code> on the anyType element. In other words the element having
     * <code>elementName</code> must exist on <code>anyType</code>.
     *
     * @param anyType the XmlObject instance where the element will be found
     * @param elementName the name of the element to find
     * @param name the name of the attribute to add
     * @param value the value to set on the attribute
     */
    static void addAttributeToElement(XmlObject anyType,
        String elementName,
        String name,
        Object value)
    {
        final String methodName = "XmlUtils.addAttributeToElement";
        if (anyType == null)
        {
            throw new IllegalArgumentException(methodName + " anyType can not be null");
        }
        // find the element using xpath so that we can add the attribute.
        String query = "declare namespace e='" + DEFAULT_QNAME_URI + "' " + ".//e:" + elementName;
        XmlObject[] results = anyType.selectPath(query);
        if (results != null && results.length > 0)
        {
            XmlObject result = results[0];
            XmlCursor cursor = result.newCursor();
            cursor.toFirstContentToken();
            if (value != null)
            {
                cursor.insertAttributeWithValue(name, String.valueOf(value));
            }
            else
            {
                cursor.insertAttributeWithValue(name, null);
            }
            cursor.dispose();
        }
    }
}