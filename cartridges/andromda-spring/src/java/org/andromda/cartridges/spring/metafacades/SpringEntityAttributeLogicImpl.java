package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringEntityAttribute.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringEntityAttribute
 */
public class SpringEntityAttributeLogicImpl
    extends SpringEntityAttributeLogic
    implements org.andromda.cartridges.spring.metafacades.SpringEntityAttribute
{
    // ---------------- constructor -------------------------------

    public SpringEntityAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityAttribute#isUnique()
     */
    public boolean handleIsUnique()
    {
        boolean unique = false;
        Object value = this
            .findTaggedValue(SpringProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_UNIQUE);
        if (value != null)
        {
            unique = Boolean.valueOf(StringUtils.trimToEmpty(value.toString()))
                .booleanValue();
        }
        return unique;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityAttribute#getIndex()
     */
    public java.lang.String handleGetIndex()
    {
        String index = (String)this
            .findTaggedValue(SpringProfile.TAGGEDVALUE_PERSISTENCE_COLUMN_INDEX);
        return index != null ? StringUtils.trimToEmpty(index) : null;
    }

}
