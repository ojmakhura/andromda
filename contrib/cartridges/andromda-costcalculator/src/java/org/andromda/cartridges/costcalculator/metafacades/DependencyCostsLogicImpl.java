package org.andromda.cartridges.costcalculator.metafacades;

import org.andromda.cartridges.costcalculator.CostCalculatorGlobals;
import org.andromda.cartridges.costcalculator.psm.SimpleCostPosition;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.costcalculator.metafacades.DependencyCosts.
 * 
 * @see org.andromda.cartridges.costcalculator.metafacades.DependencyCosts
 */
public class DependencyCostsLogicImpl extends DependencyCostsLogic
{

    // ---------------- constructor -------------------------------

    public DependencyCostsLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.costcalculator.metafacades.DependencyCosts#getCosts()
     */
    protected org.andromda.cartridges.costcalculator.psm.CostPosition handleGetCosts()
    {
        Object configuredPrice = this
                .getConfiguredProperty(CostCalculatorGlobals.DEPENDENCY_PRICE);
        double dependencyPrice = Double.valueOf(String.valueOf(configuredPrice))
                .doubleValue();

        return new SimpleCostPosition(this.getName(), dependencyPrice);
    }

}
