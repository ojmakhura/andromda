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
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getOtherEnd()
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
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getName()
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
     * @see org.andromda.metafacades.uml.ModelElementFacade#getType()
     */
    protected Object handleGetType()
    {
        return metaObject.getParticipant();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2Many()
     */
    public boolean handleIsOne2Many()
    {
        return !this.isMany() && this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2Many()
     */
    public boolean handleIsMany2Many()
    {
        return this.isMany()
            && this. getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2One()
     */
    public boolean handleIsOne2One()
    {
        return !this.isMany()
            && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2One()
     */
    public boolean handleIsMany2One()
    {
        return this.isMany()
            && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany()
     */
    public boolean handleIsMany() {
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
    public boolean handleIsOrdered()
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
    public boolean handleIsAggregation()
    {
        return AggregationKindEnum.AK_AGGREGATE.equals(
            metaObject.getAggregation());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isComposition()
     */
    public boolean handleIsComposition()
    {
        return AggregationKindEnum.AK_COMPOSITE.equals(
            metaObject.getAggregation());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isReadOnly()
     */
    public boolean handleIsReadOnly()
    {
        return ChangeableKindEnum.CK_FROZEN.equals(
            metaObject.getChangeability());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isNavigable()
     */
    public boolean handleIsNavigable()
    {
        return metaObject.isNavigable();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterName()
     */
    public java.lang.String handleGetGetterName()
    {
        return "get" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getSetterName()
     */
    public java.lang.String handleGetSetterName()
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
    public String handleGetGetterSetterTypeName()
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
    public boolean handleIsRequired() {
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
