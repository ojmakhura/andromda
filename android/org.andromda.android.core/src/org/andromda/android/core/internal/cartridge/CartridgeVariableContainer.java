package org.andromda.android.core.internal.cartridge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.andromda.android.core.cartridge.ICartridgeMetafacadeVariableDescriptor;
import org.andromda.android.core.cartridge.ICartridgeVariableContainer;
import org.andromda.android.core.cartridge.ICartridgeVariableDescriptor;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.HashedMap;

/**
 * A datastructure that stores cartridge variables organized in contexts.
 * 
 * @author Peter Friese
 * @since 20.02.2006
 */
public class CartridgeVariableContainer implements ICartridgeVariableContainer
{

    /** Context for global variables (ones that are valid in any template). */
    private static final String GLOBAL_CTX = "__global__";

    /** This map contains the contexte for the cartridge variables. */
    private HashedMap contexts = new HashedMap();

    /**
     * Add a new variable to the container.
     * 
     * @param variableDescriptor The variable descriptor to store.
     */
    public void put(final ICartridgeVariableDescriptor variableDescriptor)
    {
        String context = determineContext(variableDescriptor);
        internalStore(context, variableDescriptor);
    }

    /**
     * Returns the variable descriptor for the given variable name in the given context.
     * 
     * @param context The context to search the variable descriptor in.
     * @param variableName The name of the variable to look for.
     * @return A {@link ICartridgeVariableDescriptor}, if there is one macthing the criteria, <code>null</code>
     *         otherwise.
     */
    public ICartridgeVariableDescriptor get(final String context,
        final String variableName)
    {
        Map ctx = getContext(context);
        ICartridgeVariableDescriptor variableDescriptor = (ICartridgeVariableDescriptor)ctx.get(variableName);
        return variableDescriptor;
    }

    /**
     * Returns the variable descriptor for the given variable name in the global context.
     * 
     * @param variableName The name of the variable to look for.
     * @return A {@link ICartridgeVariableDescriptor}, if there is one matching the criteria, <code>null</code>
     *         otherwise.
     */
    public ICartridgeVariableDescriptor get(final String variableName)
    {
        return get(GLOBAL_CTX, variableName);
    }

    /**
     * Stores the given variable descriptor in the given context.
     * 
     * @param context The name of the context to store the variable descriptor in.
     * @param variableDescriptor The variable descriptor to store.
     */
    private void internalStore(final String context,
        final ICartridgeVariableDescriptor variableDescriptor)
    {
        Map ctx = getContext(context);
        ctx.put(variableDescriptor.getName(), variableDescriptor);
    }

    /**
     * Returns the context map for the given context string.
     * 
     * @param context The name of the context.
     * @return A {@link Map} for the context.
     */
    private Map getContext(final String context)
    {
        Map ctx = (Map)contexts.get(context);

        if (ctx == null)
        {
            ctx = new HashMap();
            contexts.put(context, ctx);
        }
        return ctx;
    }

    /**
     * Find out which is the right context for the given variable descriptor.
     * 
     * @param variableDescriptor The variable descriptor to find the context for.
     * @return The name of the context for the given variable descriptor.
     */
    private String determineContext(final ICartridgeVariableDescriptor variableDescriptor)
    {
        // default case:
        String context = GLOBAL_CTX;

        // special cases:
        if (variableDescriptor instanceof ICartridgeMetafacadeVariableDescriptor)
        {
            ICartridgeMetafacadeVariableDescriptor metafacadeVariableDescriptor = (ICartridgeMetafacadeVariableDescriptor)variableDescriptor;
            context = metafacadeVariableDescriptor.getTemplatePath();
        }

        return context;
    }

    /**
     * {@inheritDoc}
     */
    public Collection getAll(final String context)
    {
        Map ctx = getContext(context);
        return ctx.values();
    }

    /**
     * {@inheritDoc}
     */
    public Collection getAll()
    {
        Collection result = new ArrayList();
        
        for (MapIterator iter = contexts.mapIterator(); iter.hasNext();)
        {
            Object key = iter.next();
            Map ctx = (Map)iter.getValue();
            result.addAll(ctx.values());
        }
        return result;
    }
    
}
