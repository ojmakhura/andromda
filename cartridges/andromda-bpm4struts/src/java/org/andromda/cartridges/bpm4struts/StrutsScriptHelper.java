package org.andromda.cartridges.bpm4struts;

import org.andromda.core.common.HTMLAnalyzer;
import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.RepositoryReadException;
import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.common.DbMappingTable;
import org.andromda.core.mdr.MDRepositoryFacade;
import org.andromda.core.uml14.DirectionalAssociationEnd;
import org.andromda.core.uml14.UMLDynamicHelper;
import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.UmlPackage;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.ClassifierInState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.Model;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * This class is a script helper designed for the AndroMDA Struts cartridge that works with
 * the dynamic part of a UML model such as the Activity Graphs.
 * <p>
 * It extends UMLDynamicHelper in order to have the basic functionality, all the extras
 * are cartridge-dependent.
 *
 * @author <a href="mailto:draftdog@users.sourceforge.net">Wouter Zoons</a>
 */
public final class StrutsScriptHelper implements ScriptHelper, RepositoryFacade
{
    /**
     * The meta-data repository implementation used by this script helper.
     */
    private final MDRepositoryFacade repository = new MDRepositoryFacade();

    /**
     * The UML static helper delegate.
     */
    private final UMLStaticHelper staticHelper = new UMLStaticHelper();

    /**
     * The UML dynamic helper delegate.
     */
    private final UMLDynamicHelper dynamicHelper = new UMLDynamicHelper();

    /**
     * Returns a reference to the UML model.
     *
     * @return the UMLPackage instance that represents the UML model
     */
    private UmlPackage getUMLModel()
    {
        return (UmlPackage)staticHelper.getModel();
    }

    /**
     * Returns the model element in the model that has been marked as the controller
     * class for the argument use case.
     * <p>
     * You can mark such class by adding a tagged value, such as
     * <code>ControllerClass=org.project.web.actions.MyController</code>.
     *
     * @param useCase the graph which controller model element to return
     * @return the UMLClass that is the controller or <code>null</code> if the
     *    tagged value is not present
     */
    public UmlClass getControllerClass(UseCase useCase)
    {
        String tagValue = findTagValue(useCase, "ControllerClass");
        return findClassByName(tagValue);
    }

    /**
     * This method returns the model element that holds to information for the
     * controller form.
     * <p>
     * First this method looks up the controller class. If there is a UML class
     * found by means of the tagged values of the argument activity graph, than
     * this method will look at any associations, if one of them is marked
     * by the 'FrontEndPresentation' stereotype it will be returned.
     * <p>
     * In any other case this method returns <code>null</code>.
     *
     * @param useCase the graph which form model element to return, may not be <code>null</code>
     * @return the UMLClass that is the form, or <code>null</code> if there
     *    is no form
     * @see #getControllerClass(UseCase useCase)
     */
    public UmlClass getControllerForm(UseCase useCase)
    {
        ModelElement controllerClass = getControllerClass(useCase);
        if (controllerClass != null)
        {
            Collection associationEnds = staticHelper.getAssociationEnds(controllerClass);
            for (Iterator iterator = associationEnds.iterator(); iterator.hasNext();)
            {
                AssociationEnd associationEnd = (AssociationEnd) iterator.next();
                DirectionalAssociationEnd directionalAssociationEnd = staticHelper.getAssociationData(associationEnd);
                Classifier participant = directionalAssociationEnd.getTarget().getParticipant();
                if (staticHelper.getStereotypeNames(participant).contains("FrontEndPresentation"))
                {
                    return (UmlClass) participant;
                }
            }
        }

        return null;
    }

    /**
     * Returns a collection of all the UMLClass instances that have the
     * 'FrontEndPresentation' stereotype found in the UML model.
     *
     * @return A Collection of UMLClass instances that have the
     *  'FrontEndPresentation' stereotype, never <code>null</code>
     */
    public Collection getAllControllerForms()
    {
        final Set controllerForms = new LinkedHashSet();
        Collection classes = getUMLModel().getCore().getClassifier().refAllOfType();
        for (Iterator iterator = classes.iterator(); iterator.hasNext();)
        {
            Classifier classifier = (Classifier) iterator.next();
            if (staticHelper.getStereotypeNames(classifier).contains("FrontEndPresentation"))
            {
                controllerForms.add(classifier);
            }
        }
        return controllerForms;
    }

