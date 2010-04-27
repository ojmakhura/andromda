package org.andromda.cartridges.ejb3.metafacades;

import org.andromda.cartridges.ejb3.EJB3Profile;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3WebServiceOperationFacade.
 *
 * @see EJB3WebServiceOperationFacade
 */
public class EJB3WebServiceOperationFacadeLogicImpl
    extends EJB3WebServiceOperationFacadeLogic
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(EJB3WebServiceOperationFacadeLogicImpl.class);

    /**
     * @param metaObject
     * @param context
     */
    public EJB3WebServiceOperationFacadeLogicImpl(final Object metaObject, final String context)
    {
        super (metaObject, context);
    }

    /**
     * @see EJB3WebServiceOperationFacade#isExposed()
     */
    @Override
    protected boolean handleIsExposed()
    {
        return this.getOwner().hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE) ||
            this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }

    /**
     * @see EJB3WebServiceOperationFacadeLogic#handleIsOneway()
     */
    @Override
    protected boolean handleIsOneway()
    {
        return BooleanUtils.toBoolean(
                (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_WEBSERVICE_OPERATION_ONEWAY));
    }

    /**
     * @see EJB3WebServiceOperationFacadeLogic#getAnnotatedSignature()
     */
    @Override
    protected String handleGetAnnotatedSignature()
    {
        final StringBuilder signature = new StringBuilder(this.getName());
        signature.append("(");
        signature.append(this.getAnnotatedTypedArgumentList(true, null));
        signature.append(")");
        return signature.toString();
    }

    /**
     * @param withArgumentNames
     * @param modifier
     * @return
     */
    private String getAnnotatedTypedArgumentList(final boolean withArgumentNames, final String modifier)
    {
        final StringBuilder buffer = new StringBuilder();
        boolean commaNeeded = false;
        for (ParameterFacade paramter : this.getArguments())
        {
            String type = null;
            if (paramter.getType() == null)
            {
                EJB3WebServiceOperationFacadeLogicImpl.logger.error(
                        "ERROR! No type specified for parameter --> '" + paramter.getName() +
                        "' on operation --> '" +
                        this.getName() +
                        "', please check your model");
            }
            else
            {
                type = paramter.getType().getFullyQualifiedName();
            }

            if (commaNeeded)
            {
                buffer.append(",");
            }
            buffer.append('\n');

            // Add WebParam annotation
            if (withArgumentNames)
            {
                buffer.append("        @javax.jws.WebParam(name = \"");
                buffer.append(StringUtils.capitalize(paramter.getName())).append("\")");
                buffer.append(" ");
            }
            if (StringUtils.isNotBlank(modifier))
            {
                buffer.append(modifier);
                buffer.append(" ");
            }
            buffer.append(type);
            if (withArgumentNames)
            {
                buffer.append(" ");
                buffer.append(paramter.getName());
            }
            commaNeeded = true;
        }
        buffer.append('\n');
        if (commaNeeded)
        {
            buffer.append("    ");
        }
        return buffer.toString();
    }

    /**
     * @see EJB3WebServiceOperationFacadeLogic#handleGetMethodName()
     */
    @Override
    protected String handleGetMethodName()
    {
        String methodName = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_WEBSERVICE_OPERATION_NAME);
        if (StringUtils.isBlank(methodName))
        {
            methodName = StringUtils.capitalize(this.getName());
        }
        return methodName;
    }

    /**
     * @see EJB3WebServiceOperationFacadeLogic#handleGetResultName()
     */
    @Override
    protected String handleGetResultName()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_WEBSERVICE_OPERATION_RESULT_NAME);
    }
}
