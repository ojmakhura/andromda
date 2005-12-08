package org.andromda.metafacades.emf.uml2;

import java.util.Collection;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AttributeFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeFacade
 */
public class AttributeFacadeLogicImpl
    extends AttributeFacadeLogic
{
    public AttributeFacadeLogicImpl(
        org.eclipse.uml2.Property metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
    protected java.lang.String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtilsHelper.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getSetterName()
     */
    protected java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(metaObject.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        return metaObject.isReadOnly();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    protected java.lang.String handleGetDefaultValue()
    {
        String ret = null;
        ret = metaObject.getDefault();
        return ret;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isStatic()
     */
    protected boolean handleIsStatic()
    {
        return metaObject.isStatic();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isMany()
     */
    protected boolean handleIsMany()
    {
        return metaObject.isMultivalued();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isChangeable()
     */
    protected boolean handleIsChangeable()
    {
        return metaObject.isReadOnly();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isAddOnly()
     */
    protected boolean handleIsAddOnly()
    {
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationLiteral()
     */
    protected boolean handleIsEnumerationLiteral()
    {
        final ClassifierFacade owner = this.getOwner();
        return (owner != null) && owner.isEnumeration();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumerationValue()
     */
    protected String handleGetEnumerationValue()
    {
        String value = null;
        if (this.isEnumerationLiteral())
        {
            value = this.getDefaultValue();
            value = (value == null) ? getName() : String.valueOf(value);
        }
        if (this.getType().isStringType())
        {
            value = "\"" + value + "\"";
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterSetterTypeName()
     */
    protected java.lang.String handleGetGetterSetterTypeName()
    {
        String name = null;
        if (this.isMany())
        {
            final TypeMappings mappings = this.getLanguageMappings();
            name =
                isOrdered() ? mappings.getTo(UMLProfile.LIST_TYPE_NAME) : mappings.getTo(
                    UMLProfile.COLLECTION_TYPE_NAME);

            // set this attribute's type as a template parameter if required
            if (BooleanUtils.toBoolean(
                    ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING))))
            {
                name = name + "<? extends " + this.getType().getFullyQualifiedName() + ">";
            }
        }
        if (name == null && this.getType() != null)
        {
            name = this.getType().getFullyQualifiedName();
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isOrdered()
     */
    protected boolean handleIsOrdered()
    {
        return metaObject.isOrdered();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#findTaggedValue(java.lang.String, boolean)
     */
    protected java.lang.Object handleFindTaggedValue(
        java.lang.String name,
        boolean follow)
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getOwner()
     */
    protected java.lang.Object handleGetOwner()
    {
        return metaObject.getClass_();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getType()
     */
    protected java.lang.Object handleGetType()
    {
        return metaObject.getType();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumeration()
     */
    protected Object handleGetEnumeration()
    {
        return this.isEnumerationLiteral() ? this.getOwner() : null;
    }

    protected boolean handleIsDefaultValuePresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected boolean handleIsBindingDependenciesPresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected boolean handleIsTemplateParametersPresent()
    {
        // TODO Auto-generated method stub
        return false;
    }

    protected void handleCopyTaggedValues(ModelElementFacade element)
    {
        // TODO Auto-generated method stub
    }

    protected Object handleGetTemplateParameter(String parameterName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    protected Collection handleGetTemplateParameters()
    {
        // TODO Auto-generated method stub
        return null;
    }
}