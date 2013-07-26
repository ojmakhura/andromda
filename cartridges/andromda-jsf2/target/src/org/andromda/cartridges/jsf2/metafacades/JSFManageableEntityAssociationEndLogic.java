// license-header java merge-point
//
// Attention: generated code (by MetafacadeLogic.vsl) - do not modify!
//
package org.andromda.cartridges.jsf2.metafacades;

import java.util.Collection;
import org.andromda.core.common.Introspector;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelValidationMessage;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AssociationFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ConstraintFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.ManageableEntityAssociationEnd;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.StereotypeFacade;
import org.andromda.metafacades.uml.TaggedValueFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;

/**
 * TODO: Model Documentation for
 * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd
 * MetafacadeLogic for JSFManageableEntityAssociationEnd
 *
 * @see JSFManageableEntityAssociationEnd
 */
public abstract class JSFManageableEntityAssociationEndLogic
    extends MetafacadeBase
    implements JSFManageableEntityAssociationEnd
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
    protected JSFManageableEntityAssociationEndLogic(Object metaObjectIn, String context)
    {
        super(metaObjectIn, getContext(context));
        this.superManageableEntityAssociationEnd =
           (ManageableEntityAssociationEnd)
            MetafacadeFactory.getInstance().createFacadeImpl(
                    "org.andromda.metafacades.uml.ManageableEntityAssociationEnd",
                    metaObjectIn,
                    getContext(context));
        this.metaObject = metaObjectIn;
    }

    /**
     * Gets the context for this metafacade logic instance.
     * @param context String. Set to JSFManageableEntityAssociationEnd if null
     * @return context String
     */
    private static String getContext(String context)
    {
        if (context == null)
        {
            context = "org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd";
        }
        return context;
    }

    private ManageableEntityAssociationEnd superManageableEntityAssociationEnd;
    private boolean superManageableEntityAssociationEndInitialized = false;

    /**
     * Gets the ManageableEntityAssociationEnd parent instance.
     * @return this.superManageableEntityAssociationEnd ManageableEntityAssociationEnd
     */
    private ManageableEntityAssociationEnd getSuperManageableEntityAssociationEnd()
    {
        if (!this.superManageableEntityAssociationEndInitialized)
        {
            ((MetafacadeBase)this.superManageableEntityAssociationEnd).setMetafacadeContext(this.getMetafacadeContext());
            this.superManageableEntityAssociationEndInitialized = true;
        }
        return this.superManageableEntityAssociationEnd;
    }

    /** Reset context only for non-root metafacades
     * @param context
     * @see org.andromda.core.metafacade.MetafacadeBase#resetMetafacadeContext(String context)
     */
    @Override
    public void resetMetafacadeContext(String context)
    {
        if (!this.contextRoot) // reset context only for non-root metafacades
        {
            context = getContext(context);  // to have same value as in original constructor call
            setMetafacadeContext (context);
            if (this.superManageableEntityAssociationEndInitialized)
            {
                ((MetafacadeBase)this.superManageableEntityAssociationEnd).resetMetafacadeContext(context);
            }
        }
    }

    /**
     * @return boolean true always
     * @see JSFManageableEntityAssociationEnd
     */
    public boolean isJSFManageableEntityAssociationEndMetaType()
    {
        return true;
    }

    // --------------- attributes ---------------------

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getMessageKey()
    * @return String
    */
    protected abstract String handleGetMessageKey();

    private String __messageKey1a;
    private boolean __messageKey1aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.messageKey
     * @return (String)handleGetMessageKey()
     */
    public final String getMessageKey()
    {
        String messageKey1a = this.__messageKey1a;
        if (!this.__messageKey1aSet)
        {
            // messageKey has no pre constraints
            messageKey1a = handleGetMessageKey();
            // messageKey has no post constraints
            this.__messageKey1a = messageKey1a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageKey1aSet = true;
            }
        }
        return messageKey1a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getMessageValue()
    * @return String
    */
    protected abstract String handleGetMessageValue();

    private String __messageValue2a;
    private boolean __messageValue2aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.messageValue
     * @return (String)handleGetMessageValue()
     */
    public final String getMessageValue()
    {
        String messageValue2a = this.__messageValue2a;
        if (!this.__messageValue2aSet)
        {
            // messageValue has no pre constraints
            messageValue2a = handleGetMessageValue();
            // messageValue has no post constraints
            this.__messageValue2a = messageValue2a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__messageValue2aSet = true;
            }
        }
        return messageValue2a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getOnlineHelpKey()
    * @return String
    */
    protected abstract String handleGetOnlineHelpKey();

    private String __onlineHelpKey3a;
    private boolean __onlineHelpKey3aSet = false;

    /**
     * The key to lookup the online help documentation. This documentation is gathered from the
     * documentation entered by the user, as well as analyzing the model.
     * @return (String)handleGetOnlineHelpKey()
     */
    public final String getOnlineHelpKey()
    {
        String onlineHelpKey3a = this.__onlineHelpKey3a;
        if (!this.__onlineHelpKey3aSet)
        {
            // onlineHelpKey has no pre constraints
            onlineHelpKey3a = handleGetOnlineHelpKey();
            // onlineHelpKey has no post constraints
            this.__onlineHelpKey3a = onlineHelpKey3a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__onlineHelpKey3aSet = true;
            }
        }
        return onlineHelpKey3a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getOnlineHelpValue()
    * @return String
    */
    protected abstract String handleGetOnlineHelpValue();

    private String __onlineHelpValue4a;
    private boolean __onlineHelpValue4aSet = false;

    /**
     * The online help documentation. This documentation is gathered from the documentation entered
     * by the user, as well as analyzing the model. The format is HTML without any style.
     * @return (String)handleGetOnlineHelpValue()
     */
    public final String getOnlineHelpValue()
    {
        String onlineHelpValue4a = this.__onlineHelpValue4a;
        if (!this.__onlineHelpValue4aSet)
        {
            // onlineHelpValue has no pre constraints
            onlineHelpValue4a = handleGetOnlineHelpValue();
            // onlineHelpValue has no post constraints
            this.__onlineHelpValue4a = onlineHelpValue4a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__onlineHelpValue4aSet = true;
            }
        }
        return onlineHelpValue4a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getBackingListName()
    * @return String
    */
    protected abstract String handleGetBackingListName();

    private String __backingListName5a;
    private boolean __backingListName5aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.backingListName
     * @return (String)handleGetBackingListName()
     */
    public final String getBackingListName()
    {
        String backingListName5a = this.__backingListName5a;
        if (!this.__backingListName5aSet)
        {
            // backingListName has no pre constraints
            backingListName5a = handleGetBackingListName();
            // backingListName has no post constraints
            this.__backingListName5a = backingListName5a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__backingListName5aSet = true;
            }
        }
        return backingListName5a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getValueListName()
    * @return String
    */
    protected abstract String handleGetValueListName();

    private String __valueListName6a;
    private boolean __valueListName6aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.valueListName
     * @return (String)handleGetValueListName()
     */
    public final String getValueListName()
    {
        String valueListName6a = this.__valueListName6a;
        if (!this.__valueListName6aSet)
        {
            // valueListName has no pre constraints
            valueListName6a = handleGetValueListName();
            // valueListName has no post constraints
            this.__valueListName6a = valueListName6a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__valueListName6aSet = true;
            }
        }
        return valueListName6a;
    }

   /**
    * @see org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd#getLabelListName()
    * @return String
    */
    protected abstract String handleGetLabelListName();

    private String __labelListName7a;
    private boolean __labelListName7aSet = false;

    /**
     * TODO: Model Documentation for
     * org.andromda.cartridges.jsf2.metafacades.JSFManageableEntityAssociationEnd.labelListName
     * @return (String)handleGetLabelListName()
     */
    public final String getLabelListName()
    {
        String labelListName7a = this.__labelListName7a;
        if (!this.__labelListName7aSet)
        {
            // labelListName has no pre constraints
            labelListName7a = handleGetLabelListName();
            // labelListName has no post constraints
            this.__labelListName7a = labelListName7a;
            if (isMetafacadePropertyCachingEnabled())
            {
                this.__labelListName7aSet = true;
            }
        }
        return labelListName7a;
    }

    /**
     * @return true
     * @see ManageableEntityAssociationEnd
     */
    public boolean isManageableEntityAssociationEndMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see org.andromda.metafacades.uml.EntityAssociationEnd
     */
    public boolean isEntityAssociationEndMetaType()
    {
        return true;
    }

    /**
     * @return true
     * @see AssociationEndFacade
     */
    public boolean isAssociationEndFacadeMetaType()
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

    // ----------- delegates to ManageableEntityAssociationEnd ------------
    /**
     * A name suitable for use when adding new elements to this association end in programming code.
     * @see AssociationEndFacade#getAdderName()
     */
    public String getAdderName()
    {
        return this.getSuperManageableEntityAssociationEnd().getAdderName();
    }

    /**
     * Returns the value of the 'Aggregation' attribute (none, shared, composite). The default value
     * is "none". The literals are from the enumeration org.eclipse.uml2.uml.AggregationKind.
     * Specifies the kind of aggregation that applies to the Property.
     * @see AssociationEndFacade#getAggregationKind()
     */
    public String getAggregationKind()
    {
        return this.getSuperManageableEntityAssociationEnd().getAggregationKind();
    }

    /**
     * The association owning this association end.
     * @see AssociationEndFacade#getAssociation()
     */
    public AssociationFacade getAssociation()
    {
        return this.getSuperManageableEntityAssociationEnd().getAssociation();
    }

    /**
     * UML2: Returns the value of the 'Default' attribute. Specifies a String that represents a
     * value to be used when no argument is supplied for the Property. A String that is evaluated to
     * give a default value for the Property when an object of the owning Classifier is
     * instantiated.  Can be something like: new ValueObject(values);
     * @see AssociationEndFacade#getDefault()
     */
    public String getDefault()
    {
        return this.getSuperManageableEntityAssociationEnd().getDefault();
    }

    /**
     * A name suitable for use when accessing this association end in programming code.
     * @see AssociationEndFacade#getGetterName()
     */
    public String getGetterName()
    {
        return this.getSuperManageableEntityAssociationEnd().getGetterName();
    }

    /**
     * The name of the type that is returned on the accessor and mutator operations,  determined in
     * part by the multiplicity.
     * @see AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName()
    {
        return this.getSuperManageableEntityAssociationEnd().getGetterSetterTypeName();
    }

    /**
     * the lower value for the multiplicity
     * -only applicable for UML2
     * @see AssociationEndFacade#getLower()
     */
    public int getLower()
    {
        return this.getSuperManageableEntityAssociationEnd().getLower();
    }

    /**
     * The other association end owned by this end's association.
     * @see AssociationEndFacade#getOtherEnd()
     */
    public AssociationEndFacade getOtherEnd()
    {
        return this.getSuperManageableEntityAssociationEnd().getOtherEnd();
    }

    /**
     * A name suitable for use when removing element from this association end in programming code.
     * @see AssociationEndFacade#getRemoverName()
     */
    public String getRemoverName()
    {
        return this.getSuperManageableEntityAssociationEnd().getRemoverName();
    }

    /**
     * A name suitable for use when accessing this association end in programming code.
     * @see AssociationEndFacade#getSetterName()
     */
    public String getSetterName()
    {
        return this.getSuperManageableEntityAssociationEnd().getSetterName();
    }

    /**
     * The classifier attached to this association end.
     * @see AssociationEndFacade#getType()
     */
    public ClassifierFacade getType()
    {
        return this.getSuperManageableEntityAssociationEnd().getType();
    }

    /**
     * the upper value for the multiplicity (will be -1 for *)
     * -only applicable for UML2
     * @see AssociationEndFacade#getUpper()
     */
    public int getUpper()
    {
        return this.getSuperManageableEntityAssociationEnd().getUpper();
    }

    /**
     * True if this association end represents an aggregation relationship.
     * @see AssociationEndFacade#isAggregation()
     */
    public boolean isAggregation()
    {
        return this.getSuperManageableEntityAssociationEnd().isAggregation();
    }

    /**
     * Is true if update of one side of the association should also update the other side. false if
     * not.
     * @see AssociationEndFacade#isBidirectional()
     */
    public boolean isBidirectional()
    {
        return this.getSuperManageableEntityAssociationEnd().isBidirectional();
    }

    /**
     * Returns whether or not (true/false) this association end is the child end of the assocation
     * (i.e. the other end's aggregation is composition).
     * @see AssociationEndFacade#isChild()
     */
    public boolean isChild()
    {
        return this.getSuperManageableEntityAssociationEnd().isChild();
    }

    /**
     * True if this association end represents a composition relationship.
     * @see AssociationEndFacade#isComposition()
     */
    public boolean isComposition()
    {
        return this.getSuperManageableEntityAssociationEnd().isComposition();
    }

    /**
     * If the association end is derived (its value is computed). UML2 only. UML14 always returns
     * false. Default=false.
     * @see AssociationEndFacade#isDerived()
     */
    public boolean isDerived()
    {
        return this.getSuperManageableEntityAssociationEnd().isDerived();
    }

    /**
     * IsLeaf property in the association end property. If true, attribute is final, cannot be
     * extended or implemented by a descendant. Default=false.
     * @see AssociationEndFacade#isLeaf()
     */
    public boolean isLeaf()
    {
        return this.getSuperManageableEntityAssociationEnd().isLeaf();
    }

    /**
     * True if this association end's multiplicity is greater than one.
     * @see AssociationEndFacade#isMany()
     */
    public boolean isMany()
    {
        return this.getSuperManageableEntityAssociationEnd().isMany();
    }

    /**
     * True if this association end's and the other end's multiplicities are both many.
     * @see AssociationEndFacade#isMany2Many()
     */
    public boolean isMany2Many()
    {
        return this.getSuperManageableEntityAssociationEnd().isMany2Many();
    }

    /**
     * True if this association end's multiplicity is many while the other end's is one.
     * @see AssociationEndFacade#isMany2One()
     */
    public boolean isMany2One()
    {
        return this.getSuperManageableEntityAssociationEnd().isMany2One();
    }

    /**
     * True if it is possible to navigate from the other end to this association end .
     * @see AssociationEndFacade#isNavigable()
     */
    public boolean isNavigable()
    {
        return this.getSuperManageableEntityAssociationEnd().isNavigable();
    }

    /**
     * True if this association end's multiplicity is one while the other end's is many.
     * @see AssociationEndFacade#isOne2Many()
     */
    public boolean isOne2Many()
    {
        return this.getSuperManageableEntityAssociationEnd().isOne2Many();
    }

    /**
     * True if this association end's and the other end's multiplicities are both one.
     * @see AssociationEndFacade#isOne2One()
     */
    public boolean isOne2One()
    {
        return this.getSuperManageableEntityAssociationEnd().isOne2One();
    }

    /**
     * Indicates whether or not the association ends are ordered (if multiplicity is greater than
     * 1).
     * @see AssociationEndFacade#isOrdered()
     */
    public boolean isOrdered()
    {
        return this.getSuperManageableEntityAssociationEnd().isOrdered();
    }

    /**
     * True if the association end cannot be changed.
     * @see AssociationEndFacade#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return this.getSuperManageableEntityAssociationEnd().isReadOnly();
    }

    /**
     * True if this association end's multiplicity is strictly greater than zero.
     * @see AssociationEndFacade#isRequired()
     */
    public boolean isRequired()
    {
        return this.getSuperManageableEntityAssociationEnd().isRequired();
    }

    /**
     * Indicates if this associationEnd is 'static', meaning it has a classifier scope.
     * @see AssociationEndFacade#isStatic()
     */
    public boolean isStatic()
    {
        return this.getSuperManageableEntityAssociationEnd().isStatic();
    }

    /**
     * UML2: If the association attribute is unique within the Collection type. UML14 always returns
     * false. Unique+Ordered determines the implementation Collection type. Default=false.
     * @see AssociationEndFacade#isUnique()
     */
    public boolean isUnique()
    {
        return this.getSuperManageableEntityAssociationEnd().isUnique();
    }

    /**
     * The name of the index to create on a column that persists the foreign key attribute.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getColumnIndex()
     */
    public String getColumnIndex()
    {
        return this.getSuperManageableEntityAssociationEnd().getColumnIndex();
    }

    /**
     * The name of the column that makes up the foreign key.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getColumnName()
     */
    public String getColumnName()
    {
        return this.getSuperManageableEntityAssociationEnd().getColumnName();
    }

    /**
     * The name of the foreign key constraint to use for databases.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeyConstraintName()
     */
    public String getForeignKeyConstraintName()
    {
        return this.getSuperManageableEntityAssociationEnd().getForeignKeyConstraintName();
    }

    /**
     * The current foreign key suffix specified for this entity association end facade.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeySuffix()
     */
    public String getForeignKeySuffix()
    {
        return this.getSuperManageableEntityAssociationEnd().getForeignKeySuffix();
    }

    /**
     * The SQL type for this the foreign key column of this association end.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getSqlType()
     */
    public String getSqlType()
    {
        return this.getSuperManageableEntityAssociationEnd().getSqlType();
    }

    /**
     * The name of the unique-key that this unique association end belongs
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getUniqueGroup()
     */
    public String getUniqueGroup()
    {
        return this.getSuperManageableEntityAssociationEnd().getUniqueGroup();
    }

    /**
     * Indicates whether or not a foreign identifier should be used for the entity that owns this
     * association end.  This would only make sense in the case of a child in a one-to-one
     * parent-child association.  If this flag is true, then the identifier of this entity should
     * also be used as the foreign key to the related parent entity.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#isForeignIdentifier()
     */
    public boolean isForeignIdentifier()
    {
        return this.getSuperManageableEntityAssociationEnd().isForeignIdentifier();
    }

    /**
     * True if this association is an identifier for its entity.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#isIdentifier()
     */
    public boolean isIdentifier()
    {
        return this.getSuperManageableEntityAssociationEnd().isIdentifier();
    }

    /**
     * True if the associationEnd is marked with identifiers stereotype, false otherwise.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#isIdentifiersPresent()
     */
    public boolean isIdentifiersPresent()
    {
        return this.getSuperManageableEntityAssociationEnd().isIdentifiersPresent();
    }

    /**
     * Indicates this association end should be ignored by the persistence layer.
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#isTransient()
     */
    public boolean isTransient()
    {
        return this.getSuperManageableEntityAssociationEnd().isTransient();
    }

    /**
     * Represents an attribute of an entity.
     * @see ManageableEntityAssociationEnd#getManageableIdentifier()
     */
    public EntityAttribute getManageableIdentifier()
    {
        return this.getSuperManageableEntityAssociationEnd().getManageableIdentifier();
    }

    /**
     * Whether or not this association end should be displayed.
     * @see ManageableEntityAssociationEnd#isDisplay()
     */
    public boolean isDisplay()
    {
        return this.getSuperManageableEntityAssociationEnd().isDisplay();
    }

    /**
     * Copies all tagged values from the given ModelElementFacade to this model element facade.
     * @see ModelElementFacade#copyTaggedValues(ModelElementFacade element)
     */
    public void copyTaggedValues(ModelElementFacade element)
    {
        this.getSuperManageableEntityAssociationEnd().copyTaggedValues(element);
    }

    /**
     * Finds the tagged value with the specified 'tagName'. In case there are more values the first
     * one found will be returned.
     * @see ModelElementFacade#findTaggedValue(String tagName)
     */
    public Object findTaggedValue(String tagName)
    {
        return this.getSuperManageableEntityAssociationEnd().findTaggedValue(tagName);
    }

    /**
     * Returns all the values for the tagged value with the specified name. The returned collection
     * will contains only String instances, or will be empty. Never null.
     * @see ModelElementFacade#findTaggedValues(String tagName)
     */
    public Collection<Object> findTaggedValues(String tagName)
    {
        return this.getSuperManageableEntityAssociationEnd().findTaggedValues(tagName);
    }

    /**
     * Returns the fully qualified name of the model element. The fully qualified name includes
     * complete package qualified name of the underlying model element. The templates parameter will
     * be replaced by the correct one given the binding relation of the parameter to this element.
     * @see ModelElementFacade#getBindedFullyQualifiedName(ModelElementFacade bindedElement)
     */
    public String getBindedFullyQualifiedName(ModelElementFacade bindedElement)
    {
        return this.getSuperManageableEntityAssociationEnd().getBindedFullyQualifiedName(bindedElement);
    }

    /**
     * Gets all constraints belonging to the model element.
     * @see ModelElementFacade#getConstraints()
     */
    public Collection<ConstraintFacade> getConstraints()
    {
        return this.getSuperManageableEntityAssociationEnd().getConstraints();
    }

    /**
     * Returns the constraints of the argument kind that have been placed onto this model. Typical
     * kinds are "inv", "pre" and "post". Other kinds are possible.
     * @see ModelElementFacade#getConstraints(String kind)
     */
    public Collection<ConstraintFacade> getConstraints(String kind)
    {
        return this.getSuperManageableEntityAssociationEnd().getConstraints(kind);
    }

    /**
     * Gets the documentation for the model element, The indent argument is prefixed to each line.
     * By default this method wraps lines after 64 characters.
     * This method is equivalent to <code>getDocumentation(indent, 64)</code>.
     * @see ModelElementFacade#getDocumentation(String indent)
     */
    public String getDocumentation(String indent)
    {
        return this.getSuperManageableEntityAssociationEnd().getDocumentation(indent);
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
        return this.getSuperManageableEntityAssociationEnd().getDocumentation(indent, lineLength);
    }

    /**
     * This method returns the documentation for this model element, with the lines wrapped after
     * the specified number of characters, values of less than 1 will indicate no line wrapping is
     * required. HTML style determines if HTML Escaping is applied.
     * @see ModelElementFacade#getDocumentation(String indent, int lineLength, boolean htmlStyle)
     */
    public String getDocumentation(String indent, int lineLength, boolean htmlStyle)
    {
        return this.getSuperManageableEntityAssociationEnd().getDocumentation(indent, lineLength, htmlStyle);
    }

    /**
     * The fully qualified name of this model element.
     * @see ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName()
    {
        return this.getSuperManageableEntityAssociationEnd().getFullyQualifiedName();
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
        return this.getSuperManageableEntityAssociationEnd().getFullyQualifiedName(modelName);
    }

    /**
     * Returns the fully qualified name as a path, the returned value always starts with out a slash
     * '/'.
     * @see ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath()
    {
        return this.getSuperManageableEntityAssociationEnd().getFullyQualifiedNamePath();
    }

    /**
     * Gets the unique identifier of the underlying model element.
     * @see ModelElementFacade#getId()
     */
    public String getId()
    {
        return this.getSuperManageableEntityAssociationEnd().getId();
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
        return this.getSuperManageableEntityAssociationEnd().getKeywords();
    }

    /**
     * UML2: Retrieves a localized label for this named element.
     * @see ModelElementFacade#getLabel()
     */
    public String getLabel()
    {
        return this.getSuperManageableEntityAssociationEnd().getLabel();
    }

    /**
     * The language mappings that have been set for this model element.
     * @see ModelElementFacade#getLanguageMappings()
     */
    public TypeMappings getLanguageMappings()
    {
        return this.getSuperManageableEntityAssociationEnd().getLanguageMappings();
    }

    /**
     * Return the model containing this model element (multiple models may be loaded and processed
     * at the same time).
     * @see ModelElementFacade#getModel()
     */
    public ModelFacade getModel()
    {
        return this.getSuperManageableEntityAssociationEnd().getModel();
    }

    /**
     * The name of the model element.
     * @see ModelElementFacade#getName()
     */
    public String getName()
    {
        return this.getSuperManageableEntityAssociationEnd().getName();
    }

    /**
     * Gets the package to which this model element belongs.
     * @see ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage()
    {
        return this.getSuperManageableEntityAssociationEnd().getPackage();
    }

    /**
     * The name of this model element's package.
     * @see ModelElementFacade#getPackageName()
     */
    public String getPackageName()
    {
        return this.getSuperManageableEntityAssociationEnd().getPackageName();
    }

    /**
     * Gets the package name (optionally providing the ability to retrieve the model name and not
     * the mapped name).
     * @see ModelElementFacade#getPackageName(boolean modelName)
     */
    public String getPackageName(boolean modelName)
    {
        return this.getSuperManageableEntityAssociationEnd().getPackageName(modelName);
    }

    /**
     * Returns the package as a path, the returned value always starts with out a slash '/'.
     * @see ModelElementFacade#getPackagePath()
     */
    public String getPackagePath()
    {
        return this.getSuperManageableEntityAssociationEnd().getPackagePath();
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
        return this.getSuperManageableEntityAssociationEnd().getQualifiedName();
    }

    /**
     * Gets the root package for the model element.
     * @see ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage()
    {
        return this.getSuperManageableEntityAssociationEnd().getRootPackage();
    }

    /**
     * Gets the dependencies for which this model element is the source.
     * @see ModelElementFacade#getSourceDependencies()
     */
    public Collection<DependencyFacade> getSourceDependencies()
    {
        return this.getSuperManageableEntityAssociationEnd().getSourceDependencies();
    }

    /**
     * If this model element is the context of an activity graph, this represents that activity
     * graph.
     * @see ModelElementFacade#getStateMachineContext()
     */
    public StateMachineFacade getStateMachineContext()
    {
        return this.getSuperManageableEntityAssociationEnd().getStateMachineContext();
    }

    /**
     * The collection of ALL stereotype names for this model element.
     * @see ModelElementFacade#getStereotypeNames()
     */
    public Collection<String> getStereotypeNames()
    {
        return this.getSuperManageableEntityAssociationEnd().getStereotypeNames();
    }

    /**
     * Gets all stereotypes for this model element.
     * @see ModelElementFacade#getStereotypes()
     */
    public Collection<StereotypeFacade> getStereotypes()
    {
        return this.getSuperManageableEntityAssociationEnd().getStereotypes();
    }

    /**
     * Return the TaggedValues associated with this model element, under all stereotypes.
     * @see ModelElementFacade#getTaggedValues()
     */
    public Collection<TaggedValueFacade> getTaggedValues()
    {
        return this.getSuperManageableEntityAssociationEnd().getTaggedValues();
    }

    /**
     * Gets the dependencies for which this model element is the target.
     * @see ModelElementFacade#getTargetDependencies()
     */
    public Collection<DependencyFacade> getTargetDependencies()
    {
        return this.getSuperManageableEntityAssociationEnd().getTargetDependencies();
    }

    /**
     * Get the template parameter for this model element having the parameterName
     * @see ModelElementFacade#getTemplateParameter(String parameterName)
     */
    public Object getTemplateParameter(String parameterName)
    {
        return this.getSuperManageableEntityAssociationEnd().getTemplateParameter(parameterName);
    }

    /**
     * Get the template parameters for this model element
     * @see ModelElementFacade#getTemplateParameters()
     */
    public Collection<TemplateParameterFacade> getTemplateParameters()
    {
        return this.getSuperManageableEntityAssociationEnd().getTemplateParameters();
    }

    /**
     * The visibility (i.e. public, private, protected or package) of the model element, will
     * attempt a lookup for these values in the language mappings (if any).
     * @see ModelElementFacade#getVisibility()
     */
    public String getVisibility()
    {
        return this.getSuperManageableEntityAssociationEnd().getVisibility();
    }

    /**
     * Returns true if the model element has the exact stereotype (meaning no stereotype inheritance
     * is taken into account when searching for the stereotype), false otherwise.
     * @see ModelElementFacade#hasExactStereotype(String stereotypeName)
     */
    public boolean hasExactStereotype(String stereotypeName)
    {
        return this.getSuperManageableEntityAssociationEnd().hasExactStereotype(stereotypeName);
    }

    /**
     * Does the UML Element contain the named Keyword? Keywords can be separated by space, comma,
     * pipe, semicolon, or << >>
     * @see ModelElementFacade#hasKeyword(String keywordName)
     */
    public boolean hasKeyword(String keywordName)
    {
        return this.getSuperManageableEntityAssociationEnd().hasKeyword(keywordName);
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
        return this.getSuperManageableEntityAssociationEnd().hasStereotype(stereotypeName);
    }

    /**
     * True if there are target dependencies from this element that are instances of BindingFacade.
     * Deprecated in UML2: Use TemplateBinding parameters instead of dependencies.
     * @see ModelElementFacade#isBindingDependenciesPresent()
     */
    public boolean isBindingDependenciesPresent()
    {
        return this.getSuperManageableEntityAssociationEnd().isBindingDependenciesPresent();
    }

    /**
     * Indicates if any constraints are present on this model element.
     * @see ModelElementFacade#isConstraintsPresent()
     */
    public boolean isConstraintsPresent()
    {
        return this.getSuperManageableEntityAssociationEnd().isConstraintsPresent();
    }

    /**
     * Indicates if any documentation is present on this model element.
     * @see ModelElementFacade#isDocumentationPresent()
     */
    public boolean isDocumentationPresent()
    {
        return this.getSuperManageableEntityAssociationEnd().isDocumentationPresent();
    }

    /**
     * True if this element name is a reserved word in Java, C#, ANSI or ISO C, C++, JavaScript.
     * @see ModelElementFacade#isReservedWord()
     */
    public boolean isReservedWord()
    {
        return this.getSuperManageableEntityAssociationEnd().isReservedWord();
    }

    /**
     * True is there are template parameters on this model element. For UML2, applies to Class,
     * Operation, Property, and Parameter.
     * @see ModelElementFacade#isTemplateParametersPresent()
     */
    public boolean isTemplateParametersPresent()
    {
        return this.getSuperManageableEntityAssociationEnd().isTemplateParametersPresent();
    }

    /**
     * True if this element name is a valid identifier name in Java, C#, ANSI or ISO C, C++,
     * JavaScript. Contains no spaces, special characters etc. Constraint always applied on
     * Enumerations and Interfaces, optionally applies on other model elements.
     * @see ModelElementFacade#isValidIdentifierName()
     */
    public boolean isValidIdentifierName()
    {
        return this.getSuperManageableEntityAssociationEnd().isValidIdentifierName();
    }

    /**
     * Searches for the constraint with the specified 'name' on this model element, and if found
     * translates it using the specified 'translation' from a translation library discovered by the
     * framework.
     * @see ModelElementFacade#translateConstraint(String name, String translation)
     */
    public String translateConstraint(String name, String translation)
    {
        return this.getSuperManageableEntityAssociationEnd().translateConstraint(name, translation);
    }

    /**
     * Translates all constraints belonging to this model element with the given 'translation'.
     * @see ModelElementFacade#translateConstraints(String translation)
     */
    public String[] translateConstraints(String translation)
    {
        return this.getSuperManageableEntityAssociationEnd().translateConstraints(translation);
    }

    /**
     * Translates the constraints of the specified 'kind' belonging to this model element.
     * @see ModelElementFacade#translateConstraints(String kind, String translation)
     */
    public String[] translateConstraints(String kind, String translation)
    {
        return this.getSuperManageableEntityAssociationEnd().translateConstraints(kind, translation);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#initialize()
     */
    @Override
    public void initialize()
    {
        this.getSuperManageableEntityAssociationEnd().initialize();
    }

    /**
     * @return Object getSuperManageableEntityAssociationEnd().getValidationOwner()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    @Override
    public Object getValidationOwner()
    {
        Object owner = this.getSuperManageableEntityAssociationEnd().getValidationOwner();
        return owner;
    }

    /**
     * @return String getSuperManageableEntityAssociationEnd().getValidationName()
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationName()
     */
    @Override
    public String getValidationName()
    {
        String name = this.getSuperManageableEntityAssociationEnd().getValidationName();
        return name;
    }

    /**
     * @param validationMessages Collection<ModelValidationMessage>
     * @see org.andromda.core.metafacade.MetafacadeBase#validateInvariants(Collection validationMessages)
     */
    @Override
    public void validateInvariants(Collection<ModelValidationMessage> validationMessages)
    {
        this.getSuperManageableEntityAssociationEnd().validateInvariants(validationMessages);
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