package org.andromda.cartridges.costcalculator.metafacades;

import org.andromda.cartridges.costcalculator.CostCalculatorGlobals;
import org.andromda.cartridges.costcalculator.psm.SimpleCostPosition;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.costcalculator.metafacades.AttributeCosts.
 * 
 * @see org.andromda.cartridges.costcalculator.metafacades.AttributeCosts
 */
public class AttributeCostsLogicImpl extends AttributeCostsLogic
{

    // ---------------- constructor -------------------------------

    public AttributeCostsLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.costcalculator.metafacades.AttributeCosts#getCosts()
     */
    protected org.andromda.cartridges.costcalculator.psm.CostPosition handleGetCosts()
    {
        Object configuredPrice = this
                .getConfiguredProperty(CostCalculatorGlobals.ATTRIBUTE_PRICE);
        double attributePrice = Double.valueOf(String.valueOf(configuredPrice))
                .doubleValue();

        return new SimpleCostPosition(this.getName(), attributePrice);
    }

}
