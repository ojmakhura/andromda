package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class AttributeFacadeLogicImpl
       extends AttributeFacadeLogic
       implements org.andromda.metafacades.uml.AttributeFacade
{
    // ---------------- constructor -------------------------------
    
    public AttributeFacadeLogicImpl (org.omg.uml.foundation.core.Attribute metaObject, String context)
    {
        super (metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class AttributeFacade ...

    public java.lang.String getGetterName()
    {
        return "get"
            + StringUtilsHelper.upperCaseFirstLetter(metaObject.getName());
    }

    public java.lang.String getSetterName()
    {
        return "set"
            + StringUtilsHelper.upperCaseFirstLetter(metaObject.getName());
    }

    public String getDefaultValue()
    {
        return metaObject.getInitialValue().getBody();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AttributeFacade#handleGetType()
     */
    protected Object handleGetType()
    {
        return metaObject.getType();
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#handleGetOwner()
     */
    public Object handleGetOwner() 
    {
        return this.metaObject.getOwner();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AssociationEndFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return ChangeableKindEnum.CK_FROZEN.equals(metaObject.getChangeability());
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AttributeFacade#isStatic()
     */
    public boolean isStatic() 
    {
        return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject.getOwnerScope());
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.AttributeFacade#findTaggedValue(java.lang.String, boolean)
     */
    public String findTaggedValue(String name, boolean follow) 
    {
        name = StringUtils.trimToEmpty(name);
        String value = findTaggedValue(name);
        if (follow) {
            ClassifierFacade type = this.getType();
            while (value == null && type != null) {
                value = type.findTaggedValue(name);
                type = (ClassifierFacade)type.getGeneralization();
            }
        }
        return value;
    }
    
    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
	public boolean isRequired() {
		int lower = this.getMultiplicityRangeLower();
		return lower >= 1;
    }	

	/**
	 * Returns the lower range of the multiplicty for the 
	 * passed in associationEnd
	 * @return int the lower range of the multiplicty or null if
	 *         it can't be retrieved
	 */
	private int getMultiplicityRangeLower() {
		int lower = 1;
		Multiplicity multiplicity = metaObject.getMultiplicity();
		// assume no multiplicity is 1
		if (multiplicity != null) {
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
		}
		return lower;
	}
}
