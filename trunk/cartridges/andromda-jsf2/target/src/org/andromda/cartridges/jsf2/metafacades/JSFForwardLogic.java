// license-header java merge-point
//
// Attention: generated code (by MetafacadeLogic.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.metafacades.uml.ActionFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndControllerOperation;
import org.andromda.metafacades.uml.FrontEndEvent;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.GuardFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.translation.ocl.validation.OCLCollections;
import org.andromda.translation.ocl.validation.OCLIntrospector;
import org.andromda.translation.ocl.validation.OCLResultEnsurer;
import org.apache.log4j.Logger;

/**
 * Represents a forward within a JSF activity graph.
 * MetafacadeLogic for JSFForward
 *
 * @see JSFForward
 */
public abstract class JSFForwardLogic
    extends MetafacadeBase
    implements JSFForward
{
    /**
     * The underlying UML object
     * @see Object
     */
    protected Object metaObject;

    /** Create Metafacade implementation instance using the MetafacadeFactory from the context
     * @param metaObjectIn
     * @param context
     */
    protected JSFForwardLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superFrontEndForward =
           (FrontEndForward)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.FrontEndForward",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(JSFForwardLogic.class);

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFForward if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFForward";
        }
        return context;
    }

    private FrontEndForward superFrontEndForward;
    private boolean superFrontEndForwardInitialized = false;

    /**
     * Gets the FrontEndForward parent instance.
     * @return this.superFrontEndForward FrontEndForward
     */
    private FrontEndForward getSuperFrontEndForward()
    {
        if (!this.superFrontEndForwardInitialized)
        {
            ((MetafacadeBase)this.superFrontEndForward).setMetafacadeContext(this.getMetafacadeContext());
            this.superFrontEndForwardInitialized = true;
        }
        return this.superFrontEndForward;
    }

    /** Reset context only for non-root metafacades
     * @param context
     * @see MetafacadeBase#resetMetafacadeContext(String context)
     */
    @Override
    public void resetMetafacadeContext(String context)
    {
        if (!this.contextRoot) // reset context only for non-root metafacades
        {
            context = getContext(context);  // to have same value as in original constructor call
            setMetafacadeContext (context);
            if (this.superFrontEndForwardInitialized)
            {
                ((MetafacadeBase)this.superFrontEndForward).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFForward
     */
    public boolean isJSFForwardMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFForward#getPath()
    * @return String
    */
    protected abstract String handleGetPath();

    private String __path1a;
    private boolean __path1aSet = false;

    /**
     * The path to which this forward points.
     * @return (String)handleGetPath()
     */
    public final String getPath()
    {
        String path1a = this.__path1a;
        if (!this.__path1aSet)
        {
            // path has no pre constraints
            path1a = handleGetPath();
            // path has no post constraints
            this.__path1a = path1a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__path1aSet = true;
            }
        }
        return path1a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFForward#isFinalStateTarget()
    * @return boolean
    */
    protected abstract boolean handleIsFinalStateTarget();

    private boolean __finalStateTarget2a;
    private boolean __finalStateTarget2aSet = false;

    /**
     * Indicates whether or not a final state is the target of this forward.
     * @return (boolean)handleIsFinalStateTarget()
     */
    public final boolean isFinalStateTarget()
    {
        boolean finalStateTarget2a = this.__finalStateTarget2a;
        if (!this.__finalStateTarget2aSet)
        {
            // finalStateTarget has no pre constraints
            finalStateTarget2a = handleIsFinalStateTarget();
            // finalStateTarget has no post constraints
            this.__finalStateTarget2a = finalStateTarget2a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__finalStateTarget2aSet = true;
            }
        }
        return finalStateTarget2a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFForward#getFromOutcome()
    * @return String
    */
    protected abstract String handleGetFromOutcome();

    private String __fromOutcome3a;
    private boolean __fromOutcome3aSet = false;

    /**
     * The name that corresponds to the from-outcome in an navigational rule.
     * @return (String)handleGetFromOutcome()
     */
    public final String getFromOutcome()
    {
        String fromOutcome3a = this.__fromOutcome3a;
        if (!this.__fromOutcome3aSet)
        {
            // fromOutcome has no pre constraints
            fromOutcome3a = handleGetFromOutcome();
            // fromOutcome has no post constraints
            this.__fromOutcome3a = fromOutcome3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__fromOutcome3aSet = true;
            }
        }
        return fromOutcome3a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFForward#getSuccessMessages()
    * @return Map
    */
    protected abstract Map handleGetSuccessMessages();

    private Map __successMessages4a;
    private boolean __successMessages4aSet = false;

    /**
     * Messages used to indicate successful execution.
     * @return (Map)handleGetSuccessMessages()
     */
    public final Map getSuccessMessages()
    {
        Map successMessages4a = this.__successMessages4a;
        if (!this.__successMessages4aSet)
        {
            // successMessages has no pre constraints
            successMessages4a = handleGetSuccessMessages();
            // successMessages has no post constraints
            this.__successMessages4a = successMessages4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__successMessages4aSet = true;
            }
        }
        return successMessages4a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFForward#isSuccessMessagesPresent()
    * @return boolean
    */
    protected abstract boolean handleIsSuccessMessagesPresent();

    private boolean __successMessagesPresent5a;
    private boolean __successMessagesPresent5aSet = false;

    /**
     * Indicates whether or not any success messags are present.
     * @return (boolean)handleIsSuccessMessagesPresent()
     */
    public final boolean isSuccessMessagesPresent()
    {
        boolean successMessagesPresent5a = this.__successMessagesPresent5a;
        if (!this.__successMessagesPresent5aSet)
        {
            // successMessagesPresent has no pre constraints
            successMessagesPresent5a = handleIsSuccessMessagesPresent();
            // successMessagesPresent has no post constraints
            this.__successMessagesPresent5a = successMessagesPresent5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__successMessagesPresent5aSet = true;
            }
        }
        return successMessagesPresent5a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFForward#getWarningMessages()
    * @return Map
    */
    protected abstract Map handleGetWarningMessages();

    private Map __warningMessages6a;
    private boolean __warningMessages6aSet = false;

    /**
     * Any messages used to indicate a warning.
     * @return (Map)handleGetWarningMessages()
     */
    public final Map getWarningMessages()
    {
        Map warningMessages6a = this.__warningMessages6a;
        if (!this.__warningMessages6aSet)
        {
            // warningMessages has no pre constraints
            warningMessages6a = handleGetWarningMessages();
            // warningMessages has no post constraints
            this.__warningMessages6a = warningMessages6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__warningMessages6aSet = true;
            }
        }
        return warningMessages6a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFForward#isWarningMessagesPresent()
    * @return boolean
    */
    protected abstract boolean handleIsWarningMessagesPresent();

    private boolean __warningMessagesPresent7a;
    private boolean __warningMessagesPresent7aSet = false;

    /**
     * Whether or not any warning messages are present.
     * @return (boolean)handleIsWarningMessagesPresent()
     */
    public final boolean isWarningMessagesPresent()
    {
        boolean warningMessagesPresent7a = this.__warningMessagesPresent7a;
        if (!this.__warningMessagesPresent7aSet)
        {
            // warningMessagesPresent has no pre constraints
            warningMessagesPresent7a = handleIsWarningMessagesPresent();
            // warningMessagesPresent has no post constraints
            this.__warningMessagesPresent7a = warningMessagesPresent7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__warningMessagesPresent7aSet = true;
            }
        }
        return warningMessagesPresent7a;
    }

    // ------------- associations ------------------

    /**
     * @return true
     * @see FrontEndForward
     */
    public boolean isFrontEndForwardMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.TransitionFacade
     */
    public boolean isTransitionFacadeMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see ModelElementFacade
     */
    public boolean isModelElementFacadeMetaType()
    {
        return true;
    }

    // ----------- delegates to FrontEndForward ------------
    /**
     * The method name used to delegate to this forward.
     * @see FrontEndForward#getActionMethodName()
     */
    public String getActionMethodName()
    {
        return this.getSuperFrontEndForward().getActionMethodName();
    }

    /**
     * The front-end actions directly containing this front-end forward.
     * @see FrontEndForward#getActions()
     */
    public List<FrontEndAction> getActions()
    {
        return this.getSuperFrontEndForward().getActions();
    }

    /**
     * The trigger for this front-end forward.
     * @see FrontEndForward#getDecisionTrigger()
     */
    public FrontEndEvent getDecisionTrigger()
    {
        return this.getSuperFrontEndForward().getDecisionTrigger();
    }

    /**
     * Tthe set of parameter used during transport in this forward.
     * @see FrontEndForward#getForwardParameters()
     */
    public List<FrontEndParameter> getForwardParameters()
    {
        return this.getSuperFrontEndForward().getForwardParameters();
    }

    /**
     * The activity graph which holds this forward if the graph is contained in a FrontEndUseCase.
     * @see FrontEndForward#getFrontEndActivityGraph()
     */
    public FrontEndActivityGraph getFrontEndActivityGraph()
    {
        return this.getSuperFrontEndForward().getFrontEndActivityGraph();
    }

    /**
     * The operation to which is called during execution of this front-end forward.
     * @see FrontEndForward#getOperationCall()
     */
    public FrontEndControllerOperation getOperationCall()
    {
        return this.getSuperFrontEndForward().getOperationCall();
    }

    /**
     * The use case in which this forward is contained.
     * @see FrontEndForward#getUseCase()
     */
    public FrontEndUseCase getUseCase()
    {
        return this.getSuperFrontEndForward().getUseCase();
    }

    /**
     * Indicates if this forward is contained in a FrontEndUseCase.
     * @see FrontEndForward#isContainedInFrontEndUseCase()
     */
    public boolean isContainedInFrontEndUseCase()
    {
        return this.getSuperFrontEndForward().isContainedInFrontEndUseCase();
    }

    /**
     * Indicates if this action directly targets a "front-end" view, false otherwise.
     * @see FrontEndForward#isEnteringView()
     */
    public boolean isEnteringView()
    {
        return this.getSuperFrontEndForward().isEnteringView();
    }

    /**
     * Indicates if this forward (transition) is coming out of a front-end view.
     * @see FrontEndForward#isExitingView()
     */
    public boolean isExitingView()
    {
        return this.getSuperFrontEndForward().isExitingView();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperFrontEndForward().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperFrontEndForward().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperFrontEndForward().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperFrontEndForward().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperFrontEndForward().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperFrontEndForward().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperFrontEndForward().getDocumentation(indent);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. By default paragraphs are returned as HTML.
     * This method is equivalent to <code>getDocumentation(indent, lineLength, true)</code>.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength)
     */
    public String getDocumentation(String indent, int lineLength)
    {
        return this.getSuperFrontEndForward().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperFrontEndForward().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperFrontEndForward().getFullyQualifiedName();
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element.  If modelName is true, then
     * the original name of the model element (the name contained within the model) will be the name
     * returned, otherwise a name from a language mapping will be returned.
     * @see ModelElementFacade#getFullyQualifiedName(boolean modelName)
     */
    public String getFullyQualifiedName(boolean modelName)
    {
        return this.getSuperFrontEndForward().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperFrontEndForward().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperFrontEndForward().getId();
    }

    /**
     * UML2: Retrieves the keywords for this element. Used to modify implementation properties which
     * are not represented by other properties, i.e. native, transient, volatile, synchronized,
     * (added annotations) override, deprecated. Can also be used to suppress compiler warnings:
     * (added annotations) unchecked, fallthrough, path, serial, finally, all. Annotations require
     * JDK5 compiler level.
     * @see ModelElementFacade#getKeywords()
     */
    public Collection<String> getKeywords()
    {
        return this.getSuperFrontEndForward().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperFrontEndForward().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperFrontEndForward().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperFrontEndForward().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperFrontEndForward().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperFrontEndForward().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperFrontEndForward().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperFrontEndForward().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperFrontEndForward().getPackagePath();
    }

    /**
     * UML2: Returns the value of the 'Qualified Name' attribute. A name which allows the
     * NamedElement to be identified within a hierarchy of nested Namespaces. It is constructed from
     * the names of the containing namespaces starting at the root of the hierarchy and ending with
     * the name of the NamedElement itself.
     * @see ModelElementFacade#getQualifiedName()
     */
    public String getQualifiedName()
    {
        return this.getSuperFrontEndForward().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperFrontEndForward().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperFrontEndForward().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperFrontEndForward().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperFrontEndForward().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperFrontEndForward().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperFrontEndForward().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperFrontEndForward().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperFrontEndForward().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperFrontEndForward().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperFrontEndForward().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperFrontEndForward().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperFrontEndForward().hasKeyword(keywordName);
    }

    /**
     * Returns true if the model element has the specified stereotype.  If the stereotype itself
     * does not match, then a search will be made up the stereotype inheritance hierarchy, and if
     * one of the stereotype's ancestors has a matching name this method will return true, false
     * otherwise.
     * For example, if we have a certain stereotype called <<exception>> and a model element has a
     * stereotype called <<applicationException>> which extends <<exception>>, when calling this
     * method with 'stereotypeName' defined as 'exception' the method would return true since
     * <<applicationException>> inherits from <<exception>>.  If you want to check if the model
     * element has the exact stereotype, then use the method 'hasExactStereotype' instead.
     * @see ModelElementFacade#hasStereotype(String stereotypeName)
     */
    public boolean hasStereotype(String stereotypeName)
    {
        return this.getSuperFrontEndForward().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperFrontEndForward().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperFrontEndForward().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperFrontEndForward().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperFrontEndForward().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperFrontEndForward().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperFrontEndForward().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperFrontEndForward().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperFrontEndForward().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperFrontEndForward().translateConstraints(kind, translation);
    }

    /**
     * An action is a named element that is the fundamental unit of executable functionality. The
     * execution
     * of an action represents some transformation or processing in the modeled system, be it a
     * computer
     * system or otherwise. An action represents a single step within an activity, that is, one that
     * is not
     * further decomposed within the activity. An action has pre- and post-conditions.
     * @see org.andromda.metafacades.uml.TransitionFacade#getEffect()
     */
    public ActionFacade getEffect()
    {
        return this.getSuperFrontEndForward().getEffect();
    }

    /**
     * A representation of the model object 'Constraint'. A condition or restriction expressed in
     * natural
     * language text or in a machine readable language for the purpose of declaring some of the
     * semantics
     * of an element.
     * @see org.andromda.metafacades.uml.TransitionFacade#getGuard()
     */
    public GuardFacade getGuard()
    {
        return this.getSuperFrontEndForward().getGuard();
    }

    /**
     * A representation of the model object 'Vertex'. An abstraction of a node in a state machine
     * graph. In
     * general, it can be the source or destination of any number of transitions.
     * @see org.andromda.metafacades.uml.TransitionFacade#getSource()
     */
    public StateVertexFacade getSource()
    {
        return this.getSuperFrontEndForward().getSource();
    }

    /**
     * A representation of the model object 'Vertex'. An abstraction of a node in a state machine
     * graph. In
     * general, it can be the source or destination of any number of transitions.
     * @see org.andromda.metafacades.uml.TransitionFacade#getTarget()
     */
    public StateVertexFacade getTarget()
    {
        return this.getSuperFrontEndForward().getTarget();
    }

    /**
     * If a trigger is present on this transition, this event represents that trigger.
     * @see org.andromda.metafacades.uml.TransitionFacade#getTrigger()
     */
    public EventFacade getTrigger()
    {
        return this.getSuperFrontEndForward().getTrigger();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.enteringActionState
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringActionState()
     */
    public boolean isEnteringActionState()
    {
        return this.getSuperFrontEndForward().isEnteringActionState();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.enteringDecisionPoint
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringDecisionPoint()
     */
    public boolean isEnteringDecisionPoint()
    {
        return this.getSuperFrontEndForward().isEnteringDecisionPoint();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.enteringFinalState
     * @see org.andromda.metafacades.uml.TransitionFacade#isEnteringFinalState()
     */
    public boolean isEnteringFinalState()
    {
        return this.getSuperFrontEndForward().isEnteringFinalState();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.exitingActionState
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingActionState()
     */
    public boolean isExitingActionState()
    {
        return this.getSuperFrontEndForward().isExitingActionState();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.exitingDecisionPoint
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingDecisionPoint()
     */
    public boolean isExitingDecisionPoint()
    {
        return this.getSuperFrontEndForward().isExitingDecisionPoint();
    }

    /**
     * TODO: Model Documentation for
     * org.andromda.metafacades.uml.TransitionFacade.exitingInitialState
     * @see org.andromda.metafacades.uml.TransitionFacade#isExitingInitialState()
     */
    public boolean isExitingInitialState()
    {
        return this.getSuperFrontEndForward().isExitingInitialState();
    }

    /**
     * TODO: Model Documentation for org.andromda.metafacades.uml.TransitionFacade.triggerPresent
     * @see org.andromda.metafacades.uml.TransitionFacade#isTriggerPresent()
     */
    public boolean isTriggerPresent()
    {
        return this.getSuperFrontEndForward().isTriggerPresent();
    }

    /**
     * @see MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperFrontEndForward().initialize();
    }

    /**
     * @return Object getSuperFrontEndForward().getValidationOwner()
     * @see MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperFrontEndForward().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperFrontEndForward().getValidationName()
     * @see MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperFrontEndForward().getValidationName();
        return name;
    }

    /**
     * <p><b>Constraint:</b> org::andromda::cartridges::jsf2::metafacades::JSFForward::decision forwards need a controller operation with a non-void return type</p>
     * <p><b>Error:</b> If a transition is going into a decision point you will need to defer to an operation with a non-void return type. You will need to model this on the transition's trigger.</p>
     * <p><b>OCL:</b> context JSFForward inv: enteringDecisionPoint implies (decisionTrigger->notEmpty() and operationCall->notEmpty() and operationCall.returnTypePresent)</p>
     * @param validationMessages Collection<ModelValidationMessage>
     * @see MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperFrontEndForward().validateInvariants(validationMessages);
        try
        {
            final Object contextElement = this.THIS();
            boolean constraintValid = OCLResultEnsurer.ensure((Boolean.valueOf(String.valueOf(OCLIntrospector.invoke(contextElement,"enteringDecisionPoint"))).booleanValue()?(OCLCollections.notEmpty(OCLIntrospector.invoke(contextElement,"decisionTrigger"))&&OCLCollections.notEmpty(OCLIntrospector.invoke(contextElement,"operationCall"))&&Boolean.valueOf(String.valueOf(OCLIntrospector.invoke(contextElement,"operationCall.returnTypePresent"))).booleanValue()):true));
            if (!constraintValid)
            {
                validationMessages.add(
                    new ModelValidationMessage(
                        (MetafacadeBase)contextElement ,
                        "org::andromda::cartridges::jsf2::metafacades::JSFForward::decision forwards need a controller operation with a non-void return type",
                        "If a transition is going into a decision point you will need to defer to an operation with a non-void return type. You will need to model this on the transition's trigger."));
            }
        }
        catch (Throwable th)
        {
            Throwable cause = th.getCause();
            int depth = 0; // Some throwables have infinite recursion
            while (cause != null && depth < 7)
            {
                th = cause;
                depth++;
            }
            logger.error("Error validating constraint 'org::andromda::cartridges::jsf2::metafacades::JSFForward::decision forwards need a controller operation with a non-void return type' ON "
                + this.THIS().toString() + ": " + th.getMessage(), th);
        }
    }

    /**
     * The property that stores the name of the metafacade.
     */
    private static final String NAME_PROPERTY = "name";
    private static final String FQNAME_PROPERTY = "fullyQualifiedName";

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder toString = new StringBuilder(this.getClass().getName());
        toString.append("[");
        try
        {
            toString.append(Introspector.instance().getProperty(this, FQNAME_PROPERTY));
        }
        catch (final Throwable tryAgain)
        {
            try
            {
                toString.append(Introspector.instance().getProperty(this, NAME_PROPERTY));
            }
            catch (final Throwable ignore)
            {
                // - just ignore when the metafacade doesn't have a name or fullyQualifiedName property
            }
        }
        toString.append("]");
        return toString.toString();
    }
}