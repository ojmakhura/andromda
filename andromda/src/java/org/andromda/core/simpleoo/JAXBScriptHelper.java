package org.andromda.core.simpleoo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.andromda.core.common.DbMappingTable;
import org.andromda.core.common.HTMLAnalyzer;
import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.xml.Association;
import org.andromda.core.xml.AssociationLink;
import org.andromda.core.xml.Attribute;
import org.andromda.core.xml.Datatype;
import org.andromda.core.xml.Interface;
import org.andromda.core.xml.Klass;
import org.andromda.core.xml.Model;
import org.andromda.core.xml.Operation;
import org.andromda.core.xml.Pakkage;
import org.andromda.core.xml.Parameter;
import org.andromda.core.xml.Stereotype;
import org.andromda.core.xml.TaggedValue;
import org.andromda.core.xml.Type;

/**
 * <p>Transforms model information into a form that is easily
 * accessible by a Velocity script.</p>
 * 
 * @author Matthias Bohlen
 */
public class JAXBScriptHelper
    implements 
    	Object2SimpleOOConverter,
    	ScriptHelper
{

	public void setModel(Object model)
	{
		this.model = (Model)model;
	}
	
    public Object getModel()
    {
        return this.model;
    }
	public void setTypeMappings(DbMappingTable typeMappings)
	{
		this.typeMappings = (DbMappingTable)typeMappings;
	}
	
	/**
	 *  Gets the stereoTypeNames attribute of the MetaDataRepository object
	 *
	 *@param  object  Description of the Parameter
	 *@return         The stereoTypeNames value
	 */
	public Collection getStereotypeNames(Object object)
	{
		Type type = (Type) object;

		Vector stereoTypeNames = new Vector();
		stereoTypeNames.add(getStereotype(type.getId()));

		return stereoTypeNames;
	}
	
	public String getPackageName(Object object)
	{
		Type type = (Type) object;

		return findPackageName(type.getPackage());
	}
	
	public Collection getModelElements()
	{
		Collection modelElements = new Vector();
		Model m = this.model;

		for (Iterator it = m.getClasses().iterator(); it.hasNext(); )
		{
			modelElements.add(it.next());
		}
		// for all classes defined at model level

		for (Iterator it = m.getInterfaces().iterator(); it.hasNext(); )
		{
			modelElements.add(it.next());
		}
		// for all Interfaces defined at model level

		for (Iterator it = m.getDatatypes().iterator(); it.hasNext(); )
		{
			modelElements.add(it.next());
		}
		// for all datatypes defined at model level

		for (Iterator itPack = m.getPackages().iterator();
			itPack.hasNext();
			)
		{
			Pakkage p = (Pakkage) itPack.next();

			for (Iterator it = p.getClasses().iterator(); it.hasNext(); )
			{
				modelElements.add(it.next());
			}
			// for all classes in package

			for (Iterator it = p.getInterfaces().iterator(); it.hasNext(); )
			{
				modelElements.add(it.next());
			}
			// for all Interfaces in package

			for (Iterator it = p.getDatatypes().iterator(); it.hasNext(); )
			{
				modelElements.add(it.next());
			}
			// for all datatypes in package

		}
		// for all packages in model

		return modelElements;
	}
	
	public String getName(Object object)
	{
		Type type = (Type) object;

		return type.getName();
	}
	
    /**
     * <p>Searches for the correct association, starting at the
     * association link that was passed in as a parameter.  Returns
     * data about this association from the viewpoint of the
     * association end the association link comes from.</p>
     *
     * @param al the association link from which to access the
     *           association
     * @return DirectionalAssociationDescriptor easily accessible data
     *         about the association as seen from the given association link
     */
    public DirectionalAssociationDescriptor getAssociationData(AssociationLink al)
    {
        DirectionalAssociationDescriptor result = 
            new DirectionalAssociationDescriptor(al, this);

        return result;
    }

    /**
     * <p>Returns a wrapper class that allows for convenient access to
     * the data of the given association.</p>
     *
     * @param assoc the association which should be formatted
     * @return AssociationDescriptor easily accessible data about the
     *         given association
     */
    public AssociationDescriptor getAssociationData(Association assoc)
    {
        AssociationDescriptor result = 
            new AssociationDescriptor(assoc, this);

        return result;
    }

    /**
     * <p>Returns a wrapper class that allows for convenient access to
     * the data of the given association.</p>
     *
     * @param assocId the id of the association which should be
     *                formatted
     * @return AssociationDescriptor easily accessible data about the
     *         given association
     */
    public AssociationDescriptor getAssociationData(String assocId)
    {
        Association assoc = findAssociationById(assocId);
        if (null == assoc) {
            return null;
        }

        AssociationDescriptor result = 
            new AssociationDescriptor(assoc, this);

        return result;
    }

    /**
     * <p>Transforms the operation signature into a single string that
     * can easily be used from the Velocity script.</p>
     *
     * @param o the operation
     * @return String the complete signature
     */
    public String getOperationSignature(Operation o)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(findFullyQualifiedName(o.getType()));
        sb.append(" ");
        sb.append(o.getName());
        sb.append(" (");
        for (Iterator it = o.getParameters().iterator(); it.hasNext();)
        {
            Parameter p = (Parameter) it.next();
            sb.append(findFullyQualifiedName(p.getType()));
            sb.append(" ");
            sb.append(p.getName());
            if (it.hasNext())
            {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * <p>Builds a call to the operation with a list of parameters.
     * that can easily be used from the Velocity script.</p>
     * 
     * <p>This is good to generate business delegates.
     * See feature request #638931.</p>
     *
     * @param o the operation
     * @return String the complete call to the operation
     */
    public String getOperationCall(Operation o)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(o.getName());
        sb.append("(");
        for (Iterator it = o.getParameters().iterator(); it.hasNext();)
        {
            Parameter p = (Parameter) it.next();
            sb.append(p.getName());
            if (it.hasNext())
            {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * <p>Returns name of the stereotype in the model that references
     * this model element.</p>
     *
     * @param modelElementId id of the model element
     * @return String name of the stereotype for this model element
     */
    public String getStereotype(String modelElementId)
    {
        for (Iterator it = this.model.getStereotypes().iterator();
            it.hasNext();
            )
        {
            Stereotype st = (Stereotype) it.next();
            for (Iterator it2 = st.getExtendedElements().iterator();
                it2.hasNext();
                )
            {
                String idref = (String) it2.next();
                if (idref.equals(modelElementId))
                {
                    return st.getName();
                }
            }
        }
        return "";
    }

    /**
     * <p>Returns the first attribute of the type that has the
     * stereotype <code>PrimaryKey</code>.</p>
     *
     * @param type the type
     * @return Attribute the name of the primary key attribute
     */
    public Attribute getPrimaryKeyAttribute(Object type)
    {
        return getPrimaryKeyAttribute(type, false);
    }

    /**
     * <p>Returns all attributes of the type that have the stereotype
     * <code>PrimaryKey</code>.  This is for the cases where there may
     * be more than one primary key attribute.</p>
     *
     * @param type the type
     * @return Collection of primary key attributes
     */
    public Collection getPrimaryKeyAttributes(Object type)
    {
        return findAllAttributesWithStereotype(type, "PrimaryKey", false);
    }

    /**
     * <p>Returns the first attribute of the type that has the
     * stereotype <code>PrimaryKey</code>.</p>
     *
     * @param type the type
     * @param follow <code>true</code> if the search should be
     *               extended up the inheritance hierarchy;
     *               <code>false</code> otherwise
     * @return Attribute the name of the primary key attribute
     */
    public Attribute getPrimaryKeyAttribute(Object type, boolean follow)
    {
        return findAttributeWithStereotype(type, "PrimaryKey", follow);
    }

    /**
     * <p>Returns all attributes of the type that have the stereotype
     * <code>PrimaryKey</code>.  This is for the cases where there may
     * be more than one primary key attribute.</p>
     *
     * @param type the type
     * @param follow <code>true</code> if the search should be
     *               extended up the inheritance hierarchy;
     *               <code>false</code> otherwise
     * @return Collection of primary key attributes
     */
    public Collection getPrimaryKeyAttributes(Object type, boolean follow)
    {
        return findAllAttributesWithStereotype(type, "PrimaryKey", follow);
    }

    /**
     * <p>Returns the first attribute of the type that has the given
     * stereotype.</p>
     *
     * @param type the type
     * @param stereotype a <code>String</code> with the stereotype to
     *                   search for
     * @param follow <code>true</code> if the search should be
     *               extended up the inheritance hierarchy;
     *               <code>false</code> otherwise
     * @return Attribute the first attribute that has the given
     *                   stereotype or null if none is found
     */
    public Attribute findAttributeWithStereotype(
        Object type,
        String stereotype,
        boolean follow)
    {
        Type cl = convertToType(type);
        if (null == cl)
        {
            return null;
        }

        for (Iterator it = cl.getAttributes().iterator(); it.hasNext();)
        {
            Attribute att = (Attribute) it.next();
            if (stereotype.equals(getStereotype(att.getId())))
            {
                return att;
            }
        }

        if (follow)
        {
            for (Iterator it = cl.getSupertypes().iterator(); it.hasNext();)
            {
                Object superType = it.next();
                Attribute att =
                    findAttributeWithStereotype(superType, stereotype, follow);
                if (null != att)
                {
                    return att;
                }
            }
        }

        return null;
    }

    /**
     * <p>Returns all attributes of the type that have the given
     * stereotype.</p>
     *
     * @param type the type
     * @param stereotype a <code>String</code> with the stereotype to
     *                   search for
     * @param follow <code>true</code> if the search should be
     *               extended up the inheritance hierarchy;
     *               <code>false</code> otherwise
     * @return Collection all attributes that have the given
     *                    stereotype or null if type wasn't valid
     */
    public Collection findAllAttributesWithStereotype(
        Object type,
        String stereotype,
        boolean follow)
    {
        Type cl = convertToType(type);
        if (null == cl)
        {
            return null;
        }

        ArrayList attributes = new ArrayList();

        for (Iterator it = cl.getAttributes().iterator(); it.hasNext();)
        {
            Attribute att = (Attribute) it.next();
            if (stereotype.equals(getStereotype(att.getId())))
            {
                attributes.add(att);
            }
        }

        if (follow)
        {
            for (Iterator it = cl.getSupertypes().iterator(); it.hasNext();)
            {
                Object superType = it.next();
                Collection superAttributes =
                    findAllAttributesWithStereotype(
                        superType,
                        stereotype,
                        follow);
                if (null != superAttributes)
                {
                    attributes.addAll(superAttributes);
                }
            }
        }

        return attributes;
    }

    /**
     * <p>Returns the list of attributes as a single string that can
     * easily be used from the Velocity script to generate
     * declarations for <code>ejbCreate()</code> and
     * <code>ejbPostCreate()</code>.</p>
     * 
     * @param type the class
     * @param withTypeNames true, if type names should be included
     * @param includePK true, if PK field of this class should be included
     * @return String the list of attributes
     */
    public String getAttributesAsList(
        Object type,
        boolean withTypeNames,
        boolean includePK)
    {
        Type cl = convertToType(type);
        if (null == cl)
        {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        String separator = "";
        sb.append("(");

        for (Iterator it = cl.getAttributes().iterator(); it.hasNext();)
        {
            Attribute a = (Attribute) it.next();

            // check if attribute is the PK of this class
            // and include it only if includePK is true.
            if (includePK || !"PrimaryKey".equals(getStereotype(a.getId())))
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
     * <p>Returns a reference to the class with the specified name.
     * Drawback: Class names have to be unique within the model.</p>
     *
     * @param className the name of class
     * @return Klass the description for the class
     *
     * @deprecated use {@link #findClassById(String) findClassById()}
     *             or {@link #findClassByFullyQualifiedName(String) findClassByFullyQualifiedName()}
     *             instead as the name need not be unique.
     */
    public Klass findClassByName(String className)
    {
        for (Iterator it = model.getClasses().iterator(); it.hasNext();)
        {
            Klass cl = (Klass) it.next();
            if (cl.getName().equals(className))
            {
                return cl;
            }
        } // for all classes defined at model level

        for (Iterator itPack = model.getPackages().iterator();
            itPack.hasNext();
            )
        {
            Pakkage p = (Pakkage) itPack.next();

            for (Iterator it = p.getClasses().iterator(); it.hasNext();)
            {
                Klass cl = (Klass) it.next();
                if (cl.getName().equals(className))
                {
                    return cl;
                }
            } // for all classes in package
        } // for all packages in model

        return null;
    }

    /**
     * <p>Returns a reference to the type (that is a class, interface
     * or datatype) with the specified fullyQualifiedName.</p>
     *
     * @param fullyQualifiedName the fullyQualifiedName of the datatype
     * @return Type the description for the type
     */
    public Type findTypeByFullyQualifiedName(String fullyQualifiedName)
    {
        Type type;

        type = findClassByFullyQualifiedName(fullyQualifiedName);
        if (null != type)
        {
            return type;
        }
        type = findDatatypeByFullyQualifiedName(fullyQualifiedName);
        if (null != type)
        {
            return type;
        }
        type = findInterfaceByFullyQualifiedName(fullyQualifiedName);
        if (null != type)
        {
            return type;
        }

        return null;
    }

    /**
     * <p>Returns a reference to the datatype with the specified fully
     * qualified name.</p>
     *
     * @param datatypeName the fully qualified name of a datatype
     * @return Datatype the description for the datatype
     */
    public Datatype findDatatypeByFullyQualifiedName(String datatypeName)
    {
        for (Iterator it = model.getDatatypes().iterator(); it.hasNext();)
        {
            Datatype dt = (Datatype)it.next();
            if (findFullyQualifiedName(dt).equals(datatypeName))
            {
                return dt;
            }
        } // for all datatypes defined at model level

        for (Iterator itPack = model.getPackages().iterator();
            itPack.hasNext();
            )
        {
            Pakkage p = (Pakkage) itPack.next();

            for (Iterator it = p.getDatatypes().iterator(); it.hasNext();)
            {
                Datatype dt = (Datatype) it.next();
                if (findFullyQualifiedName(dt).equals(datatypeName))
                {
                    return dt;
                }
            } // for all datatypes in package
        } // for all packages in model

        return null;
    }

    /**
     * <p>Returns a reference to the interface with the specified fully
     * qualified name.</p>
     *
     * @param interfaceName the fully qualified name of a interface
     * @return Interface the description for the interface
     */
    public Interface findInterfaceByFullyQualifiedName(String interfaceName)
    {
        for (Iterator it = model.getInterfaces().iterator(); it.hasNext();)
        {
            Interface dt = (Interface)it.next();
            if (findFullyQualifiedName(dt).equals(interfaceName))
            {
                return dt;
            }
        } // for all interfaces defined at model level

        for (Iterator itPack = model.getPackages().iterator();
            itPack.hasNext();
            )
        {
            Pakkage p = (Pakkage) itPack.next();

            for (Iterator it = p.getInterfaces().iterator(); it.hasNext();)
            {
                Interface dt = (Interface) it.next();
                if (findFullyQualifiedName(dt).equals(interfaceName))
                {
                    return dt;
                }
            } // for all interfaces in package
        } // for all packages in model

        return null;
    }

    /**
     * <p>Returns a reference to the class with the specified fully
     * qualified name.</p>
     *
     * @param className the fully qualified name of a class
     * @return Klass the description for the class
     */
    public Klass findClassByFullyQualifiedName(String className)
    {
        for (Iterator it = model.getClasses().iterator(); it.hasNext();)
        {
            Klass cl = (Klass) it.next();
            if (findFullyQualifiedName(cl).equals(className))
            {
                return cl;
            }
        } // for all classes defined at model level

        for (Iterator itPack = model.getPackages().iterator();
            itPack.hasNext();
            )
        {
            Pakkage p = (Pakkage) itPack.next();

            for (Iterator it = p.getClasses().iterator(); it.hasNext();)
            {
                Klass cl = (Klass) it.next();
                if (findFullyQualifiedName(cl).equals(className))
                {
                    return cl;
                }
            } // for all classes in package
        } // for all packages in model

        return null;
    }

    /**
     * <p>Returns a reference to the type (that is a class, interface
     * or datatype) with the specified id.</p>
     *
     * @param id the id of the type
     * @return Type the description for the type
     */
    public Type findTypeById(String id)
    {
        Type cl;

        cl = findClassById(id);
        if (null != cl)
        {
            return cl;
        }
        cl = findDatatypeById(id);
        if (null != cl)
        {
            return cl;
        }
        cl = findInterfaceById(id);
        if (null != cl)
        {
            return cl;
        }
        return null;
    }

    /**
     * <p>Returns a reference to the datatype with the specified id.</p>
     *
     * @param datatypeId the id of the datatype
     * @return Datatype the description for the datatype
     */
    public Datatype findDatatypeById(String datatypeId)
    {
        for (Iterator it = model.getDatatypes().iterator(); it.hasNext();)
        {
            Datatype dt = (Datatype) it.next();
            if (dt.getId().equals(datatypeId))
            {
                return dt;
            }
        } // for all datatypes defined at model level

        for (Iterator itPack = model.getPackages().iterator();
            itPack.hasNext();
            )
        {
            Pakkage p = (Pakkage) itPack.next();

            for (Iterator it = p.getDatatypes().iterator(); it.hasNext();)
            {
                Datatype dt = (Datatype) it.next();
                if (dt.getId().equals(datatypeId))
                {
                    return dt;
                }
            } // for all datatypes in package
        } // for all packages in model

        return null;
    }

    /**
     * <p>Returns a reference to the interface with the specified id.</p>
     *
     * @param interfaceId the id of the interface
     * @return Interface the description for the interface
     */
    public Interface findInterfaceById(String interfaceId)
    {
        for (Iterator it = model.getInterfaces().iterator(); it.hasNext();)
        {
            Interface inter = (Interface) it.next();
            if (inter.getId().equals(interfaceId))
            {
                return inter;
            }
        } // for all interfaces defined at model level

        for (Iterator itPack = model.getPackages().iterator();
            itPack.hasNext();
            )
        {
            Pakkage p = (Pakkage) itPack.next();

            for (Iterator it = p.getInterfaces().iterator(); it.hasNext();)
            {
                Interface inter = (Interface) it.next();
                if (inter.getId().equals(interfaceId))
                {
                    return inter;
                }
            } // for all interfaces in package
        } // for all packages in model

        return null;
    }

    /**
     * <p>Returns a reference to the class with the specified id.</p>
     *
     * @param classId the id of the class
     * @return Klass the description for the class
     */
    public Klass findClassById(String classId)
    {
        for (Iterator it = model.getClasses().iterator(); it.hasNext();)
        {
            Klass cl = (Klass) it.next();
            if (cl.getId().equals(classId))
            {
                return cl;
            }
        } // for all classes defined at model level

        for (Iterator itPack = model.getPackages().iterator();
            itPack.hasNext();
            )
        {
            Pakkage p = (Pakkage) itPack.next();

            for (Iterator it = p.getClasses().iterator(); it.hasNext();)
            {
                Klass cl = (Klass) it.next();
                if (cl.getId().equals(classId))
                {
                    return cl;
                }
            } // for all classes in package
        } // for all packages in model

        return null;
    }

    /**
     * <p>Returns a reference to the package with the specified id.</p>
     *
     * @param packageId the id of the package
     * @return Pakkage the description for the package
     */
    public Pakkage findPackageById(String packageId)
    {
        for (Iterator itPack = model.getPackages().iterator();
            itPack.hasNext();
            )
        {
            Pakkage p = (Pakkage) itPack.next();

            if (p.getId().equals(packageId))
            {
                return p;
            }
        } // for all packages in model

        return null;
    }

    /**
     * <p>Returns a reference to the association with the specified id.</p>
     *
     * @param associationId the id of the association
     * @return Association the description for the association
     */
    public Association findAssociationById(String associationId)
    {
        for (Iterator it = model.getAssociations().iterator();
            it.hasNext();
            )
        {
            Association assoc = (Association) it.next();

            if (assoc.getId().equals(associationId))
            {
                return assoc;
            }
        } // for all associations in model

        return null;
    }

    /**
     * <p>Returns the fully qualified name of a type
     * (that is a class, interface or datatype).</p>
     *
     * @param type the <code>Type</code> for which the fully
     *             qualified name should be returned or a
     *             <code>String</code> with it's id
     * @return a <code>String</code> with the fully qualified name for
     *         the Type
     */
    public String findFullyQualifiedName(Object type)
    {
        // Special case "null" as sometimes there is no return type
        // given for procedures ==> "void" in Java
        if (null == type)
        {
            return "void";
        }

        Type cl = convertToType(type);
        if (null == cl)
        {
            return null;
        }

        String name = cl.getName();
        String packageName = findPackageName(cl.getPackage());

        // Special case the Java primitive types as ArgoUML seems to
        // represent them as classes within packages. :-(
        if (StringUtilsHelper.isPrimitiveType(name))
        {
            return name;
        }

        if (null == packageName || "".equals(packageName))
        {
            return name;
        }

        return packageName + '.' + name;
    }

    /**
     * <p>Returns the fully qualified name of a package.</p>
     *
     * @param pakkage the <code>Pakkage</code> for which the fully
     *                qualified name should be returned or a
     *                <code>String</code> with it's id
     * @return a <code>String</code> with the fully qualified name for
     *         the package
     */
    public String findPackageName(Object pakkage)
    {
        Pakkage sooPackage = convertToPackage(pakkage);
        if (null == sooPackage)
        {
            return "";
        }

        String name = sooPackage.getName();
        String parentName = findPackageName(sooPackage.getPackage());
        if (null != parentName && !"".equals(parentName))
        {
            name = parentName + "." + name;
        }

        return name;
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
        String value =
            findAttributeTagValue(attribute, "uml2ejb:JDBCType", true);
        if (null == value)
        {
            Object type = attribute.getType();
            String typeName = findFullyQualifiedName(type);
            value = this.typeMappings.getJDBCType(typeName);
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
        String value =
            findAttributeTagValue(attribute, "uml2ejb:SQLType", true);
        if (null == value)
        {
            value = findAttributeSQLFieldLength(attribute);
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
        String value =
            findAttributeTagValue(attribute, "uml2ejb:SQLFieldLength", true);
        return value;
    }

    /**
     * <p>Returns the value stored under the given tag for the given
     * attribute.</p>
     *
     * @param attribute the attribute
     * @param tagName the name of the tag
     * @param follow <code>true</code> if the search should be
     *               extended to the type of the attribute and up that
     *               types inheritance hierarchy; <code>false</code>
     *               otherwise
     * @return String the value stored under the tag or null if the
     *                tag isn't found
     */
    public String findAttributeTagValue(
        Attribute attribute,
        String tagName,
        boolean follow)
    {
        List taggedValues = attribute.getTaggedValues();
        String value = findTagValue(taggedValues, tagName);
        if (null != value)
        {
            return value;
        }

        if (follow)
        {
            Object cl = attribute.getType();
            value = findTypeTagValue(cl, tagName, follow);
            return value;
        }

        return null;
    }

    /**
     * <p>Returns the value stored under the given tag for the given
     * type.</p>
     *
     * @param type the type
     * @param tagName the name of the tag
     * @param follow <code>true</code> if the search should be
     *               extended up the inheritance hierarchy of type;
     *               <code>false</code> otherwise
     * @return String the value stored under the tag or null if the
     *                tag isn't found
     */
    public String findTypeTagValue(Object type, String tagName, boolean follow)
    {
        Type cl = convertToType(type);
        if (null == cl)
        {
            return null;
        }

        List taggedValues = cl.getTaggedValues();
        List superTypes = cl.getSupertypes();

        String value = findTagValue(taggedValues, tagName);
        if (null != value)
        {
            return value;
        }

        if (follow && (null != superTypes))
        {
            Iterator iter = superTypes.iterator();
            while (iter.hasNext())
            {
                Object superType = iter.next();
                value = findTypeTagValue(superType, tagName, follow);
                if (null != value)
                {
                    return value;
                }
            }
        }

        return null;
    }

    /**
     * <p>Returns the value stored under the given tag.</p>
     *
     * @param taggedValues a list of tagged values
     * @param tagName the name of the tag
     * @return String the value stored under the tag or null if the
     *                tag isn't found
     */
    public String findTagValue(List taggedValues, String tagName)
    {
        if (null == taggedValues)
        {
            return null;
        }

        Iterator iter = taggedValues.iterator();
        while (iter.hasNext())
        {
            TaggedValue taggedValue = (TaggedValue) iter.next();
            if (taggedValue.getTag().equals(tagName))
            {
                return taggedValue.getValue();
            }
        }

        return null;
    }

    /**
     * <p>Gets the name of the component interface, depending on
     * whether it is an entity bean or another session bean.<p>
     *
     * @param cl the class
     * @return String the name of the interface
     */
    public String getComponentInterfaceName(Klass cl)
    {
        if ("EntityBean".equals(getStereotype(cl.getId())))
        {
            return cl.getName() + "Local";
        }
        return cl.getName();
    }

    /**
     * <p>Gets the name of the home interface, depending on whether it
     * is an entity bean or another session bean.</p>
     *
     * @param cl the class
     * @return String the name of the interface
     */
    public String getHomeInterfaceName(Klass cl)
    {
        if ("EntityBean".equals(getStereotype(cl.getId())))
        {
            return cl.getName() + "LocalHome";
        }
        return cl.getName() + "Home";
    }

    /**
     * <p>Gets the view type of the bean representing a given class.
     * depending on whether it is an entity bean or another session
     * bean.</p>
     * 
     * <p>This is used to construct ejb-refs properly.</p>
     * 
     * @param cl the class
     * @return String the view type of the bean
     */
    public String getEjbRefViewType(Klass cl)
    {
        if ("EntityBean".equals(getStereotype(cl.getId())))
        {
            return "local";
        }
        return "remote";
    }

    /**
     * <p>Formats an HTML String as a collection of paragraphs.
     * Each paragraph has a getLines() method that returns a collection
     * of Strings.</p>
     * 
     * @param string the String to be formatted
     * @return Collection the collection of paragraphs found.
     */
    public Collection formatHTMLStringAsParagraphs(String string)
    {
        try
        {
            return new HTMLAnalyzer().htmlToParagraphs(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>Casts the given object into a Type.  This is a convenience
     * method to overcome bugs and shortcomings in the JAXB EA1
     * release.  It claims to resolve attributes defined as IDREFS to
     * the referenced elements but currently does not.</p>
     *
     * @param o an <code>Object</code> that is either an instance of
     *          <code>Type</code> or a <code>String</code> with the id
     *          of a type
     * @return the casted <code>Type</code> value or null
     */
    public Type convertToType(Object o)
    {
        // Allow String as a convenience
        if (o instanceof String)
        {
            o = findTypeById((String) o);
        }

        if (o instanceof Type)
        {
            return (Type) o;
        }
        else
        {
            return null;
        }
    }

    /**
     * <p>Casts the given object into a Pakkage.  This is a
     * convenience method to overcome bugs and shortcomings in the
     * JAXB EA1 release.  It claims to resolve attributes defined as
     * IDREFS to the referenced elements but currently does not.</p>
     *
     * @param o an <code>Object</code> that is either an instance of
     *          <code>Pakkage</code> or a <code>String</code> with the id
     *          of a package
     * @return the casted <code>Pakkage</code> value or null
     */
    public Pakkage convertToPackage(Object o)
    {
        // Allow String as a convenience
        if (o instanceof String)
        {
            o = findPackageById((String) o);
        }

        if (o instanceof Pakkage)
        {
            return (Pakkage) o;
        }
        else
        {
            return null;
        }
    }

    /**
     * <p>Casts the given object into an Association.  This is a
     * convenience method to overcome bugs and shortcomings in the
     * JAXB EA1 release.  It claims to resolve attributes defined as
     * IDREFS to the referenced elements but currently does not.</p>
     *
     * @param o an <code>Object</code> that is either an instance of
     *          <code>Association</code> or a <code>String</code> with
     *          the id of an association
     * @return the casted <code>Association</code> value or null
     */
    public Association convertToAssociation(Object o)
    {
        // Allow String as a convenience
        if (o instanceof String)
        {
            o = findAssociationById((String) o);
        }

        if (o instanceof Association)
        {
            return (Association) o;
        }
        else
        {
            return null;
        }
    }

    private Model          model;
    private DbMappingTable typeMappings;
}
