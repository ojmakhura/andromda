package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
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
     * Used by XmlSeeAlso to reference immediate descendants of this VO class
     * @return specializations
     * @see org.andromda.metafacades.uml.ValueObject#getTypeSpecializations()
     */
    @Override
    protected Collection<ValueObjectLogic> handleGetTypeSpecializations()
    {
        StringBuilder stereotypes = new StringBuilder();
        for (StereotypeFacade stereotype : this.getStereotypes())
        {
            stereotypes.append(stereotype.getName()).append(",");
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
        for (GeneralizableElementFacade classifier : this.getAllSpecializations())
        {
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
