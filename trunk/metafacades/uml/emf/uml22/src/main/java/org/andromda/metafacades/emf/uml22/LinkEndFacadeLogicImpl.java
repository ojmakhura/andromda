package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import org.andromda.metafacades.uml.InstanceFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.eclipse.uml2.uml.InstanceValue;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkEndFacade.
 *
 * @see org.andromda.metafacades.uml.LinkEndFacade
 */
public class LinkEndFacadeLogicImpl extends LinkEndFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public LinkEndFacadeLogicImpl(LinkEnd metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstance()
     */
    @Override
    protected InstanceFacade handleGetInstance()
    {
        final Collection<InstanceFacade> values = this.getInstances();
        return (values.isEmpty() ? null : values.iterator().next());
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstances()
     */
    @Override
    protected Collection handleGetInstances()
    {
        final Collection values = new ArrayList<ValueSpecification>(this.metaObject.getValues());

        CollectionUtils.transform(values, new Transformer()
        {
            public Object transform(Object object)
            {
                return UmlUtilities.ELEMENT_TRANSFORMER.transform(((InstanceValue)object).getInstance());
            }
        });

        return values;
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getAssociationEnd()
     */
    @Override
    protected Object handleGetAssociationEnd()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getDefiningFeature());
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getLink()
     */
    @Override
    protected Object handleGetLink()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwner());
    }
}
