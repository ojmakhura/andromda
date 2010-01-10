package org.andromda.cartridges.jsf.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFSessionObject.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFSessionObject
 */
public class JSFSessionObjectLogicImpl
    extends JSFSessionObjectLogic
{

    public JSFSessionObjectLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFSessionObject#getFullPath()
     */
    protected java.lang.String handleGetFullPath()
    {
        return '/' + this.getFullyQualifiedName().replace('.', '/');
    }
}