package org.andromda.metafacades.emf.uml2;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.Dependency;
import org.eclipse.uml2.Deployment;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.InstanceSpecification;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Namespace;
import org.eclipse.uml2.PackageableElement;
import org.eclipse.uml2.Slot;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.StringExpression;
import org.eclipse.uml2.TemplateBinding;
import org.eclipse.uml2.TemplateParameter;
import org.eclipse.uml2.TemplateSignature;
import org.eclipse.uml2.ValueSpecification;
import org.eclipse.uml2.VisibilityKind;

/**
 *
 */
public class ObjectInstanceImpl implements ObjectInstance
{
    /**
     *
     */
    final InstanceSpecification instanceSpecification;

    /**
     * @param instanceSpecification
     */
    ObjectInstanceImpl(InstanceSpecification instanceSpecification)
    {
        this.instanceSpecification = instanceSpecification;
    }

    /**
     * @see Object#equals(Object)
     */
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

    /**
     * @see Object#hashCode()
     */
    public int hashCode()
    {
        return this.instanceSpecification.hashCode();
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return this.getClass().getName() + '[' + this.instanceSpecification.toString() + ']';
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#getDeployments()
     */
    public EList getDeployments()
    {
        return this.instanceSpecification.getDeployments();
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#getDeployment(String)
     */
    public Deployment getDeployment(String string)
    {
        return this.instanceSpecification.getDeployment(string);
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#createDeployment(org.eclipse.emf.ecore.EClass)
     * @deprecated
     */
    public Deployment createDeployment(EClass eClass)
    {
        return this.instanceSpecification.createDeployment(eClass);
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#createDeployment()
     */
    public Deployment createDeployment()
    {
        return this.instanceSpecification.createDeployment();
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#getDeployedElements()
     */
    public EList getDeployedElements()
    {
        return this.instanceSpecification.getDeployedElements();
    }

    /**
     * @see org.eclipse.uml2.DeploymentTarget#getDeployedElement(String)
     */
    public PackageableElement getDeployedElement(String string)
    {
        return this.instanceSpecification.getDeployedElement(string);
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#getSlots()
     */
    public EList getSlots()
    {
        return this.instanceSpecification.getSlots();
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#createSlot(org.eclipse.emf.ecore.EClass)
     * @deprecated
     */
    public Slot createSlot(EClass eClass)
    {
        return this.instanceSpecification.createSlot(eClass);
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#createSlot()
     */
    public Slot createSlot()
    {
        return this.instanceSpecification.createSlot();
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#getClassifiers()
     */
    public EList getClassifiers()
    {
        return this.instanceSpecification.getClassifiers();
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#getClassifier(String)
     */
    public Classifier getClassifier(String string)
    {
        return this.instanceSpecification.getClassifier(string);
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#getSpecification()
     */
    public ValueSpecification getSpecification()
    {
        return this.instanceSpecification.getSpecification();
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#setSpecification(org.eclipse.uml2.ValueSpecification)
     */
    public void setSpecification(ValueSpecification valueSpecification)
    {
        this.instanceSpecification.setSpecification(valueSpecification);
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#createSpecification(org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createSpecification(EClass eClass)
    {
        return this.instanceSpecification.createSpecification(eClass);
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#validateSlotsAreDefined(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateSlotsAreDefined(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateSlotsAreDefined(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.InstanceSpecification#validateNoDuplicateSlots(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNoDuplicateSlots(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateNoDuplicateSlots(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getClientDependencies()
     */
    public EList getClientDependencies()
    {
        return this.instanceSpecification.getClientDependencies();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature, boolean)
     */
    public Object eGet(EStructuralFeature eStructuralFeature, boolean b)
    {
        return this.instanceSpecification.eGet(eStructuralFeature, b);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature, Object)
     */
    public void eSet(EStructuralFeature eStructuralFeature, Object object)
    {
        this.instanceSpecification.eSet(eStructuralFeature, object);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public void eUnset(EStructuralFeature eStructuralFeature)
    {
        this.instanceSpecification.eUnset(eStructuralFeature);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public boolean eIsSet(EStructuralFeature eStructuralFeature)
    {
        return this.instanceSpecification.eIsSet(eStructuralFeature);
    }

    /**
     * @see org.eclipse.uml2.ParameterableElement#getTemplateParameter()
     */
    public TemplateParameter getTemplateParameter()
    {
        return this.instanceSpecification.getTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.ParameterableElement#setTemplateParameter(org.eclipse.uml2.TemplateParameter)
     */
    public void setTemplateParameter(TemplateParameter templateParameter)
    {
        this.instanceSpecification.setTemplateParameter(templateParameter);
    }

    /**
     * @see org.eclipse.uml2.ParameterableElement#getOwningParameter()
     */
    public TemplateParameter getOwningParameter()
    {
        return this.instanceSpecification.getOwningParameter();
    }

    /**
     * @see org.eclipse.uml2.ParameterableElement#setOwningParameter(org.eclipse.uml2.TemplateParameter)
     */
    public void setOwningParameter(TemplateParameter templateParameter)
    {
        this.instanceSpecification.setOwningParameter(templateParameter);
    }

    /**
     * @see org.eclipse.uml2.PackageableElement#getPackageableElement_visibility()
     */
    public VisibilityKind getPackageableElement_visibility()
    {
        return this.instanceSpecification.getPackageableElement_visibility();
    }

    /**
     * @see org.eclipse.uml2.PackageableElement#setPackageableElement_visibility(org.eclipse.uml2.VisibilityKind)
     */
    public void setPackageableElement_visibility(VisibilityKind visibilityKind)
    {
        this.instanceSpecification.setPackageableElement_visibility(visibilityKind);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getVisibility()
     */
    public VisibilityKind getVisibility()
    {
        return this.instanceSpecification.getVisibility();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#setVisibility(org.eclipse.uml2.VisibilityKind)
     */
    public void setVisibility(VisibilityKind visibilityKind)
    {
        this.instanceSpecification.setVisibility(visibilityKind);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getName()
     */
    public String getName()
    {
        return this.instanceSpecification.getName();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#setName(String)
     */
    public void setName(String string)
    {
        this.instanceSpecification.setName(string);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getQualifiedName()
     */
    public String getQualifiedName()
    {
        return this.instanceSpecification.getQualifiedName();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getClientDependency(String)
     */
    public Dependency getClientDependency(String string)
    {
        return this.instanceSpecification.getClientDependency(string);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getNameExpression()
     */
    public StringExpression getNameExpression()
    {
        return this.instanceSpecification.getNameExpression();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#setNameExpression(org.eclipse.uml2.StringExpression)
     */
    public void setNameExpression(StringExpression stringExpression)
    {
        this.instanceSpecification.setNameExpression(stringExpression);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#createNameExpression(org.eclipse.emf.ecore.EClass)
     * @deprecated
     */
    public StringExpression createNameExpression(EClass eClass)
    {
        return this.instanceSpecification.createNameExpression(eClass);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#createNameExpression()
     */
    public StringExpression createNameExpression()
    {
        return this.instanceSpecification.createNameExpression();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#validateNoName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNoName(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateNoName(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#validateQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateQualifiedName(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateQualifiedName(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#allNamespaces()
     */
    public List allNamespaces()
    {
        return this.instanceSpecification.allNamespaces();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#isDistinguishableFrom(org.eclipse.uml2.NamedElement, org.eclipse.uml2.Namespace)
     */
    public boolean isDistinguishableFrom(NamedElement namedElement, Namespace namespace)
    {
        return this.instanceSpecification.isDistinguishableFrom(namedElement, namespace);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#separator()
     */
    public String separator()
    {
        return this.instanceSpecification.separator();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#qualifiedName()
     */
    public String qualifiedName()
    {
        return this.instanceSpecification.qualifiedName();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#validateVisibilityNeedsOwnership(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateVisibilityNeedsOwnership(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateVisibilityNeedsOwnership(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getNamespace()
     */
    public Namespace getNamespace()
    {
        return this.instanceSpecification.getNamespace();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getLabel()
     */
    public String getLabel()
    {
        return this.instanceSpecification.getLabel();
    }

    /**
     * @see org.eclipse.uml2.NamedElement#getLabel(boolean)
     */
    public String getLabel(boolean b)
    {
        return this.instanceSpecification.getLabel(b);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#createDependency(org.eclipse.uml2.NamedElement)
     */
    public Dependency createDependency(NamedElement namedElement)
    {
        return this.instanceSpecification.createDependency(namedElement);
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#getTemplateBindings()
     */
    public EList getTemplateBindings()
    {
        return this.instanceSpecification.getTemplateBindings();
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#createTemplateBinding(org.eclipse.emf.ecore.EClass)
     * @deprecated
     */
    public TemplateBinding createTemplateBinding(EClass eClass)
    {
        return this.instanceSpecification.createTemplateBinding(eClass);
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#createTemplateBinding()
     */
    public TemplateBinding createTemplateBinding()
    {
        return this.instanceSpecification.createTemplateBinding();
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#getOwnedTemplateSignature()
     */
    public TemplateSignature getOwnedTemplateSignature()
    {
        return this.instanceSpecification.getOwnedTemplateSignature();
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#setOwnedTemplateSignature(org.eclipse.uml2.TemplateSignature)
     */
    public void setOwnedTemplateSignature(TemplateSignature templateSignature)
    {
        this.instanceSpecification.setOwnedTemplateSignature(templateSignature);
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#createOwnedTemplateSignature(org.eclipse.emf.ecore.EClass)
     */
    public TemplateSignature createOwnedTemplateSignature(EClass eClass)
    {
        return this.instanceSpecification.createOwnedTemplateSignature(eClass);
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#createOwnedTemplateSignature()
     */
    public TemplateSignature createOwnedTemplateSignature()
    {
        return this.instanceSpecification.createOwnedTemplateSignature();
    }

    /**
     * @see org.eclipse.uml2.TemplateableElement#parameterableElements()
     */
    public Set parameterableElements()
    {
        return this.instanceSpecification.parameterableElements();
    }

    /**
     * @see org.eclipse.uml2.Element#getOwnedElements()
     */
    public EList getOwnedElements()
    {
        return this.instanceSpecification.getOwnedElements();
    }

    /**
     * @see org.eclipse.uml2.Element#getOwner()
     */
    public Element getOwner()
    {
        return this.instanceSpecification.getOwner();
    }

    /**
     * @see org.eclipse.uml2.Element#getOwnedComments()
     */
    public EList getOwnedComments()
    {
        return this.instanceSpecification.getOwnedComments();
    }

    /**
     * @see org.eclipse.uml2.Element#createOwnedComment(org.eclipse.emf.ecore.EClass)
     * @deprecated
     */
    public Comment createOwnedComment(EClass eClass)
    {
        return this.instanceSpecification.createOwnedComment(eClass);
    }

    /**
     * @see org.eclipse.uml2.Element#createOwnedComment()
     */
    public Comment createOwnedComment()
    {
        return this.instanceSpecification.createOwnedComment();
    }

    /**
     * @see org.eclipse.uml2.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNotOwnSelf(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateNotOwnSelf(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasOwner(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateHasOwner(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.Element#allOwnedElements()
     */
    public Set allOwnedElements()
    {
        return this.instanceSpecification.allOwnedElements();
    }

    /**
     * @see org.eclipse.uml2.Element#mustBeOwned()
     */
    public boolean mustBeOwned()
    {
        return this.instanceSpecification.mustBeOwned();
    }

    /**
     * @see org.eclipse.uml2.Element#createEAnnotation(String)
     */
    public EAnnotation createEAnnotation(String string)
    {
        return this.instanceSpecification.createEAnnotation(string);
    }

    /**
     * @see org.eclipse.uml2.Element#apply(org.eclipse.uml2.Stereotype)
     */
    public void apply(Stereotype stereotype)
    {
        this.instanceSpecification.apply(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#getApplicableStereotype(String)
     */
    public Stereotype getApplicableStereotype(String string)
    {
        return this.instanceSpecification.getApplicableStereotype(string);
    }

    /**
     * @see org.eclipse.uml2.Element#getApplicableStereotypes()
     */
    public Set getApplicableStereotypes()
    {
        return this.instanceSpecification.getApplicableStereotypes();
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedStereotype(String)
     */
    public Stereotype getAppliedStereotype(String string)
    {
        return this.instanceSpecification.getAppliedStereotype(string);
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedStereotypes()
     */
    public Set getAppliedStereotypes()
    {
        return this.instanceSpecification.getAppliedStereotypes();
    }

    /**
     * @see org.eclipse.uml2.Element#getModel()
     */
    public Model getModel()
    {
        return this.instanceSpecification.getModel();
    }

    /**
     * @see org.eclipse.uml2.Element#getNearestPackage()
     */
    public org.eclipse.uml2.Package getNearestPackage()
    {
        return this.instanceSpecification.getNearestPackage();
    }

    /**
     * @see org.eclipse.uml2.Element#getValue(org.eclipse.uml2.Stereotype, String)
     */
    public Object getValue(Stereotype stereotype, String string)
    {
        return this.instanceSpecification.getValue(stereotype, string);
    }

    /**
     * @see org.eclipse.uml2.Element#isApplied(org.eclipse.uml2.Stereotype)
     */
    public boolean isApplied(Stereotype stereotype)
    {
        return this.instanceSpecification.isApplied(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#isRequired(org.eclipse.uml2.Stereotype)
     */
    public boolean isRequired(Stereotype stereotype)
    {
        return this.instanceSpecification.isRequired(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#setValue(org.eclipse.uml2.Stereotype, String, Object)
     */
    public void setValue(Stereotype stereotype, String string, Object object)
    {
        this.instanceSpecification.setValue(stereotype, string, object);
    }

    /**
     * @see org.eclipse.uml2.Element#hasValue(org.eclipse.uml2.Stereotype, String)
     */
    public boolean hasValue(Stereotype stereotype, String string)
    {
        return this.instanceSpecification.hasValue(stereotype, string);
    }

    /**
     * @see org.eclipse.uml2.Element#unapply(org.eclipse.uml2.Stereotype)
     */
    public void unapply(Stereotype stereotype)
    {
        this.instanceSpecification.unapply(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#destroy()
     */
    public void destroy()
    {
        this.instanceSpecification.destroy();
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedVersion(org.eclipse.uml2.Stereotype)
     */
    public String getAppliedVersion(Stereotype stereotype)
    {
        return this.instanceSpecification.getAppliedVersion(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#addKeyword(String)
     */
    public void addKeyword(String string)
    {
        this.instanceSpecification.addKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.Element#getKeywords()
     */
    public Set getKeywords()
    {
        return this.instanceSpecification.getKeywords();
    }

    /**
     * @see org.eclipse.uml2.Element#hasKeyword(String)
     */
    public boolean hasKeyword(String string)
    {
        return this.instanceSpecification.hasKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.Element#removeKeyword(String)
     */
    public void removeKeyword(String string)
    {
        this.instanceSpecification.removeKeyword(string);
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotations()
     */
    public EList getEAnnotations()
    {
        return this.instanceSpecification.getEAnnotations();
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotation(String)
     */
    public EAnnotation getEAnnotation(String string)
    {
        return this.instanceSpecification.getEAnnotation(string);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    public EList eAdapters()
    {
        return this.instanceSpecification.eAdapters();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
     */
    public boolean eDeliver()
    {
        return this.instanceSpecification.eDeliver();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
     */
    public void eSetDeliver(boolean b)
    {
        this.instanceSpecification.eSetDeliver(b);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsProxy()
     */
    public boolean eIsProxy()
    {
        return this.instanceSpecification.eIsProxy();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eClass()
     */
    public EClass eClass()
    {
        return this.instanceSpecification.eClass();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainer()
     */
    public EObject eContainer()
    {
        return this.instanceSpecification.eContainer();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContents()
     */
    public EList eContents()
    {
        return this.instanceSpecification.eContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    public EList eCrossReferences()
    {
        return this.instanceSpecification.eCrossReferences();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    public TreeIterator eAllContents()
    {
        return this.instanceSpecification.eAllContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
     */
    public EReference eContainmentFeature()
    {
        return this.instanceSpecification.eContainmentFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
     */
    public EStructuralFeature eContainingFeature()
    {
        return this.instanceSpecification.eContainingFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eResource()
     */
    public Resource eResource()
    {
        return this.instanceSpecification.eResource();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public Object eGet(EStructuralFeature eStructuralFeature)
    {
        return this.instanceSpecification.eGet(eStructuralFeature);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    public void eNotify(Notification notification)
    {
        this.instanceSpecification.eNotify(notification);
    }
}
