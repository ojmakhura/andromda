package org.andromda.android.core.cartridge;


/**
 *
 * @author Peter Friese
 * @since 02.02.2006
 */
public interface ICartridgeMetafacadeVariableDescriptor extends ICartridgeJavaVariableDescriptor
{

    /**
     * If the template collects the values of more than just one model element instance (by defining
     * outputToSingleFile="true"), then the variable is a {@link java.util.Collection} containing the instances.
     *
     * @return <code>true</code> if the template gathers information of more than one model element instance.
     */
    boolean isCollection();
    
    /**
     * Returns the releative path to the template. The variable specified by this descriptor is only valid in the
     * context of this template.
     * 
     * @return The path to the template this variable is valid in.
     */
    String getTemplatePath();

}
