package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class TaggedValueFacadeLogicImpl
        extends TaggedValueFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public TaggedValueFacadeLogicImpl(TaggedValue metaObject, String context)
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
            final TagDefinition type = this.metaObject.getType();
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
    @Override
    public Collection handleGetValues()
    {
        final Collection values = new ArrayList();
        values.addAll(metaObject.getDataValue());
        values.addAll(this.shieldedElements(metaObject.getReferenceValue()));
        return values;
    }

    /**
     * @see org.andromda.metafacades.uml.TaggedValueFacade#getValue()
     */
    @Override
    public Object handleGetValue()
    {
        final Collection values = this.getValues();
        return (values.isEmpty()) ? null : values.iterator().next();
    }

}