package org.andromda.metafacades.emf.uml22;

import java.util.Objects;

import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.ParameterEffectKind;
import org.eclipse.uml2.uml.Type;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ParameterFacade.
 *
 * @see org.andromda.metafacades.uml.ParameterFacade
 * @author Bob Fields
 */
public class ParameterFacadeLogicImpl
    extends ParameterFacadeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObjectIn
     * @param context
     */
    public ParameterFacadeLogicImpl(
        final Parameter metaObjectIn,
        final String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getDefaultValue()
     */
    @Override
    protected String handleGetDefaultValue()
    {
        String defaultValue = this.metaObject.getDefault();
        // Put single or double quotes around default in case modeler forgot to do it. Most templates
        // declare Type parameter = $parameter.defaultValue, requiring quotes around the value
        if (StringUtils.isNotBlank(defaultValue) && !this.handleIsMany())
        {
            final String typeName = this.metaObject.getType().getName();
            if ("String".equals(typeName) && defaultValue.indexOf('"')<0)
            {
                defaultValue = '"' + defaultValue + '"';
            }
            else if (("char".equals(typeName) || "Character".equals(typeName))
                && defaultValue.indexOf('\'')<0)
            {
                defaultValue = "'" + defaultValue.charAt(0) + '\'';
            }
        }
        if (defaultValue==null) {defaultValue="";}
        return defaultValue;
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.PARAMETER_NAME_MASK));
        String name = NameMasker.mask(
            super.handleGetName(),
            nameMask);
        if (this.handleIsMany() && this.isPluralizeParameterNames())
        {
            name = StringUtilsHelper.pluralize(name);
        }

        return name;
    }

    /**
     * Indicates whether or not we should pluralize parameter names if parameter[*].
     *
     * @return true/false
     */
    private boolean isPluralizeParameterNames()
    {
        final Object value = this.getConfiguredProperty(UMLMetafacadeProperties.PLURALIZE_PARAMETER_NAMES);
        return value != null && Boolean.valueOf(String.valueOf(value));
    }

    /**
     * @return this.getUpper() > 1 || this.getUpper() == LiteralUnlimitedNatural.UNLIMITED
     * @see org.andromda.metafacades.uml.ParameterFacade#isMany()
     */
    protected boolean handleIsMany()
    {
        // Because of MD11.5 (their multiplicity are String), we cannot use
        // isMultiValued()
        return this.getUpper() > 1 || this.getUpper() == LiteralUnlimitedNatural.UNLIMITED
        || (this.getType() != null && (this.getType().isArrayType() || this.getType().isCollectionType()));
    }

    /**
     * UML2 Only: Returns "NONE" if no effect specified.
     * @see org.andromda.metafacades.uml.ParameterFacade#isException()
     */
    @Override
    public String handleGetEffect()
    {
        final ParameterEffectKind effect = this.metaObject.getEffect();
        if (effect==null)
        {
            return "NONE";
        }
        return effect.getLiteral();
        //return effect.getName();
    }

    /**
     * UML2 Only: Returns false always.
     * @see org.andromda.metafacades.uml.ParameterFacade#isException()
     */
    @Override
    public boolean handleIsException()
    {
        return this.metaObject.isException();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isOrdered()
     */
    @Override
    protected boolean handleIsOrdered()
    {
        return this.metaObject.isOrdered();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isUnique()
     */
    @Override
    protected boolean handleIsUnique()
    {
        return this.metaObject.isUnique();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isReturn()
     */
    @Override
    protected boolean handleIsReturn()
    {
        return this.metaObject.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    @Override
    protected boolean handleIsRequired()
    {
        return !this.hasStereotype(UMLProfile.STEREOTYPE_NULLABLE) && this.getLower() >= 1;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterName()
     */
    @Override
    protected String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType(), this.getLower()) + StringUtils.capitalize(this.handleGetName());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getSetterName()
     */
    @Override
    protected String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.handleGetName());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isReadable()
     */
    @Override
    protected boolean handleIsReadable()
    {
        return this.isInParameter() || this.isInoutParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isWritable()
     */
    @Override
    protected boolean handleIsWritable()
    {
        return this.isOutParameter() || this.isInoutParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isDefaultValuePresent()
     */
    @Override
    protected boolean handleIsDefaultValuePresent()
    {
        return StringUtils.isNotBlank(this.getDefaultValue());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInParameter()
     */
    @Override
    protected boolean handleIsInParameter()
    {
        return this.metaObject.getDirection().equals(ParameterDirectionKind.IN_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isOutParameter()
     */
    @Override
    protected boolean handleIsOutParameter()
    {
        return this.metaObject.getDirection().equals(ParameterDirectionKind.OUT_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInoutParameter()
     */
    @Override
    protected boolean handleIsInoutParameter()
    {
        return this.metaObject.getDirection().equals(ParameterDirectionKind.INOUT_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getOperation()
     */
    @Override
    protected Operation handleGetOperation()
    {
        final Object owner = this.metaObject.getOwner();
        if (owner instanceof Operation)
        {
            return (Operation) owner;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getEvent()
     */
    @Override
    protected Activity handleGetEvent()
    {
        final Element owner = this.metaObject.getOwner();
        if (owner instanceof Activity)
        {
            return (Activity)owner;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getType()
     */
    @Override
    protected Type handleGetType()
    {
        return this.metaObject.getType();
    }

    private String getTemplatingType()
    {
        String type = null;
        
        if (BooleanUtils.toBoolean(
                Objects.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING), ""))
                && this.getType() != null)
        {
            type = this.getType().getFullyQualifiedName();
            if (this.getType().isPrimitive())
            {
                // Can't template primitive values, Objects only. Convert to wrapped.
                type = this.getType().getWrapperName();
            }
            // Allow List<Type[]> implementations.
            /*// Don't apply templating to modeled array types
            if (this.getType().isArrayType())
            {
                type = type.substring(0, type.length()-2);
            }*/
        }
        return type;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterSetterTypeName()
     */
    @Override
    protected String handleGetGetterSetterTypeName()
    {
        String name = null;

        if (this.getUpper() > 1 || this.getUpper() == LiteralUnlimitedNatural.UNLIMITED)
        {
            final TypeMappings mappings = this.getLanguageMappings();
            if (this.handleIsUnique())
            {
                name =
                    this.handleIsOrdered() ? mappings.getTo(UMLProfile.ORDERED_SET_TYPE_NAME)
                                     : mappings.getTo(UMLProfile.SET_TYPE_NAME);

            }
            else
            {
                name =
                    this.handleIsOrdered() ? mappings.getTo(UMLProfile.LIST_TYPE_NAME)
                                     : mappings.getTo(UMLProfile.COLLECTION_TYPE_NAME);
            }

            // set this attribute's type as a template parameter if required
            final String type=getTemplatingType();
            if(type != null)
            {
                /*Collection<GeneralizableElementFacade> specializations = this.handleGetType().getAllSpecializations();
                if ((specializations != null && !specializations.isEmpty()))
                {
                    name += "<? extends " + type + '>';
                }
                else
                {*/
                
                name += '<' + type + '>';
                //}
            }
        }
        if (name == null && this.getType() != null)
        {
            name = this.getType().getFullyQualifiedName();
            // Special case: lower bound overrides primitive/wrapped type declaration
            // TODO Apply to all primitive types, not just booleans. This is a special case because of is/get Getters.
            if (this.getType().isBooleanType())
            {
                if (this.getType().isPrimitive() && this.getLower() < 1)
                {
                    // Type is optional, should not be primitive
                    name = StringUtils.capitalize(name);
                }
                else if (!this.getType().isPrimitive() && this.getLower() > 0)
                {
                    // Type is required, should not be wrapped
                    name = StringUtils.uncapitalize(name);
                }
            }
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterSetterTypeNameImpl()
     */
    @Override
    protected String handleGetGetterSetterTypeNameImpl()
    {
        String nameImpl = null;
        if (this.getUpper() > 1 || this.getUpper() == LiteralUnlimitedNatural.UNLIMITED)
        {
            final TypeMappings mappings = this.getLanguageMappings();
            if (this.handleIsUnique())
            {
                nameImpl =
                    this.handleIsOrdered() ? mappings.getTo(UMLProfile.ORDERED_SET_IMPL_TYPE_NAME)
                                     : mappings.getTo(UMLProfile.SET_IMPL_TYPE_NAME);
            }
            else
            {
                nameImpl =
                    this.handleIsOrdered() ? mappings.getTo(UMLProfile.LIST_IMPL_TYPE_NAME)
                                     : mappings.getTo(UMLProfile.COLLECTION_IMPL_TYPE_NAME);
            }
            // set this attribute's type as a template parameter if required
            final String type=getTemplatingType();
            if(type != null)
            {
                nameImpl += '<' + type + '>';
            }
        }
        if (nameImpl == null )
        {
            nameImpl = getGetterSetterTypeName();
        }
        return nameImpl;
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getOperation();
        if (owner == null)
        {
            owner = this.getEvent();
        }
        return owner;
    }

    /**
     * Get the UML upper multiplicity
     * @return int upperMultiplicity, based on UML multiplicity, default 1
     */
    @Override
    protected int handleGetUpper()
    {
        return UmlUtilities.parseMultiplicity(this.metaObject.getUpperValue(), 1);
    }

    /**
     * Retrieve the default value for lower multiplicity from configured property defaultMultiplicity.
     *
     * @return 1/0
     */
    @SuppressWarnings("unused")
    private int getDefaultMultiplicity()
    {
        final Object value = this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_MULTIPLICITY);
        return (value != null ? Integer.valueOf(String.valueOf(value)).intValue() : 1);
    }

    /**
     * Get the UML lower multiplicity
     * @return int lowerMultiplicity, based on primitive/wrapped type and UML multiplicity, default 1
     */
    @Override
    protected int handleGetLower()
    {
        return UmlUtilities.parseLowerMultiplicity(this.metaObject.getLowerValue(),
                this.getType(),
                Objects.toString(this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_MULTIPLICITY), ""));
    }
}
