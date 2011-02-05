package org.andromda.cartridges.hibernate.metafacades;

import java.util.Collection;
import org.andromda.cartridges.hibernate.HibernateProfile;
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
     * @param classifier
     * @param defaultViewType
     * @return String the view type name.
     */
    static String getViewType(
        ClassifierFacade classifier,
        String defaultViewType)
    {
        ExceptionUtils.checkNull("classifer", classifier);
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
            if (StringUtils.isNotBlank(viewTypeValue))
            {
                viewType = viewTypeValue;
            }
        }
        if (StringUtils.isEmpty(viewType) || viewType==null)
        {
            viewType = defaultViewType;
        }
        return viewType.toLowerCase();
    }

    /**
     * Creates a fully qualified Hibernate name from the given <code>packageName</code>,
     * <code>name</code>, and <code>suffix</code>. Not to be used for attributes, types, or parameters
     * which could possibly be java.lang. types.
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
        StringBuilder fullyQualifiedName = new StringBuilder(StringUtils.trimToEmpty(packageName));
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
     * @param operations
     * @return businessOperations
     */
    static Collection filterBusinessOperations(Collection operations)
    {
        Collection businessOperations =
            new FilteredCollection(operations)
            {
                private static final long serialVersionUID = 34L;
                public boolean evaluate(Object object)
                {
                    return !((OperationFacade)object).isStatic();
                }
            };
        return businessOperations;
    }

    /**
     * Checks whether the passed in operation is a query and should be using named parameters.
     *
     * @param operation the operation.
     * @param defaultUseNamedParameters the default value.
     * @return whether named parameters should be used.
     */
    static boolean getUseNamedParameters(OperationFacade operation,
        boolean defaultUseNamedParameters)
    {
        ExceptionUtils.checkNull("operation", operation);
        boolean useNamedParameters = defaultUseNamedParameters;
        if (operation.isQuery())
        {
            String useNamedParametersValue = StringUtils.trimToEmpty((String)operation
                    .findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_USE_NAMED_PARAMETERS));
            if (StringUtils.isNotBlank(useNamedParametersValue))
            {
                useNamedParameters = Boolean.valueOf(useNamedParametersValue).booleanValue();
            }
        }
        return useNamedParameters;
    }

}