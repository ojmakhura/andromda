package org.andromda.cartridges.bpm4struts.validator;

import org.omg.uml.foundation.core.ModelElement;

public abstract class ValidationMessage
{
    private String message = null;

    public ValidationMessage(ModelElement modelElement, String message)
    {
        this.message = "[" + getModelElementName(modelElement) + "] " + message;
    }

    public String getMessage()
    {
        return message;
    }

    private String getModelElementName(ModelElement modelElement)
    {
        String modelElementName = modelElement.getName();
        if (modelElementName == null)
        {
            modelElementName = "anonmyous " + modelElement.getClass().getName();
        }
        return modelElementName;
    }
}
