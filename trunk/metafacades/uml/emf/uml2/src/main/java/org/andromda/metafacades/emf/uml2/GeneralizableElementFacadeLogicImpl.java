package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.Generalization;


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
    protected java.lang.String handleGetGeneralizationList()
    {
        final StringBuffer list = new StringBuffer();
        if (this.getGeneralizations() != null)
        {
            for (final Iterator iterator = this.getGeneralizations().iterator(); iterator.hasNext();)
            {
                final ModelElementFacade element = (ModelElementFacade)iterator.next();
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
    protected java.lang.Object handleGetGeneralization()
    {
        Object parent = null;
        final Collection generalizations = ((Classifier)this.metaObject).getGeneralizations();
        if (generalizations != null && !generalizations.isEmpty())
        {
            parent = ((Generalization)generalizations.iterator().next()).getGeneral();
        }
        return parent;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getSpecializations()
     */
    protected java.util.Collection handleGetSpecializations()
    {
        return UmlUtilities.getSpecializations((Classifier)this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizations()
     */
    protected java.util.Collection handleGetGeneralizations()
    {
        Collection parents = new LinkedHashSet();
        Collection generalizations = ((Classifier)this.metaObject).getGeneralizations();
        if (generalizations != null && !generalizations.isEmpty())
        {
            for (final Iterator iterator = generalizations.iterator(); iterator.hasNext();)
            {
                parents.add(((Generalization)iterator.next()).getGeneral());
            }
        }
        return parents;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizationLinks()
     */
    protected java.util.Collection handleGetGeneralizationLinks()
    {
        return ((Classifier)this.metaObject).getGeneralizations();
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllSpecializations()
     */
    protected java.util.Collection handleGetAllSpecializations()
    {
        final Set allSpecializations = new LinkedHashSet();
        if (this.getSpecializations() != null)
        {
            allSpecializations.addAll(this.getSpecializations());
            for (final Iterator iterator = this.getSpecializations().iterator(); iterator.hasNext();)
            {
                final GeneralizableElementFacade element = (GeneralizableElementFacade)iterator.next();
                allSpecializations.addAll(element.getAllSpecializations());
            }
        }
        return allSpecializations;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllGeneralizations()
     */
    protected java.util.Collection handleGetAllGeneralizations()
    {
        final Collection generalizations = new ArrayList();
        for (final Iterator iterator = this.getGeneralizations().iterator(); iterator.hasNext();)
        {
            final GeneralizableElementFacade element = (GeneralizableElementFacade)iterator.next();
            generalizations.add(element);
            generalizations.addAll(element.getAllGeneralizations());
        }
        return generalizations;
    }

    protected Object handleFindTaggedValue(
        final String tagName,
        final boolean follow)
    {
        Object value = this.findTaggedValue(tagName);
        if (value == null && follow)
        {
            for (GeneralizableElementFacade element = this.getGeneralization(); value == null && element != null;
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

    protected Object handleGetGeneralizationRoot()
    {
        GeneralizableElementFacade generalizableElement = (GeneralizableElementFacade)THIS();

        while (generalizableElement.getGeneralization() != null)
        {
            generalizableElement = generalizableElement.getGeneralization();
        }

        return generalizableElement;
    }
}