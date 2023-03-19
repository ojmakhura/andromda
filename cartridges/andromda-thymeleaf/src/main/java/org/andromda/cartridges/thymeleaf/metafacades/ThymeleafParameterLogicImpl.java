package org.andromda.cartridges.thymeleaf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.andromda.cartridges.thymeleaf.ThymeleafUtils;
import org.andromda.cartridges.web.CartridgeWebGlobals;
import org.andromda.cartridges.web.CartridgeWebProfile;
import org.andromda.cartridges.web.CartridgeWebUtils;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.web.MetafacadeWebUtils;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafParameter.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafParameter
 */
public class ThymeleafParameterLogicImpl
    extends ThymeleafParameterLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafParameterLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }
    
    private static final String DEFAULT = "default";
    private static final String EMPTY_STRING = "";
    private static final String BOOLEAN_FALSE = "false";
    //private static final String DEFAULT_TYPE = "PathParam";

    private static final String QUOTE = "\"";
    private static final String RPARENS = "(";
    private static final String LPARENS = ")";
    private static final String AT = "@";

    @Override
    protected String handleGetRestPathParam() {
        String pathParam = (String)this.findTaggedValue(CartridgeWebGlobals.REST_PATH_PARAM);
        
        pathParam = AT + handleGetRestParamType() + "(\"" + pathParam + "\")";
        return pathParam;
    }

    @Override
    protected String handleGetRestParamType() {
        String paramType = (String)this.findTaggedValue(CartridgeWebGlobals.REST_PARAM_TYPE);
        if (StringUtils.isBlank(paramType) || paramType.equals(DEFAULT))
        {
            paramType = EMPTY_STRING;
        }
        else
        {
            String pathSegment = handleGetRestPathSegment();
            if (StringUtils.isBlank(pathSegment))
            {
                // paramType always needed with annotation
                pathSegment = this.getName();
            }
            paramType = "@thymeleaf.ws.rs." + paramType + RPARENS + QUOTE + pathSegment + QUOTE + LPARENS;
        }

        return paramType;
    }

    @Override
    protected boolean handleIsRestEncoded() {
        String restEncoded = (String)this.findTaggedValue(CartridgeWebGlobals.REST_ENCODED);
        if (StringUtils.isBlank(restEncoded) || restEncoded.equals(DEFAULT))
        {
            restEncoded = BOOLEAN_FALSE;
        }

        return Boolean.valueOf(restEncoded);
    }

    @Override
    protected String handleGetRestPathSegment() {
        String pathSegment = (String)this.findTaggedValue(CartridgeWebGlobals.REST_PATH_SEGMENT);
        if (StringUtils.isBlank(pathSegment) || pathSegment.equals(DEFAULT))
        {
            pathSegment = EMPTY_STRING;
        }
        return pathSegment;
    }

    @Override
    protected Boolean handleGetComponent() {
        
        return MetafacadeWebUtils.isComponent(this.getType());
    }
}