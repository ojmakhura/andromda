package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeException;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.Association;
import org.eclipse.uml2.AssociationClass;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Generalization;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.MultiplicityElement;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Operation;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.Property;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.Type;
import org.eclipse.uml2.TypedElement;
import org.eclipse.uml2.UML2Package;


/**
 * Contains utilities for the Eclipse/UML2 metafacades.
 *
 * @author Steve Jerman
 * @author Chad Brandon
 */
public class UmlUtilities
{
    /**
     * The logger instance.
     */
    private static Logger logger = Logger.getLogger(UmlUtilities.class);

    /**
     * Get the comments for a UML Element. This will be a string with
     * each comment separated by a 2 newlines.
     *
     * @param element
     * @return concatenated string
     */
    public static String getComment(Element element)
    {
        String commentString = "";
        EList comments = element.getOwnedComments();

        for (Iterator iterator = comments.iterator(); iterator.hasNext();)
        {
            final Comment comment = (Comment)iterator.next();
            if (!commentString.equalsIgnoreCase(""))
            {
                commentString = commentString + "\n\n";
            }
            commentString = commentString.concat(comment.getBody());
        }
        return cleanText(commentString);
    }

    /**
     * Gets read of all excess whitespace.
     *
     * @param text the text from which to remove the white space.
     * @return the cleaned text.
     */
    public static String cleanText(String text)
    {
        text = text.replaceAll(
                "[\\t\\n]*",
                "");
        text = text.replaceAll(
                "\\s+",
                " ");

        return text;
    }

    /**
     * Retrieves the name of the type for the given <code>element</code>.
     *
     * @param element the element for which to retrieve the type.
     * @return the type name.
     */
    public static String getType(final TypedElement element)
    {
        final Type elementType = element.getType();
        String type = elementType.getName();
        if (type != null && type.trim().length() > 0)
        {
            if (element instanceof MultiplicityElement)
            {
                MultiplicityElement multiplicity = (MultiplicityElement)element;
                if (multiplicity.isMultivalued())
                {
                    type = type + "[";
                    if (multiplicity.lower() > 0)
                    {
                        type = type + multiplicity.lower() + "..";
                    }
                    if (multiplicity.upper() == MultiplicityElement.UNLIMITED_UPPER_BOUND)
                    {
                        type = type + "*]";
                    }
                    else
                    {
                        type = type + multiplicity.upper() + "]";
                    }
                }
            }
        }
        return type;
    }

    /**
     * Return the Superclass for this class. If more than one superclass is
     * defined an error will be logged to the console.
     *
     * @param classifier the classifier instance.
     * @return the super class or null if it could not be found.
     */
    public static Classifier getSuperclass(Classifier classifier)
    {
        final List superClasses = classifier.getGenerals();
        if (superClasses.size() > 1)
        {
            throw new MetafacadeException("Error: found " + superClasses.size() + " generalizations for " +
                classifier.getQualifiedName());
        }
        Classifier superClass = null;
        if (superClasses.size() == 1)
        {
            final Object generalization = superClasses.get(0);
            if (generalization instanceof Classifier)
            {
                superClass = (Classifier)generalization;
            }
            else
            {
                throw new MetafacadeException("Error: generalization for " + classifier.getQualifiedName() + " is a " +
                    superClass.getClass().getName());
            }
        }
        return superClass;
    }

