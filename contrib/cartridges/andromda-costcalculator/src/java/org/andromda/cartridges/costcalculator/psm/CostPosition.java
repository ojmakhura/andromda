package org.andromda.cartridges.costcalculator.psm;

import java.util.List;

/**
 * Represents the costs of a project artifact or of a collection of project
 * artifacts (composite pattern).
 * 
 * @since 21.03.2005
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public interface CostPosition
{

    /**
     * Returns the name of this cost position.
     * 
     * @return the name
     */
    public String getName();

    /**
     * Returns the (total) price of this cost position, including the price for
     * all subpositions, if applicable.
     * 
     * @return the total price
     */
    public double getPrice();

    /**
     * Returns a list of all the subpositions of this cost position. The type of
     * the list elements is also CostPosition.
     * 
     * @return the subpositions
     */
    public List getSubPositions();
}
