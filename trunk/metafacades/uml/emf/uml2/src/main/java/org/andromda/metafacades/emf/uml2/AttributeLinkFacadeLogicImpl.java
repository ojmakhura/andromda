package org.andromda.metafacades.emf.uml2;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.eclipse.uml2.ValueSpecification;

import java.util.ArrayList;
import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AttributeLinkFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeLinkFacade
 */
public class AttributeLinkFacadeLogicImpl
    extends AttributeLinkFacadeLogic
{
    public AttributeLinkFacadeLogicImpl(AttributeLink metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getAttribute()
     */
    protected java.lang.Object handleGetAttribute()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getDefiningFeature());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getInstance()
     */
    protected java.lang.Object handleGetInstance()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwningInstance());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValue()
     */
    protected java.lang.Object handleGetValue()
    {
        final Collection values = this.getValues();
        return values.isEmpty() ? null : values.iterator().next();
    }

    /**
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