package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute
 */
public class SpringCriteriaAttributeLogicImpl extends SpringCriteriaAttributeLogic
{
    // ---------------- constructor -------------------------------

    public SpringCriteriaAttributeLogicImpl(Object metaObject, String context)
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
            if (comparator.equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LIKE))
            {
                comparatorConstant = "LIKE_COMPARATOR";
            }
            else if (comparator.equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_EQUAL))
            {
                comparatorConstant = "EQUAL_COMPARATOR";
            }
            else if (comparator.equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER_OR_EQUAL))
            {
                comparatorConstant = "GREATER_THAN_OR_EQUAL_COMPARATOR";
            }
            else if (comparator.equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER))
            {
                comparatorConstant = "GREATER_THAN_COMPARATOR";
            }
            else if (comparator.equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS_OR_EQUAL))
            {
                comparatorConstant = "LESS_THAN_OR_EQUAL_COMPARATOR";
            }
            else if (comparator.equals(SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS))
            {
                comparatorConstant = "LESS_THAN_COMPARATOR";
            }
        }
        return comparatorConstant;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic#handleIsNullable()
     */
    protected boolean handleIsNullable()
    {
        boolean result = false;
        String value = StringUtils.trimToEmpty(
                (String) findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_CRITERIA_NULLABLE));
        if (!StringUtils.isEmpty(value))
        {
            result = BooleanUtils.toBoolean(value);
        }
        return result;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic#handleGetMatchMode()
     */
    protected String handleGetMatchMode()
    {
        String matchMode = null;
        Object value = findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_CRITERIA_MATCHMODE);
        if (value != null)
        {
            matchMode = String.valueOf(value);
        }
        String result = StringUtils.trimToEmpty(matchMode);
        return result;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic#handleGetMatchModeConstant()
     */
    protected String handleGetMatchModeConstant()
    {
        String matchMode = getMatchMode();
        String matchModeConstant = null;

        if (matchMode != null)
        {
            if (matchMode.equals(SpringProfile.TAGGEDVALUEVALUE_MATCHMODE_ANYWHERE))
            {
                matchModeConstant = "ANYWHERE";
            }
            else if (matchMode.equals(SpringProfile.TAGGEDVALUEVALUE_MATCHMODE_END))
            {
                matchModeConstant = "END";
            }
            else if (matchMode.equals(SpringProfile.TAGGEDVALUEVALUE_MATCHMODE_EXACT))
            {
                matchModeConstant = "EXACT";
            }
            else if (matchMode.equals(SpringProfile.TAGGEDVALUEVALUE_MATCHMODE_START))
            {
                matchModeConstant = "START";
            }
        }
        return matchModeConstant;
    }

    protected boolean handleIsMatchModePresent()
    {
        return !StringUtils.isEmpty(getMatchMode());
    }

}