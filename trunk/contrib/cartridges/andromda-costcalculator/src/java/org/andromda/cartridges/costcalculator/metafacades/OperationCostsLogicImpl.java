package org.andromda.cartridges.costcalculator.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.costcalculator.CostCalculatorGlobals;
import org.andromda.cartridges.costcalculator.psm.CompositeCostPosition;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.costcalculator.metafacades.OperationCosts.
 * 
 * @see org.andromda.cartridges.costcalculator.metafacades.OperationCosts
 */
public class OperationCostsLogicImpl extends OperationCostsLogic
{

    // ---------------- constructor -------------------------------

    public OperationCostsLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.costcalculator.metafacades.OperationCosts#getCosts()
     */
    protected org.andromda.cartridges.costcalculator.psm.CostPosition handleGetCosts()
    {
        Object configuredPrice = this
                .getConfiguredProperty(CostCalculatorGlobals.OPERATION_PRICE);
        double operationPrice = Double.valueOf(String.valueOf(configuredPrice))
                .doubleValue();

        CompositeCostPosition result = new CompositeCostPosition(
                this.getName(), operationPrice);

        Collection parameters = this.getParameters();
        for (Iterator iter = parameters.iterator(); iter.hasNext();)
        {
            ParameterCosts element = (ParameterCosts) iter.next();
            if (!element.isReturn())
            {
                result.addSubPosition(element.getCosts());
            }
        }

        return result;
    }

}
