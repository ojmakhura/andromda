package org.andromda.metafacades.uml14;

import java.util.Collection;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Classifier;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationFacade.
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
    public EnumerationFacadeLogicImpl(Classifier metaObject, String context)
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
        final String nameMask = String.valueOf(
                this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiterals()
     */
    @Override
    protected Collection<AttributeFacade> handleGetLiterals()
    {
        Collection<AttributeFacade> literals = this.getAttributes();
        CollectionUtils.filter(
            literals,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean isLiteral = true;
                    final AttributeFacade attribute = (AttributeFacade)object; 
                    if (attribute.isEnumerationMember())
                    {
                        isLiteral = false;
                    }
                    return isLiteral;
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
        Collection<AttributeFacade> variables = super.getAttributes();
        CollectionUtils.filter(
            variables,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean isMember = false;
                    final AttributeFacade attribute = (AttributeFacade)object;
                    if (attribute.isEnumerationMember())
                    {
                        isMember = true;
                    }
                    return isMember;
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
    protected ClassifierFacade handleGetLiteralType()
    {
        ClassifierFacade type = null;
        final Collection<AttributeFacade> literals = this.getLiterals();
        if (literals != null && !literals.isEmpty())
        {
            type = ((AttributeFacade)literals.iterator().next()).getType();
            /*ModelElementFacade literal = (ModelElementFacade)literals.iterator().next();
            if (literal instanceof AttributeFacade)
            {
                type = ((AttributeFacade)literals.iterator().next()).getType();
            }*/
        }
        return type;
    }
}