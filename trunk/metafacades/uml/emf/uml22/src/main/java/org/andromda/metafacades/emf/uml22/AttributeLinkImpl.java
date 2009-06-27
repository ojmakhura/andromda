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
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * Implementation of AttributeLink.
 *
 * We extend Slot. We keep a reference to the original slot and we defer
 * almost all method calls to it.
 *
 * @author Wouter Zoons
 */
public class AttributeLinkImpl implements AttributeLink
{
    final Slot slot;

    AttributeLinkImpl(final Slot slotIn)
    {
        this.slot = slotIn;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof AttributeLinkImpl)
        {
            return this.slot.equals(((AttributeLinkImpl)object).slot);
        }
        if (object instanceof LinkEndImpl)
        {
            return this.slot.equals(((LinkEndImpl)object).slot);
        }
        return this.slot.equals(object);
    }

    @Override
    public int hashCode()
    {
        return this.slot.hashCode();
    }

    @Override
    public String toString()
    {
        return this.getClass().getName() + '[' + this.slot.toString() + ']';
    }

    public InstanceSpecification getOwningInstance()
    {
        return this.slot.getOwningInstance();
    }

    public void setOwningInstance(InstanceSpecification instanceSpecification)
    {
        this.slot.setOwningInstance(instanceSpecification);
    }

    public EList<ValueSpecification> getValues()
    {
        return this.slot.getValues();
    }

    public ValueSpecification getValue(String string)
    {
        return this.slot.getValue(string, null);
    }

    public ValueSpecification getValue(String string, Type type)
    {
        return this.slot.getValue(string, type);
    }

    public ValueSpecification getValue(String name, Type type, boolean ignoreCase, EClass eClass, boolean createOnDemand)
    {
        return this.slot.getValue(name, type, ignoreCase, eClass, createOnDemand);
    }

    public ValueSpecification createValue(EClass eClass)
    {
        return this.slot.createValue(null, null, eClass);
    }

    public ValueSpecification createValue(String name, Type type, EClass eClass)
    {
        return this.slot.createValue(name, type, eClass);
    }

    public StructuralFeature getDefiningFeature()
    {
        return this.slot.getDefiningFeature();
    }

    public void setDefiningFeature(StructuralFeature structuralFeature)
    {
        this.slot.setDefiningFeature(structuralFeature);
    }

    public Object eGet(EStructuralFeature eStructuralFeature, boolean b)
    {
        return this.slot.eGet(eStructuralFeature, b);
    }

    public void eSet(EStructuralFeature eStructuralFeature, Object object)
    {
        this.slot.eSet(eStructuralFeature, object);
    }

    public void eUnset(EStructuralFeature eStructuralFeature)
    {
        this.slot.eUnset(eStructuralFeature);
    }

    public boolean eIsSet(EStructuralFeature eStructuralFeature)
    {
        return this.slot.eIsSet(eStructuralFeature);
    }

    public EList<Element> getOwnedElements()
    {
        return this.slot.getOwnedElements();
    }

    public Element getOwner()
    {
        return this.slot.getOwner();
    }

    public EList<Comment> getOwnedComments()
    {
        return this.slot.getOwnedComments();
    }

    public Comment createOwnedComment(EClass eClass)
    {
        return this.slot.createOwnedComment();
    }

    public Comment createOwnedComment()
    {
        return this.slot.createOwnedComment();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasOwner(DiagnosticChain diagnosticChain, Map<Object, Object> context)
    {
        return this.slot.validateHasOwner(diagnosticChain, context);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNotOwnSelf(DiagnosticChain diagnosticChain, Map<Object, Object> context)
    {
        return this.slot.validateNotOwnSelf(diagnosticChain, context);
    }

    public EList<Element> allOwnedElements()
    {
        return this.slot.allOwnedElements();
    }

    public boolean mustBeOwned()
    {
        return this.slot.mustBeOwned();
    }

    public EAnnotation createEAnnotation(String string)
    {
        return this.slot.createEAnnotation(string);
    }

    public void apply(Stereotype stereotype)
    {
        this.slot.applyStereotype(stereotype);
    }

    public EObject applyStereotype(Stereotype stereotype)
    {
        return this.slot.applyStereotype(stereotype);
    }

    public Stereotype getApplicableStereotype(String string)
    {
        return this.slot.getApplicableStereotype(string);
    }

    public EList<Stereotype> getApplicableStereotypes()
    {
        return this.slot.getApplicableStereotypes();
    }

    public Stereotype getAppliedStereotype(String string)
    {
        return this.slot.getAppliedStereotype(string);
    }

    public Stereotype getAppliedSubstereotype(Stereotype stereotype, String string)
    {
        return this.slot.getAppliedSubstereotype(stereotype, string);
    }

    public EList<Stereotype> getAppliedStereotypes()
    {
        return this.slot.getAppliedStereotypes();
    }

    public EList<Stereotype> getAppliedSubstereotypes(Stereotype stereotype)
    {
        return this.slot.getAppliedSubstereotypes(stereotype);
    }

    public Model getModel()
    {
        return (Model) UmlUtilities.findModel(this.slot);
    }

    public Package getNearestPackage()
    {
        return this.slot.getNearestPackage();
    }

    public Object getValue(Stereotype stereotype, String string)
    {
        return this.slot.getValue(stereotype, string);
    }

    public boolean isStereotypeApplicable(Stereotype stereotype)
    {
        return this.slot.isStereotypeApplicable(stereotype);
    }

    @Deprecated
    public boolean isApplied(Stereotype stereotype)
    {
        return this.slot.isStereotypeApplied(stereotype);
    }

    public boolean isStereotypeApplied(Stereotype stereotype)
    {
        return this.slot.isStereotypeApplied(stereotype);
    }

    @Deprecated
    public boolean isRequired(Stereotype stereotype)
    {
        return this.slot.isStereotypeRequired(stereotype);
    }

    public boolean isStereotypeRequired(Stereotype stereotype)
    {
        return this.slot.isStereotypeRequired(stereotype);
    }

    public void setValue(Stereotype stereotype, String string, Object object)
    {
        this.slot.setValue(stereotype, string, object);
    }

    public boolean hasValue(Stereotype stereotype, String string)
    {
        return this.slot.hasValue(stereotype, string);
    }

    public void unapply(Stereotype stereotype)
    {
        this.slot.unapplyStereotype(stereotype);
    }

    public EObject unapplyStereotype(Stereotype stereotype)
    {
        return this.slot.unapplyStereotype(stereotype);
    }

    public void destroy()
    {
        this.slot.destroy();
    }

    // TODO: Map getAppliedVersion?
    /*public String getAppliedVersion(Stereotype stereotype)
    {
        return this.slot.getAppliedVersion(stereotype);
    }*/

    public boolean addKeyword(String string)
    {
        return this.slot.addKeyword(string);
    }

    public EList<String> getKeywords()
    {
        return this.slot.getKeywords();
    }

    public boolean hasKeyword(String string)
    {
        return this.slot.hasKeyword(string);
    }

    public boolean removeKeyword(String string)
    {
        return this.slot.removeKeyword(string);
    }

    public EList<EAnnotation> getEAnnotations()
    {
        return this.slot.getEAnnotations();
    }

    public EAnnotation getEAnnotation(String string)
    {
        return this.slot.getEAnnotation(string);
    }

    public EList<Adapter> eAdapters()
    {
        return this.slot.eAdapters();
    }

    public boolean eDeliver()
    {
        return this.slot.eDeliver();
    }

    public void eSetDeliver(boolean b)
    {
        this.slot.eSetDeliver(b);
    }

    public boolean eIsProxy()
    {
        return this.slot.eIsProxy();
    }

    public EClass eClass()
    {
        return this.slot.eClass();
    }

    public EObject eContainer()
    {
        return this.slot.eContainer();
    }

    public EList<EObject> eContents()
    {
        return this.slot.eContents();
    }

    public EList<EObject> eCrossReferences()
    {
        return this.slot.eCrossReferences();
    }

    public TreeIterator<EObject> eAllContents()
    {
        return this.slot.eAllContents();
    }

    public EReference eContainmentFeature()
    {
        return this.slot.eContainmentFeature();
    }

    public EStructuralFeature eContainingFeature()
    {
        return this.slot.eContainingFeature();
    }

    public Resource eResource()
    {
        return this.slot.eResource();
    }

    public Object eGet(EStructuralFeature eStructuralFeature)
    {
        return this.slot.eGet(eStructuralFeature);
    }

    public void eNotify(Notification notification)
    {
        this.slot.eNotify(notification);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships()
     */
    public EList<Relationship> getRelationships()
    {
        return this.slot.getRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<Relationship> getRelationships(EClass eClass)
    {
        return this.slot.getRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotype(String)
     */
    public Stereotype getRequiredStereotype(String qualifiedName)
    {
        return this.slot.getRequiredStereotype(qualifiedName);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getRequiredStereotypes()
     */
    public EList<Stereotype> getRequiredStereotypes()
    {
        return this.slot.getRequiredStereotypes();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships()
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships()
    {
        return this.slot.getSourceDirectedRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getSourceDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getSourceDirectedRelationships(EClass eClass)
    {
        return this.slot.getSourceDirectedRelationships(eClass);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplication(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject getStereotypeApplication(Stereotype stereotype)
    {
        return this.slot.getStereotypeApplication(stereotype);
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getStereotypeApplications()
     */
    public EList<EObject> getStereotypeApplications()
    {
        return this.slot.getStereotypeApplications();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships()
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships()
    {
        return this.slot.getTargetDirectedRelationships();
    }

    /**
     * @see org.eclipse.uml2.uml.Element#getTargetDirectedRelationships(org.eclipse.emf.ecore.EClass)
     */
    public EList<DirectedRelationship> getTargetDirectedRelationships(EClass eClass)
    {
        return this.slot.getTargetDirectedRelationships(eClass);
    }
}
