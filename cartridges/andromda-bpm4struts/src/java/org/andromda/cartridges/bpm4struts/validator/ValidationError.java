package org.andromda.cartridges.bpm4struts.validator;

import org.omg.uml.foundation.core.ModelElement;

public class ValidationError extends ValidationMessage
{
    public ValidationError(ModelElement modelElement, String errorMessage)
    {
        super(modelElement, errorMessage);
    }

    public String getMessage()
    {
        return "  [Error] " + super.getMessage();
    }
}
