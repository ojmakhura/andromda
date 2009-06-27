package org.andromda.metafacades.emf.uml2;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.eclipse.uml2.InstanceValue;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkEndFacade.
 *
 * @see org.andromda.metafacades.uml.LinkEndFacade
 */
public class LinkEndFacadeLogicImpl extends LinkEndFacadeLogic
{
    public LinkEndFacadeLogicImpl(LinkEnd metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstance()
     */
    protected java.lang.Object handleGetInstance()
    {
        final Collection values = this.getInstances();
        return values.isEmpty() ? null : values.iterator().next();
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstances()
     */
    protected java.util.Collection handleGetInstances()
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
     * @see org.andromda.metafacades.uml.LinkEndFacade#getAssociationEnd()
     */
    protected java.lang.Object handleGetAssociationEnd()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getDefiningFeature());
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getLink()
     */
    protected java.lang.Object handleGetLink()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwner());
    }
}