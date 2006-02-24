package org.andromda.android.core.cartridge;

import java.util.Collection;

/**
 * A datastructure that stores cartridge variables organized in contexts.
 * 
 * @author Peter Friese
 * @since 20.02.2006
 */
public interface ICartridgeVariableContainer
{

    /**
     * Add a new variable to the container.
     * 
     * @param variableDescriptor The variable descriptor to store.
     */
    void put(final ICartridgeVariableDescriptor variableDescriptor);

    /**
     * Returns the variable descriptor for the given variable name in the given context.
     * 
     * @param context The context to search the variable descriptor in.
     * @param variableName The name of the variable to look for.
     * @return A {@link ICartridgeVariableDescriptor}, if there is one macthing the criteria, <code>null</code>
     *         otherwise.
     */
    ICartridgeVariableDescriptor get(final String context,
        final String variableName);

    /**
     * Returns the variable descriptor for the given variable name in the global context.
     * 
     * @param variableName The name of the variable to look for.
     * @return A {@link ICartridgeVariableDescriptor}, if there is one matching the criteria, <code>null</code>
     *         otherwise.
     */
    ICartridgeVariableDescriptor get(final String variableName);

    /**
     * Returns all variable descriptors of the given context.
     * 
     * @param context The desired context.
     * @return A collection of all variable descriptors in the specified context.
     */
    Collection getAll(final String context);

    /**
     * Returns a collection of all variables descriptors.
     * 
     * @return A collection of all variable descriptors.
     */
    Collection getAll();

}
