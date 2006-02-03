package org.andromda.android.core.cartridge;

/**
 * A cartridge variable descriptor.
 * 
 * @author Peter Friese
 * @since 02.02.2006
 */
public interface ICartridgeVariableDescriptor
{

    /**
     * @return The name of the cartridge variable.
     */
    String getName();

    /**
     * @return The documentation for this variable.
     */
    String getDocumentation();

}
