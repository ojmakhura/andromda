package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKind;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;

/**
 * Metaclass facade implementation.
 */
public class AttributeFacadeLogicImpl
    extends AttributeFacadeLogic
{
    // ---------------- constructor -------------------------------

    public AttributeFacadeLogicImpl(
        org.omg.uml.foundation.core.Attribute metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getOwner();
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
                UMLProfile.BOOLEAN_TYPE_NAME) ? "is" : "get";
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

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumeration()
     */
    protected Object handleGetEnumeration()
    {
        return (isEnumerationLiteral()) ? getOwner() : null;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationLiteral()
     */
    protected boolean handleIsEnumerationLiteral()
    {
        final ClassifierFacade owner = getOwner();
        return (owner == null) ? false : owner.isEnumeration();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumerationValue()
     */
    protected String handleGetEnumerationValue()
    {
        String value = null;

        if (isEnumerationLiteral())
        {
            value = getDefaultValue();
            value = (value == null) ? getName() : String.valueOf(value);
        }
         
        if (this.getType().isStringType())
        {
            value = "\"" + value + "\"";
        }
        return value;
    }
    
    /**
     * Overridden to provide different handling of the name if this
     * attribute represents a literal.
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        String name = super.handleGetName();
        if (this.getOwner() instanceof EnumerationFacade)
        {
            final String mask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_LITERAL_NAME_MASK));
            name = MetafacadeUtils.getMaskedName(name, mask);
        }
        return name;
    }
    
    /**
     * @see org.andromda.metafacades.uml.Attribute#isOrdered()
     */
    public boolean handleIsOrdered()
    {
        boolean ordered = false;

        OrderingKind ordering = metaObject.getOrdering();
        // no ordering is 'unordered'
        if (ordering != null)
        {
            ordered = ordering.equals(OrderingKindEnum.OK_ORDERED);
        }

        return ordered;
    }
    
    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterSetterTypeName()
     */
    public String handleGetGetterSetterTypeName()
    {
        // if many, then list or collection
        if (this.isMany())
        {
            Mappings mappings = getLanguageMappings();
            return isOrdered() ? mappings
                .getTo(UMLProfile.LIST_TYPE_NAME) : mappings
                .getTo(UMLProfile.COLLECTION_TYPE_NAME);
        }

        // if single element, then return the type
        return this.getType().getFullyQualifiedName();
    }
}