    /**
     * Returns the use-case that holds the argument activity graph.
     *
     * @param activityGraph an activity graph, may not be <code>null</code>
     * @return the use-case of which this activity graph is a part,
     *  or <code>null</code> in case there is none
     */
    public UseCase getParentUseCase(ActivityGraph activityGraph)
    {
        Namespace parentNamespace = activityGraph.getNamespace();
        if (parentNamespace instanceof UseCase)
        {
            return (UseCase) parentNamespace;
        }
        else
        {
            return null;
        }
    }

    /**
     * Delegates to UMLStaticHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper
     */
    public String findTagValue(ModelElement modelElement, String tagName)
    {
        return staticHelper.findTagValue(modelElement, tagName);
    }

    /**
     * Gets the 'state' property of an ObjectFlowState instance.
     *
     * @param objectFlowState an object flow state
     * @return the argument's state property
     */
    public State getObjectFlowStateState(ObjectFlowState objectFlowState)
    {
        Object stateObject = ((ClassifierInState) objectFlowState.getType()).getInState().iterator().next();
        return (State) stateObject;
    }

    public Set getObjectFlowStateTypes(ActivityGraph activityGraph)
    {
        Set types = new LinkedHashSet();
        Collection states = dynamicHelper.getObjectFlowStates(activityGraph);
        for (Iterator iterator = states.iterator(); iterator.hasNext();)
        {
            ObjectFlowState objectFlowState = (ObjectFlowState) iterator.next();
            types.add(objectFlowState.getType());
        }
        return types;
    }

    protected Set toSet(Collection firstElements, Collection secondElements)
    {
        Set set = new LinkedHashSet(firstElements);
        set.addAll(secondElements);
        return set;
    }


    /**
     * Delegates to UMLStaticHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper
     */
    public Collection getAttributes(Classifier classifier)
    {
        return staticHelper.getAttributes(classifier);
    }


    /**
     * Overridden to make sure each activity graph in the model has the 'FrontEndUseCase' stereotype and is included
     * in the resulting collection.
     * <p>
     * Many CASE tools do not cleanly (without proprietary extensions) support stereotypes/tagged-values to activity
     * diagrams or graph. What is done in this method is a workaround.
     */
    public Collection getModelElements()
    {
        Collection modelElements = getModelElements();
        Collection allActivityGraphs = dynamicHelper.getAllActivityGraphs();
        return toSet(modelElements, allActivityGraphs);
    }

    /**
     * Returns the package name of the argument ModelElement.
     * <p>
     * To construct the complete package name this method will consider only the
     * 'package' model elements from the argument to the root of the UML model, their names
     * are concatenated and separated with a dot.
     * <p>
     * Like this: org.andromda.core.uml14
     * <p>
     * <i>Note: This method will be called in order to determine a model element's package name.</i>
     *
     * @param object A model element
     * @return The package name of the model element, as a Java fully qualified name, never <code>null>/code>
     */
    public String getPackageName(Object object)
    {
        if (!(object instanceof ModelElement))
        {
            return "";
        }

        // action states never belong to any package directly
        if (dynamicHelper.isActionState(object))
        {
            return getPackageName(dynamicHelper.getStateMachine((ActionState) object));
        }

        if (dynamicHelper.isFinalState(object))
        {
            return getPackageName(dynamicHelper.getStateMachine((FinalState) object));
        }

        if (dynamicHelper.isInitialState(object))
        {
            return getPackageName(dynamicHelper.getStateMachine((Pseudostate) object));
        }

        ModelElement modelElement = (ModelElement) object;
        Namespace namespace = modelElement.getNamespace();

        String packageName = "";

        while ((namespace instanceof ModelElement) && !(namespace instanceof Model))
        {
            if (namespace instanceof UmlPackage)
            {
                String namespaceName = namespace.getName().trim();
                if (!"".equals(namespaceName))
                {
                    packageName = ("".equals(packageName)) ? namespaceName : namespaceName + '.' + packageName;
                }
            }
            namespace = namespace.getNamespace();
        }

        return packageName.toLowerCase();
    }


