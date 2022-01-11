package org.andromda.cartridges.hibernate.metafacades;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Chad Brandon
 * @author Carlos Cuenca
 * @see HibernateFinderMethodLogic
 *      Metaclass facade implementation.
 */
public class HibernateFinderMethodLogicImpl
        extends HibernateFinderMethodLogic {
    private static final long serialVersionUID = 34L;

    // ---------------- constructor -------------------------------
    /**
     * @param metaObject
     * @param context
     */
    public HibernateFinderMethodLogicImpl(
            Object metaObject,
            String context) {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethod#getQuery()
     */
    @Override
    protected String handleGetQuery() {
        // first see if we can retrieve the query from the super class as an OCL
        // translation
        // String queryString = this.getTranslatedQuery();
        StringBuilder builder = new StringBuilder();

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(this.getTranslatedQuery())) {
            Object value = this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_QUERY);
            if (value != null) {
                // remove any excess whitespace
                builder.append(((String) value).replaceAll("[$\\s]+", " "));
            }
        } else {
            builder.append(this.getTranslatedQuery());
        }

        // if there wasn't any stored query, create one by default.
        if (builder.length() == 0) {

            String variableName = StringUtils.uncapitalize(this.getOwner().getName()).substring(0, 1);
            builder.append("SELECT " + variableName + " FROM " + this.getOwner().getName() + " AS " + variableName);

            Collection arguments = this.getArguments();

            if (arguments != null && !arguments.isEmpty()) {
                // Check if there is need for inner and add them
                Iterator argumentIt = arguments.iterator();
                for (; argumentIt.hasNext();) {
                    HibernateFinderMethodArgument argument = (HibernateFinderMethodArgument) argumentIt.next();

                    if (!CollectionUtils.isEmpty(argument.getType().getAttributes())) {

                        // Some arguments may be complex, but NOT a Criteria object
                        if (argument.getType().getStereotypeNames().toString().contains("Criteria")) {

                            Iterator<AttributeFacade> paramIt = argument.getType().getAttributes().iterator();
                            for (; paramIt.hasNext();) {
                                AttributeFacade attribute = paramIt.next();

                                if (!CollectionUtils.isEmpty(attribute.getType().getAttributes())) {
                                    builder.append(" INNER JOIN ");
                                    if (attribute instanceof HibernateCriteriaAttribute) {
                                        HibernateCriteriaAttribute criteriaAttribute = (HibernateCriteriaAttribute) attribute;
                                        if (StringUtils.isBlank(criteriaAttribute.getAttributeName())) {
                                            builder.append(variableName + '.' + criteriaAttribute.getName());
                                        } else {
                                            builder.append(variableName + '.' + criteriaAttribute.getAttributeName());
                                        }
                                    } else {
                                        builder.append(variableName + '.' + attribute.getName());
                                    }
                                }
                            }
                        } else {
                            builder.append(" INNER JOIN ");
                            builder.append(variableName + '.' + argument.getName());
                        }
                    }
                }

                builder.append(" WHERE");
                argumentIt = arguments.iterator();
                for (; argumentIt.hasNext();) {
                    HibernateFinderMethodArgument argument = (HibernateFinderMethodArgument) argumentIt.next();

                    if (CollectionUtils.isEmpty(argument.getType().getAttributes())) {

                        String parameter = "?";
                        if (this.isUseNamedParameters()) {
                            parameter = ':' + argument.getName();
                        }
                        builder.append(' ' + variableName + '.' + argument.getName() + " = " + parameter);
                        if (argumentIt.hasNext()) {
                            builder.append(" AND");
                        }
                    } else if (argument.getType().getStereotypeNames().toString().contains("Criteria")) { // Only expand
                                                                                                          // if this is
                                                                                                          // a criteria
                        Iterator<AttributeFacade> paramIt = argument.getType().getAttributes().iterator();
                        for (; paramIt.hasNext();) {
                            AttributeFacade attribute = paramIt.next();

                            // Do not add complex objects from the WHERE part of the query
                            // if(!CollectionUtils.isEmpty(attribute.getType().getAttributes())) {
                            // continue;
                            // }

                            if (attribute instanceof HibernateCriteriaAttribute) {
                                HibernateCriteriaAttribute criteriaAttribute = (HibernateCriteriaAttribute) attribute;
                                String columnName = variableName;

                                if (StringUtils.isBlank(criteriaAttribute.getAttributeName())) {
                                    columnName = columnName + '.' + criteriaAttribute.getName();
                                } else {
                                    columnName = columnName + '.' + criteriaAttribute.getAttributeName();
                                }
                                builder.append(" (:");
                                builder.append(criteriaAttribute.getName());
                                builder.append(" IS NULL OR ");
                                builder.append(columnName);

                                if (criteriaAttribute.isComparatorPresent()) {
                                    if (criteriaAttribute.getComparatorConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_EQUAL)) {
                                        builder.append(" = ");
                                    } else if (criteriaAttribute.getComparatorConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER)) {
                                        builder.append(" > ");
                                    } else if (criteriaAttribute.getComparatorConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER_OR_EQUAL)) {
                                        builder.append(" >= ");
                                    } else if (criteriaAttribute.getComparatorConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_IN)) {
                                        builder.append(" IN ");
                                    } else if (criteriaAttribute.getComparatorConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS)) {
                                        builder.append(" < ");
                                    } else if (criteriaAttribute.getComparatorConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS_OR_EQUAL)) {
                                        builder.append(" <= ");
                                    } else if (criteriaAttribute.getComparatorConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LIKE)) {
                                        builder.append(" LIKE ");
                                    } else {
                                        builder.append(" != ");
                                    }
                                } else {
                                    builder.append(" = ");
                                }

                                if (criteriaAttribute.isMatchModePresent()) {
                                    if (criteriaAttribute.getMatchModeConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_END)) {
                                        builder.append("% + :");
                                        builder.append(criteriaAttribute.getName());

                                    } else if (criteriaAttribute.getMatchModeConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_ANYWHERE)) {
                                        builder.append("% + :");
                                        builder.append(criteriaAttribute.getName());
                                        builder.append(" + %");
                                    } else if (criteriaAttribute.getMatchModeConstant()
                                            .equals(HibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_EXACT)) {
                                        builder.append(":");
                                        builder.append(criteriaAttribute.getName());
                                    } else {
                                        builder.append(":");
                                        builder.append(criteriaAttribute.getName());
                                        builder.append(" + %");
                                    }
                                } else {
                                    builder.append(":");
                                    builder.append(criteriaAttribute.getName());
                                }

                                builder.append(")");

                            } else {

                                String parameter = "?";
                                if (this.isUseNamedParameters()) {
                                    parameter = ':' + attribute.getName();
                                }
                                builder.append(' ' + variableName + '.' + attribute.getName() + " = " + parameter);
                            }

                            if (paramIt.hasNext()) {
                                builder.append(" AND");
                            }
                        }
                    }
                }
            }
        }

        return builder.toString();
    }

    /**
     * Stores the translated query so that its only translated once.
     */
    private String translatedQuery = null;

    /**
     * Retrieves the translated query.
     */
    private String getTranslatedQuery() {
        if (StringUtils.isBlank(this.translatedQuery)) {
            this.translatedQuery = super.getQuery("query.Hibernate-QL");
        }
        return this.translatedQuery;
    }

    /**
     * Stores whether or not named parameters should be used in hibernate
     * queries.
     */
    private static final String USE_NAMED_PARAMETERS = "hibernateQueryUseNamedParameters";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethod#isUseNamedParameters()
     */
    @Override
    protected boolean handleIsUseNamedParameters() {
        boolean useNamedParameters = Boolean.valueOf(String.valueOf(this.getConfiguredProperty(USE_NAMED_PARAMETERS)))
                .booleanValue()
                || StringUtils.isNotBlank(this.getTranslatedQuery());
        return HibernateMetafacadeUtils.getUseNamedParameters(this, useNamedParameters);
    }

    /**
     * Stores the value indicating whether or not to use hibernate query
     * caching.
     */
    private static final String HIBERNATE_USE_QUERY_CACHE = "hibernateUseQueryCache";

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.HibernateFinderMethod#isUseQueryCache()
     */
    @Override
    protected boolean handleIsUseQueryCache() {
        boolean useQueryCache = Boolean.valueOf(String.valueOf(this.getConfiguredProperty(HIBERNATE_USE_QUERY_CACHE)))
                .booleanValue();

        if (useQueryCache) {
            useQueryCache = Boolean.valueOf(
                    String.valueOf(findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_USE_QUERY_CACHE)))
                    .booleanValue();
        }
        return useQueryCache;
    }

    @Override
    protected ParameterFacade handleGetCriteriaArgument() {

        ParameterFacade foundParameter = null;
        for (final ParameterFacade parameter : this.getParameters()) {
            final ClassifierFacade type = parameter.getType();
            if (type != null && type.hasStereotype(UMLProfile.STEREOTYPE_CRITERIA)) {
                foundParameter = parameter;
                break;
            }
        }
        return foundParameter;
    }

    @Override
    protected boolean handleIsCriteriaFinder() {
        return this.getCriteriaArgument() != null;
    }

    @Override
    protected String handleGetQuery(HibernateEntity entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String handleGetPredicates() {
        // first see if we can retrieve the query from the super class as an OCL
        // translation
        // String queryString = this.getTranslatedQuery();
        StringBuilder builder = new StringBuilder();

        // Collection arguments = this.getArguments();

        // if (arguments != null && !arguments.isEmpty()) {
        //     // Check if there is need for inner and add them
        //     Iterator argumentIt = arguments.iterator();

        //     for (; argumentIt.hasNext();) {
        //         HibernateFinderMethodArgument argument = (HibernateFinderMethodArgument) argumentIt.next();

        //         if (CollectionUtils.isEmpty(argument.getType().getAttributes())) {

        //             String parameter = "?";
        //             builder.append("if(");
        //             if (this.isUseNamedParameters()) {
        //                 parameter = ':' + argument.getName();
        //             }
        //             builder.append(' ' + variableName + '.' + argument.getName() + " = " + parameter);
        //             if (argumentIt.hasNext()) {
        //                 builder.append(" AND");
        //             }

        //             builder.append(parameter);
        //             builder.append(" == null) {\n");
        //             //builder.append(s)
        //             builder.append("}\n");


        //         } else if (argument.getType().getStereotypeNames().toString().contains("Criteria")) { // Only expand if
        //                                                                                               // this is a
        //                                                                                               // criteria
        //             Iterator<AttributeFacade> paramIt = argument.getType().getAttributes().iterator();
        //             for (; paramIt.hasNext();) {
        //                 AttributeFacade attribute = paramIt.next();

        //                 // Do not add complex objects from the WHERE part of the query
        //                 // if(!CollectionUtils.isEmpty(attribute.getType().getAttributes())) {
        //                 // continue;
        //                 // }

        //                 if (attribute instanceof HibernateCriteriaAttribute) {
        //                     HibernateCriteriaAttribute criteriaAttribute = (HibernateCriteriaAttribute) attribute;
        //                     String columnName = variableName;

        //                     if (StringUtils.isBlank(criteriaAttribute.getAttributeName())) {
        //                         columnName = columnName + '.' + criteriaAttribute.getName();
        //                     } else {
        //                         columnName = columnName + '.' + criteriaAttribute.getAttributeName();
        //                     }
        //                     builder.append(" (:");
        //                     builder.append(criteriaAttribute.getName());
        //                     builder.append(" IS NULL OR ");
        //                     builder.append(columnName);

        //                     if (criteriaAttribute.isComparatorPresent()) {
        //                         if (criteriaAttribute.getComparatorConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_EQUAL)) {
        //                             builder.append(" = ");
        //                         } else if (criteriaAttribute.getComparatorConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER)) {
        //                             builder.append(" > ");
        //                         } else if (criteriaAttribute.getComparatorConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER_OR_EQUAL)) {
        //                             builder.append(" >= ");
        //                         } else if (criteriaAttribute.getComparatorConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_IN)) {
        //                             builder.append(" IN ");
        //                         } else if (criteriaAttribute.getComparatorConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS)) {
        //                             builder.append(" < ");
        //                         } else if (criteriaAttribute.getComparatorConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS_OR_EQUAL)) {
        //                             builder.append(" <= ");
        //                         } else if (criteriaAttribute.getComparatorConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LIKE)) {
        //                             builder.append(" LIKE ");
        //                         } else {
        //                             builder.append(" != ");
        //                         }
        //                     } else {
        //                         builder.append(" = ");
        //                     }

        //                     if (criteriaAttribute.isMatchModePresent()) {
        //                         if (criteriaAttribute.getMatchModeConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_END)) {
        //                             builder.append("% + :");
        //                             builder.append(criteriaAttribute.getName());

        //                         } else if (criteriaAttribute.getMatchModeConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_ANYWHERE)) {
        //                             builder.append("% + :");
        //                             builder.append(criteriaAttribute.getName());
        //                             builder.append(" + %");
        //                         } else if (criteriaAttribute.getMatchModeConstant()
        //                                 .equals(HibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_EXACT)) {
        //                             builder.append(":");
        //                             builder.append(criteriaAttribute.getName());
        //                         } else {
        //                             builder.append(":");
        //                             builder.append(criteriaAttribute.getName());
        //                             builder.append(" + %");
        //                         }
        //                     } else {
        //                         builder.append(":");
        //                         builder.append(criteriaAttribute.getName());
        //                     }

        //                     builder.append(")");

        //                 } else {

        //                     String parameter = "?";
        //                     if (this.isUseNamedParameters()) {
        //                         parameter = ':' + attribute.getName();
        //                     }
        //                     builder.append(' ' + variableName + '.' + attribute.getName() + " = " + parameter);
        //                 }

        //                 if (paramIt.hasNext()) {
        //                     builder.append(" AND");
        //                 }
        //             }
        //         }
        //     }
        // }

        return builder.toString();
    }
}
