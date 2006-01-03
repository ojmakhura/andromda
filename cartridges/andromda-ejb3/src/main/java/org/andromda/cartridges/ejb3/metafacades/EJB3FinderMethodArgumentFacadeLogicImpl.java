package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Globals;
import org.andromda.cartridges.ejb3.EJB3Profile;
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
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_FINDER_PARAMETER_TEMPORAL_TYPE);
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
        boolean firstResult = false;
        String resultType = 
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_FINDER_PARAMETER_RESULT_TYPE);
        if (StringUtils.isNotBlank(resultType))
        {
            if (resultType.equalsIgnoreCase(EJB3Globals.FINDER_RESULT_TYPE_FIRST))
            {
                firstResult = true;
            }
        }
        return firstResult;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3FinderMethodArgumentFacadeLogic#handleIsMaxResults()
     */
    protected boolean handleIsMaxResults()
    {
        boolean maxResults = false;
        String resultType = 
            (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_PERSISTENCE_FINDER_PARAMETER_RESULT_TYPE);
        if (StringUtils.isNotBlank(resultType))
        {
            if (resultType.equalsIgnoreCase(EJB3Globals.FINDER_RESULT_TYPE_MAX))
            {
                maxResults = true;
            }
        }
        return maxResults;
    }

}