package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.StereotypeFacade;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ValueObject.
 *
 * @see org.andromda.metafacades.uml.ValueObject
 */
public class ValueObjectLogicImpl
    extends ValueObjectLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public ValueObjectLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return specializations
     * @see org.andromda.metafacades.uml.ValueObject#getTypeSpecializations()
     */
    @Override
    protected Collection<ValueObjectLogic> handleGetTypeSpecializations()
    {
        StringBuilder stereotypes = new StringBuilder();
        for (final Iterator<StereotypeFacade> iterator = this.getStereotypes().iterator(); iterator.hasNext();)
        {
            stereotypes.append(iterator.next().getName()).append(',');
        }
        return this.handleGetTypeSpecializations(stereotypes.toString());
    }

    /**
     * @param stereotypes 
     * @return specializations
     */
    protected Collection<ValueObjectLogic> handleGetTypeSpecializations(String stereotypes)
    {
        Collection<ValueObjectLogic> specializations = new TreeSet<ValueObjectLogic>();
        String[] stereotypeList = stereotypes.split(",", -1);
        for (final Iterator<GeneralizableElementFacade> iterator = this.getAllSpecializations().iterator(); iterator.hasNext();)
        {
            final GeneralizableElementFacade classifier = iterator.next();
            if (classifier instanceof ValueObjectLogic)
            {
                for (int i=0; i<stereotypeList.length; i++)
                {
                    if (classifier.hasStereotype(stereotypeList[i]) && !specializations.contains(classifier))
                    {
                        specializations.add((ValueObjectLogic) classifier);
                    }
                }
            }
        }
        return specializations;
    }
}