package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute
 */
public class SpringCriteriaAttributeLogicImpl
    extends SpringCriteriaAttributeLogic
{
    // ---------------- constructor -------------------------------

    public SpringCriteriaAttributeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute#getAttributeName()
     */
    protected java.lang.String handleGetAttributeName()
    {
        // use the attribute name by default
        String attributeName = getName();

        // if there is a tagged value, use it instead
        Object value = findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_CRITERIA_ATTRIBUTE);
        if (value != null)
        {
            attributeName = String.valueOf(value);
        }

        return StringUtils.trimToEmpty(attributeName);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute#getComparator()
     */
    protected java.lang.String handleGetComparator()
    {
        String comparator = null;
        Object value = findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_CRITERIA_COMPARATOR);
        if (value != null)
        {
            comparator = String.valueOf(value);
        }
        return StringUtils.trimToEmpty(comparator);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute#isComparatorPresent()
     */
    protected boolean handleIsComparatorPresent()
    {
        return !StringUtils.isEmpty(getComparator());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute#getComparatorConstant()
     */
    protected String handleGetComparatorConstant()
    {
        String comparator = getComparator();
        String comparatorConstant = null;

        if (comparator != null)
        {
            if (comparator
                .equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LIKE))
            {
                comparatorConstant = "LIKE_COMPARATOR";
            }
            else if (comparator
                .equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_EQUAL))
            {
                comparatorConstant = "EQUALS_COMPARATOR";
            }
            else if (comparator
                .equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER_OR_EQUAL))
            {
                comparatorConstant = "GREATER_THAN_OR_EQUAL_COMPARATOR";
            }
            else if (comparator
                .equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER))
            {
                comparatorConstant = "GREATER_THAN_COMPARATOR";
            }
            else if (comparator
                .equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS_OR_EQUAL))
            {
                comparatorConstant = "LESS_THAN_OR_EQUAL_COMPARATOR";
            }
            else if (comparator
                .equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS))
            {
                comparatorConstant = "LESS_THAN_COMPARATOR";
            }
        }
        return comparatorConstant;
    }

}