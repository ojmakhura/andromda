package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.datatypes.AggregationKindEnum;
import org.omg.uml.foundation.datatypes.ChangeableKindEnum;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.OrderingKind;
import org.omg.uml.foundation.datatypes.OrderingKindEnum;


/**
 * Metaclass facade implementation.
 */
public class AssociationEndFacadeLogicImpl
    extends AssociationEndFacadeLogic
    implements org.andromda.metafacades.uml.AssociationEndFacade
{
    public AssociationEndFacadeLogicImpl(
        org.omg.uml.foundation.core.AssociationEnd metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getType();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getOtherEnd()
     */
    protected Object handleGetOtherEnd()
    {
        final Collection ends = metaObject.getAssociation().getConnection();
        for (final Iterator endIt = ends.iterator(); endIt.hasNext();)
        {
            final AssociationEnd end = (AssociationEnd)endIt.next();
            if (!metaObject.equals(end))
            {
                return end;
            }
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetName()
     */
    protected String handleGetName()
    {
        String name = super.handleGetName();

        // if name is empty, then get the name from the type
        if (StringUtils.isEmpty(name))
        {
            final ClassifierFacade type = this.getType();
            if (type != null)
            {
                name = StringUtils.uncapitalize(StringUtils.trimToEmpty(type.getName()));
            }
            if (this.isMany() && this.isPluralizeAssociationEndNames())
            {
                name = StringUtilsHelper.pluralize(name);
            }
        }
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.CLASSIFIER_PROPERTY_NAME_MASK));
        return NameMasker.mask(
            name,
            nameMask);
    }

    /**
     * Indicates whether or not we should pluralize association end names.
     *
     * @return true/false
     */
    private boolean isPluralizeAssociationEndNames()
    {
        boolean pluralize = false;
        final Object value = this.getConfiguredProperty(UMLMetafacadeProperties.PLURALIZE_ASSOCIATION_END_NAMES);
        if (value != null)
        {
            pluralize = Boolean.valueOf(String.valueOf(value)).booleanValue();
        }
        return pluralize;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getType()
     */
    protected Object handleGetType()
    {
        return metaObject.getParticipant();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2Many()
     */
    protected boolean handleIsOne2Many()
    {
        return !this.isMany() && this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2Many()
     */
    protected boolean handleIsMany2Many()
    {
        return this.isMany() && this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2One()
     */
    protected boolean handleIsOne2One()
    {
        return !this.isMany() && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2One()
     */
    protected boolean handleIsMany2One()
    {
        return this.isMany() && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany()
     */
    protected boolean handleIsMany()
    {
        boolean isMany = false;
        final Multiplicity multiplicity = this.metaObject.getMultiplicity();

        // we'll say a null multiplicity is 1
        if (multiplicity != null)
        {
            final Collection ranges = multiplicity.getRange();
            if (ranges != null && !ranges.isEmpty())
            {
                final Iterator rangeIt = ranges.iterator();
                while (rangeIt.hasNext())
                {
                    final MultiplicityRange multiplicityRange = (MultiplicityRange)rangeIt.next();
                    final int upper = multiplicityRange.getUpper();
                    isMany = upper > 1 || upper < 0;
                }
            }
        }
        return isMany;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOrdered()
     */
    protected boolean handleIsOrdered()
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
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isAggregation()
     */
    protected boolean handleIsAggregation()
    {
        return AggregationKindEnum.AK_AGGREGATE.equals(metaObject.getAggregation());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isComposition()
     */
    protected boolean handleIsComposition()
    {
        return AggregationKindEnum.AK_COMPOSITE.equals(metaObject.getAggregation());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        return ChangeableKindEnum.CK_FROZEN.equals(metaObject.getChangeability());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isNavigable()
     */
    protected boolean handleIsNavigable()
    {
        return metaObject.isNavigable();
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
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getAssociation()
     */
    protected Object handleGetAssociation()
    {
        return metaObject.getAssociation();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    protected String handleGetGetterSetterTypeName()
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
            if (BooleanUtils.toBoolean(
                    ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING))))
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
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        final int lower = this.getMultiplicityRangeLower();
        return lower >= 1;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isChild()
     */
    protected boolean handleIsChild()
    {
        return this.getOtherEnd() != null && this.getOtherEnd().isComposition();
    }

    /**
     * Returns the lower range of the multiplicty for the passed in associationEnd
     *
     * @return int the lower range of the multiplicty or 1 if it isn't defined.
     */
    private int getMultiplicityRangeLower()
    {
        Integer lower = null;
        final Multiplicity multiplicity = this.metaObject.getMultiplicity();
        if (multiplicity != null)
        {
            final Collection ranges = multiplicity.getRange();
            if (ranges != null && !ranges.isEmpty())
            {
                final Iterator rangeIt = ranges.iterator();
                while (rangeIt.hasNext())
                {
                    final MultiplicityRange multiplicityRange = (MultiplicityRange)rangeIt.next();
                    lower = new Integer(multiplicityRange.getLower());
                }
            }
        }
        if (lower == null)
        {
            final String defaultMultiplicity = this.getDefaultMultiplicity();
            if (defaultMultiplicity.startsWith("0"))
            {
                lower = new Integer(0);
            }
            else
            {
                lower = new Integer(1);
            }
        }
        return lower.intValue();
    }

    /**
     * Gets the default multiplicity for this attribute (the
     * multiplicity if none is defined).
     *
     * @return the defautl multiplicity as a String.
     */
    private String getDefaultMultiplicity()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_MULTIPLICITY));
    }
}