package org.andromda.cartridges.meta;

import java.util.Iterator;

import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;

/**
 *
 * @since 10.12.2003
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class UMLOperationDescriptor extends MethodDescriptor
{

    /**
     * Builds a method descriptor from a UML operation.
     * 
     * @param o the operation in UML
     */
    public UMLOperationDescriptor(Operation o)
    {
        super(buildMethodData(o));
    }

    private static MethodData buildMethodData(Operation o)
    {
        String returnTypeName = "void";

        for (Iterator it = o.getParameter().iterator(); it.hasNext();)
        {
            Parameter p = (Parameter) it.next();
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                returnTypeName = getFullyQualifiedName(p.getType());
                break;
            }
        }

        MethodData md =
            new MethodData(
                "",
                visibility(o.getVisibility()),
                false,
                returnTypeName,
                o.getName());

        for (Iterator it = o.getParameter().iterator(); it.hasNext();)
        {
            Parameter p = (Parameter) it.next();
            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                md.addArgument(
                    new ArgumentData(getFullyQualifiedName(p.getType()), p.getName()));
            }
        }

        return md;
    }

    /**
     * @param kind
     */
    private static String visibility(VisibilityKind kind)
    {
        if (VisibilityKindEnum.VK_PUBLIC.equals(kind))
            return "public";    
        if (VisibilityKindEnum.VK_PROTECTED.equals(kind))
            return "protected";    
        if (VisibilityKindEnum.VK_PRIVATE.equals(kind))
            return "private";    
        return "";
    }

    private static String getPackageName(ModelElement m)
    {
        String packageName = "";

        for (ModelElement namespace = m.getNamespace();
            (namespace instanceof org.omg.uml.modelmanagement.UmlPackage)
                && !(namespace instanceof Model);
            namespace = namespace.getNamespace())
        {
            packageName =
                "".equals(packageName)
                    ? namespace.getName()
                    : namespace.getName() + "." + packageName;
        }

        return packageName;
    }

    private static String getFullyQualifiedName(Classifier c)
    {
        String fullName = c.getName();

        if (isPrimitiveType(fullName))
        {
            return fullName;
        }

        String packageName = getPackageName(c);
        fullName =
            "".equals(packageName)
                ? fullName
                : packageName + "." + fullName;

        return fullName;
    }


    private static boolean isPrimitiveType(String name)
    {
        return (
            "void".equals(name)
                || "char".equals(name)
                || "byte".equals(name)
                || "short".equals(name)
                || "int".equals(name)
                || "long".equals(name)
                || "float".equals(name)
                || "double".equals(name)
                || "boolean".equals(name));
    }
}
