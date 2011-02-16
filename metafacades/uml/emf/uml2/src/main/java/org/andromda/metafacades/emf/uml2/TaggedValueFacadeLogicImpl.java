package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TaggedValueFacade.
 *
 * @see org.andromda.metafacades.uml.TaggedValueFacade
 */
public class TaggedValueFacadeLogicImpl
    extends TaggedValueFacadeLogic
{
    private static final long serialVersionUID = 6355393498526276057L;

    /**
     * @param metaObject
     * @param context
     */
    public TaggedValueFacadeLogicImpl(
        final TagDefinition metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return metaObject.getValue()
     * @see org.andromda.metafacades.uml.TaggedValueFacade#getValue()
     */
    protected Object handleGetValue()
    {
        return this.metaObject.getValue();
    }

    /**
     * @return metaObject.getValues()
     * @see org.andromda.metafacades.uml.TaggedValueFacade#getValues()
     */
    protected Collection handleGetValues()
    {
        Collection collection = new ArrayList();
        collection.addAll(this.metaObject.getValues());
        return collection;
    }
}