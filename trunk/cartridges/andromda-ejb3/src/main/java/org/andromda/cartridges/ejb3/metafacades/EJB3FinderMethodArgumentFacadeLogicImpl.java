package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacade
 */
public class EJB3FinderMethodArgumentFacadeLogicImpl
    extends EJB3FinderMethodArgumentFacadeLogic
{

    public EJB3FinderMethodArgumentFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacade#getTemporalType()
     */
    protected java.lang.String handleGetTemporalType()
    {
        String temporalType = 
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_TEMPORAL_TYPE);
        if (StringUtils.isBlank(temporalType))
        {
            ClassifierFacade classifier = this.getType();
            if (classifier != null)
            {
                if (!classifier.isPrimitive()) {
                    if (classifier.isDateType())
                    {
                        temporalType = EJB3Globals.TEMPORAL_TYPE_DATE;
                    }
                    else if (classifier.isTimeType())
                    {
                        temporalType = EJB3Globals.TEMPORAL_TYPE_TIME;
                    }
                    else if (classifier.getName().equals("Timestamp"))
                    {
                        temporalType = EJB3Globals.TEMPORAL_TYPE_TIMESTAMP;
                    }
                }
            }
        }
        return temporalType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacadeLogic#handleIsFirstResult()
     */
    protected boolean handleIsFirstResult()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_FINDER_RESULT_TYPE_FIRST);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacadeLogic#handleIsMaxResults()
     */
    protected boolean handleIsMaxResults()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_FINDER_RESULT_TYPE_MAX);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacadeLogic#
     *      handleIsEnumerationTypeOrdinal()
     */
    protected boolean handleIsEnumerationTypeOrdinal()
    {
        //AttributeFacade attribute = this.getType().findAttribute(this.getName());
        boolean ordinalType = false;
        if (this.getType().isEnumeration())
        {
            AttributeFacade literal = (AttributeFacade)this.getType().getAttributes().iterator().next();
            if (!literal.getType().isStringType())
            {
                ordinalType = true;
            }
        }
        return ordinalType;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacadeLogic#
     *      handleGetEnumerationTypeString()
     */
    protected boolean handleIsEnumerationTypeString()
    {
        boolean stringType = false;
        if (this.getType().isEnumeration())
        {
            AttributeFacade literal = (AttributeFacade)this.getType().getAttributes().iterator().next();
            if (literal.getType().isStringType())
            {
                stringType = true;
            }
        }
        return stringType;
    }

}