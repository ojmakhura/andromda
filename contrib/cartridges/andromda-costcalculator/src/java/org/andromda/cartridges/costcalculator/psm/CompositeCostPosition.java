package org.andromda.cartridges.costcalculator.psm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @since 21.03.2005
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class CompositeCostPosition extends AbstractCostPosition implements CostPosition
{
    private ArrayList subPositions = new ArrayList();
    private double initialPrice;

    /**
     * Constructor.
     * 
     * @param name
     *            the name of this position
     * @param initialPrice
     *            the price of this position without the price for the
     *            subpositions
     */
    public CompositeCostPosition(String name, double initialPrice)
    {
        super (name);
        this.initialPrice = initialPrice;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.costcalculator.psm.CostPosition#getPrice()
     */
    public double getPrice()
    {
        double total = this.initialPrice;
        for (Iterator iter = this.subPositions.iterator(); iter.hasNext();)
        {
            CostPosition element = (CostPosition) iter.next();
            total += element.getPrice();
        }
        return total;
    }

    /**
     * Add a subposition to this cost position. This will cause a price
     * increase.
     * 
     * @see #getPrice()
     * @param newPosition
     *            the cost position to be added
     */
    public void addSubPosition(CostPosition newPosition)
    {
        this.subPositions.add(newPosition);
    }

    
    /* (non-Javadoc)
     * @see org.andromda.cartridges.costcalculator.psm.CostPosition#getSubPositions()
     */
    public List getSubPositions()
    {
        return subPositions;
    }
    
}
