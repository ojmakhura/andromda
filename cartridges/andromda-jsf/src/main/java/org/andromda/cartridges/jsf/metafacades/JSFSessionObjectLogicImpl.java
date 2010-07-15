package org.andromda.cartridges.jsf.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFSessionObject.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFSessionObject
 */
public class JSFSessionObjectLogicImpl
    extends JSFSessionObjectLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public JSFSessionObjectLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @return '/' + getFullyQualifiedName().replace('.', '/')
     * @see org.andromda.cartridges.jsf.metafacades.JSFSessionObject#getFullPath()
     */
    protected String handleGetFullPath()
    {
        return '/' + this.getFullyQualifiedName().replace('.', '/');
    }
}