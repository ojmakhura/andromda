package org.andromda.metafacades.emf.uml22;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * Contains utilities for the Eclipse/UML2 metafacades.
 *
 * @author Steve Jerman
 * @author Chad Brandon
 * @author Wouter Zoons
 * @author Bob Fields
 */
public class UmlUtilities
{
    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(UmlUtilities.class);

    private static List<Package> models = new ArrayList<Package>();

    /**
     * Utility method to return ALL loaded models, populated by RepositoryFacade
     * @return models
     */
    public static List<Package> getModels()
    {
        return UmlUtilities.models;
    }

    /**
     * @param resources
     */
    public static void setModels(final List<Package> resources)
    {
        models = Collections.synchronizedList(resources);
    }

    /**
     * @param resource
     */
    public static void addModel(final Package resource)
    {
        models.add(resource);
    }

    /**
     * @param resource
     */
    public static void removeModel(final Package resource)
    {
        models.remove(resource);
    }

    /**
     * A transformer which transforms:
     * <ul>
     *   <li>each property in an attribute or an association end to AssociationEnd or Attribute</li>
     *   <li>each slot in an attribute link or a link end to LinkEnd or AttributeLink</li>
     *   <li>each instance specification in an object instance or a link instance</li>
     * </ul>
     * This is needed because UML2 is an API in which there is no conceptual difference between
     * fundamentally different elements (see list above); which makes it harder to map to metafacades
     * geared towards UML 1.4
     */
    protected static final Transformer ELEMENT_TRANSFORMER =
        new Transformer()
        {
            public Object transform(final Object element)
            {
                final Object transformedObject;

                if (element instanceof Property)
                {
                    final Property property = (Property)element;
                    if (property instanceof AssociationEnd || property instanceof Attribute)
                    {
                        transformedObject = property;
                    }
                    else if (property.getAssociation() == null)
                    {
                        transformedObject = new AttributeImpl(property);
                    }
                    else
                    {
                        transformedObject = new AssociationEndImpl(property);
                    }
                    if (LOGGER.isDebugEnabled() && property.getName() != null && !property.getName().startsWith("andromda"))
                    {
                        LOGGER.debug("UMLUtilities.transform " + property.getName() + " "
                            + property.getType().getName() + " " + property + " " + transformedObject);
                    }
                }
                else if (element instanceof Slot)
                {
                    final Slot slot = (Slot)element;

                    // TODO: This mixes uml2 types and metafacade types, uml2 cannot be instanceof mf type
                    if (slot instanceof LinkEnd || slot instanceof AttributeLink)
                    {
                        transformedObject = slot;
                    }
                    else if (this.transform(slot.getDefiningFeature()) instanceof Attribute)
                    {
                        transformedObject = new AttributeLinkImpl(slot);
                    }
                    else
                    {
                        transformedObject = new LinkEndImpl(slot);
                    }
                }
                else if (element instanceof InstanceSpecification)
                {
                    final InstanceSpecification instanceSpecification = (InstanceSpecification)element;

                    if (instanceSpecification instanceof LinkInstance ||
                        instanceSpecification instanceof ObjectInstance ||
                        instanceSpecification instanceof EnumerationLiteral)
                    {
                        transformedObject = instanceSpecification;
                    }
                    else if (!instanceSpecification.getClassifiers().isEmpty() &&
                        instanceSpecification.getClassifiers().iterator().next() instanceof org.eclipse.uml2.uml.Class)
                    {
                        transformedObject = new ObjectInstanceImpl(instanceSpecification);
                    }
                    else
                    {
                        transformedObject = new LinkInstanceImpl(instanceSpecification);
                    }
                }
                else
                {
                    transformedObject = element;
                }

                return transformedObject;
            }
        };

    private static final Map<String,List<EObject>> ALL_META_OBJECTS_CACHE =
        Collections.synchronizedMap(new HashMap<String,List<EObject>>());

    /**
     * List all meta objects instances of a given meta class It's a way to
     * achieve refAllOfType method in a JMI implementation. Please take care of the
     * fact that properties are not transformed here.
     *
     * @param metaClass The meta class we're looking for its instances
     * @param models     The models where we're searching
     * @return a list of objects owned by model, instance of metaClass
     */
    public static List<? extends EObject> getAllMetaObjectsInstanceOf(
        final Class metaClass,
        final List<Package> models)
    {
        if (metaClass==null)
        {
            return new ArrayList<EObject>();
        }
        List<EObject> metaObjects = ALL_META_OBJECTS_CACHE.get(metaClass.getCanonicalName());
        if (metaObjects == null)
        {
            metaObjects = new ArrayList<EObject>();

            for (final Package model : models)
            {
                if (model!=null)
                {
                    //for (Object metaObject : model.eAllContents())
                    for (final Iterator<EObject> it = model.eAllContents(); it.hasNext();)
                    {
                        final EObject metaObject = it.next();
                        if (metaClass.isInstance(metaObject))
                        {
                            metaObjects.add(metaObject);
                            if (LOGGER.isDebugEnabled())
                            {
                                LOGGER.debug("getAllMetaObjectsInstanceOf class: " + metaClass.getCanonicalName() + " " + metaClass.getClass() + " Found: " + metaObject.getClass());
                            }
                        }
                    }
                }
            }
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("getAllMetaObjectsInstanceOf class: " + metaClass.getCanonicalName() + ' ' + metaClass.getClass() + " Found: " + metaObjects.size());
        }
        ALL_META_OBJECTS_CACHE.put(metaClass.getCanonicalName(), metaObjects);

        return metaObjects;
    }

