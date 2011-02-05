package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacade.
 *
 * @see EJB3FinderMethodArgumentFacade
 */
public class EJB3FinderMethodArgumentFacadeLogicImpl
    extends EJB3FinderMethodArgumentFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EJB3FinderMethodArgumentFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3FinderMethodArgumentFacade#getTemporalType()
     */
    @Override
    protected String handleGetTemporalType()
    {
        String temporalType =
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_TEMPORAL_TYPE);
        if (StringUtils.isBlank(temporalType))
        {
            ClassifierFacade classifier = this.getType();
            if (classifier != null)
            {
                if (!classifier.isPrimitive()) 
                {
                    if (classifier.isDateType())
                    {
                        temporalType = EJB3Globals.TEMPORAL_TYPE_DATE;
                    }
                    else if (classifier.isTimeType())
                    {
                        temporalType = EJB3Globals.TEMPORAL_TYPE_TIME;
                    }
                    else if ("Timestamp".equals(classifier.getName()))
                    {
                        temporalType = EJB3Globals.TEMPORAL_TYPE_TIMESTAMP;
                    }
                }
            }
        }
        return temporalType;
    }

    /**
     * @see EJB3FinderMethodArgumentFacadeLogic#handleIsFirstResult()
     */
    @Override
    protected boolean handleIsFirstResult()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_FINDER_RESULT_TYPE_FIRST);
    }

    /**
     * @see EJB3FinderMethodArgumentFacadeLogic#handleIsMaxResults()
     */
    @Override
    protected boolean handleIsMaxResults()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_FINDER_RESULT_TYPE_MAX);
    }

    /**
     * @see EJB3FinderMethodArgumentFacadeLogic#handleIsEnumerationTypeOrdinal()
     */
    @Override
    protected boolean handleIsEnumerationTypeOrdinal()
    {
        boolean ordinalType = false;
        if (this.getType().isEnumeration())
        {
            AttributeFacade literal = this.getType().getAttributes().iterator().next();
            if (!literal.getType().isStringType())
            {
                ordinalType = true;
            }
        }
        return ordinalType;
    }

    /**
     * @see EJB3FinderMethodArgumentFacadeLogic#handleIsEnumerationTypeString()
     */
    @Override
    protected boolean handleIsEnumerationTypeString()
    {
        boolean stringType = false;
        if (this.getType().isEnumeration())
        {
            AttributeFacade literal = this.getType().getAttributes().iterator().next();
            if (literal.getType().isStringType())
            {
                stringType = true;
            }
        }
        return stringType;
    }
}
