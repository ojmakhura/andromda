package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Utilities for dealing with UML 1.4 metafacades
 * 
 * @author Chad Brandon
 */
public class UMLMetafacadeUtils
{

    /**
     * Finds a given model element in the model having the specified
     * <code>fullyQualifiedName</code>. If the model element can <strong>NOT
     * </strong> be found, <code>null</code> will be returned instead.
     * 
     * @param model the model to search
     * @return the found model element
     */
    static Object findByFullyQualifiedName(String fullyQualifiedName)
    {
        final String methodName = "UMLMetafacadeUtils.findModelElement";
        Object modelElement = null;
        Collection rootPackages = UMLMetafacadeUtils.getModel()
            .getModelManagement().getModel().refAllOfType();
        Iterator packageIt = rootPackages.iterator();
        while (packageIt.hasNext())
        {
            Object rootPackage = packageIt.next();
            if (rootPackage != null)
            {
                ExceptionUtils.checkAssignable(
                    methodName,
                    UmlPackage.class,
                    "rootPackage",
                    rootPackage.getClass());
                if (rootPackage != null)
                {
                    String[] names = fullyQualifiedName.split("\\"
                        + UMLMetafacadeGlobals.PACKAGE_SEPERATOR);
                    if (names != null && names.length > 0)
                    {
                        Object element = rootPackage;
                        for (int ctr = 0; ctr < names.length && element != null; ctr++)
                        {
                            String name = names[ctr];
                            if (UmlPackage.class.isAssignableFrom(element
                                .getClass()))
                            {
                                element = getElement(((UmlPackage)element)
                                    .getOwnedElement(), name);
                            }
                            modelElement = element;
                        }
                    }
                }
            }
        }
        return modelElement;
    }

    /**
     * Finds the model element having the <code>name</code> contained within
     * the <code>elements</code>, returns null if it can't be found.
     * 
     * @param elements the collection of model elements to search
     * @param name the name of the model element.
     * @return the found model element or null if not found.
     */
    private static Object getElement(Collection elements, final String name)
    {
        return CollectionUtils.find(elements, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return StringUtils
                    .trimToEmpty(((ModelElement)object).getName()).equals(name);
            }
        });
    }

    /**
     * Basically just checks to make sure the <code>model</code> is of type
     * <code>org.omg.uml.UmlPackage</code> and retrieves the
     * <code>CorePackage</code> from it.
     * 
     * @param model the model form which to retrieve the core package.
     * @return the <code>model</code> as a <code>org.omg.uml.UmlPackage</code>
     */
    static CorePackage getCorePackage()
    {
        return ((org.omg.uml.UmlPackage)MetafacadeFactory.getInstance()
            .getModel().getModel()).getCore();
    }

    /**
     * Finds and returns the first model element having the given
     * <code>name</code> in the <code>modelPackage</code>, returns
     * <code>null</code> if not found.
     * 
     * @param modelPackage The modelPackage to search
     * @param name the name to find.
     * @return the found model element.
     */
    static Object findByName(final String name)
    {
        Object modelElement = null;
        if (StringUtils.isNotBlank(name))
        {
            modelElement = CollectionUtils.find(getModel().getCore()
                .getModelElement().refAllOfType(), new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return StringUtils.trimToEmpty(
                        ((ModelElement)object).getName()).equals(name);
                }
            });
        }
        return modelElement;
    }

    /**
     * Gets the root package in the model.
     * 
     * @return the root package as a UmlPackage.
     */
    static UmlPackage getRootPackage()
    {
        Object rootPackage = null;
        Collection rootPackages = UMLMetafacadeUtils.getModel()
            .getModelManagement().getModel().refAllOfType();
        Iterator packageIt = rootPackages.iterator();
        while (packageIt.hasNext())
        {
            rootPackage = packageIt.next();
            // get the first package that's a ModelElement
            // instance
            if (rootPackage instanceof ModelElement)
            {
                break;
            }
        }
        return (UmlPackage)rootPackage;
    }

    /**
     * Returns the entire model.
     * 
     * @return org.omg.uml.UmlPackage model instance.
     */
    static org.omg.uml.UmlPackage getModel()
    {
        return (org.omg.uml.UmlPackage)MetafacadeFactory.getInstance()
            .getModel().getModel();
    }

    /**
     * Gets the correct meta model visibility kind for the given
     * <code>visibility</code> string.
     * 
     * @param visibility the visibility to retrieve.
     * @return the VisibilityKind
     */
    static VisibilityKind getVisibilityKind(String visibility)
    {
        VisibilityKind visibilityKind = null;
        visibility = StringUtils.trimToEmpty(visibility);
        if (visibility.equals("public"))
        {
            visibilityKind = VisibilityKindEnum.VK_PUBLIC;
        }
        else if (visibility.equals("private"))
        {
            visibilityKind = VisibilityKindEnum.VK_PRIVATE;
        }
        else if (visibility.equals(""))
        {
            visibilityKind = VisibilityKindEnum.VK_PACKAGE;
        }
        else if (visibility.equals("protected"))
        {
            visibilityKind = VisibilityKindEnum.VK_PROTECTED;
        }
        return visibilityKind;
    }

    /**
     * Creates an attribute having the give <code>name</code> and the type
     * having the given <code>fullyQualifiedTypeName</code>, with the
     * specified visibility, if no type can be found with the given name, no
     * type is set.
     * 
     * @param name the new name
     * @param fullyQualifiedTypeName the name of the fully qualified type
     * @param visibility the visibility name
     * @return the new Attribute.
     */
    static Attribute createAttribute(
        String name,
        String fullyQualifiedTypeName,
        String visibility)
    {
        Attribute attribute = UMLMetafacadeUtils.getCorePackage()
            .getAttribute().createAttribute();
        attribute.setName(name);
        attribute.setVisibility(UMLMetafacadeUtils
            .getVisibilityKind(visibility));
        Object type = UMLMetafacadeUtils
            .findByFullyQualifiedName(fullyQualifiedTypeName);
        if (type != null && Classifier.class.isAssignableFrom(type.getClass()))
        {
            attribute.setType((Classifier)type);
        }
        return attribute;
    }

    /**
     * Finds or creates a stereotype with the given name. If the stereotype
     * isn't found, it will be created.
     * 
     * @param name the name of the stereotype.
     * @return the new Stereotype.
     */
    static Stereotype findOrCreateStereotype(String name)
    {
        Object stereotype = UMLMetafacadeUtils.findByName(name);
        if (stereotype == null
            || !Stereotype.class.isAssignableFrom(stereotype.getClass()))
        {
            stereotype = UMLMetafacadeUtils.getCorePackage().getStereotype()
                .createStereotype();
            ((Stereotype)stereotype).setName(name);
        }
        return (Stereotype)stereotype;
    }

    /**
     * Returns true if the passed in constraint <code>expression</code> is of
     * type <code>kind</code>, false otherwise.
     * 
     * @param expression the expression to check.
     * @param kind the constraint kind (i.e. <em>inv</em>,<em>pre</em>,
     *        <em>body</em>, etc).
     * @return boolean
     */
    static boolean isConstraintKind(String expression, String kind)
    {
        Pattern pattern = Pattern.compile(
            ".*\\s*" + StringUtils.trimToEmpty(kind) + "\\s*:.*",
            Pattern.DOTALL);
        Matcher matcher = pattern.matcher(StringUtils.trimToEmpty(expression));
        return matcher.matches();
    }
}