    /**
     * List all meta objects instances of a given meta class It's a way to
     * achieve refAllOfType method in a JMI implementation. Please take care of the
     * fact that properties are not transformed here.
     *
     * @param metaClass The meta class we're looking for its instances
     * @param model     The model where we're searching
     * @return a list of objects owned by model, instance of metaClass
     */
    private static List getAllMetaObjectsInstanceOf(
        final Class metaClass,
        final Package model)
    {
        if (metaClass==null)
        {
            return new ArrayList<EObject>();
        }
        final List<EObject> metaObjects = new ArrayList<EObject>();

        if (model!=null)
        {
            //for (Object metaObject : model.eAllContents())
            for (final Iterator<EObject> it = model.eAllContents(); it.hasNext();)
            {
                final EObject metaObject = it.next();
                if (metaClass.isInstance(metaObject))
                {
                    metaObjects.add(metaObject);
                }
            }
        }

        return metaObjects;
    }

    /**
     * This clears the meta objects cache.  Even though this
     * isn't the "cleanest" way to handle things, we need this
     * for performance reasons (getAllMetaObjectsInstanceOf is WAY
     * to slow otherwise).
     */
    public static void clearAllMetaObjectsCache()
    {
        ALL_META_OBJECTS_CACHE.clear();
    }

    /**
     * Get the comments for a UML Element. This will be a string with each
     * comment separated by a 2 newlines.
     *
     * @param element
     * @return concatenated string
     */
    public static String getComment(final Element element)
    {
        if (element==null)
        {
            return null;
        }
        final StringBuilder commentString = new StringBuilder();
        final Collection<Comment> comments = element.getOwnedComments();

        for (final Comment comment : comments)
        {
            if (commentString.length()>0)
            {
                commentString.append("\n\n");
            }
            commentString.append(comment.getBody());
        }
        return cleanText(commentString.toString());
    }

    /**
     * Gets rid of all excess whitespace.
     *
     * @param text the text from which to remove the white space.
     * @return the cleaned text.
     */
    public static String cleanText(String text)
    {
        if (StringUtils.isBlank(text))
        {
            return text;
        }
        text =
            text.replaceAll(
                "[\\t\\n]*",
                "");
        text =
            text.replaceAll(
                "\\s+",
                " ");

        return text;
    }

    /**
     * returns all owned properties of the given classifier with the right type:
     * attribute if <code>isAssociation</code> is false and association end
     * otherwise.
     *
     * @param classifier the classifier to inspect.
     * @param follow whether to follow inheritance hierarchy upward.
     * @param isAssociation Retrieve only AssociationEnd properties.
     * @return all owned properties of the given classifier with the right type.
     */
    public static List<Property> getOwnedProperty(
        final Classifier classifier,
        final boolean follow,
        final boolean isAssociation)
    {
        if (classifier==null)
        {
            return new ArrayList<Property>();
        }
        final Map<String, Property> attributeMap = new LinkedHashMap<String, Property>(); // preserve ordering
        final List<NamedElement> members = new ArrayList<NamedElement>(classifier.getOwnedMembers());

        if (follow)
        {
            members.addAll(classifier.getInheritedMembers());
        }

        for (NamedElement nextCandidate : members)
        {
            if (nextCandidate instanceof Property)
            {
                final Property property = (Property)nextCandidate;
                /*if (attributeMap.containsKey(property.getName()))
                {
                    logger.warn(
                        "Attribute with this name has already been registered on " +
                        classifier.getQualifiedName() + ": " + property.getName());
                }*/

                if (isAssociation && property.getAssociation() != null)
                {
                    /*if (LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("Association found for " + classifier.getName() + ": " + property.getName());
                    }*/
                    // property represents an association end
                    attributeMap.put(
                        property.getName(),
                        property);
                }
                else if (!isAssociation && property.getAssociation() == null)
                {
                    /*if (LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("Attribute found for " + classifier.getName() + ": " + property.getName());
                    }*/
                    // property represents an attribute
                    attributeMap.put(
                        property.getName(),
                        property);
                }
            }
        }

        return new ArrayList<Property>(attributeMap.values());
    }

    /**
     * Gets a collection containing all of the attributes for this
     * class/interface. Superclass properties will included if
     * <code>follow</code> is true. Overridden properties will be omitted.
     *
     * @param classifier the UML class instance from which to retrieve all properties
     * @param follow whether or not the inheritance hierarchy should be followed upward
     * @return all retrieved attributes. No associations or enumerations.
     */
    public static List<Property> getAttributes(
        final Classifier classifier,
        final boolean follow)
    {
        final List<Property> attributeList = getOwnedProperty(classifier, follow, false);
        CollectionUtils.transform(
            attributeList,
            ELEMENT_TRANSFORMER);
        //Collections.sort(attributeList, new PropertyComparator());
        if (LOGGER.isDebugEnabled())
        {
            for (Property property : attributeList)
            {
                if (!classifier.getQualifiedName().startsWith("andromda") && LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("UMLUtilities.getAttributes " + classifier.getQualifiedName()
                        + " " + property.getName() + " " + property.getType().getName());
                }
            }
        }
        return attributeList;
    }

