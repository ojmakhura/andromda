package org.andromda.core.metadecorators.uml14;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;

public class DecoratorFactory
{
    private static DecoratorFactory factory = new DecoratorFactory();

    private HashMap namespaces = new HashMap();
    private String activeNamespace;
    private org.omg.uml.UmlPackage model;

    // just to make sure that nobody instantiates it
    private DecoratorFactory()
    {
        registerCoreDecoratorClasses();
    }

    /**
     * 
     */
    private void registerCoreDecoratorClasses()
    {
        setActiveNamespace("core");
        registerDecoratorClass(
            "org.omg.uml.modelmanagement.UmlPackage$Impl",
            null,
            PackageDecoratorImpl.class.getName());
        registerDecoratorClass(
            "org.omg.uml.modelmanagement.Model$Impl",
            null,
            PackageDecoratorImpl.class.getName());
    }

    /**
     * Returns the decorator factory singleton.
     * @return the only instance
     */
    public static DecoratorFactory getInstance()
    {
        return factory;
    }

    /**
     * Sets the active namespace. The AndroMDA core and each cartridge
     * have their own namespace for decorator registrations.
     *  
     * @param namespaceName the name of the namespace
     */
    public void setActiveNamespace(String namespaceName)
    {
        HashMap namespace = (HashMap) namespaces.get(namespaceName);
        if (namespace == null)
        {
            namespace = new HashMap();
            namespaces.put(namespaceName, namespace);
        }
        this.activeNamespace = namespaceName;
    }

    /**
     * Returns the name of the active namespace.
     * 
     * @return String the namespace name
     */
    public String getActiveNamespace()
    {
        return this.activeNamespace;
    }

    /**
     * Registers a decorator class for a given metaclass and (optionally)
     * a stereotype.
     * 
     * @param umlMetaClassName the FQCN of the metaclass
     * @param stereotypeName the stereotype name (may be null)
     * @param decoratorClassName the FQCN of the decorator class
     */
    public void registerDecoratorClass(
        String umlMetaClassName,
        String stereotypeName,
        String decoratorClassName)
    {
        HashMap namespace = (HashMap) namespaces.get(activeNamespace);
        String key =
            (stereotypeName == null
                ? umlMetaClassName
                : umlMetaClassName + "::" + stereotypeName);
        namespace.put(key, decoratorClassName);
    }

    /**
     * Method (package local) for testing the behavior of the dictionary.
     * Looks up a registered decorator class name for a metaclass name and
     * (optionally) a stereotype.
     * 
     * @param umlMetaClassName the FQCN of the metaclass
     * @param stereotypeName the stereotype name (may be null)
     * @return the FQCN of the decorator class to be instantiated
     */
    String lookupDecoratorClass(
        String umlMetaClassName,
        String stereotypeName)
    {
        // first, lookup in active namespace
        HashMap namespace = (HashMap) namespaces.get(activeNamespace);
        String decoratorClassName =
            internalLookupDecoratorClass(
                namespace,
                umlMetaClassName,
                stereotypeName);
        if (decoratorClassName != null)
        {
            System.out.println(
                "lookupDecoratorClass: "
                    + umlMetaClassName
                    + " -> "
                    + decoratorClassName);
            return decoratorClassName;
        }

        // if not found, lookup in core namespace
        namespace = (HashMap) namespaces.get("core");
        decoratorClassName =
            internalLookupDecoratorClass(
                namespace,
                umlMetaClassName,
                stereotypeName);
        System.out.println(
            "lookupDecoratorClass: "
                + umlMetaClassName
                + " -> "
                + decoratorClassName);
        return decoratorClassName;
    }

    /**
     * Internal helper class for lookup. Called twice, once with the
     * active namespace, once more with the "core" namespace.
     * 
     * @param namespace the namespace to search
     * @param umlMetaClassName the FQCN of the metaclass
     * @param stereotypeName the stereotype name (may be null)
     * @return the FQCN of the decorator class to be instantiated
     */
    private String internalLookupDecoratorClass(
        HashMap namespace,
        String umlMetaClassName,
        String stereotypeName)
    {
        if (stereotypeName != null)
        {
            String decoratorClassName =
                (String) namespace.get(
                    umlMetaClassName + "::" + stereotypeName);
            if (decoratorClassName != null)
            {
                return decoratorClassName;
            }
            // fall thru into default case...
        }
        return (String) namespace.get(umlMetaClassName);
    }

    /**
     * Returns a decorator for a metaobject, depending on its
     * metaclass and (optionally) its stereotype.
     * 
     * @param m the model element
     * @return DecoratorBase the decorator object (not yet attached to metaclass object)
     */
    public DecoratorBase createDecoratorObject(ModelElement metaobject)
    {
        String stereotypeName = getStereotypeName(metaobject);
        String decoratorClassName =
            lookupDecoratorClass(
                metaobject.getClass().getName(),
                stereotypeName);
        if (decoratorClassName == null)
        {
            return null;
        }
        try
        {
            Class dynamicClass = Class.forName(decoratorClassName);
            Constructor constructor =
                findConstructor(dynamicClass, metaobject.getClass());

            Object[] constructorParams = { metaobject };
            return (DecoratorBase) constructor.newInstance(constructorParams);
        }
        catch (Exception e)
        {
            e.printStackTrace(); // TODO: better logging!
            return null;
        }
    }

    /**
     * Finds the right constructor to create an object of class
     * <code>dynamicClass</code>, using a parameter of type
     * <code>parameterClass</code>.
     * 
     * @param dynamicClass class in which the constructor should be found
     * @param parameterClass type of parameter that the constructor should accept
     * @return the appropriate constructor or null
     */
    private Constructor findConstructor(
        Class dynamicClass,
        Class parameterClass)
    {
        Constructor[] c = dynamicClass.getConstructors();
        for (int i = 0; i < c.length; i++)
        {
            Class[] ptypes = c[i].getParameterTypes();
            if (ptypes.length == 1
                && ptypes[0].isAssignableFrom(parameterClass))
            {
                return c[i];
            }
        }
        return null;
    }

    /**
     * Create a decorator for the whole model itself.
     * 
     * @param model the model as a package
     * @return a decorator for the model
     */
    public ModelDecorator createDecoratorObject(UmlPackage model)
    {
        return new ModelDecoratorImpl(model);
    }

    /**
     * Return the name of the first stereotype attached to a given
     * model element.
     * @param modelElement the model element
     * @return String the stereotype name or null if the element has no stereotype
     */
    private String getStereotypeName(ModelElement modelElement)
    {
        Collection stereotypes = modelElement.getStereotype();
        for (Iterator i = stereotypes.iterator(); i.hasNext();)
        {
            ModelElement stereotype = (ModelElement) i.next();
            return stereotype.getName();
        }

        return null;
    }

    /**
     * @return the model
     */
    public org.omg.uml.UmlPackage getModel()
    {
        return model;
    }

    /**
     * @param model the model
     */
    public void setModel(org.omg.uml.UmlPackage model)
    {
        this.model = model;
    }

    // ----------- these methods support unit testing --------------- 

    int getNamespaceCount()
    {
        return namespaces.size();
    }

    int getDecoratorCount()
    {
        HashMap namespace = (HashMap) namespaces.get(activeNamespace);
        return namespace.size();
    }
}
