package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.modelmanagement.Model;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.ModelElement
 *
 *
 */
public class ModelElementDecoratorImpl extends ModelElementDecorator
{
    // ---------------- constructor -------------------------------

    public ModelElementDecoratorImpl(
        org.omg.uml.foundation.core.ModelElement metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ModelElementDecorator ...

    // ------------- relations ------------------

    /**
     *
     */
    public java.util.Collection handleGetTaggedValues()
    {
        return metaObject.getTaggedValue();
    }

    // ------------------------------------------------------------

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getPackageName()
     */
    public String getPackageName()
    {
        String packageName = "";

        for (ModelElement namespace = metaObject.getNamespace();
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

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        String fullName = getName();
        String packageName = getPackageName();
        fullName =
            "".equals(packageName)
                ? fullName
                : packageName + "." + fullName;

        return fullName;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#findTaggedValue(java.lang.String)
     */
    public String findTaggedValue(String tagName)
    {
        for (Iterator iter = metaObject.getTaggedValue().iterator();
            iter.hasNext();
            )
        {
            TaggedValue element = (TaggedValue) iter.next();
            if (element.getName().equals(tagName))
            {
                return (
                    (TaggedValueDecorator) DecoratorFactory
                        .getInstance()
                        .createDecoratorObject(
                        element))
                    .getValue();
            }

        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getStereotypeName()
     */
    public String getStereotypeName()
    {
        Collection stereotypes = metaObject.getStereotype();
        if (stereotypes == null)
        {
            return null;
        }

        ModelElement stereotype =
            (ModelElement) stereotypes.iterator().next();
        return stereotype.getName();
    }

}
