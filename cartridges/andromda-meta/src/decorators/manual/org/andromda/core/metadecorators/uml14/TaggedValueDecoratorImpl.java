package org.andromda.core.metadecorators.uml14;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.HTMLAnalyzer;


        
/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.TaggedValue
 *
 *
 */
public class TaggedValueDecoratorImpl extends TaggedValueDecorator
{
    // ---------------- constructor -------------------------------
    
    public TaggedValueDecoratorImpl (org.omg.uml.foundation.core.TaggedValue metaObject)
    {
        super (metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class TaggedValueDecorator ...

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.TaggedValueDecorator#getTag()
     */
    public java.lang.String getTag() {
        String tgvName = metaObject.getName();
            
        // sometimes the tag name is on the TagDefinition
        if ( (tgvName == null) && (metaObject.getType() != null) )
        {
            tgvName = metaObject.getType().getName();
                
            // sometimes it is the TagType
            if (tgvName == null)
            {
                tgvName = metaObject.getType().getTagType();
            }
        }
                    
        return tgvName;
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
            // TODO: Better exception handling!
            e.printStackTrace();
            return null;
        }
    }

    // ------------- relations ------------------
    
}
