package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.commonbehavior.DataValue;
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
        Pattern pattern = Pattern
            .compile(".*\\s*" + StringUtils.trimToEmpty(kind)
                + "\\s*\\w*\\s*:.*", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(StringUtils.trimToEmpty(expression));
        return matcher.matches();
    }

    /**
     * Returns true or false depending on whether or not this Classifier or any
     * of its specializations is of the given type having the specified
     * <code>typeName</code>
     * 
     * @param typeName the name of the type (i.e. datatype.Collection)
     * @return true/false
     */
    static boolean isType(ClassifierFacade classifier, String typeName)
    {
        final String type = StringUtils.trimToEmpty(typeName);
        String name = StringUtils.trimToEmpty(classifier
            .getFullyQualifiedName(true));
        boolean isType = name.equals(type);
        // if this isn't a type defined by typeName, see if we can find any
        // types that inherit from the type.
        if (!isType)
        {
            isType = CollectionUtils.find(
                classifier.getAllGeneralizations(),
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        String name = StringUtils
                            .trimToEmpty(((ModelElementFacade)object)
                                .getFullyQualifiedName(true));
                        return name.equals(type);
                    }
                }) != null;
        }
        return isType;
    }

    /**
     * Returns the first use-case it can find with the given name.
     */
    static UseCase findFirstUseCaseWithName(String name)
    {
        return findFirstUseCaseWithNameAndStereotype(name, null);
    }

    /**
     * Returns the first use-case it can find with the given name and
     * stereotype, if the stereotype is not specified (it is null) it will be
     * ignored and the returned use-case may have any arbitrary stereotype.
     */
    static UseCase findFirstUseCaseWithNameAndStereotype(
        String name,
        String stereotypeName)
    {
        UseCase useCaseWithNameAndStereotype = null;

        Collection useCases = getModel().getUseCases().getUseCase()
            .refAllOfType();
        for (Iterator useCaseIterator = useCases.iterator(); useCaseIterator
            .hasNext()
            && useCaseWithNameAndStereotype == null;)
        {
            UseCase useCase = (UseCase)useCaseIterator.next();
            if (name.equals(useCase.getName()))
            {
                if (stereotypeName == null
                    || isStereotypePresent(useCase, stereotypeName))
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
     * Returns the first activity graph it can find with the given name and
     * stereotype, if the stereotype is not specified (it is null) it will be
     * ignored and the returned activity graph may have any arbitrary
     * stereotype.
     */
    static ActivityGraph findFirstActivityGraphWithNameAndStereotype(
        String name,
        String stereotypeName)
    {
        ActivityGraph graphWithNameAndStereotype = null;

        Collection graphs = getModel().getActivityGraphs().getActivityGraph()
            .refAllOfType();
        for (Iterator graphIterator = graphs.iterator(); graphIterator
            .hasNext()
            && graphWithNameAndStereotype == null;)
        {
            ActivityGraph graph = (ActivityGraph)graphIterator.next();
            if (name.equals(graph.getName()))
            {
                if (stereotypeName == null
                    || isStereotypePresent(graph, stereotypeName))
                {
                    graphWithNameAndStereotype = graph;
                }
            }
        }

        return graphWithNameAndStereotype;
    }

    /**
     * Returns true if the given model element has a tag with the given name and
     * value, returns false otherwise.
     */
    static boolean isTagPresent(ModelElement element, String tag, Object value)
    {
        boolean tagPresent = false;

        Collection taggedValues = element.getTaggedValue();
        for (Iterator taggedValueIterator = taggedValues.iterator(); taggedValueIterator
            .hasNext()
            && !tagPresent;)
        {
            TaggedValue taggedValue = (TaggedValue)taggedValueIterator.next();
            if (tag.equals(taggedValue.getName()))
            {
                for (Iterator valueIterator = taggedValue.getDataValue()
                    .iterator(); valueIterator.hasNext() && !tagPresent;)
                {
                    DataValue dataValue = (DataValue)valueIterator.next();
                    if (value.equals(dataValue))
                    {
                        tagPresent = true;
                    }
                }
                for (Iterator valueIterator = taggedValue.getReferenceValue()
                    .iterator(); valueIterator.hasNext() && !tagPresent;)
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
     * Returns true if the given model element has a hyperlink with the given
     * value, returns false otherwise.
     */
    static boolean isHyperlinkPresent(ModelElement element, Object value)
    {
        return isTagPresent(element, "hyperlinkModel", value);
    }

    static boolean isStereotypePresent(
        ModelElement element,
        String stereotypeName)
    {
        boolean stereotypePresent = false;

        Collection stereotypes = element.getStereotype();
        for (Iterator stereotypeIterator = stereotypes.iterator(); stereotypeIterator
            .hasNext()
            && !stereotypePresent;)
        {
            Stereotype stereotype = (Stereotype)stereotypeIterator.next();
            if (stereotypeName.equals(stereotype.getName()))
            {
                stereotypePresent = true;
            }
        }
        return stereotypePresent;
    }

    /**
     * Returns the first use-case this method can find with the given tagged
     * value or hyperlink. Both arguments are used to look for the tagged value
     * but only <code>value</code> is used to search for the hyperlink.
     */
    static UseCase findUseCaseWithTaggedValueOrHyperlink(
        String tag,
        String value)
    {
        UseCase useCaseWithTaggedValue = null;

        Collection useCases = getModel().getUseCases().getUseCase()
            .refAllOfType();
        for (Iterator useCaseIterator = useCases.iterator(); useCaseIterator
            .hasNext()
            && useCaseWithTaggedValue == null;)
        {
            // loop over all use-cases
            UseCase useCase = (UseCase)useCaseIterator.next();
            if (isTagPresent(useCase, tag, value)
                || isHyperlinkPresent(useCase, value))
            {
                useCaseWithTaggedValue = useCase;
            }
        }

        return useCaseWithTaggedValue;
    }

    /**
     * Returns the first class this method can find with the given tagged value
     * or hyperlink. Both arguments are used to look for the tagged value but
     * only <code>value</code> is used to search for the hyperlink.
     */
    static UmlClass findClassWithTaggedValueOrHyperlink(String tag, String value)
    {
        UmlClass classWithTaggedValue = null;

        Collection classes = getModel().getCore().getUmlClass().refAllOfType();
        for (Iterator classIterator = classes.iterator(); classIterator
            .hasNext()
            && classWithTaggedValue == null;)
        {
            // loop over all use-cases
            UmlClass clazz = (UmlClass)classIterator.next();
            if (isTagPresent(clazz, tag, value)
                || isHyperlinkPresent(clazz, value))
            {
                classWithTaggedValue = clazz;
            }
        }

        return classWithTaggedValue;
    }

    static Collection findFinalStatesWithNameOrHyperlink(UseCase useCase)
    {
        List finalStates = new ArrayList();

        if (useCase != null && useCase.getName() != null)
        {
            String useCaseName = useCase.getName();
            Collection allFinalStates = getModel().getStateMachines()
                .getFinalState().refAllOfType();
            for (Iterator iterator = allFinalStates.iterator(); iterator
                .hasNext();)
            {
                FinalState finalState = (FinalState)iterator.next();
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

    static ActivityGraph getMetaClass(ActivityGraphFacade facade)
    {
        ActivityGraph activityGraph = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection graphs = getModel().getActivityGraphs()
                .getActivityGraph().refAllOfType();
            for (Iterator iterator = graphs.iterator(); iterator.hasNext()
                && activityGraph == null;)
            {
                ModelElement element = (ModelElement)iterator.next();
                if (id.equals(element.refMofId()))
                {
                    activityGraph = (ActivityGraph)element;
                }
            }
        }
        return activityGraph;
    }

    static UseCase getMetaClass(UseCaseFacade facade)
    {
        UseCase useCase = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection useCases = getModel().getUseCases().getUseCase()
                .refAllOfType();
            for (Iterator iterator = useCases.iterator(); iterator.hasNext()
                && useCase == null;)
            {
                ModelElement element = (ModelElement)iterator.next();
                if (id.equals(element.refMofId()))
                {
                    useCase = (UseCase)element;
                }
            }
        }
        return useCase;
    }

    static Parameter getMetaClass(ParameterFacade facade)
    {
        Parameter parameter = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection parameters = getModel().getCore().getParameter()
                .refAllOfType();
            for (Iterator iterator = parameters.iterator(); iterator.hasNext()
                && parameter == null;)
            {
                ModelElement element = (ModelElement)iterator.next();
                if (id.equals(element.refMofId()))
                {
                    parameter = (Parameter)element;
                }
            }
        }
        return parameter;
    }

    static Event getMetaClass(EventFacade facade)
    {
        Event event = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection events = getModel().getStateMachines().getEvent()
                .refAllOfType();
            for (Iterator iterator = events.iterator(); iterator.hasNext()
                && event == null;)
            {
                ModelElement element = (ModelElement)iterator.next();
                if (id.equals(element.refMofId()))
                {
                    event = (Event)element;
                }
            }
        }
        return event;
    }

    static ModelElement getMetaClass(ModelElementFacade facade)
    {
        ModelElement modelElement = null;

        if (facade != null)
        {
            String id = facade.getId();
            Collection modelElements = getModel().getCore().getModelElement()
                .refAllOfType();
            for (Iterator iterator = modelElements.iterator(); iterator
                .hasNext()
                && modelElement == null;)
            {
                ModelElement element = (ModelElement)iterator.next();
                if (id.equals(element.refMofId()))
                {
                    modelElement = element;
                }
            }
        }
        return modelElement;
    }

}