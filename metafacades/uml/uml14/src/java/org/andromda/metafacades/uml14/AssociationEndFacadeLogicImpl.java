package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKind;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class AssociationEndFacadeLogicImpl
       extends AssociationEndFacadeLogic
       implements org.andromda.metafacades.uml.AssociationEndFacade
{
    // ---------------- constructor -------------------------------
    
    public AssociationEndFacadeLogicImpl (org.omg.uml.foundation.core.AssociationEnd metaObject, String context)
    {
        super (metaObject, context);
    } 

    /*** 
     * @see org.andromda.metafacades.uml.AssociationEndFacadeLogic#handleGetOtherEnd()
     */
    public Object handleGetOtherEnd()
    {
        Collection ends = metaObject.getAssociation().getConnection();
        for (Iterator i = ends.iterator(); i.hasNext();)
        {
            AssociationEnd ae = (AssociationEnd) i.next();
            if (!metaObject.equals(ae))
            {
                return ae;
            }
        }

        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName()
    {
        String name = super.getName();
        //if name is empty, then get the name from the type
        if (StringUtils.isEmpty(name))
        {
            ClassifierFacade type = this.getType();
            name =
                StringUtils.uncapitalize(
                    StringUtils.trimToEmpty(type.getName()));
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacadeLogic#handleGetType()
     */
    public Object handleGetType()
    {
        return metaObject.getParticipant();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2Many()
     */
    public boolean isOne2Many()
    {
        return !this.isMany() && this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2Many()
     */
    public boolean isMany2Many()
    {
        return this.isMany()
            && this. getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2One()
     */
    public boolean isOne2One()
    {
        return !this.isMany()
            && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2One()
     */
    public boolean isMany2One()
    {
        return this.isMany()
            && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany()
     */
    public boolean isMany() {
        boolean isMultiple = false;
        Multiplicity multiplicity = this.metaObject.getMultiplicity();
        //we'll say a null multiplicity is 1
        if (multiplicity != null) {
            Collection ranges = multiplicity.getRange();
            if (ranges != null && !ranges.isEmpty()) {
                Iterator rangeIt = ranges.iterator();
                while (rangeIt.hasNext()) {
                    MultiplicityRange multiplicityRange =
                        (MultiplicityRange) rangeIt.next();
                    int upper = multiplicityRange.getUpper();
                    int lower = multiplicityRange.getLower();
                    int rangeSize = upper - lower;
                    if (upper > 1) {
                        isMultiple = true;
                    } else if (rangeSize < 0) {
                        isMultiple = true;
                    } else {
                        isMultiple = false;
                    }
                }
            }
        }
        return isMultiple;
    }   

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isOrdered()
     */
    public boolean isOrdered()
    {
        boolean ordered = false;

        OrderingKind ordering = metaObject.getOrdering();
        //no ordering is 'unordered'
        if (ordering != null)
        {
            ordered = ordering.equals(OrderingKindEnum.OK_ORDERED);
        }

        return ordered;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isAggregation()
     */
    public boolean isAggregation()
    {
        return AggregationKindEnum.AK_AGGREGATE.equals(
            metaObject.getAggregation());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isComposition()
     */
    public boolean isComposition()
    {
        return AggregationKindEnum.AK_COMPOSITE.equals(
            metaObject.getAggregation());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return ChangeableKindEnum.CK_FROZEN.equals(
            metaObject.getChangeability());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isNavigable()
     */
    public boolean isNavigable()
    {
        return metaObject.isNavigable();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterName()
     */
    public java.lang.String getGetterName()
    {
        return "get" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getSetterName()
     */
    public java.lang.String getSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    // relations

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getAssociation()
     */
    protected Object handleGetAssociation()
    {
        return metaObject.getAssociation();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        // if many, then list or collection
        if (isOne2Many() || isMany2Many())
        {
            Mappings lm = getLanguageMappings();
            return isOrdered()
                ? lm.getTo("datatype.List")
                : lm.getTo("datatype.Collection");
        }
        
        // if single element, then return the type
        return getOtherEnd().getType().getFullyQualifiedName();
    }
    
    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    public boolean isRequired() {
		int lower = this.getMultiplicityRangeLower();
		return lower >= 1;
    }
    
    /**
     * Returns the lower range of the multiplicty for the 
     * passed in associationEnd
     * @return int the lower range of the multiplicty or 1 if it
     *         isn't defined.
     */
    private int getMultiplicityRangeLower() {
    	int lower = 1;
    	Multiplicity multiplicity = this.metaObject.getMultiplicity();
    	if (multiplicity != null) {
	    	Collection ranges = multiplicity.getRange();
	    	if (ranges != null && !ranges.isEmpty()) {
	    		Iterator rangeIt = ranges.iterator();
	    		while (rangeIt.hasNext()) {
	    			MultiplicityRange multiplicityRange =
	    				(MultiplicityRange) rangeIt.next();
	    			lower = multiplicityRange.getLower();
	    		}
	    	}
    	}
    	return lower;
    }

}
