package org.andromda.metafacades.emf.uml22;

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
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Type;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.AttributeFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeFacade
 * @author Bob Fields
 */
public class AttributeFacadeLogicImpl
    extends AttributeFacadeLogic
{
    /**
     * @param metaObjectIn
     * @param context
     */
    public AttributeFacadeLogicImpl(
        final Attribute metaObjectIn,
        final String context)
    {
        super(metaObjectIn, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
    @Override
    protected String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType(), this.getLower()) + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getSetterName()
     */
    @Override
    protected String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    @Override
    protected boolean handleIsReadOnly()
    {
        return this.metaObject.isReadOnly();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    @Override
    protected String handleGetDefaultValue()
    {
        String defaultValue = this.metaObject.getDefault();
        // Put single or double quotes around default in case modeler forgot to do it. Most templates
        // declare Type attribute = $attribute.defaultValue, requiring quotes around the value
        if (StringUtils.isNotEmpty(defaultValue) && !this.isMany())
        {
            String typeName = this.metaObject.getType().getName();
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
        if (defaultValue==null) defaultValue="";
        return defaultValue;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isStatic()
     */
    @Override
    protected boolean handleIsStatic()
    {
        return this.metaObject.isStatic();
    }

    /**
     * @return this.metaObject.isLeaf()
     * @see org.andromda.metafacades.uml.AttributeFacade#isLeaf()
     */
    @Override
    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany()
     */
    @Override
    protected boolean handleIsMany()
    {
        // Because of MD11.5 (their multiplicity are String), we cannot use isMultiValued()
        return this.getUpper() > 1 || this.getUpper() == LiteralUnlimitedNatural.UNLIMITED
                // Name or type may be null during the model validation process.
               || (null!=this.getType() && this.getType().isArrayType());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    @Override
    protected boolean handleIsRequired()
    {
        return this.getLower() > 0;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isChangeable()
     */
    @Override
    protected boolean handleIsChangeable()
    {
        return !this.metaObject.isReadOnly();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isAddOnly()
     */
    @Override
    protected boolean handleIsAddOnly()
    {
        // TODO: really nothing to do here ?
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationLiteral()
     */
    @Override
    protected boolean handleIsEnumerationLiteral()
    {
        final ClassifierFacade owner = this.getOwner();
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
            value = (StringUtils.isBlank(value)) ? this.getName() : String.valueOf(value);
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
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterSetterTypeName()
     */
    @Override
    protected String handleGetGetterSetterTypeName()
    {
        String name = null;
        if (this.isMany())
        {
            final TypeMappings mappings = this.getLanguageMappings();
            //TODO: Create Implementation types for declared types, with mappings from declaration -> implementation
            /*if (this.handleIsUnique())
            {
                name =
                    this.isOrdered() ? mappings.getTo(UMLProfile.ORDERED_SET_TYPE_NAME)
                                     : mappings.getTo(UMLProfile.SET_TYPE_NAME);
            }
            else
            {*/
                name =
                    this.isOrdered() ? mappings.getTo(UMLProfile.LIST_TYPE_NAME)
                                     : mappings.getTo(UMLProfile.COLLECTION_TYPE_NAME);
            /*}*/

            // set this attribute's type as a template parameter if required
            if (BooleanUtils.toBoolean(
                    ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING))))
            {
                String type = this.getType().getFullyQualifiedName();
                if (this.getType().isPrimitive())
                {
                    // Can't template primitive values, Objects only. Convert to wrapped.
                    type = this.getType().getWrapperName();
                }
                name += '<' + type + '>';
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
     * @see org.andromda.metafacades.uml.AttributeFacade#isOrdered()
     */
    @Override
    protected boolean handleIsOrdered()
    {
        return this.metaObject.isOrdered();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isUnique()
     */
    @Override
    protected boolean handleIsUnique()
    {
        return this.metaObject.isUnique() || this.hasStereotype(UMLProfile.STEREOTYPE_UNIQUE);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getOwner()
     */
    @Override
    protected Class handleGetOwner()
    {
        // This is sure for attribute
        return this.metaObject.getClass_();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        return this.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getType()
     */
    @Override
    protected Type handleGetType()
    {
        return this.metaObject.getType();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumeration()
     */
    @Override
    protected EnumerationFacade handleGetEnumeration()
    {
        return (EnumerationFacade)(this.isEnumerationLiteral() ? this.getOwner() : null);
    }

    @Override
    protected boolean handleIsDefaultValuePresent()
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
        final String mask = String.valueOf(this.getConfiguredProperty(
            // http://galaxy.andromda.org/jira/browse/UMLMETA-88
            this.getOwner() instanceof EnumerationFacade && !this.isEnumerationMember()
                ? UMLMetafacadeProperties.ENUMERATION_LITERAL_NAME_MASK
                : UMLMetafacadeProperties.CLASSIFIER_PROPERTY_NAME_MASK ));

        String name = NameMasker.mask(super.handleGetName(), mask);
        if (this.isMany() && this.isPluralizeAttributeNames() && !this.isEnumerationMember())
        {
            name = StringUtilsHelper.pluralize(name);
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

    /*  protected boolean handleIsBindingDependenciesPresent()
      {
          // TODO: Implement this with Templates.
          // This method has been overridden. Why ?
          return false;
      }

      protected boolean handleIsTemplateParametersPresent()
      {
          // TODO: This method has been overridden. Why ?
          return false;
      }

      protected void handleCopyTaggedValues(final ModelElementFacade element)
      {
          // TODO: This method has been overridden. Why ?
      }

      protected Object handleGetTemplateParameter(final String parameterName)
      {
          // TODO: This method has been overridden. Why ?
          return null;
      }

      protected Collection handleGetTemplateParameters()
      {
          // TODO: This method has been overridden. Why ?
          return null;
      } */

    /**
     * Get the UML upper multiplicity Not implemented for UML1.4
     */
    @Override
    protected int handleGetUpper()
    {
        // MD11.5 Exports multiplicity as String
        return UmlUtilities.parseMultiplicity(this.metaObject.getUpperValue());
    }

    /**
     * Get the UML lower multiplicity Not implemented for UML1.4
     */
    @Override
    protected int handleGetLower()
    {
        // MD11.5 Exports multiplicity as String
        return UmlUtilities.parseMultiplicity(this.metaObject.getLowerValue());
            //ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_MULTIPLICITY)));
    }

    @Override
    protected Object handleFindTaggedValue(
        String name,
        final boolean follow)
    {
        name = StringUtils.trimToEmpty(name);
        Object value = this.findTaggedValue(name);
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
}
