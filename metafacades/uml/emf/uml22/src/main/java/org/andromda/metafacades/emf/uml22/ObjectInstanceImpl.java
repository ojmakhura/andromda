package org.andromda.metafacades.emf.uml22;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
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
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ObjectInstanceImpl)
        {
            return this.instanceSpecification.equals(((ObjectInstanceImpl)object).instanceSpecification);
        }
        /*if (object instanceof LinkInstanceImpl)
        {
            return this.instanceSpecification.equals(((LinkInstanceImpl)object).instanceSpecification);
        }*/
        return this.instanceSpecification.equals(object);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.instanceSpecification.hashCode();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getClass().getName() + '[' + this.instanceSpecification.toString() + ']';
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployments()
     */
    public EList getDeployments()
    {
        return this.instanceSpecification.getDeployments();
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployment(String)
     */
    public Deployment getDeployment(String string)
    {
        return this.instanceSpecification.getDeployment(string);
    }

    /*public Deployment createDeployment(EClass eClass)
    {
        return this.instanceSpecification.createDeployment(eClass);
    }

    public Deployment createDeployment()
    {
        return this.instanceSpecification.createDeployment();
    }*/

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElements()
     */
    public EList getDeployedElements()
    {
        return this.instanceSpecification.getDeployedElements();
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElement(String)
     */
    public PackageableElement getDeployedElement(String string)
    {
        return this.instanceSpecification.getDeployedElement(string);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#getSlots()
     */
    public EList getSlots()
    {
        return this.instanceSpecification.getSlots();
    }

    /**
     * @param eClass
     * @return instanceSpecification.createSlot()
     */
    public Slot createSlot(EClass eClass)
    {
        return this.instanceSpecification.createSlot();
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#createSlot()
     */
    public Slot createSlot()
    {
        return this.instanceSpecification.createSlot();
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#getClassifiers()
     */
    public EList getClassifiers()
    {
        return this.instanceSpecification.getClassifiers();
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#getClassifier(String)
     */
    public Classifier getClassifier(String string)
    {
        return this.instanceSpecification.getClassifier(string);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#getSpecification()
     */
    public ValueSpecification getSpecification()
    {
        return this.instanceSpecification.getSpecification();
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#setSpecification(org.eclipse.uml2.uml.ValueSpecification)
     */
    public void setSpecification(ValueSpecification valueSpecification)
    {
        this.instanceSpecification.setSpecification(valueSpecification);
    }

    /**
     * @param eClass
     * @return instanceSpecification.createSpecification(null, null, eClass)
     */
    public ValueSpecification createSpecification(EClass eClass)
    {
        return this.instanceSpecification.createSpecification(null, null, eClass);
    }

    /*public boolean validateSlotsAreDefined(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateSlotsAreDefined(diagnosticChain, map);
    }

    public boolean validateNoDuplicateSlots(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateNoDuplicateSlots(diagnosticChain, map);
    }*/

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependencies()
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
     * @see org.eclipse.uml2.uml.ParameterableElement#getTemplateParameter()
     */
    public TemplateParameter getTemplateParameter()
    {
        return this.instanceSpecification.getTemplateParameter();
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#setTemplateParameter(org.eclipse.uml2.uml.TemplateParameter)
     */
    public void setTemplateParameter(TemplateParameter templateParameter)
    {
        this.instanceSpecification.setTemplateParameter(templateParameter);
    }

    /**
     * @return instanceSpecification.getOwningTemplateParameter()
     */
    public TemplateParameter getOwningParameter()
    {
        return this.instanceSpecification.getOwningTemplateParameter();
    }

    /**
     * @param templateParameter
     */
    public void setOwningParameter(TemplateParameter templateParameter)
    {
        this.instanceSpecification.setOwningTemplateParameter(templateParameter);
    }

    /**
     * @return instanceSpecification.getVisibility()
     */
    public VisibilityKind getPackageableElement_visibility()
    {
        return this.instanceSpecification.getVisibility();
    }

    /**
     * @param visibilityKind
     */
    public void setPackageableElement_visibility(VisibilityKind visibilityKind)
    {
        this.instanceSpecification.setVisibility(visibilityKind);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getVisibility()
     */
    public VisibilityKind getVisibility()
    {
        return this.instanceSpecification.getVisibility();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#setVisibility(org.eclipse.uml2.uml.VisibilityKind)
     */
    public void setVisibility(VisibilityKind visibilityKind)
    {
        this.instanceSpecification.setVisibility(visibilityKind);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getName()
     */
    public String getName()
    {
        return this.instanceSpecification.getName();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#setName(String)
     */
    public void setName(String string)
    {
        this.instanceSpecification.setName(string);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getQualifiedName()
     */
    public String getQualifiedName()
    {
        return this.instanceSpecification.getQualifiedName();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependency(String)
     */
    public Dependency getClientDependency(String string)
    {
        return this.instanceSpecification.getClientDependency(string);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getNameExpression()
     */
    public StringExpression getNameExpression()
    {
        return this.instanceSpecification.getNameExpression();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#setNameExpression(org.eclipse.uml2.uml.StringExpression)
     */
    public void setNameExpression(StringExpression stringExpression)
    {
        this.instanceSpecification.setNameExpression(stringExpression);
    }

    /*public StringExpression createNameExpression(EClass eClass)
    {
        return this.instanceSpecification.createNameExpression(eClass);
    }

    public StringExpression createNameExpression()
    {
        return this.instanceSpecification.createNameExpression();
    }*/

    /**
     * @param diagnosticChain
     * @param map
     * @return !instanceSpecification.validateHasQualifiedName(diagnosticChain, map)
     */
    public boolean validateNoName(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return !this.instanceSpecification.validateHasQualifiedName(diagnosticChain, map);
    }

    /**
     * @param diagnosticChain
     * @param map
     * @return instanceSpecification.validateHasQualifiedName(diagnosticChain, map)
     */
    public boolean validateQualifiedName(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasQualifiedName(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#allNamespaces()
     */
    public EList<Namespace> allNamespaces()
    {
        return this.instanceSpecification.allNamespaces();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isDistinguishableFrom(org.eclipse.uml2.uml.NamedElement, org.eclipse.uml2.uml.Namespace)
     */
    public boolean isDistinguishableFrom(NamedElement namedElement, Namespace namespace)
    {
        return this.instanceSpecification.isDistinguishableFrom(namedElement, namespace);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#separator()
     */
    public String separator()
    {
        return this.instanceSpecification.separator();
    }

    /**
     * @return instanceSpecification.getQualifiedName()
     */
    public String qualifiedName()
    {
        return this.instanceSpecification.getQualifiedName();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateVisibilityNeedsOwnership(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateVisibilityNeedsOwnership(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateVisibilityNeedsOwnership(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getNamespace()
     */
    public Namespace getNamespace()
    {
        return this.instanceSpecification.getNamespace();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getLabel()
     */
    public String getLabel()
    {
        return this.instanceSpecification.getLabel();
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getLabel(boolean)
     */
    public String getLabel(boolean b)
    {
        return this.instanceSpecification.getLabel(b);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createDependency(org.eclipse.uml2.uml.NamedElement)
     */
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

    /**
     * @see org.eclipse.uml2.uml.Element#getOwnedElements()
     */
    public EList getOwnedElements()
    {
        return this.instanceSpecification.getOwnedElements();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getOwner()
     */
    public Element getOwner()
    {
        return this.instanceSpecification.getOwner();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getOwnedComments()
     */
    public EList getOwnedComments()
    {
        return this.instanceSpecification.getOwnedComments();
    }

    /**
     * @param eClass
     * @return instanceSpecification.createOwnedComment()
     */
    public Comment createOwnedComment(EClass eClass)
    {
        return this.instanceSpecification.createOwnedComment();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#createOwnedComment()
     */
    public Comment createOwnedComment()
    {
        return this.instanceSpecification.createOwnedComment();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNotOwnSelf(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateNotOwnSelf(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasOwner(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasOwner(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#allOwnedElements()
     */
    public EList<Element> allOwnedElements()
    {
        return this.instanceSpecification.allOwnedElements();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#mustBeOwned()
     */
    public boolean mustBeOwned()
    {
        return this.instanceSpecification.mustBeOwned();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#createEAnnotation(String)
     */
    public EAnnotation createEAnnotation(String string)
    {
        return this.instanceSpecification.createEAnnotation(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#applyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject applyStereotype(Stereotype stereotype)
    {
        return this.instanceSpecification.applyStereotype(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotype(String)
     */
    public Stereotype getApplicableStereotype(String string)
    {
        return this.instanceSpecification.getApplicableStereotype(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotypes()
     */
    public EList<Stereotype> getApplicableStereotypes()
    {
        return this.instanceSpecification.getApplicableStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedStereotype(String)
     */
    public Stereotype getAppliedStereotype(String string)
    {
        return this.instanceSpecification.getAppliedStereotype(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedStereotypes()
     */
    public EList<Stereotype> getAppliedStereotypes()
    {
        return this.instanceSpecification.getAppliedStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getModel()
     */
    public Model getModel()
    {
        return (Model) UmlUtilities.findModel(this.instanceSpecification);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getNearestPackage()
     */
    public org.eclipse.uml2.uml.Package getNearestPackage()
    {
        return this.instanceSpecification.getNearestPackage();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getValue(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Object getValue(Stereotype stereotype, String string)
    {
        return this.instanceSpecification.getValue(stereotype, string);
    }

    /**
     * @param stereotype
     * @return instanceSpecification.isStereotypeApplied(stereotype)
     */
    public boolean isApplied(Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeApplied(stereotype);
    }

    /**
     * @param stereotype
     * @return instanceSpecification.isStereotypeRequired(stereotype)
     */
    public boolean isRequired(Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeRequired(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#setValue(org.eclipse.uml2.uml.Stereotype, String, Object)
     */
    public void setValue(Stereotype stereotype, String string, Object object)
    {
        this.instanceSpecification.setValue(stereotype, string, object);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#hasValue(org.eclipse.uml2.uml.Stereotype, String)
     */
    public boolean hasValue(Stereotype stereotype, String string)
    {
        return this.instanceSpecification.hasValue(stereotype, string);
    }

    /*public void unapply(Stereotype stereotype)
    {
        this.instanceSpecification.unapply(stereotype);
    }*/

    /**
     * @see org.eclipse.uml2.uml.Element#destroy()
     */
    public void destroy()
    {
        this.instanceSpecification.destroy();
    }

   /* public String getAppliedVersion(Stereotype stereotype)
    {
        return this.instanceSpecification.getAppliedVersion(stereotype);
    }*/

    /**
     * @see org.eclipse.uml2.uml.Element#addKeyword(String)
     */
    public boolean addKeyword(String string)
    {
        return this.instanceSpecification.addKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getKeywords()
     */
    public EList<String> getKeywords()
    {
        return this.instanceSpecification.getKeywords();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#hasKeyword(String)
     */
    public boolean hasKeyword(String string)
    {
        return this.instanceSpecification.hasKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#removeKeyword(String)
     */
    public boolean removeKeyword(String string)
    {
        return this.instanceSpecification.removeKeyword(string);
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

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#createSpecification(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createSpecification(String name, Type type, EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#getClassifier(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Classifier getClassifier(String name, boolean ignoreCase, EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDefiningFeature(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDefiningFeature(DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDeploymentArtifact(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDeploymentArtifact(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDeploymentTarget(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDeploymentTarget(DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateStructuralFeature(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateStructuralFeature(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#createDeployment(String)
     */
    public Deployment createDeployment(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElement(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public PackageableElement getDeployedElement(String name, boolean ignoreCase, EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployment(String, boolean, boolean)
     */
    public Deployment getDeployment(String name, boolean ignoreCase, boolean createOnDemand)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#allOwningPackages()
     */
    public EList<Package> allOwningPackages()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createNameExpression(String, org.eclipse.uml2.uml.Type)
     */
    public StringExpression createNameExpression(String name, Type type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createUsage(org.eclipse.uml2.uml.NamedElement)
     */
    public Usage createUsage(NamedElement supplier)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependency(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Dependency getClientDependency(String name, boolean ignoreCase, EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isSetName()
     */
    public boolean isSetName()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#isSetVisibility()
     */
    public boolean isSetVisibility()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#unsetName()
     */
    public void unsetName()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#unsetVisibility()
     */
    public void unsetVisibility()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateHasNoQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasNoQualifiedName(DiagnosticChain diagnostics,
            Map<Object, Object> context)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateHasQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasQualifiedName(DiagnosticChain diagnostics, Map<Object, Object> context)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotype(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Stereotype getAppliedSubstereotype(Stereotype stereotype, String qualifiedName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotypes(org.eclipse.uml2.uml.Stereotype)
     */
    public EList<Stereotype> getAppliedSubstereotypes(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships()
     */
    public EList<Relationship> getRelationships()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<Relationship> getRelationships(EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotype(String)
     */
    public Stereotype getRequiredStereotype(String qualifiedName)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotypes()
     */
    public EList<Stereotype> getRequiredStereotypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships()
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships(EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplication(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject getStereotypeApplication(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplications()
     */
    public EList<EObject> getStereotypeApplications()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships()
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships(EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplicable(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplicable(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplied(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplied(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeRequired(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeRequired(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#unapplyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject unapplyStereotype(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#getOwningTemplateParameter()
     */
    public TemplateParameter getOwningTemplateParameter()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#isCompatibleWith(org.eclipse.uml2.uml.ParameterableElement)
     */
    public boolean isCompatibleWith(ParameterableElement p)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#isTemplateParameter()
     */
    public boolean isTemplateParameter()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.eclipse.uml2.uml.ParameterableElement#setOwningTemplateParameter(org.eclipse.uml2.uml.TemplateParameter)
     */
    public void setOwningTemplateParameter(TemplateParameter value)
    {
        // TODO Auto-generated method stub
    }

    /**
     * UML2 3.1 (Eclipse 3.6) only
     * @see org.eclipse.emf.ecore.EObject#eInvoke(org.eclipse.emf.ecore.EOperation, org.eclipse.emf.common.util.EList)
     */
    public Object eInvoke(EOperation arg0, EList<?> arg1) throws InvocationTargetException
    {
        return this.instanceSpecification.eInvoke(arg0, arg1);
    }
}
