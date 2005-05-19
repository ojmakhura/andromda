package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAttribute.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAttribute
 */
public class StrutsManageableEntityAttributeLogicImpl
        extends StrutsManageableEntityAttributeLogic
{
    // ---------------- constructor -------------------------------

    public StrutsManageableEntityAttributeLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAttribute#getTitleKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        String titleKey = "";

        final ClassifierFacade owner = getOwner();
        if (owner != null)
        {
            titleKey += owner.getName() + '.';
        }

        return StringUtilsHelper.toResourceMessageKey(titleKey + getName());
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAttribute#getTitleValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    protected java.lang.String handleGetDateFormat()
    {
        String defaultDateFormat = null;

        if (getType() != null && getType().isDateType())
        {
            final Object taggedValueObject = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_DATE_FORMAT);
            if (taggedValueObject == null)
            {
                defaultDateFormat = (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_DATEFORMAT);
            }
            else
            {
                defaultDateFormat = taggedValueObject.toString();
            }
        }

        return defaultDateFormat;
    }

    protected boolean handleIsNeedsFileUpload()
    {
        // @todo: this implementation must be improved to handle Blob & Clob in a better way
        return getType().getFullyQualifiedName().equals("byte[]");
    }

    protected boolean handleIsHidden()
    {
        return !isDisplay() || Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_HIDDEN.equals(getWidgetType());
    }

    protected String handleGetWidgetType()
    {
        final Object widgetTag = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE);
        return (widgetTag == null) ? Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXT : widgetTag.toString();
    }
}