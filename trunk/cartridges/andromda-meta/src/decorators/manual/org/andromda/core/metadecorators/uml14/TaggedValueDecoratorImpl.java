package org.andromda.core.metadecorators.uml14;

import java.util.Iterator;


        
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

    // ------------- relations ------------------
    
}
