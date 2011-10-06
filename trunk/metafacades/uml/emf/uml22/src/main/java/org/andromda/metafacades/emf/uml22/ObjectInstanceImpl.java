package org.andromda.metafacades.emf.uml22;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.eclipse.emf.common.notify.Adapter;
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
    ObjectInstanceImpl(final InstanceSpecification instanceSpecification)
    {
        this.instanceSpecification = instanceSpecification;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
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
    public EList<Deployment> getDeployments()
    {
        return this.instanceSpecification.getDeployments();
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployment(String, boolean, boolean)
     */
    public Deployment getDeployment(final String string, final boolean ignoreCase, final boolean createOnDemand)
    {
        return this.instanceSpecification.getDeployment(string, ignoreCase, createOnDemand);
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployment(String)
     */
    public Deployment getDeployment(final String string)
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
     * @see org.eclipse.uml2.uml.DeploymentTarget#createDeployment(String)
     */
    public Deployment createDeployment(final String name)
    {
        return this.instanceSpecification.createDeployment(name);
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElements()
     */
    public EList<PackageableElement> getDeployedElements()
    {
        return this.instanceSpecification.getDeployedElements();
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElement(String)
     */
    public PackageableElement getDeployedElement(final String string)
    {
        return this.instanceSpecification.getDeployedElement(string);
    }

    /**
     * @see org.eclipse.uml2.uml.DeploymentTarget#getDeployedElement(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public PackageableElement getDeployedElement(final String string, final boolean bool, final EClass eClass)
    {
        return this.instanceSpecification.getDeployedElement(string, bool, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#getSlots()
     */
    public EList<Slot> getSlots()
    {
        return this.instanceSpecification.getSlots();
    }

    /**
     * @param eClass
     * @return instanceSpecification.createSlot()
     */
    public Slot createSlot(final EClass eClass)
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
    public EList<Classifier> getClassifiers()
    {
        return this.instanceSpecification.getClassifiers();
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#getClassifier(String)
     */
    public Classifier getClassifier(final String string)
    {
        return this.instanceSpecification.getClassifier(string);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#getClassifier(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Classifier getClassifier(final String string, final boolean ignoreCase, final EClass eClass)
    {
        return this.instanceSpecification.getClassifier(string, ignoreCase, eClass);
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
    public void setSpecification(final ValueSpecification valueSpecification)
    {
        this.instanceSpecification.setSpecification(valueSpecification);
    }

    /**
     * @param eClass
     * @return instanceSpecification.createSpecification(null, null, eClass)
     */
    public ValueSpecification createSpecification(final EClass eClass)
    {
        return this.instanceSpecification.createSpecification(null, null, eClass);
    }

    /*public boolean validateSlotsAreDefined(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateSlotsAreDefined(diagnosticChain, map);
    }

    public boolean validateNoDuplicateSlots(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.instanceSpecification.validateNoDuplicateSlots(diagnosticChain, map);
    }*/

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependencies()
     */
    public EList<Dependency> getClientDependencies()
    {
        return this.instanceSpecification.getClientDependencies();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature, boolean)
     */
    public Object eGet(final EStructuralFeature eStructuralFeature, final boolean resolve)
    {
        return this.instanceSpecification.eGet(eStructuralFeature, resolve);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature, Object)
     */
    public void eSet(final EStructuralFeature eStructuralFeature, final Object object)
    {
        this.instanceSpecification.eSet(eStructuralFeature, object);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public void eUnset(final EStructuralFeature eStructuralFeature)
    {
        this.instanceSpecification.eUnset(eStructuralFeature);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public boolean eIsSet(final EStructuralFeature eStructuralFeature)
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
    public void setTemplateParameter(final TemplateParameter templateParameter)
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
    public void setOwningParameter(final TemplateParameter templateParameter)
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
    public void setPackageableElement_visibility(final VisibilityKind visibilityKind)
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
    public void setVisibility(final VisibilityKind visibilityKind)
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
    public void setName(final String string)
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
    public Dependency getClientDependency(final String string)
    {
        return this.instanceSpecification.getClientDependency(string);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#getClientDependency(String, boolean, org.eclipse.emf.ecore.EClass)
     */
    public Dependency getClientDependency(final String string, final boolean ignoreCase, final EClass eClass)
    {
        return this.instanceSpecification.getClientDependency(string, ignoreCase, eClass);
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
    public void setNameExpression(final StringExpression stringExpression)
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
    public boolean validateNoName(final DiagnosticChain diagnosticChain, final Map<Object, Object> map)
    {
        return !this.instanceSpecification.validateHasQualifiedName(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateHasNoQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasNoQualifiedName(final DiagnosticChain diagnosticChain, final Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasNoQualifiedName(diagnosticChain, map);
    }

    /**
     * @param diagnosticChain
     * @param map
     * @return instanceSpecification.validateHasQualifiedName(diagnosticChain, map)
     */
    public boolean validateQualifiedName(final DiagnosticChain diagnosticChain, final Map<Object, Object> map)
    {
        return this.instanceSpecification.validateHasQualifiedName(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#validateHasQualifiedName(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasQualifiedName(final DiagnosticChain diagnosticChain, final Map<Object, Object> map)
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
    public boolean isDistinguishableFrom(final NamedElement namedElement, final Namespace namespace)
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
    public boolean validateVisibilityNeedsOwnership(final DiagnosticChain diagnosticChain, final Map<Object, Object> map)
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
    public String getLabel(final boolean localize)
    {
        return this.instanceSpecification.getLabel(localize);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createDependency(org.eclipse.uml2.uml.NamedElement)
     */
    public Dependency createDependency(final NamedElement namedElement)
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
    public EList<Element> getOwnedElements()
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
    public EList<Comment> getOwnedComments()
    {
        return this.instanceSpecification.getOwnedComments();
    }

    /**
     * @param eClass
     * @return instanceSpecification.createOwnedComment()
     */
    public Comment createOwnedComment(final EClass eClass)
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
    public boolean validateNotOwnSelf(final DiagnosticChain diagnosticChain, final Map<Object, Object> map)
    {
        return this.instanceSpecification.validateNotOwnSelf(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasOwner(final DiagnosticChain diagnosticChain, final Map<Object, Object> map)
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
    public EAnnotation createEAnnotation(final String string)
    {
        return this.instanceSpecification.createEAnnotation(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#applyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject applyStereotype(final Stereotype stereotype)
    {
        return this.instanceSpecification.applyStereotype(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getApplicableStereotype(String)
     */
    public Stereotype getApplicableStereotype(final String string)
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
    public Stereotype getAppliedStereotype(final String string)
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
    public Package getNearestPackage()
    {
        return this.instanceSpecification.getNearestPackage();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getValue(org.eclipse.uml2.uml.Stereotype, String)
     */
    public Object getValue(final Stereotype stereotype, final String string)
    {
        return this.instanceSpecification.getValue(stereotype, string);
    }

    /**
     * @param stereotype
     * @return instanceSpecification.isStereotypeApplied(stereotype)
     */
    public boolean isApplied(final Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeApplied(stereotype);
    }

    /**
     * @param stereotype
     * @return instanceSpecification.isStereotypeRequired(stereotype)
     */
    public boolean isRequired(final Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeRequired(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#setValue(org.eclipse.uml2.uml.Stereotype, String, Object)
     */
    public void setValue(final Stereotype stereotype, final String string, final Object object)
    {
        this.instanceSpecification.setValue(stereotype, string, object);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#hasValue(org.eclipse.uml2.uml.Stereotype, String)
     */
    public boolean hasValue(final Stereotype stereotype, final String string)
    {
        return this.instanceSpecification.hasValue(stereotype, string);
    }

    /*public void unapply(Stereotype stereotype)
    {
        this.instanceSpecification.unapply(stereotype);
    }*/

    /**
     * @see org.eclipse.uml2.uml.Element#unapplyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject unapplyStereotype(final Stereotype stereotype)
    {
        return this.instanceSpecification.unapplyStereotype(stereotype);
    }

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
    public boolean addKeyword(final String string)
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
    public boolean hasKeyword(final String string)
    {
        return this.instanceSpecification.hasKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#removeKeyword(String)
     */
    public boolean removeKeyword(final String string)
    {
        return this.instanceSpecification.removeKeyword(string);
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotations()
     */
    public EList<EAnnotation> getEAnnotations()
    {
        return this.instanceSpecification.getEAnnotations();
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotation(String)
     */
    public EAnnotation getEAnnotation(final String string)
    {
        return this.instanceSpecification.getEAnnotation(string);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    public EList<Adapter> eAdapters()
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
    public void eSetDeliver(final boolean deliver)
    {
        this.instanceSpecification.eSetDeliver(deliver);
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
    public EList<EObject> eContents()
    {
        return this.instanceSpecification.eContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    public EList<EObject> eCrossReferences()
    {
        return this.instanceSpecification.eCrossReferences();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    public TreeIterator<EObject> eAllContents()
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
    public Object eGet(final EStructuralFeature eStructuralFeature)
    {
        return this.instanceSpecification.eGet(eStructuralFeature);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    public void eNotify(final Notification notification)
    {
        this.instanceSpecification.eNotify(notification);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#createSpecification(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createSpecification(final String name, final Type type, final EClass eClass)
    {
        return this.instanceSpecification.createSpecification(name, type, eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDefiningFeature(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDefiningFeature(final DiagnosticChain diagnostics, final Map<Object, Object> context)
    {
        return this.instanceSpecification.validateDefiningFeature(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDeploymentArtifact(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDeploymentArtifact(final DiagnosticChain diagnostics,
        final Map<Object, Object> context)
    {
        return this.instanceSpecification.validateDeploymentArtifact(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateDeploymentTarget(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateDeploymentTarget(final DiagnosticChain diagnostics, final Map<Object, Object> context)
    {
        return this.instanceSpecification.validateDeploymentTarget(diagnostics, context);
    }

    /**
     * @see org.eclipse.uml2.uml.InstanceSpecification#validateStructuralFeature(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateStructuralFeature(final DiagnosticChain diagnostics,
        final Map<Object, Object> context)
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
    public StringExpression createNameExpression(final String name, final Type type)
    {
        return this.instanceSpecification.createNameExpression(name, type);
    }

    /**
     * @see org.eclipse.uml2.uml.NamedElement#createUsage(org.eclipse.uml2.uml.NamedElement)
     */
    public Usage createUsage(final NamedElement supplier)
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
        return this.instanceSpecification.isSetVisibility();
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
    public Stereotype getAppliedSubstereotype(final Stereotype stereotype, final String qualifiedName)
    {
        return this.instanceSpecification.getAppliedSubstereotype(stereotype, qualifiedName);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getAppliedSubstereotypes(org.eclipse.uml2.uml.Stereotype)
     */
    public EList<Stereotype> getAppliedSubstereotypes(final Stereotype stereotype)
    {
        return this.instanceSpecification.getAppliedSubstereotypes(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships()
     */
    public EList<Relationship> getRelationships()
    {
        return this.instanceSpecification.getRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<Relationship> getRelationships(final EClass eClass)
    {
        return this.instanceSpecification.getRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotype(String)
     */
    public Stereotype getRequiredStereotype(final String qualifiedName)
    {
        return this.instanceSpecification.getRequiredStereotype(qualifiedName);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotypes()
     */
    public EList<Stereotype> getRequiredStereotypes()
    {
        return this.instanceSpecification.getRequiredStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships()
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships()
    {
        return this.instanceSpecification.getSourceDirectedRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships(final EClass eClass)
    {
        return this.instanceSpecification.getSourceDirectedRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplication(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject getStereotypeApplication(final Stereotype stereotype)
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
    public EList<DirectedRelationship> getTargetDirectedRelationships(final EClass eClass)
    {
        return this.instanceSpecification.getTargetDirectedRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplicable(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplicable(final Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeApplicable(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeApplied(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeApplied(final Stereotype stereotype)
    {
        return this.instanceSpecification.isStereotypeApplied(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#isStereotypeRequired(org.eclipse.uml2.uml.Stereotype)
     */
    public boolean isStereotypeRequired(final Stereotype stereotype)
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
    public boolean isCompatibleWith(final ParameterableElement param)
    {
        return this.instanceSpecification.isCompatibleWith(param);
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
    public void setOwningTemplateParameter(final TemplateParameter value)
    {
        this.instanceSpecification.setOwningTemplateParameter(value);
    }

    /**
     * UML2 3.1 (Eclipse 3.6) only
     * @see org.eclipse.emf.ecore.EObject#eInvoke(org.eclipse.emf.ecore.EOperation, org.eclipse.emf.common.util.EList)
     */
    public Object eInvoke(final EOperation operation, final EList<?> arguments) throws InvocationTargetException
    {
        return this.instanceSpecification.eInvoke(operation, arguments);
    }
}
