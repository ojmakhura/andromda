package org.andromda.metafacades.uml14;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.HTMLAnalyzer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.TagDefinition;


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

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class TaggedValueDecorator ...

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElement#getName()
     */
    public String getName() {
        String name = super.getName();
        if (StringUtils.isEmpty(name)) {
            TagDefinition type = this.metaObject.getType();
            if (type != null) {
                name = type.getName();
                // sometimes it is the TagType
                if (StringUtils.isEmpty(name)) {
                    name = type.getTagType();
                }
            }
        }
        return name;        
    }


    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.TaggedValueDecorator#getValue()
     */
    public java.lang.String getValue() {
        StringBuffer sb = new StringBuffer();
        for (Iterator i = metaObject.getDataValue().iterator(); i.hasNext(); )
        {
            Object v = i.next();
            sb.append(v.toString());
            
            if (i.hasNext())
            {
                sb.append(" ");
            }
        }
        
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.TaggedValueDecorator#formatHTMLStringAsParagraphs()
     */
    public Collection formatHTMLStringAsParagraphs()
    {
        try
        {
            return new HTMLAnalyzer().htmlToParagraphs(getValue());
        }
        catch (IOException e)
        {
            logger.error(e);
            return null;
        }
    }

    // ------------- relations ------------------
    
}
