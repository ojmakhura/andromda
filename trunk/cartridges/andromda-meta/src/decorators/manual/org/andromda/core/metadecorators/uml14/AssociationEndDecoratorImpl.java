package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.MultiplicityRange;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.AssociationEnd
 *
 *
 */
public class AssociationEndDecoratorImpl extends AssociationEndDecorator
{
    // ---------------- constructor -------------------------------

    public AssociationEndDecoratorImpl(
        org.omg.uml.foundation.core.AssociationEnd metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class AssociationEndDecorator ...

    public org
        .andromda
        .core
        .metadecorators
        .uml14
        .AssociationEndDecorator getOtherEnd()
    {
        AssociationEnd otherEnd;

        Collection ends = metaObject.getAssociation().getConnection();
        for (Iterator i = ends.iterator(); i.hasNext();)
        {
            AssociationEnd ae = (AssociationEnd) i.next();
            if (!metaObject.equals(ae))
            {
                return (AssociationEndDecorator) decoratedElement(ae);
            }
        }

        return null;
    }

    public java.lang.String getRoleName()
    {
        String roleName = metaObject.getName();
        if ((roleName == null) || (roleName.length() == 0))
        {
            roleName = "the" + metaObject.getParticipant().getName();
        }

        return roleName;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#getType()
     */
    public org
        .andromda
        .core
        .metadecorators
        .uml14
        .ClassifierDecorator getType()
    {
        return (ClassifierDecorator) DecoratorFactory
            .getInstance()
            .createDecoratorObject(metaObject.getParticipant());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#getId()
     */
    public String getId()
    {
        return metaObject.refMofId();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#getSource()
     */
    public AssociationEndDecorator getSource()
    {
        return this;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#getTarget()
     */
    public AssociationEndDecorator getTarget()
    {
        return getOtherEnd();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isOne2Many()
     */
    public boolean isOne2Many()
    {
        return !isMany(metaObject) && isMany(getOtherEnd());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isMany2Many()
     */
    public boolean isMany2Many()
    {
        return isMany(metaObject) && isMany(getOtherEnd());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isOne2One()
     */
    public boolean isOne2One()
    {
        return !isMany(metaObject) && !isMany(getOtherEnd());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isMany2One()
     */
    public boolean isMany2One()
    {
        return isMany(metaObject) && !isMany(getOtherEnd());
    }

    static protected boolean isMany(AssociationEnd ae)
    {
        Collection ranges = ae.getMultiplicity().getRange();

        for (Iterator i = ranges.iterator(); i.hasNext();)
        {
            MultiplicityRange range = (MultiplicityRange) i.next();
            if (range.getUpper() > 1)
            {
                return true;
            }

            int rangeSize = range.getUpper() - range.getLower();
            if (rangeSize < 0)
            {
                return true;
            }

        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isAggregation()
     */
    public boolean isAggregation()
    {
        return AggregationKindEnum.AK_AGGREGATE.equals(
            metaObject.getAggregation());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndDecorator#isComposition()
     */
    public boolean isComposition()
    {
        return AggregationKindEnum.AK_COMPOSITE.equals(
            metaObject.getAggregation());
    }

    // ------------- relations ------------------

}
