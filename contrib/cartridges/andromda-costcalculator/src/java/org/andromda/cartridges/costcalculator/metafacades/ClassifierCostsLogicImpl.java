package org.andromda.cartridges.costcalculator.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.costcalculator.CostCalculatorGlobals;
import org.andromda.cartridges.costcalculator.psm.CompositeCostPosition;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.apache.log4j.Priority;

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

        // TODO: BUG-BUG-BUG this code will count each association twice!
        Collection associationEnds = this.getAssociationEnds();
        for (Iterator iter = operations.iterator(); iter.hasNext();)
        {
            Object o = iter.next();
            if (!(o instanceof AssociationEndFacade))
            {
                this.logger.log(Priority.ERROR,
                        "ClassifierCostsLogicImpl: cannot cast type '"
                                + o.getClass().getName()
                                + "' to AssociationEndFacade");
            }

            AssociationEndFacade aef = (AssociationEndFacade) o;
            AssociationCosts element = (AssociationCosts) aef.getAssociation();
            result.addSubPosition(element.getCosts());
        }

        Collection dependencies = this.getSourceDependencies();
        for (Iterator iter = operations.iterator(); iter.hasNext();)
        {
            DependencyCosts element = (DependencyCosts) iter.next();
            result.addSubPosition(element.getCosts());
        }

        return result;
    }

}
