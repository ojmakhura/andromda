package org.andromda.metafacades.uml;

import java.util.Collection;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * A class containing utlities for metafacade manipulation.
 * 
 * @author Chad Brandon
 */
public class MetafacadeUtils {

	/**
	 * Filters out the model elements from the <code>modelElements</code> collection
	 * that don't have the specified <code>stereotype</code>
	 * @param modelElements the model elements to filter.
	 * @param stereotype the stereotype that a model element must have
	 *        in order to stay remain within the <code>modelElements</code>
	 *        collection.
	 */
	public static void filterByStereotype(Collection modelElements, final String stereotype) {
		final String methodName = "MetafacadeUtils.filterByStereotype";
		ExceptionUtils.checkEmpty(methodName, "stereotype", stereotype);
		class StereotypeFilter implements Predicate {
			public boolean evaluate(Object object) {
				return ((ModelElementFacade)object).hasStereotype(
						stereotype);
			}
		}	
		CollectionUtils.filter(modelElements, new StereotypeFilter());
	}
	
    /**
     * <p>Returns a consistent name for a relation, independent from
     * the end of the relation one is looking at.</p>
     *
     * <p>In order to guarantee consistency with relation names, they
     * must appear the same whichever angle (ie entity) that you come
     * from.  For example, if you are at Customer end of a
     * relationship to an Address then your relation may appear with
     * the name Customer-Address.  But if you are in the Address
     * entity looking at the Customer then you will get an error
     * because the relation will be called Address-Customer.  A simple
     * way to guarantee that both ends of the relationship have the
     * same name is merely to use alphabetical ordering.</p>
     *
     * @param roleName       name of role in relation
     * @param targetRoleName name of target role in relation
     * @param separator      character used to separate words
     * @return uniform mapping name (in alphabetical order)
     */
    public static String toRelationName(
    	String roleName, 
		String targetRoleName,
        String separator) {
        if (roleName.compareTo(targetRoleName) <= 0) {
            return (roleName + separator + targetRoleName);
        }

        return (targetRoleName + separator + roleName);
    }

	
}
