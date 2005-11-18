/*
 * Created on Oct 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.andromda.metafacades.emf.uml2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import org.eclipse.uml2.Dependency;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Model;
import org.eclipse.uml2.NamedElement;
import org.eclipse.uml2.Namespace;
import org.eclipse.uml2.Package;
import org.eclipse.uml2.Stereotype;
import org.eclipse.uml2.StringExpression;
import org.eclipse.uml2.TemplateBinding;
import org.eclipse.uml2.TemplateSignature;
import org.eclipse.uml2.VisibilityKind;


public class TagDefinitionImpl
    implements TagDefinition
{
    private String name;
    private String value;

    /**
     * Constructor
     * @param name
     * @param value
     */
    public TagDefinitionImpl(
        String name,
        String value)
    {
        this.name = name;
        this.value = value;
    }

    /**
     * @param object
     * @param object2
     */
    public TagDefinitionImpl(
        Object name,
        Object value)
    {
        if (name instanceof String && value instanceof String)
        {
            this.name = (String)name;
            this.value = (String)value;
        }
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    public Collection getValues()
    {
        ArrayList c = new ArrayList();
        c.add(value);
        return c;
    }

    public String toString()
    {
        return name + " : " + value;
    }

    public void setName(String arg0)
    {
        // TODO Auto-generated method stub
    }

    public String getQualifiedName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public VisibilityKind getVisibility()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setVisibility(VisibilityKind arg0)
    {
        // TODO Auto-generated method stub
    }

    public EList getClientDependencies()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Dependency getClientDependency(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public StringExpression getNameExpression()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setNameExpression(StringExpression arg0)
    {
        // TODO Auto-generated method stub
    }

    public StringExpression createNameExpression(EClass arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public StringExpression createNameExpression()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List allNamespaces()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isDistinguishableFrom(
        NamedElement arg0,
        Namespace arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String separator()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String qualifiedName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean validateVisibilityNeedsOwnership(
        DiagnosticChain arg0,
        Map arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Namespace getNamespace()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean validateNoName(
        DiagnosticChain arg0,
        Map arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean validateQualifiedName(
        DiagnosticChain arg0,
        Map arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getLabel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getLabel(boolean arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Dependency createDependency(NamedElement arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EList getTemplateBindings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TemplateBinding createTemplateBinding(EClass arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TemplateBinding createTemplateBinding()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TemplateSignature getOwnedTemplateSignature()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setOwnedTemplateSignature(TemplateSignature arg0)
    {
        // TODO Auto-generated method stub
    }

    public TemplateSignature createOwnedTemplateSignature(EClass arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TemplateSignature createOwnedTemplateSignature()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Set parameterableElements()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EList getOwnedElements()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Element getOwner()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EList getOwnedComments()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Comment createOwnedComment(EClass arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Comment createOwnedComment()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean validateNotOwnSelf(
        DiagnosticChain arg0,
        Map arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean validateHasOwner(
        DiagnosticChain arg0,
        Map arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Set allOwnedElements()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean mustBeOwned()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public EAnnotation createEAnnotation(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isApplied(Stereotype arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isRequired(Stereotype arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Set getApplicableStereotypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Stereotype getApplicableStereotype(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Set getAppliedStereotypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Stereotype getAppliedStereotype(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void apply(Stereotype arg0)
    {
        // TODO Auto-generated method stub
    }

    public void unapply(Stereotype arg0)
    {
        // TODO Auto-generated method stub
    }

    public Object getValue(
        Stereotype arg0,
        String arg1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(
        Stereotype arg0,
        String arg1,
        Object arg2)
    {
        // TODO Auto-generated method stub
    }

    public boolean hasValue(
        Stereotype arg0,
        String arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Model getModel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Package getNearestPackage()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void destroy()
    {
        // TODO Auto-generated method stub
    }

    public String getAppliedVersion(Stereotype arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Set getKeywords()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasKeyword(String arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void addKeyword(String arg0)
    {
        // TODO Auto-generated method stub
    }

    public void removeKeyword(String arg0)
    {
        // TODO Auto-generated method stub
    }

    public EList getEAnnotations()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EAnnotation getEAnnotation(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EClass eClass()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Resource eResource()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EObject eContainer()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EStructuralFeature eContainingFeature()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EReference eContainmentFeature()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public EList eContents()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TreeIterator eAllContents()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean eIsProxy()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public EList eCrossReferences()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object eGet(EStructuralFeature arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object eGet(
        EStructuralFeature arg0,
        boolean arg1)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void eSet(
        EStructuralFeature arg0,
        Object arg1)
    {
        // TODO Auto-generated method stub
    }

    public boolean eIsSet(EStructuralFeature arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void eUnset(EStructuralFeature arg0)
    {
        // TODO Auto-generated method stub
    }

    public EList eAdapters()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean eDeliver()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void eSetDeliver(boolean arg0)
    {
        // TODO Auto-generated method stub
    }

    public void eNotify(Notification arg0)
    {
        // TODO Auto-generated method stub
    }
}