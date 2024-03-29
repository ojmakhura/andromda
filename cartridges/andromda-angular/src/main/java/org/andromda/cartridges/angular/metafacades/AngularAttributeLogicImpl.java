// license-header java merge-point
//
// Generated by: MetafacadeLogicImpl.vsl in andromda-meta-cartridge.
package org.andromda.cartridges.angular.metafacades;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.andromda.cartridges.angular.AngularGlobals;
import org.andromda.cartridges.angular.AngularProfile;
import org.andromda.cartridges.angular.AngularUtils;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EntityAttribute;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents an attribute on a classifier used by a Angular application.
 * MetafacadeLogic implementation for org.andromda.cartridges.angular.metafacades.AngularAttribute.
 *
 * @see org.andromda.cartridges.angular.metafacades.AngularAttribute
 */
public class AngularAttributeLogicImpl
    extends AngularAttributeLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * Public constructor for AngularAttributeLogicImpl
     * @see org.andromda.cartridges.angular.metafacades.AngularAttribute
     */
    public AngularAttributeLogicImpl (Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @param ownerParameter
     * @return validatorVars
     * @see AngularAttribute#getValidatorVars(AngularParameter)
     */
    protected Collection<List<String>> handleGetValidatorVars(AngularParameter ownerParameter)
    {
        return AngularUtils.getValidatorVars(
            (ModelElementFacade)this.THIS(),
            this.getType(),
            ownerParameter);
    }

    /**
     * @param ownerParameter
     * @return dateFormatter
     * @see AngularAttribute#getDateFormatter(org.andromda.cartridges.angular.metafacades.AngularParameter)
     */
    protected String handleGetDateFormatter(final AngularParameter ownerParameter)
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isDateType() ? this.getFormPropertyId(ownerParameter) + "DateFormatter" : null;
    }

    /**
     * @param ownerParameter
     * @return timeFormatter
     * @see AngularAttribute#getTimeFormatter(org.andromda.cartridges.angular.metafacades.AngularParameter)
     */
    protected String handleGetTimeFormatter(final AngularParameter ownerParameter)
    {
        final ClassifierFacade type = this.getType();
        return type != null && type.isTimeType() ? this.getFormPropertyId(ownerParameter) + "TimeFormatter" : null;
    }

    @Override
    protected Collection<ModelElementFacade> handleGetImports() {
        HashSet<ModelElementFacade> imports = new HashSet<>();

        for(AttributeFacade attribute : this.getType().getAttributes()) {
            if(attribute.getType().isEnumeration() || !attribute.getType().getAttributes().isEmpty()) {
                imports.add(attribute.getType());
            }
        }

        return imports;
    }

    @Override
    protected String handleGetAngularTypeName() {

        if(this.isInputFile() || this.getType().isBlobType()) {
            return "File";
        }

        

        return AngularUtils.getDatatype(this.getType().getFullyQualifiedName());
    }

    @Override
    protected String handleGetImportFilePath() {
        return this.getPackagePath() + '/';
    }
}