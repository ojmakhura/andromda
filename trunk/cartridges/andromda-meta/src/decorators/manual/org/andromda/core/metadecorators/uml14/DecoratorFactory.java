package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.omg.uml.foundation.core.ModelElement;

public class DecoratorFactory
{
    private static DecoratorFactory factory = new DecoratorFactory();

    private HashMap namespaces = new HashMap();
    private String activeNamespace;

    // just to make sure that nobody instantiates it
    private DecoratorFactory()
    {
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
            return decoratorClassName;
        }

        // if not found, lookup in core namespace
        namespace = (HashMap) namespaces.get("core");
        return internalLookupDecoratorClass(
            namespace,
            umlMetaClassName,
            stereotypeName);
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
            return (DecoratorBase) Class
                .forName(decoratorClassName)
                .newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace(); // TODO: better logging!
            return null;
        }
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