    /**
     * Depending on the type of the argument this method will return a suitable name for it, this is specific to
     * this cartridge only.
     * <p>
     * <table>
     *   <tr>
     *     <th>Object instance type</th>
     *     <th>returning name style</th>
     *   </tr>
     *   <tr>
     *     <td>StateVertex</td>
     *     <td>aaa-bbb-ccc</td>
     *   </tr>
     *   <tr>
     *     <td>UseCase</td>
     *     <td>TypicalClassName</td>
     *   </tr>
     *   <tr>
     *     <td>Other</td>
     *     <td>delegated to UMLStaticHelper</td>
     *   </tr>
     * </table>
     * <p>
     * <i>Note: This method will be called in order to determine a model element's file name.</i>
     *
     * @return a suitable file name (no extension), never <code>null</code>
     * @see org.andromda.core.uml14.UMLStaticHelper
     */
    public String getName(Object object)
    {
        if (!(object instanceof ModelElement))
        {
            return "";
        }

        ModelElement modelElement = (ModelElement) object;

        if (modelElement instanceof StateVertex)
        {
            return toWebFileName(modelElement);
        }
        else
            if (modelElement instanceof UseCase)
            {
                UmlClass controllerClass = getControllerClass((UseCase) modelElement);
                if (controllerClass == null)
                {
                    return "no_controller_class_name";
                }
                else
                {
                    return toJavaClassName(controllerClass);
                }
            }
            else
            {
                return staticHelper.getName(object);
            }
    }


    /**
     * Gets the package path to the argument object.
     * <p>
     * This is equivalent to <code>getPackageName(object)</code> and replacing each occurence
     * of a dot '.' with a slash '/'.
     *
     * @param modelElement a model element, may not be <code>null</code>
     * @return the package name as a path, never <code>null</code>
     */
    public String getPackagePath(ModelElement modelElement)
    {
        return getPackageName(modelElement).replace('.', '/');
    }


    /**
     * Returns the collection of Pseudostate instances of kind 'initial' found in the argument use-case.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Pseudostate</code>
     *
     * @param useCases A collection containing only UseCase instances, may not be <code>null</code>
     * @return the Pseudostate instances of kind 'initial' found in the UML model, never <code>null</code>
     */
    public Collection getAllInitialStates(Collection useCases)
    {
        final Collection initialStates = new LinkedList();
        final Collection activityGraphs = new LinkedList();

        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            UseCase useCase = (UseCase) iterator.next();
            activityGraphs.addAll(dynamicHelper.getActivityGraphs(useCase));
        }

        for (Iterator iterator = activityGraphs.iterator(); iterator.hasNext();)
        {
            ActivityGraph activityGraph = (ActivityGraph) iterator.next();
            initialStates.addAll(dynamicHelper.getInitialStates(activityGraph));
        }

