package org.andromda.cartridges.meta.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @author Cyril Combe
 * @since 10.12.2003
 */
public class MethodData implements Comparable
{
    private String metafacadeName;
    private String visibility;
    private boolean isAbstract;
    private String name;
    private String returnTypeName;
    private String documentation;
    private final ArrayList arguments = new ArrayList();
    private final ArrayList exceptions = new ArrayList();

    /**
     * @param metafacadeNameIn
     * @param visibilityIn
     * @param isAbstractIn
     * @param returnTypeNameIn
     * @param nameIn
     * @param documentationIn
     */
    public MethodData(
        String metafacadeNameIn,
        String visibilityIn,
        boolean isAbstractIn,
        String returnTypeNameIn,
        String nameIn,
        String documentationIn)
    {
        this.metafacadeName = metafacadeNameIn;
        this.visibility = visibilityIn;
        this.isAbstract = isAbstractIn;
        this.name = nameIn;
        this.returnTypeName = returnTypeNameIn;
        this.documentation = documentationIn;
    }

    /**
     * @param argument
     */
    public void addArgument(ArgumentData argument)
    {
        this.arguments.add(argument);
    }

    /**
     * @return arguments
     */
    public Collection getArguments()
    {
        return this.arguments;
    }

    /**
     * @param typeName
     */
    public void addException(String typeName)
    {
        this.exceptions.add(typeName);
    }

    /**
     * @return exceptions
     */
    public Collection getExceptions()
    {
        return this.exceptions;
    }

    /**
     * Gets the metafacade name.
     * 
     * @return the name of the metafacade
     */
    public String getMetafacadeName()
    {
        return this.metafacadeName;
    }

    /**
     * Gets the name of the method.
     * 
     * @return the name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the name of the return type for this method.
     * 
     * @return the return type name.
     */
    public String getReturnTypeName()
    {
        return this.returnTypeName;
    }

    /**
     * Builds a string representing a declaration for this method.
     * 
     * @param suppressAbstractDeclaration
     *            optionally suppress the "abstract" modifier
     * @return String the declaration
     */
    public String buildMethodDeclaration(boolean suppressAbstractDeclaration)
    {
        String declaration = this.visibility + " " 
                + ((this.isAbstract && !suppressAbstractDeclaration) ? "abstract " : "")
                + ((this.returnTypeName != null) ? (this.returnTypeName + " ") : "") + this.name + "(";

        declaration += getTypedArgumentList();
        /*for (final Iterator iterator = this.arguments.iterator(); iterator.hasNext();)
        {
            final ArgumentData argument = (ArgumentData)iterator.next();
            declaration += (argument.getFullyQualifiedTypeName() + " " + argument.getName());
            if (iterator.hasNext())
            {
                declaration += ", ";
            }
        }*/
        declaration += ")";

        if (this.exceptions.size() > 0)
        {
            declaration += " throws ";
            for (final Iterator iterator = this.exceptions.iterator(); iterator.hasNext();)
            {
                String exception = (String)iterator.next();
                declaration += exception;
                if (iterator.hasNext())
                {
                    declaration += ", ";
                }
            }
        }

        return declaration;
    }

    /**
     * Builds a string representing a comma separated parameter type + name list.
     *
     * @return String the declaration
     */
    public String getTypedArgumentList()
    {
        return getTypedArgumentList(null);
    }

    /**
     * Builds a string representing a comma separated parameter type + name list.
     *
     * @param modifier Optional modifier before each parameter
     * @return String the declaration
     */
    public String getTypedArgumentList(String modifier)
    {
        String declaration = "";

        for (final Iterator iterator = this.arguments.iterator(); iterator.hasNext();)
        {
            final ArgumentData argument = (ArgumentData)iterator.next();
            if (modifier!=null)
            {
                declaration += modifier + " ";
            }
            declaration += (argument.getFullyQualifiedTypeName() + " " + argument.getName());
            if (iterator.hasNext())
            {
                declaration += ", ";
            }
        }
        return declaration;
    }

    /**
     * Builds a string representing a call to the method.
     * 
     * @return String how a call would look like
     */
    public String buildMethodCall()
    {
        String call = getName() + "(";

        for (final Iterator iterator = this.arguments.iterator(); iterator.hasNext();)
        {
            final ArgumentData argument = (ArgumentData)iterator.next();
            call += argument.getName();
            if (iterator.hasNext())
            {
                call += ", ";
            }
        }
        call += ")";
        return call;
    }

    /**
     * Builds a signature which can be used as a key into a map. Consists of the methodName, number of arguments, 
     * return type, the name and the f.q. types of the arguments.
     * 
     * @return String the key that identifies this method
     */
    public String buildCharacteristicKey()
    {
        String key = ((this.returnTypeName != null) ? (this.returnTypeName + " ") : "") + this.name + "(";

        for (final Iterator iterator = this.arguments.iterator(); iterator.hasNext();)
        {
            final ArgumentData argument = (ArgumentData)iterator.next();
            key += argument.getFullyQualifiedTypeName();
            if (iterator.hasNext())
            {
                key += ",";
            }
        }
        key += ")";

        return key;
    }

    /**
     * Indicates whether or not this method is abstract.
     * 
     * @return true/false
     */
    public boolean isAbstract()
    {
        return this.isAbstract;
    }

    /**
     * Gets the visibility of this method.
     * 
     * @return the visibility.
     */
    public String getVisibility()
    {
        return this.visibility;
    }

    /**
     * Gets the documentation for this method.
     * 
     * @return the documentation.
     */
    public String getDocumentation()
    {
        return this.documentation;
    }

    /**
     * Tells if this method returns something.
     * 
     * @return boolean
     */
    public boolean isReturnTypePresent()
    {
        return this.returnTypeName != null && !this.returnTypeName.equals("void");
    }

    /**
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final Object object)
    {
        MethodData other = (MethodData)object;
        int result = getMetafacadeName().compareTo(other.getMetafacadeName());

        // Use the characteristic key in order to have a deterministic order, starting with method name and number of arguments
        return (result != 0) ? result : (name + arguments.size() + ", " + buildCharacteristicKey())
            .compareTo(other.getName() + other.getArguments().size() + ", " + other.buildCharacteristicKey());
    }
}