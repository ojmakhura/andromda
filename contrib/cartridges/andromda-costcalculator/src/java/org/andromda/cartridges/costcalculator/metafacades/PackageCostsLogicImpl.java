package org.andromda.cartridges.costcalculator.metafacades;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.costcalculator.CostCalculatorGlobals;
import org.andromda.cartridges.costcalculator.psm.CompositeCostPosition;
import org.andromda.cartridges.costcalculator.psm.SimpleCostPosition;
import org.apache.log4j.Priority;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.costcalculator.metafacades.PackageCosts.
 * 
 * @see org.andromda.cartridges.costcalculator.metafacades.PackageCosts
 */
public class PackageCostsLogicImpl extends PackageCostsLogic
{

    // ---------------- constructor -------------------------------

    public PackageCostsLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.costcalculator.metafacades.PackageCosts#getCosts()
     */
    protected org.andromda.cartridges.costcalculator.psm.CostPosition handleGetCosts()
    {
        try
        {
            Object configuredPrice = this
                    .getConfiguredProperty(CostCalculatorGlobals.PACKAGE_PRICE);
            double packagePrice = Double.valueOf(
                    String.valueOf(configuredPrice)).doubleValue();

            Collection classes = this.getClasses();
            if (classes.size() == 0)
            {
                return new SimpleCostPosition(this.getFullyQualifiedName(),
                        packagePrice);
            }

            CompositeCostPosition result = new CompositeCostPosition(this
                    .getFullyQualifiedName(), packagePrice);
            for (Iterator iter = classes.iterator(); iter.hasNext();)
            {
                    ClassifierCosts element = (ClassifierCosts) iter.next();
                    result.addSubPosition(element.getCosts());
            }

            return result;
        }
        catch (RuntimeException e)
        {
            this.logger.log(Priority.ERROR, "exception in handleGetCosts()", e);
            throw e;
        }
    }

}
