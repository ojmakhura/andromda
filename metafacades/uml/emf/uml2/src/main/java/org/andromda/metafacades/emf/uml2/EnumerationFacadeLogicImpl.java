package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import org.andromda.core.metafacade.MetafacadeConstants;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.Enumeration;
import org.eclipse.uml2.EnumerationLiteral;
import org.eclipse.uml2.NamedElement;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EnumerationFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationFacade
 * @author Bob Fields
 */
public class EnumerationFacadeLogicImpl
    extends EnumerationFacadeLogic
{
    /**
     * @param metaObject
     * @param context
     */
    public EnumerationFacadeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    @Override
    protected String handleGetName()
    {
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiterals()
     */
    @Override
    protected Collection handleGetLiterals()
    {
        // To Check: could be sufficient to return the collection of literals only
        //           without filtering
        final Collection literals = (this.metaObject instanceof Enumeration
            ? ((Enumeration)this.metaObject).getOwnedLiterals()
            : CollectionUtils.collect(this.getAttributes(), UmlUtilities.ELEMENT_TRANSFORMER));

        CollectionUtils.filter(
            literals,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    // evaluates to true in case it's a native literal
                    // or an attribute which is not considered a regular member
                    return
                        (object instanceof EnumerationLiteral) ||
                          (object instanceof AttributeFacade && !((AttributeFacade)object).isEnumerationMember());
                }
            }
        );
        return literals;
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getMemberVariables()
     */
    @Override
    protected Collection<AttributeFacade> handleGetMemberVariables()
    {
        // To Check: could be sufficient to return the collection of attributes only
        //           without filtering
        // http://galaxy.andromda.org/jira/browse/UMLMETA-87
        final Collection variables = (this.metaObject instanceof Enumeration
                ? ((Enumeration)this.metaObject).getOwnedAttributes()
                : CollectionUtils.collect(this.getAttributes(), UmlUtilities.ELEMENT_TRANSFORMER));

        CollectionUtils.filter(
            variables,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    // evaluates to true in case it's NOT a native literal
                    // but an attribute which considered as a regular member
                    return object instanceof AttributeFacade && ((AttributeFacade)object).isEnumerationMember();
                }
            }
        );
        return variables;
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationSignature()
     */
    @Override
    protected String handleGetFromOperationSignature()
    {
        final StringBuffer signature = new StringBuffer(this.getFromOperationName());
        final ClassifierFacade type = this.getLiteralType();
        if (type != null)
        {
            signature.append('(');
            signature.append(type.getFullyQualifiedName());
            signature.append(" value)");
        }
        return signature.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#isTypeSafe()
     */
    @Override
    protected boolean handleIsTypeSafe()
    {
        return BooleanUtils.toBoolean(
                String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.TYPE_SAFE_ENUMS_ENABLED)));
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationName()
     */
    @Override
    protected String handleGetFromOperationName()
    {
        final StringBuffer name = new StringBuffer("from");
        final ClassifierFacade type = this.getLiteralType();
        if (type != null)
        {
            name.append(StringUtils.capitalize(type.getName()));
        }
        return name.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiteralType()
     */
    @Override
    protected Object handleGetLiteralType()
    {
        Object type = null;
        final Collection<AttributeFacade> literals = this.getLiterals();
        if (literals != null && !literals.isEmpty())
        {
            ModelElementFacade literal = (ModelElementFacade)literals.iterator().next();
            if (literal instanceof AttributeFacade)
            {
                type = ((AttributeFacade)literals.iterator().next()).getType();
            }
            else
            {
                //String defaultType = String.valueOf(this.getConfiguredProperty(
                //    UMLMetafacadeProperties.DEFAULT_ENUMERATION_LITERAL_TYPE));
                type = UmlUtilities.findByFullyQualifiedName(
                    ((NamedElement)this.metaObject).eResource().getResourceSet(),
                    "datatype::String", // TODO: use this (doesn't work for some reason): UMLMetafacadeProperties.DEFAULT_ENUMERATION_LITERAL_TYPE,
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                    true);
                if (type==null)
                {
                    // Requires customized mapping in etc/*Mappings.xml files
                    type = UmlUtilities.findByFullyQualifiedName(
                    ((NamedElement)this.metaObject).eResource().getResourceSet(),
                    "UML2::String",
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                    true);
                }
                if (type==null)
                {
                    // Requires customized mapping in etc/*Mappings.xml files
                    type = UmlUtilities.findByFullyQualifiedName(
                    ((NamedElement)this.metaObject).eResource().getResourceSet(),
                    "PrimitiveTypes::String",
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                    true);
                }
                if (type==null)
                {
                    // Requires customized mapping in etc/*Mappings.xml files
                    type = UmlUtilities.findByFullyQualifiedName(
                    ((NamedElement)this.metaObject).eResource().getResourceSet(),
                    "UMLPrimitiveTypes::String",
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                    true);
                }
                if (type==null)
                {
                    // Requires customized mapping in etc/*Mappings.xml files
                    type = UmlUtilities.findByFullyQualifiedName(
                    ((NamedElement)this.metaObject).eResource().getResourceSet(),
                    "UMLTypes::String",
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                    true);
                }
                if (type==null)
                {
                    // Requires customized mapping in etc/*Mappings.xml files
                    type = UmlUtilities.findByFullyQualifiedName(
                    ((NamedElement)this.metaObject).eResource().getResourceSet(),
                    "String",
                    MetafacadeConstants.NAMESPACE_SCOPE_OPERATOR,
                    true);
                }
            }
        }
        return type;
    }
}