    /**
     * Returns <code>true</code> if the given association end's type is an ancestor of the classifier, or just the
     * argument classifier if follow is <code>false</code>.
     * @param classifier
     * @param property this method returns false if this argument is not an association end
     * @param follow
     * @return isAssociationEndAttachedToType((Classifier)parent, property, follow)
     */
    public static boolean isAssociationEndAttachedToType(
        final Classifier classifier,
        final Property property,
        final boolean follow)
    {
        boolean attachedToType = false;

        if (property.getAssociation() != null)
        {
            attachedToType = classifier.equals(property.getType());
            if (follow && !attachedToType)
            {

                for (Classifier parent : classifier.getGenerals())
                {
                    //if (parent instanceof Classifier)
                    //{
                    attachedToType =
                        isAssociationEndAttachedToType(
                            parent,
                            property,
                            follow);
                    //}
                }
            }
            if (LOGGER.isDebugEnabled() && attachedToType)
            {
                LOGGER.debug("isAssociationEndAttachedToType " + classifier.getQualifiedName() + ' ' + property + ' ' + property.getQualifiedName() + ' ' + property.getAssociation() + ' ' + property.getAssociationEnd() + ' ' + attachedToType);
            }
        }
        return attachedToType;
    }

    /**
     * Gets a collection containing all of the associationEnds for this
     * class/interface. Superclass properties will be included if
     * <code>follow</code> is true. Overridden properties will be omitted.
     * Finds all Property classes in model and iterates through to see which are of type classifier.
     * <p/>
     * cejeanne: Changed the way association ends are found.
     *
     * @param classifier the UML class instance from which to retrieve all properties
     * @param follow     whether or not the inheritance hierarchy should be followed
     * @return all retrieved attributes.
     */
    public static List<Property> getAssociationEnds(
        final Classifier classifier,
        final boolean follow)
    {
        final Set<Property> associationEnds = new LinkedHashSet<Property>();
        if (classifier==null)
        {
            return Collections.emptyList();
        }
        associationEnds.addAll(getOwnedProperty(classifier, follow, true));
        CollectionUtils.transform(associationEnds, new Transformer()
        {
            public Object transform(final Object input) {
                return getOppositeProperty((Property)input);
            }
        });
        // TODO: Iterate through all referenced models, not just the model containing this classifier.
        // TODO: UML2 bug? getModel returns null because UMLUtil.getOwningElement getBaseElement(owner.eContainer()) changes owningElement to null
        final Package modelPackage = UmlUtilities.findModel(classifier);
        /*if (modelPackage==null)
        {
            logger.error(classifier + " getModel was null: " + classifier.getOwner() + " " + classifier.getQualifiedName());
            Element classifierOwner = classifier.getOwner();
            Element owner = null;
            while (classifierOwner!=null)
            {
                owner = classifierOwner;
                classifierOwner = owner.getOwner();
            }
            // Find the last owner in the chain... Top level package.
            modelPackage = (Package) owner;
        }*/
        final List<Property> allProperties = getAllMetaObjectsInstanceOf(
                Property.class,
                modelPackage);
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("getAssociationEnds " + classifier.getQualifiedName() + ": getAllMetaObjectsInstanceOf=" + allProperties.size());
        }

        for (Property property : allProperties)
        {
            // only treat association ends, ignore attributes
            if (property.getAssociation() != null && isAssociationEndAttachedToType(
                    classifier,
                    property,
                    follow))
            {
                /*int ownedSize = property.getAssociation().getOwnedEnds().size();
                if (ownedSize==1)
                {
                    associationEnds.add(property.getAssociation().getOwnedEnds().get(0));
                    logger.debug("getAssociationEnds " + classifier.getQualifiedName() + ": addedOwnedAssociationEnd " + property + " " + property.getType() + " " + property.getAssociation() + " AssociationEnd=" + property.getAssociationEnd() + " Qualifiers=" + property.getQualifiers() + " Opposite=" + property.getOpposite());
                }
                else if (ownedSize==0 || ownedSize>1)
                {
                    logger.error("associationEnds ownedEnds=" + ownedSize);
                }
                else
                {*/
                    // TODO: associationEnds always show up as non-navigable because the association property (not the end) is added.
                    associationEnds.add(property);
                    if (LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug("getAssociationEnds " + classifier.getQualifiedName() + ": addedAssociation " + property + ' ' + property.getType() + ' ' + property.getAssociation() + " AssociationEnd=" + property.getAssociationEnd() + " OwnedEnds=" + property.getAssociation().getOwnedEnds() + " Qualifiers=" + property.getQualifiers() + " Navigable=" + property.isNavigable());
                    }
               /* }*/
            }
        }

