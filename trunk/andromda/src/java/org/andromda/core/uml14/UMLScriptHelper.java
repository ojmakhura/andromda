package org.andromda.core.uml14;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StringUtilsHelper;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.StructuralFeature;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;

/**
 *  Description of the Class
 *
 *@author    Anthony Mowers
 */
public class UMLScriptHelper extends UMLBaseHelper implements ScriptHelper
{
	private final static String PRIMARY_KEY = "PrimaryKey";
	private final static String ENTITY_BEAN = "EntityBean";

	/**
	 *  Gets the primaryKeyAttribute attribute of the UMLScriptHelper object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The primaryKeyAttribute value
	 */
	public Attribute getPrimaryKeyAttribute(Object object)
	{
		Collection attributes = getAttributes(object);
		for (Iterator i = attributes.iterator(); i.hasNext();)
		{
			Object attribute = i.next();
			if (getStereotypeNames(attribute).contains(PRIMARY_KEY))
			{
				return (Attribute) attribute;
			}
		}

		return null;
	}

	/**
	 *  Description of the Method
	 *
	 *@param  object  Description of the Parameter
	 *@return         Description of the Return Value
	 */
	public String findFullyQualifiedName(Object object)
	{
		if ((object == null) | !(object instanceof ModelElement))
		{
			return null;
		}

		ModelElement modelElement = (ModelElement) object;

		String fullName = modelElement.getName();
		String packageName = findPackageName(modelElement);
		fullName =
			"".equals(packageName) ? fullName : packageName + "." + fullName;

		return fullName;
	}

	public Collection getTaggedValues(Object object)
	{
		if ((object == null) | !(object instanceof ModelElement))
		{
			return Collections.EMPTY_LIST;
		}

		ModelElement modelElement = (ModelElement) object;

		return modelElement.getTaggedValue();
	}

	public String getOperationSignature(Object object)
	{
		if ((object == null) | !(object instanceof Operation))
		{
			return null;
		}

		Operation o = (Operation) object;
		Iterator it = o.getParameter().iterator();
		if (!it.hasNext())
		{
			return "void " + o.getName() + "()";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(" ");
		sb.append(o.getName());
		sb.append("(");

        boolean commaNeeded = false;
		while (it.hasNext())
		{
            Parameter p = (Parameter) it.next();
            
            String type;
            if (p.getType() == null)
            {
                type = "int";
            } else {
                type = findFullyQualifiedName(p.getType());
            }
            
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                sb.insert(0,type);
            } else {
                if (commaNeeded)
                {
                    sb.append(", ");
                }
                sb.append(type);
                sb.append(" ");
                sb.append(p.getName());
                commaNeeded = true;
            } 
                
		}
		sb.append(")");

		return sb.toString();
	}

	public DirectionalAssociationDescriptor getAssociationData(Object object)
	{
		if ((object == null) | !(object instanceof AssociationEnd))
		{
			return null;
		}
		
		AssociationEnd ae = (AssociationEnd)object;
		
		return new DirectionalAssociationDescriptor(this,ae);
	}

	
	/**
	 *  Gets the classifierProxy attribute of the UMLScriptHelper object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The classifierProxy value
	 */
	public Object wrapInProxy(Object object)
	{
		if ((object == null) | !(object instanceof Classifier))
		{
			return null;
		}

		return ClassifierProxy.newInstance(this, (Classifier) object);
	}

	public String getEjbRefViewType(Object object)
	{
		if (ENTITY_BEAN.equals(getStereotype(object)))
		{
			return "local";
		}
		
		return "remote";
	}
	
	public String findTaggedValue(ModelElement modelElement, String tagName)
	{
		Collection taggedValues = getTaggedValues(modelElement);
		for (Iterator i = taggedValues.iterator(); i.hasNext(); )
		{
			TaggedValue taggedValue = (TaggedValue)i.next();
			if (tagName.equals(taggedValue.getName()))
			{
				Iterator it = taggedValue.getDataValue().iterator();
				if (it.hasNext())
				{
					return it.next().toString();
				}
				return null;
			}
		}
		
		return null;
	}
		