    /**
     * Gets a collection containing all of the properties (UML
     * attributes/association ends) for this class. Superclass properties will
     * included if <code>follow</code> is true. Overridden properties will be
     * omitted.
     *
     * @param umlClass the UML class instance from which to retrieve all
     *        properties
     * @param follow whether or not the inheritance hierarchy should be followed
     * @return all retrieved properties.
     */
    public static Collection getProperties(
        final Class umlClass,
        final boolean follow,
        final boolean excludeEnds)
    {
        List classAttributes = new ArrayList();

        //first add all the class properties
        if (umlClass == null || umlClass.getAttributes() == null)
        {
            return classAttributes;
        }
        for (final Iterator iterator = umlClass.getAttributes().iterator(); iterator.hasNext();)
        {
            Property property = (Property)iterator.next();
            if (excludeEnds)
            {
                if (!property.isNavigable())
                {
                    classAttributes.add(property);
                }
            }
            else
            {
                classAttributes.add(property);
            }
        }
        if (follow)
        {
            // then interate through all the inherited members adding those not already
            // defined (ie overridden)
            for (final Iterator iterator = umlClass.getInheritedMembers().iterator(); iterator.hasNext();)
            {
                NamedElement element = (NamedElement)iterator.next();
                if (element instanceof Property)
                {
                    Iterator propIter = classAttributes.iterator();
                    boolean defined = false;
                    while (propIter.hasNext())
                    {
                        Property testProp = (Property)propIter.next();
                        if (testProp.getName().equalsIgnoreCase(element.getName()))
                        {
                            defined = true;
                        }
                    }
                    if (!defined)
                    {
                        if (excludeEnds)
                        {
                            if (!((Property)element).isNavigable())
                            {
                                classAttributes.add(element);
                            }
                        }
                        else
                        {
                            classAttributes.add(element);
                        }
                    }
                }
            }
        }
        return classAttributes;
    }

    /**
     * Attempts to retrieve the attribute for the given <code>umlClass</code>
     * having the given <code>name</code>.
     *
     * @param umlClass the name of the UML class.
     * @param name the name of the attribute.
     * @return the attribute or null if not found.
     */
    public static Property getAttribute(
        final Class umlClass,
        final String name)
    {
        Property attribute = null;
        final Collection properties = getProperties(
                umlClass,
                true,
                false);
        for (final Iterator iterator = properties.iterator(); iterator.hasNext();)
        {
            final Property property = (Property)iterator.next();
            if (property.getAssociation() == null && property.getName().equalsIgnoreCase(name))
            {
                attribute = property;
                break;
            }
        }
        return attribute;
    }

    /**
     * Return a collection containing all of the methods (UML operations) for
     * this class. Superclass properties will included if <code>follow</code>
     * is true. Overridden methods will be omitted.
     *
     * @param umlClass the UML class instance.
     * @param follow whether or not to follow the inheritance hierarchy
     * @return the collection of operations.
     */
    public static Collection getOperations(
        Class umlClass,
        boolean follow)
    {
        Iterator iterator;
        ArrayList classOperations = new ArrayList();

        // - first add all the class properties
        if (umlClass == null || umlClass.getOwnedOperations() == null)
        {
            return classOperations;
        }
        iterator = umlClass.getOwnedOperations().iterator();
        while (iterator.hasNext())
        {
            classOperations.add(iterator.next());
        }
        if (follow)
        {
            // then interate through all the inherited members adding those not already
            // defined (ie overridden)
            iterator = umlClass.getInheritedMembers().iterator();
            while (iterator.hasNext())
            {
                NamedElement el = (NamedElement)iterator.next();
                if (el instanceof Operation)
                {
                    Iterator classIter = classOperations.iterator();
                    boolean defined = false;
                    while (classIter.hasNext())
                    {
                        Operation testMeth = (Operation)classIter.next();
                        if (testMeth.getName().equalsIgnoreCase(el.getName()))
                        {
                            defined = true;
                        }
                    }
                    if (!defined)
                    {
                        classOperations.add(el);
                    }
                }
            }
        }
        return classOperations;
    }

    /**
     * Gets the operation with the given <code>name</code>
     * for the given <code>umlClass</code>.
     *
     * @param umlClass the UML class instance.
     * @param name the name of the operation.
     * @return the operation instance or null
     */
    public static Operation getOperation(
        final Class umlClass,
        final String name)
    {
        Operation foundOperation = null;
        final Collection operations = getOperations(
                umlClass,
                true);
        for (final Iterator iterator = operations.iterator(); iterator.hasNext();)
        {
            final Operation operation = (Operation)iterator.next();
            if (operation.getName().equalsIgnoreCase(name))
            {
                foundOperation = operation;
                break;
            }
        }
        return foundOperation;
    }

