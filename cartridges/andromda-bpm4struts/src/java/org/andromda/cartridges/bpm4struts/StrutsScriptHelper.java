package org.andromda.cartridges.bpm4struts;

import org.andromda.core.common.CollectionFilter;
import org.andromda.core.common.DbMappingTable;
import org.andromda.core.common.HTMLAnalyzer;
import org.andromda.core.common.RepositoryFacade;
import org.andromda.core.common.RepositoryReadException;
import org.andromda.core.common.ScriptHelper;
import org.andromda.core.common.StringUtilsHelper;
import org.andromda.core.mdr.MDRepositoryFacade;
import org.andromda.core.uml14.DirectionalAssociationEnd;
import org.andromda.core.uml14.UMLDynamicHelper;
import org.andromda.core.uml14.UMLStaticHelper;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.ClassifierInState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.behavioralelements.usecases.UseCase;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.Model;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
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
    public final static String ASPECT_FRONT_END_VIEW = "FrontEndView";
    public final static String ASPECT_FRONT_END_USE_CASE = "FrontEndUseCase";
    public final static String ASPECT_FRONT_END_WORKFLOW = "FrontEndWorkflow";
    public final static String ASPECT_FRONT_END_PRESENTATION = "FrontEndPresentation";
    public final static String ASPECT_CONTROLLER_CLASS = "ControllerClass";

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
     * This map serves as a cache for quickly finding classes in the UML model.
     * This is to avoid iterating the list of classes for each lookup.
     * <p>
     * This is a performance improvement since in most UML model you will have
     * at least 20 classes. Iterating over them can quickly lead to O(n²) performance loss.
     * <p>
     * The keys are fully qualified class names, while the values are the corresponding
     * <code>org.omg.uml.foundation.core.UmlClass</code> instances.
     * <p>
     * CAUTION! this map needs to be invalidated and rebuilt whenever the model changes,
     * the contract is that when the value is <code>null</code> it needs to be rebuilt,
     * any other value denotes a map built from the current model.
     */
    private HashMap qualifiedNameToClassMap = null;

    /**
     * This map serves as a cache for quickly finding use-cases in the UML model.
     * This is to avoid iterating the list of use-cases for each lookup.
     * <p>
     * CAUTION! this map needs to be invalidated and rebuilt whenever the model changes,
     * the contract is that when the value is <code>null</code> it needs to be rebuilt,
     * any other value denotes a map built from the current model.
     *
     * @see #qualifiedNameToClassMap
     */
    private HashMap qualifiedNameToUseCaseMap = null;

    /**
     * Returns the static UML helper delegate.
     *
     * @return The UML helper for the static part of the UML model.
     */
    public UMLStaticHelper getStaticHelper()
    {
        return staticHelper;
    }

    /**
     * Returns the dynamic UML helper delegate.
     *
     * @return The UML helper for the dynamic part of the UML model.
     */
    public UMLDynamicHelper getDynamicHelper()
    {
        return dynamicHelper;
    }

    /**
     * Returns the model element in the model that has been marked as the controller
     * class for the argument use case.
     * <p>
     * You can mark such class by adding a tagged value, such as
     * <code>ControllerClass=org.project.web.actions.MyController</code>.
     *
     * @param useCase the graph which controller model element to return
     * @return the UmlClass that is the controller or <code>null</code> if the
     *    tagged value is not present
     */
    public UmlClass getControllerClass(UseCase useCase)
    {
        String tagValue = staticHelper.findTagValue(useCase, ASPECT_CONTROLLER_CLASS);
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
     * @return the UmlClass that is the form, or <code>null</code> if there
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
                if (staticHelper.getStereotypeNames(participant).contains(ASPECT_FRONT_END_PRESENTATION))
                {
                    return (UmlClass) participant;
                }
            }
        }

        return null;
    }

    /**
     * Returns the use-case that holds the argument activity graph.
     *
     * @param activityGraph an activity graph, may not be <code>null</code>
     * @return the use-case of which this activity graph is a part,
     *  or <code>null</code> in case there is none
     */
    public UseCase getUseCaseContext(ActivityGraph activityGraph)
    {
        ModelElement modelElement = activityGraph.getContext();
        if (modelElement instanceof UseCase)
        {
            return (UseCase) modelElement;
        }
        else
        {
            return null;
        }
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

    /**
     * Gets the set of types for the ObjectFlowStates in the argument
     * activity graph.
     *
     * @param activityGraph an activity graph
     * @return a set of object flow state types (no doubles)
     */
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

    /**
     * Returns the elements in the argument collection as a set.
     *
     * @param elements the collection containing the elements
     * @return a subset of the argument, filtered from any doubles
     */
    public Set toSet(Collection elements)
    {
        return new LinkedHashSet(elements);
    }

    /**
     * Merges both collection into a set, ignoring any duplicate entries.
     *
     * @param firstElements the first collection
     * @param secondElements the second collection
     * @return both collections, filtered from any doubles
     */
    public Set toSet(Collection firstElements, Collection secondElements)
    {
        Set set = new LinkedHashSet(firstElements);
        set.addAll(secondElements);
        return set;
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
        Collection modelElements = staticHelper.getModelElements();
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
     * @param object A model element, should be an instance of
     *  <code>org.omg.uml.foundation.core.ModelElement</code>
     * @return The package name of the model element, as a Java fully qualified name, never <code>null>/code>
     */
    public String getPackageName(Object object)
    {
        // only model elements, different from the root UML model may continue
        if ((!(object instanceof ModelElement)) || (object instanceof Model))
        {
            return "";
        }

        // states never belong to any package directly
        if (dynamicHelper.isStateVertex(object))
        {
            return getPackageName(dynamicHelper.getStateMachineContext((StateVertex) object));
        }

        // state machines pass on their context
        if (dynamicHelper.isStateMachine(object))
        {
            return getPackageName( ((StateMachine)object).getContext() );
        }

        // only packages will be considered
        if (staticHelper.isPackage(object))
        {
            org.omg.uml.modelmanagement.UmlPackage umlPackage = (org.omg.uml.modelmanagement.UmlPackage)object;
            String parentPackageName = getPackageName(umlPackage.getNamespace());

            if ( (parentPackageName != null) && (!"".equals(parentPackageName)) )
            {
                parentPackageName = parentPackageName + '.';
            }

            return (parentPackageName + umlPackage.getName()).toLowerCase();
        }

        return getPackageName( ((ModelElement)object).getNamespace() );
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
     * Returns the collection of Pseudostate instances of kind 'initial' found in the argument use-cases.
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
            activityGraphs.addAll(dynamicHelper.getStateMachines(useCase));
        }

        for (Iterator iterator = activityGraphs.iterator(); iterator.hasNext();)
        {
            ActivityGraph activityGraph = (ActivityGraph) iterator.next();
            initialStates.addAll(dynamicHelper.getInitialStates(activityGraph));
        }

        return initialStates;
    }

    /**
     * Returns the initial state in the given StateMachine.
     * <p>
     * UML allows more than one initial state, but here we allow only one since it represents the
     * entry-point for the context use-case.
     * <p>
     * The returned Pseudostate is always of kind 'choice', in case there is no initial state for the given
     * graph this method will return <code>null</code>.
     * <p>
     * Should there be more than one initial state in the argument StateMachine then
     * this method will return the first one it finds.
     *
     * @param stateMachine a StateMachine, may not be <code>null</code>
     * @return a Pseudostate of kind 'choice', or <code>null</code>
     * @see org.andromda.core.uml14.UMLDynamicHelper#getInitialStates(StateMachine stateMachine)
     */
    public Pseudostate getInitialState(StateMachine stateMachine)
    {
        Iterator iterator = dynamicHelper.getInitialStates(stateMachine).iterator();
        if (iterator.hasNext())
        {
            return (Pseudostate)iterator.next();
        }
        else
        {
            return null;
        }
    }

    /**
     * Given a state machine, this method will return the first state encountered when traversing starting
     * from the initial state and following the transitions.
     * <p>
     * If no such state is found this method returns <code>null</code>.
     * <p>
     * Please note that an initial state is in fact a Pseudostate and, although the name might be
     * a little confusing, this is not a State but a StateVertex.
     *
     * @param stateMachine a state machine, may not be <code>null</code>
     * @return the first state in the state machine, or <code>null</code> if there is none
     */
    public State getFirstState(StateMachine stateMachine)
    {
        return dynamicHelper.getStateTarget(dynamicHelper.getNextStateTransition(getInitialState(stateMachine)));
    }

    /**
     * Returns the set of guard names that are included in the argument state machine.
     *
     * @param stateMachine a state machine, may not be <code>null</code>
     * @return a set of String instances that represent the names of the guards in the argument state machine,
     *  never <code>null</code>.
     */
    public Collection getGuardNames(StateMachine stateMachine)
    {
        final Collection guardNames = new LinkedHashSet();

        Collection guardedTransitions = dynamicHelper.getGuardedTransitions(stateMachine);
        for (Iterator iterator = guardedTransitions.iterator(); iterator.hasNext();)
        {
            Transition transition = (Transition) iterator.next();
            guardNames.add(transition.getGuard().getName());
        }

        return guardNames;
    }

    /**
     * Returns a Collection containing all Transition instances that are going
     * out of a decision point.
     * <p>
     * Each element in the collection is an instance of
     * <code>org.omg.uml.behavioralelements.statemachines.Transition</code>
     *
     * @param stateMachine a StateMachine instance, may not be <code>null</code>
     * @return the collection of the Transition instances going out of 'choice'
     *    Pseudostates found in the argument state machine.
     * @see org.andromda.core.uml14.UMLDynamicHelper#isDecisionPoint(Object object)
     */
    public Collection getDecisionTransitions(StateMachine stateMachine)
    {
        final Set transitions = new HashSet();

        Collection decisions = dynamicHelper.getDecisionPoints(stateMachine);
        for (Iterator iterator = decisions.iterator(); iterator.hasNext();)
        {
            Pseudostate decision = (Pseudostate) iterator.next();
            transitions.addAll(dynamicHelper.getNextTriggeredTransitions(decision));
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
     * @see #getDecisionTransitions(StateMachine stateMachine)
     */
    public Collection getUseCaseDecisionTransitions(UseCase useCase)
    {
        final Collection decisionTransitions = new LinkedList();
        Collection stateMachines = dynamicHelper.getStateMachines(useCase);
        for (Iterator iterator = stateMachines.iterator(); iterator.hasNext();)
        {
            StateMachine stateMachine = (StateMachine) iterator.next();
            decisionTransitions.addAll(getDecisionTransitions(stateMachine));
        }
        return decisionTransitions;
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
            Transition transition = (Transition)modelElement;
            StateVertex target = transition.getTarget();

            if (dynamicHelper.isState(target))
            {
                return toForwardName(target);
            }
            else
            {
                return toForwardName(dynamicHelper.getNextStateTransition((Pseudostate)target));
            }
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
     * Looks for a class in the UML model that corresponds to the
     * argument qualified class name.
     * <p>
     * Returns <code>null</code> if no such class would exist.
     *
     * @param qualifiedName a fully qualified class name such as
     *    <code>org.andromda.test.MyTestClass</code>, may not be <code>null</code>
     * @return the UML class corresponding with the argument class name,
     *    or <code>null</code> if there is none
     */
    public UmlClass findClassByName(String qualifiedName)
    {
        // build cache if necessary
        if (qualifiedNameToClassMap == null)
        {
            qualifiedNameToClassMap = new HashMap();
            Collection umlClasses = staticHelper.getAllClasses();
            for (Iterator iterator = umlClasses.iterator(); iterator.hasNext();)
            {
                UmlClass umlClass = (UmlClass) iterator.next();
                String qualifiedElementName = getFullyQualifiedName(umlClass);
                qualifiedNameToClassMap.put(qualifiedElementName, umlClass);
            }
        }

        return (UmlClass)qualifiedNameToClassMap.get(qualifiedName);
    }

    /**
     * Looks for a use-case in the UML model that corresponds to the
     * argument qualified use-case name.
     * <p>
     * Returns <code>null</code> if no such use-case would exist.
     *
     * @param qualifiedName a fully qualified use-case name such as
     *    <code>org.andromda.test.MyTestUseCase</code>, may not be <code>null</code>
     * @return the UML UseCase corresponding with the argument use-case name,
     *    or <code>null</code> if there is none
     */
    public UseCase findUseCaseByName(String qualifiedName)
    {
        if (qualifiedNameToUseCaseMap == null)
        {
            qualifiedNameToUseCaseMap = new HashMap();
            Collection useCases = dynamicHelper.getAllUseCases();
            for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
            {
                UseCase useCase = (UseCase) iterator.next();
                String qualifiedElementName = getFullyQualifiedName(useCase);
                qualifiedNameToUseCaseMap.put(qualifiedElementName, useCase);
            }
        }

        return (UseCase)qualifiedNameToUseCaseMap.get(qualifiedName);
    }

    /**
     * Returns the name of a model element fully qualified by the
     * name of the package that contains it. If the model element
     * is a primitive type it will return the primitive type itself.
     *
     *@param object model element, may not be <code>null</code>
     *@return fully qualifed name, never <code>null</code>
     */
    public String getFullyQualifiedName(Object object)
    {
        if (!(object instanceof ModelElement))
        {
            return "";
        }

        ModelElement modelElement = (ModelElement) object;

        String fullName = modelElement.getName();

        if (StringUtilsHelper.isPrimitiveType(fullName))
        {
            return fullName;
        }

        String packageName = getPackageName(modelElement);
        fullName = "".equals(packageName) ? fullName : packageName + "." + fullName;
        return fullName;
    }

    /**
     * Delegates to an MDRepositoryFacade.
     *
     * @see org.andromda.core.mdr.MDRepositoryFacade#open()
     */
    public void open()
    {
        repository.open();
    }

    /**
     * Delegates to an MDRepositoryFacade.
     *
     * @see org.andromda.core.mdr.MDRepositoryFacade#close()
     */
    public void close()
    {
        repository.close();
    }

    /**
     * Delegates to an MDRepositoryFacade.
     *
     * @see org.andromda.core.mdr.MDRepositoryFacade#readModel(URL modelURL)
     */
    public void readModel(URL modelURL) throws RepositoryReadException, IOException
    {
        repository.readModel(modelURL);
    }

    /**
     * Delegates to an MDRepositoryFacade.
     *
     * @see org.andromda.core.mdr.MDRepositoryFacade#getLastModified()
     */
    public long getLastModified()
    {
        return repository.getLastModified();
    }

    /**
     * Delegates to UMLStaticHelper and UMLDynamicHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper#setModel(Object model)
     * @see org.andromda.core.uml14.UMLDynamicHelper#setModel(Object model)
     */
    public void setModel(Object model)
    {
        qualifiedNameToClassMap = null; // invalidate the cache
        staticHelper.setModel(model);
        dynamicHelper.setModel(model);
    }

    /**
     * Delegates to UMLStaticHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper#getModel()
     */
    public Object getModel()
    {
        return staticHelper.getModel();
    }

    /**
     * Delegates to UMLStaticHelper and UMLDynamicHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper#setTypeMappings(DbMappingTable mappings)
     * @see org.andromda.core.uml14.UMLDynamicHelper#setTypeMappings(DbMappingTable mappings)
     */
    public void setTypeMappings(DbMappingTable mappings)
    {
        staticHelper.setTypeMappings(mappings);
        dynamicHelper.setTypeMappings(mappings);
    }

    /**
     * Delegates to UMLStaticHelper.
     *
     * @see org.andromda.core.uml14.UMLStaticHelper#getStereotypeNames(Object modelElement)
     */
    public Collection getStereotypeNames(Object modelElement)
    {
        return staticHelper.getStereotypeNames(modelElement);
    }

    /**
     * Filters the argument non-null collection from all objects that are no ModelElement instances that do not have
     * a stereotype with the argument stereotype name.
     *
     * @param collection A collection of objects that will be filtered, any Object may be found inside, even
     *  <code>null</code>.
     * @param stereotypeName The name of the stereotype on which to filter the collection
     * @return A Collection of that is a subset of the argument collection, all elements are guarantueed
     *  to be ModelElement instances that have at least one Stereotype with the specified name.
     */
    public Collection filterWithStereotypeName(Collection collection, final String stereotypeName)
    {
        final CollectionFilter stereotypeFilter =
            new CollectionFilter()
            {
                public boolean accept(Object object)
                {
                    return hasStereotypeWithName(object, stereotypeName);
                }
            };
        return dynamicHelper.filter(collection, stereotypeFilter);
    }

    /**
     * Returns <code>true</code> if and only if the argument object is a ModelElement instance with at least
     * one stereotype with the specified name associated to it.
     *
     * @param object the object to search for stereotypes
     * @param stereotypeName the stereotype to look for, may not be <code>null</code>
     * @return <code>true</code> if the argument object is a ModelElement with the specified stereotype,
     *  <code>false</code> in any other case.
     */
    public boolean hasStereotypeWithName(Object object, String stereotypeName)
    {
        if ((object instanceof ModelElement) && (stereotypeName != null))
        {
            ModelElement modelElement = (ModelElement) object;
            Collection stereotypes = modelElement.getStereotype();

            for (Iterator iterator = stereotypes.iterator(); iterator.hasNext();)
            {
                Stereotype stereotype = (Stereotype) iterator.next();
                if (stereotypeName.equals(stereotype.getName()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the state machine associated with the argument use-case. If there is more than one state machine
     * this method will return the first one it finds.
     * <p>
     * If the use-case has no state machines associated to it this method will return <code>null<code>.
     *
     * @param useCase a use-case
     * @return the state machine associated to the use-case, or <code>null</code> if none is found
     */
    public StateMachine getStateMachine(UseCase useCase)
    {
        Iterator iterator = dynamicHelper.getStateMachines(useCase).iterator();
        if (iterator.hasNext())
        {
            return (StateMachine)iterator.next();
        }
        else
        {
            return null;
        }
    }

    /**
     * Given the name of a use-case in the UML model, this method will try to find that use-case and return
     * the fully qualified name (this means the package will be prefixed to the argument name).
     * <p>
     * In case the use-case cannot be found, this method will return <code>null</code>
     * <p>
     * Alternatively you may pass an already fully qualified use-case name, if it corresponds to a
     * use-case in the UML model, than the argument itself will be returned, otherwise <code>null</code>
     *
     * @param useCaseName the name of a use-case in the UML model, may not be <code>null</code>
     * @return the fully qualified name of the use-case identified with the argument name, or <code>null</code> in
     *  if there is no such use-case
     */
    public String findFullyQualifiedUseCaseName(String useCaseName)
    {
        Collection useCases = dynamicHelper.getAllUseCases();
        for (Iterator iterator = useCases.iterator(); iterator.hasNext();)
        {
            UseCase useCase = (UseCase) iterator.next();
            String fullyQualifiedName = getFullyQualifiedName(useCase);

            if ( (useCaseName.equalsIgnoreCase(useCase.getName())) || (useCaseName.equalsIgnoreCase(fullyQualifiedName)) )
            {
                return fullyQualifiedName;
            }
        }
        return null;
    }

    /**
     * Recursively traverses through each workflow to eventually find a use-case, this one will be returned.
     * <p>
     * A workflow is a <code>UseCase</code> with the <code>ASPECT_FRONT_END_WORKFLOW</code> stereotype, a use-case is a
     * <code>UseCase</code> with the <code>ASPECT_FRONT_END_USE_CASE</code> stereotype.
     * <p>
     * For each workflow the first action state will be interpreted as the name of an existing use-case (the name
     * may be either fully qualified or not) or another workflow.
     * <p>
     * This method returns <code>null</code> in case one of the UseCase instances would be neither workflow neither
     * use-case.
     *
     * @param useCase a workflow or use-case, may not be <code>null</code>
     * @return The first use-case found (depth first), or <code>null</code>
     */
    public UseCase getFirstFrontEndUseCase(UseCase useCase)
    {
        Collection stereotypeNames = staticHelper.getStereotypeNames(useCase);

        if (stereotypeNames.contains(ASPECT_FRONT_END_USE_CASE))
        {
            return useCase;
        }
        else if (stereotypeNames.contains(ASPECT_FRONT_END_WORKFLOW))
        {
            String firstUseCaseName = getFirstState(getStateMachine(useCase)).getName();
            UseCase nextUseCase = findUseCaseByName(findFullyQualifiedUseCaseName(firstUseCaseName));
            return getFirstFrontEndUseCase(nextUseCase);
        }
        else
        {
            return null;
        }
    }

    /**
     * This method will lookup the given use-case's workflow, namely the state machine in which it is being
     * denoted as a state. Having this workflow it is easy to determine what the next use-case will be
     * after this one has finished.
     * <p>
     * It is possible that a use-case has more than one outgoing transition, each of those transitions are considered
     * to be uniquely defined by their triggers.
     * <p>
     * You may choose to not specifiy any trigger name here, in that case
     * this method will follow the first outgoing transition, disregarding any
     * triggers.
     * <p>
     * The name comparison is case-insensitive.
     *
     * @param useCase the source use-case to start from, may not be <code>null</code>
     * @param triggerName the next trigger to follow
     * @return the next use-case, as defined in the parent workflow, or <code>null</code> in case
     *  <ul>
     *    <li>the source use-case has no name
     *    <li>the target is not a valid use-case state
     *  </ul>
     * @see #getTriggerTarget(State state, String triggerName)
     */
    public UseCase findNextUseCaseInWorkflow(UseCase useCase, String triggerName)
    {
        final String useCaseName = useCase.getName();

        if (useCaseName != null)
        {
            Collection states = dynamicHelper.getAllStates();
            for (Iterator iterator = states.iterator(); iterator.hasNext();)
            {
                State state = (State) iterator.next();
                if (useCaseName.equalsIgnoreCase(state.getName()))
                {
                    // this means we found the state for this use-case
                    // now lookup the next state's use-case and return it
                    StateVertex stateVertex = getTriggerTarget(state, triggerName);
                    if (dynamicHelper.isState(stateVertex) && !dynamicHelper.isFinalState(stateVertex))
                    {
                        String qualifiedName = findFullyQualifiedUseCaseName(stateVertex.getName());
                        return findUseCaseByName(qualifiedName);
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns the target of the outgoing transition with a trigger with the argument name.
     * This method will perform all the necessary navigation through complex transition constructions.
     * <p>
     * If there is only an anonymous outgoing transition this one will be followed, if there are triggered transitions
     * (one or more) the desired one is followed.
     *
     * @param state the source of the outgoing transitions, may not be <code>null</code>
     * @param triggerName the trigger to follow
     * @return the next state vertex after following the trigger from the source vertex, is <code>null</code> if
     *  <ul>
     *    <li>there are no outgoing transitions from the argument state vertex
     *    <li>the argument trigger name is not <code>null</code> but is not found in any of the
     *        outgoing transitions
     *  </ul>
     * @see org.andromda.core.uml14.UMLDynamicHelper#getNextTriggeredTransitions(State state)
     */
    private StateVertex getTriggerTarget(State state, String triggerName)
    {
        Collection transitions = dynamicHelper.getNextTriggeredTransitions(state);
        StateVertex target = null;

        int transitionCount = transitions.size();

        switch (transitionCount)
        {
            case 0 :
                target = dynamicHelper.getStateTarget( dynamicHelper.getNextStateTransition(state) );
                break;
            case 1 :
                target = dynamicHelper.getStateTarget((Transition)transitions.iterator().next());
                break;
            default :
                for (Iterator iterator = transitions.iterator(); iterator.hasNext();)
                {
                    Transition transition = (Transition) iterator.next();
                    if (triggerName.equalsIgnoreCase(getTriggerName(transition)))
                    {
                        target = dynamicHelper.getStateTarget(transition);
                        break;  // from the for-loop
                    }
                }
        }

        return target;
    }

    /**
     * Returns the name of the trigger for the argument transition.
     *
     * @param transition a transition, may not be <code>null</code>
     * @return the name of the transition's trigger, is <code>null</code> if there is no trigger, or the trigger
     *  is anonymous.
     */
    public String getTriggerName(Transition transition)
    {
        Event trigger = transition.getTrigger();
        return (trigger == null) ? null : trigger.getName();
    }

}





















