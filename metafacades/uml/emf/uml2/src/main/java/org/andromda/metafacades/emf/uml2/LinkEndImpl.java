package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.Slot;
import org.eclipse.uml2.InstanceSpecification;
import org.eclipse.uml2.ValueSpecification;
import org.eclipse.uml2.StructuralFeature;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.Model;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;

import java.util.Map;
import java.util.Set;

public class LinkEndImpl implements LinkEnd
{
    final Slot slot;

    LinkEndImpl(final Slot slot)
    {
        this.slot = slot;
    }

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

    public int hashCode()
    {
        return this.slot.hashCode();
    }

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

    public EList getValues()
    {
        return this.slot.getValues();
    }

    public ValueSpecification getValue(String string)
    {
        return this.slot.getValue(string);
    }

    public ValueSpecification createValue(EClass eClass)
    {
        return this.slot.createValue(eClass);
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

    public EList getOwnedElements()
    {
        return this.slot.getOwnedElements();
    }

    public Element getOwner()
    {
        return this.slot.getOwner();
    }

    public EList getOwnedComments()
    {
        return this.slot.getOwnedComments();
    }

    public Comment createOwnedComment(EClass eClass)
    {
        return this.slot.createOwnedComment(eClass);
    }

    public Comment createOwnedComment()
    {
        return this.slot.createOwnedComment();
    }

    public boolean validateNotOwnSelf(DiagnosticChain diagnosticChain, Map map)
    {
        return this.slot.validateNotOwnSelf(diagnosticChain, map);
    }

    public boolean validateHasOwner(DiagnosticChain diagnosticChain, Map map)
    {
        return this.slot.validateHasOwner(diagnosticChain, map);
    }

    public Set allOwnedElements()
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
        this.slot.apply(stereotype);
    }

    public Stereotype getApplicableStereotype(String string)
    {
        return this.slot.getApplicableStereotype(string);
    }

    public Set getApplicableStereotypes()
    {
        return this.slot.getApplicableStereotypes();
    }

    public Stereotype getAppliedStereotype(String string)
    {
        return this.slot.getAppliedStereotype(string);
    }

    public Set getAppliedStereotypes()
    {
        return this.slot.getAppliedStereotypes();
    }

    public Model getModel()
    {
        return this.slot.getModel();
    }

    public org.eclipse.uml2.Package getNearestPackage()
    {
        return this.slot.getNearestPackage();
    }

    public Object getValue(Stereotype stereotype, String string)
    {
        return this.slot.getValue(stereotype, string);
    }

    public boolean isApplied(Stereotype stereotype)
    {
        return this.slot.isApplied(stereotype);
    }

    public boolean isRequired(Stereotype stereotype)
    {
        return this.slot.isRequired(stereotype);
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
        this.slot.unapply(stereotype);
    }

    public void destroy()
    {
        this.slot.destroy();
    }

    public String getAppliedVersion(Stereotype stereotype)
    {
        return this.slot.getAppliedVersion(stereotype);
    }

    public void addKeyword(String string)
    {
        this.slot.addKeyword(string);
    }

    public Set getKeywords()
    {
        return this.slot.getKeywords();
    }

    public boolean hasKeyword(String string)
    {
        return this.slot.hasKeyword(string);
    }

    public void removeKeyword(String string)
    {
        this.slot.removeKeyword(string);
    }

    public EList getEAnnotations()
    {
        return this.slot.getEAnnotations();
    }

    public EAnnotation getEAnnotation(String string)
    {
        return this.slot.getEAnnotation(string);
    }

    public EList eAdapters()
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

    public EList eContents()
    {
        return this.slot.eContents();
    }

    public EList eCrossReferences()
    {
        return this.slot.eCrossReferences();
    }

    public TreeIterator eAllContents()
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
}
