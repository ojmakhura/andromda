package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttribute
 */
public class SpringCriteriaAttributeLogicImpl
    extends SpringCriteriaAttributeLogic
{
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
            if (SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LIKE.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "LIKE_COMPARATOR";
            }
            else if (SpringProfile.TAGGEDVALUEVALUE_INSENSITIVE_LIKE_COMPARATOR.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "INSENSITIVE_LIKE_COMPARATOR";
            }
            else if (SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_EQUAL.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "EQUAL_COMPARATOR";
            }
            else if (SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER_OR_EQUAL.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "GREATER_THAN_OR_EQUAL_COMPARATOR";
            }
            else if (SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_GREATER.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "GREATER_THAN_COMPARATOR";
            }
            else if (SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS_OR_EQUAL.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "LESS_THAN_OR_EQUAL_COMPARATOR";
            }
            else if (SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_LESS.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "LESS_THAN_COMPARATOR";
            }
            else if (SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_IN.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "IN_COMPARATOR";
            }
            else if (SpringProfile.TAGGEDVALUEVALUE_COMPARATOR_NOT_EQUAL.equalsIgnoreCase(comparator))
            {
                comparatorConstant = "NOT_EQUAL_COMPARATOR";
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
        String value =
            StringUtils.trimToEmpty((String)findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_CRITERIA_NULLABLE));
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

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic#handleIsMatchModePresent()
     */
    protected boolean handleIsMatchModePresent()
    {
        return !StringUtils.isEmpty(getMatchMode());
    }

    private static final String ORDER_UNSET = "ORDER_UNSET";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic#handleIsOrderable()
     */
    protected boolean handleIsOrderable()
    {
        return !ORDER_UNSET.equals(getOrderDirection());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic#handleGetOrderDirection()
     */
    protected String handleGetOrderDirection()
    {
        String result = ORDER_UNSET;
        String value =
            StringUtils.trimToEmpty(
                (String)findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_CRITERIA_ORDER_DIRECTION));
        if (!StringUtils.isEmpty(value))
        {
            if (value.equals(SpringProfile.TAGGEDVALUEVALUE_ORDER_ASCENDING))
            {
                result = "ORDER_ASC";
            }
            else if (value.equals(SpringProfile.TAGGEDVALUEVALUE_ORDER_DESCENDING))
            {
                result = "ORDER_DESC";
            }
        }
        return result;
    }

    /**
     * Used for undefined states of the criteria ordering.
     */
    private static final int UNSET = -1;

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic#handleGetOrderRelevance()
     */
    protected int handleGetOrderRelevance()
    {
        int result = UNSET;
        String value =
            StringUtils.trimToEmpty(
                (String)findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_CRITERIA_ORDER_RELEVANCE));
        if (!StringUtils.isEmpty(value))
        {
            result = Integer.parseInt(value);
        }
        return result;
    }

    /**
     * The default value for whether hibernate criteria arguments are case insensitive or not.
     */
    private static final String HIBERNATE_CRITERIA_QUERY_IGNORE_CASE = "hibernateCriteriaQueryIgnoreCase";

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaAttributeLogic#isIgnoreCase()
     */
    protected boolean handleIsIgnoreCase()
    {
        Object value = this.findTaggedValue(SpringProfile.TAGGEDVALUE_HIBERNATE_CRITERIA_COMPARATOR_IGNORE_CASE);
        if (value == null)
        {
            value = this.getConfiguredProperty(HIBERNATE_CRITERIA_QUERY_IGNORE_CASE);
        }
        return Boolean.valueOf(ObjectUtils.toString(value)).booleanValue();
    }
}