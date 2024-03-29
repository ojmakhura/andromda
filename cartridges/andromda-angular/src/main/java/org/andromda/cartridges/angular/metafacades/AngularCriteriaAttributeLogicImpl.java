// license-header java merge-point
//
// Generated by: MetafacadeLogicImpl.vsl in andromda-meta-cartridge.
package org.andromda.cartridges.angular.metafacades;

/**
 * Represents a criteria search attribute.
 * MetafacadeLogic implementation for org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute.
 *
 * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute
 */
public class AngularCriteriaAttributeLogicImpl
    extends AngularCriteriaAttributeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * Public constructor for AngularCriteriaAttributeLogicImpl
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute
     */
    public AngularCriteriaAttributeLogicImpl (Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Returns the name of the attribute to be used in the criteria query.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#getAttributeName()
     */
    protected String handleGetAttributeName()
    {
        // TODO put your implementation here.
        return null;
    }

    /**
     * Returns the comparator for the attribute.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#getComparator()
     */
    protected String handleGetComparator()
    {
        // TODO put your implementation here.
        return null;
    }

    /**
     * Indicates whether or not the attribute has a tagged value for the comparator.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#isComparatorPresent()
     */
    protected boolean handleIsComparatorPresent()
    {
        // TODO put your implementation here.
        return false;
    }

    /**
     * The comparator as a constant expression usable in the template.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#getComparatorConstant()
     */
    protected String handleGetComparatorConstant()
    {
        // TODO put your implementation here.
        return null;
    }

    /**
     * Defines whether the underlying attribute on the queried entity may be NULL and should
     * therefore be included in the search.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#isNullable()
     */
    protected boolean handleIsNullable()
    {
        // TODO put your implementation here.
        return false;
    }

    /**
     * Returns the hibernate matchmode constant to use for matching Strings.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#getMatchMode()
     */
    protected String handleGetMatchMode()
    {
        // TODO put your implementation here.
        return null;
    }

    /**
     * Returns the matchmode constant for usage in templates.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#getMatchModeConstant()
     */
    protected String handleGetMatchModeConstant()
    {
        // TODO put your implementation here.
        return null;
    }

    /**
     * Returns <code>true</code> if a matchmode has ben set.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#isMatchModePresent()
     */
    protected boolean handleIsMatchModePresent()
    {
        // TODO put your implementation here.
        return false;
    }

    /**
     * Returns true if the attribute is used to order the result set.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#isOrderable()
     */
    protected boolean handleIsOrderable()
    {
        // TODO put your implementation here.
        return false;
    }

    /**
     * Returns the order direction if it has been set.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#getOrderDirection()
     */
    protected String handleGetOrderDirection()
    {
        // TODO put your implementation here.
        return null;
    }

    /**
     * Returns the relevance of the ordering setting. The lower the number, the more relevant it is.
     * @see org.andromda.cartridges.angular.metafacades.AngularCriteriaAttribute#getOrderRelevance()
     */
    protected int handleGetOrderRelevance()
    {
        // TODO put your implementation here.
        return 0;
    }
}