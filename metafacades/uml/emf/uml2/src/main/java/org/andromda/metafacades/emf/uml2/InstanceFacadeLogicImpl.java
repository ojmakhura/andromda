package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.andromda.metafacades.uml.InstanceFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.uml2.InstanceSpecification;
import org.eclipse.uml2.LiteralBoolean;
import org.eclipse.uml2.LiteralInteger;
import org.eclipse.uml2.LiteralString;
import org.eclipse.uml2.Slot;
import org.eclipse.uml2.ValueSpecification;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.InstanceFacade.
 *
 * @see org.andromda.metafacades.uml.InstanceFacade
 */
public class InstanceFacadeLogicImpl extends InstanceFacadeLogic
{
    /**
     * Internal value reference in case this instance is supposed to wrap a ValueSpecificstion metaclass
     */
    private Object value = null;
    private boolean valueSet = false;

    /**
     * @param metaObject
     * @param context
     */
    public InstanceFacadeLogicImpl(ObjectInstance metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @param valueSpecification
     * @return instance
     */
    public static InstanceFacade createInstanceFor(ValueSpecification valueSpecification)
    {
        final InstanceFacadeLogicImpl instance = new InstanceFacadeLogicImpl(null, null);

        if (valueSpecification instanceof LiteralString)
        {
            instance.value = ((LiteralString)valueSpecification).getValue();
        }
        else if (valueSpecification instanceof LiteralInteger)
        {
            instance.value = ((LiteralInteger) valueSpecification).getValue();
        }
        else if (valueSpecification instanceof LiteralBoolean)
        {
            instance.value = ((LiteralBoolean) valueSpecification).isValue();
        }
        else
        {
            instance.value = valueSpecification;
        }

        instance.valueSet = true;
        return instance;
    }

    protected String handleGetName()
    {
        return this.valueSet ? (this.value == null ? null : value.toString()) : super.handleGetName();
    }

    /**
     * In case we wrap a value specification we just want to be able to print out that value when calling toString()
     * @return valueSet ? getName() : super.toString()
     */
    public String toString()
    {
        return this.valueSet ? this.getName() : super.toString();
    }

    /**
     * @return metaObject.getClassifiers()
     * @see org.andromda.metafacades.uml.InstanceFacade#getClassifiers()
     */
    protected Collection handleGetClassifiers()
    {
        return this.metaObject.getClassifiers();
    }

    /**
     * @return getOwnedInstances
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedInstances()
     */
    protected Collection handleGetOwnedInstances()
    {
        final Collection ownedElements = new ArrayList(this.metaObject.getOwnedElements());
        CollectionUtils.filter(ownedElements, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return object instanceof InstanceSpecification;
            }
        });
        return CollectionUtils.collect(ownedElements, UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * Instances do not own Links in UML2 (unlike UML1.4+), this method always returns an empty collection.
     * @return getOwnedLinks
     *
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedLinks()
     */
    protected Collection handleGetOwnedLinks()
    {
        return Collections.emptyList();
    }

    /**
     * @return getSlots
     * @see org.andromda.metafacades.uml.InstanceFacade#getSlots()
     */
    protected Collection handleGetSlots()
    {
        return CollectionUtils.collect(this.metaObject.getSlots(), UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * @return getAttributeLinks
     * @see org.andromda.metafacades.uml.InstanceFacade#getAttributeLinks()
     */
    protected Collection handleGetAttributeLinks()
    {
        // collect the slots
        final List slots = new ArrayList(this.metaObject.getSlots());
        // only retain the slots mapping onto attributes
        CollectionUtils.filter(slots, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return UmlUtilities.ELEMENT_TRANSFORMER.transform(((Slot)object).getDefiningFeature()) instanceof Attribute;
            }
        });

        return CollectionUtils.collect(slots, UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * @return getLinkEnds
     * @see org.andromda.metafacades.uml.InstanceFacade#getLinkEnds()
     */
    protected Collection handleGetLinkEnds()
    {
        // collect the slots
        final List slots = new ArrayList(this.metaObject.getSlots());
        // only retain the slots mapping onto association ends
        CollectionUtils.filter(slots, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return UmlUtilities.ELEMENT_TRANSFORMER.transform(((Slot)object).getDefiningFeature()) instanceof AssociationEnd;
            }
        });

        return CollectionUtils.collect(slots, UmlUtilities.ELEMENT_TRANSFORMER);
    }
}