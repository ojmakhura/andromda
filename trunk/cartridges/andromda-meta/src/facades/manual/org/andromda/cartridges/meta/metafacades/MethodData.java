package org.andromda.cartridges.meta.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @since 10.12.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class MethodData implements Comparable
{
    private String interfaceName;
    private String visibility;
    private boolean abstract_;
    private String name;
    private String returnTypeName;
    private ArrayList arguments;
    private ArrayList exceptions;
    private String documentation;

    public MethodData(
        String interfaceName,
        String visibility,
        boolean isAbstract,
        String returnTypeName,
        String name,
        String documentation)
    {
        this.interfaceName = interfaceName;
        this.visibility = visibility;
        this.abstract_ = isAbstract;
        this.name = name;
        this.returnTypeName = returnTypeName;
        this.arguments = new ArrayList();
        this.exceptions = new ArrayList();
        this.documentation = documentation;
    }

    public void addArgument(ArgumentData arg)
    {
        arguments.add(arg);
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
     * @return
     */
    public String getInterfaceName()
    {
        return interfaceName;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return
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
            visibility
                + " "
                + ((abstract_ && !suppressAbstractDeclaration)
                    ? "abstract "
                    : "")
                + ((returnTypeName != null) ? returnTypeName + " " : "")
                + name
                + "(";

        for (Iterator it = arguments.iterator(); it.hasNext();)
        {
            ArgumentData ad = (ArgumentData) it.next();
            declaration += ad.getFullyQualifiedTypeName()
                + " "
                + ad.getName();
            if (it.hasNext())
            {
                declaration += ", ";
            }
        }
        declaration += ")";

        if (exceptions.size() > 0)
        {
            declaration += " throws ";
            for (Iterator iter = exceptions.iterator(); iter.hasNext();)
            {
                String exc = (String) iter.next();
                declaration += exc;
                if (iter.hasNext())
                {
                    declaration += ", ";
                }
            }
        }

        return declaration;
    }

    /**
     * Builds a string representing a call to the method.
     * @return String how a call would look like
     */
    public String buildMethodCall()
    {
        String call = getName() + " (";

        for (Iterator it = arguments.iterator(); it.hasNext();)
        {
            ArgumentData ad = (ArgumentData) it.next();
            call += ad.getName();
            if (it.hasNext())
                call += ", ";
        }
        call += ")";
        return call;
    }

    /**
     * Builds a signature which can be used as a key into a map.
     * Consists of the return type, the name and the f.q. types
     * of the arguements.
     * 
     * @return String the key that identifies this method
     */
    public String buildCharacteristicKey()
    {
        String key =
            ((returnTypeName != null) ? returnTypeName + " " : "")
                + name
                + "(";

        for (Iterator it = arguments.iterator(); it.hasNext();)
        {
            ArgumentData ad = (ArgumentData) it.next();
            key += ad.getFullyQualifiedTypeName();
            if (it.hasNext())
            {
                key += ",";
            }
        }
        key += ")";

        return key;
    }

    /**
     * @return
     */
    public boolean isAbstract()
    {
        return abstract_;
    }

    /**
     * @return
     */
    public String getVisibility()
    {
        return visibility;
    }

    /**
     * @return
     */
    public String getDocumentation()
    {
        return documentation;
    }

    /**
     * @param string
     */
    public void setDocumentation(String string)
    {
        documentation = string;
    }

    /**
     * Tells if this method returns something.
     * 
     * @return boolean
     */
    public boolean hasReturnType()
    {
        return returnTypeName != null && !returnTypeName.equals("void"); 
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        MethodData otherMd = (MethodData) o;
        int result = getInterfaceName().compareTo(otherMd.getInterfaceName());
        return result != 0 ? result : getName().compareTo(otherMd.getName());
    }

}

