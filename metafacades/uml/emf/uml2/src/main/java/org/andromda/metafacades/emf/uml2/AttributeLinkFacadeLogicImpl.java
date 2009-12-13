package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.eclipse.uml2.ValueSpecification;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AttributeLinkFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeLinkFacade
 */
public class AttributeLinkFacadeLogicImpl
    extends AttributeLinkFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public AttributeLinkFacadeLogicImpl(AttributeLink metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return attribute
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getAttribute()
     */
    protected Object handleGetAttribute()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getDefiningFeature());
    }

    /**
     * @return instance
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getInstance()
     */
    protected Object handleGetInstance()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwningInstance());
    }

    /**
     * @return value
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValue()
     */
    protected Object handleGetValue()
    {
        final Collection values = this.getValues();
        return values.isEmpty() ? null : values.iterator().next();
    }

    /**
     * @return values
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValues()
     */
    protected Collection handleGetValues()
    {
        final Collection values = new ArrayList(this.metaObject.getValues());

        CollectionUtils.transform(values, new Transformer()
        {
            public Object transform(Object object)
            {
                return InstanceFacadeLogicImpl.createInstanceFor((ValueSpecification)object);
            }
        });

        return values;
    }
}