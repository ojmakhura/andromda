package org.andromda.cartridges.costcalculator.metafacades;

import org.andromda.cartridges.costcalculator.CostCalculatorGlobals;
import org.andromda.cartridges.costcalculator.psm.SimpleCostPosition;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.costcalculator.metafacades.AssociationCosts.
 * 
 * @see org.andromda.cartridges.costcalculator.metafacades.AssociationCosts
 */
public class AssociationCostsLogicImpl extends AssociationCostsLogic
{

    // ---------------- constructor -------------------------------

    public AssociationCostsLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.costcalculator.metafacades.AssociationCosts#getCosts()
     */
    protected org.andromda.cartridges.costcalculator.psm.CostPosition handleGetCosts()
    {
        Object configuredPrice = this
                .getConfiguredProperty(CostCalculatorGlobals.ASSOCIATION_PRICE);
        double associationPrice = Double.valueOf(
                String.valueOf(configuredPrice)).doubleValue();

        return new SimpleCostPosition(this.getName(), associationPrice);
    }

}
