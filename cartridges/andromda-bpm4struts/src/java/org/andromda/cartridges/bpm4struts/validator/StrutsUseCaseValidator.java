package org.andromda.cartridges.bpm4struts.validator;

import org.andromda.cartridges.bpm4struts.StrutsScriptHelper;
import org.andromda.core.uml14.UMLDynamicHelper;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.UmlClass;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class StrutsUseCaseValidator
{
    private StrutsModelValidator modelValidator = null;
    private final Collection validationMessages = new LinkedList();

    StrutsUseCaseValidator(StrutsModelValidator modelValidator)
    {
        this.modelValidator = modelValidator;
    }

    public Collection validate()
    {
        validationMessages.clear();

        final StrutsScriptHelper helper = modelValidator.getHelper();
        final UMLDynamicHelper dynamicHelper = helper.getDynamicHelper();
        final Collection allUseCases = dynamicHelper.getAllUseCases();
        Collection useCases = helper.filterWithStereotypeName(allUseCases, StrutsScriptHelper.ASPECT_FRONT_END_USE_CASE);

        Iterator useCaseIterator = useCases.iterator();
        while (useCaseIterator.hasNext())
        {
            UseCase useCase = (UseCase) useCaseIterator.next();

            validate(useCase);

            StrutsUseCaseStateValidator useCaseStateValidator = new StrutsUseCaseStateValidator(useCase, modelValidator);
            validationMessages.addAll( useCaseStateValidator.validate() );

            StrutsUseCasePseudostateValidator useCasePseudostateValidator = new StrutsUseCasePseudostateValidator(useCase, modelValidator);
            validationMessages.addAll( useCasePseudostateValidator.validate() );
        }

        return Collections.unmodifiableCollection(validationMessages);
    }

    private void validate(UseCase useCase)
    {
        // needs controller class tagged value
        StrutsScriptHelper helper = modelValidator.getHelper();
        String controllerClassName = helper.getStaticHelper().findTagValue(useCase, StrutsScriptHelper.TAG_CONTROLLER_CLASS);

        if (controllerClassName == null)
        {
            validationMessages.add(new ValidationError(useCase, "A use-case must have a ControllerClass tagged value!"));
        }
        else
        {
            UmlClass umlClass = helper.findClassByName(controllerClassName);
            if (umlClass == null)
            {
                validationMessages.add(new ValidationError(umlClass, "Controller class \'"+controllerClassName+"\' not found"));
            }
        }
    }
}
