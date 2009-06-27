package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Collections;
import org.andromda.metafacades.uml.InstanceFacade;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.behavioralelements.commonbehavior.AttributeLink;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AttributeLinkFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeLinkFacade
 * @author Bob Fields
 */
public class AttributeLinkFacadeLogicImpl
    extends AttributeLinkFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public AttributeLinkFacadeLogicImpl(
        AttributeLink metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getAttribute()
     */
    @Override
    protected Attribute handleGetAttribute()
    {
        return metaObject.getAttribute();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getInstance()
     */
    @Override
    protected Instance handleGetInstance()
    {
        return metaObject.getInstance();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValue()
     */
    @Override
    protected Instance handleGetValue()
    {
        return metaObject.getValue();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValues()
     */
    @Override
    protected Collection<InstanceFacade> handleGetValues()
    {
        return Collections.singleton(this.getValue());
    }
}