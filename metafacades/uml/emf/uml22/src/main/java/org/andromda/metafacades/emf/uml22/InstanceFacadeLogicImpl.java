package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.andromda.metafacades.uml.InstanceFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.ValueSpecification;


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
     * @return LiteralInteger or LiteralBoolean or LiteralString
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

    @Override
    protected String handleGetName()
    {
        return this.valueSet ? (this.value == null ? null : this.value.toString()) : super.handleGetName();
    }

    /**
     * In case we wrap a value specification we just want to be able to print out that value when calling toString()
     */
    @Override
    public String toString()
    {
        return this.valueSet ? this.getName() : super.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getClassifiers()
     */
    @Override
    protected Collection<Classifier> handleGetClassifiers()
    {
        return this.metaObject.getClassifiers();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedInstances()
     */
    @Override
    protected Collection<InstanceSpecification> handleGetOwnedInstances()
    {
        final Collection<Element> ownedElements = new ArrayList<Element>(this.metaObject.getOwnedElements());
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
     *
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedLinks()
     */
    @Override
    protected Collection handleGetOwnedLinks()
    {
        return Collections.emptyList();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getSlots()
     */
    @Override
    protected Collection<Slot> handleGetSlots()
    {
        return CollectionUtils.collect(this.metaObject.getSlots(), UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getAttributeLinks()
     */
    @Override
    protected Collection<Attribute> handleGetAttributeLinks()
    {
        // collect the slots
        final List<Slot> slots = new ArrayList<Slot>(this.metaObject.getSlots());
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
     * @see org.andromda.metafacades.uml.InstanceFacade#getLinkEnds()
     */
    @Override
    protected Collection<AssociationEnd> handleGetLinkEnds()
    {
        // collect the slots
        final List<Slot> slots = new ArrayList<Slot>(this.metaObject.getSlots());
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
