package org.andromda.metafacades.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Parameter;
import org.omg.uml.foundation.datatypes.ParameterDirectionKindEnum;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;


/**
 *
 * Metaclass facade implementation.
 *
 */
public class OperationFacadeLogicImpl
       extends OperationFacadeLogic
       implements org.andromda.metafacades.uml.OperationFacade
{
    // ---------------- constructor -------------------------------

    public OperationFacadeLogicImpl (org.omg.uml.foundation.core.Operation metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature()
     */
    public String getSignature()
    {
        return this.getSignature(true);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getSignature(boolean)
     */
    public String getSignature(boolean withArgumentNames)
    {
        StringBuffer signature = new StringBuffer(this.getName());
        signature.append("(");
        signature.append(this.getTypedArgumentList(withArgumentNames));
        signature.append(")");
        return signature.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getTypedArgumentList()
     */
    public String getTypedArgumentList() {
        return this.getTypedArgumentList(true);
    }


    private String getTypedArgumentList(boolean withArgumentNames)
    {
        StringBuffer sb = new StringBuffer();
        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                String type = null;
                if (p.getType() == null)
                {
                    this.logger.error("ERROR! No type specified for parameter --> '"
                        + p.getName() + "' on operation --> '"
                        + this.getName() + "', please chek your model");
                }
                else
                {
                    type =
                        ((ClassifierFacade) this.shieldedElement(p.getType()))
                            .getFullyQualifiedName();
                }

                if (commaNeeded)
                {
                    sb.append(", ");
                }
                sb.append(type);
                if (withArgumentNames) {
                    sb.append(" ");
                    sb.append(p.getName());
                }
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    public String getCall()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(metaObject.getName());
        sb.append("(");
        sb.append(getArgumentNames());
        sb.append(")");

        return sb.toString();
    }

    public String getArgumentNames()
    {
        StringBuffer sb = new StringBuffer();

        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                if (commaNeeded)
                {
                    sb.append(", ");
                }
                sb.append(p.getName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    public String getArgumentTypeNames()
    {
        StringBuffer sb = new StringBuffer();

        Iterator it = metaObject.getParameter().iterator();

        boolean commaNeeded = false;
        while (it.hasNext())
        {
            Parameter p = (Parameter) it.next();

            if (!ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                if (commaNeeded)
                {
                    sb.append(", ");
                }
                ParameterFacade facade = (ParameterFacade)shieldedElement(p);
                sb.append(facade.getType().getFullyQualifiedName());
                commaNeeded = true;
            }
        }
        return sb.toString();
    }

    protected Object handleGetType()
    {
        Collection parms = metaObject.getParameter();
        for (Iterator i = parms.iterator(); i.hasNext();)
        {
            Parameter p = (Parameter) i.next();
            if (ParameterDirectionKindEnum.PDK_RETURN.equals(p.getKind()))
            {
                return p.getType();
            }
        }

        return null;
    }

    public Collection getArguments()
    {
        Collection arguments = new ArrayList(metaObject.getParameter());

        CollectionUtils.filter(
            arguments,
            new Predicate() {
                public boolean evaluate(Object object) {
                    return !ParameterDirectionKindEnum.PDK_RETURN.equals(
                        ((Parameter)object).getKind());
                }
            });
       return this.shieldedElements(arguments);
    }

    public Object handleGetOwner()
    {
        return this.metaObject.getOwner();
    }

    protected Collection handleGetParameters()
    {
        return metaObject.getParameter();
    }

    public Object findTaggedValue(String name, boolean follow)
    {
        name = StringUtils.trimToEmpty(name);
        Object value = findTaggedValue(name);
        if (follow) {
            ClassifierFacade type = this.getType();
            while (value == null && type != null) {
                value = type.findTaggedValue(name);
                type = (ClassifierFacade)type.getGeneralization();
            }
        }
        return value;
    }

    public boolean isStatic()
    {
        return ScopeKindEnum.SK_CLASSIFIER.equals(this.metaObject.getOwnerScope());
    }

    public boolean isAbstract()
    {
        return metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#hasExceptions()
     */
    public boolean hasExceptions()
    {
        return !this.getExceptions().isEmpty();
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptions()
     */
    public Collection getExceptions()
    {
        Collection exceptions = new HashSet();

        // finds both exceptions and exception references
        final class ExceptionFilter implements Predicate
        {
            public boolean evaluate(Object object)
            {
                DependencyFacade dependency =
                    (DependencyFacade)object;
                // first check for exception references
                boolean hasException =
                    dependency.hasStereotype(
                        UMLProfile.STEREOTYPE_EXCEPTION_REF);

                // if there wasn't any exception reference
                // now check for actual exceptions
                if (!hasException) {
                    ModelElementFacade targetElement =
                        dependency.getTargetElement();
                    hasException =
                        targetElement != null &&
                        targetElement.hasStereotype(
                            UMLProfile.STEREOTYPE_EXCEPTION);
                }
                return hasException;
            }
        }

        // first get any dependencies on this operation's
        // owner (because these will represent the default exception(s))
        Collection ownerDependencies = this.getOwner().getDependencies();
        if (ownerDependencies != null && !ownerDependencies.isEmpty())
        {
            CollectionUtils.filter(ownerDependencies, new ExceptionFilter());
            exceptions.addAll(ownerDependencies);
        }

        Collection operationDependencies = this.getDependencies();
        // now get any exceptions directly on the operation
        if (operationDependencies != null && !operationDependencies.isEmpty())
        {
            CollectionUtils.filter(operationDependencies, new ExceptionFilter());
            exceptions.addAll(operationDependencies);
        }

        // now transform the dependency(s) to the actual exception(s)
        CollectionUtils.transform(
            exceptions,
            new Transformer()
            {
                public Object transform(Object object)
                {
                    return ((DependencyFacade)object).getTargetElement();
                }
            });
        return exceptions;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList()
     */
    public String getExceptionList() {
        return this.getExceptionList(null);
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#hasReturnType()
     */
    public boolean hasReturnType() {
        boolean hasReturnType = true;
        if (this.getType() != null) {
            hasReturnType =
                !StringUtils.trimToEmpty(
                    this.getType().getFullyQualifiedName()).equals("void");
        }
        return hasReturnType;
    }

    /**
     * @see org.andromda.metafacades.uml.OperationFacade#getExceptionList(java.lang.String)
     */
    public String getExceptionList(String initialExceptions) {
        initialExceptions = StringUtils.trimToEmpty(initialExceptions);
        StringBuffer exceptionList = new StringBuffer(initialExceptions);
        Collection exceptions = this.getExceptions();
        if (exceptions != null && !exceptions.isEmpty()) {
            if (StringUtils.isNotEmpty(initialExceptions)) {
                exceptionList.append(", ");
            }
            Iterator exceptionIt = exceptions.iterator();
            while (exceptionIt.hasNext()) {
                ModelElementFacade exception =
                    (ModelElementFacade)exceptionIt.next();
                exceptionList.append(exception.getFullyQualifiedName());
                if (exceptionIt.hasNext()) {
                    exceptionList.append(", ");
                }
            }
        }

        return exceptionList.toString();
    }

}
