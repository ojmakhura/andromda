package org.andromda.cartridges.hibernate.metafacades;

import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Contains utilities for use with Hibernate metafacades.
 * 
 * @author Chad Brandon
 */
class HibernateMetafacadeUtils
{

    /**
     * Gets the view type for the passed in <code>classifier</code>. Checks
     * to see if the classifier is a service, if so checks to see
     * if it has the tagged value defining the view type.  If the classifier
     * is not a service, the view type 'local' is always returned.
     * 
     * @return String the view type name.
     */
    static String getViewType(ClassifierFacade classifier)
    {
        final String methodName = "HibernateMetafacadeUtils.getViewType";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        String viewType = "local";
        if (classifier.hasStereotype(HibernateProfile.STEREOTYPE_SERVICE))
        {
            String viewTypeValue = (String)classifier
                .findTaggedValue(HibernateProfile.TAGGEDVALUE_EJB_VIEWTYPE);
            // if the view type wasn't found, search all super classes
            if (StringUtils.isEmpty(viewTypeValue))
            {
                viewType = (String)CollectionUtils.find(classifier
                    .getAllGeneralizations(), new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        return ((ModelElementFacade)object)
                            .findTaggedValue(HibernateProfile.TAGGEDVALUE_EJB_VIEWTYPE) != null;
                    }
                });
            }
            if (StringUtils.isNotEmpty(viewTypeValue))
            {
                viewType = viewTypeValue;
            }
            else
            {
                viewType = "remote";
            }
        }
        return viewType;
    }
}