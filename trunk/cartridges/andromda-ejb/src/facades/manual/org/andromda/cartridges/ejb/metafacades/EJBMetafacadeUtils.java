package org.andromda.cartridges.ejb.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
        final String methodName = "EJBMetafacadeUtils.getCreateMethods";
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
        final String methodName = "EJBMetafacadeUtils.getHomeInterfaceName";
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
        final String methodName = "EJBMetafacadeUtils.getViewType";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        if (classifier.hasStereotype(UMLProfile.STEREOTYPE_ENTITY)) {
            return "local";
        }
        return "remote";
    }
    
    /**
     * Gets all the inherited instance attributes, excluding
     * the instance attributes directory from this <code>classifier</code>.
     * @param classifer the ClassifierFacade from which to retrieve the
     *        inherited attributes.
     * @return a list of ordered attributes.
     */
    static List getInheritedInstanceAttributes(ClassifierFacade classifier) {
        final String methodName = "EJBMetafacadeUtils.getInheritedInstanceAttributes";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        ClassifierFacade current = (EJBEntityFacade)classifier.getGeneralization();
        if (current == null) {
            return new ArrayList();
        } else {
            List retval = getInheritedInstanceAttributes(current);
            return retval;
        }
    }
    
    /**
     * Gets all instance attributes including those instance attributes
     * belonging to the <code>classifier</code> and any inherited ones.
     * @param classifier the ClassifierFacade from which to retrieve the instance
     *        attributes.
     * @return the list of all instance attributes.
     */
    static List getAllInstanceAttributes(ClassifierFacade classifier) {
        final String methodName = "EJBMetafacadeUtils.getAllInstanceAttributes";
        ExceptionUtils.checkNull(methodName, "classifer", classifier);
        List retval = getInheritedInstanceAttributes(classifier);
        retval.addAll(classifier.getInstanceAttributes());
        return retval;      
    }
    
}
