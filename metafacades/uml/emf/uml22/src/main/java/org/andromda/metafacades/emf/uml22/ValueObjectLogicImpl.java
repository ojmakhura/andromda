package org.andromda.metafacades.emf.uml22;

import java.util.Collection;
import java.util.TreeSet;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebProfile;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.ValueObject.
 *
 * @see org.andromda.metafacades.uml.ValueObject
 */
public class ValueObjectLogicImpl
    extends ValueObjectLogic
{
    private static final long serialVersionUID = 34L;
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
        final StringBuilder stereotypes = new StringBuilder();
        for (StereotypeFacade stereotype : this.getStereotypes())
        {
            stereotypes.append(stereotype.getName()).append(',');
        }
        return this.handleGetTypeSpecializations(stereotypes.toString());
    }

    /**
     * @param stereotypes
     * @return specializations
     */
    protected Collection<ValueObjectLogic> handleGetTypeSpecializations(final String stereotypes)
    {
        final Collection<ValueObjectLogic> specializations = new TreeSet<ValueObjectLogic>();
        final String[] stereotypeList = stereotypes.split(",", -1);
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

    @Override
    public String handleGetMappedComponent() {
        String component = StringUtils.stripToNull(((String) this.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_PRESENTATION_COMPONENT)));
        
        return component;
    }
}
