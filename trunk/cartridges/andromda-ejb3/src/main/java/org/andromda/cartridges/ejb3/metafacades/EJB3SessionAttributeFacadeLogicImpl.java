package org.andromda.cartridges.ejb3.metafacades;

import java.util.ArrayList;

import org.andromda.cartridges.ejb3.EJB3Profile;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacade
 */
public class EJB3SessionAttributeFacadeLogicImpl
    extends EJB3SessionAttributeFacadeLogic
{

    // ---------------- constructor -------------------------------

    public EJB3SessionAttributeFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    // ---------------- methods -------------------------------

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacade#getTransactionType()
     */
    protected String handleGetTransactionType()
    {
        return (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_EJB_TRANSACTION_TYPE, true);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamAttribute()
     */
    protected boolean handleIsSeamAttribute()
    {
        boolean isSeamAttribute = false;
        if (this.isSeamBijectionIn() ||
                this.isSeamBijectionLogger() ||
                this.isSeamBijectionOut() ||
                this.isSeamBijectionRequestParameter() ||
                this.isSeamDataModel() ||
                this.isSeamDataModelSelection())
        {
            isSeamAttribute = true;
        }
        return isSeamAttribute;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamBijectionIn()
     */
    protected boolean handleIsSeamBijectionIn()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_BIJECTION_IN);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleGetSeamBijectionInParameters()
     */
    protected String handleGetSeamBijectionInParameters()
    {
        ArrayList parameters = new ArrayList();
        if(!isRequired())
        {
            parameters.add("required = false");
        }
        else
        {
            if(BooleanUtils.toBoolean(
                    (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_BIJECTION_IN_CREATE, true)))
            {
                parameters.add("create = true");
            }
        }
        String value = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_BIJECTION_IN_VALUE, true);
        if(StringUtils.isNotBlank(value))
        {
            parameters.add("value = \"" + value + "\"");
        }

        return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamBijectionOut()
     */
    protected boolean handleIsSeamBijectionOut()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_BIJECTION_OUT);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleGetSeamBijectionOutParameters()
     */
    protected String handleGetSeamBijectionOutParameters()
    {
        ArrayList parameters = new ArrayList();
        if(!isRequired())
        {
            parameters.add("required = false");
        }

        String scope = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_BIJECTION_OUT_SCOPE_TYPE, true);
        if(StringUtils.isNotBlank(scope))
        {
            parameters.add("scope = org.jboss.seam.ScopeType." + scope);
        }

        String value = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_BIJECTION_OUT_VALUE, true);
        if(StringUtils.isNotBlank(value))
        {
            parameters.add("value = \"" + value + "\"");
        }

        return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamValidationValid()
     */
    protected boolean handleIsSeamValidationValid()
    {
        boolean isSeamValidComponent = false;
        if (this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_VALIDATION_VALID))
        {
            isSeamValidComponent = true;
        }
        return isSeamValidComponent;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamDataModel()
     */
    protected boolean handleIsSeamDataModel()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_DATA_DATAMODEL);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleGetSeamDataModelParameters()
     */
    protected String handleGetSeamDataModelParameters()
    {
        ArrayList parameters = new ArrayList();
        String value = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_DATA_DATAMODEL_VALUE, true);
        if(StringUtils.isNotBlank(value))
        {
            parameters.add("value = \"" + value + "\"");
        }
        String scope = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_DATA_DATAMODEL_SCOPE_TYPE, true);
        if(StringUtils.isNotBlank(scope))
        {
            parameters.add("scope = org.jboss.seam.ScopeType." + scope);
        }

        return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamDataModelSelection()
     */
    protected boolean handleIsSeamDataModelSelection()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_DATA_DATAMODEL_SELECTION);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleGetSeamDataModelSelectionParameters()
     */
    protected String handleGetSeamDataModelSelectionParameters()
    {
        ArrayList parameters = new ArrayList();
        String value = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_DATA_DATAMODEL_SELECTION_VALUE, true);
        if(StringUtils.isNotBlank(value))
        {
            parameters.add("value = \"" + value + "\"");
        }

        return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamDataModelSelection()
     */
    protected boolean handleIsSeamDataModelSelectionIndex()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_DATA_DATAMODEL_SELECTION_INDEX);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleGetSeamDataModelSelectionIndexParameters()
     */
    protected String handleGetSeamDataModelSelectionIndexParameters()
    {
        ArrayList parameters = new ArrayList();
        String value = (String)this.findTaggedValue(
                EJB3Profile.TAGGEDVALUE_SEAM_DATA_DATAMODEL_SELECTION_INDEX_VALUE, true);
        if(StringUtils.isNotBlank(value))
        {
            parameters.add("value = \"" + value + "\"");
        }

        return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamBijectionLogger()
     */
    protected boolean handleIsSeamBijectionLogger()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_BIJECTION_LOGGER);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleGetSeamBijectionLoggerParameters()
     */
    protected String handleGetSeamBijectionLoggerParameters()
    {
        if(!this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_BIJECTION_LOGGER))
        {
            return null;
        }
        else
        {
            ArrayList parameters = new ArrayList();
            String value = (String)this.findTaggedValue(EJB3Profile.TAGGEDVALUE_SEAM_BIJECTION_LOGGER_VALUE, true);
            if(StringUtils.isNotBlank(value))
            {
                parameters.add("value = \"" + value + "\"");
            }

            return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
        }
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleIsSeamBijectionRequestParameter()
     */
    protected boolean handleIsSeamBijectionRequestParameter()
    {
        return this.hasStereotype(EJB3Profile.STEREOTYPE_SEAM_BIJECTION_REQUEST_PARAMETER);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3SessionAttributeFacadeLogic#handleGetSeamBijectionRequestParameterParameters()
     */
    protected String handleGetSeamBijectionRequestParameterParameters()
    {
        ArrayList parameters = new ArrayList();
        String value = (String)this.findTaggedValue(
                EJB3Profile.TAGGEDVALUE_SEAM_BIJECTION_REQUEST_PARAMETER_VALUE, true);
        if(StringUtils.isNotBlank(value))
        {
            parameters.add("value = \"" + value + "\"");
        }

        return EJB3MetafacadeUtils.buildAnnotationParameters(parameters);
    }
}