package org.andromda.cartridges.aspdotnet.metafacades;

import org.andromda.cartridges.aspdotnet.CommonProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.aspdotnet.metafacades.AspParameter.
 *
 * @see org.andromda.cartridges.aspdotnet.metafacades.AspParameter
 */
public class AspParameterLogicImpl
    extends AspParameterLogic
{
    public AspParameterLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.cartridges.aspdotnet.metafacades.AspParameter#getViewType()
     */
    protected java.lang.String handleGetViewType()
    {
    	String taggedValue = new String();
    	if (this.findTaggedValue(CommonProperties.FIELD_TYPE)!=null)
    		taggedValue = this.findTaggedValue(CommonProperties.FIELD_TYPE).toString();
    	
    	if (taggedValue.equals(CommonProperties.FIELD_TYPE_SELECT))
    		return "DropDownList";
    	else if (taggedValue.equals(CommonProperties.FIELD_TYPE_CHECKBOX))
    		return "CheckBox";
    	else if (taggedValue.equals(CommonProperties.FIELD_TYPE_RADIO))
    		return "RadioButton";
    	else //in any other case we return TextBox
    		return "TextBox";
    }
	protected String handleGetDataPropertyName() 
	{
		return null;
	}
	protected String handleGetCapitalisedName() 
	{
		return StringUtils.capitalise(this.getName());
	}
	protected boolean handleIsMandatoryField() 
	{
		return !(this.findTaggedValue(CommonProperties.FIELD_MANDATORY) == null || this.findTaggedValue(CommonProperties.FIELD_MANDATORY).equals("false"));
	}
	protected String handleGetRadioButtonGroup() 
	{
		//todo
		return this.findTaggedValue(CommonProperties.RADIO_BUTTON_GROUP).toString();
	}
	protected String handleGetComparatorType() 
	{
		String type = this.getType().getName();
		if(type.equals("int") || type.equals("Integer"))
			return "Integer";
		else if(type.equals("double") || type.equals("Double"))
			return "Double";
		else if(type.equals("Date"))
			return "Date";
		return "String";
	}

}