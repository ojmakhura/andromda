package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.GeneralizableElementFacade.
 *
 * @see org.andromda.metafacades.uml.GeneralizableElementFacade
 */
public class GeneralizableElementFacadeLogicImpl
    extends GeneralizableElementFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public GeneralizableElementFacadeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizationList()
     */
    @Override
    protected String handleGetGeneralizationList()
    {
        final StringBuilder list = new StringBuilder();
        if (this.getGeneralizations() != null)
        {
            for (final Iterator<GeneralizableElementFacade> iterator = this.getGeneralizations().iterator(); iterator.hasNext();)
            {
                final GeneralizableElementFacade element = iterator.next();
                list.append(element.getFullyQualifiedName());
                if (iterator.hasNext())
                {
                    list.append(", ");
                }
            }
        }
        return list.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralization()
     */
    @Override
    protected Classifier handleGetGeneralization()
    {
        Classifier parent = null;
        final Collection<Generalization>  generalizations = ((Classifier)this.metaObject).getGeneralizations();
        if (generalizations != null && !generalizations.isEmpty())
        {
            parent = generalizations.iterator().next().getGeneral();
        }
        return parent;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getSpecializations()
     */
    @Override
    protected Collection<Classifier> handleGetSpecializations()
    {
        return UmlUtilities.getSpecializations((Classifier)this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizations()
     */
    @Override
    protected Collection<Classifier> handleGetGeneralizations()
    {
        Collection<Classifier> parents = new LinkedHashSet<Classifier>();
        Collection<Generalization> generalizations = ((Classifier)this.metaObject).getGeneralizations();
        if (generalizations != null && !generalizations.isEmpty())
        {
            for (Generalization general : generalizations)
            {
                parents.add(general.getGeneral());
            }
        }
        return parents;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizationLinks()
     */
    @Override
    protected Collection<Generalization> handleGetGeneralizationLinks()
    {
        return ((Classifier)this.metaObject).getGeneralizations();
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllSpecializations()
     */
    @Override
    protected Set<GeneralizableElementFacade> handleGetAllSpecializations()
    {
        final Set<GeneralizableElementFacade> allSpecializations = new LinkedHashSet<GeneralizableElementFacade>();
        if (this.getSpecializations() != null)
        {
            allSpecializations.addAll(this.getSpecializations());
            for (GeneralizableElementFacade element : this.getSpecializations())
            {
                allSpecializations.addAll(element.getAllSpecializations());
            }
        }
        return allSpecializations;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllGeneralizations()
     */
    @Override
    protected Collection<GeneralizableElementFacade> handleGetAllGeneralizations()
    {
        final Collection<GeneralizableElementFacade> generalizations = new ArrayList<GeneralizableElementFacade>();
        for (GeneralizableElementFacade element : this.getGeneralizations())
        {
            generalizations.add(element);
            generalizations.addAll(element.getAllGeneralizations());
        }
        return generalizations;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.GeneralizableElementFacadeLogic#handleFindTaggedValue(java.lang.String, boolean)
     */
    @Override
    protected Object handleFindTaggedValue(
        final String tagName,
        final boolean follow)
    {
        Object value = this.findTaggedValue(tagName);
        if (value == null && follow)
        {
            for (GeneralizableElementFacade element = this.getGeneralization(); element != null;
                element = element.getGeneralization())
            {
                value = element.findTaggedValue(
                        tagName,
                        follow);
                if (value != null)
                {
                    break;
                }
            }
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.emf.uml22.GeneralizableElementFacadeLogic#handleGetGeneralizationRoot()
     */
    @Override
    protected GeneralizableElementFacade handleGetGeneralizationRoot()
    {
        GeneralizableElementFacade generalizableElement = (GeneralizableElementFacade)THIS();

        while (generalizableElement.getGeneralization() != null)
        {
            generalizableElement = generalizableElement.getGeneralization();
        }

        return generalizableElement;
    }
}