    /**
     * Retrieves all specializations of the given <code>classifier</code> instance.
     *
     * @param classifier the classifier from which to retrieve the specializations.
     * @return all specializations.
     */
    public static List getSpecializations(final Classifier classifier)
    {
        final List specials = new ArrayList();
        for (final TreeIterator iterator = EcoreUtil.getRootContainer(classifier).eAllContents(); iterator.hasNext();)
        {
            final EObject object = (EObject)iterator.next();
            if (object instanceof Generalization)
            {
                final Generalization generalization = (Generalization)object;
                if (generalization.getGeneral().equals(classifier))
                {
                    specials.add(generalization.getSpecific());
                }
                iterator.prune();
            }
        }
        return specials;
    }

    /**
     * Gets all classes for the given model
     *
     * @return the list of Class objects
     */
    public static List getClasses(final Model model)
    {
        final List classList = new ArrayList();
        for (final TreeIterator iterator = model.eAllContents(); iterator.hasNext();)
        {
            final EObject object = (EObject)iterator.next();
            if (object instanceof Class)
            {
                final Class umlClass = (Class)object;
                classList.add(umlClass);
                iterator.prune();
            }
        }
        return classList;
    }

    /**
     * Retrieves the names of the stereotypes for the given <code>element</code>
     *
     * @param element the element for which to retrieve the stereotypes.
     * @return all stereotype names
     */
    public static List getStereotypeNames(NamedElement element)
    {
        final Collection stereotypes = element.getAppliedStereotypes();
        final List names = new ArrayList();
        if (stereotypes != null)
        {
            for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
            {
                final Stereotype stereotype = (Stereotype)iterator.next();
                names.add(stereotype.getName());
            }
        }
        return names;
    }

    /**
     * Indicates whether or not the given <code>element</code>
     * contains a stereotype with the given <code>name</code>.
     * @param element the element instance.
     * @param name the name of the element
     * @return true/false
     */
    public static boolean containsStereotype(
        final NamedElement element,
        final String name)
    {
        boolean result = false;
        Collection names = getStereotypeNames(element);
        for (Iterator iterator = names.iterator(); iterator.hasNext();)
        {
            final String stereotypeName = (String)iterator.next();
            if (stereotypeName.equals(name))
            {
                result = true;
            }
        }
        if (logger.isDebugEnabled())
        {
            logger.debug(element.getQualifiedName() + " has stereotype:" + name + " : " + result);
        }
        return result;
    }

    /**
     * Stores the tagged values that may be applied to an element.
     */
    private static final String TAGGED_VALUES_STEREOTYPE = "AndroMDATags";

    /**
     * Retrieges the TagDefinitions for the given element.
     *
     * @param element the element from which to retrieve the tagged values.
     * @return the collection of {@TagDefinition} instances.
     */
    public static Collection getAndroMDATags(Element element)
    {
        final Collection tags = new ArrayList();
        Stereotype tagStereotype = findAppliedStereotype(
                element,
                TAGGED_VALUES_STEREOTYPE);
        if (tagStereotype != null)
        {
            List tagNames = (List)element.getValue(
                    tagStereotype,
                    "TagName");
            List tagValues = (List)element.getValue(
                    tagStereotype,
                    "TagValue");
            for (int ctr = 0; ctr < tagValues.size(); ctr++)
            {
                tags.add(new TagDefinitionImpl(
                        tagNames.get(ctr),
                        tagValues.get(ctr)));
            }
        }
        return tags;
    }

    /**
     * Attempts to find the applied stereotype with the given name on the given <code>element</code>.
     * First tries to find it with the fully qualified name, and then tries it with just the name.
     *
     * @param name the name of the stereotype
     * @return the found stereotype or null if not found.
     */
    public static Stereotype findAppliedStereotype(
        final Element element,
        final String name)
    {
        Stereotype foundStereotype = element.getAppliedStereotype(name);
        if (foundStereotype == null)
        {
            final Set stereotypes = element.getAppliedStereotypes();
            if (stereotypes != null)
            {
                for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
                {
                    final Stereotype stereotype = (Stereotype)iterator.next();
                    if (stereotype.getName().equals(name))
                    {
                        foundStereotype = stereotype;
                        break;
                    }
                }
            }
        }
        return foundStereotype;
    }

