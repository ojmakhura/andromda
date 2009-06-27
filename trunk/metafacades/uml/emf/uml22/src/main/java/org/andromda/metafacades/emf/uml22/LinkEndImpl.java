package org.andromda.metafacades.emf.uml22;

import java.util.Map;
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
 * @author RJF3
 */
public class LinkEndImpl implements LinkEnd
{
    final Slot slot;

    LinkEndImpl(final Slot slot)
    {
        this.slot = slot;
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

    public EList getValues()
    {
        return this.slot.getValues();
    }

    public ValueSpecification getValue(String string)
    {
        return this.slot.getValue(string, null);
    }

    public ValueSpecification createValue(EClass eClass)
    {
        return this.slot.createValue(null, null, eClass);
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
        return this.slot.createOwnedComment();
    }

    public Comment createOwnedComment()
    {
        return this.slot.createOwnedComment();
    }

    public boolean validateNotOwnSelf(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.slot.validateNotOwnSelf(diagnosticChain, map);
    }

    public boolean validateHasOwner(DiagnosticChain diagnosticChain, Map<Object, Object> map)
    {
        return this.slot.validateHasOwner(diagnosticChain, map);
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

    public EList<Stereotype> getAppliedStereotypes()
    {
        return this.slot.getAppliedStereotypes();
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

    public boolean isApplied(Stereotype stereotype)
    {
        return this.slot.isStereotypeApplied(stereotype);
    }

    public boolean isRequired(Stereotype stereotype)
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

    /*public void unapply(Stereotype stereotype)
    {
        this.slot.unapply(stereotype);
    }*/

    public void destroy()
    {
        this.slot.destroy();
    }

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

    /**
     * @see org.eclipse.uml2.uml.Slot#createValue(String, org.eclipse.uml2.uml.Type, org.eclipse.emf.ecore.EClass)
     */
    public ValueSpecification createValue(String name, Type type, EClass class1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#getValue(String, org.eclipse.uml2.uml.Type)
     */
    public ValueSpecification getValue(String name, Type type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Slot#getValue(String, org.eclipse.uml2.uml.Type, boolean, org.eclipse.emf.ecore.EClass, boolean)
     */
    public ValueSpecification getValue(String name, Type type, boolean ignoreCase, EClass class1,
            boolean createOnDemand)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eclipse.uml2.uml.Element#applyStereotype(org.eclipse.uml2.uml.Stereotype)
     */
    public EObject applyStereotype(Stereotype stereotype)
    {
        // TODO Auto-generated method stub
        return null;
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
}
