package org.andromda.metafacades.uml14;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


/**
 * Contains a UML model, follows the ModelAccessFacade interface and can therefore be processed by AndroMDA.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class UMLModelAccessFacade
    implements ModelAccessFacade
{
    private Logger logger = Logger.getLogger(UMLModelAccessFacade.class);
    private UmlPackage model;

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#setModel(java.lang.Object)
     */
    public void setModel(Object model)
    {
        final String methodName = "UMLModelAccessFacade.setModel";
        ExceptionUtils.checkNull(methodName, "model", model);
        ExceptionUtils.checkAssignable(
            methodName,
            UmlPackage.class,
            "modelElement",
            model.getClass());
        this.model = (UmlPackage)model;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModel()
     */
    public Object getModel()
    {
        return model;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getName(java.lang.Object)
     */
    public String getName(Object modelElement)
    {
        final String methodName = "UMLModelAccessFacade.getName";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        ExceptionUtils.checkAssignable(
            methodName,
            ModelElementFacade.class,
            "modelElement",
            modelElement.getClass());
        return ((ModelElementFacade)modelElement).getName();
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getPackageName(java.lang.Object)
     */
    public String getPackageName(Object modelElement)
    {
        final String methodName = "UMLModelAccessFacade.getPackageName";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        ExceptionUtils.checkAssignable(
            methodName,
            ModelElementFacade.class,
            "modelElement",
            modelElement.getClass());
        return ((ModelElementFacade)modelElement).getPackageName(true);
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getStereotypeNames(java.lang.Object)
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        Collection stereotypeNames = new ArrayList();
        if (modelElement instanceof ModelElement)
        {
            ModelElement element = (ModelElement)modelElement;
            Collection stereotypes = element.getStereotype();
            for (Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
            {
                ModelElement stereotype = (ModelElement)iterator.next();
                stereotypeNames.add(stereotype.getName());
            }
        }
        else if (modelElement instanceof ModelElementFacade)
        {
            stereotypeNames = ((ModelElementFacade)modelElement).getStereotypeNames();
        }
        return stereotypeNames;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#findByStereotype(java.lang.String)
     */
    public Collection findByStereotype(String stereotype)
    {
        final String methodName = "UMLModelAccessFacade.findByStereotype";
        Collection modelElements = new ArrayList();
        stereotype = StringUtils.trimToEmpty(stereotype);
        if (StringUtils.isNotEmpty(stereotype))
        {
            if (this.model != null)
            {
                Collection underlyingElements = model.getCore().getModelElement().refAllOfType();
                if (underlyingElements != null || !underlyingElements.isEmpty())
                {
                    Iterator elementIt = underlyingElements.iterator();
                    while (elementIt.hasNext())
                    {
                        ModelElement element = (ModelElement)elementIt.next();
                        Collection stereotypeNames = this.getStereotypeNames(element);
                        if (stereotypeNames != null && stereotypeNames.contains(stereotype))
                        {
                            modelElements.add(element);
                        }
                    }
                }
                if (logger.isDebugEnabled())
                {
                    logger.debug("completed " + methodName + " with " + modelElements.size() + " modelElements");
                }
            }
        }
        return modelElements;
    }

    /**
     * @see org.andromda.core.metafacade.ModelAccessFacade#getModelElements()
     */
    public Collection getModelElements()
    {
        Collection modelElements = Collections.EMPTY_LIST;
        if (this.model != null)
        {
            modelElements =
                MetafacadeFactory.getInstance().createMetafacades(
                    this.model.getCore().getModelElement().refAllOfType());
        }
        return modelElements;
    }
}