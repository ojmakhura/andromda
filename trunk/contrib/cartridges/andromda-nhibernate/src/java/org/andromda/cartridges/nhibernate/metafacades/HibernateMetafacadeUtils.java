package org.andromda.cartridges.nhibernate.metafacades;

import java.util.Collection;

import org.andromda.cartridges.nhibernate.HibernateProfile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
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
     * Gets the view type for the passed in <code>classifier</code>. If the
     * view type can be retrieved from the <code>classifier</code>, then that
     * is used, otherwise the <code>defaultViewType</code> is returned.
     *
     * @return String the view type name.
     */
    static String getViewType(
        ClassifierFacade classifier,
        String defaultViewType)
    {
        final String methodName = "HibernateMetafacadeUtils.getViewType";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        String viewType = null;
        if (classifier.hasStereotype(HibernateProfile.STEREOTYPE_SERVICE))
        {
            String viewTypeValue = (String)classifier.findTaggedValue(HibernateProfile.TAGGEDVALUE_EJB_VIEWTYPE);

            // if the view type wasn't found, search all super classes
            if (StringUtils.isEmpty(viewTypeValue))
            {
                viewType =
                    (String)CollectionUtils.find(
                        classifier.getAllGeneralizations(),
                        new Predicate()
                        {
                            public boolean evaluate(Object object)
                            {
                                return ((ModelElementFacade)object).findTaggedValue(
                                    HibernateProfile.TAGGEDVALUE_EJB_VIEWTYPE) != null;
                            }
                        });
            }
            if (StringUtils.isNotEmpty(viewTypeValue))
            {
                viewType = viewTypeValue;
            }
        }
        if (StringUtils.isEmpty(viewType))
        {
            viewType = defaultViewType;
        }
        return viewType.toLowerCase();
    }

    /**
     * Creates a fully qualified name from the given <code>packageName</code>,
     * <code>name</code>, and <code>suffix</code>.
     *
     * @param packageName the name of the model element package.
     * @param name the name of the model element.
     * @param suffix the suffix to append.
     * @return the new fully qualified name.
     */
    static String getFullyQualifiedName(
        String packageName,
        String name,
        String suffix)
    {
        StringBuffer fullyQualifiedName = new StringBuffer(StringUtils.trimToEmpty(packageName));
        if (StringUtils.isNotBlank(packageName))
        {
            fullyQualifiedName.append('.');
        }
        fullyQualifiedName.append(StringUtils.trimToEmpty(name));
        fullyQualifiedName.append(StringUtils.trimToEmpty(suffix));
        return fullyQualifiedName.toString();
    }

    /**
     * filters all static operations
     */
    static java.util.Collection filterBusinessOperations(Collection operations)
    {
        Collection businessOperations =
            new FilteredCollection(operations)
            {
                public boolean evaluate(Object object)
                {
                    return !((OperationFacade)object).isStatic();
                }
            };
        return businessOperations;
    }
}