package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3EmbeddedValueFacade.
 *
 * @see EJB3EmbeddedValueFacade
 */
public class EJB3EmbeddedValueFacadeLogicImpl
    extends EJB3EmbeddedValueFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * The property which stores the pattern defining the embedded value
     * implementation name.
     */
    static final String EMBEDDED_VALUE_IMPLEMENTATION_NAME_PATTERN = "embeddedValueImplementationNamePattern";

    /**
     * @param metaObject
     * @param context
     */
    public EJB3EmbeddedValueFacadeLogicImpl(final Object metaObject, final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see EJB3EmbeddedValueFacade#isImmutable()
     */
    @Override
    protected boolean handleIsImmutable()
    {
        boolean immutable = false;
        Object value = this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_IMMUTABLE);
        if (value != null)
        {
            immutable = Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
        }
        return immutable;
    }

    /**
     * @see EJB3EmbeddedValueFacade#getImplementationName()
     */
    @Override
    protected String handleGetImplementationName()
    {
        return MessageFormat.format(
                this.getImplementationNamePattern(),
                StringUtils.trimToEmpty(this.getName()));
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

    /**
     * @see EJB3EmbeddedValueFacadeLogic#handleGetFullyQualifiedImplementationName()
     */
    @Override
    protected String handleGetFullyQualifiedImplementationName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getPackageName(),
                this.getImplementationName(),
                null);
    }
}