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
    public TaggedValueFacadeLogicImpl(
        final TagDefinition metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TaggedValueFacade#getValue()
     */
    protected java.lang.Object handleGetValue()
    {
        return this.metaObject.getValue();
    }

    /**
     * @see org.andromda.metafacades.uml.TaggedValueFacade#getValues()
     */
    protected java.util.Collection handleGetValues()
    {
        Collection collection = new ArrayList();
        collection.addAll(this.metaObject.getValues());
        return collection;
    }
}