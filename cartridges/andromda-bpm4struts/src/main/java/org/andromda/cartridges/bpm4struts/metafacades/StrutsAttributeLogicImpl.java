package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.Collection;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.ClassifierFacade;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.bpm4struts.metafacades.StrutsAttribute.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAttribute
 */
public class StrutsAttributeLogicImpl extends StrutsAttributeLogic {
    private static final long serialVersionUID = 34L;

    /**
     * @param metaObject
     * @param context
     */
    public StrutsAttributeLogicImpl(Object metaObject, String context) {
        super(metaObject, context);
    }

    /**
     * @return dateFormat
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAttribute#getDateFormat()
     */
    protected String handleGetDateFormat() {
        String dateFormat = null;

        if (this.getType() != null && this.getType().isDateType()) {
            final Object taggedValueObject = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_FORMAT);
            if (taggedValueObject == null) {
                dateFormat = (String) this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_DEFAULT_DATEFORMAT);
            } else {
                dateFormat = taggedValueObject.toString();
            }
        }

        return dateFormat;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAttributeLogic#handleGetDummyValue(int)
     */
    protected String handleGetDummyValue(int seed) {
        String dummyValue = null;

        final ClassifierFacade type = this.getType();

        if (type == null) {
            dummyValue = "";
        } else if (type.isDateType()) {
            dummyValue = "new java.util.Date()";
        } else if (type.isBooleanType()) {
            dummyValue = String.valueOf(seed % 2 == 0);
        } else if (type.isPrimitive()) {
            dummyValue = String.valueOf(seed);
        } else {
            dummyValue = '\"' + this.getName() + '-' + seed + '\"';
        }

        return dummyValue;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsAttributeLogic#handleIsSafeNamePresent()
     */
    protected boolean handleIsSafeNamePresent() {
        return Bpm4StrutsUtils.isSafeName(this.getName());
    }
}