        return initialStates;
    }

    /**
     * Returns a Collection containing all Transition instances that are going
     * out of a Pseudostate of kind 'choice'.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Transition</code>
     *
     * @param activityGraph an ActivityGraph instance, may not be <code>null</code>
     * @return the collection of the Transition instances going out of 'choice'
     *    Pseudostates found in the argument activity graph.
     */
    public Collection getActivityGraphChoiceTransitions(ActivityGraph activityGraph)
    {
        Set transitions = new HashSet();

        Collection choices = dynamicHelper.getChoices(activityGraph);
        for (Iterator iterator = choices.iterator(); iterator.hasNext();)
        {
            Pseudostate choice = (Pseudostate) iterator.next();
            transitions.addAll(getOutgoingStateTransitions(choice));
        }

        return transitions;
    }

    /**
     * Performs <code>getActivityGraphChoiceTransitions()</code> for each activity graph in the
     * argument use-case.
     *
     * @param useCase a use-case, may not be <code>null</code>
     * @return the collection of the Transition instances going out of 'choice'
     *    Pseudostates found for each activity graph in the argument use-case
     * @see #getActivityGraphChoiceTransitions(ActivityGraph activityGraph)
     */
    public Collection getUseCaseChoiceTransitions(UseCase useCase)
    {
        final Collection choiceTransitions = new LinkedList();
        Collection activityGraphs = dynamicHelper.getActivityGraphs(useCase);
        for (Iterator iterator = activityGraphs.iterator(); iterator.hasNext();)
        {
            ActivityGraph activityGraph = (ActivityGraph) iterator.next();
            choiceTransitions.addAll(getActivityGraphChoiceTransitions(activityGraph));
        }
        return choiceTransitions;
    }

    /**
     * Returns a Collection containing all Transition instances that are going
     * out of the argument StateVertex.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Transition</code>
     *
     * @param stateVertex the argument StateVertex, may not be <code>null</code>
     * @return the collection of the Transition instances going out of
     *    the argument state vertex.
     */
    public Collection getOutgoingStateTransitions(StateVertex stateVertex)
    {
        return stateVertex.getOutgoing();
    }


    /**
     * Converts the argument ModelElement's name to a class name as per Java conventions.
     * <p>
     * Any non-word characters (including whitespace) will be removed.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified,
     *  mya not be <code>null</code>
     * @return the argument's name converted into a Java class name
     */
    public String toJavaClassName(ModelElement modelElement)
    {
        return StringUtilsHelper.upperCaseFirstLetter(toJavaMethodName(modelElement));
    }

    /**
     * Converts the argument ModelElement's name to a method name as per Java conventions.
     * <p>
     * Any non-word characters (including whitespace) will be removed.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified,
     *  may not be <code>null</code>
     * @return the argument's name converted into a Java method name
     */
    public String toJavaMethodName(ModelElement modelElement)
    {
        String[] parts = splitAtNonWordCharacters(modelElement.getName());
        StringBuffer conversionBuffer = new StringBuffer();
        for (int i = 0; i < parts.length; i++)
        {
            conversionBuffer.append(StringUtilsHelper.upperCaseFirstLetter(parts[i]));
        }
        return StringUtilsHelper.lowerCaseFirstLetter(conversionBuffer.toString());
    }

    /**
     * Converts the argument ModelElement's name to a name suitable for web files.
     * <p>
     * Any non-word characters (including whitespace) will be removed (each sequence will be replaced
     * by a single hyphen '-').
     * <p>
     * The returned name contains no uppercase characters.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified,
     *  may not be <code>null</code>
     * @return the argument's name converted into a suitable web file name, there will be no extension added
     */
    public String toWebFileName(ModelElement modelElement)
    {
        return toLowercaseHyphenSeparatedName(modelElement.getName());
    }

    /**
     * Converts the argument to lowercase, removes all non-word characters, and replaces each of those
     * sequences by a hyphen '-'.
     */
    protected String toLowercaseHyphenSeparatedName(String name)
    {
        if (name == null)
        {
            return "";
        }

        final char separator = '-';
        String[] parts = splitAtNonWordCharacters(name.toLowerCase());
        StringBuffer conversionBuffer = new StringBuffer();

        for (int i = 0; i < parts.length - 1; i++)
        {
            conversionBuffer.append(parts[i]).append(separator);
        }
        conversionBuffer.append(parts[parts.length - 1]);

        return conversionBuffer.toString();
    }

    /**
     * Splits at each sequence of non-word characters.
     */
    protected String[] splitAtNonWordCharacters(String s)
    {
        return s.split("\\W+");
    }


    /**
     * <p>Formats an HTML String as a collection of paragraphs.
     * Each paragraph has a getLines() method that returns a collection
     * of Strings.</p>
     *
     * @param string the String to be formatted
     * @return Collection the collection of paragraphs found.
     */
    public Collection formatHTMLStringAsParagraphs(String string)
    {
        try
        {
            return new HTMLAnalyzer().htmlToParagraphs(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts the argument ModelElement's name to a fully qualified class name as per Java conventions.
     * <p>
     * Any non-word characters (including whitespace) will be removed.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified
     * @return the argument's name converted into a fully qualified Java class name
     */
    public String toFullyQualifiedJavaClassName(ModelElement modelElement)
    {
        String packageName = getPackageName(modelElement);
        String elementName = toJavaClassName(modelElement);
        return ("".equals(packageName)) ? elementName : packageName + '.' + elementName;
    }

    /**
     * Converts the argument ModelElement's name to a fully qualified name suitable for a web file.
     * <p>
     * Any non-word characters (including whitespace) will be removed.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified
     * @return the argument's name converted into a fully qualified suitable web file name,
     *    there will be no extension added
     */
    public String toFullyQualifiedWebFileName(ModelElement modelElement)
    {
        String packageName = getPackagePath(modelElement);
        String elementName = toWebFileName(modelElement);
        return ("".equals(packageName)) ? elementName : packageName + '/' + elementName;
    }

    /**
     * Converts the argument ModelElement's name to a name suitable as a StrutsForward name.
     * <p>
     * Any non-word characters (including whitespace) will be removed (each sequence will be replaced
     * by a single hyphen '-').
     * <p>
     * The returned name contains no uppercase characters.
     * <p>
     * If the argument would be an anonymous
     * <code>org.omg.uml.behavioralelements.statemachines.Transition</code> instance its resulting ActionState or
     * FinalState target will be considered instead (meaning Joins etc. will be further traversed).
     *
     * @param modelElement the modelElement which name to convert (the argument will not be modified in any way)
     * @return the argument's name converted into a suitable StrutsForward name, there will be no extension added
     */
    public String toForwardName(ModelElement modelElement)
    {
        if (modelElement instanceof Transition)
        {
            return toForwardName(getLastTransitionTarget(((Transition) modelElement).getTarget()));
        }
        else
        {
            return toLowercaseHyphenSeparatedName(modelElement.getName());
        }
    }

    /**
     * Converts the argument ModelElement's name to a name suitable as a final class member name.
     * <p>
     * Any non-word characters (including whitespace) will be removed (each sequence will be replaced
     * by a single hyphen '-').
     * <p>
     * The returned name contains no uppercase characters.
     *
     * @param modelElement the modelElement which name to convert, the argument will not be modified
     * @return the argument's name converted into a suitable final class member name, there will be no extension added
     */
    public String toFinalMemberName(ModelElement modelElement)
    {
        return toUppercaseUnderscoreSeparatedName(modelElement.getName());
    }

    /**
     * Converts the argument to uppercase, removes all non-word characters, and replaces each of those
     * sequences by an underscore '_'.
     */
    private String toUppercaseUnderscoreSeparatedName(String name)
    {
        if (name == null)
        {
            return "";
        }

        final char separator = '_';
        String[] parts = splitAtNonWordCharacters(name.toUpperCase());
        StringBuffer conversionBuffer = new StringBuffer();

        for (int i = 0; i < parts.length - 1; i++)
        {
            conversionBuffer.append(parts[i]).append(separator);
        }
        conversionBuffer.append(parts[parts.length - 1]);

        return conversionBuffer.toString();
    }

    /**
     * Returns the first outgoing transition.
     */
    public Transition getNextStateTransition(StateVertex stateVertex)
    {
        return (Transition) stateVertex.getOutgoing().iterator().next();
    }

    /**
     * Basically the same as
     * <code>getNextStateTransition(StateVertex stateVertex)</code>, but ignores
     * any Pseudostates of kind 'join'.
     * <p>
     * This way you can more easily get the actual Pseudostate.Choice, FinalState
     * or ActionState.
     * <p>
     * This is actually a workaround for the fact that there is no support for recursion
     * in VTL: any number of joins may be connected like this.
     *
     * @param stateVertex A state with only one outgoing transition,
     * @return the last state reached starting from the argument state
     */
    public StateVertex getLastTransitionTarget(StateVertex stateVertex)
    {
        if (dynamicHelper.isPseudostate(stateVertex) || dynamicHelper.isObjectFlowState(stateVertex))
        {
            return getLastTransitionTarget(getNextStateTransition(stateVertex).getTarget());
        }
        else
        {
            return stateVertex;
        }
    }

    /**
     * Basically the same as <code>getLastTransitionTarget()</code>, but this method will only stop when it meets
     * either an action state or a final state.
     *
     * @param stateVertex A state with only one outgoing transition,
     * @return the last action state or final state reached starting from the argument state
     * @see #getLastTransitionTarget(StateVertex stateVertex)
     */
    public StateVertex getLastTransitionState(StateVertex stateVertex)
    {
        StateVertex end = getLastTransitionTarget(stateVertex);

        if (dynamicHelper.isActionState(end) || dynamicHelper.isFinalState(end))
        {
            return end;
        }
        else
        {
            return getLastTransitionState(end);
        }
    }

    /**
     * Looks for a class in the UML model that corresponds to the
     * argument qualified class name.
     * <p>
     * Returns <code>null</code> if no such class would exist.
     *
     * @param qualifiedName a fully qualified class name such as
     *    <code>org.andromda.test.MyTest</code>, may not be <code>null</code>
     * @return the UML class corresponding with the argument class name,
     *    or <code>null</code> if there is none
     */
    public UmlClass findClassByName(String qualifiedName)
    {
        Collection umlClasses = getUMLModel().getCore().getUmlClass().refAllOfType();
        for (Iterator iterator = umlClasses.iterator(); iterator.hasNext();)
        {
            UmlClass umlClass = (UmlClass) iterator.next();
            String elementName = getFullyQualifiedName(umlClass);
            if (qualifiedName.equals(elementName))
            {
                return umlClass;
            }
        }
        return null;
    }

    /**
     * Looks for a use case in the UML model that corresponds to the
     * argument qualified use case name.
     * <p>
     * Returns <code>null</code> if no such use case would exist.
     *
     * @param qualifiedName a fully qualified use case name such as
     *    <code>org.andromda.test.TestUseCase</code>, may not be <code>null</code>
     * @return the UML class corresponding with the argument class name,
     *    or <code>null</code> if there is none
     */
    public UseCase findUseCaseByName(String qualifiedName)
    {
        Collection useCases = getUMLModel().getUseCases().getUseCase().refAllOfType();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            UseCase useCase = (UseCase) iterator.next();
            String elementName = getFullyQualifiedName(useCase);
            if (qualifiedName.equals(elementName))
            {
                return useCase;
            }
        }
        return null;
    }

    /**
     * Returns the name of a model element fully qualified by the
     * name of the package that contains it. If the model element
     * is a primitive type it will return the primitive type itself.
     *
     *@param object model element
     *@return fully qualifed name
     */
    public String getFullyQualifiedName(Object object)
    {
        if (!(object instanceof ModelElement))
        {
            return null;
        }

        ModelElement modelElement = (ModelElement) object;

        String fullName = modelElement.getName();

        if (StringUtilsHelper.isPrimitiveType(fullName))
        {
            return fullName;
        }

        String packageName = getPackageName(modelElement);
        fullName =
            "".equals(packageName) ? fullName : packageName + "." + fullName;

        return fullName;
    }

    /**
     * Delegates to an MDRepositoryFacade.
     *
     * @see org.andromda.core.mdr.MDRepositoryFacade#open
     */
    public void open()
    {
        repository.open();
    }

    /**
     * Delegates to an MDRepositoryFacade.
     *
     * @see org.andromda.core.mdr.MDRepositoryFacade#close
     */
    public void close()
    {
        repository.close();
    }

    /**
     * Delegates to an MDRepositoryFacade.
     *
     * @see org.andromda.core.mdr.MDRepositoryFacade#readModel
     */
    public void readModel(URL modelURL) throws RepositoryReadException, IOException
    {
        repository.readModel(modelURL);
    }

    /**
     * Delegates to an MDRepositoryFacade.
     *
     * @see org.andromda.core.mdr.MDRepositoryFacade#getLastModified
     */
    public long getLastModified()
    {
        return repository.getLastModified();
    }

    /**
     * Delegates to UMLStaticHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper
     */
    public void setModel(Object model)
    {
        staticHelper.setModel(model);
    }

    /**
     * Delegates to UMLStaticHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper
     */
    public Object getModel()
    {
        return staticHelper.getModel();
    }

    /**
     * Delegates to UMLStaticHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper
     */
    public void setTypeMappings(DbMappingTable mappings)
    {
        staticHelper.setTypeMappings(mappings);
    }

    /**
     * Delegates to UMLStaticHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        return staticHelper.getStereotypeNames(modelElement);
    }
}





















