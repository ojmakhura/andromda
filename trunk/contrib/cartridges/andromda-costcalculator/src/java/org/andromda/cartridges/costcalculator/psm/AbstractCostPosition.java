package org.andromda.cartridges.costcalculator.psm;

import java.util.Iterator;

/**
 * Represents the development costs of a project artifact.
 * 
 * @since 27.03.2005
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public abstract class AbstractCostPosition implements CostPosition
{

    private String name;

    private int indentationLevel;

    /**
     * Constructor.
     * 
     * @param name
     *            the name of this position
     * @param price
     *            the price of this position
     */
    public AbstractCostPosition(String name)
    {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.costcalculator.psm.CostPosition#getName()
     */
    public String getName()
    {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.costcalculator.psm.CostPosition#getIndentationLevel()
     */
    public int getIndentationLevel()
    {
        return this.indentationLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.cartridges.costcalculator.psm.CostPosition#assignIndentationLevels(int)
     */
    public void assignIndentationLevels(int startLevel)
    {
        this.indentationLevel = startLevel;
        for (Iterator iter = this.getSubPositions().iterator(); iter.hasNext();)
        {
            CostPosition element = (CostPosition) iter.next();
            element.assignIndentationLevels(startLevel + 1);
        }
    }

}
