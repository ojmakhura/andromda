package org.andromda.metafacades.emf.uml2;

import java.util.Collection;

import org.andromda.core.metafacade.MetafacadeImplsException;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.Enumeration;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationFacade
 */
public class EnumerationFacadeLogicImpl
    extends EnumerationFacadeLogic
{
    public EnumerationFacadeLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
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
    protected java.util.Collection handleGetLiterals()
    {
        if (metaObject instanceof Enumeration)
        {
            return this.shieldedElements(((Enumeration)metaObject).getOwnedLiterals());
        }
        return this.getAttributes();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationSignature()
     */
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
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationName()
     */
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
    protected Object handleGetLiteralType()
    {
        Object type = null;
        final Collection literals = this.getLiterals();
        if (literals != null && !literals.isEmpty())
        {
            ModelElementFacade literal = (ModelElementFacade)literals.iterator().next();
            if (literal instanceof AttributeFacade)
            {
                type = ((AttributeFacade)literals.iterator().next()).getType();
            }
            else
            {
                throw new MetafacadeImplsException("Note that real Literals are not supported yet.");
            }
        }
        return type;
    }
}