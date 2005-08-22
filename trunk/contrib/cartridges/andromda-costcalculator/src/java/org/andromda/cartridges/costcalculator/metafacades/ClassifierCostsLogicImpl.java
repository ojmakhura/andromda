package org.andromda.cartridges.costcalculator.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.costcalculator.CostCalculatorGlobals;
import org.andromda.cartridges.costcalculator.psm.CompositeCostPosition;
import org.andromda.metafacades.uml.AssociationEndFacade;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.costcalculator.metafacades.ClassifierCosts.
 * 
 * @see org.andromda.cartridges.costcalculator.metafacades.ClassifierCosts
 */
public class ClassifierCostsLogicImpl extends ClassifierCostsLogic
{

    // ---------------- constructor -------------------------------

    public ClassifierCostsLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.costcalculator.metafacades.ClassifierCosts#getCosts()
     */
    protected org.andromda.cartridges.costcalculator.psm.CostPosition handleGetCosts()
    {
        Object configuredPrice = this
                .getConfiguredProperty(CostCalculatorGlobals.CLASSIFIER_PRICE);
        double classifierPrice = Double
                .valueOf(String.valueOf(configuredPrice)).doubleValue();

        CompositeCostPosition result = new CompositeCostPosition(
                this.getName(), classifierPrice);

        Collection attributes = this.getAttributes();
        for (Iterator iter = attributes.iterator(); iter.hasNext();)
        {
            AttributeCosts element = (AttributeCosts) iter.next();
            result.addSubPosition(element.getCosts());
        }

        Collection operations = this.getOperations();
        for (Iterator iter = operations.iterator(); iter.hasNext();)
        {
            OperationCosts element = (OperationCosts) iter.next();
            result.addSubPosition(element.getCosts());
        }

        Collection associationEnds = this.getAssociationEnds();
        for (Iterator iter = associationEnds.iterator(); iter.hasNext();)
        {
            AssociationEndFacade aef = (AssociationEndFacade) iter.next();
            AssociationCosts element = (AssociationCosts) aef.getAssociation();
            result.addSubPosition(element.getCosts());
        }

        Collection dependencies = this.getSourceDependencies();
        for (Iterator iter = dependencies.iterator(); iter.hasNext();)
        {
            DependencyCosts element = (DependencyCosts) iter.next();
            result.addSubPosition(element.getCosts());
        }

        return result;
    }

}
