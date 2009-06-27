package org.andromda.metafacades.emf.uml2;

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

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectInstanceImpl implements ObjectInstance
{
    final InstanceSpecification instanceSpecification;

    ObjectInstanceImpl(InstanceSpecification instanceSpecification)
    {
        this.instanceSpecification = instanceSpecification;
    }

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

    public int hashCode()
    {
        return this.instanceSpecification.hashCode();
    }

    public String toString()
    {
        return this.getClass().getName() + '[' + this.instanceSpecification.toString() + ']';
    }

    public EList getDeployments()
    {
        return this.instanceSpecification.getDeployments();
    }

    public Deployment getDeployment(String string)
    {
        return this.instanceSpecification.getDeployment(string);
    }

    public Deployment createDeployment(EClass eClass)
    {
        return this.instanceSpecification.createDeployment(eClass);
    }

    public Deployment createDeployment()
    {
        return this.instanceSpecification.createDeployment();
    }

    public EList getDeployedElements()
    {
        return this.instanceSpecification.getDeployedElements();
    }

    public PackageableElement getDeployedElement(String string)
    {
        return this.instanceSpecification.getDeployedElement(string);
    }

    public EList getSlots()
    {
        return this.instanceSpecification.getSlots();
    }

    public Slot createSlot(EClass eClass)
    {
        return this.instanceSpecification.createSlot(eClass);
    }

    public Slot createSlot()
    {
        return this.instanceSpecification.createSlot();
    }

    public EList getClassifiers()
    {
        return this.instanceSpecification.getClassifiers();
    }

    public Classifier getClassifier(String string)
    {
        return this.instanceSpecification.getClassifier(string);
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
        return this.instanceSpecification.createSpecification(eClass);
    }

    public boolean validateSlotsAreDefined(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateSlotsAreDefined(diagnosticChain, map);
    }

    public boolean validateNoDuplicateSlots(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateNoDuplicateSlots(diagnosticChain, map);
    }

    public EList getClientDependencies()
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
        return this.instanceSpecification.getOwningParameter();
    }

    public void setOwningParameter(TemplateParameter templateParameter)
    {
        this.instanceSpecification.setOwningParameter(templateParameter);
    }

    public VisibilityKind getPackageableElement_visibility()
    {
        return this.instanceSpecification.getPackageableElement_visibility();
    }

    public void setPackageableElement_visibility(VisibilityKind visibilityKind)
    {
        this.instanceSpecification.setPackageableElement_visibility(visibilityKind);
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
        return this.instanceSpecification.createNameExpression(eClass);
    }

    public StringExpression createNameExpression()
    {
        return this.instanceSpecification.createNameExpression();
    }

    public boolean validateNoName(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateNoName(diagnosticChain, map);
    }

    public boolean validateQualifiedName(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateQualifiedName(diagnosticChain, map);
    }

    public List allNamespaces()
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
        return this.instanceSpecification.qualifiedName();
    }

    public boolean validateVisibilityNeedsOwnership(DiagnosticChain diagnosticChain, Map map)
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

    public EList getTemplateBindings()
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
    }

    public EList getOwnedElements()
    {
        return this.instanceSpecification.getOwnedElements();
    }

    public Element getOwner()
    {
        return this.instanceSpecification.getOwner();
    }

    public EList getOwnedComments()
    {
        return this.instanceSpecification.getOwnedComments();
    }

    public Comment createOwnedComment(EClass eClass)
    {
        return this.instanceSpecification.createOwnedComment(eClass);
    }

    public Comment createOwnedComment()
    {
        return this.instanceSpecification.createOwnedComment();
    }

    public boolean validateNotOwnSelf(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateNotOwnSelf(diagnosticChain, map);
    }

    public boolean validateHasOwner(DiagnosticChain diagnosticChain, Map map)
    {
        return this.instanceSpecification.validateHasOwner(diagnosticChain, map);
    }

    public Set allOwnedElements()
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

    public void apply(Stereotype stereotype)
    {
        this.instanceSpecification.apply(stereotype);
    }

    public Stereotype getApplicableStereotype(String string)
    {
        return this.instanceSpecification.getApplicableStereotype(string);
    }

    public Set getApplicableStereotypes()
    {
        return this.instanceSpecification.getApplicableStereotypes();
    }

    public Stereotype getAppliedStereotype(String string)
    {
        return this.instanceSpecification.getAppliedStereotype(string);
    }

    public Set getAppliedStereotypes()
    {
        return this.instanceSpecification.getAppliedStereotypes();
    }

    public Model getModel()
    {
        return this.instanceSpecification.getModel();
    }

    public org.eclipse.uml2.Package getNearestPackage()
    {
        return this.instanceSpecification.getNearestPackage();
    }

    public Object getValue(Stereotype stereotype, String string)
    {
        return this.instanceSpecification.getValue(stereotype, string);
    }

    public boolean isApplied(Stereotype stereotype)
    {
        return this.instanceSpecification.isApplied(stereotype);
    }

    public boolean isRequired(Stereotype stereotype)
    {
        return this.instanceSpecification.isRequired(stereotype);
    }

    public void setValue(Stereotype stereotype, String string, Object object)
    {
        this.instanceSpecification.setValue(stereotype, string, object);
    }

    public boolean hasValue(Stereotype stereotype, String string)
    {
        return this.instanceSpecification.hasValue(stereotype, string);
    }

    public void unapply(Stereotype stereotype)
    {
        this.instanceSpecification.unapply(stereotype);
    }

    public void destroy()
    {
        this.instanceSpecification.destroy();
    }

    public String getAppliedVersion(Stereotype stereotype)
    {
        return this.instanceSpecification.getAppliedVersion(stereotype);
    }

    public void addKeyword(String string)
    {
        this.instanceSpecification.addKeyword(string);
    }

    public Set getKeywords()
    {
        return this.instanceSpecification.getKeywords();
    }

    public boolean hasKeyword(String string)
    {
        return this.instanceSpecification.hasKeyword(string);
    }

    public void removeKeyword(String string)
    {
        this.instanceSpecification.removeKeyword(string);
    }

    public EList getEAnnotations()
    {
        return this.instanceSpecification.getEAnnotations();
    }

    public EAnnotation getEAnnotation(String string)
    {
        return this.instanceSpecification.getEAnnotation(string);
    }

    public EList eAdapters()
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

    public EList eContents()
    {
        return this.instanceSpecification.eContents();
    }

    public EList eCrossReferences()
    {
        return this.instanceSpecification.eCrossReferences();
    }

    public TreeIterator eAllContents()
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
}
