package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.common.DbMappingTable;
import org.andromda.core.common.ScriptHelper;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.modelmanagement.Model;

/**
 * @author Anthony Mowers
 *
 * 
 */
public class UMLDefaultHelper
	implements ScriptHelper
{
	protected UmlPackage model;
	protected DbMappingTable typeMappings;

	public void setModel(Object model)
	{
		this.model = (UmlPackage) model;
	}
	
    public Object getModel()
    {
        return this.model;
    }
    
	public void setTypeMappings(DbMappingTable mappings)
	{
		this.typeMappings = mappings;
	}
	
	/**
	 *  Description of the Method
	 *
	 *@param  object  Description of the Parameter
	 *@return         Description of the Return Value
	 */
	public String getName(Object object)
	{
		if ((object == null) | !(object instanceof ModelElement))
		{
			return null;
		}
		ModelElement modelElement = (ModelElement) object;

		return modelElement.getName();
	}


	
	/**
	 *  Description of the Method
	 *
	 *@param  object  Description of the Parameter
	 *@return         Description of the Return Value
	 */
	public String getPackageName(Object object)
	{
		if ((object == null) | !(object instanceof ModelElement))
		{
			return null;
		}

		ModelElement modelElement = (ModelElement) object;
		String packageName = "";

		for (ModelElement namespace = modelElement.getNamespace();
			(namespace instanceof
			org.omg.uml.modelmanagement.UmlPackage)
			 && !
			(namespace instanceof Model);
			namespace = namespace.getNamespace())
		{
			packageName = "".equals(packageName) ?
				namespace.getName()
				 : namespace.getName() + "." + packageName;
		}

		return packageName;
	}
	
	/**
	 *  Gets the modelElements attribute of the UMLScriptHelper object
	 *
	 *@return    The modelElements value
	 */
	public Collection getModelElements()
	{
		return model.getCore().getModelElement().refAllOfType();
	}

	/**
	 *  Description of the Method
	 *
	 *@param  object  Description of the Parameter
	 *@return         Description of the Return Value
	 */
	public Collection getStereotypeNames(Object object)
	{
		if ((object == null) | !(object instanceof ModelElement))
		{
			return Collections.EMPTY_LIST;
		}

		ModelElement modelElement = (ModelElement) object;
		Collection stereoTypeNames = new Vector();

		Collection stereotypes = modelElement.getStereotype();
		for (Iterator i = stereotypes.iterator(); i.hasNext(); )
		{
			ModelElement stereotype = (ModelElement) i.next();
			stereoTypeNames.add(stereotype.getName());
		}

		return stereoTypeNames;
	}


}
