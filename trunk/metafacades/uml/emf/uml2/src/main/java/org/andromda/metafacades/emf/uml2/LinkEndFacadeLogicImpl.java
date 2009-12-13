package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.eclipse.uml2.InstanceValue;

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
     * @return getInstances().get(0)
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstance()
     */
    protected Object handleGetInstance()
    {
        final Collection values = this.getInstances();
        return values.isEmpty() ? null : values.iterator().next();
    }

    /**
     * @return metaObject.getValues().getInstances()
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstances()
     */
    protected Collection handleGetInstances()
    {
        final Collection values = new ArrayList(this.metaObject.getValues());

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
     * @return UmlUtilities.ELEMENT_TRANSFORMER.transform(metaObject.getDefiningFeature())
     * @see org.andromda.metafacades.uml.LinkEndFacade#getAssociationEnd()
     */
    protected Object handleGetAssociationEnd()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getDefiningFeature());
    }

    /**
     * @return UmlUtilities.ELEMENT_TRANSFORMER.transform(metaObject.getOwner())
     * @see org.andromda.metafacades.uml.LinkEndFacade#getLink()
     */
    protected Object handleGetLink()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwner());
    }
}