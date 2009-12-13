package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.GeneralizationFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Generalization;

/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.metafacades.uml.GeneralizableElementFacade
 * @author Bob Fields
 */
public class GeneralizableElementFacadeLogicImpl
        extends GeneralizableElementFacadeLogic
{

    /**
     * @param metaObject
     * @param context
     */
    public GeneralizableElementFacadeLogicImpl(org.omg.uml.foundation.core.GeneralizableElement metaObject,
                                               String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllGeneralizations()
     */
    @Override
    public Collection<GeneralizableElementFacade> handleGetAllGeneralizations()
    {
        final Collection<GeneralizableElementFacade> generalizations = new ArrayList();
        for (final Iterator<GeneralizableElementFacade> iterator = this.getGeneralizations().iterator(); iterator.hasNext();)
        {
            final GeneralizableElementFacade element = iterator.next();
            generalizations.add(element);
            generalizations.addAll(element.getAllGeneralizations());
        }
        return generalizations;
    }

    // ------------- relations ------------------

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralization()
     */
    @Override
    public GeneralizableElement handleGetGeneralization()
    {
        GeneralizableElement parent = null;
        Collection<Generalization> generalizations = metaObject.getGeneralization();
        if (generalizations != null)
        {
            Iterator<Generalization> iterator = generalizations.iterator();
            if (iterator.hasNext())
            {
                parent = iterator.next().getParent();
            }
        }
        return parent;
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizations()
     */
    @Override
    protected Collection<GeneralizableElementFacade> handleGetGeneralizations()
    {
        Collection<GeneralizableElement> parents = new LinkedHashSet();
        Collection<Generalization> generalizations = metaObject.getGeneralization();
        if (generalizations != null && !generalizations.isEmpty())
        {
            Iterator<Generalization> iterator = generalizations.iterator();
            while (iterator.hasNext())
            {
                parents.add(iterator.next().getParent());
            }
        }
        return this.shieldedElements(parents);
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizationLinks()
     */
    @Override
    protected Collection<GeneralizationFacade> handleGetGeneralizationLinks()
    {
        return this.shieldedElements(metaObject.getGeneralization());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getSpecializations()
     */
    @Override
    public Collection<GeneralizableElement> handleGetSpecializations()
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
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizationList()
     */
    @Override
    protected String handleGetGeneralizationList()
    {
        final StringBuffer list = new StringBuffer();
        if (this.getGeneralizations() != null)
        {
            for (final Iterator iterator = this.getGeneralizations().iterator(); iterator.hasNext();)
            {
                list.append(((ModelElementFacade)iterator.next()).getFullyQualifiedName());
                if (iterator.hasNext())
                {
                    list.append(", ");
                }
            }
        }
        return list.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllSpecializations()
     */
    @Override
    protected Collection<GeneralizableElementFacade> handleGetAllSpecializations()
    {
        final Set<GeneralizableElementFacade> allSpecializations = new LinkedHashSet<GeneralizableElementFacade>();
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
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#findTaggedValue(String, boolean)
     */
    @Override
    protected Object handleFindTaggedValue(final String tagName, boolean follow)
    {
        Object value = this.findTaggedValue(tagName);
        if (value == null)
        {
            for (GeneralizableElementFacade element = this.getGeneralization();
                 value == null && element != null; element = element.getGeneralization())
           {
               value = element.findTaggedValue(tagName, follow);
           }
        }
        return value;
    }

    protected GeneralizableElementFacade handleGetGeneralizationRoot()
    {
        return this.getGeneralization() == null
            ? (GeneralizableElementFacade)THIS()
            : this.getGeneralization().getGeneralizationRoot();
    }
}