        CollectionUtils.transform(
            associationEnds,
            ELEMENT_TRANSFORMER);
        return new ArrayList<Property>(associationEnds);
    }

    /**
     * Returns <code>true</code> if and only if the given operation would have an identical signature.
     * This means:
     * <ul>
     *  <li>the same name</li>
     *  <li>the same number of parameters</li>
     *  <li>matching parameter types (in that very same order)</li>
     * </ul>
     * @param first
     * @param second
     * @return isEqual(firstParameter.getType(), secondParameter.getType())
     */
    public static boolean isSameSignature(
        final Operation first,
        final Operation second)
    {
        boolean sameSignature = true;

        // test name
        if (isEqual(
                first.getName(),
                second.getName()))
        {
            final List<Parameter> firstParameters = first.getOwnedParameters();
            final List<Parameter> secondParameters = second.getOwnedParameters();

            // test number of parameters
            if (firstParameters.size() == secondParameters.size())
            {
                for (int i = 0; i < firstParameters.size() && sameSignature; i++)
                {
                    final Parameter firstParameter = firstParameters.get(i);
                    final Parameter secondParameter = secondParameters.get(i);

                    // test each parameter's type
                    sameSignature =
                        isEqual(
                            firstParameter.getType(),
                            secondParameter.getType());
                }
            }
            else
            {
                sameSignature = false;
            }
        }
        else
        {
            sameSignature = false;
        }

        return sameSignature;
    }

    /**
     * Returns <code>true</code> if and only if both arguments are equal, this method handles potential
     * incoming <code>null</code> values.
     */
    private static boolean isEqual(
        final Object first,
        final Object second)
    {
        return first == null ? second == null : first.equals(second);
    }

    /**
     * Retrieves all specializations of the given <code>classifier</code>
     * instance.
     *
     * @param classifier the classifier from which to retrieve the specializations.
     * @return all specializations.
     */
    public static List<Classifier> getSpecializations(final Classifier classifier)
    {
        final List<Classifier> specials = new ArrayList<Classifier>();
        if (classifier==null)
        {
            return specials;
        }
        /*for(DirectedRelationship gen :
            classifier.getTargetDirectedRelationships(UMLPackage.eINSTANCE.getGeneralization()))
        {
           for(Element elt : gen.getSources())
           {
             if (elt instanceof Classifier)
                 specials.add((Classifier)elt);
           }
        }*/

        for (final TreeIterator<EObject> iterator = EcoreUtil.getRootContainer(classifier).eAllContents(); iterator.hasNext();)
        {
            final EObject object = iterator.next();
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
     * Retrieves the names of the stereotypes for the given <code>element</code>
     *
     * @param element the element for which to retrieve the stereotypes.
     * @return all stereotype names
     */
    public static List<String> getStereotypeNames(final Element element)
    {
        final List<String> names = new ArrayList<String>();
        if (element==null)
        {
            return names;
        }
        final Collection<Stereotype> stereotypes = element.getAppliedStereotypes();
        if (stereotypes != null)
        {
            for (Stereotype stereotype : stereotypes)
            {
                names.add(stereotype.getName());
            }
        }
        return names;
    }

    /**
     * Indicates whether or not the given <code>element</code> contains a
     * stereotype with the given <code>stereotypeName</code>.
     *
     * @param element the element instance.
     * @param stereotypeName the name of the stereotype
     * @return true/false
     */
    public static boolean containsStereotype(
        final Element element,
        final String stereotypeName)
    {
        if (element==null || StringUtils.isBlank(stereotypeName))
        {
            return false;
        }
        final Collection<Stereotype> stereotypes = element.getAppliedStereotypes();

        boolean hasStereotype = StringUtils.isNotBlank(stereotypeName) && stereotypes != null &&
            !stereotypes.isEmpty();

        if (hasStereotype)
        {
            class StereotypeFilter
                implements Predicate
            {
                public boolean evaluate(final Object object)
                {
                    boolean valid;
                    final Stereotype stereotype = (Stereotype)object;
                    final String name = StringUtils.trimToEmpty(stereotype.getName());
                    valid = stereotypeName.equalsIgnoreCase(name);
                    for (Classifier itStereo : stereotype.allParents())
                    {
                        valid = valid || StringUtils.trimToEmpty(itStereo.getName()).equalsIgnoreCase(stereotypeName);
                    }
                    return valid;
                }
            }
            hasStereotype =
                CollectionUtils.find(
                    stereotypes,
                    new StereotypeFilter()) != null;
        }
        if (LOGGER.isDebugEnabled() && hasStereotype)
        {
            if (element instanceof NamedElement)
            {
                LOGGER.debug(
                    ((NamedElement)element).getQualifiedName() + " has stereotype <<" + stereotypeName + ">> : " +
                    hasStereotype);
            }
            else
            {
                LOGGER.debug(element.toString() + " has stereotype <<" + stereotypeName + ">> : " + hasStereotype);
            }
        }
        return hasStereotype;
    }

    /**
     * @deprecated old way to handle tag values
     *             Note: The uml profile defines it as "AndroMdaTags" and not "AndroMDATags"
     *             Stores the tagged values that may be applied to an element.
     */
    @Deprecated
    private static final String TAGGED_VALUES_STEREOTYPE = "AndroMdaTags";

    /**
     * Retrieves the TagDefinitions for the given element.
     *
     * @param element the element from which to retrieve the tagged values.
     * @return the collection of {@link TagDefinition} instances.
     */
    public static Collection<TagDefinition> getTaggedValue(final Element element)
    {
        final Collection<TagDefinition> tags = new ArrayList<TagDefinition>();
        if (element==null)
        {
            return tags;
        }
        String elementName = "";

        if (element instanceof NamedElement)
        {
            elementName = ((NamedElement)element).getName();
        }
        else
        {
            elementName = element.toString();
        }

        /*if (logger.isDebugEnabled())
        {
            logger.debug("Searching Tagged Values for " + elementName);
        }*/
        final Collection<Stereotype> stereotypes = element.getAppliedStereotypes();
        for (final Stereotype stereo : stereotypes)
        {
            if (TAGGED_VALUES_STEREOTYPE.equals(stereo.getName()))
            {
                final List tagNames = (List)element.getValue(
                        stereo,
                        "TagName");
                final List tagValues = (List)element.getValue(
                        stereo,
                        "TagValue");
                for (int ctr = 0; ctr < tagValues.size(); ctr++)
                {
                    tags.add(new TagDefinitionImpl(
                            tagNames.get(ctr).toString(),
                            tagValues.get(ctr)));
                }
            }
            else if (element.hasValue(
                    stereo,
                    "value"))
            {
                final Object value = element.getValue(
                        stereo,
                        "value");
                tags.add(new TagDefinitionImpl(
                        stereo.getName(),
                        value));
            }
            else
            {
                for (final Property tagProperty : getAttributes(stereo, true))
                {
                    final String tagName = tagProperty.getName();
                    // Some metafacades depend on an actual returned value. hasValue returns nothing if taggedValue=default for the attribute.
                    if (!tagName.startsWith("base$") && element.hasValue(stereo, tagName))
                    {
                        // Obtain its value
                        final Object tagValue = element.getValue(stereo, tagName);
                        if (tagValue instanceof Collection)
                        {
                            final Collection tagValues = (Collection)tagValue;
                            if (!tagValues.isEmpty())
                            {
                                final Collection tagValuesInString =
                                    CollectionUtils.collect(
                                        tagValues,
                                        new Transformer()
                                        {
                                            public Object transform(final Object object)
                                            {
                                                return getTagValueAsString(object);
                                            }
                                        });
                                final TagDefinition tagDefinition = new TagDefinitionImpl(tagName, tagValuesInString);
                                tags.add(tagDefinition);
                            }
                        }
                        else
                        {
                            final String tagString = getTagValueAsString(tagValue);
                            if (!StringUtils.isBlank(tagString) && !"default".equalsIgnoreCase(tagString))
                            {
                                final TagDefinition tagDefinition =
                                    new TagDefinitionImpl(tagName, tagString);
                                tags.add(tagDefinition);
                            }
                        }
                    }
                }
            }
        }

        if (LOGGER.isDebugEnabled() && !tags.isEmpty())
        {
            LOGGER.debug("Found " + tags.size() + " tagged values for " + elementName);
        }

        return tags;
    }

    /**
     * The toString() method isn't suitable to transform the values of tagValue as String.
     * @param tagValue
     * @return the tag value as a string.
     */
    static String getTagValueAsString(final Object tagValue)
    {
        String valueAsString = null;
        if (tagValue != null)
        {
            valueAsString = tagValue.toString();
            if (tagValue instanceof ValueSpecification)
            {
                final ValueSpecification literal = (ValueSpecification)tagValue;
                valueAsString = literal.stringValue();
            }
            else if (tagValue instanceof NamedElement)
            {
                final NamedElement instance = (NamedElement)tagValue;
                valueAsString = instance.getName();
            }
        }
        return valueAsString;
    }

    /**
     * Attempts to find the applied stereotype with the given name on the given
     * <code>element</code>. First tries to find it with the fully qualified
     * name, and then tries it with just the name.
     * @param element
     * @param name
     *            the name of the stereotype
     * @return the found stereotype or null if not found.
     */
    public static Stereotype findAppliedStereotype(
        final Element element,
        final String name)
    {
        if (element==null || StringUtils.isBlank(name))
        {
            return null;
        }
        Stereotype foundStereotype = element.getAppliedStereotype(name);
        if (foundStereotype == null)
        {
            final EList<Stereotype> stereotypes = element.getAppliedStereotypes();
            if (stereotypes != null)
            {
                for (Stereotype stereotype : stereotypes)
                {
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
     * Attempts to find the applicable stereotype with the given name on the
     * given <code>element</code>. First tries to find it with the fully
     * qualified name, and then tries it with just the name.
     * @param element
     * @param name the name of the stereotype
     * @return the found stereotype or null if not found.
     */
    public static Stereotype findApplicableStereotype(
        final Element element,
        final String name)
    {
        if (element==null || StringUtils.isBlank(name))
        {
            return null;
        }
        Stereotype foundStereotype = element.getApplicableStereotype(name);
        if (foundStereotype == null)
        {
            final EList<Stereotype> stereotypes = element.getApplicableStereotypes();
            if (stereotypes != null)
            {
                for (Stereotype stereotype : stereotypes)
                {
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
    static String getSerialVersionUID(final ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull(
            "classifer",
            classifier);
        final String serialVersionString = (String)classifier.findTaggedValue(UMLProfile.TAGGEDVALUE_SERIALVERSION_UID);
        return StringUtils.trimToNull(serialVersionString);
    }

    /**
     * Gets the opposite end of the given <code>associationEnd</code> if the
     * property is indeed an association end, otherwise returns null.
     *
     * @param associationEnd the association end from which to retrieve the opposite end.
     * @return the opposite association end or null.
     */
    public static Property getOppositeProperty(final Property associationEnd)
    {
        if (associationEnd==null)
        {
            return null;
        }
        Property opposite = associationEnd.getOpposite();
        if (opposite == null)
        {
            final Association association = associationEnd.getAssociation();
            if (association != null)
            {
                final Collection<Property> ends = association.getMemberEnds();
                for (final Property end : ends)
                {
                    if (end != null && !associationEnd.equals(end))
                    {
                        opposite = end;
                        break;
                    }
                }
            }
        }
        return opposite;
    }


    /**
     * Gets the opposite end of the given <code>associationEnd</code> if the
     * property is indeed an association end, otherwise returns null.
     *
     * @param associationEnd the association end from which to retrieve the opposite end.
     * @return the opposite association end or null.
     */
    public static AssociationEnd getOppositeAssociationEnd(final Property associationEnd)
    {
        if (associationEnd==null)
        {
            return null;
        }
        return new AssociationEndImpl(getOppositeProperty(associationEnd));
    }

    /**
     * Finds and returns the first model element having the given
     * <code>name</code> in the <code>modelPackage</code>, returns
     * <code>null</code> if not found.
     * @param resourceSet
     * @param pred
     *
     * @return the found model element.
     */
    public static Object findByPredicate(
        final ResourceSet resourceSet,
        final Predicate pred)
    {
        Object modelElement = null;
        if (resourceSet==null || pred==null)
        {
            return modelElement;
        }
        for (Resource resource : resourceSet.getResources())
        {
            final Package model =
                (Package)EcoreUtil.getObjectByType(
                    resource.getContents(),
                    UMLPackage.eINSTANCE.getPackage());
            if (model != null)
            {
                for (final TreeIterator<EObject> elementIterator = model.eAllContents();
                    elementIterator.hasNext() && modelElement == null;)
                {
                    final Object object = elementIterator.next();
                    if (pred.evaluate(object))
                    {
                        modelElement = object;
                    }
                }
            }
            if (modelElement != null)
            {
                break;
            }
        }

        return modelElement;
    }

    /**
     * Find the Model of a resource (UML2 Model)
     * @param resource
     * @return (Model)EcoreUtil.getObjectByType(resource.getContents(), EcorePackage.eINSTANCE.getEObject())
     */
    public static Package findModel(final UMLResource resource)
    {
        if (resource==null)
        {
            return null;
        }
        final Package model = (Package)EcoreUtil.getObjectByType(
                resource.getContents(),
                EcorePackage.eINSTANCE.getEObject());
        if (model==null)
        {
            LOGGER.error("getModel was null: " + resource);
        }
        else if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Model found: " + model);
        }
        return model;
    }

    /**
     * Find the Model of a resource (UML2 Model)
     * @param element
     * @return (Model)EcoreUtil.getObjectByType(resource.getContents(), EcorePackage.eINSTANCE.getEObject())
     */
    public static Package findModel(final Element element)
    {
        if (element==null)
        {
            return null;
        }
        Package modelPackage = element.getModel();
        if (modelPackage==null)
        {
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.error("getModel was null: " + element + " OWNER: " + element.getOwner());
            }
            Element classifierOwner = element.getOwner();
            Element owner = null;
            while (classifierOwner!=null)
            {
                owner = classifierOwner;
                classifierOwner = owner.getOwner();
            }
            // Find the last owner in the chain... Top level package.
            modelPackage = (Package) owner;
        }
        return modelPackage;
    }

    /**
     * Constructs the package name for the given <code>metaObject</code>,
     * separating the package name by the given <code>separator</code>.
     *
     * @param metaObject the Model Element
     * @param separator  the PSM namespace separator, ignored if <code>modelName</code> is <code>true</code>
     * @param modelName  true/false on whether or not to get the model package name
     *                   instead of the PSM package name.
     * @return the package name.
     */
    public static String getPackageName(
        final NamedElement metaObject,
        final String separator,
        final boolean modelName)
    {
        if (metaObject==null || StringUtils.isBlank(separator))
        {
            return null;
        }
        final StringBuilder buffer = new StringBuilder();

        final String usedSeparator = modelName ? MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR : separator;

        for (Namespace namespace = metaObject.getNamespace(); namespace != null;
            namespace = namespace.getNamespace())
        {
            if (namespace instanceof Package && !(namespace instanceof Model) && !(namespace instanceof Profile))
            {
                if (buffer.length() != 0)
                {
                    buffer.insert(
                        0,
                        usedSeparator);
                }

                buffer.insert(
                    0,
                    namespace.getName());
            }
        }
        String packageName = buffer.toString();
        /* // TODO Remove Model Name from the package hierarchy
        if (StringUtils.isBlank(packageName))
        {
            packageName =
                getPackageName(
                    metaObject.getOwner(),
                    separator,
                    modelName);
        }
        if (StringUtils.isBlank(packageName) && metaObject instanceof Classifier)
        {
            packageName = ((Classifier)metaObject).getPackage().getQualifiedName();
        }
        // Allow for empty namespace - new after UML2 v3
        if (StringUtils.isBlank(packageName))
        {
            Package modelPackage = metaObject.getModel();
            String name = metaObject.getNearestPackage().getQualifiedName();
            if (modelPackage instanceof Model)
            {
                // Remove model name from the front of Package Name string
                String model = modelPackage.getName();
                if (model != null && name.indexOf(model) > -1 && (name.length() >= model.length() + separator.length() + 1))
                {
                    packageName = name.substring(model.length() + separator.length() + 1);
                }
            }
        }*/
        if (modelName && StringUtils.isNotBlank(packageName))
        {
            packageName =
                StringUtils.replace(
                    packageName,
                    separator,
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
        }

        return packageName;
    }

    /**
     * Returns the package name of the closest ancestor that is an instance of <code>NamedElement</code>. If no such
     * ancestor exists the empty String is returned.
     * <p/>
     * If the argument would be an instance of <code>NamedElement</code> then this method returns that object's
     * package name.
     * @param metaObject
     * @param separator
     * @param modelName
     * @return packageName
     *
     * @see #getPackageName(org.eclipse.uml2.uml.NamedElement, String, boolean)
     */
    public static String getPackageName(
        final Element metaObject,
        final String separator,
        final boolean modelName)
    {
        if (metaObject==null || StringUtils.isBlank(separator))
        {
            return null;
        }
        String packageName = null;

        if (metaObject instanceof NamedElement)
        {
            packageName =
                getPackageName(
                    (NamedElement)metaObject,
                    separator,
                    modelName);
        }
        else if (metaObject.getOwner() == null)
        {
            packageName = "";
        }
        else
        {
            packageName =
                getPackageName(
                    metaObject.getOwner(),
                    separator,
                    modelName);
        }
        /* if (StringUtils.isBlank(packageName))
        {
            packageName =
                getPackageName(
                    metaObject.getOwner(),
                    separator,
                    modelName);
        }
        // TODO Remove model name from the front of the FQ package name
        if (StringUtils.isBlank(packageName) && metaObject instanceof Classifier)
        {
            packageName = ((Classifier)metaObject).getPackage().getQualifiedName();
        }
        if (StringUtils.isBlank(packageName))
        {
            packageName = metaObject.getNearestPackage().getQualifiedName();
        }*/

        return packageName;
    }

    /**
     * Returns the fully-qualified name of the model element by iterating up through the getOwner hierarchy.
     * @param metaObject
     * @param separator
     * @param modelName
     * @return packageName
     */
    public static String getFullyQualifiedName(
        final Element metaObject,
        final String separator,
        final boolean modelName)
    {
        if (metaObject==null || StringUtils.isBlank(separator) || !(metaObject instanceof NamedElement))
        {
            return "";
        }
        final NamedElement element = (NamedElement)metaObject;
        String name = element.getName();
        Element owner = element.getOwner();
        String ownerName = null;

        while (owner != null)
        {
            // Don't add the top level model name(s) to the FQN, unless the model is a Package.
            if (owner instanceof NamedElement && !(owner instanceof Model) && !(owner instanceof Profile))
            {
                ownerName = ((NamedElement)owner).getName();
                name = ownerName + separator + name;
            }
            owner = owner.getOwner();
        }
        /*// remove the top level model name, so that mapping works correctly...
        if (ownerName != null)
        {
            name = name.substring(ownerName.length() + separator.length());
        }*/

        return name;
    }

    /**
     * Finds and returns the first model element having the given
     * <code>name</code> in the <code>modelPackage</code>, returns
     * <code>null</code> if not found.
     *
     * @param rs   the resource set to search in
     * @param name the name to find.
     * @return the found model element.
     */
    public static Object findByName(
        final ResourceSet rs,
        final String name)
    {
        if (rs==null || StringUtils.isBlank(name))
        {
            return null;
        }
        Object modelElement = null;
        if (StringUtils.isNotBlank(name))
        {
            modelElement =
                findByPredicate(
                    rs,
                    new Predicate()
                    {
                        public boolean evaluate(final Object object)
                        {
                            if (object instanceof NamedElement)
                            {
                                return StringUtils.trimToEmpty(((NamedElement)object).getName()).equals(name);
                            }
                            return false;
                        }
                    });
        }
        return modelElement;
    }

    /**
     * Finds a given model element in the model having the specified
     * <code>fullyQualifiedName</code>. If the model element can <strong>NOT
     * </strong> be found, <code>null</code> will be returned instead.
     *
     * @param resourceSet        the resource set to search in
     * @param fullyQualifiedName the fully qualified name of the element to search for.
     * @param separator          the PSM separator used for qualifying the name (example ".").
     * @param modelName          a flag indicating whether or not a search shall be performed
     *                           using the fully qualified model name or fully qualified PSM
     *                           name.
     * @return the found model element
     */
    public static Object findByFullyQualifiedName(
        final ResourceSet resourceSet,
        final String fullyQualifiedName,
        final String separator,
        final boolean modelName)
    {
        if (resourceSet==null || StringUtils.isBlank(fullyQualifiedName) || StringUtils.isBlank(separator))
        {
            return null;
        }
        Object modelElement;
        modelElement =
            findByPredicate(
                resourceSet,
                new Predicate()
                {
                    public boolean evaluate(final Object object)
                    {
                        if (object instanceof NamedElement)
                        {
                            final NamedElement element = (NamedElement)object;
                            final StringBuilder fullName = new StringBuilder(getPackageName(
                                        element,
                                        separator,
                                        modelName));
                            final String name = element.getName();
                            if (StringUtils.isNotBlank(name))
                            {
                                String namespaceSeparator = MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR;
                                if (!modelName)
                                {
                                    namespaceSeparator = separator;
                                }
                                fullName.append(namespaceSeparator);
                                fullName.append(name);
                            }
                            return fullName.toString().equals(fullyQualifiedName);
                        }
                        return false;
                    }
                });
        return modelElement;
    }

    /**
     * Multiplicity can be expressed as Value. String, integer... This method
     * parses it. MD11.5 uses string, and RSM integers.
     *
     * @param multValue a ValueSpecification, which needs to be parsed
     * @param type ClassifierFacade type used to determine default lower multiplicity (primitive=1, wrapped=0)
     * @param defaultMultiplicity from ClassifierFacadeLogic.getConfiguredProperty(UMLMetafacadeProperties.DEFAULT_MULTIPLICITY)
     * @return the parsed integer. Defaults to 1.
     */
    public static int parseLowerMultiplicity(final ValueSpecification multValue, final ClassifierFacade type, final String defaultMultiplicity)
    {
        int value = 1;
        if (multValue == null)
        {
            if (type.isWrappedPrimitive())
            {
                value = 0;
            }
            else if (!type.isPrimitive())
            {
                if (StringUtils.isNotBlank(defaultMultiplicity) && (defaultMultiplicity.charAt(0) == '0'))
                {
                    value = 0;
                }
                else
                {
                    value = 1;
                }
            }
            // Defaults to 1 if a Primitive
        }
        else
        {
            value = parseMultiplicity(multValue, Integer.parseInt(defaultMultiplicity));
        }
        return value;
    }

    /**
     * Multiplicity can be expressed as Value. String, integer... This method
     * parses it. MD11.5 uses string, and RSM integers.
     *
     * @param multValue a ValueSpecification, which needs to be parsed
     * @param defaultValue when null: 1 for upper multiplicity, 0 for lower multiplicity.
     * @return the parsed integer. Defaults to 1.
     */
    public static int parseMultiplicity(final ValueSpecification multValue, final int defaultValue)
    {
        int value = defaultValue;
        if (multValue != null)
        {
            if (multValue instanceof LiteralInteger)
            {
            	
                final LiteralInteger litInt = (LiteralInteger)multValue;
                value = litInt.getValue();
            }
            else if (multValue instanceof LiteralUnlimitedNatural)
            {
                final LiteralUnlimitedNatural litInt = (LiteralUnlimitedNatural)multValue;
                value = litInt.getValue();
            }

            else if (multValue instanceof LiteralString)
            {
                final LiteralString litStr = (LiteralString)multValue;
                final String multString = litStr.getValue();
                if ("*".equals(multString))
                {
                    value = LiteralUnlimitedNatural.UNLIMITED;
                }
                else
                {
                    value = Integer.parseInt(multString);
                }
            }
            else
            {
                // Construct a String with the named element and default value
                String multString = multValue.toString();
                String forValue = "";
                // property owns the upper/lower value OpaqueExpression which owns the multiplicity value body
                final Element element = multValue.getOwner();
                if (element instanceof Property)
                {
                    final Property property = (Property)element;
                    forValue = " in property " + property.getQualifiedName();
                }
                if (multValue instanceof OpaqueExpression)
                {
                    final OpaqueExpression expression = (OpaqueExpression)multValue;
                    final EList<String> bodies = expression.getBodies();
                    if (bodies != null && !bodies.isEmpty())
                    {
                        multString = bodies.get(0);
                    }
                }
                LOGGER.error("Invalid multiplicity value" + forValue + ", using default " + defaultValue + ": " + multString);
            }
        }
        /*if (logger.isDebugEnabled())
        {
            logger.debug("Parsing multiplicity: intValue = " + value + " value: " + multValue);
        }*/
        return value;
    }

    /**
     * There is an issue with EMF / XMI about tag value name (there should not be any @ or . inside)
     * This method checks whether <code>tagValueName</code> can be seen as <code>requestedName</code>.
     * <li>We compare them either:
     *   without name transformation
     *   removing initial '@' and replacing '.' by '_' (rsm / emf-uml2 profile)
     *   replacing initial '@' with '_' and removing '.' (EMF Normalization, RSM migration of MD 9.5 profiles)
     * EMF normalization (for MD11.5 export)
     *
     * @param requestedName
     * @param tagValueName
     * @return Equals
     */
    public static boolean doesTagValueNameMatch(
        final String requestedName,
        final String tagValueName)
    {
        if (StringUtils.isBlank(requestedName) || StringUtils.isBlank(tagValueName))
        {
            return false;
        }
        boolean result = requestedName.equals(tagValueName);
        if (!result)
        {
            if (requestedName.charAt(0) == '@')
            {
                // let's try rsm guess
                String rsmName = requestedName.substring(1);
                rsmName =
                    rsmName.replace(
                        '.',
                        '_');
                result = rsmName.equals(tagValueName);
                if (!result)
                {
                    // let's try emf normalization
                    final String emfName = EMFNormalizer.getEMFName(requestedName);
                    result = emfName.equals(tagValueName);
                }
                /*}
                if (!result && tagValueName.startsWith("@"))
                {
                    // let's try rsm guess
                    String rsmName = tagValueName.substring(1);
                    rsmName =
                        rsmName.replace(
                            '.',
                            '_');
                    result = requestedName.equals(rsmName);
                    if (!result)
                    {
                        // let's try emf normalization
                        String emfName = EMFNormalizer.getEMFName(tagValueName);
                        result = requestedName.equals(emfName);
                    }
                }*/
                // RSM converts @andromda.value to _andromdavalue when upgrading MD 9.5 profile
                if (!result)
                {
                    rsmName =
                        '_' + StringUtils.remove(requestedName.substring(1), '.');
                    result = rsmName.equals(tagValueName);
                }
            }
            else
            {
                // MD converts to _andromdavalue when exporting to EMF UML2 (v2.x) XMI and
                // the requestValue uses andromda_value
                final String emfName = '_' + StringUtils.remove(requestedName, '_');
                result = emfName.equals(tagValueName);
            }
        }
        return result;
    }

    // hack to use a protected method
    private static class EMFNormalizer
        extends UML2Util
    {
        public static String getEMFName(final String name)
        {
            return getValidJavaIdentifier(name);
        }
    }

    // Sort Attributes and AssociationEnds
    @SuppressWarnings("unused")
    private static class PropertyComparator implements Comparator<Property>
    {
        private static final long serialVersionUID = 1L;
        public int compare(final Property property1, final Property property2)
        {
            return property1.getName().compareTo(property2.getName());
        }
    }
}
