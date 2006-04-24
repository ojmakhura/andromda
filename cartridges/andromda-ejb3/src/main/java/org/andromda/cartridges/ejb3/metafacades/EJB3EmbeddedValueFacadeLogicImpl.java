package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.ejb3.EJB3Profile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3EmbeddedValueFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3EmbeddedValueFacade
 */
public class EJB3EmbeddedValueFacadeLogicImpl
    extends EJB3EmbeddedValueFacadeLogic
{

    /**
     * The property which stores the pattern defining the embedded value
     * implementation name.
     */
    static final String EMBEDDED_VALUE_IMPLEMENTATION_NAME_PATTERN = "embeddedValueImplementationNamePattern";
    
    public EJB3EmbeddedValueFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EmbeddedValueFacade#isImmutable()
     */
    protected boolean handleIsImmutable()
    {
        boolean immutable = false;
        Object value = this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_IMMUTABLE);
        if (value != null)
        {
            immutable = Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
        }
        return immutable;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3EmbeddedValueFacade#getImplementationName()
     */
    protected java.lang.String handleGetImplementationName()
    {
        return MessageFormat.format(
                getImplementationNamePattern(),
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * Gets the value of the {@link #EMBEDDED_VALUE_IMPLEMENTATION_NAME_PATTERN}
     *
     * @return the embedded value name pattern.
     */
    private String getImplementationNamePattern()
    {
        return String.valueOf(this.getConfiguredProperty(EMBEDDED_VALUE_IMPLEMENTATION_NAME_PATTERN));
    }
}