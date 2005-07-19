package org.andromda.metafacades.uml14;

import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.TagDefinition;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Metaclass facade implementation.
 */
public class TaggedValueFacadeLogicImpl
        extends TaggedValueFacadeLogic
{
    public TaggedValueFacadeLogicImpl(org.omg.uml.foundation.core.TaggedValue metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetName()
     */
    public String handleGetName()
    {
        String name = super.handleGetName();
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
        Collection values = new ArrayList();
        values.addAll(metaObject.getDataValue());
        values.addAll(shieldedElements(metaObject.getReferenceValue()));
        return values;
    }

    /**
     * @see org.andromda.metafacades.uml.TaggedValueFacade#getValue()
     */
    public java.lang.Object handleGetValue()
    {
        Collection values = getValues();
        return (values.isEmpty()) ? null : values.iterator().next();
    }

}