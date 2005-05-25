package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.omg.uml.foundation.core.Generalization;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GeneralizableElementFacade
 */
public class GeneralizableElementFacadeLogicImpl
        extends GeneralizableElementFacadeLogic
{

    public GeneralizableElementFacadeLogicImpl(org.omg.uml.foundation.core.GeneralizableElement metaObject,
                                               java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllGeneralizations()()
     */
    public java.util.Collection handleGetAllGeneralizations()
    {
        Collection generalizations = new ArrayList();
        for (GeneralizableElementFacade element = this.getGeneralization();
             element != null; element = element.getGeneralization())
        {
            generalizations.add(element);
        }
        return generalizations;
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralization()
     */
    public java.lang.Object handleGetGeneralization()
    {
        Object parent = null;
        Collection generalizations = metaObject.getGeneralization();
        if (generalizations != null)
        {
            Iterator iterator = generalizations.iterator();
            if (iterator.hasNext())
            {
                parent = ((Generalization)iterator.next()).getParent();
            }
        }
        return parent;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizations()
     */
    protected Collection handleGetGeneralizations()
    {
        Collection parents = new HashSet();
        Collection generalizations = metaObject.getGeneralization();
        if (generalizations != null && !generalizations.isEmpty())
        {
            Iterator iterator = generalizations.iterator();
            while (iterator.hasNext())
            {
                parents.add(((Generalization)iterator.next()).getParent());
            }
        }
        return parents;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizationLinks()
     */
    protected Collection handleGetGeneralizationLinks()
    {
        return metaObject.getGeneralization();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getSpecializations()
     */
    public Collection handleGetSpecializations()
    {
        Collection specializations = new ArrayList(UML14MetafacadeUtils.getCorePackage().getAParentSpecialization()
                .getSpecialization(this.metaObject));
        CollectionUtils.transform(specializations, new Transformer()
        {
            public Object transform(Object object)
            {
                return ((Generalization)object).getChild();
            }
        });
        return specializations;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacadeLogic#getGeneralizationList()
     */
    protected String handleGetGeneralizationList()
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
     * @see org.andromda.metafacades.uml.GeneralizableElementFacadeLogic#getAllSpecializations()
     */
    protected Collection handleGetAllSpecializations()
    {
        final Set allSpecializations = new HashSet();
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
}
