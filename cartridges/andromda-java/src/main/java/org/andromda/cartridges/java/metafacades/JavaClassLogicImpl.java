package org.andromda.cartridges.java.metafacades;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TemplateParameterFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.java.metafacades.JavaClass.
 *
 * @see org.andromda.cartridges.java.metafacades.JavaClass
 */
public class JavaClassLogicImpl
    extends JavaClassLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public JavaClassLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * If WebFault stereotype, change name from XXException to XXFault. Class should
     * also have XXException stereotype so that the corresponding Exception referencing the
     * fault is also created.
     * @param fault true if name is to be converted to Fault version, for VO Template only
     * @return the class name.
     */
    public String getName(boolean fault)
    {
        String name = super.getName();
        if (fault && this.hasStereotype("WebFault"))
        {
            name = name.replace("Exception", "Fault");
            if (!name.contains("Fault"))
            {
                name += "Fault";
            }
        }
        return name;
    }

    /**
     * UML22 implementation for TemplateParameter logic
     * @return the class name.
     */
    public Object getType()
    {
        /*if (this.metaObject instanceof TemplateParameterFacade)
        {
            return
        }
        System.out.println(this.metaObject);*/
        return this.metaObject;
    }

    /**
     * UML22 implementation for TemplateParameter logic
     * @return the class name.
    public Object getOwner()
    {
        if (this.metaObject instanceof ClassifierFacade)
        {
            ClassifierFacade facade = (ClassifierFacade)this.metaObject;
            return facade.getOwner();
        }
        return this.metaObject;
    }
     */

    /**
     * @return InterfaceImplementionName
     * @see org.andromda.cartridges.java.metafacades.JavaClass#getInterfaceImplementationName()
     */
    @Override
    protected String handleGetInterfaceImplementationName()
    {
        return this.getInterfaceImplementionName().replaceAll(
            "\\{0\\}",
            this.getName());
    }

    /**
     * Gets the value of the {@link JavaGlobals#INTERFACE_IMPLEMENTATION_NAME_PATTERN}.
     *
     * @return the interface implementation name..
     */
    private String getInterfaceImplementionName()
    {
        return String.valueOf(this.getConfiguredProperty(JavaGlobals.INTERFACE_IMPLEMENTATION_NAME_PATTERN));
    }

    /**
     * @return InterfaceImplementationName
     * @see org.andromda.cartridges.java.metafacades.JavaClass#getFullyQualifiedInterfaceImplementationName()
     */
    @Override
    protected String handleGetFullyQualifiedInterfaceImplementationName()
    {
        final StringBuilder fullName = new StringBuilder();
        final String packageName = this.getPackageName();
        if (StringUtils.isNotBlank(packageName))
        {
            fullName.append(packageName).append('.');
        }
        return fullName.append(this.getInterfaceImplementationName()).toString();
    }

    /**
     * @return abstractImplementation
     * @see org.andromda.cartridges.java.metafacades.JavaClass#isAbstractInterfaceImplementation()
     */
    @Override
    protected boolean handleIsAbstractInterfaceImplementation()
    {
        boolean abstractImplementation = !this.getOperations().isEmpty();
        if (!abstractImplementation)
        {
            for (GeneralizableElementFacade generalizableElementFacade : this.getAllGeneralizations())
            {
                final ClassifierFacade classifier = (ClassifierFacade) generalizableElementFacade;
                abstractImplementation = !classifier.getOperations().isEmpty();
                if (abstractImplementation)
                {
                    break;
                }
            }
        }
        return abstractImplementation;
    }

    /**
     * @return templateParams
     * @see org.andromda.cartridges.java.metafacades.JavaClassLogic#getTemplateParameters()
     */
    //@Override
    protected String handleGetTemplateParams()
    {
        String fullName = "";
        if (this.isTemplateParametersPresent() &&
            BooleanUtils.toBoolean(
                Objects.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING), "")))
        {
            // we'll be constructing the parameter list in this buffer
            final StringBuilder buffer = new StringBuilder();

            // add the name we've constructed so far
            buffer.append(fullName);

            // start the parameter list
            buffer.append('<');

            // loop over the parameters, we are so to have at least one (see
            // outer condition)
            final Collection<TemplateParameterFacade> templateParameters = this.getTemplateParameters();
            for (Iterator<TemplateParameterFacade> parameterIterator = templateParameters.iterator(); parameterIterator.hasNext();)
            {
                final ModelElementFacade modelElement =
                    (parameterIterator.next()).getParameter();

                // TODO: UML14 returns ParameterFacade, UML2 returns ModelElementFacade, so types are wrong from fullyQualifiedName
                // Mapping from UML2 should return ParameterFacade, with a getType method. Need TemplateParameter.getName method.
                if (modelElement instanceof ParameterFacade)
                {
                    buffer.append(((ParameterFacade)modelElement).getType().getFullyQualifiedName());
                }
                else
                {
                    buffer.append(modelElement.getFullyQualifiedName());
                }

                if (parameterIterator.hasNext())
                {
                    buffer.append(", ");
                }
            }

            // we're finished listing the parameters
            buffer.append('>');

            // we have constructed the full name in the buffer
            fullName = buffer.toString();
        }
        return fullName;
    }
}