	public String findTaggedValue(
		StructuralFeature feature, String tagName, boolean follow)
	{
        if (feature == null) return null;
        
		String value = findTaggedValue(feature,tagName);
		ModelElement element = feature.getType();
		while ( ( value == null ) & (element != null) ) 
		{
			value = findTaggedValue(element,tagName);
			element = getGeneralization(element);
		}
			
		
		return value;
	}
	
	/**
     * <p>Returns the JDBC type for an attribute.  It gets the type
     * from the tag <code>uml2ejb:JDBCType</code> for this.</p>
     *
     * @param attribute the attribute
     * @return String the string to be used with JDBC
     */
    public String findAttributeJDBCType(Attribute attribute)
    {
        if (attribute == null) return null;

        String value = findTaggedValue(attribute, "uml2ejb:JDBCType", true);
        
        if (null == value)
        {
            Object type = attribute.getType();
            value = findFullyQualifiedName(type);
            if (typeMappings != null)
            {
                value = typeMappings.getJDBCType(value);
            }
        }
        
        return value;
    }
    
    /**
     * <p>Returns the SQL type for an attribute.  Normally it gets the
     * type from the tag <code>uml2ejb:SQLType</code>.  If this tag
     * doesn't exist, it uses {@link
     * #findAttributeSQLFieldLength(Attribute)
     * findAttributeSQLFieldLength()} and combines it's result with
     * the standard SQL type for the attributes type from the
     * type mapping configuration file.</p>
     *
     * @param attribute the attribute
     * @return String the string to be used as SQL type
     */
    public String findAttributeSQLType(Attribute attribute)
    {
        String value = findTaggedValue(attribute, "uml2ejb:SQLType", true);
        
        if (null == value)
        {
            Object type = attribute.getType();
            String typeName = findFullyQualifiedName(type);
            value = this.typeMappings.getSQLType(typeName, value);
        }
        return value;
    }
    
     /**
     * <p>Returns the length for the SQL type of an attribute.  It
     * gets the length from the tag
     * <code>uml2ejb:SQLFieldLength</code>.  This might return "50"
     * for a VARCHAR field or "12,2" for a DECIMAL field.</p>
     *
     * @param attribute the attribute
     * @return String the length of the underlying SQL field
     */
    public String findAttributeSQLFieldLength(Attribute attribute)
    {
        // String value = findAttributeTagValue(attribute, "uml2ejb:SQLFieldLength", true);
        String value = null;
        return value;
    }
    
	/**
	 *  Gets the homeInterfaceName attribute of the UMLScriptHelper object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The homeInterfaceName value
	 */
	public String getHomeInterfaceName(Object object)
	{
		if (getStereotypeNames(object).contains(ENTITY_BEAN))
		{
			return getName(object) + "LocalHome";
		}

		return getName(object) + "Home";
	}

    public String getComponentInterfaceName(Object object)
    {
        if (getStereotypeNames(object).contains(ENTITY_BEAN))
        {
            return getName(object) + "Local";
        }

        return getName(object);
    }
    
	public Object findClassById(Object object)
	{
		if (object instanceof Classifier)
		{
			return object;
		}
		
		return null;
	}
	
	/**
	 *  Gets the usages attribute of the UMLScriptHelper object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The usages value
	 */
	public Collection getDependencies(Object object)
	{
		if ((object == null) | !(object instanceof ModelElement))
		{
			return Collections.EMPTY_LIST;
		}

		ModelElement modelElement = (ModelElement) object;

		Collection clientDependencies =
			model.getCore().getAClientClientDependency().getClientDependency(
				modelElement);

		return new FilteredCollection(clientDependencies)
		{
			protected boolean accept(Object object)
			{
				return 
					(object instanceof Dependency) & !(object instanceof Abstraction);
			}
		};
	}

	/**
	 *  Description of the Method
	 *
	 *@param  object  Description of the Parameter
	 *@return         Description of the Return Value
	 */
	public String findPackageName(Object object)
	{
		return getPackageName(object);
	}

	/**
	 *  Gets the attributes attribute of the UMLScriptHelper object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The attributes value
	 */
	public Collection getAttributes(Object object)
	{
		if ((object == null) | !(object instanceof Classifier))
		{
			return Collections.EMPTY_LIST;
		}

		Classifier classifier = (Classifier) object;
		Collection features = new FilteredCollection(classifier.getFeature())
		{
			protected boolean accept(Object object)
			{
				return object instanceof Attribute;
			}
		};

		return features;
	}

