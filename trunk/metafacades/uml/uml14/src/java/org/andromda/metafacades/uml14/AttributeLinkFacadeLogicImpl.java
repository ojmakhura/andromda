package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AttributeLinkFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeLinkFacade
 */
public class AttributeLinkFacadeLogicImpl
    extends AttributeLinkFacadeLogic
{
    public AttributeLinkFacadeLogicImpl(
        org.omg.uml.behavioralelements.commonbehavior.AttributeLink metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getAttribute()
     */
    protected java.lang.Object handleGetAttribute()
    {
        return metaObject.getAttribute();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getLinkEnd()
     */
    protected java.lang.Object handleGetLinkEnd()
    {
        return metaObject.getLinkEnd();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getInstance()
     */
    protected java.lang.Object handleGetInstance()
    {
        return metaObject.getInstance();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValue()
     */
    protected java.lang.Object handleGetValue()
    {
        return metaObject.getValue();
    }
}