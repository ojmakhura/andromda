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
 * Metaclass facade implementation.
 */
public class AttributeFacadeLogicImpl
    extends AttributeFacadeLogic
    implements org.andromda.metafacades.uml.AttributeFacade
{
    // ---------------- constructor -------------------------------

    public AttributeFacadeLogicImpl(
        org.omg.uml.foundation.core.Attribute metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
    public java.lang.String handleGetGetterName()
    {
        String prefix = null;
        if (getType() != null)
        {
            prefix = UMLMetafacadeUtils.isType(
                getType(),
                UMLMetafacadeGlobals.BOOLEAN_TYPE_NAME) ? "is" : "get";
        }

        return StringUtils.trimToEmpty(prefix)
            + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getSetterName()
     */
    public java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(metaObject.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    public String handleGetDefaultValue()
    {
        String defaultValue = null;
        if (this.metaObject.getInitialValue() != null)
        {
            defaultValue = this.metaObject.getInitialValue().getBody();
        }
        return defaultValue;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isChangeable()
     */
    public boolean handleIsChangeable()
    {
        return ChangeableKindEnum.CK_CHANGEABLE.equals(metaObject
            .getChangeability());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isAddOnly()
     */
    public boolean handleIsAddOnly()
    {
        return ChangeableKindEnum.CK_ADD_ONLY.equals(metaObject
            .getChangeability());
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
        return ChangeableKindEnum.CK_FROZEN.equals(metaObject
            .getChangeability());
    }

    /**
     * @see org.andromda.core.metadecorators.uml.AttributeFacade#isStatic()
     */
    public boolean handleIsStatic()
    {
        return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject
            .getOwnerScope());
    }

    /**
     * @see org.andromda.core.metadecorators.uml.AttributeFacade#findTaggedValue(java.lang.String,
     *      boolean)
     */
    public Object handleFindTaggedValue(String name, boolean follow)
    {
        name = StringUtils.trimToEmpty(name);
        Object value = findTaggedValue(name);
        if (follow)
        {
            ClassifierFacade type = this.getType();
            while (value == null && type != null)
            {
                value = type.findTaggedValue(name);
                type = (ClassifierFacade)type.getGeneralization();
            }
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    public boolean handleIsRequired()
    {
        int lower = this.getMultiplicityRangeLower();
        return lower >= 1;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isMany()
     */
    public boolean handleIsMany()
    {
        boolean isMany = false;
        Multiplicity multiplicity = this.metaObject.getMultiplicity();
        // assume no multiplicity is 1
        if (multiplicity != null)
        {
            Collection ranges = multiplicity.getRange();
            if (ranges != null && !ranges.isEmpty())
            {
                Iterator rangeIt = ranges.iterator();
                while (rangeIt.hasNext())
                {
                    MultiplicityRange multiplicityRange = (MultiplicityRange)rangeIt
                        .next();
                    int upper = multiplicityRange.getUpper();
                    isMany = upper > 1 || upper < 0;
                }
            }
        }
        return isMany;
    }

    /**
     * Returns the lower range of the multiplicty for the passed in
     * associationEnd
     * 
     * @return int the lower range of the multiplicty or 1 if it isn't defined.
     */
    private int getMultiplicityRangeLower()
    {
        int lower = 1;
        Multiplicity multiplicity = metaObject.getMultiplicity();
        // assume no multiplicity is 1
        if (multiplicity != null)
        {
            if (multiplicity != null)
            {
                Collection ranges = multiplicity.getRange();
                if (ranges != null && !ranges.isEmpty())
                {
                    Iterator rangeIt = ranges.iterator();
                    while (rangeIt.hasNext())
                    {
                        MultiplicityRange multiplicityRange = (MultiplicityRange)rangeIt
                            .next();
                        lower = multiplicityRange.getLower();
                    }
                }
            }
        }
        return lower;
    }
}