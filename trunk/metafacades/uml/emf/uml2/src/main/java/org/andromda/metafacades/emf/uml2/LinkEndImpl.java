package org.andromda.metafacades.emf.uml2;

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
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.InstanceSpecification;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.Slot;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.StructuralFeature;
import org.eclipse.uml2.ValueSpecification;

/**
 *
 */
public class LinkEndImpl implements LinkEnd
{
    /**
     *
     */
    final Slot slot;

    /**
     * @param slot
     */
    LinkEndImpl(final Slot slot)
    {
        this.slot = slot;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return this.slot.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.getClass().getName() + '[' + this.slot.toString() + ']';
    }

    /**
     * @see org.eclipse.uml2.Slot#getOwningInstance()
     */
    public InstanceSpecification getOwningInstance()
    {
        return this.slot.getOwningInstance();
    }

    /**
     * @see org.eclipse.uml2.Slot#setOwningInstance(org.eclipse.uml2.InstanceSpecification)
     */
    public void setOwningInstance(InstanceSpecification instanceSpecification)
    {
        this.slot.setOwningInstance(instanceSpecification);
    }

    /**
     * @see org.eclipse.uml2.Slot#getValues()
     */
    public EList getValues()
    {
        return this.slot.getValues();
    }

    /**
     * @see org.eclipse.uml2.Slot#getValue(java.lang.String)
     */
    public ValueSpecification getValue(String string)
    {
        return this.slot.getValue(string);
    }

    /**
     * @see org.eclipse.uml2.Slot#createValue(org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createValue(EClass eClass)
    {
        return this.slot.createValue(eClass);
    }

    /**
     * @see org.eclipse.uml2.Slot#getDefiningFeature()
     */
    public StructuralFeature getDefiningFeature()
    {
        return this.slot.getDefiningFeature();
    }

