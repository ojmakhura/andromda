package org.andromda.cartridges.hibernate.metafacades;

import java.util.Collection;

import org.andromda.core.mapping.Mappings;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.NamespaceFacade;
import org.andromda.metafacades.uml.PackageFacade;


/**
 *
 * @since 25.10.2004
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class MockClassifierFacade implements ClassifierFacade {

    private String name;
    
    public MockClassifierFacade (String name) {
        this.name = name;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    public Collection getAbstractions() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArray()
     */
    public ClassifierFacade getArray() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    public Collection getAssociationEnds() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes()
     */
    public Collection getAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    public Collection getAttributes(boolean follow) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceAttributes()
     */
    public Collection getInstanceAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getJavaNullString()
     */
    public String getJavaNullString() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    public ClassifierFacade getNonArray() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    public String getOperationCallFromAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperations()
     */
    public Collection getOperations() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    public Collection getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticAttributes()
     */
    public Collection getStaticAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#getWrapperName()
     */
    public String getWrapperName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isAbstract()
     */
    public boolean isAbstract() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isArrayType()
     */
    public boolean isArrayType() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    public boolean isCollectionType() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDataType()
     */
    public boolean isDataType() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    public boolean isDateType() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isEnumeration()
     */
    public boolean isEnumeration() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isInterface()
     */
    public boolean isInterface() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isListType()
     */
    public boolean isListType() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitive()
     */
    public boolean isPrimitive() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ClassifierFacade#isSetType()
     */
    public boolean isSetType() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getAllGeneralizations()
     */
    public Collection getAllGeneralizations() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralization()
     */
    public GeneralizableElementFacade getGeneralization() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getGeneralizations()
     */
    public Collection getGeneralizations() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.GeneralizableElementFacade#getSpecializations()
     */
    public Collection getSpecializations() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#initialize()
     */
    public void initialize() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#validateInvariants(java.util.Collection)
     */
    public void validateInvariants(Collection validationMessages) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValue(java.lang.String)
     */
    public Object findTaggedValue(String tagName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#findTaggedValues(java.lang.String)
     */
    public Collection findTaggedValues(String tagName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints()
     */
    public Collection getConstraints() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getConstraints(java.lang.String)
     */
    public Collection getConstraints(String kind) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTargetDependencies()
     */
    public Collection getTargetDependencies() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getSourceDependencies()
     */
    public Collection getSourceDependencies() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String, int, boolean)
     */
    public String getDocumentation(String indent, int lineLength,
            boolean htmlStyle) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String, int)
     */
    public String getDocumentation(String indent, int lineLength) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getDocumentation(java.lang.String)
     */
    public String getDocumentation(String indent) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName(boolean)
     */
    public String getFullyQualifiedName(boolean modelName) {
        return name;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedName()
     */
    public String getFullyQualifiedName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getFullyQualifiedNamePath()
     */
    public String getFullyQualifiedNamePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getId()
     */
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getLanguageMappings()
     */
    public Mappings getLanguageMappings() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getModel()
     */
    public ModelFacade getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getNameSpace()
     */
    public NamespaceFacade getNameSpace() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackage()
     */
    public ModelElementFacade getPackage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackageName()
     */
    public String getPackageName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getPackagePath()
     */
    public String getPackagePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getRootPackage()
     */
    public PackageFacade getRootPackage() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypeNames()
     */
    public Collection getStereotypeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getStereotypes()
     */
    public Collection getStereotypes() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getTaggedValues()
     */
    public Collection getTaggedValues() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#getVisibility()
     */
    public String getVisibility() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasExactStereotype(java.lang.String)
     */
    public boolean hasExactStereotype(String stereotypeName) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#hasStereotype(java.lang.String)
     */
    public boolean hasStereotype(String stereotypeName) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraint(java.lang.String, java.lang.String)
     */
    public String translateConstraint(String name, String translation) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String)
     */
    public String[] translateConstraints(String translation) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.andromda.metafacades.uml.ModelElementFacade#translateConstraints(java.lang.String, java.lang.String)
     */
    public String[] translateConstraints(String kind, String translation) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isFileType() {
        return false;
    }
}
