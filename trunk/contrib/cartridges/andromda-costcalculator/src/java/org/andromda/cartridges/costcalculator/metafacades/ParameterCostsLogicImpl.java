package org.andromda.cartridges.costcalculator.metafacades;

import org.andromda.cartridges.costcalculator.CostCalculatorGlobals;
import org.andromda.cartridges.costcalculator.psm.SimpleCostPosition;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.costcalculator.metafacades.ParameterCosts.
 * 
 * @see org.andromda.cartridges.costcalculator.metafacades.ParameterCosts
 */
public class ParameterCostsLogicImpl extends ParameterCostsLogic
{

    // ---------------- constructor -------------------------------

    public ParameterCostsLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.costcalculator.metafacades.ParameterCosts#getCosts()
     */
    protected org.andromda.cartridges.costcalculator.psm.CostPosition handleGetCosts()
    {
        Object configuredPrice = this
                .getConfiguredProperty(CostCalculatorGlobals.PARAMETER_PRICE);
        double parameterPrice = Double.valueOf(String.valueOf(configuredPrice))
                .doubleValue();

        return new SimpleCostPosition(this.getName(), parameterPrice);
    }

}
