package org.andromda.cartridges.thymeleaf.metafacades;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafSessionObject.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafSessionObject
 */
public class ThymeleafSessionObjectLogicImpl
    extends ThymeleafSessionObjectLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafSessionObjectLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return '/' + getFullyQualifiedName().replace('.', '/')
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafSessionObject#getFullPath()
     */
    protected String handleGetFullPath()
    {
        return '/' + this.getFullyQualifiedName().replace('.', '/');
    }
}
