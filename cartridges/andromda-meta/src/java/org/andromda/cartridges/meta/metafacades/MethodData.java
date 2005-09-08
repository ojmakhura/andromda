package org.andromda.cartridges.meta.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 * @since 10.12.2003
 */
public class MethodData
    implements Comparable
{
    private String metafacadeName;
    private String visibility;
    private boolean isAbstract;
    private String name;
    private String returnTypeName;
    private String documentation;
    private final ArrayList arguments = new ArrayList();
    private final ArrayList exceptions = new ArrayList();

    public MethodData(
        String metafacadeName,
        String visibility,
        boolean isAbstract,
        String returnTypeName,
        String name,
        String documentation)
    {
        this.metafacadeName = metafacadeName;
        this.visibility = visibility;
        this.isAbstract = isAbstract;
        this.name = name;
        this.returnTypeName = returnTypeName;
        this.documentation = documentation;
    }

    public void addArgument(ArgumentData argument)
    {
        arguments.add(argument);
    }

    /**
     * @return
     */
    public Collection getArguments()
    {
        return arguments;
    }

    public void addException(String typeName)
    {
        exceptions.add(typeName);
    }

    public Collection getExceptions()
    {
        return exceptions;
    }

    /**
     * Gets the metafacade name.
     *
     * @return the name of the metafacade
     */
    public String getMetafacadeName()
    {
        return metafacadeName;
    }

    /**
     * Gets the name of the method.
     *
     * @return the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the name of the return type for this method.
     *
     * @return the return type name.
     */
    public String getReturnTypeName()
    {
        return returnTypeName;
    }

    /**
     * Builds a string representing a declaration for this method.
     *
     * @param suppressAbstractDeclaration optionally suppress the "abstract" modifier
     * @return String the declaration
     */
    public String buildMethodDeclaration(boolean suppressAbstractDeclaration)
    {
        String declaration =
            visibility + " " + ((isAbstract && !suppressAbstractDeclaration) ? "abstract " : "") +
            ((returnTypeName != null) ? (returnTypeName + " ") : "") + name + "(";

        for (final Iterator iterator = arguments.iterator(); iterator.hasNext();)
        {
            final ArgumentData argument = (ArgumentData)iterator.next();
            declaration += (argument.getFullyQualifiedTypeName() + " " + argument.getName());
            if (iterator.hasNext())
            {
                declaration += ", ";
            }
        }
        declaration += ")";

        if (exceptions.size() > 0)
        {
            declaration += " throws ";
            for (final Iterator iterator = exceptions.iterator(); iterator.hasNext();)
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
     * Builds a string representing a call to the method.
     *
     * @return String how a call would look like
     */
    public String buildMethodCall()
    {
        String call = getName() + "(";

        for (final Iterator iterator = arguments.iterator(); iterator.hasNext();)
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
     * Builds a signature which can be used as a key into a map. Consists of the return type, the name and the f.q.
     * types of the arguements.
     *
     * @return String the key that identifies this method
     */
    public String buildCharacteristicKey()
    {
        String key = ((returnTypeName != null) ? (returnTypeName + " ") : "") + name + "(";

        for (final Iterator iterator = arguments.iterator(); iterator.hasNext();)
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
        return isAbstract;
    }

    /**
     * Gets the visibility of this method.
     *
     * @return the visibility.
     */
    public String getVisibility()
    {
        return visibility;
    }

    /**
     * Gets the documentation for this method.
     *
     * @return the documentation.
     */
    public String getDocumentation()
    {
        return documentation;
    }

    /**
     * Tells if this method returns something.
     *
     * @return boolean
     */
    public boolean isReturnTypePresent()
    {
        return returnTypeName != null && !returnTypeName.equals("void");
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Object object)
    {
        MethodData other = (MethodData)object;
        int result = getMetafacadeName().compareTo(other.getMetafacadeName());
        return (result != 0) ? result : getName().compareTo(other.getName());
    }
}