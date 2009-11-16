package org.andromda.cartridges.spring.metafacades;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringCriteriaSearch.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaSearch
 */
public class SpringCriteriaSearchLogicImpl
        extends SpringCriteriaSearchLogic
{
    /**
     * Public constructor for SpringCriteriaSearchLogicImpl
     * @param metaObject 
     * @param context 
     * @see org.andromda.cartridges.spring.metafacades.SpringCriteriaSearch
     */
    public SpringCriteriaSearchLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @param follow 
     * @return super.getAttributes(follow).getType()
     * @see org.andromda.cartridges.spring.metafacades.SpringEntityOperation#getImplementationCall()
     */
    protected Collection<SpringCriteriaAttributeLogic> handleGetAttributes(boolean follow)
    {
        Collection<SpringCriteriaAttributeLogic> arguments = new ArrayList<SpringCriteriaAttributeLogic>();
        for (Object parameter : super.getAttributes(follow))
        {
            arguments.add((SpringCriteriaAttributeLogic)((SpringCriteriaAttributeLogic)parameter).getType());
        }
        return arguments;
    }

}
