package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

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

    /**
     * This method is overridden to make sure the parameter 
     * name will result in uncompilable Java code.
     */
    public String handleGetName()
    {
        return StringUtils.deleteWhitespace(super.getName());
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class AttributeFacade ...
    
    
    public java.lang.String handleGetGetterName()
    {
        return "get" + StringUtils.capitalize(metaObject.getName());
    }

    public java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(metaObject.getName());
    }

    public String handleGetDefaultValue()
    {
        String defaultValue = null;
        if (this.metaObject.getInitialValue() != null) 
        {
            defaultValue = 
                this.metaObject.getInitialValue().getBody();
        }
        return defaultValue;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getType()
     */
    protected Object handleGetType()
    {
        return metaObject.getType();
    }

    /**
     * @see org.andromda.core.metadecorators.uml.AssociationEndFacade#getOwner()
     */
    public Object handleGetOwner()
    {
        return this.metaObject.getOwner();
    }

    /**
     * @see org.andromda.core.metadecorators.uml.AssociationEndFacade#isReadOnly()
     */
    public boolean handleIsReadOnly()
    {
        return ChangeableKindEnum.CK_FROZEN.equals(metaObject.getChangeability());
    }

    /**
     * @see org.andromda.core.metadecorators.uml.AttributeFacade#isStatic()
     */
    public boolean handleIsStatic()
    {
        return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject.getOwnerScope());
    }

    /**
     * @see org.andromda.core.metadecorators.uml.AttributeFacade#findTaggedValue(java.lang.String, boolean)
     */
    public Object handleFindTaggedValue(String name, boolean follow)
    {
        name = StringUtils.trimToEmpty(name);
        Object value = findTaggedValue(name);
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
	public boolean handleIsRequired() {
		int lower = this.getMultiplicityRangeLower();
		return lower >= 1;
    }

	/**
	 * Returns the lower range of the multiplicty for the
	 * passed in associationEnd
	 * @return int the lower range of the multiplicty or 1 if
	 *         it isn't defined.
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
