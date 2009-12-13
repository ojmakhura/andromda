package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKind;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;

/**
 * Metaclass facade implementation.
 * @author Bob Fields
 */
public class AttributeFacadeLogicImpl
    extends AttributeFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public AttributeFacadeLogicImpl(
        Attribute metaObject,
        String context)
    {
        super(metaObject, context);
    }
    
    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public ClassifierFacade getValidationOwner()
    {
        return this.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
    @Override
    public String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtilsHelper.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getSetterName()
     */
    @Override
    public String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    @Override
    public String handleGetDefaultValue()
    {
        String defaultValue = null;
        if (this.metaObject.getInitialValue() != null)
        {
            defaultValue = this.metaObject.getInitialValue().getBody();
        }
        // Put single or double quotes around default in case modeler forgot to do it. Most templates
        // declare Type attribute = $attribute.defaultValue, requiring quotes around the value
        if (StringUtils.isNotEmpty(defaultValue) && !this.isMany())
        {
            String typeName = this.metaObject.getType().getName();
            if (typeName.equals("String") && defaultValue.indexOf('"')<0)
            {
                defaultValue = '"' + defaultValue + '"';
            }
            else if ((typeName.equals("char") || typeName.equals("Character"))
                && defaultValue.indexOf('\'')<0)
            {
                defaultValue = "'" + defaultValue.charAt(0) + '\'';
            }
        }
        if (defaultValue==null) defaultValue="";
        return defaultValue;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isChangeable()
     */
    @Override
    public boolean handleIsChangeable()
    {
        return ChangeableKindEnum.CK_CHANGEABLE.equals(metaObject.getChangeability());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isAddOnly()
     */
    @Override
    public boolean handleIsAddOnly()
    {
        return ChangeableKindEnum.CK_ADD_ONLY.equals(metaObject.getChangeability());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getType()
     */
    @Override
    protected Classifier handleGetType()
    {
        return metaObject.getType();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getOwner()
     */
    @Override
    public Classifier handleGetOwner()
    {
        return this.metaObject.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    @Override
    public boolean handleIsReadOnly()
    {
        return ChangeableKindEnum.CK_FROZEN.equals(metaObject.getChangeability());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isStatic()
     */
    @Override
    public boolean handleIsStatic()
    {
        return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject.getOwnerScope());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#findTaggedValue(String, boolean)
     */
    @Override
    public Object handleFindTaggedValue(
        String name,
        boolean follow)
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
    @Override
    public boolean handleIsRequired()
    {
        int lower = this.getMultiplicityRangeLower();
        return lower >= 1;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isMany()
     */
    @Override
    public boolean handleIsMany()
    {
        boolean isMany = false;
        final Multiplicity multiplicity = this.metaObject.getMultiplicity();

        // assume no multiplicity is 1
        if (multiplicity != null)
        {
            final Collection<MultiplicityRange> ranges = multiplicity.getRange();
            if (ranges != null && !ranges.isEmpty())
            {
                final Iterator<MultiplicityRange> rangeIt = ranges.iterator();
                while (rangeIt.hasNext())
                {
                    final MultiplicityRange multiplicityRange = rangeIt.next();
                    final int upper = multiplicityRange.getUpper();
                    isMany = upper > 1 || upper < 0;
                }
            }
        }
        return isMany;
    }

    /**
     * Returns the lower range of the multiplicity for the passed in attribute. If not specified, default is based on
     * primitive or wrappedPrimitive type, or the default multiplicity.
     *
     * @return int the lower range of the multiplicity or the default multiplicity or 1 if it isn't defined.
     */
    private int getMultiplicityRangeLower()
    {
        Integer lower = null;
        final Multiplicity multiplicity = metaObject.getMultiplicity();
        if (multiplicity != null)
        {
            final Collection<MultiplicityRange> ranges = multiplicity.getRange();
            if (ranges != null && !ranges.isEmpty())
            {
                final Iterator<MultiplicityRange> rangeIt = ranges.iterator();
                while (rangeIt.hasNext())
                {
                    final MultiplicityRange multiplicityRange = rangeIt.next();
                    lower = multiplicityRange.getLower();
                }
            }
        }
        if (lower == null)
        {
            if (this.getType().isPrimitive())
            {
                lower = 1;
            }
            /*else if (this.getType().isWrappedPrimitive())
            {
                lower = Integer.valueOf(0);
            }*/
            else
            {
                final String defaultMultiplicity = this.getDefaultMultiplicity();
                if (defaultMultiplicity.startsWith("0"))
                {
                    lower = 0;
                }
                else
                {
                    lower = 1;
                }
            }
        }
        return lower;
    }

    /**
     * Returns the upper range of the multiplicity for the passed in attribute
     *
     * @return int the upper range of the multiplicity or 1 if it isn't defined.
     */
    private int getMultiplicityRangeUpper()
    {
        Integer upper = null;
        final Multiplicity multiplicity = metaObject.getMultiplicity();
        if (multiplicity != null)
        {
            final Collection<MultiplicityRange> ranges = multiplicity.getRange();
            if (ranges != null && !ranges.isEmpty())
            {
                final Iterator<MultiplicityRange> rangeIt = ranges.iterator();
                while (rangeIt.hasNext())
                {
                    final MultiplicityRange multiplicityRange = rangeIt.next();
                    upper = multiplicityRange.getUpper();
                }
            }
        }
        if (upper == null)
        {
            upper = 1;
        }
        return upper;
    }

    /**
     * Gets the default multiplicity for this attribute (the
     * multiplicity if none is defined).
     *
     * @return the default multiplicity as a String.
     */
    private String getDefaultMultiplicity()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_MULTIPLICITY));
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumeration()
     */
    @Override
    protected EnumerationFacade handleGetEnumeration()
    {
        return (EnumerationFacade)(this.isEnumerationLiteral() ? this.getOwner() : null);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationLiteral()
     */
    @Override
    protected boolean handleIsEnumerationLiteral()
    {
        final ClassifierFacade owner = (ClassifierFacade)this.getOwner();
        return (owner != null) && owner.isEnumeration();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumerationValue()
     */
    @Override
    protected String handleGetEnumerationValue()
    {
        String value = null;
        if (this.isEnumerationLiteral())
        {
            value = this.getDefaultValue();
            value = StringUtils.isEmpty(value) ? this.getName() : String.valueOf(value);
        }
        if (this.getType().isStringType() && value!=null && value.indexOf('"')<0)
        {
            value = '\"' + value + '\"';
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationMember()
     */
    @Override
    protected boolean handleIsEnumerationMember()
    {
        boolean isMemberVariable = false;
        final String isMemberVariableAsString = (String)this.findTaggedValue(
                UMLProfile.TAGGEDVALUE_PERSISTENCE_ENUMERATION_MEMBER_VARIABLE);
        if (StringUtils.isNotEmpty(isMemberVariableAsString) && BooleanUtils.toBoolean(isMemberVariableAsString))
        {
            isMemberVariable = true;
        }
        return isMemberVariable;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumerationLiteralParameters()
     */
    @Override
    protected String handleGetEnumerationLiteralParameters()
    {
        return (String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_ENUMERATION_LITERAL_PARAMETERS);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationLiteralParametersExist()
     */
    @Override
    protected boolean handleIsEnumerationLiteralParametersExist()
    {
        boolean parametersExist = false;
        if (StringUtils.isNotBlank(this.getEnumerationLiteralParameters()))
        {
            parametersExist = true;
        }
        return parametersExist;
    }
    
    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isDefaultValuePresent()
     */
    @Override
    public boolean handleIsDefaultValuePresent()
    {
        return StringUtils.isNotBlank(this.getDefaultValue());
    }

    /**
     * Overridden to provide different handling of the name if this attribute represents a literal.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        String name;
        if (this.isEnumerationMember())
        {
            name = super.handleGetName();
        }
        else
        {
            final String mask = String.valueOf(this.getConfiguredProperty(
                this.getOwner() instanceof EnumerationFacade
                    ? UMLMetafacadeProperties.ENUMERATION_LITERAL_NAME_MASK
                    : UMLMetafacadeProperties.CLASSIFIER_PROPERTY_NAME_MASK ));
    
            name = NameMasker.mask(super.handleGetName(), mask);
            final boolean templating = Boolean.parseBoolean(String.valueOf(
                this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING)));
            final boolean arrayType = this.getType().isArrayType();
            if (this.isPluralizeAttributeNames() && ((this.isMany() && templating) || arrayType))
            {
                name = StringUtilsHelper.pluralize(name);
            }
        }
        
        return name;
    }

    /**
     * Indicates whether or not we should pluralize association end names.
     *
     * @return true/false
     */
    private boolean isPluralizeAttributeNames()
    {
        final Object value = this.getConfiguredProperty(UMLMetafacadeProperties.PLURALIZE_ATTRIBUTE_NAMES);
        return value != null && Boolean.valueOf(String.valueOf(value));
    }

    /**
     * UML2 Only: Returns false always.
     * @return false
     * @see org.andromda.metafacades.uml.AttributeFacade#isLeaf()
     */
    @Override
    public boolean handleIsLeaf()
    {
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isOrdered()
     */
    @Override
    public boolean handleIsOrdered()
    {
        boolean ordered = false;

        final OrderingKind ordering = metaObject.getOrdering();

        // no ordering is 'unordered'
        if (ordering != null)
        {
            ordered = ordering.equals(OrderingKindEnum.OK_ORDERED);
        }

        return ordered;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isUnique()
     */
    protected boolean handleIsUnique()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterSetterTypeName()
     */
    @Override
    public String handleGetGetterSetterTypeName()
    {
        String name = null;
        if (this.isMany())
        {
            final TypeMappings mappings = this.getLanguageMappings();
            if (this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE))
            {
                name =
                    isOrdered() ? mappings.getTo(UMLProfile.ORDERED_SET_TYPE_NAME) : mappings.getTo(
                        UMLProfile.SET_TYPE_NAME);
            }
            else
            {
                name =
                    isOrdered() ? mappings.getTo(UMLProfile.LIST_TYPE_NAME) : mappings.getTo(
                        UMLProfile.COLLECTION_TYPE_NAME);
            }

            // set this attribute's type as a template parameter if required
            if (BooleanUtils.toBoolean(
                    ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING))))
            {
                name = name + '<' + this.getType().getFullyQualifiedName() + ">";
            }
        }
        if (name == null && this.getType() != null)
        {
            name = this.getType().getFullyQualifiedName();
        }
        return name;
    }

    /**
     * Get the UML upper multiplicity
     */
    @Override
    protected int handleGetUpper()
    {
        return this.getMultiplicityRangeUpper();
     }

    /**
     * Get the UML lower multiplicity
     */
    @Override
    protected int handleGetLower()
    {
        return this.getMultiplicityRangeLower();
    }
}