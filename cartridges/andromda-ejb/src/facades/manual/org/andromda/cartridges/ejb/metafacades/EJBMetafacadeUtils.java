package org.andromda.cartridges.ejb.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * Contains utilities for 
 * use with EJB metafacades.
 * 
 * @author Chad Brandon
 */
class EJBMetafacadeUtils {
    
    /**
     * Gets all create methods for the given <code>classifier</code>.
     * 
     * @param follow if true, all super type create methods are also retrieved
     * @return Collection of create methods found.
     */
    public static Collection getCreateMethods(ClassifierFacade classifier, boolean follow) {
        final String methodName = "getCreateMethods";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        Collection retval = new ArrayList();
        EJBEntityFacade entity = null;
        do {
            Collection ops = classifier.getOperations();
            for (Iterator i = ops.iterator(); i.hasNext();) {
                OperationFacade op = (OperationFacade) i.next();
                if (op.hasStereotype(EJBProfile.STEREOTYPE_CREATE_METHOD)) {
                    retval.add(op);
                }
            }
            if (follow) {
                entity = (EJBEntityFacade)classifier.getGeneralization();
            } else {
                break;
            }
        } while (entity != null);
        return retval;
    }
    
    /**
     * Gets the interface name for the passed in <code>classifier</code>.
     * Returns 'LocalHome' if the mode element has the entity stereotype,
     * returns 'Home' otherwise.
     * 
     * @return the interface name.
     */
    static String getHomeInterfaceName(ClassifierFacade classifier) {
        final String methodName = "getHomeInterfaceName";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        String homeInterfaceName;
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_ENTITY)) {
            homeInterfaceName = classifier.getName() + "LocalHome";
        } else {
            homeInterfaceName = classifier.getName() + "Home";
        }
        return homeInterfaceName;
    }

    /**
     * Gets the view type for the passed in <code>classifier</code>. 
     * Returns 'local' if the model element has the entity
     * stereotype, returns 'remote' otherwise.
     * 
     * @return String the view type name.
     */
    static String getViewType(ClassifierFacade classifier) {
        final String methodName = "getViewType";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_ENTITY)) {
            return "local";
        }
        return "remote";
    }

    
}
