
import java.util.Collection;

import org.andromda.core.simpleuml.SimpleOOHelper;
import org.omg.uml.modelmanagement.Model;

/**
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 *
 * Adds a stereotype to the UML model at runtime so that AndroMDA code generation
 * can be triggered even though no model elements in the original UML have any
 * stereotypes.
 */
public class ReportHelper 
    extends SimpleOOHelper
{
    static final String MODEL_STEREOTYPE = "model";
    
	/**
	 * @see org.andromda.core.common.ScriptHelper#getStereotypeNames(Object)
	 */
	public Collection getStereotypeNames(Object modelElement) {
		Collection c = super.getStereotypeNames(modelElement);
        
        // If this model element is the model then dynamically add a stereotype
        if (modelElement instanceof Model)
        {
            c.add(MODEL_STEREOTYPE);
        }
        return c;
	}

}
