package org.andromda.cartridges.costcalculator.psm;

import java.util.Collections;
import java.util.List;

/**
 * Represents the development costs of a particular single project artifact.
 * 
 * @since 21.03.2005
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class SimpleCostPosition extends AbstractCostPosition implements
        CostPosition
{

    private double price;

    /**
     * Constructor.
     * 
     * @param name
     *            the name of this position
     * @param price
     *            the price of this position
     */
    public SimpleCostPosition(String name, double price)
    {
        super(name);
        this.price = price;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.costcalculator.psm.CostPosition#getPrice()
     */
    public double getPrice()
    {
        return this.price;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.costcalculator.psm.CostPosition#getSubPositions()
     */
    public List getSubPositions()
    {
        return Collections.EMPTY_LIST;
    }

}
