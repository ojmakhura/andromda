package org.andromda.adminconsole.db;

/**
 * Objects that can be refreshed implement this interface
 */
public interface Refreshable
{
    /**
     * Refreshes this object
     */
    public void refresh();
}
