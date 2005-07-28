package org.andromda.cartridges.bpm4jsf.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.bpm4jsf.BPM4JSFGlobals;
import org.andromda.cartridges.bpm4jsf.BPM4JSFProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter.
 *
 * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter
 */
public class JSFParameterLogicImpl
    extends JSFParameterLogic
{

    public JSFParameterLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        final StringBuffer messageKey = new StringBuffer();

        if (!this.isNormalizeMessages())
        {
            if (isActionParameter())
            {
                final JSFAction action = (JSFAction)this.getAction();
                if (action != null)
                {
                    messageKey.append(action.getMessageKey());
                    messageKey.append('.');
                }
            }
            else
            {
                final JSFView view = (JSFView)this.getView();
                if (view != null)
                {
                    messageKey.append(view.getMessageKey());
                    messageKey.append('.');
                }
            }
            messageKey.append("param.");
        }

        messageKey.append(StringUtilsHelper.toResourceMessageKey(super.getName()));
        return messageKey.toString();
    }
 
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDocumentationKey()
     */
    protected String handleGetDocumentationKey()
    {
        return getMessageKey() + '.' + BPM4JSFGlobals.DOCUMENTATION_MESSAGE_KEY_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getDocumentationValue(()
     */
    protected String handleGetDocumentationValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        return value == null ? "" : value;
    }
    
    /**
     * Indicates whether or not we should normalize messages.
     *
     * @return true/false
     */
    private final boolean isNormalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(BPM4JSFGlobals.NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(super.getName()); // the actual name is used for displaying
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#getTableColumnNames()
     */
    protected Collection handleGetTableColumnNames()
    {
        final Collection tableColumnNames = new ArrayList();
        if (!this.isActionParameter() && !this.isControllerOperationArgument())
        {
            final Collection taggedValues = findTaggedValues(BPM4JSFProfile.TAGGEDVALUE_TABLE_COLUMNS);
            if (!taggedValues.isEmpty())
            {
                for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
                {
                    final String taggedValue = StringUtils.trimToNull(String.valueOf(iterator.next()));
                    if (taggedValue != null)
                    {
                        final String[] properties = taggedValue.split("[,\\s]+");
                        for (int ctr = 0; ctr < properties.length; ctr++)
                        {
                            final String property = properties[ctr];
                            tableColumnNames.add(property);
                        }
                    }
                }
            }
        }
        return tableColumnNames;
    }
    
    /**
     * @see org.andromda.cartridges.bpm4jsf.metafacades.JSFParameter#isTable()
     */
    protected boolean handleIsTable()
    {
        boolean isTable = false;

        final ClassifierFacade type = getType();
        if (type != null)
        {
            isTable = (type.isCollectionType() || type.isArrayType()) && !this.getTableColumnNames().isEmpty();
        }
        return isTable;
    }

}