    /**
     * @see org.eclipse.uml2.Slot#setDefiningFeature(org.eclipse.uml2.StructuralFeature)
     */
    public void setDefiningFeature(StructuralFeature structuralFeature)
    {
        this.slot.setDefiningFeature(structuralFeature);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature, boolean)
     */
    public Object eGet(EStructuralFeature eStructuralFeature, boolean b)
    {
        return this.slot.eGet(eStructuralFeature, b);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eSet(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
     */
    public void eSet(EStructuralFeature eStructuralFeature, Object object)
    {
        this.slot.eSet(eStructuralFeature, object);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eUnset(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public void eUnset(EStructuralFeature eStructuralFeature)
    {
        this.slot.eUnset(eStructuralFeature);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public boolean eIsSet(EStructuralFeature eStructuralFeature)
    {
        return this.slot.eIsSet(eStructuralFeature);
    }

    /**
     * @see org.eclipse.uml2.Element#getOwnedElements()
     */
    public EList getOwnedElements()
    {
        return this.slot.getOwnedElements();
    }

    /**
     * @see org.eclipse.uml2.Element#getOwner()
     */
    public Element getOwner()
    {
        return this.slot.getOwner();
    }

    /**
     * @see org.eclipse.uml2.Element#getOwnedComments()
     */
    public EList getOwnedComments()
    {
        return this.slot.getOwnedComments();
    }

    /**
     * @see org.eclipse.uml2.Element#createOwnedComment(org.eclipse.emf.ecore.EClass)
     * @deprecated
     */
    public Comment createOwnedComment(EClass eClass)
    {
        return this.slot.createOwnedComment(eClass);
    }

    /**
     * @see org.eclipse.uml2.Element#createOwnedComment()
     */
    public Comment createOwnedComment()
    {
        return this.slot.createOwnedComment();
    }

    /**
     * @see org.eclipse.uml2.Element#validateNotOwnSelf(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateNotOwnSelf(DiagnosticChain diagnosticChain, Map map)
    {
        return this.slot.validateNotOwnSelf(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.Element#validateHasOwner(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
     */
    public boolean validateHasOwner(DiagnosticChain diagnosticChain, Map map)
    {
        return this.slot.validateHasOwner(diagnosticChain, map);
    }

    /**
     * @see org.eclipse.uml2.Element#allOwnedElements()
     */
    public Set allOwnedElements()
    {
        return this.slot.allOwnedElements();
    }

    /**
     * @see org.eclipse.uml2.Element#mustBeOwned()
     */
    public boolean mustBeOwned()
    {
        return this.slot.mustBeOwned();
    }

    /**
     * @see org.eclipse.uml2.Element#createEAnnotation(java.lang.String)
     */
    public EAnnotation createEAnnotation(String string)
    {
        return this.slot.createEAnnotation(string);
    }

    /**
     * @see org.eclipse.uml2.Element#apply(org.eclipse.uml2.Stereotype)
     */
    public void apply(Stereotype stereotype)
    {
        this.slot.apply(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#getApplicableStereotype(java.lang.String)
     */
    public Stereotype getApplicableStereotype(String string)
    {
        return this.slot.getApplicableStereotype(string);
    }

    /**
     * @see org.eclipse.uml2.Element#getApplicableStereotypes()
     */
    public Set getApplicableStereotypes()
    {
        return this.slot.getApplicableStereotypes();
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedStereotype(java.lang.String)
     */
    public Stereotype getAppliedStereotype(String string)
    {
        return this.slot.getAppliedStereotype(string);
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedStereotypes()
     */
    public Set getAppliedStereotypes()
    {
        return this.slot.getAppliedStereotypes();
    }

    /**
     * @see org.eclipse.uml2.Element#getModel()
     */
    public Model getModel()
    {
        return this.slot.getModel();
    }

    /**
     * @see org.eclipse.uml2.Element#getNearestPackage()
     */
    public org.eclipse.uml2.Package getNearestPackage()
    {
        return this.slot.getNearestPackage();
    }

    /**
     * @see org.eclipse.uml2.Element#getValue(org.eclipse.uml2.Stereotype, java.lang.String)
     */
    public Object getValue(Stereotype stereotype, String string)
    {
        return this.slot.getValue(stereotype, string);
    }

    /**
     * @see org.eclipse.uml2.Element#isApplied(org.eclipse.uml2.Stereotype)
     */
    public boolean isApplied(Stereotype stereotype)
    {
        return this.slot.isApplied(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#isRequired(org.eclipse.uml2.Stereotype)
     */
    public boolean isRequired(Stereotype stereotype)
    {
        return this.slot.isRequired(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#setValue(org.eclipse.uml2.Stereotype, java.lang.String, java.lang.Object)
     */
    public void setValue(Stereotype stereotype, String string, Object object)
    {
        this.slot.setValue(stereotype, string, object);
    }

    /**
     * @see org.eclipse.uml2.Element#hasValue(org.eclipse.uml2.Stereotype, java.lang.String)
     */
    public boolean hasValue(Stereotype stereotype, String string)
    {
        return this.slot.hasValue(stereotype, string);
    }

    /**
     * @see org.eclipse.uml2.Element#unapply(org.eclipse.uml2.Stereotype)
     */
    public void unapply(Stereotype stereotype)
    {
        this.slot.unapply(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#destroy()
     */
    public void destroy()
    {
        this.slot.destroy();
    }

    /**
     * @see org.eclipse.uml2.Element#getAppliedVersion(org.eclipse.uml2.Stereotype)
     */
    public String getAppliedVersion(Stereotype stereotype)
    {
        return this.slot.getAppliedVersion(stereotype);
    }

    /**
     * @see org.eclipse.uml2.Element#addKeyword(java.lang.String)
     */
    public void addKeyword(String string)
    {
        this.slot.addKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.Element#getKeywords()
     */
    public Set getKeywords()
    {
        return this.slot.getKeywords();
    }

    /**
     * @see org.eclipse.uml2.Element#hasKeyword(java.lang.String)
     */
    public boolean hasKeyword(String string)
    {
        return this.slot.hasKeyword(string);
    }

    /**
     * @see org.eclipse.uml2.Element#removeKeyword(java.lang.String)
     */
    public void removeKeyword(String string)
    {
        this.slot.removeKeyword(string);
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotations()
     */
    public EList getEAnnotations()
    {
        return this.slot.getEAnnotations();
    }

    /**
     * @see org.eclipse.emf.ecore.EModelElement#getEAnnotation(java.lang.String)
     */
    public EAnnotation getEAnnotation(String string)
    {
        return this.slot.getEAnnotation(string);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eAdapters()
     */
    public EList eAdapters()
    {
        return this.slot.eAdapters();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eDeliver()
     */
    public boolean eDeliver()
    {
        return this.slot.eDeliver();
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eSetDeliver(boolean)
     */
    public void eSetDeliver(boolean b)
    {
        this.slot.eSetDeliver(b);
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eIsProxy()
     */
    public boolean eIsProxy()
    {
        return this.slot.eIsProxy();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eClass()
     */
    public EClass eClass()
    {
        return this.slot.eClass();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainer()
     */
    public EObject eContainer()
    {
        return this.slot.eContainer();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContents()
     */
    public EList eContents()
    {
        return this.slot.eContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eCrossReferences()
     */
    public EList eCrossReferences()
    {
        return this.slot.eCrossReferences();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eAllContents()
     */
    public TreeIterator eAllContents()
    {
        return this.slot.eAllContents();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainmentFeature()
     */
    public EReference eContainmentFeature()
    {
        return this.slot.eContainmentFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eContainingFeature()
     */
    public EStructuralFeature eContainingFeature()
    {
        return this.slot.eContainingFeature();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eResource()
     */
    public Resource eResource()
    {
        return this.slot.eResource();
    }

    /**
     * @see org.eclipse.emf.ecore.EObject#eGet(org.eclipse.emf.ecore.EStructuralFeature)
     */
    public Object eGet(EStructuralFeature eStructuralFeature)
    {
        return this.slot.eGet(eStructuralFeature);
    }

    /**
     * @see org.eclipse.emf.common.notify.Notifier#eNotify(org.eclipse.emf.common.notify.Notification)
     */
    public void eNotify(Notification notification)
    {
        this.slot.eNotify(notification);
    }
}
