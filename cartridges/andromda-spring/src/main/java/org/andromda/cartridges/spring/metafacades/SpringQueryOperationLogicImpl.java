package org.andromda.cartridges.spring.metafacades;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.hibernate.CartridgeHibernateProfile;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringQueryOperation.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation
 */
public class SpringQueryOperationLogicImpl
        extends SpringQueryOperationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * Public constructor for SpringQueryOperationLogicImpl
     * @param metaObject
     * @param context
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation
     */
    public SpringQueryOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getQuery((SpringEntity)null)
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperationLogic#getQuery()
     */
    protected String handleGetQuery()
    {
        this.ar
        return this.getQuery((SpringEntity)null);
    }

    /**
     * Stores the translated query so that its only translated once.
     */
    private String translatedQuery = null;

    /**
     * Retrieves the translated query.
     */
    private String getTranslatedQuery()
    {
        if (this.translatedQuery == null)
        {
            this.translatedQuery = super.getQuery("query.Hibernate-QL");
        }
        return this.translatedQuery;
    }

    /**
     * Stores whether or not named parameters should be used in hibernate queries.
     */
    private static final String USE_NAMED_PARAMETERS = "hibernateQueryUseNamedParameters";

    /**
     * @return SpringMetafacadeUtils.getUseNamedParameters(this, useNamedParameters)
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperationLogic#isUseNamedParameters()
     */
    protected boolean handleIsUseNamedParameters()
    {
        boolean useNamedParameters = Boolean.valueOf(String.valueOf(this.getConfiguredProperty(USE_NAMED_PARAMETERS)))
                .booleanValue()
                || StringUtils.isNotBlank(this.getTranslatedQuery());

        return SpringMetafacadeUtils.getUseNamedParameters(this, useNamedParameters);
    }

    /**
     * @return getCriteriaArgument() != null
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation#isCriteriaFinder()
     */
    protected boolean handleIsCriteriaFinder()
    {
        return this.getCriteriaArgument() != null;
    }

    /**
     * @return Parameter with UMLProfile.STEREOTYPE_CRITERIA
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation#getCriteriaArgument()
     */
    protected ParameterFacade handleGetCriteriaArgument()
    {
        ParameterFacade foundParameter = null;
        for (final ParameterFacade parameter : this.getParameters())
        {
            final ClassifierFacade type = parameter.getType();
            if (type != null && type.hasStereotype(UMLProfile.STEREOTYPE_CRITERIA))
            {
                foundParameter = parameter;
                break;
            }
        }
        return foundParameter;
    }

    /**
     * @param entity
     * @return query
     * @see org.andromda.cartridges.spring.metafacades.SpringQueryOperation#getQuery(org.andromda.cartridges.spring.metafacades.SpringEntity)
     */
    protected String handleGetQuery(SpringEntity entity)
    {
        // first see if we can retrieve the query from the super class as an OCL
        // translation
        String queryString = this.getTranslatedQuery();
        StringBuilder builder = new StringBuilder();

        // otherwise see if there is a query stored as a tagged value
        if (StringUtils.isEmpty(queryString)) {
            Object value = this.findTaggedValue(CartridgeHibernateProfile.TAGGEDVALUE_HIBERNATE_QUERY);
            if (value != null) {
                // remove any excess whitespace
                builder.append('\"' + ((String) value).replaceAll("[$\\s]+", " ") + '\"');
            }
        } else {
            builder.append('\"' + queryString + '\"');
        }

        // if there wasn't any stored query, create one by default.
        // if (builder.length() == 0) {

        //     String variableName = StringUtils.uncapitalize(this.getOwner().getName()).substring(0, 1);
        //     builder.append("\"SELECT " + variableName + " FROM " + this.getOwner().getName() + " AS " + variableName);

        //     Collection arguments = this.getArguments();

        //     if (arguments != null && !arguments.isEmpty()) {
        //         // Check if there is need for inner and add them
        //         Iterator argumentIt = arguments.iterator();

        //         builder.append(" \" +\n\t\t\t\t\"WHERE");
        //         for (; argumentIt.hasNext();) {
        //             ParameterFacade argument = (ParameterFacade) argumentIt.next();

        //             if (CollectionUtils.isEmpty(argument.getType().getAttributes())) {

        //                 String parameter = "?";
        //                 if (this.isUseNamedParameters()) {
        //                     parameter = ':' + argument.getName();
        //                 }
        //                 builder.append(' ' + variableName + '.' + argument.getName() + " = " + parameter + " \" ");
        //             } else if (argument.getType().getStereotypeNames().toString().contains("Criteria")) { 
                        
        //                 Iterator<AttributeFacade> paramIt = argument.getType().getAttributes().iterator();
        //                 for (; paramIt.hasNext();) {
        //                     AttributeFacade attribute = paramIt.next();

        //                     // Do not add complex objects from the WHERE part of the query
        //                     // if(!CollectionUtils.isEmpty(attribute.getType().getAttributes())) {
        //                     // continue;
        //                     // }
        //                     boolean insentive = false;

        //                     if (attribute instanceof SpringCriteriaAttribute) {
        //                         SpringCriteriaAttribute criteriaAttribute = (SpringCriteriaAttribute) attribute;
        //                         String columnName = variableName;

        //                         if (StringUtils.isBlank(criteriaAttribute.getAttributeName())) {
        //                             columnName = columnName + '.' + criteriaAttribute.getName();
        //                         } else {
        //                             columnName = columnName + '.' + criteriaAttribute.getAttributeName();
        //                         }

        //                         if (criteriaAttribute.isComparatorPresent() && criteriaAttribute.getComparator()
        //                             .equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_INSENSITIVE_LIKE_COMPARATOR)) {

        //                             insentive = true;
        //                         }
                                
        //                         builder.append(" (:");
        //                         builder.append(criteriaAttribute.getName());
        //                         builder.append(" IS NULL OR ");
        //                         builder.append(insentive ? "lower(" + columnName + ")" : columnName);

        //                         String comparator = " = ";
        //                         if (criteriaAttribute.isComparatorPresent()) {

        //                             String tmp = criteriaAttribute.getComparator();

        //                             if (tmp.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER)) {
        //                                 comparator = " > ";
        //                             } else if (tmp.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER_OR_EQUAL)) {
        //                                 comparator = " >= ";
        //                             } else if (tmp.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_IN)) {
        //                                 comparator = " IN ";
        //                             } else if (tmp
        //                                     .equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS)) {
        //                                 comparator = " < ";
        //                             } else if (tmp.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS_OR_EQUAL)) {
        //                                 comparator = " <= ";
        //                             } else if (tmp.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_COMPARATOR_LIKE) || 
        //                                     tmp.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_INSENSITIVE_LIKE_COMPARATOR)) {
        //                                 comparator = " LIKE ";
        //                             }
        //                         }
        //                         builder.append(comparator);
        //                         String q = ":" + criteriaAttribute.getName();

        //                         if (criteriaAttribute.isMatchModePresent()) {
                                    
        //                             String mode = criteriaAttribute.getMatchMode();

        //                             if (mode.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_END)) {
                                                
        //                                 q = "CONCAT('%', " + q + ')';

        //                             } else if (mode.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_ANYWHERE)) {
                                                
        //                                 q = "CONCAT('%', " + q + ", '%')";
        //                             } else if(mode.equals(CartridgeHibernateProfile.TAGGEDVALUEVALUE_MATCHMODE_START)){
                                                
        //                                 q = "CONCAT(" + q + ", '%')";
        //                             }
        //                         }

        //                         if(insentive) {
        //                             q = "lower(" + q + ")";
        //                         }

        //                         builder.append(q);

        //                         builder.append(") \"");

        //                     } else {
        //                         String parameter = "?";
        //                         if (this.isUseNamedParameters()) {
        //                             parameter = ':' + attribute.getName();
        //                         }

        //                         if(attribute.getType().isStringType()) {
        //                             builder.append(" lower(" + variableName + '.' + attribute.getName() + ") = lower(" + parameter + ") \"");
        //                         } else {
        //                             builder.append(' ' + variableName + '.' + attribute.getName() + " = " + parameter + " \"");
        //                         }
        //                     }

        //                     if (paramIt.hasNext()) {
        //                         builder.append(" +\n\t\t\t\t\"AND");
        //                     }
        //                 }
        //             } else if (argument.getType().getStereotypeNames().toString().contains("Entity")) { // We are dealing with an entity

        //                 // We get all the identifiers from the entity
        //                 SpringEntity ent = (SpringEntity) argument.getType();
        //                 Iterator<ModelElementFacade> it = ent.getIdentifiers().iterator();

        //                 while(it.hasNext()) {
        //                     ModelElementFacade element = it.next();
        //                     builder.append(' ' + variableName + '.' + argument.getName() + '.' + element.getName() + " = :" + argument.getName() + StringUtils.capitalize(element.getName()) + " \"");

        //                     if (it.hasNext()) {
        //                         builder.append("+\n\t\t\t\t\"AND");
        //                     }

        //                 }
        //             }

        //             if (argumentIt.hasNext()) {
        //                 builder.append("+\n\t\t\t\t\"AND");
        //             }
        //         }
        //     }
        // }

        return builder.toString();
    }
}