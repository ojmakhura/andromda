package org.andromda.cartridges.jakarta.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jakarta.metafacades.JakartaSessionObject.
 *
 * @see org.andromda.cartridges.jakarta.metafacades.JakartaSessionObject
 */
public class JakartaSessionObjectLogicImpl
    extends JakartaSessionObjectLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JakartaSessionObjectLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return '/' + getFullyQualifiedName().replace('.', '/')
     * @see org.andromda.cartridges.jakarta.metafacades.JakartaSessionObject#getFullPath()
     */
    protected String handleGetFullPath()
    {
        return '/' + this.getFullyQualifiedName().replace('.', '/');
    }
}
