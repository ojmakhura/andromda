package org.andromda.cartridges.hibernate.metafacades;

import java.util.Collection;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.*;

/**
 * Mock facade, used as a test data holder for AssociationLinkManagerFinderTest.
 * 
 * @see AssociationLinkManagerFinderTest
 * @since 25.10.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 */
public class MockAssociationEndFacade implements AssociationEndFacade {

    private String oneOrMany;

    private boolean navigable;

    private boolean aggregation;

    private boolean composition;

    private AssociationEndFacade otherEnd;

    private ClassifierFacade type;

    public MockAssociationEndFacade(ClassifierFacade type, String oneOrMany,
            boolean navigable, boolean aggregation, boolean composition) {
        this.type = type;
        this.oneOrMany = oneOrMany;
        this.navigable = navigable;
        this.aggregation = aggregation;
        this.composition = composition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getAssociation()
     */
    public AssociationFacade getAssociation() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterName()
     */
    public String getGetterName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    public String getGetterSetterTypeName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getOtherEnd()
     */
    public AssociationEndFacade getOtherEnd() {
        return otherEnd;
    }
    
    public void setOtherEnd (AssociationEndFacade otherEnd) {
        this.otherEnd = otherEnd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getSetterName()
     */
    public String getSetterName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getType()
     */
    public ClassifierFacade getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isAggregation()
     */
    public boolean isAggregation() {
        return aggregation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isComposition()
     */
    public boolean isComposition() {
        return composition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany()
     */
    public boolean isMany() {
        return "many".equals(oneOrMany);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2Many()
     */
    public boolean isMany2Many() {
        return isMany() && otherEnd.isMany();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2One()
     */
    public boolean isMany2One() {
        return isMany() && !otherEnd.isMany();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isNavigable()
     */
    public boolean isNavigable() {
        return navigable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2Many()
     */
    public boolean isOne2Many() {
        return !isMany() && otherEnd.isMany();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2One()
     */
    public boolean isOne2One() {
        return !isMany() && !otherEnd.isMany();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOrdered()
     */
    public boolean isOrdered() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isReadOnly()
     */
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    public boolean isRequired() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#initialize()
     */
    public void initialize() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#validateInvariants(java.util.Collection)
     */
    public void validateInvariants(Collection validationMessages) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValue(java.lang.String)
     */
    public Object findTaggedValue(String tagName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValues(java.lang.String)
     */
    public Collection findTaggedValues(String tagName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints()
     */
    public Collection getConstraints() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints(java.lang.String)
     */
    public Collection getConstraints(String kind) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTargetDependencies()
     */
    public Collection getTargetDependencies() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getSourceDependencies()
     */
    public Collection getSourceDependencies() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String,
     *      int, boolean)
     */
    public String getDocumentation(String indent, int lineLength,
            boolean htmlStyle) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String,
     *      int)
     */
    public String getDocumentation(String indent, int lineLength) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String)
     */
    public String getDocumentation(String indent) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName(boolean)
     */
    public String getFullyQualifiedName(boolean modelName) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getId()
     */
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLanguageMappings()
     */
    public Mappings getLanguageMappings() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getModel()
     */
    public ModelFacade getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getNameSpace()
     */
    public NamespaceFacade getNameSpace() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    public String getPackageName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackagePath()
     */
    public String getPackagePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypeNames()
     */
    public Collection getStereotypeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypes()
     */
    public Collection getStereotypes() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTaggedValues()
     */
    public Collection getTaggedValues() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#getVisibility()
     */
    public String getVisibility() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasExactStereotype(java.lang.String)
     */
    public boolean hasExactStereotype(String stereotypeName) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasStereotype(java.lang.String)
     */
    public boolean hasStereotype(String stereotypeName) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraint(java.lang.String,
     *      java.lang.String)
     */
    public String translateConstraint(String name, String translation) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String)
     */
    public String[] translateConstraints(String translation) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String,
     *      java.lang.String)
     */
    public String[] translateConstraints(String kind, String translation) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isChild()
     */
    public boolean isChild() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getActivityGraphContext()
     */
    public ActivityGraphFacade getActivityGraphContext() {
        // TODO Auto-generated method stub
        return null;
    }
}
