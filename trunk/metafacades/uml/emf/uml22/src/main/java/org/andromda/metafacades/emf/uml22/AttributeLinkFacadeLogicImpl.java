package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import org.andromda.metafacades.uml.InstanceFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.eclipse.uml2.uml.ValueSpecification;


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
     * @see org.andromda.metafacades.uml.AttributeLinkFacade
     */
    public AttributeLinkFacadeLogicImpl(AttributeLink metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getAttribute()
     */
    @Override
    protected Object handleGetAttribute()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getDefiningFeature());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getInstance()
     */
    @Override
    protected Object handleGetInstance()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwningInstance());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValue()
     */
    @Override
    protected InstanceFacade handleGetValue()
    {
        final Collection<InstanceFacade> values = this.getValues();
        return values.isEmpty() ? null : values.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValues()
     */
    @Override
    protected Collection<ValueSpecification> handleGetValues()
    {
        final Collection<ValueSpecification> values = new ArrayList<ValueSpecification>(this.metaObject.getValues());

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
