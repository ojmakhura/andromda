package org.andromda.cartridges.interfaces;


/**
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 *
 */
public class DefaultAndroMDACartridge
    implements IAndroMDACartridge
{
    private ICartridgeDescriptor desc = null;

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#getDescriptor()
     */
    public ICartridgeDescriptor getDescriptor()
    {
        return desc;
    }

    /**
     * @see org.andromda.cartridges.interfaces.IAndroMDACartridge#setDescriptor(org.andromda.cartridges.interfaces.ICartridgeDescriptor)
     */
    public void setDescriptor(ICartridgeDescriptor d)
    {
        this.desc = d;
    }

}
