package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFProfile;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFAttribute.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute
 */
public class JSFAttributeLogicImpl
    extends JSFAttributeLogic
{

    public JSFAttributeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();
        final ClassifierFacade owner = this.getOwner();
        if (owner != null)
        {
            messageKey.append(StringUtilsHelper.toResourceMessageKey(owner.getName()));
            messageKey.append('.');
        }
        final String name = this.getName();
        if (name != null && name.trim().length() > 0)
        {
            messageKey.append(StringUtilsHelper.toResourceMessageKey(name));
        }
        return messageKey.toString();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName());
    }
    
    /**
     * Retrieves the input format defined by the {@link JSFProfile#TAGGEDVALUE_INPUT_FORMAT}.
     *
     * @return the input format.
     */
    private final String getDisplayFormat()
    {
        final Object value = findTaggedValue(JSFProfile.TAGGEDVALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return format == null ? null : format.trim();
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getFormat()
     */
    protected String handleGetFormat()
    {
        String format = null;
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            format = this.getDisplayFormat();
            if (format == null)
            {
                if (type.isTimeType())
                {
                    format = this.getDefaultTimeFormat();
                }
                else if (type.isDateType())
                {
                    format = this.getDefaultDateFormat();
                }
            }
            else if (type.isDateType())
            {
                format = JSFUtils.getDateFormat(format);
            }
        }
        return format;
    }
    
    /**
     * @return the default time format pattern as defined using the configured property
     */
    private String getDefaultTimeFormat()
    {
        return (String)this.getConfiguredProperty(JSFGlobals.PROPERTY_DEFAULT_TIMEFORMAT);
    }
    
    /**
     * @return the default date format pattern as defined using the configured property
     */
    private String getDefaultDateFormat()
    {
        return (String)this.getConfiguredProperty(JSFGlobals.PROPERTY_DEFAULT_DATEFORMAT);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFAttribute#getDummyValue()
     */
    protected String handleGetDummyValue()
    {
        final ClassifierFacade type = this.getType();
        if (type != null)
        {
            final String typeName = type.getFullyQualifiedName();
            final String name = this.getName();
            if ("java.lang.String".equals(typeName)) return "\"" + name + "-test" + "\"";
            if ("java.util.Date".equals(typeName)) return "new java.util.Date()";
            if ("java.sql.Date".equals(typeName)) return "new java.sql.Date(new java.util.Date().getTime())";
            if ("java.sql.Timestamp".equals(typeName)) return "new java.sql.Timestamp(new Date().getTime())";
            if ("java.util.Calendar".equals(typeName)) return "java.util.Calendar.getInstance()";
            if ("int".equals(typeName)) return "(int)" + name.hashCode();
            if ("boolean".equals(typeName)) return "false";
            if ("long".equals(typeName)) return "(long)" + name.hashCode();
            if ("char".equals(typeName)) return "(char)" + name.hashCode();
            if ("float".equals(typeName)) return "(float)" + name.hashCode() / hashCode();
            if ("double".equals(typeName)) return "(double)" + name.hashCode() / hashCode();
            if ("short".equals(typeName)) return "(short)" + name.hashCode();
            if ("byte".equals(typeName)) return "(byte)" + name.hashCode();
            if ("java.lang.Integer".equals(typeName)) return "new Integer((int)" + name.hashCode() + ")";
            if ("java.lang.Boolean".equals(typeName)) return "Boolean.FALSE";
            if ("java.lang.Long".equals(typeName)) return "new Long((long)" + name.hashCode() + ")";
            if ("java.lang.Character".equals(typeName)) return "new Character(char)" + name.hashCode() + ")";
            if ("java.lang.Float".equals(typeName)) return "new Float((float)" + name.hashCode() / hashCode() + ")";
            if ("java.lang.Double".equals(typeName)) return "new Double((double)" + name.hashCode() / hashCode() + ")";
            if ("java.lang.Short".equals(typeName)) return "new Short((short)" + name.hashCode() + ")";
            if ("java.lang.Byte".equals(typeName)) return "new Byte((byte)" + name.hashCode() + ")";
            //if (type.isArrayType()) return constructDummyArray();
            if (type.isSetType()) return "new java.util.HashSet(java.util.Arrays.asList(" + constructDummyArray() + "))";
            if (type.isCollectionType()) return "java.util.Arrays.asList(" + constructDummyArray() + ")";

            // maps and others types will simply not be treated
        }
        return "null";
    }
    
    /**
     * Constructs a string representing an array initialization in Java.
     *
     * @return A String representing Java code for the initialization of an array.
     */
    private final String constructDummyArray()
    {
        return JSFUtils.constructDummyArrayDeclaration(this.getName(), JSFGlobals.DUMMY_ARRAY_COUNT);
    }
}