package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.AggregationKind;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AssociationEndFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationEndFacade
 */
public class AssociationEndFacadeLogicImpl
    extends AssociationEndFacadeLogic
{
    public AssociationEndFacadeLogicImpl(
        org.eclipse.uml2.Property metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2One()
     */
    protected boolean handleIsOne2One()
    {
        return !this.isMany() && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2Many()
     */
    protected boolean handleIsOne2Many()
    {
        return !this.isMany() && this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2One()
     */
    protected boolean handleIsMany2One()
    {
        return this.isMany() && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2Many()
     */
    protected boolean handleIsMany2Many()
    {
        return this.isMany() && this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isAggregation()
     */
    protected boolean handleIsAggregation()
    {
        return metaObject.getAggregation().equals(AggregationKind.SHARED_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isComposition()
     */
    protected boolean handleIsComposition()
    {
        return metaObject.getAggregation().equals(AggregationKind.COMPOSITE_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOrdered()
     */
    protected boolean handleIsOrdered()
    {
        return metaObject.isOrdered();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        return this.metaObject.isReadOnly();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isNavigable()
     */
    protected boolean handleIsNavigable()
    {
        return this.metaObject.isNavigable();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterName()
     */
    protected java.lang.String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtilsHelper.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getSetterName()
     */
    protected java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    protected java.lang.String handleGetGetterSetterTypeName()
    {
        String name = null;
        if (this.isMany())
        {
            final TypeMappings mappings = this.getLanguageMappings();
            if (mappings != null)
            {
                name =
                    this.isOrdered() ? mappings.getTo(UMLProfile.LIST_TYPE_NAME)
                                     : mappings.getTo(UMLProfile.COLLECTION_TYPE_NAME);
            }

            // set this association end's type as a template parameter if required
            if ("true".equals(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING)))
            {
                name = name + "<" + this.getType().getFullyQualifiedName() + ">";
            }
        }
        if (name == null && this.getType() != null)
        {
            name = this.getType().getFullyQualifiedName();
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany()
     */
    protected boolean handleIsMany()
    {
        return metaObject.isMultivalued();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isChild()
     */
    protected boolean handleIsChild()
    {
        return this.getOtherEnd() != null && this.getOtherEnd().isComposition();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getOtherEnd()
     */
    protected java.lang.Object handleGetOtherEnd()
    {
        return UmlUtilities.getOppositeAssociationEnd(this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getAssociation()
     */
    protected java.lang.Object handleGetAssociation()
    {
        return this.metaObject.getAssociation();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getType()
     */
    protected java.lang.Object handleGetType()
    {
        return this.metaObject.getType();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getType();
    }
}