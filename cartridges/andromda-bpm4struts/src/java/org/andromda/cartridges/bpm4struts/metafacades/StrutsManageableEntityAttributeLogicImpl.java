package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAttribute.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsManageableEntityAttribute
 */
public class StrutsManageableEntityAttributeLogicImpl
    extends StrutsManageableEntityAttributeLogic
{
    public StrutsManageableEntityAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see StrutsManageableEntityAttribute#getMessageKey()
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
     * @see StrutsManageableEntityAttribute#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    private String internalGetDateFormat()
    {
        String dateFormat = null;

        if (this.getType() != null && this.getType().isDateType())
        {
            final Object taggedValueObject = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_FORMAT);
            if (taggedValueObject == null)
            {
                dateFormat = (String)this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_DATEFORMAT);
            }
            else
            {
                dateFormat = taggedValueObject.toString();
            }
        }

        return dateFormat;
    }

    protected java.lang.String handleGetDateFormat()
    {
        String dateFormat = this.internalGetDateFormat();

        if (dateFormat != null)
        {
            final String[] tokens = dateFormat.split("[\\s]+");
            int tokenIndex = 0;
            if (tokenIndex < tokens.length && tokens[tokenIndex].trim().equals("strict"))
            {
                tokenIndex++;
            }
            if (tokenIndex < tokens.length)
            {
                dateFormat = tokens[tokenIndex].trim();
            }
        }

        return dateFormat;
    }

    protected boolean handleIsStrictDateFormat()
    {
        final String dateFormat = this.internalGetDateFormat();
        return (dateFormat != null && dateFormat.trim().startsWith("strict"));
    }
    
    protected boolean handleIsNeedsFileUpload()
    {
        return this.getType() != null && this.getType().isBlobType();
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