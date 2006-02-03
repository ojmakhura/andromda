package org.andromda.android.core.cartridge;

import org.eclipse.jdt.core.IType;

/**
 *
 * @author Peter Friese
 * @since 02.02.2006
 */
public interface ICartridgeJavaVariableDescriptor extends ICartridgeVariableDescriptor
{
    
    /**
     * @return The Java type of this variable.
     */
    IType getType();

}