    /**
     * Attempts to find the applicable stereotype with the given name on the given <code>element</code>.
     * First tries to find it with the fully qualified name, and then tries it with just the name.
     *
     * @param name the name of the stereotype
     * @return the found stereotype or null if not found.
     */
    public static Stereotype findApplicableStereotype(
        final Element element,
        final String name)
    {
        Stereotype foundStereotype = element.getApplicableStereotype(name);
        if (foundStereotype == null)
        {
            final Set stereotypes = element.getApplicableStereotypes();
            if (stereotypes != null)
            {
                for (final Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
                {
                    final Stereotype stereotype = (Stereotype)iterator.next();
                    if (stereotype.getName().equals(name))
                    {
                        foundStereotype = stereotype;
                        break;
                    }
                }
            }
        }
        return foundStereotype;
    }

    /**
     * Retrieves the serial version UID by reading the tagged value
     * {@link UMLProfile#TAGGEDVALUE_SERIALVERSION_UID} of the
     * <code>classifier</code>.
     *
     * @param classifier the classifier to be inspected.
     * @return the serial version UID of the classifier. Returns
     *         <code>null</code> if the tagged value cannot be found.
     */
    final static String getSerialVersionUID(ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull(
            "classifer",
            classifier);
        String serialVersionString = (String)classifier.findTaggedValue(UMLProfile.TAGGEDVALUE_SERIALVERSION_UID);
        return StringUtils.trimToNull(serialVersionString);
    }

    /**
     * Gets the opposite end of the given <code>associationEnd</code> if the
     * property is indeed an association end, otherwise returns null.
     *
     * @param associationEnd the association end from which to retrieve the
     *        opposite end.
     * @return the opposite association end or null.
     */
    final static Object getOppositeAssociationEnd(Property associationEnd)
    {
        Object opposite = null;
        Association association = associationEnd.getAssociation();

        // - for now we don't return association classes with associations (maybe we should?)
        if (association != null && !(association instanceof AssociationClass))
        {
            Collection ends = association.getMemberEnds();
            for (final Iterator endIterator = ends.iterator(); endIterator.hasNext();)
            {
                final Object end = endIterator.next();
                if (end != null && !end.equals(associationEnd))
                {
                    opposite = end;
                    break;
                }
            }
        }
        return opposite;
    }

    /**
     * Finds and returns the first model element having the given <code>name</code> in the <code>modelPackage</code>,
     * returns <code>null</code> if not found.
     *
     * @param model The model to search.
     * @param name the name of the model element to find.
     * @return the found model element.
     */
    static Object findByName(
        final ResourceSet resourceSet,
        final String name)
    {
        Object modelElement = null;
        if (StringUtils.isNotBlank(name))
        {
            for (final Iterator iterator = resourceSet.getResources().iterator(); iterator.hasNext();)
            {
                final Resource resource = (Resource)iterator.next();
                final Package model =
                    (Package)EcoreUtil.getObjectByType(
                        resource.getContents(),
                        UML2Package.eINSTANCE.getPackage());
                if (model != null)
                {
                    for (final TreeIterator elementIterator = model.eAllContents(); elementIterator.hasNext();)
                    {
                        final Object object = elementIterator.next();
                        if (object instanceof NamedElement)
                        {
                            final NamedElement element = (NamedElement)object;
                            final Package nearestPackage = element.getNearestPackage();
                            final String packageName = nearestPackage != null ? nearestPackage.getName() : "";
                            final String fullyQualifiedName =
                                packageName + MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR + element.getName();
                            if (fullyQualifiedName.equals(name))
                            {
                                modelElement = object;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return modelElement;
    }
}