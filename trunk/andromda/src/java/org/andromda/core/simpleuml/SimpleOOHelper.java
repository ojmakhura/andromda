package org.andromda.core.simpleuml;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;

/**
 * This script helper simulates the old-style UML2EJB SimpleOO based
 * script helper, and is provided mostly for backward compatability
 * with the UML2EJB code generation scripts - it would be nice to 
 * deprecate it at some point, but at present there is no plan to do so.
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers</A>
 */
public class SimpleOOHelper extends UMLStaticHelper 
{
    private final static String PRIMARY_KEY = "PrimaryKey";
    private final static String ENTITY_BEAN = "EntityBean";
    
    public Object getModel()
    {
        return PModel.newInstance(this,this.model);
    }
    
    public Collection getModelElements()
    {
        Collection elements = new Vector();
        for (Iterator i= super.getModelElements().iterator();i.hasNext();)
        {
            Object o = i.next();
            if (o instanceof Classifier )
            {
                o = PClassifier.newInstance(this,(Classifier)o);
            }
            elements.add(o);
        }
        
        return elements;
    }
    
    
    public org.andromda.core.uml14.DirectionalAssociationEnd getAssociationData(Object object)
    {
        if ((object == null) || !(object instanceof AssociationEnd))
        {
            return null;
        }
        
        AssociationEnd ae = (AssociationEnd)object;
        
        return new DirectionalAssociationEnd(this,ae);
    }

    
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
	 * Returns a string indicating whether the Bean is
     * a local or remotely accessable bean.
     * 
	 * @param object Bean class
	 * @return String 'local' or 'remote'
	 */
    public String getEjbRefViewType(Object object)
    {
        if (ENTITY_BEAN.equals(getStereotype(object)))
        {
            return "local";
        }
        
        return "remote";
    }
       
    /**
     * Returns a string representing the name of the
     * home interface for the Bean.
     *
     *@param  object bean class
     *@return string homeInterfaceName 
     */
    public String getHomeInterfaceName(Object object)
    {
        if (getStereotypeNames(object).contains(ENTITY_BEAN))
        {
            return getName(object) + "LocalHome";
        }

        return getName(object) + "Home";
    }

	/**
	 * Returns a string representing the component name
     * for the Bean appending the 'local' suffix if the
     * Bean is locally accessable.
     * 
	 * @param object
	 * @return String
	 */
    public String getComponentInterfaceName(Object object)
    {
        if (getStereotypeNames(object).contains(ENTITY_BEAN))
        {
            return getName(object) + "Local";
        }

        return getName(object);
    }
    
    
	/**
	 * Returns a list of attributes for a class. The list is
     * useful for generating method signatures for constructors and/or
     * generating code for calling such a constructor
     * 
	 * @param object class object
	 * @param withTypeNames should attribute types appear in the list
	 * @param includePK should primary key be included in the list
	 * @return String representation of attribute list
	 */
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
	 * returns a string representation for the Java signature for
     * a given operation
     * 
	 * @param model element representing the operation
	 * @return String representation of the operation signature
	 */
    public String getOperationSignature(Object object)
    {
        if ((object == null) || !(object instanceof Operation))
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
                type = getFullyQualifiedName(p.getType());
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
	 * Returns the fully qualified name of the given
     * model element.  The fully qualified name includes
     * complete package qualified name of the model element.
     * 
	 * @param object model element
	 * @return String fully qualified name
	 */
    public String findFullyQualifiedName(Object object)
    {
        return getFullyQualifiedName(object);
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

        String value = findTagValue(attribute, "uml2ejb:JDBCType", true);
        
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
        String value = findTagValue(attribute, "uml2ejb:SQLType", true);
        
        if (null == value)
        {
            Object type = attribute.getType();
            String typeName = findFullyQualifiedName(type);
            value = this.typeMappings.getSQLType(typeName, value);
        }
        return value;
    }
    
     /**
     * Returns the name of the package that contains the
     * given model element.
     *
     *@param  object  model element
     *@return  fully qualified name of the package
     */
    public String findPackageName(Object object)
    {
        return getPackageName(object);
    }
    
	/**
	 * Provided only for backward compatability with 
     * UML2EJB code generation scripts.  It does nothing
     * in this implementation except return the object that is passed
     * into it.
     * 
	 * @param object
	 * @return Object
	 */
    public Object findClassById(Object object)
    {
        if (object instanceof Classifier)
        {
            return object;
        }
        
        return null;
    }
    
	/**
	 * Provided only for backward compatability with UML2EJB
     * code generation scripts.   It does not except
     * return the object that it was passed in.
     * 
	 * @param object a model element
	 * @return the model element that was passed
	 */
    public Object convertToType(Object object)
    {
        return object;
    }
    
}