	/**
	 *  Gets the attributes attribute of the UMLScriptHelper object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The attributes value
	 */
	public Collection getOperations(Object object)
	{
		if ((object == null) | !(object instanceof Classifier))
		{
			return Collections.EMPTY_LIST;
		}

		Classifier classifier = (Classifier) object;
		Collection features = new FilteredCollection(classifier.getFeature())
		{
			protected boolean accept(Object object)
			{
				return object instanceof Operation;
			}
		};

		return features;
	}

	/**
	 *  Constructor for the getAssociations object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The associations value
	 */
	public Collection getAssociationEnds(Object object)
	{
		if ((object == null) | !(object instanceof Classifier))
		{
			return Collections.EMPTY_LIST;
		}

		Classifier classifier = (Classifier) object;
		return model.getCore().getAParticipantAssociation().getAssociation(
			classifier);
	}

	public GeneralizableElement getGeneralization(Object object)
	{
		if ((object == null) | !(object instanceof GeneralizableElement))
		{
			return null;
		}
		
		GeneralizableElement element = (GeneralizableElement) object;
		Iterator i = 
			model.getCore().getAChildGeneralization().getGeneralization(element).
			iterator();
		if (i.hasNext())
		{
			return (GeneralizableElement)i.next();
		}
			
		return null;
	}
	
	/**
	 *  Gets the abstractionClientEnds attribute of the UMLScriptHelper object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The abstractionClientEnds value
	 */
	public Collection getAbstractions(Object object)
	{
		if ((object == null) | !(object instanceof Classifier))
		{
			return Collections.EMPTY_LIST;
		}

		ModelElement modelElement = (ModelElement) object;

		Collection clientDependencies =
			model.getCore().getAClientClientDependency().getClientDependency(
				modelElement);

		return new FilteredCollection(clientDependencies)
		{
			public boolean add(Object object)
			{
				Abstraction abstraction = (Abstraction) object;
				return super.add(abstraction.getSupplier().iterator().next());
			}

			protected boolean accept(Object object)
			{
				return object instanceof Abstraction;
			}
		};
	}

	public String getAttributesAsList(
		Object object,
		boolean withTypeNames,
		boolean includePK)
	{
		StringBuffer sb = new StringBuffer();
		String separator = "";
		sb.append("(");

		for (Iterator it = getAttributes(object).iterator(); it.hasNext();)
		{
			Attribute a = (Attribute) it.next();
            
			// check if attribute is the PK of this class
			// and include it only if includePK is true.
			if (includePK || !getStereotypeNames(a).contains(PRIMARY_KEY))
			{
				sb.append(separator);
				if (withTypeNames)
				{
					String typeName = findFullyQualifiedName(a.getType());
					sb.append(typeName);
					sb.append(" ");
					sb.append(a.getName());
				}
				else
				{
					sb.append("get");
					sb.append(
						StringUtilsHelper.upperCaseFirstLetter(a.getName()));
					sb.append("()");
				}
				separator = ", ";
			}
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Provided only for backward compatability with old velocity scripts.
	 * In truth all model elements can be assigned more than one stereotype.
	 * 
	 * @param object
	 * @return String
	 */
	public String getStereotype(Object object)
	{
		Iterator i = getStereotypeNames(object).iterator();

		if (i.hasNext())
		{
			String stereotype = (String) i.next();

			return stereotype;
		}

		return "";

	}

	/**
	 *  Description of the Class
	 *
	 *@author    amowers
	 */
	private abstract static class FilteredCollection extends Vector
	{
		/**
		 *  Constructor for the FilterCollection object
		 *
		 *@param  c  Description of the Parameter
		 */
		public FilteredCollection(Collection c)
		{
			for (Iterator i = c.iterator(); i.hasNext();)
			{
				Object object = i.next();
				if (accept(object))
				{
					add(object);
				}
			}
		}

		/**
		 *  Description of the Method
		 *
		 *@param  object  Description of the Parameter
		 *@return         Description of the Return Value
		 */
		protected abstract boolean accept(Object object);
	}

	public Object convertToType(Object object)
    {
        return object;
    }
	
}
