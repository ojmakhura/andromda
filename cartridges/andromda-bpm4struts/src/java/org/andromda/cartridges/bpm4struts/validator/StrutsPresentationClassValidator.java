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

public class StrutsPresentationClassValidator
{
    private StrutsModelValidator modelValidator = null;
    private Collection presentationClasses = null;
    private final Collection validationMessages = new LinkedList();

    public StrutsPresentationClassValidator(StrutsModelValidator modelValidator)
    {
        this.modelValidator = modelValidator;
        Collection allClasses = modelValidator.getHelper().getStaticHelper().getAllClasses();
        presentationClasses = modelValidator.getHelper().filterWithStereotypeName(allClasses, StrutsScriptHelper.ASPECT_FRONT_END_MODEL);
    }

    public Collection validate()
    {
        validationMessages.clear();

        Iterator iterator = presentationClasses.iterator();
        while (iterator.hasNext())
        {
            UmlClass umlClass = (UmlClass) iterator.next();
            validate(umlClass);
        }

        return Collections.unmodifiableCollection(validationMessages);
    }

    private void validate(UmlClass umlClass)
    {
        // should be associated to a controller class
        UMLStaticHelper staticHelper = modelValidator.getHelper().getStaticHelper();

        short linkCount = 0;
        Collection associationEnds = staticHelper.getAssociationEnds(umlClass);
        for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
        {
            AssociationEnd associationEnd = (AssociationEnd) iterator.next();
            DirectionalAssociationEnd directionalAssociationEnd = staticHelper.getAssociationData(associationEnd);
            Classifier participant = directionalAssociationEnd.getTarget().getParticipant();
            if (staticHelper.getStereotypeNames(participant).contains(StrutsScriptHelper.ASPECT_FRONT_END_CONTROLLER_CLASS))
            {
                linkCount++;
            }
        }

        if (linkCount == 0)
        {
            validationMessages.add(new ValidationWarning(umlClass, "Presentation class not linked to a controller class"));
        }
    }
}
