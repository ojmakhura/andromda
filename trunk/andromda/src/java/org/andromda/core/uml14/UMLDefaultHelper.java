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
 * Implements the minimum set of API, for UML v1.4 based models, needed
 * by AndroMDA to perform code generation.
 * 
 * @author Anthony Mowers
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
	

	public String getName(Object object)
	{
		if ((object == null) || !(object instanceof ModelElement))
		{
			return null;
		}
		ModelElement modelElement = (ModelElement) object;

		return modelElement.getName();
	}


	public String getPackageName(Object object)
	{
		if ((object == null) || !(object instanceof ModelElement))
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
	
	public Collection getModelElements()
	{
		return model.getCore().getModelElement().refAllOfType();
	}

	public Collection getStereotypeNames(Object object)
	{
		if ((object == null) || !(object instanceof ModelElement))
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
