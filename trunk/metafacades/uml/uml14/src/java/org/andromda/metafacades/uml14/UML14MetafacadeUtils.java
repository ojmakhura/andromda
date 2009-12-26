package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Utilities for dealing with UML 1.4 metafacades
 *
 * @author Chad Brandon
 * @author Bob Fields
 */
public class UML14MetafacadeUtils
{
    /**
     * Finds a given model element in the model having the specified
     * <code>fullyQualifiedName</code>. If the model element can <strong>NOT
     * </strong> be found, <code>null</code> will be returned instead.
     * 
     * @param fullyQualifiedName the fully qualified name of the element to
     *        search for.
     * @param separator the PSM separator used for qualifying the name (example
     *        ".").
     * @param modelName a flag indicating whether or not a search shall be performed using
     *        the fully qualified model name or fully qualified PSM name.
     * @return the found model element
     */
    static Object findByFullyQualifiedName(final String fullyQualifiedName, final String separator, final boolean modelName)
    {
        Object modelElement;
        Collection elements = ((org.omg.uml.UmlPackage)MetafacadeFactory.getInstance().getModel().getModel()).getCore()
                .getModelElement()
                .refAllOfType();
        modelElement = CollectionUtils.find(elements, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                ModelElement element = (ModelElement)object;
                StringBuffer fullName = new StringBuffer(getPackageName(element, separator, modelName));
                String name = element.getName();
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
        });
        return modelElement;
    }

    private static String empty = "";
    /**
     * Constructs the package name for the given <code>metaObject</code>, separating the package name by the given
     * <code>separator</code>.
     *
     * @param metaObject the Model Element
     * @param separator the PSM namespace separator
     * @param modelName true/false on whether or not to get the model package name instead
     *        of the PSM package name.
     * @return the package name.
     */
    static String getPackageName(ModelElement metaObject, String separator, boolean modelName)
    {
        String packageName = empty;
        for (ModelElement namespace = metaObject.getNamespace(); (namespace instanceof UmlPackage) &&
                !(namespace instanceof Model); namespace = namespace.getNamespace())
        {
            packageName = packageName.equals(empty) ? namespace.getName() : namespace.getName() + separator + packageName;
        }
        if (modelName && StringUtils.isNotBlank(packageName))
        {
            packageName = StringUtils.replace(packageName, separator, MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR);
        }
        return packageName;
    }

    /**
     * Basically just checks to make sure the <code>model</code> is of type <code>org.omg.uml.UmlPackage</code> and
     * retrieves the <code>CorePackage</code> from it.
     *
     * @return the <code>model</code> as a <code>org.omg.uml.UmlPackage</code>
     */
    static CorePackage getCorePackage()
    {
        return ((org.omg.uml.UmlPackage)MetafacadeFactory.getInstance().getModel().getModel()).getCore();
    }

    /**
     * Finds and returns the first model element having the given <code>name</code> in the <code>modelPackage</code>,
     * returns <code>null</code> if not found.
     *
     * @param name the name to find.
     * @return the found model element.
     */
    static Object findByName(final String name)
    {
        Object modelElement = null;
        if (StringUtils.isNotBlank(name))
        {
            modelElement = CollectionUtils.find(getModel().getCore().getModelElement().refAllOfType(), new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    return StringUtils.trimToEmpty(((ModelElement)object).getName()).equals(name);
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
        Collection rootPackages = UML14MetafacadeUtils.getModel().getModelManagement().getModel().refAllOfType();
        Iterator packageIt = rootPackages.iterator();
        while (packageIt.hasNext())
        {
            rootPackage = packageIt.next();
            // get the first package that's a ModelElement instance
            // Note: UML2 allows top level ModelElement to be a Package.
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
        return (org.omg.uml.UmlPackage)MetafacadeFactory.getInstance().getModel().getModel();
    }

    /**
     * Gets the correct meta model visibility kind for the given <code>visibility</code> string.
     *
     * @param visibility the visibility to retrieve.
     * @return the VisibilityKind
     */
    static VisibilityKind getVisibilityKind(String visibility)
    {
        VisibilityKind visibilityKind = null;
        visibility = StringUtils.trimToEmpty(visibility);
        if ("public".equals(visibility))
        {
            visibilityKind = VisibilityKindEnum.VK_PUBLIC;
        }
        else if ("private".equals(visibility))
        {
            visibilityKind = VisibilityKindEnum.VK_PRIVATE;
        }
        else if (StringUtils.isEmpty(visibility))
        {
            visibilityKind = VisibilityKindEnum.VK_PACKAGE;
        }
        else if ("protected".equals(visibility))
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
     * @param separator the separator used for qualifying the name.
     * @return the new Attribute.
     */
    static Attribute createAttribute(String name, String fullyQualifiedTypeName, String visibility, String separator)
    {
        Attribute attribute = UML14MetafacadeUtils.getCorePackage().getAttribute().createAttribute();
        attribute.setName(name);
        attribute.setVisibility(UML14MetafacadeUtils.getVisibilityKind(visibility));
        Object type = UML14MetafacadeUtils.findByFullyQualifiedName(fullyQualifiedTypeName, separator, false);
        if (type != null && Classifier.class.isAssignableFrom(type.getClass()))
        {
            attribute.setType((Classifier)type);
        }
        return attribute;
    }

    /**
     * Indicates whether or not the attribute exists on the given </code>classifier</code>.
     * 
     * @param classifier the classifier to check
     * @param name the name of the attribute
     * @return true/false
     */
    static boolean attributeExists(Object classifier, String name)
    {
        boolean exists = false;
        if (Classifier.class.isAssignableFrom(classifier.getClass()))
        {
            List features = ((Classifier)classifier).getFeature();
            if (features != null && !features.isEmpty())
            {
                for (final Iterator featureIterator = features.iterator(); featureIterator.hasNext();)
                {
                    Object feature = featureIterator.next();
                    if (feature != null && Attribute.class.isAssignableFrom(feature.getClass()))
                    {
                        exists = StringUtils.trimToEmpty(((Attribute)feature).getName()).equals(name);
                        if(exists)
                        {
                            break;
                        }
                    }
                }
            }
        }
        return exists;
    }

    /**
     * Finds or creates a stereotype with the given name. If the stereotype isn't found, it will be created.
     *
     * @param name the name of the stereotype.
     * @return the new Stereotype.
     */
    static Stereotype findOrCreateStereotype(String name)
    {
        Object stereotype = UML14MetafacadeUtils.findByName(name);
        if (stereotype == null || !Stereotype.class.isAssignableFrom(stereotype.getClass()))
        {
            stereotype = UML14MetafacadeUtils.getCorePackage().getStereotype().createStereotype();
            ((Stereotype)stereotype).setName(name);
        }
        return (Stereotype)stereotype;
    }

    /**
     * Returns the first use-case it can find with the given name.
     */
    static UseCase findFirstUseCaseWithName(String name)
    {
        return findFirstUseCaseWithNameAndStereotype(name, null);
    }

    /**
     * Returns the first use-case it can find with the given name and stereotype, if the stereotype is not specified (it
     * is null) it will be ignored and the returned use-case may have any arbitrary stereotype.
     */
    static UseCase findFirstUseCaseWithNameAndStereotype(String name, String stereotypeName)
    {
        UseCase useCaseWithNameAndStereotype = null;

        Collection<UseCase> useCases = getModel().getUseCases().getUseCase().refAllOfType();
        for (final Iterator<UseCase> useCaseIterator = useCases.iterator(); useCaseIterator.hasNext() && useCaseWithNameAndStereotype ==
                null;)
        {
            UseCase useCase = useCaseIterator.next();
            if (name.equals(useCase.getName()))
            {
                if (stereotypeName == null || isStereotypePresent(useCase, stereotypeName))
                {
                    useCaseWithNameAndStereotype = useCase;
                }
            }
        }

        return useCaseWithNameAndStereotype;
    }

    /**
     * Returns the first activity graph it can find with the given name.
     */
    static ActivityGraph findFirstActivityGraphWithName(String name)
    {
        return findFirstActivityGraphWithNameAndStereotype(name, null);
    }

    /**
     * Returns the first activity graph it can find with the given name and stereotype, if the stereotype is not
     * specified (it is null) it will be ignored and the returned activity graph may have any arbitrary stereotype.
     */
    static ActivityGraph findFirstActivityGraphWithNameAndStereotype(String name, String stereotypeName)
    {
        ActivityGraph graphWithNameAndStereotype = null;

        Collection<ActivityGraph> graphs = getModel().getActivityGraphs().getActivityGraph().refAllOfType();
        for (final Iterator<ActivityGraph> graphIterator = graphs.iterator();
             graphIterator.hasNext() && graphWithNameAndStereotype == null;)
        {
            ActivityGraph graph = graphIterator.next();
            if (name.equals(graph.getName()))
            {
                if (stereotypeName == null || isStereotypePresent(graph, stereotypeName))
                {
                    graphWithNameAndStereotype = graph;
                }
            }
        }

        return graphWithNameAndStereotype;
    }

    /**
     * Returns true if the given model element has a tag with the given name and value, returns false otherwise.
     * @param element 
     * @param tag 
     * @param value 
     * @return tagPresent
     */
    static boolean isTagPresent(ModelElement element, String tag, Object value)
    {
        boolean tagPresent = false;

        Collection<TaggedValue> taggedValues = element.getTaggedValue();
        for (final Iterator<TaggedValue> taggedValueIterator = taggedValues.iterator(); taggedValueIterator.hasNext() && !tagPresent;)
        {
            TaggedValue taggedValue = taggedValueIterator.next();
            // does this name match the argument tagged value name ?
            // Check both the UML14 format name @andromda.value and EMF Format andromda_whatever
            String tagName = taggedValue.getName();
            if (tag.equals(tagName) || MetafacadeUtils.getEmfTaggedValue(tag).equals(tagName)
                || MetafacadeUtils.getUml14TaggedValue(tag).equals(tagName))
            {
                for (final Iterator valueIterator = taggedValue.getDataValue().iterator(); valueIterator.hasNext() &&
                        !tagPresent;)
                {
                    Object dataValue = valueIterator.next();
                    if (value.equals(dataValue))
                    {
                        tagPresent = true;
                    }
                }
                for (final Iterator valueIterator = taggedValue.getReferenceValue().iterator(); valueIterator.hasNext() &&
                        !tagPresent;)
                {
                    Object referenceValue = valueIterator.next();
                    if (value.equals(referenceValue))
                    {
                        tagPresent = true;
                    }
                }
            }
        }
        return tagPresent;
    }

    /**
     * Returns true if the given model element has a hyperlink with the given value, returns false otherwise.
     */
    static boolean isHyperlinkPresent(ModelElement element, Object value)
    {
        return isTagPresent(element, "hyperlinkModel", value);
    }

    static boolean isStereotypePresent(ModelElement element, String stereotypeName)
    {
        boolean stereotypePresent = false;

        Collection<Stereotype> stereotypes = element.getStereotype();
        for (final Iterator<Stereotype> stereotypeIterator = stereotypes.iterator();
             stereotypeIterator.hasNext() && !stereotypePresent;)
        {
            Stereotype stereotype = stereotypeIterator.next();
            if (stereotypeName.equals(stereotype.getName()))
            {
                stereotypePresent = true;
            }
        }
        return stereotypePresent;
    }

    /**
     * Returns the first use-case this method can find with the given tagged value or hyperlink. Both arguments are used
     * to look for the tagged value but only <code>value</code> is used to search for the hyperlink.
     */
    static UseCase findUseCaseWithTaggedValueOrHyperlink(String tag, String value)
    {
        UseCase useCaseWithTaggedValue = null;

        Collection<UseCase> useCases = getModel().getUseCases().getUseCase().refAllOfType();
        for (final Iterator<UseCase> useCaseIterator = useCases.iterator(); useCaseIterator.hasNext() && useCaseWithTaggedValue ==
                null;)
        {
            // loop over all use-cases
            UseCase useCase = useCaseIterator.next();
            if (isTagPresent(useCase, tag, value) || isHyperlinkPresent(useCase, value))
            {
                useCaseWithTaggedValue = useCase;
            }
        }

        return useCaseWithTaggedValue;
    }

    /**
     * Returns the first class this method can find with the given tagged value or hyperlink. Both arguments are used to
     * look for the tagged value but only <code>value</code> is used to search for the hyperlink.
     */
    static UmlClass findClassWithTaggedValueOrHyperlink(String tag, String value)
    {
        UmlClass classWithTaggedValue = null;

        Collection<UmlClass> classes = getModel().getCore().getUmlClass().refAllOfType();
        for (final Iterator<UmlClass> classIterator = classes.iterator(); classIterator.hasNext() && classWithTaggedValue == null;)
        {
            // loop over all classes
            UmlClass clazz = classIterator.next();
            if (isTagPresent(clazz, tag, value) || isHyperlinkPresent(clazz, value))
            {
                classWithTaggedValue = clazz;
            }
        }

        return classWithTaggedValue;
    }

    static Collection<FinalState> findFinalStatesWithNameOrHyperlink(UseCase useCase)
    {
        List finalStates = new ArrayList();

        if (useCase != null && useCase.getName() != null)
        {
            String useCaseName = useCase.getName();
            Collection<FinalState> allFinalStates = getModel().getStateMachines().getFinalState().refAllOfType();
            for (final Iterator<FinalState> iterator = allFinalStates.iterator(); iterator.hasNext();)
            {
                FinalState finalState = iterator.next();
                if (useCaseName != null)
                {
                    if (useCaseName.equals(finalState.getName()))
                    {
                        finalStates.add(finalState);
                    }
                    else
                    {
                        if (isHyperlinkPresent(finalState, useCase))
                        {
                            finalStates.add(finalState);
                        }
                    }
                }
                else
                {
                    if (isHyperlinkPresent(finalState, useCase))
                    {
                        finalStates.add(finalState);
                    }
                }
            }
        }

        return finalStates;
    }

    /**
     * Finds the given metafacade class for the passed in <code>facade</code>.
     *
     * @param facade the model element facade for which to find the meta class.
     * @return the meta model element
     */
    static ActivityGraph getMetaClass(ActivityGraphFacade facade)
    {
        ActivityGraph activityGraph = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection<ModelElement> graphs = getModel().getActivityGraphs().getActivityGraph().refAllOfType();
            for (final Iterator<ModelElement> iterator = graphs.iterator(); iterator.hasNext() && activityGraph == null;)
            {
                ModelElement element = iterator.next();
                if (id.equals(element.refMofId()))
                {
                    activityGraph = (ActivityGraph)element;
                }
            }
        }
        return activityGraph;
    }

    /**
     * Finds the given metafacade class for the passed in <code>facade</code>.
     *
     * @param facade the model element facade for which to find the meta class.
     * @return the meta model element
     */
    static UseCase getMetaClass(UseCaseFacade facade)
    {
        UseCase useCase = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection<ModelElement> useCases = getModel().getUseCases().getUseCase().refAllOfType();
            for (final Iterator<ModelElement> iterator = useCases.iterator(); iterator.hasNext() && useCase == null;)
            {
                ModelElement element = iterator.next();
                if (id.equals(element.refMofId()))
                {
                    useCase = (UseCase)element;
                }
            }
        }
        return useCase;
    }

    /**
     * Finds the given metafacade class for the passed in <code>facade</code>.
     *
     * @param facade the model element facade for which to find the meta class.
     * @return the meta model element
     */
    static Parameter getMetaClass(ParameterFacade facade)
    {
        Parameter parameter = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection<ModelElement> parameters = getModel().getCore().getParameter().refAllOfType();
            for (final Iterator<ModelElement> iterator = parameters.iterator(); iterator.hasNext() && parameter == null;)
            {
                ModelElement element = iterator.next();
                if (id.equals(element.refMofId()))
                {
                    parameter = (Parameter)element;
                }
            }
        }
        return parameter;
    }

    /**
     * Finds the given metafacade class for the passed in <code>facade</code>.
     *
     * @param facade the model element facade for which to find the meta class.
     * @return the meta model element
     */
    static Event getMetaClass(EventFacade facade)
    {
        Event event = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection<ModelElement> events = getModel().getStateMachines().getEvent().refAllOfType();
            for (final Iterator<ModelElement> iterator = events.iterator(); iterator.hasNext() && event == null;)
            {
                ModelElement element = iterator.next();
                if (id.equals(element.refMofId()))
                {
                    event = (Event)element;
                }
            }
        }
        return event;
    }

    /**
     * Finds the given metafacade class for the passed in <code>facade</code>.
     *
     * @param facade the model element facade for which to find the meta class.
     * @return the meta model element
     */
    static ModelElement getMetaClass(ModelElementFacade facade)
    {
        ModelElement modelElement = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection<ModelElement> modelElements = getModel().getCore().getModelElement().refAllOfType();
            for (final Iterator<ModelElement> iterator = modelElements.iterator(); iterator.hasNext() && modelElement == null;)
            {
                ModelElement element = iterator.next();
                if (id.equals(element.refMofId()))
                {
                    modelElement = element;
                }
            }
        }
        return modelElement;
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
    static String getSerialVersionUID(ClassifierFacade classifier)
    {
        ExceptionUtils.checkNull("classifer", classifier);
        String serialVersionString = (String)classifier
                .findTaggedValue(UMLProfile.TAGGEDVALUE_SERIALVERSION_UID);
        return StringUtils.trimToNull(serialVersionString);
    }
    
    /**
     * This method removes all duplicates within the <code>elements</code> collection while at the same
     * time copying tagged values from duplicates to the one remaining element with the given name.
     * 
     * @param elements the elements to remove duplicates and copy tagged values to.
     * @return the elements with duplicates removed.
     */
    public static List<ModelElementFacade> removeDuplicatesAndCopyTaggedValues(final Collection<ModelElementFacade> elements)
    {
        final Map<String, ModelElementFacade> map = new LinkedHashMap<String, ModelElementFacade>();
        if (elements != null)
        {
            for (final Iterator<ModelElementFacade> iterator = elements.iterator(); iterator.hasNext();)
            {
                ModelElementFacade element = iterator.next();
                final String name = element.getName();
                final ModelElementFacade existingVariable = map.get(name);
                // - copy over any tagged values from the existing variable to the new one.
                if (existingVariable != null)
                {
                    element.copyTaggedValues(existingVariable);
                }
                map.put(
                    name,
                    element);
            }   
        }
        return new ArrayList<ModelElementFacade>(map.values());
    }
}