package org.andromda.metafacades.emf.uml22;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.RedefinableElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.TemplateBinding;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.TemplateSignature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * Implementation of Attribute.
 *
 * We extend Property. We keep a reference to the original property and defer
 * almost all method calls to it.
 *
 * @author Cédric Jeanneret
 */
public class AttributeImpl
    implements Attribute
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(AttributeImpl.class);

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * org.eclipse.uml2.uml.Property
     */
    final Property property;

    /**
     * @param propertyIn
     */
    AttributeImpl(final Property propertyIn)
    {
        this.property = propertyIn;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Attribute)
        {
            Property other = ((AttributeImpl)object).property;
            return this.property.equals(other);
        }
        if (object instanceof AssociationEnd)
        {
            Property other = ((AssociationEndImpl)object).property;
            return this.property.equals(other);
        }
        return this.property.equals(object);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.property.hashCode();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getClass().getName() + '[' + this.property.toString() + ']';
    }

    /**
     * @see org.eclipse.uml2.uml.Element#addKeyword(String)
     */
    public boolean addKeyword(final String arg0)
    {
        return this.property.addKeyword(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#allNamespaces()
     */
    public EList<Namespace> allNamespaces()
    {
        return this.property.allNamespaces();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#allOwnedElements()
     */
    public EList<Element> allOwnedElements()
    {
        return this.property.allOwnedElements();
    }

    /**
     * @param arg0
     */
    public void apply(final Stereotype arg0)
    {
        this.property.applyStereotype(arg0);
    }

    /**
     * @param arg0
     * @return this.property.createDefaultValue(null, null, arg0)
     */
    public ValueSpecification createDefaultValue(final EClass arg0)
    {
        return this.property.createDefaultValue(null, null, arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createDependency(org.eclipse.uml2.uml.NamedElement)
     */
    public Dependency createDependency(final NamedElement arg0)
    {
        return this.property.createDependency(arg0);
    }

    /**
     * @return this.property.createDeployment(null)
     * @see org.eclipse.uml2.uml.Property#createDeployment(String)
     */
    public Deployment createDeployment()
    {
        return this.property.createDeployment(null);
    }

    /**
     * @param arg0
     * @return this.property.createDeployment(arg0.getName())
     */
    public Deployment createDeployment(final EClass arg0)
    {
        return this.property.createDeployment(arg0.getName());
    }

    /**
     * @see org.eclipse.uml2.uml.Element#createEAnnotation(String)
     */
    public EAnnotation createEAnnotation(final String arg0)
    {
        return this.property.createEAnnotation(arg0);
    }

    /**
     * @param arg0
     * @return this.property.createLowerValue(null, null, arg0)
     * @see org.eclipse.uml2.uml.Property#createLowerValue(String, Type, EClass)
     */
    public ValueSpecification createLowerValue(final EClass arg0)
    {
        return this.property.createLowerValue(null, null, arg0);
    }

    /**
     * @return this.property.createNameExpression(null, null)
     * @see org.eclipse.uml2.uml.Property#createNameExpression(String, Type)
     */
    public StringExpression createNameExpression()
    {
        return this.property.createNameExpression(null, null);
    }

    /**
     * @param arg0
     * @return this.property.createNameExpression(arg0.getName(), null)
     * @see org.eclipse.uml2.uml.Property#createNameExpression(String, Type)
     */
    public StringExpression createNameExpression(final EClass arg0)
    {
        return this.property.createNameExpression(arg0.getName(), null);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#createOwnedComment()
     */
    public Comment createOwnedComment()
    {
        return this.property.createOwnedComment();
    }

    /**
     * @param arg0
     * @return this.property.createOwnedComment()
     * @see org.eclipse.uml2.uml.Property#createOwnedComment()
     */
    public Comment createOwnedComment(final EClass arg0)
    {
        return this.property.createOwnedComment();
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#createOwnedTemplateSignature()
     */
    public TemplateSignature createOwnedTemplateSignature()
    {
        //return this.property.createOwnedTemplateSignature();
        logger.error("AttributeImpl.createOwnedTemplateSignature is UML14 only");
        return null;
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @param arg0 
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#createOwnedTemplateSignature(org.eclipse.emf.ecore.EClass)
     */
    public TemplateSignature createOwnedTemplateSignature(final EClass arg0)
    {
        //return this.property.createOwnedTemplateSignature(arg0);
        logger.error("AttributeImpl.createOwnedTemplateSignature(EClass) is UML14 only");
        return null;
    }

    /**
     * @return this.property.createQualifier(null, null)
     * @see org.eclipse.uml2.uml.Property#createQualifier(String, Type)
     */
    public Property createQualifier()
    {
        return this.property.createQualifier(null, null);
    }

    /**
     * @param arg0
     * @return this.property.createQualifier(null, null, arg0)
     * @see org.eclipse.uml2.uml.Property#createQualifier(String, Type, EClass)
     */
    public Property createQualifier(final EClass arg0)
    {
        return this.property.createQualifier(null, null, arg0);
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @return this.property.createTemplateBinding(null)
     * @see org.eclipse.uml2.uml.Property#createTemplateBinding(TemplateSignature)
     */
    public TemplateBinding createTemplateBinding()
    {
        //return this.property.createTemplateBinding(null);
        logger.error("AttributeImpl.property.createTemplateBinding is UML14 only");
        return null;
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @param arg0
     * @return this.property.createTemplateBinding((TemplateSignature) arg0)
     * @see org.eclipse.uml2.uml.Property#createTemplateBinding(TemplateSignature)
     */
    public TemplateBinding createTemplateBinding(final EClass arg0)
    {
        //return this.property.createTemplateBinding((TemplateSignature) arg0);
        logger.error("AttributeImpl.property.createTemplateBinding(EClass) is UML14 only");
        return null;
    }

    /**
     * @param arg0
     * @return this.property.createUpperValue(null, null, arg0)
     * @see org.eclipse.uml2.uml.Property#createUpperValue(String, Type, EClass)
     */
    public ValueSpecification createUpperValue(final EClass arg0)
    {
        return this.property.createUpperValue(null, null, arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#destroy()
     */
    public void destroy()
    {
        this.property.destroy();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    public EList eAdapters()
    {
        return this.property.eAdapters();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    public TreeIterator eAllContents()
    {
        return this.property.eAllContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eClass()
     */
    public EClass eClass()
    {
        return this.property.eClass();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainer()
     */
    public EObject eContainer()
    {
        return this.property.eContainer();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
     */
    public EStructuralFeature eContainingFeature()
    {
        return this.property.eContainingFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
     */
    public EReference eContainmentFeature()
    {
        return this.property.eContainmentFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContents()
     */
    public EList eContents()
    {
        return this.property.eContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    public EList eCrossReferences()
    {
        return this.property.eCrossReferences();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
     */
    public boolean eDeliver()
    {
        return this.property.eDeliver();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public Object eGet(final EStructuralFeature arg0)
    {
        return this.property.eGet(arg0);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature, boolean)
     */
    public Object eGet(
        final EStructuralFeature arg0,
        final boolean arg1)
    {
        return this.property.eGet(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsProxy()
     */
    public boolean eIsProxy()
    {
        return this.property.eIsProxy();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public boolean eIsSet(final EStructuralFeature arg0)
    {
        return this.property.eIsSet(arg0);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    public void eNotify(final Notification arg0)
    {
        this.property.eNotify(arg0);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eResource()
     */
    public Resource eResource()
    {
        return this.property.eResource();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature, Object)
     */
    public void eSet(
        final EStructuralFeature arg0,
        final Object arg1)
    {
        this.property.eSet(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
     */
    public void eSetDeliver(final boolean arg0)
    {
        this.property.eSetDeliver(arg0);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public void eUnset(final EStructuralFeature arg0)
    {
        this.property.eUnset(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getAggregation()
     */
    public AggregationKind getAggregation()
    {
        return this.property.getAggregation();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotype(String)
     */
    public Stereotype getApplicableStereotype(final String arg0)
    {
        return this.property.getApplicableStereotype(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotypes()
     */
    public EList<Stereotype> getApplicableStereotypes()
    {
        return this.property.getApplicableStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedStereotype(String)
     */
    public Stereotype getAppliedStereotype(final String arg0)
    {
        return this.property.getAppliedStereotype(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedStereotypes()
     */
    public EList<Stereotype> getAppliedStereotypes()
    {
        return this.property.getAppliedStereotypes();
    }

    // TODO: Map getAppliedVersion?
    /*public String getAppliedVersion(final Stereotype arg0)
    {
        return this.property.getAppliedVersion(arg0);
    }*/

    /**
     * @see org.eclipse.uml2.uml.Property#getAssociation()
     */
    public Association getAssociation()
    {
        return this.property.getAssociation();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getAssociationEnd()
     */
    public Property getAssociationEnd()
    {
        return this.property.getAssociationEnd();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getClass_()
     */
    public Class getClass_()
    {
        return this.property.getClass_();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependencies()
     */
    public EList getClientDependencies()
    {
        return this.property.getClientDependencies();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependency(String)
     */
    public Dependency getClientDependency(final String arg0)
    {
        return this.property.getClientDependency(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getDatatype()
     */
    public DataType getDatatype()
    {
        return this.property.getDatatype();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getDefault()
     */
    public String getDefault()
    {
        return this.property.getDefault();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getDefaultValue()
     */
    public ValueSpecification getDefaultValue()
    {
        return this.property.getDefaultValue();
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElement(String)
     */
    public PackageableElement getDeployedElement(final String arg0)
    {
        return this.property.getDeployedElement(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElements()
     */
    public EList getDeployedElements()
    {
        return this.property.getDeployedElements();
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployment(String)
     */
    public Deployment getDeployment(final String arg0)
    {
        return this.property.getDeployment(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployments()
     */
    public EList getDeployments()
    {
        return this.property.getDeployments();
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotation(String)
     */
    public EAnnotation getEAnnotation(final String arg0)
    {
        return this.property.getEAnnotation(arg0);
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotations()
     */
    public EList getEAnnotations()
    {
        return this.property.getEAnnotations();
    }

    /**
     * @see org.eclipse.uml2.uml.ConnectableElement#getEnds()
     */
    public EList getEnds()
    {
        return this.property.getEnds();
    }

    /**
     * @see org.eclipse.uml2.uml.Feature#getFeaturingClassifier(String)
     */
    public Classifier getFeaturingClassifier(final String arg0)
    {
        return this.property.getFeaturingClassifier(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Feature#getFeaturingClassifiers()
     */
    public EList getFeaturingClassifiers()
    {
        return this.property.getFeaturingClassifiers();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getKeywords()
     */
    public EList<String> getKeywords()
    {
        return this.property.getKeywords();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getLabel()
     */
    public String getLabel()
    {
        return this.property.getLabel();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getLabel(boolean)
     */
    public String getLabel(final boolean arg0)
    {
        return this.property.getLabel(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#getLower()
     */
    public int getLower()
    {
        return this.property.getLower();
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#getLowerValue()
     */
    public ValueSpecification getLowerValue()
    {
        return this.property.getLowerValue();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getModel()
     */
    public Model getModel()
    {
        return (Model) UmlUtilities.findModel(this.property);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getName()
     */
    public String getName()
    {
        return this.property.getName();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getNameExpression()
     */
    public StringExpression getNameExpression()
    {
        return this.property.getNameExpression();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getNamespace()
     */
    public Namespace getNamespace()
    {
        return this.property.getNamespace();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getNearestPackage()
     */
    public Package getNearestPackage()
    {
        return this.property.getNearestPackage();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getOpposite()
     */
    public Property getOpposite()
    {
        return this.property.getOpposite();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getOwnedComments()
     */
    public EList getOwnedComments()
    {
        return this.property.getOwnedComments();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getOwnedElements()
     */
    public EList getOwnedElements()
    {
        return this.property.getOwnedElements();
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#getOwnedTemplateSignature()
     */
    public TemplateSignature getOwnedTemplateSignature()
    {
        //return this.property.getOwnedTemplateSignature();
        logger.error("AttributeImpl.property.getOwnedTemplateSignature() is UML14 only");
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getOwner()
     */
    public Element getOwner()
    {
        return this.property.getOwner();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getOwningAssociation()
     */
    public Association getOwningAssociation()
    {
        return this.property.getOwningAssociation();
    }

    /**
     * @return this.property.getOwningTemplateParameter()
     */
    public TemplateParameter getOwningParameter()
    {
        return this.property.getOwningTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getQualifiedName()
     */
    public String getQualifiedName()
    {
        return this.property.getQualifiedName();
    }

    /**
     * @param arg0
     * @return this.property.getQualifier(arg0, null)
     * @see org.eclipse.uml2.uml.Property#getQualifier(String, Type)
     */
    public Property getQualifier(final String arg0)
    {
        return this.property.getQualifier(arg0, null);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getQualifiers()
     */
    public EList getQualifiers()
    {
        return this.property.getQualifiers();
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#getRedefinedElement(String)
     */
    public RedefinableElement getRedefinedElement(final String arg0)
    {
        return this.property.getRedefinedElement(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#getRedefinedElements()
     */
    public EList getRedefinedElements()
    {
        return this.property.getRedefinedElements();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getRedefinedProperties()
     */
    public EList getRedefinedProperties()
    {
        return this.property.getRedefinedProperties();
    }

    /**
     * @param arg0
     * @return this.property.getRedefinedProperty(arg0, null)
     * @see org.eclipse.uml2.uml.Property#getRedefinedProperty(String, Type)
     */
    public Property getRedefinedProperty(final String arg0)
    {
        return this.property.getRedefinedProperty(arg0, null);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#getRedefinitionContext(String)
     */
    public Classifier getRedefinitionContext(final String arg0)
    {
        return this.property.getRedefinitionContext(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#getRedefinitionContexts()
     */
    public EList getRedefinitionContexts()
    {
        return this.property.getRedefinitionContexts();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getSubsettedProperties()
     */
    public EList getSubsettedProperties()
    {
        return this.property.getSubsettedProperties();
    }

    /**
     * @param arg0
     * @return this.property.getSubsettedProperty(arg0, null)
     * @see org.eclipse.uml2.uml.Property#getSubsettedProperty(String, Type)
     */
    public Property getSubsettedProperty(final String arg0)
    {
        return this.property.getSubsettedProperty(arg0, null);
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#getTemplateBindings()
     */
    public EList getTemplateBindings()
    {
        //return this.property.getTemplateBindings();
        logger.error("AttributeImpl.property.getTemplateBindings() is UML14 only");
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#getTemplateParameter()
     */
    public TemplateParameter getTemplateParameter()
    {
        return this.property.getTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.uml.TypedElement#getType()
     */
    public Type getType()
    {
        return this.property.getType();
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#getUpper()
     */
    public int getUpper()
    {
        return this.property.getUpper();
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#getUpperValue()
     */
    public ValueSpecification getUpperValue()
    {
        return this.property.getUpperValue();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getValue(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Object getValue(
        final Stereotype arg0,
        final String arg1)
    {
        return this.property.getValue(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getVisibility()
     */
    public VisibilityKind getVisibility()
    {
        return this.property.getVisibility();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#hasKeyword(String)
     */
    public boolean hasKeyword(final String arg0)
    {
        return this.property.hasKeyword(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#hasValue(org.eclipse.uml2.uml.Stereotype, String)
     */
    public boolean hasValue(
        final Stereotype arg0,
        final String arg1)
    {
        return this.property.hasValue(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#includesCardinality(int)
     */
    public boolean includesCardinality(final int arg0)
    {
        return this.property.includesCardinality(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#includesMultiplicity(org.eclipse.uml2.uml.MultiplicityElement)
     */
    public boolean includesMultiplicity(final MultiplicityElement arg0)
    {
        return this.property.includesMultiplicity(arg0);
    }

    /**
     * @param arg0
     * @return this.property.getAppliedStereotype(arg0.getName())!=null
     * @see org.eclipse.uml2.uml.Property#getAppliedStereotype(String)
     */
    public boolean isApplied(final Stereotype arg0)
    {
        return this.property.getAppliedStereotype(arg0.getName())!=null;
    }

    /**
     * @see org.eclipse.uml2.uml.Property#isComposite()
     */
    public boolean isComposite()
    {
        return this.property.isComposite();
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#isConsistentWith(org.eclipse.uml2.uml.RedefinableElement)
     */
    public boolean isConsistentWith(final RedefinableElement arg0)
    {
        return this.property.isConsistentWith(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#isDerived()
     */
    public boolean isDerived()
    {
        return this.property.isDerived();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#isDerivedUnion()
     */
    public boolean isDerivedUnion()
    {
        return this.property.isDerivedUnion();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isDistinguishableFrom(org.eclipse.uml2.uml.NamedElement, org.eclipse.uml2.uml.Namespace)
     */
    public boolean isDistinguishableFrom(
        final NamedElement arg0,
        final Namespace arg1)
    {
        return this.property.isDistinguishableFrom(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#isLeaf()
     */
    public boolean isLeaf()
    {
        return this.property.isLeaf();
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#isMultivalued()
     */
    public boolean isMultivalued()
    {
        return this.property.isMultivalued();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#isNavigable()
     */
    public boolean isNavigable()
    {
        return this.property.isNavigable();
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#isOrdered()
     */
    public boolean isOrdered()
    {
        return this.property.isOrdered();
    }

    /**
     * @see org.eclipse.uml2.uml.StructuralFeature#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return this.property.isReadOnly();
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#isRedefinitionContextValid(org.eclipse.uml2.uml.RedefinableElement)
     */
    public boolean isRedefinitionContextValid(final RedefinableElement arg0)
    {
        return this.property.isRedefinitionContextValid(arg0);
    }

    /**
     * @param arg0
     * @return this.property.isStereotypeRequired(arg0)
     * @see org.eclipse.uml2.uml.Property#isStereotypeRequired(Stereotype)
     */
    public boolean isRequired(final Stereotype arg0)
    {
        return this.property.isStereotypeRequired(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Feature#isStatic()
     */
    public boolean isStatic()
    {
        return this.property.isStatic();
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#isUnique()
     */
    public boolean isUnique()
    {
        return this.property.isUnique();
    }

    /**
     * @return this.property.getLower()
     * @see org.eclipse.uml2.uml.Property#getLower()
     */
    public int lower()
    {
        return this.property.getLower();
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#lowerBound()
     */
    public int lowerBound()
    {
        return this.property.lowerBound();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#mustBeOwned()
     */
    public boolean mustBeOwned()
    {
        return this.property.mustBeOwned();
    }

    /**
     * @return this.property.getOpposite()
     * @see org.eclipse.uml2.uml.Property#getOpposite()
     */
    public Property opposite()
    {
        return this.property.getOpposite();
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#parameterableElements()
     */
    public EList<ParameterableElement> parameterableElements()
    {
        //return this.property.parameterableElements();
        logger.error("AttributeImpl.property.parameterableElements() is UML14 only");
        return null;
    }

    /**
     * @return this.property.getQualifiedName()
     * @see org.eclipse.uml2.uml.Property#getQualifiedName()
     */
    public String qualifiedName()
    {
        return this.property.getQualifiedName();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#removeKeyword(String)
     */
    public boolean removeKeyword(final String arg0)
    {
        return this.property.removeKeyword(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#separator()
     */
    public String separator()
    {
        return this.property.separator();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setAggregation(org.eclipse.uml2.uml.AggregationKind)
     */
    public void setAggregation(final AggregationKind arg0)
    {
        this.property.setAggregation(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setAssociation(org.eclipse.uml2.uml.Association)
     */
    public void setAssociation(final Association arg0)
    {
        this.property.setAssociation(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setAssociationEnd(org.eclipse.uml2.uml.Property)
     */
    public void setAssociationEnd(final Property arg0)
    {
        this.property.setAssociationEnd(arg0);
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.uml.Property#setBooleanDefaultValue(boolean)
     */
    public void setBooleanDefault(final boolean arg0)
    {
        this.property.setBooleanDefaultValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setDatatype(org.eclipse.uml2.uml.DataType)
     */
    public void setDatatype(final DataType arg0)
    {
        this.property.setDatatype(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setDefaultValue(org.eclipse.uml2.uml.ValueSpecification)
     */
    public void setDefaultValue(final ValueSpecification arg0)
    {
        this.property.setDefaultValue(arg0);
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.uml.Property#setIntegerDefaultValue(int)
     */
    public void setIntegerDefault(final int arg0)
    {
        this.property.setIntegerDefaultValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setIsDerived(boolean)
     */
    public void setIsDerived(final boolean arg0)
    {
        this.property.setIsDerived(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setIsDerivedUnion(boolean)
     */
    public void setIsDerivedUnion(final boolean arg0)
    {
        this.property.setIsDerivedUnion(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#setIsLeaf(boolean)
     */
    public void setIsLeaf(final boolean arg0)
    {
        this.property.setIsLeaf(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#setIsOrdered(boolean)
     */
    public void setIsOrdered(final boolean arg0)
    {
        this.property.setIsOrdered(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.StructuralFeature#setIsReadOnly(boolean)
     */
    public void setIsReadOnly(final boolean arg0)
    {
        this.property.setIsReadOnly(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Feature#setIsStatic(boolean)
     */
    public void setIsStatic(final boolean arg0)
    {
        this.property.setIsStatic(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#setIsUnique(boolean)
     */
    public void setIsUnique(final boolean arg0)
    {
        this.property.setIsUnique(arg0);
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.uml.Property#setLower(int)
     */
    public void setLowerBound(final int arg0)
    {
        this.property.setLower(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#setLowerValue(org.eclipse.uml2.uml.ValueSpecification)
     */
    public void setLowerValue(final ValueSpecification arg0)
    {
        this.property.setLowerValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#setName(String)
     */
    public void setName(final String arg0)
    {
        this.property.setName(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#setNameExpression(org.eclipse.uml2.uml.StringExpression)
     */
    public void setNameExpression(final StringExpression arg0)
    {
        this.property.setNameExpression(arg0);
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.uml.Property#setIsNavigable(boolean)
     */
    public void setNavigable(final boolean arg0)
    {
        this.property.setIsNavigable(arg0);
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @param arg0 
     * @see org.eclipse.uml2.uml.TemplateableElement#setOwnedTemplateSignature(org.eclipse.uml2.uml.TemplateSignature)
     */
    public void setOwnedTemplateSignature(final TemplateSignature arg0)
    {
        //this.property.setOwnedTemplateSignature(arg0);
        logger.error("AttributeImpl.property.setOwnedTemplateSignature(TemplateSignature) is UML14 only");
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setOwningAssociation(org.eclipse.uml2.uml.Association)
     */
    public void setOwningAssociation(final Association arg0)
    {
        this.property.setOwningAssociation(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#setOwningTemplateParameter(org.eclipse.uml2.uml.TemplateParameter)
     */
    public void setOwningTemplateParameter(final TemplateParameter arg0)
    {
        this.property.setOwningTemplateParameter(arg0);
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.uml.Property#setStringDefaultValue(String)
     */
    public void setStringDefault(final String arg0)
    {
        this.property.setStringDefaultValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#setTemplateParameter(org.eclipse.uml2.uml.TemplateParameter)
     */
    public void setTemplateParameter(final TemplateParameter arg0)
    {
        this.property.setTemplateParameter(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.TypedElement#setType(org.eclipse.uml2.uml.Type)
     */
    public void setType(final Type arg0)
    {
        this.property.setType(arg0);
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.uml.Property#setUnlimitedNaturalDefaultValue(int)
     */
    public void setUnlimitedNaturalDefault(final int arg0)
    {
        this.property.setUnlimitedNaturalDefaultValue(arg0);
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.uml.Property#setUpper(int)
     */
    public void setUpperBound(final int arg0)
    {
        this.property.setUpper(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#setUpperValue(org.eclipse.uml2.uml.ValueSpecification)
     */
    public void setUpperValue(final ValueSpecification arg0)
    {
        this.property.setUpperValue(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#setValue(org.eclipse.uml2.uml.Stereotype, String, Object)
     */
    public void setValue(
        final Stereotype arg0,
        final String arg1,
        final Object arg2)
    {
        this.property.setValue(
            arg0,
            arg1,
            arg2);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#setVisibility(org.eclipse.uml2.uml.VisibilityKind)
     */
    public void setVisibility(final VisibilityKind arg0)
    {
        this.property.setVisibility(arg0);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#subsettingContext()
     */
    public EList<Type> subsettingContext()
    {
        return this.property.subsettingContext();
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.uml.Property#unapplyStereotype(Stereotype)
     */
    public void unapply(final Stereotype arg0)
    {
        this.property.unapplyStereotype(arg0);
    }

    /**
     * @return this.property.getUpper()
     * @see org.eclipse.uml2.uml.Property#getUpper()
     */
    public int upper()
    {
        return this.property.getUpper();
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#upperBound()
     */
    public int upperBound()
    {
        return this.property.upperBound();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateDerivedUnionIsDerived(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDerivedUnionIsDerived(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateDerivedUnionIsDerived(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasOwner(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateHasOwner(
            arg0,
            arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @return this.property.validateLowerGe0(arg0, arg1)
     * @see org.eclipse.uml2.uml.Property#validateLowerGe0(DiagnosticChain, Map)
     */
    public boolean validateLowerEqLowerbound(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        // TODO: Verify validateLowerGe0 == validateLowerEqLowerbound
        return this.property.validateLowerGe0(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#validateLowerGe0(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateLowerGe0(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateLowerGe0(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateMultiplicityOfComposite(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateMultiplicityOfComposite(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateMultiplicityOfComposite(
            arg0,
            arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @return this.property.validateRedefinedPropertyInherited(arg0, arg1)
     * @see org.eclipse.uml2.uml.Property#validateRedefinedPropertyInherited(DiagnosticChain, Map)
     */
    public boolean validateNavigablePropertyRedefinition(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateRedefinedPropertyInherited(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateNavigableReadonly(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNavigableReadonly(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateNavigableReadonly(
            arg0,
            arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @return this.property.validateHasQualifiedName(arg0, arg1)
     * @see org.eclipse.uml2.uml.Property#validateHasQualifiedName(DiagnosticChain, Map)
     */
    public boolean validateNoName(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        // TODO: validateHasQualifiedName == validateNoName
        return this.property.validateHasQualifiedName(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNotOwnSelf(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateNotOwnSelf(
            arg0,
            arg1);
    }

    // TODO: Map validateOppositeIsOtherEnd
    /*public boolean validateOppositeIsOtherEnd(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateOppositeIsOtherEnd(
            arg0,
            arg1);
    }*/

    /**
     * @param arg0
     * @param arg1
     * @return this.property.validateHasQualifiedName(arg0, arg1)
     * @see org.eclipse.uml2.uml.Property#validateHasQualifiedName(DiagnosticChain, Map)
     */
    public boolean validateQualifiedName(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateHasQualifiedName(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#validateRedefinitionConsistent(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateRedefinitionConsistent(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateRedefinitionConsistent(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#validateRedefinitionContextValid(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateRedefinitionContextValid(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateRedefinitionContextValid(
            arg0,
            arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @return this.property.validateSubsettingContextConforms(arg0, arg1)
     * @see org.eclipse.uml2.uml.Property#validateSubsettingContextConforms(DiagnosticChain, Map)
     */
    public boolean validateSubsettingContext(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateSubsettingContextConforms(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateSubsettingRules(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateSubsettingRules(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateSubsettingRules(
            arg0,
            arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @return this.property.validateUpperGeLower(arg0, arg1)
     * @see org.eclipse.uml2.uml.Property#validateUpperGeLower(DiagnosticChain, Map)
     */
    public boolean validateUpperEqUpperbound(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateUpperGeLower(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#validateUpperGeLower(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateUpperGeLower(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateUpperGeLower(
            arg0,
            arg1);
    }

    /** UML2 3.0: property.validateUpperGt0 no longer exists
     * @param arg0 
     * @param arg1 
     * @return property.getUpper() > 0
     */
    public boolean validateUpperGt0(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.getUpper() > 0;
        /* return this.property.validateUpperGt0(
            arg0,
            arg1); */
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateVisibilityNeedsOwnership(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateVisibilityNeedsOwnership(
        final DiagnosticChain arg0,
        final Map<Object, Object> arg1)
    {
        return this.property.validateVisibilityNeedsOwnership(
            arg0,
            arg1);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#createDefaultValue(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createDefaultValue(String name, Type type, EClass eClass)
    {
        return this.property.createLowerValue(name, type, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#createQualifier(String, org.eclipse.uml2.uml.Type)
     */
    public Property createQualifier(String name, Type type)
    {
        return this.property.createQualifier(name, type);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#createQualifier(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public Property createQualifier(String name, Type type, EClass eClass)
    {
        return this.property.createQualifier(name, type, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getOtherEnd()
     */
    public Property getOtherEnd()
    {
        return this.property.getOtherEnd();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getQualifier(String, org.eclipse.uml2.uml.Type)
     */
    public Property getQualifier(String name, Type type)
    {
        return this.property.getQualifier(name, type);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getQualifier(String, org.eclipse.uml2.uml.Type, boolean, org.eclipse.emf.ecore.EClass, boolean)
     */
    public Property getQualifier(String name, Type type, boolean ignoreCase, EClass eClass,
            boolean createOnDemand)
    {
        return this.property.getQualifier(name, type, ignoreCase, eClass, createOnDemand);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getRedefinedProperty(String, org.eclipse.uml2.uml.Type)
     */
    public Property getRedefinedProperty(String name, Type type)
    {
        return this.property.getRedefinedProperty(name, type);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getRedefinedProperty(String, org.eclipse.uml2.uml.Type, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Property getRedefinedProperty(String name, Type type, boolean ignoreCase, EClass eClass)
    {
        return this.property.getRedefinedProperty(name, type, ignoreCase, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getSubsettedProperty(String, org.eclipse.uml2.uml.Type)
     */
    public Property getSubsettedProperty(String name, Type type)
    {
        return this.property.getSubsettedProperty(name, type);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#getSubsettedProperty(String, org.eclipse.uml2.uml.Type, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Property getSubsettedProperty(String name, Type type, boolean ignoreCase, EClass eClass)
    {
        return this.property.getSubsettedProperty(name, type, ignoreCase, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#isAttribute(org.eclipse.uml2.uml.Property)
     */
    public boolean isAttribute(Property p)
    {
        return this.property.isAttribute(p);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#isSetDefault()
     */
    public boolean isSetDefault()
    {
        return this.property.isSetDefault();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setBooleanDefaultValue(boolean)
     */
    public void setBooleanDefaultValue(boolean value)
    {
        this.property.setBooleanDefaultValue(value);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setDefault(String)
     */
    public void setDefault(String value)
    {
        this.property.setDefault(value);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setIntegerDefaultValue(int)
     */
    public void setIntegerDefaultValue(int value)
    {
        this.property.setIntegerDefaultValue(value);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setIsComposite(boolean)
     */
    public void setIsComposite(boolean value)
    {
        this.property.setIsComposite(value);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setIsNavigable(boolean)
     */
    public void setIsNavigable(boolean isNavigable)
    {
        this.property.setIsNavigable(isNavigable);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setNullDefaultValue()
     */
    public void setNullDefaultValue()
    {
        this.property.setNullDefaultValue();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setOpposite(org.eclipse.uml2.uml.Property)
     */
    public void setOpposite(Property value)
    {
        this.property.setOpposite(value);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setStringDefaultValue(String)
     */
    public void setStringDefaultValue(String value)
    {
        this.property.setStringDefaultValue(value);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#setUnlimitedNaturalDefaultValue(int)
     */
    public void setUnlimitedNaturalDefaultValue(int value)
    {
        this.property.setUnlimitedNaturalDefaultValue(value);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#unsetDefault()
     */
    public void unsetDefault()
    {
        this.property.unsetDefault();
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateBindingToAttribute(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateBindingToAttribute(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.property.validateBindingToAttribute(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateDeploymentTarget(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDeploymentTarget(DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return this.property.validateDeploymentTarget(diagnostics, context);
    }


    /**
     * @see org.eclipse.uml2.uml.Property#validateDerivedUnionIsReadOnly(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDerivedUnionIsReadOnly(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.property.validateDerivedUnionIsDerived(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateRedefinedPropertyInherited(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateRedefinedPropertyInherited(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.property.validateRedefinedPropertyInherited(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateSubsettedPropertyNames(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateSubsettedPropertyNames(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.property.validateSubsettedPropertyNames(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Property#validateSubsettingContextConforms(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateSubsettingContextConforms(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.property.validateSubsettingContextConforms(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Feature#getFeaturingClassifier(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Classifier getFeaturingClassifier(String name, boolean ignoreCase, EClass eClass)
    {
        return this.property.getFeaturingClassifier(name, ignoreCase, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#getRedefinedElement(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public RedefinableElement getRedefinedElement(String name, boolean ignoreCase, EClass eClass)
    {
        return this.property.getRedefinedElement(name, ignoreCase, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.RedefinableElement#getRedefinitionContext(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Classifier getRedefinitionContext(String name, boolean ignoreCase, EClass eClass)
    {
        return this.property.getRedefinitionContext(name, ignoreCase, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#allOwningPackages()
     */
    public EList<Package> allOwningPackages()
    {
        return this.property.allOwningPackages();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createNameExpression(String, org.eclipse.uml2.uml.Type)
     */
    public StringExpression createNameExpression(String name, Type type)
    {
        return this.property.createNameExpression(name, type);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createUsage(org.eclipse.uml2.uml.NamedElement)
     */
    public Usage createUsage(NamedElement supplier)
    {
        return this.property.createUsage(supplier);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependency(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Dependency getClientDependency(String name, boolean ignoreCase, EClass eClass)
    {
        return this.property.getClientDependency(name, ignoreCase, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isSetName()
     */
    public boolean isSetName()
    {
        return this.property.isSetName();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isSetVisibility()
     */
    public boolean isSetVisibility()
    {
        return this.property.isSetVisibility();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#unsetName()
     */
    public void unsetName()
    {
        this.property.unsetName();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#unsetVisibility()
     */
    public void unsetVisibility()
    {
        this.property.unsetVisibility();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateHasNoQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasNoQualifiedName(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.property.validateHasNoQualifiedName(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateHasQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return this.property.validateHasQualifiedName(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#applyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject applyStereotype(Stereotype stereotype)
    {
        return this.property.applyStereotype(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotype(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Stereotype getAppliedSubstereotype(Stereotype stereotype, String qualifiedName)
    {
        return this.property.getAppliedSubstereotype(stereotype, qualifiedName);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotypes(org.eclipse.uml2.uml.Stereotype)
     */
    public EList<Stereotype> getAppliedSubstereotypes(Stereotype stereotype)
    {
        return this.property.getAppliedStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships()
     */
    public EList<Relationship> getRelationships()
    {
        return this.property.getRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<Relationship> getRelationships(EClass eClass)
    {
        return this.property.getRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotype(String)
     */
    public Stereotype getRequiredStereotype(String qualifiedName)
    {
        return this.property.getRequiredStereotype(qualifiedName);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotypes()
     */
    public EList<Stereotype> getRequiredStereotypes()
    {
        return this.property.getRequiredStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships()
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships()
    {
        return this.property.getSourceDirectedRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships(EClass eClass)
    {
        return this.property.getSourceDirectedRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplication(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject getStereotypeApplication(Stereotype stereotype)
    {
        return this.property.getStereotypeApplication(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplications()
     */
    public EList<EObject> getStereotypeApplications()
    {
        return this.property.getStereotypeApplications();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships()
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships()
    {
        return this.property.getTargetDirectedRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships(EClass eClass)
    {
        return this.property.getTargetDirectedRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplicable(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplicable(Stereotype stereotype)
    {
        return this.property.isStereotypeApplicable(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplied(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplied(Stereotype stereotype)
    {
        return this.property.isStereotypeApplied(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeRequired(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeRequired(Stereotype stereotype)
    {
        return this.property.isStereotypeRequired(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#unapplyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject unapplyStereotype(Stereotype stereotype)
    {
        return this.property.unapplyStereotype(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#compatibleWith(org.eclipse.uml2.uml.MultiplicityElement)
     */
    public boolean compatibleWith(MultiplicityElement other)
    {
        return this.property.compatibleWith(other);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#createLowerValue(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createLowerValue(String name, Type type, EClass eClass)
    {
        return this.property.createLowerValue(name, type, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#createUpperValue(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createUpperValue(String name, Type type, EClass eClass)
    {
        return this.property.createUpperValue(name, type, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#is(int, int)
     */
    public boolean is(int lowerbound, int upperbound)
    {
        return this.property.is(lowerbound, upperbound);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#setLower(int)
     */
    public void setLower(int value)
    {
        this.property.setLower(value);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#setUpper(int)
     */
    public void setUpper(int value)
    {
        this.property.setUpper(value);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#validateValueSpecificationConstant(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateValueSpecificationConstant(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.property.validateValueSpecificationConstant(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.MultiplicityElement#validateValueSpecificationNoSideEffects(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateValueSpecificationNoSideEffects(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.property.validateValueSpecificationNoSideEffects(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#getOwningTemplateParameter()
     */
    public TemplateParameter getOwningTemplateParameter()
    {
        return this.property.getOwningTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#isCompatibleWith(org.eclipse.uml2.uml.ParameterableElement)
     */
    public boolean isCompatibleWith(ParameterableElement p)
    {
        return this.property.isCompatibleWith(p);
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#isTemplateParameter()
     */
    public boolean isTemplateParameter()
    {
        return this.property.isTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#createDeployment(String)
     */
    public Deployment createDeployment(String name)
    {
        return this.property.createDeployment(name);
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElement(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public PackageableElement getDeployedElement(String name, boolean ignoreCase, EClass eClass)
    {
        return this.property.getDeployedElement(name, ignoreCase, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployment(String, boolean, boolean)
     */
    public Deployment getDeployment(String name, boolean ignoreCase, boolean createOnDemand)
    {
        return this.property.getDeployment(name, ignoreCase, createOnDemand);
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @param signature 
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#createTemplateBinding(org.eclipse.uml2.uml.TemplateSignature)
     */
    public TemplateBinding createTemplateBinding(TemplateSignature signature)
    {
        //return this.property.createTemplateBinding(signature);
        logger.error("AttributeImpl.property.createTemplateBinding(TemplateSignature) is UML14 only");
        return null;
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @param signature 
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#getTemplateBinding(org.eclipse.uml2.uml.TemplateSignature)
     */
    public TemplateBinding getTemplateBinding(TemplateSignature signature)
    {
        //return this.property.getTemplateBinding(signature);
        logger.error("AttributeImpl.property.getTemplateBinding(TemplateSignature) is UML14 only");
        return null;
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @param signature 
     * @param createOnDemand 
     * @return null
     * @see org.eclipse.uml2.uml.TemplateableElement#getTemplateBinding(org.eclipse.uml2.uml.TemplateSignature, boolean)
     */
    public TemplateBinding getTemplateBinding(TemplateSignature signature, boolean createOnDemand)
    {
        //return this.property.getTemplateBinding(signature, createOnDemand);
        logger.error("AttributeImpl.property.getTemplateBinding(TemplateSignature, boolean) is UML14 only");
        return null;
    }

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * @return false
     * @see org.eclipse.uml2.uml.TemplateableElement#isTemplate()
     */
    public boolean isTemplate()
    {
        //return this.property.isTemplate();
        logger.error("AttributeImpl.property.isTemplate() is UML14 only");
        return false;
    }
}
