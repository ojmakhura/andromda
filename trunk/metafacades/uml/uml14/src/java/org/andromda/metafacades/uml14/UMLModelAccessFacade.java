package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;

/**
 * Contains a UML model, follows the ModelAccessFacade interface
 * and can therefore be processed by AndroMDA.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 * @author Chad Brandon
 */
public class UMLModelAccessFacade implements ModelAccessFacade
{
    private Logger logger = Logger.getLogger(UMLModelAccessFacade.class);
    
    private UmlPackage model;
    
    public void setModel(Object model) 
    {
        final String methodName = "UMLModelFacade.setModel";
        ExceptionUtils.checkNull(methodName, "model", model);
        ExceptionUtils.checkAssignable(
            methodName, 
            UmlPackage.class, 
            "modelElement",
            model.getClass());
        this.model = (UmlPackage)model;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getModel()
     */
    public Object getModel()
    {
        return model;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getName(java.lang.Object)
     */
    public String getName(Object modelElement)
    {
        final String methodName = "UMLModelFacade.getName";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        ExceptionUtils.checkAssignable(
            methodName, 
            ModelElementFacade.class, 
            "modelElement",
            modelElement.getClass());
        return ((ModelElementFacade)modelElement).getName();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getPackageName(java.lang.Object)
     */
    public String getPackageName(Object modelElement)
    {
        final String methodName = "UMLModelFacade.getPackageName";
        ExceptionUtils.checkNull(methodName, "modelElement", modelElement);
        ExceptionUtils.checkAssignable(
            methodName, 
            ModelElementFacade.class, 
            "modelElement",
            modelElement.getClass());
        return ((ModelElementFacade)modelElement).getPackageName();
    }

    /* (non-Javadoc)
     * @see org.andromda.core.common.ModelFacade#getStereotypeNames(java.lang.Object)
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        Collection stereoTypeNames = new Vector();
        if (modelElement instanceof ModelElement)
        {
            ModelElement m = (ModelElement) modelElement;
            Collection stereotypes = m.getStereotype();
            for (Iterator i = stereotypes.iterator(); i.hasNext();)
            {
                ModelElement stereotype = (ModelElement) i.next();
                stereoTypeNames.add(stereotype.getName());
            }
        }

        return stereoTypeNames;
    }
    
    /**
     * @see edu.duke.dcri.mda.model.ModelAccessFacade#findByStereotype(java.lang.String)
     */
    public Collection findByStereotype(String stereotype) 
    {
        final String methodName = "UMLModelFacade.findByStereotype";
        Collection modelElements = new ArrayList();
        stereotype = StringUtils.trimToEmpty(stereotype);
        if (StringUtils.isNotEmpty(stereotype)) {
            if (this.model != null) {
                Collection underlyingElements = 
                    model.getCore().getModelElement().refAllOfType();
                if (underlyingElements != null || !underlyingElements.isEmpty()) {
                    Iterator elementIt = underlyingElements.iterator();
                    while (elementIt.hasNext()) {
                        ModelElement element = (ModelElement)elementIt.next();
                        Collection stereotypeNames = this.getStereotypeNames(element);
                        if (stereotypeNames != null && stereotypeNames.contains(stereotype)) {
                            modelElements.add(element);
                        }
                    }
                }
                if (logger.isDebugEnabled())
                    logger.debug("completed " + methodName 
                        + " with " + modelElements.size() + " modelElements");
            }
        }
        return modelElements;
    }

}
