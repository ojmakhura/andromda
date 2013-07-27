package org.andromda.cartridges.jsf2.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf2.metafacades.JSFSessionObject.
 *
 * @see org.andromda.cartridges.jsf2.metafacades.JSFSessionObject
 */
public class JSFSessionObjectLogicImpl
    extends JSFSessionObjectLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JSFSessionObjectLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return '/' + getFullyQualifiedName().replace('.', '/')
     * @see org.andromda.cartridges.jsf2.metafacades.JSFSessionObject#getFullPath()
     */
    protected String handleGetFullPath()
    {
        return '/' + this.getFullyQualifiedName().replace('.', '/');
    }
}
