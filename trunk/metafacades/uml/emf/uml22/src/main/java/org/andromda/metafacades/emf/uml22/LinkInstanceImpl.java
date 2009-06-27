package org.andromda.metafacades.emf.uml22;

import java.util.Map;
import org.eclipse.emf.common.notify.Adapter;
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
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.ParameterableElement;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;



/**
 * @author RJF3
 */
public class LinkInstanceImpl implements LinkInstance
{
    final InstanceSpecification instanceSpecification;

    LinkInstanceImpl(InstanceSpecification instanceSpecificationIn)
    {
        this.instanceSpecification = instanceSpecificationIn;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ObjectInstanceImpl)
        {
            return this.instanceSpecification.equals(((ObjectInstanceImpl)object).instanceSpecification);
        }
        if (object instanceof LinkInstanceImpl)
        {
            return this.instanceSpecification.equals(((LinkInstanceImpl)object).instanceSpecification);
        }
        return this.instanceSpecification.equals(object);
    }

    @Override
    public int hashCode()
    {
        return this.instanceSpecification.hashCode();
    }

    @Override
    public String toString()
    {
        return this.getClass().getName() + '[' + this.instanceSpecification.toString() + ']';
    }

    public EList<Deployment> getDeployments()
    {
        return this.instanceSpecification.getDeployments();
    }

    public Deployment getDeployment(String string, boolean ignoreCase, boolean createOnDemand)
    {
        return this.instanceSpecification.getDeployment(string, ignoreCase, createOnDemand);
    }

    public Deployment getDeployment(String string)
    {
        return this.instanceSpecification.getDeployment(string);
    }

    public Deployment createDeployment(EClass eClass)
    {
        return this.instanceSpecification.createDeployment(eClass.getName());
    }

    public Deployment createDeployment(String eClass)
    {
        return this.instanceSpecification.createDeployment(eClass);
    }

    public Deployment createDeployment()
    {
        return this.instanceSpecification.createDeployment(null);
    }

    public EList<PackageableElement> getDeployedElements()
    {
        return this.instanceSpecification.getDeployedElements();
    }

    public PackageableElement getDeployedElement(String string)
    {
        return this.instanceSpecification.getDeployedElement(string);
    }

    public PackageableElement getDeployedElement(String string, boolean bool, EClass eClass)
    {
        return this.instanceSpecification.getDeployedElement(string, bool, eClass);
    }

    public EList<Slot> getSlots()
    {
        return this.instanceSpecification.getSlots();
    }

    public Slot createSlot(EClass eClass)
    {
        return this.instanceSpecification.createSlot();
    }

    public Slot createSlot()
    {
        return this.instanceSpecification.createSlot();
    }

    public EList<Classifier> getClassifiers()
    {
        return this.instanceSpecification.getClassifiers();
    }

    public Classifier getClassifier(String string)
    {
        return this.instanceSpecification.getClassifier(string);
    }

    public Classifier getClassifier(String string, boolean ignoreCase, EClass eClass)
    {
        return this.instanceSpecification.getClassifier(string, ignoreCase, eClass);
    }

    public ValueSpecification getSpecification()
    {
        return this.instanceSpecification.getSpecification();
    }

    public void setSpecification(ValueSpecification valueSpecification)
    {
        this.instanceSpecification.setSpecification(valueSpecification);
    }

    public ValueSpecification createSpecification(EClass eClass)
    {
        return this.instanceSpecification.createSpecification(null, null, eClass);
    }

    public boolean validateSlotsAreDefined(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateDefiningFeature(diagnosticChain, map);
    }

    public boolean validateNoDuplicateSlots(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateStructuralFeature(diagnosticChain, map);
    }

    public EList<Dependency> getClientDependencies()
    {
        return this.instanceSpecification.getClientDependencies();
    }

    public Object eGet(EStructuralFeature eStructuralFeature, boolean b)
    {
        return this.instanceSpecification.eGet(eStructuralFeature, b);
    }

    public void eSet(EStructuralFeature eStructuralFeature, Object object)
    {
        this.instanceSpecification.eSet(eStructuralFeature, object);
    }

    public void eUnset(EStructuralFeature eStructuralFeature)
    {
        this.instanceSpecification.eUnset(eStructuralFeature);
    }

    public boolean eIsSet(EStructuralFeature eStructuralFeature)
    {
        return this.instanceSpecification.eIsSet(eStructuralFeature);
    }

    public TemplateParameter getTemplateParameter()
    {
        return this.instanceSpecification.getTemplateParameter();
    }

    public void setTemplateParameter(TemplateParameter templateParameter)
    {
        this.instanceSpecification.setTemplateParameter(templateParameter);
    }

    public TemplateParameter getOwningParameter()
    {
        return this.instanceSpecification.getOwningTemplateParameter();
    }

    public void setOwningParameter(TemplateParameter templateParameter)
    {
        this.instanceSpecification.setOwningTemplateParameter(templateParameter);
    }

    public VisibilityKind getPackageableElement_visibility()
    {
        return this.instanceSpecification.getVisibility();
    }

    public void setPackageableElement_visibility(VisibilityKind visibilityKind)
    {
        this.instanceSpecification.setVisibility(visibilityKind);
    }

    public VisibilityKind getVisibility()
    {
        return this.instanceSpecification.getVisibility();
    }

    public void setVisibility(VisibilityKind visibilityKind)
    {
        this.instanceSpecification.setVisibility(visibilityKind);
    }

    public String getName()
    {
        return this.instanceSpecification.getName();
    }

    public void setName(String string)
    {
        this.instanceSpecification.setName(string);
    }

    public String getQualifiedName()
    {
        return this.instanceSpecification.getQualifiedName();
    }

    public Dependency getClientDependency(String string)
    {
        return this.instanceSpecification.getClientDependency(string);
    }

    public Dependency getClientDependency(String string, boolean ignoreCase, EClass eClass)
    {
        return this.instanceSpecification.getClientDependency(string, ignoreCase, eClass);
    }

    public StringExpression getNameExpression()
    {
        return this.instanceSpecification.getNameExpression();
    }

    public void setNameExpression(StringExpression stringExpression)
    {
        this.instanceSpecification.setNameExpression(stringExpression);
    }

    public StringExpression createNameExpression(EClass eClass)
    {
        return this.instanceSpecification.createNameExpression(eClass.getName(), null);
    }

    public StringExpression createNameExpression(String name, EClass eClass)
    {
        return this.instanceSpecification.createNameExpression(name, null);
    }

    public StringExpression createNameExpression()
    {
        return this.instanceSpecification.createNameExpression(null, null);
    }

    public boolean validateNoName(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasNoQualifiedName(diagnosticChain, map);
    }

    public boolean validateHasNoQualifiedName(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasNoQualifiedName(diagnosticChain, map);
    }

    public boolean validateQualifiedName(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasQualifiedName(diagnosticChain, map);
    }

    public boolean validateHasQualifiedName(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasQualifiedName(diagnosticChain, map);
    }

    public EList<Namespace> allNamespaces()
    {
        return this.instanceSpecification.allNamespaces();
    }

    public boolean isDistinguishableFrom(NamedElement namedElement, Namespace namespace)
    {
        return this.instanceSpecification.isDistinguishableFrom(namedElement, namespace);
    }

    public String separator()
    {
        return this.instanceSpecification.separator();
    }

    public String qualifiedName()
    {
        return this.instanceSpecification.getQualifiedName();
    }

    public boolean validateVisibilityNeedsOwnership(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateVisibilityNeedsOwnership(diagnosticChain, map);
    }

    public Namespace getNamespace()
    {
        return this.instanceSpecification.getNamespace();
    }

    public String getLabel()
    {
        return this.instanceSpecification.getLabel();
    }

    public String getLabel(boolean b)
    {
        return this.instanceSpecification.getLabel(b);
    }

    public Dependency createDependency(NamedElement namedElement)
    {
        return this.instanceSpecification.createDependency(namedElement);
    }

    /*public EList getTemplateBindings()
    {
        return this.instanceSpecification.getTemplateBindings();
    }

    public TemplateBinding createTemplateBinding(EClass eClass)
    {
        return this.instanceSpecification.createTemplateBinding(eClass);
    }

    public TemplateBinding createTemplateBinding()
    {
        return this.instanceSpecification.createTemplateBinding();
    }

    public TemplateSignature getOwnedTemplateSignature()
    {
        return this.instanceSpecification.getOwnedTemplateSignature();
    }

    public void setOwnedTemplateSignature(TemplateSignature templateSignature)
    {
        this.instanceSpecification.setOwnedTemplateSignature(templateSignature);
    }

    public TemplateSignature createOwnedTemplateSignature(EClass eClass)
    {
        return this.instanceSpecification.createOwnedTemplateSignature(eClass);
    }

    public TemplateSignature createOwnedTemplateSignature()
    {
        return this.instanceSpecification.createOwnedTemplateSignature();
    }

    public Set parameterableElements()
    {
        return this.instanceSpecification.parameterableElements();
    }*/

    public EList<Element> getOwnedElements()
    {
        return this.instanceSpecification.getOwnedElements();
    }

    public Element getOwner()
    {
        return this.instanceSpecification.getOwner();
    }

    public EList<Comment> getOwnedComments()
    {
        return this.instanceSpecification.getOwnedComments();
    }

    public Comment createOwnedComment(EClass eClass)
    {
        return this.instanceSpecification.createOwnedComment();
    }

    public Comment createOwnedComment()
    {
        return this.instanceSpecification.createOwnedComment();
    }

    public boolean validateNotOwnSelf(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateNotOwnSelf(diagnosticChain, map);
    }

    public boolean validateHasOwner(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasOwner(diagnosticChain, map);
    }

    public EList<Element> allOwnedElements()
    {
        return this.instanceSpecification.allOwnedElements();
    }

    public boolean mustBeOwned()
    {
        return this.instanceSpecification.mustBeOwned();
    }

    public EAnnotation createEAnnotation(String string)
    {
        return this.instanceSpecification.createEAnnotation(string);
    }

    public EObject apply(Stereotype stereotype)
    {
        return this.applyStereotype(stereotype);
    }

    public EObject applyStereotype(Stereotype stereotype)
    {
        return this.instanceSpecification.applyStereotype(stereotype);
    }

    public Stereotype getApplicableStereotype(String string)
    {
        return this.instanceSpecification.getApplicableStereotype(string);
    }

    public EList<Stereotype> getApplicableStereotypes()
    {
        return this.instanceSpecification.getApplicableStereotypes();
    }

    public Stereotype getAppliedStereotype(String string)
    {
        return this.instanceSpecification.getAppliedStereotype(string);
    }

    public EList<Stereotype> getAppliedStereotypes()
    {
        return this.instanceSpecification.getAppliedStereotypes();
    }

    public Model getModel()
    {
        return (Model) UmlUtilities.findModel(this.instanceSpecification);
    }

    public Package getNearestPackage()
    {
        return this.instanceSpecification.getNearestPackage();
    }

    public Object getValue(Stereotype stereotype, String string)
    {
        return this.instanceSpecification.getValue(stereotype, string);
    }

    public boolean isApplied(Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeApplied(stereotype);
    }

    public boolean isRequired(Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeRequired(stereotype);
    }

    public void setValue(Stereotype stereotype, String string, Object object)
    {
        this.instanceSpecification.setValue(stereotype, string, object);
    }

    public boolean hasValue(Stereotype stereotype, String string)
    {
        return this.instanceSpecification.hasValue(stereotype, string);
    }

    public EObject unapply(Stereotype stereotype)
    {
        return this.unapplyStereotype(stereotype);
    }

    public EObject unapplyStereotype(Stereotype stereotype)
    {
        return this.instanceSpecification.unapplyStereotype(stereotype);
    }

    public void destroy()
    {
        this.instanceSpecification.destroy();
    }

    /*public String getAppliedVersion(Stereotype stereotype)
    {
        return this.instanceSpecification.getAppliedVersion(stereotype);
    }*/

    public boolean addKeyword(String string)
    {
        return this.instanceSpecification.addKeyword(string);
    }

    public EList<String> getKeywords()
    {
        return this.instanceSpecification.getKeywords();
    }

    public boolean hasKeyword(String string)
    {
        return this.instanceSpecification.hasKeyword(string);
    }

    public boolean removeKeyword(String string)
    {
        return this.instanceSpecification.removeKeyword(string);
    }

    public EList<EAnnotation> getEAnnotations()
    {
        return this.instanceSpecification.getEAnnotations();
    }

    public EAnnotation getEAnnotation(String string)
    {
        return this.instanceSpecification.getEAnnotation(string);
    }

    public EList<Adapter> eAdapters()
    {
        return this.instanceSpecification.eAdapters();
    }

    public boolean eDeliver()
    {
        return this.instanceSpecification.eDeliver();
    }

    public void eSetDeliver(boolean b)
    {
        this.instanceSpecification.eSetDeliver(b);
    }

    public boolean eIsProxy()
    {
        return this.instanceSpecification.eIsProxy();
    }

    public EClass eClass()
    {
        return this.instanceSpecification.eClass();
    }

    public EObject eContainer()
    {
        return this.instanceSpecification.eContainer();
    }

    public EList<EObject> eContents()
    {
        return this.instanceSpecification.eContents();
    }

    public EList<EObject> eCrossReferences()
    {
        return this.instanceSpecification.eCrossReferences();
    }

    public TreeIterator<EObject> eAllContents()
    {
        return this.instanceSpecification.eAllContents();
    }

    public EReference eContainmentFeature()
    {
        return this.instanceSpecification.eContainmentFeature();
    }

    public EStructuralFeature eContainingFeature()
    {
        return this.instanceSpecification.eContainingFeature();
    }

    public Resource eResource()
    {
        return this.instanceSpecification.eResource();
    }

    public Object eGet(EStructuralFeature eStructuralFeature)
    {
        return this.instanceSpecification.eGet(eStructuralFeature);
    }

    public void eNotify(Notification notification)
    {
        this.instanceSpecification.eNotify(notification);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#createSpecification(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createSpecification(String name, Type type, EClass eClass)
    {
        return this.instanceSpecification.createSpecification(name, type, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDefiningFeature(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDefiningFeature(DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return this.instanceSpecification.validateDefiningFeature(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDeploymentArtifact(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDeploymentArtifact(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.instanceSpecification.validateDeploymentArtifact(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDeploymentTarget(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDeploymentTarget(DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        return this.instanceSpecification.validateDeploymentTarget(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateStructuralFeature(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateStructuralFeature(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        return this.instanceSpecification.validateStructuralFeature(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#allOwningPackages()
     */
    public EList<Package> allOwningPackages()
    {
        return this.instanceSpecification.allOwningPackages();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createNameExpression(String, org.eclipse.uml2.uml.Type)
     */
    public StringExpression createNameExpression(String name, Type type)
    {
        return this.instanceSpecification.createNameExpression(name, type);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createUsage(org.eclipse.uml2.uml.NamedElement)
     */
    public Usage createUsage(NamedElement supplier)
    {
        return this.instanceSpecification.createUsage(supplier);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isSetName()
     */
    public boolean isSetName()
    {
        return this.instanceSpecification.isSetName();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isSetVisibility()
     */
    public boolean isSetVisibility()
    {
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#unsetName()
     */
    public void unsetName()
    {
        this.instanceSpecification.unsetName();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#unsetVisibility()
     */
    public void unsetVisibility()
    {
        this.instanceSpecification.unsetVisibility();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotype(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Stereotype getAppliedSubstereotype(Stereotype stereotype, String qualifiedName)
    {
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotypes(org.eclipse.uml2.uml.Stereotype)
     */
    public EList<Stereotype> getAppliedSubstereotypes(Stereotype stereotype)
    {
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships()
     */
    public EList<Relationship> getRelationships()
    {
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<Relationship> getRelationships(EClass class1)
    {
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotype(String)
     */
    public Stereotype getRequiredStereotype(String qualifiedName)
    {
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotypes()
     */
    public EList<Stereotype> getRequiredStereotypes()
    {
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships()
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships()
    {
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships(EClass eClass)
    {
        return this.instanceSpecification.getSourceDirectedRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplication(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject getStereotypeApplication(Stereotype stereotype)
    {
        return this.instanceSpecification.getStereotypeApplication(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplications()
     */
    public EList<EObject> getStereotypeApplications()
    {
        return this.instanceSpecification.getStereotypeApplications();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships()
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships()
    {
        return this.instanceSpecification.getTargetDirectedRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships(EClass eClass)
    {
        return this.instanceSpecification.getTargetDirectedRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplicable(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplicable(Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeApplicable(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplied(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplied(Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeApplied(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeRequired(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeRequired(Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeRequired(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#getOwningTemplateParameter()
     */
    public TemplateParameter getOwningTemplateParameter()
    {
        return this.instanceSpecification.getTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#isCompatibleWith(org.eclipse.uml2.uml.ParameterableElement)
     */
    public boolean isCompatibleWith(ParameterableElement p)
    {
        return this.instanceSpecification.isCompatibleWith(p);
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#isTemplateParameter()
     */
    public boolean isTemplateParameter()
    {
        return this.instanceSpecification.isTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#setOwningTemplateParameter(org.eclipse.uml2.uml.TemplateParameter)
     */
    public void setOwningTemplateParameter(TemplateParameter value)
    {
        this.instanceSpecification.setOwningTemplateParameter(value);
    }
}
