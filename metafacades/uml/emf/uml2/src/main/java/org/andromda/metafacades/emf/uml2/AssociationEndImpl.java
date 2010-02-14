package org.andromda.metafacades.emf.uml2;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.eclipse.uml2.AggregationKind;
import org.eclipse.uml2.Association;
import org.eclipse.uml2.Class;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.Comment;
import org.eclipse.uml2.DataType;
import org.eclipse.uml2.Dependency;
import org.eclipse.uml2.Deployment;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.MultiplicityElement;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Namespace;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.PackageableElement;
import org.eclipse.uml2.Property;
import org.eclipse.uml2.RedefinableElement;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.StringExpression;
import org.eclipse.uml2.TemplateBinding;
import org.eclipse.uml2.TemplateParameter;
import org.eclipse.uml2.TemplateSignature;
import org.eclipse.uml2.Type;
import org.eclipse.uml2.ValueSpecification;
import org.eclipse.uml2.VisibilityKind;

/**
 * Implementation of AssociationEnd. We extend Property, we keep a reference to
 * the original property, and we defer almost all method calls to it.
 *
 * @author Cédric Jeanneret
 * @author Bob Fields
 */
public class AssociationEndImpl
    implements AssociationEnd
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(AssociationEndImpl.class);

    /** UML2 3.0: Property no longer inherits from TemplateableElement
     * org.eclipse.uml2.uml.Property
     */
    final Property property;

    /**
     * @param p
     */
    AssociationEndImpl(final Property p)
    {
        this.property = p;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof Attribute)
        {
            Property other = ((AttributeImpl)obj).property;
            return this.property.equals(other);
        }
        if (obj instanceof AssociationEnd)
        {
            Property other = ((AssociationEndImpl)obj).property;
            return this.property.equals(other);
        }
        return this.property.equals(obj);
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
     * @see org.eclipse.uml2.Element#addKeyword(String)
     */
    public void addKeyword(final String arg0)
    {
        this.property.addKeyword(arg0);
    }

    /**
     * @see org.eclipse.uml2.NamedElement#allNamespaces()
     */
    public List allNamespaces()
    {
        return this.property.allNamespaces();
    }

    /**
     * @see org.eclipse.uml2.Element#allOwnedElements()
     */
    public Set allOwnedElements()
    {
        return this.property.allOwnedElements();
    }

    /**
     * @param arg0
     * @see org.eclipse.uml2.Element#apply(Stereotype)
     */
    public void apply(final Stereotype arg0)
    {
        this.property.apply(arg0);
    }

    /**
     * @param arg0
     * @return this.property.createDefaultValue(eClass)
     */
    public ValueSpecification createDefaultValue(final EClass arg0)
    {
        return this.property.createDefaultValue(arg0);
    }

    public Dependency createDependency(final NamedElement arg0)
    {
        return this.property.createDependency(arg0);
    }

    public Deployment createDeployment()
    {
        return this.property.createDeployment();
    }

    public Deployment createDeployment(final EClass arg0)
    {
        return this.property.createDeployment(arg0);
    }

    public EAnnotation createEAnnotation(final String arg0)
    {
        return this.property.createEAnnotation(arg0);
    }

    public ValueSpecification createLowerValue(final EClass arg0)
    {
        return this.property.createLowerValue(arg0);
    }

    public StringExpression createNameExpression()
    {
        return this.property.createNameExpression();
    }

    public StringExpression createNameExpression(final EClass arg0)
    {
        return this.property.createNameExpression(arg0);
    }

    public Comment createOwnedComment()
    {
        return this.property.createOwnedComment();
    }

    public Comment createOwnedComment(final EClass arg0)
    {
        return this.property.createOwnedComment(arg0);
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#createOwnedTemplateSignature()
     */
    public TemplateSignature createOwnedTemplateSignature()
    {
        //return this.property.createOwnedTemplateSignature();
        logger.error("AssociationEndImpl.createOwnedTemplateSignature is UML14 only");
        return null;
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#createOwnedTemplateSignature(org.eclipse.emf.ecore.EClass)
     */
    public TemplateSignature createOwnedTemplateSignature(final EClass arg0)
    {
        //return this.property.createOwnedTemplateSignature(arg0);
        logger.error("AssociationEndImpl.createOwnedTemplateSignature(arg0) is UML14 only");
        return null;
    }

    public Property createQualifier()
    {
        return this.property.createQualifier();
    }

    public Property createQualifier(final EClass arg0)
    {
        return this.property.createQualifier(arg0);
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#createTemplateBinding()
     */
    public TemplateBinding createTemplateBinding()
    {
        //return this.property.createTemplateBinding();
        logger.error("AssociationEndImpl.createTemplateBinding is UML14 only");
        return null;
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#createTemplateBinding(org.eclipse.emf.ecore.EClass)
     */
    public TemplateBinding createTemplateBinding(final EClass arg0)
    {
        //return this.property.createTemplateBinding(arg0);
        logger.error("AssociationEndImpl.createTemplateBinding(arg0) is UML14 only");
        return null;
    }

    public ValueSpecification createUpperValue(final EClass arg0)
    {
        return this.property.createUpperValue(arg0);
    }

    public void destroy()
    {
        this.property.destroy();
    }

    public EList eAdapters()
    {
        return this.property.eAdapters();
    }

    public TreeIterator eAllContents()
    {
        return this.property.eAllContents();
    }

    public EClass eClass()
    {
        return this.property.eClass();
    }

    public EObject eContainer()
    {
        return this.property.eContainer();
    }

    public EStructuralFeature eContainingFeature()
    {
        return this.property.eContainingFeature();
    }

    public EReference eContainmentFeature()
    {
        return this.property.eContainmentFeature();
    }

    public EList eContents()
    {
        return this.property.eContents();
    }

    public EList eCrossReferences()
    {
        return this.property.eCrossReferences();
    }

    public boolean eDeliver()
    {
        return this.property.eDeliver();
    }

    public Object eGet(final EStructuralFeature arg0)
    {
        return this.property.eGet(arg0);
    }

    public Object eGet(
        final EStructuralFeature arg0,
        final boolean arg1)
    {
        return this.property.eGet(
            arg0,
            arg1);
    }

    public boolean eIsProxy()
    {
        return this.property.eIsProxy();
    }

    public boolean eIsSet(final EStructuralFeature arg0)
    {
        return this.property.eIsSet(arg0);
    }

    public void eNotify(final Notification arg0)
    {
        this.property.eNotify(arg0);
    }

    public Resource eResource()
    {
        return this.property.eResource();
    }

    public void eSet(
        final EStructuralFeature arg0,
        final Object arg1)
    {
        this.property.eSet(
            arg0,
            arg1);
    }

    public void eSetDeliver(final boolean arg0)
    {
        this.property.eSetDeliver(arg0);
    }

    public void eUnset(final EStructuralFeature arg0)
    {
        this.property.eUnset(arg0);
    }

    public AggregationKind getAggregation()
    {
        return this.property.getAggregation();
    }

    public Stereotype getApplicableStereotype(final String arg0)
    {
        return this.property.getApplicableStereotype(arg0);
    }

    public Set getApplicableStereotypes()
    {
        return this.property.getApplicableStereotypes();
    }

    public Stereotype getAppliedStereotype(final String arg0)
    {
        return this.property.getAppliedStereotype(arg0);
    }

    public Set getAppliedStereotypes()
    {
        return this.property.getAppliedStereotypes();
    }

    public String getAppliedVersion(final Stereotype arg0)
    {
        return this.property.getAppliedVersion(arg0);
    }

    public Association getAssociation()
    {
        return this.property.getAssociation();
    }

    public Property getAssociationEnd()
    {
        return this.property.getAssociationEnd();
    }

    public Class getClass_()
    {
        return this.property.getClass_();
    }

    public EList getClientDependencies()
    {
        return this.property.getClientDependencies();
    }

    public Dependency getClientDependency(final String arg0)
    {
        return this.property.getClientDependency(arg0);
    }

    public DataType getDatatype()
    {
        return this.property.getDatatype();
    }

    public String getDefault()
    {
        return this.property.getDefault();
    }

    public ValueSpecification getDefaultValue()
    {
        return this.property.getDefaultValue();
    }

    public PackageableElement getDeployedElement(final String arg0)
    {
        return this.property.getDeployedElement(arg0);
    }

    public EList getDeployedElements()
    {
        return this.property.getDeployedElements();
    }

    public Deployment getDeployment(final String arg0)
    {
        return this.property.getDeployment(arg0);
    }

    public EList getDeployments()
    {
        return this.property.getDeployments();
    }

    public EAnnotation getEAnnotation(final String arg0)
    {
        return this.property.getEAnnotation(arg0);
    }

    public EList getEAnnotations()
    {
        return this.property.getEAnnotations();
    }

    public EList getEnds()
    {
        return this.property.getEnds();
    }

    public Classifier getFeaturingClassifier(final String arg0)
    {
        return this.property.getFeaturingClassifier(arg0);
    }

    public EList getFeaturingClassifiers()
    {
        return this.property.getFeaturingClassifiers();
    }

    public Set getKeywords()
    {
        return this.property.getKeywords();
    }

    public String getLabel()
    {
        return this.property.getLabel();
    }

    public String getLabel(final boolean arg0)
    {
        return this.property.getLabel(arg0);
    }

    public int getLower()
    {
        return this.property.getLower();
    }

    public ValueSpecification getLowerValue()
    {
        return this.property.getLowerValue();
    }

    public Model getModel()
    {
        return this.property.getModel();
    }

    public String getName()
    {
        return this.property.getName();
    }

    public StringExpression getNameExpression()
    {
        return this.property.getNameExpression();
    }

    public Namespace getNamespace()
    {
        return this.property.getNamespace();
    }

    public Package getNearestPackage()
    {
        return this.property.getNearestPackage();
    }

    public Property getOpposite()
    {
        return this.property.getOpposite();
    }

    public EList getOwnedComments()
    {
        return this.property.getOwnedComments();
    }

    public EList getOwnedElements()
    {
        return this.property.getOwnedElements();
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#getOwnedTemplateSignature()
     */
    public TemplateSignature getOwnedTemplateSignature()
    {
        //return this.property.getOwnedTemplateSignature();
        logger.error("AssociationEndImpl.getOwnedTemplateSignature is UML14 only");
        return null;
    }

    public Element getOwner()
    {
        return this.property.getOwner();
    }

    public Association getOwningAssociation()
    {
        return this.property.getOwningAssociation();
    }

    public TemplateParameter getOwningParameter()
    {
        return this.property.getOwningParameter();
    }

    public String getQualifiedName()
    {
        return this.property.getQualifiedName();
    }

    public Property getQualifier(final String arg0)
    {
        return this.property.getQualifier(arg0);
    }

    public EList getQualifiers()
    {
        return this.property.getQualifiers();
    }

    public RedefinableElement getRedefinedElement(final String arg0)
    {
        return this.property.getRedefinedElement(arg0);
    }

    public EList getRedefinedElements()
    {
        return this.property.getRedefinedElements();
    }

    public EList getRedefinedProperties()
    {
        return this.property.getRedefinedProperties();
    }

    public Property getRedefinedProperty(final String arg0)
    {
        return this.property.getRedefinedProperty(arg0);
    }

    public Classifier getRedefinitionContext(final String arg0)
    {
        return this.property.getRedefinitionContext(arg0);
    }

    public EList getRedefinitionContexts()
    {
        return this.property.getRedefinitionContexts();
    }

    public EList getSubsettedProperties()
    {
        return this.property.getSubsettedProperties();
    }

    public Property getSubsettedProperty(final String arg0)
    {
        return this.property.getSubsettedProperty(arg0);
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#getTemplateBindings()
     */
    public EList getTemplateBindings()
    {
        //return this.property.getTemplateBindings();
        logger.error("AssociationEndImpl.getTemplateBindings is UML14 only");
        return null;
    }

    public TemplateParameter getTemplateParameter()
    {
        return this.property.getTemplateParameter();
    }

    public Type getType()
    {
        return this.property.getType();
    }

    public int getUpper()
    {
        return this.property.getUpper();
    }

    public ValueSpecification getUpperValue()
    {
        return this.property.getUpperValue();
    }

    public Object getValue(
        final Stereotype arg0,
        final String arg1)
    {
        return this.property.getValue(
            arg0,
            arg1);
    }

    public VisibilityKind getVisibility()
    {
        return this.property.getVisibility();
    }

    public boolean hasKeyword(final String arg0)
    {
        return this.property.hasKeyword(arg0);
    }

    public boolean hasValue(
        final Stereotype arg0,
        final String arg1)
    {
        return this.property.hasValue(
            arg0,
            arg1);
    }

    public boolean includesCardinality(final int arg0)
    {
        return this.property.includesCardinality(arg0);
    }

    public boolean includesMultiplicity(final MultiplicityElement arg0)
    {
        return this.property.includesMultiplicity(arg0);
    }

    public boolean isApplied(final Stereotype arg0)
    {
        return this.property.isApplied(arg0);
    }

    public boolean isComposite()
    {
        return this.property.isComposite();
    }

    public boolean isConsistentWith(final RedefinableElement arg0)
    {
        return this.property.isConsistentWith(arg0);
    }

    public boolean isDerived()
    {
        return this.property.isDerived();
    }

    public boolean isDerivedUnion()
    {
        return this.property.isDerivedUnion();
    }

    public boolean isDistinguishableFrom(
        final NamedElement arg0,
        final Namespace arg1)
    {
        return this.property.isDistinguishableFrom(
            arg0,
            arg1);
    }

    public boolean isLeaf()
    {
        return this.property.isLeaf();
    }

    public boolean isMultivalued()
    {
        return this.property.isMultivalued();
    }

    public boolean isNavigable()
    {
        return this.property.isNavigable();
    }

    public boolean isOrdered()
    {
        return this.property.isOrdered();
    }

    public boolean isReadOnly()
    {
        return this.property.isReadOnly();
    }

    public boolean isRedefinitionContextValid(final RedefinableElement arg0)
    {
        return this.property.isRedefinitionContextValid(arg0);
    }

    public boolean isRequired(final Stereotype arg0)
    {
        return this.property.isRequired(arg0);
    }

    public boolean isStatic()
    {
        return this.property.isStatic();
    }

    public boolean isUnique()
    {
        return this.property.isUnique();
    }

    public int lower()
    {
        return this.property.lower();
    }

    public int lowerBound()
    {
        return this.property.lowerBound();
    }

    public boolean mustBeOwned()
    {
        return this.property.mustBeOwned();
    }

    public Property opposite()
    {
        return this.property.opposite();
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#parameterableElements()
     */
    public Set parameterableElements()
    {
        //return this.property.parameterableElements();
        logger.error("AssociationEndImpl.property.parameterableElements is UML14 only");
        return null;
    }

    public String qualifiedName()
    {
        return this.property.qualifiedName();
    }

    public void removeKeyword(final String arg0)
    {
        this.property.removeKeyword(arg0);
    }

    public String separator()
    {
        return this.property.separator();
    }

    public void setAggregation(final AggregationKind arg0)
    {
        this.property.setAggregation(arg0);
    }

    public void setAssociation(final Association arg0)
    {
        this.property.setAssociation(arg0);
    }

    public void setAssociationEnd(final Property arg0)
    {
        this.property.setAssociationEnd(arg0);
    }

    public void setBooleanDefault(final boolean arg0)
    {
        this.property.setBooleanDefault(arg0);
    }

    public void setDatatype(final DataType arg0)
    {
        this.property.setDatatype(arg0);
    }

    public void setDefaultValue(final ValueSpecification arg0)
    {
        this.property.setDefaultValue(arg0);
    }

    public void setIntegerDefault(final int arg0)
    {
        this.property.setIntegerDefault(arg0);
    }

    public void setIsDerived(final boolean arg0)
    {
        this.property.setIsDerived(arg0);
    }

    public void setIsDerivedUnion(final boolean arg0)
    {
        this.property.setIsDerivedUnion(arg0);
    }

    public void setIsLeaf(final boolean arg0)
    {
        this.property.setIsLeaf(arg0);
    }

    public void setIsOrdered(final boolean arg0)
    {
        this.property.setIsOrdered(arg0);
    }

    public void setIsReadOnly(final boolean arg0)
    {
        this.property.setIsReadOnly(arg0);
    }

    public void setIsStatic(final boolean arg0)
    {
        this.property.setIsStatic(arg0);
    }

    public void setIsUnique(final boolean arg0)
    {
        this.property.setIsUnique(arg0);
    }

    public void setLowerBound(final int arg0)
    {
        this.property.setLowerBound(arg0);
    }

    public void setLowerValue(final ValueSpecification arg0)
    {
        this.property.setLowerValue(arg0);
    }

    public void setName(final String arg0)
    {
        this.property.setName(arg0);
    }

    public void setNameExpression(final StringExpression arg0)
    {
        this.property.setNameExpression(arg0);
    }

    public void setNavigable(final boolean arg0)
    {
        this.property.setNavigable(arg0);
    }

    /**
     * @return null
     * @see org.eclipse.uml2.TemplateableElement#setOwnedTemplateSignature(org.eclipse.uml2.TemplateSignature)
     */
    public void setOwnedTemplateSignature(final TemplateSignature arg0)
    {
        //this.property.setOwnedTemplateSignature(arg0);
        logger.error("AssociationEndImpl.property.setOwnedTemplateSignature(arg0) is UML14 only");
    }

    public void setOwningAssociation(final Association arg0)
    {
        this.property.setOwningAssociation(arg0);
    }

    public void setOwningParameter(final TemplateParameter arg0)
    {
        this.property.setOwningParameter(arg0);
    }

    public void setStringDefault(final String arg0)
    {
        this.property.setStringDefault(arg0);
    }

    public void setTemplateParameter(final TemplateParameter arg0)
    {
        this.property.setTemplateParameter(arg0);
    }

    public void setType(final Type arg0)
    {
        this.property.setType(arg0);
    }

    public void setUnlimitedNaturalDefault(final int arg0)
    {
        this.property.setUnlimitedNaturalDefault(arg0);
    }

    public void setUpperBound(final int arg0)
    {
        this.property.setUpperBound(arg0);
    }

    public void setUpperValue(final ValueSpecification arg0)
    {
        this.property.setUpperValue(arg0);
    }

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

    public void setVisibility(final VisibilityKind arg0)
    {
        this.property.setVisibility(arg0);
    }

    public Set subsettingContext()
    {
        return this.property.subsettingContext();
    }

    public void unapply(final Stereotype arg0)
    {
        this.property.unapply(arg0);
    }

    public int upper()
    {
        return this.property.upper();
    }

    public int upperBound()
    {
        return this.property.upperBound();
    }

    public boolean validateDerivedUnionIsDerived(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateDerivedUnionIsDerived(
            arg0,
            arg1);
    }

    public boolean validateHasOwner(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateHasOwner(
            arg0,
            arg1);
    }

    public boolean validateLowerEqLowerbound(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateLowerEqLowerbound(
            arg0,
            arg1);
    }

    public boolean validateLowerGe0(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateLowerGe0(
            arg0,
            arg1);
    }

    public boolean validateMultiplicityOfComposite(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateMultiplicityOfComposite(
            arg0,
            arg1);
    }

    public boolean validateNavigablePropertyRedefinition(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateNavigablePropertyRedefinition(
            arg0,
            arg1);
    }

    public boolean validateNavigableReadonly(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateNavigableReadonly(
            arg0,
            arg1);
    }

    public boolean validateNoName(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateNoName(
            arg0,
            arg1);
    }

    public boolean validateNotOwnSelf(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateNotOwnSelf(
            arg0,
            arg1);
    }

    public boolean validateOppositeIsOtherEnd(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateOppositeIsOtherEnd(
            arg0,
            arg1);
    }

    public boolean validateQualifiedName(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateQualifiedName(
            arg0,
            arg1);
    }

    public boolean validateRedefinitionConsistent(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateRedefinitionConsistent(
            arg0,
            arg1);
    }

    public boolean validateRedefinitionContextValid(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateRedefinitionContextValid(
            arg0,
            arg1);
    }

    public boolean validateSubsettingContext(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateSubsettingContext(
            arg0,
            arg1);
    }

    public boolean validateSubsettingRules(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateSubsettingRules(
            arg0,
            arg1);
    }

    public boolean validateUpperEqUpperbound(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateUpperEqUpperbound(
            arg0,
            arg1);
    }

    public boolean validateUpperGeLower(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateUpperGeLower(
            arg0,
            arg1);
    }

    public boolean validateUpperGt0(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateUpperGt0(
            arg0,
            arg1);
    }

    public boolean validateVisibilityNeedsOwnership(
        final DiagnosticChain arg0,
        final Map arg1)
    {
        return this.property.validateVisibilityNeedsOwnership(
            arg0,
            arg1);
    }
}
