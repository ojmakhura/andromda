package org.andromda.metafacades.uml14;

import org.andromda.core.common.HTMLAnalyzer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.TagDefinition;

import java.io.IOException;
import java.util.Collection;


/**
 *
 *
 * Metaclass facade implementation.
 *
 */
public class TaggedValueFacadeLogicImpl
       extends TaggedValueFacadeLogic
       implements org.andromda.metafacades.uml.TaggedValueFacade
{
    // ---------------- constructor -------------------------------

    public TaggedValueFacadeLogicImpl (org.omg.uml.foundation.core.TaggedValue metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.core.metadecorators.uml14.ModelElement#getName()
     */
    public String getName() 
    {
        String name = super.getName();
        if (StringUtils.isEmpty(name)) 
        {
            TagDefinition type = this.metaObject.getType();
            if (type != null) 
            {
                name = type.getName();
                // sometimes it is the TagType
                if (StringUtils.isEmpty(name)) 
                {
                    name = type.getTagType();
                }
            }
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.TaggedValueFacade#getValues()
     */
    public Collection handleGetValues()
    {
        return metaObject.getDataValue();
    }

    /**
     * @see org.andromda.metafacades.uml.TaggedValueFacade#getValue()
     */
    public java.lang.Object handleGetValue() 
    {
        Collection values = getValues();
        return (values.isEmpty()) ? null : values.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.TaggedValueFacade#formatHTMLStringAsParagraphs()
     */
    public Collection handleFormatHTMLStringAsParagraphs()
    {
        try
        {
            return new HTMLAnalyzer().htmlToParagraphs(getValue().toString());
        }
        catch (IOException e)
        {
            logger.error(e);
            return null;
        }
    }
    
}
