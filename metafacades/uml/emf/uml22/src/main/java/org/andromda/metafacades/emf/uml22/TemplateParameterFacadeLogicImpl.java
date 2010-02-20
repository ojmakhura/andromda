package org.andromda.metafacades.emf.uml22;


import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.ClassifierTemplateParameter;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.ParameterableElement;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.TemplateParameterFacade.
 *
 * @see org.andromda.metafacades.uml.TemplateParameterFacade
 */
public class TemplateParameterFacadeLogicImpl
    extends TemplateParameterFacadeLogic
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(TemplateParameterFacadeLogicImpl.class);

    /**
     * @param metaObject
     * @param context
     */
    public TemplateParameterFacadeLogicImpl(
        final ClassifierTemplateParameter metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getParameter()
     */
    @Override
    protected ParameterableElement handleGetParameter()
    {
        // TODO: Map to ParameterImpl return type?
        return this.metaObject.getOwnedParameteredElement();
    }

    /**
     * @see org.andromda.metafacades.uml.TemplateParameterFacade#getDefaultElement()
     */
    @Override
    protected ParameterableElement handleGetDefaultElement()
    {
        // TODO: Be sure it works with RSM / MD11.5
        return this.metaObject.getDefault();
    }

    // The methods below are a hack, because TemplateParameter no longer inherits from Parameter in UML2
    // so the Parameter methods normally part of the FacadeLogic must be implemented here. Model hierarchy is: 
    // ownedTemplateSignature (type=RedefinableTemplateSignature) with ownedParameteredElement
    //   -> ownedParameter (type=ClassifierTemplateParameter) with constrainingClassifier
    //     -> ownedParameteredElement (type=DataType) with name
    /**
     * @return getOwnedParameteredElement().getName()
     */
    public final String getName()
    {
        // ParameterableElement is actually uml:DataType in the model, even though it doesn't inherit from
        DataType type = (DataType) this.metaObject.getOwnedParameteredElement();
        // Assumes no templating of template types, no array types, same declared/impl types
        return type.getName();
    }
    /**
     * @return getOwnedParameteredElement().getName()
     */
    public final String getGetterSetterTypeName()
    {
        // ParameterableElement is actually uml:DataType in the model, even though it doesn't inherit from
        // Assumes no templating of template types, no array types, same declared/impl types
        // Classifier name is the parameter type
        return this.getType().getFullyQualifiedName();
    }

    /**
     * @return getOwnedParameteredElement()
     */
    public final ClassifierFacade getType()
    {
        ClassifierFacade getType2r = null;
        ClassifierTemplateParameter param = (ClassifierTemplateParameter)this.metaObject;
        // param.getConstrainingClassifiers()) for UML2 3.0
        Classifier type = param.getConstrainingClassifier();
        /*if (type==null && param.getConstrainingClassifiers().size()==1)
        {
            type = param.getConstrainingClassifiers().get(0);
        }*/
        Object result = this.shieldedElement(type);
        try
        {
            getType2r = (ClassifierFacade)result;
        }
        catch (ClassCastException ex)
        {
            // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
            TemplateParameterFacadeLogicImpl.logger.warn("incorrect metafacade cast for TemplateParameterFacadeLogicImpl.getType ClassifierFacade " + handleGetParameter() + ": " + result);
        }
        return getType2r;
    }

    /**
     * @return getOwnedParameteredElement().getName()
     */
    public final String getFullyQualifiedName()
    {
        ClassifierFacade getClassifier2r = null;
        String name = null;
        DataType type = (DataType) this.metaObject.getOwnedParameteredElement();
        Object result = this.shieldedElement(type);
        try
        {
            getClassifier2r = (ClassifierFacade)result;
            name = getClassifier2r.getFullyQualifiedName(true);
        }
        catch (ClassCastException ex)
        {
            // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
            TemplateParameterFacadeLogicImpl.logger.warn("incorrect metafacade cast for TemplateParameterFacadeLogicImpl.getFullyQualifiedName ClassifierFacade " + handleGetParameter() + ": " + result);
        }
        // templateParameterFacade has no post constraints
        return name;
    }

    /**
     * @return getOwnedParameteredElement().getName()
     */
    public final int getLower()
    {
        int lower = 0;
        ClassifierFacade type = this.getType();
        if (type != null)
        {
            if (type.isPrimitive())
            {
                lower = 1;
            }
        }
        return  lower;        
    }

    /**
     * Takes lower bound and datatype primitive/wrapped into account.
     * @return GetterName
     */
    public final String getGetterName()
    {
        ClassifierFacade getClassifier2r = this.getType();
        String name = null;
        DataType type = (DataType) this.metaObject.getOwnedParameteredElement();
        Object result = this.shieldedElement(type);
        try
        {
            getClassifier2r = (ClassifierFacade)result;
            name = UMLMetafacadeUtils.getGetterPrefix(getClassifier2r, this.getLower()) + StringUtils.capitalize(this.getName());
        }
        catch (ClassCastException ex)
        {
            // Bad things happen if the metafacade type mapping in metafacades.xml is wrong - Warn
            TemplateParameterFacadeLogicImpl.logger.warn("incorrect metafacade cast for TemplateParameterFacadeLogicImpl.getFullyQualifiedName ClassifierFacade " + handleGetParameter() + ": " + result);
        }
        // templateParameterFacade has no post constraints
        return name;
    }

    /**
     * @return SetterName
     */
    public final String getSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }
}
