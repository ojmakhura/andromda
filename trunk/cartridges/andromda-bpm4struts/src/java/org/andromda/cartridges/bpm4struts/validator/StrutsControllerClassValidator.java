package org.andromda.cartridges.bpm4struts.validator;

import org.andromda.cartridges.bpm4struts.StrutsScriptHelper;
import org.andromda.core.uml14.UMLStaticHelper;
import org.andromda.core.uml14.DirectionalAssociationEnd;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Iterator;

public class StrutsControllerClassValidator
{
    private Collection controllerClasses = null;
    private final Collection validationMessages = new LinkedList();
    private StrutsModelValidator modelValidator = null;

    public StrutsControllerClassValidator(StrutsModelValidator modelValidator)
    {
        this.modelValidator = modelValidator;
        Collection allClasses = modelValidator.getHelper().getStaticHelper().getAllClasses();
        controllerClasses = modelValidator.getHelper().filterWithStereotypeName(allClasses, StrutsScriptHelper.ASPECT_FRONT_END_CONTROLLER_CLASS);
    }

    public Collection validate()
    {
        validationMessages.clear();

        Iterator iterator = controllerClasses.iterator();
        while (iterator.hasNext())
        {
            UmlClass umlClass = (UmlClass) iterator.next();
            validate(umlClass);
        }

        return Collections.unmodifiableCollection(validationMessages);
    }

    /**
     * May be associated to only one form bean
     */
    private void validate(UmlClass umlClass)
    {
        UMLStaticHelper staticHelper = modelValidator.getHelper().getStaticHelper();
        Collection associationEnds = staticHelper.getAssociationEnds(umlClass);

        int formCounter = 0;
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            DirectionalAssociationEnd directionalAssociationEnd = staticHelper.getAssociationData(associationEnd);
            Classifier participant = directionalAssociationEnd.getTarget().getParticipant();
            if (staticHelper.getStereotypeNames(participant).contains(StrutsScriptHelper.ASPECT_FRONT_END_MODEL))
            {
                formCounter++;
            }
        }

        if (formCounter > 1)
        {
            validationMessages.add(new ValidationWarning(umlClass, "A controller class should have not more than one associated form class, found "+formCounter));
        }
    }
}
