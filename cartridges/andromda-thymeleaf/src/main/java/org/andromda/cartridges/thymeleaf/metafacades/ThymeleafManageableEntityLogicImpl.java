package org.andromda.cartridges.thymeleaf.metafacades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.andromda.cartridges.thymeleaf.ThymeleafGlobals;
import org.andromda.cartridges.thymeleaf.ThymeleafProfile;
import org.andromda.cartridges.thymeleaf.ThymeleafUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ManageableEntityAssociationEnd;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity.
 *
 * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity
 */
public class ThymeleafManageableEntityLogicImpl
    extends ThymeleafManageableEntityLogic
{
    private static final long serialVersionUID = 34L;
    
    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(ThymeleafManageableEntityLogicImpl.class);    
    
    /**
     * @param metaObject
     * @param context
     */
    public ThymeleafManageableEntityLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getName().toLowerCase() + "-crud"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getViewName()
     */
    protected String handleGetViewName()
    {
        return this.getName().toLowerCase() + "-crud";
    }

    /**
     * @return toResourceMessageKey(this.getName()) + ".view.title"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getViewTitleKey()
     */
    protected String handleGetViewTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".view.title";
    }

    /**
     * @return StringUtilsHelper.toPhrase(getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getViewTitleValue()
     */
    protected String handleGetViewTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return "manageableList"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getListName()
     */
    protected String handleGetListName()
    {
        return "manageableList";
    }

    /**
     * @return getManageablePackageName() + getNamespaceProperty() + this.getFormBeanClassName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getFormBeanType()
     */
    protected String handleGetFormBeanType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getFormBeanClassName();
    }

    /**
     * @return formBeanName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getFormBeanName()
     */
    protected String handleGetFormBeanName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(ThymeleafGlobals.FORM_BEAN_PATTERN));
        final String formBeanName = pattern.replaceFirst("\\{0\\}", "manage");
        return formBeanName.replaceFirst("\\{1\\}", this.getName());
    }

    /**
     * @return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".exception"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getExceptionKey()
     */
    protected String handleGetExceptionKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".exception";
    }

    /**
     * @return getManageablePackageName() + getNamespaceProperty() + getActionClassName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getActionType()
     */
    protected String handleGetActionType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getActionClassName();
    }

    /**
     * @return '/' + StringUtils.replace(this.getActionType(), getNamespaceProperty(), "/")
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getActionFullPath()
     */
    protected String handleGetActionFullPath()
    {
        return '/' + StringUtils.replace(this.getActionType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return '/' + getName() + "/Manage"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getActionPath()
     */
    protected String handleGetActionPath()
    {
        return super.getActionFullPath();
    }

    /**
     * @return "Manage" + getName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getActionClassName()
     */
    protected String handleGetActionClassName()
    {
        return getName();
    }

    /**
     * @return getViewFullPath()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getExceptionPath()
     */
    protected String handleGetExceptionPath()
    {
        return this.getViewFullPath();
    }

    /**
     * @return false
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#isPreload()
     */
    protected boolean handleIsPreload()
    {
        return false; //TODO think about...
//        return this.isCreate() || this.isRead() || this.isUpdate() || this.isDelete();
    }

    /**
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntityLogic#getManageableIdentifier()
     */
    @Override
    public org.andromda.metafacades.uml.ManageableEntityAttribute getManageableIdentifier()
    {
        return super.getManageableIdentifier();
    }

    /**
     * @return StringUtils.capitalize(this.getFormBeanName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getFormBeanClassName()
     */
    protected String handleGetFormBeanClassName()
    {
        return StringUtils.capitalize(this.getFormBeanName());
    }

    /**
     * @return StringUtils.replace(getFormBeanType(), getNamespaceProperty(), "/")
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getFormBeanFullPath()
     */
    protected String handleGetFormBeanFullPath()
    {
        return StringUtils.replace(this.getFormBeanType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return "getManageableList"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getListGetterName()
     */
    protected String handleGetListGetterName()
    {
        return "getManageableList";
    }

    /**
     * @return "setManageableList"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getListSetterName()
     */
    protected String handleGetListSetterName()
    {
        return "setManageableList";
    }

    /**
     * @return StringUtilsHelper.toResourceMessageKey(this.getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName());
    }

    /**
     * @return StringUtilsHelper.toPhrase(this.getName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    /**
     * @return getMessageKey() + ".online.help"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getOnlineHelpKey()
     */
    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    /**
     * @return onlineHelpValue
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getOnlineHelpValue()
     */
    protected String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No entity documentation has been specified" : value;
    }

    /**
     * @return getActionPath() + "Help"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getOnlineHelpActionPath()
     */
    protected String handleGetOnlineHelpActionPath()
    {
        return this.getActionPath() + "Help";
    }

    /**
     * @return '/' + getManageablePackagePath() + '/' + getName().toLowerCase() + "_help"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getOnlineHelpPagePath()
     */
    protected String handleGetOnlineHelpPagePath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getName().toLowerCase() + "_help";
    }

    /**
     * @return getTableExportTypes().indexOf("none") == -1
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#isTableExportable()
     */
    protected boolean handleIsTableExportable()
    {
        return this.getTableExportTypes().indexOf("none") == -1;
    }

    /**
     * @return null
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getTableExportTypes()
     */
    protected String handleGetTableExportTypes()
    {
        return null;
        //TODO a resolver
//        return ThymeleafUtils.getDisplayTagExportTypes(
//            this.findTaggedValues(ThymeleafProfile.TAGGEDVALUE_TABLE_EXPORT),
//            (String)getConfiguredProperty(ThymeleafGlobals.PROPERTY_DEFAULT_TABLE_EXPORT_TYPES) );
    }

    /**
     * @return tableMaxRows
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getTableMaxRows()
     */
    protected int handleGetTableMaxRows()
    {
        final Object taggedValue = this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_TABLE_MAXROWS);
        int pageSize;

        try
        {
            pageSize = Integer.parseInt(String.valueOf(taggedValue));
        }
        catch (Exception e)
        {
            pageSize = ThymeleafProfile.TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT;
        }

        return pageSize;
    }

    /**
     * @return tableSortable
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#isTableSortable()
     */
    protected boolean handleIsTableSortable()
    {
        final Object taggedValue = this.findTaggedValue(ThymeleafProfile.TAGGEDVALUE_TABLE_SORTABLE);
        return (taggedValue == null)
            ? ThymeleafProfile.TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE
            : Boolean.valueOf(String.valueOf(taggedValue)).booleanValue();
    }

    /**
     * @return controllerType
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getControllerType()
     */
     protected String handleGetControllerType()
     {
         return this.getManageablePackageName() + this.getNamespaceProperty() + this.getControllerName();
     }

    /**
     * @return StringUtils.uncapitalize(this.getName()) + "Controller"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getControllerBeanName()
     */
    protected String handleGetControllerBeanName()
    {
        return StringUtils.uncapitalize(this.getName()) + "Controller";
    }

    /**
     * @return '/' + StringUtils.replace(getControllerType(), getNamespaceProperty(), "/")
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getControllerFullPath()
     */
    protected String handleGetControllerFullPath()
    {
        return '/' + StringUtils.replace(this.getControllerType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return getName() + "Controller"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getControllerName()
     */
    protected String handleGetControllerName()
    {
        return this.getName() + "Controller";
    }

    /**
     * @return getName() + this.getConfiguredProperty(ThymeleafGlobals.CRUD_VALUE_OBJECT_SUFFIX)
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getValueObjectClassName()
     */
    protected String handleGetValueObjectClassName()
    {
        return getName() + this.getConfiguredProperty(ThymeleafGlobals.CRUD_VALUE_OBJECT_SUFFIX);
    }

    /**
     * @return formSerialVersionUID
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getFormSerialVersionUID()
     */
    protected String handleGetFormSerialVersionUID()
    {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.getFormBeanType());
        addSerialUIDData(buffer);
        return ThymeleafUtils.calcSerialVersionUID(buffer);
    }

    /**
     * @return actionSerialVersionUID
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getActionSerialVersionUID()
     */
    protected String handleGetActionSerialVersionUID()
    {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.getActionFullPath());
        addSerialUIDData(buffer);
        return ThymeleafUtils.calcSerialVersionUID(buffer);
    }

    /**
     * @return populatorName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getPopulatorName()
     */
    protected String handleGetPopulatorName()
    {
        return ObjectUtils.toString(this.getConfiguredProperty(ThymeleafGlobals.VIEW_POPULATOR_PATTERN)).replaceAll(
            "\\{0\\}",
            StringUtilsHelper.upperCamelCaseName(this.getFormBeanClassName()));
    }

    /**
     * @return '/' + StringUtils.replace(getPopulatorType(), getNamespaceProperty(), "/")
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getPopulatorFullPath()
     */
    protected String handleGetPopulatorFullPath()
    {
        return '/' + StringUtils.replace(this.getPopulatorType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return getManageablePackageName() + getNamespaceProperty() + getPopulatorName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getPopulatorType()
     */
    protected String handleGetPopulatorType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getPopulatorName();
    }

    /**
     * @return '/' + getManageablePackagePath() + '/' + getViewName()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getViewFullPath()
     */
    protected String handleGetViewFullPath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getViewName();
    }

    /**
     * @return '/' + getManageablePackagePath() + '/' + getName().toLowerCase() + "-ods-export"
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getViewFullPath()
     */
    protected String handleGetOdsExportFullPath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getName().toLowerCase()+".ods-export";
    }

    /**
     * @return isValidationRequired
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        for (final ManageableEntityAttribute attribute : this.getManageableAttributes())
        {
            if(attribute instanceof ThymeleafManageableEntityAttribute)
            {
                final ThymeleafManageableEntityAttribute thymeleafAttribute = (ThymeleafManageableEntityAttribute)attribute;
                if (thymeleafAttribute.isValidationRequired())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return searchFormBeanName
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getSearchFormBeanName()
     */
    protected String handleGetSearchFormBeanName()
    {
        final String pattern = ObjectUtils.toString(this.getConfiguredProperty(ThymeleafGlobals.FORM_BEAN_PATTERN));
        final String formBeanName = pattern.replaceFirst("\\{0\\}", "manage");
        return formBeanName.replaceFirst("\\{1\\}",this.getName() + "Search");
    }

    /**
     * @return searchFormBeanType
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getSearchFormBeanType()
     */
    protected String handleGetSearchFormBeanType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getSearchFormBeanClassName();
    }

    /**
     * @return searchFormBeanFullPath
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getSearchFormBeanFullPath()
     */
    protected String handleGetSearchFormBeanFullPath()
    {
        return StringUtils.replace(this.getSearchFormBeanType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return StringUtils.capitalize(this.getSearchFormBeanName())
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getSearchFormBeanClassName()
     */
    protected String handleGetSearchFormBeanClassName()
    {
        return StringUtils.capitalize(this.getSearchFormBeanName());
    }

    private Boolean usingManageableSearchable=null;
    private boolean isUsingManageableSearchable()
    {
        if(usingManageableSearchable == null)
        {
            for(final ManageableEntityAttribute attr: getManageableAttributes())
            {
                if(BooleanUtils.toBoolean(ObjectUtils.toString(attr.findTaggedValue(ThymeleafProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE))))
                {
                    usingManageableSearchable=true;
                    break;
                }
            }
            if(usingManageableSearchable == null)
            {
                for(final ManageableEntityAssociationEnd end: getManageableAssociationEnds())
                {
                    //TODO constant should go to ThymeleafProfile of UMLPROFILE
                    if(BooleanUtils.toBoolean(ObjectUtils.toString(end.findTaggedValue(ThymeleafProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE))))
                    {
                        usingManageableSearchable=true;
                        break;
                    }
                }
            }
            if(usingManageableSearchable == null)
            {
                usingManageableSearchable = false;
            }
        }
        return usingManageableSearchable;
    }
    
    
    /**
     * @return manageableSearchAttributes
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getManageableSearchAttributes()
     */
    protected Collection<ThymeleafManageableEntityAttribute> handleGetManageableSearchAttributes()
    {
        final Collection<ThymeleafManageableEntityAttribute> searchAttributes=new ArrayList<ThymeleafManageableEntityAttribute>();
        
        if(isUsingManageableSearchable())
        {
            for(final ManageableEntityAttribute attr: getManageableAttributes())
            {
                if(BooleanUtils.toBoolean(ObjectUtils.toString(attr.findTaggedValue(ThymeleafProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE))))
                {
                    searchAttributes.add((ThymeleafManageableEntityAttribute)attr);
                }
            }
        }
        else
        {
            for(ManageableEntityAttribute attr: getManageableAttributes())
            {
                if(attr.isDisplay() && !attr.getType().isBlobType() && !attr.getType().isClobType())
                {
                    searchAttributes.add((ThymeleafManageableEntityAttribute)attr);
                }
            }
        }

        return searchAttributes;
    }

    /**
     * @return getManageableAssociationEnds()
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getManageableSearchAssociationEnds()
     */
    protected Collection<ManageableEntityAssociationEnd> handleGetManageableSearchAssociationEnds()
    {
        final Collection<ManageableEntityAssociationEnd> searchAssociationEnds=new ArrayList<ManageableEntityAssociationEnd>();
        
        if(isUsingManageableSearchable())
        {
            for(final ManageableEntityAssociationEnd end: getManageableAssociationEnds())
            {
                if(BooleanUtils.toBoolean(ObjectUtils.toString(end.findTaggedValue(ThymeleafProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE)))
                   || end.isComposition())
                {
                    searchAssociationEnds.add(end);
                }
            }
        }
        else
        {
            for(final ManageableEntityAssociationEnd end: getManageableAssociationEnds())
            {
                if(end.isDisplay() || end.isComposition())
                {
                    searchAssociationEnds.add(end);
                }
            }
        }

        return searchAssociationEnds;
    }

    /**
     * @param element
     * @return isSearchable
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#isSearchable(Object)
     */
    protected boolean handleIsSearchable(Object element)
    {
       return getManageableSearchAttributes().contains(element) || getManageableSearchAssociationEnds().contains(element);
    }

    /**
     * @return the configured property denoting the character sequence to use for the separation of namespaces
     */
    private String getNamespaceProperty()
    {
        return (String)this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR);
    }

   private void addSerialUIDData(StringBuilder buffer)
   {
       for (final ManageableEntityAttribute attribute : this.getManageableAttributes())
       {
           buffer.append(attribute.getName());
       }
       for (final ManageableEntityAssociationEnd end : this.getManageableAssociationEnds())
       {
           buffer.append(end.getName());
       }
   }

   /**
    * @return allRoles
    * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getRoles()
    */
   @SuppressWarnings("rawtypes")
protected Collection handleGetRoles()
   {
       //copied form the Service <<Metafacade>>
       final Collection<DependencyFacade> roles = new ArrayList<DependencyFacade>(this.getTargetDependencies());
       CollectionUtils.filter(roles, new Predicate()
       {
           public boolean evaluate(final Object object)
           {
               DependencyFacade dependency = (DependencyFacade)object;
               return dependency != null && dependency.getSourceElement() instanceof Role;
           }
       });
       CollectionUtils.transform(roles, new Transformer()
       {
           public Object transform(final Object object)
           {
               return ((DependencyFacade)object).getSourceElement();
           }
       });
       @SuppressWarnings({ "unchecked" })
    final Collection allRoles = new LinkedHashSet(roles);
       // add all roles which are generalizations of this one
       CollectionUtils.forAllDo(roles, new Closure()
       {
           @SuppressWarnings("unchecked")
        public void execute(final Object object)
           {
               allRoles.addAll(((Role)object).getAllSpecializations());
           }
       });
       return allRoles;
   }

   /**
    * @return actionRoles
    * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getActionRoles()
    */
   protected String handleGetActionRoles()
   {
       //copied from ThymeleafUseCaseLogicImpl
       final StringBuilder rolesBuffer = new StringBuilder();
       boolean first = true;
       for (final Role role : this.getRoles())
       {
           if (first)
           {
               first = false;
           }
           else
           {
               rolesBuffer.append(',');
           }
           rolesBuffer.append(role.getName());
       }
       return rolesBuffer.toString();
   }

   /**
    * @return needsFileUpload
    * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#isNeedsFileUpload()
    */
   protected boolean handleIsNeedsFileUpload()
   {
       for (final ManageableEntityAttribute attribute : this.getManageableAttributes())
       {
           if(attribute instanceof ThymeleafManageableEntityAttribute)
           {
               final ThymeleafManageableEntityAttribute thymeleafAttribute = (ThymeleafManageableEntityAttribute)attribute;
               if(thymeleafAttribute.isNeedsFileUpload())
               {
                   return true;
               }
           }
       }
       return false;
   }

   /**
    * @return needsUserInterface
    * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#isNeedsUserInterface()
    */
   protected boolean handleIsNeedsUserInterface()
   {
       if(isAbstract())
       {
           return false;
       }
       for (final ManageableEntityAttribute attribute : this.getManageableAttributes())
       {
           if(attribute instanceof ThymeleafManageableEntityAttribute)
           {
               final ThymeleafManageableEntityAttribute thymeleafAttribute = (ThymeleafManageableEntityAttribute)attribute;
               if(!thymeleafAttribute.isHidden())
               {
                   return true;
               }
           }
       }
       for (final ManageableEntityAssociationEnd associationEnd : this.getManageableAssociationEnds())
       {
           if(associationEnd instanceof ThymeleafManageableEntityAssociationEnd)
           {
               final ThymeleafManageableEntityAssociationEnd thymeleafAssociationEnd = (ThymeleafManageableEntityAssociationEnd)associationEnd;
               if(!thymeleafAssociationEnd.isDisplay())
               {
                   return true;
               }
           }
       }
       return false;
   }

   /**
    * @return converterClassName
    * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getConverterClassName
    */
    public String handleGetConverterClassName()
    {
        return StringUtils.replace(
            ObjectUtils.toString(this.getConfiguredProperty(ThymeleafGlobals.CONVERTER_PATTERN)),
            "{0}",
            this.getName());
    }

    /**
     * @return converterType
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getConverterType
     */
     public String handleGetConverterType()
     {
         return this.getManageablePackageName() + this.getNamespaceProperty() + this.getConverterClassName();
     }

    /**
     * @return converterFullPath
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getConverterFullPath
     */
     public String handleGetConverterFullPath()
     {
         return StringUtils.replace(this.getConverterType(), this.getNamespaceProperty(), "/");
     }

    //TODO review
    @Override
    public ManageableEntityAttribute getDisplayAttribute() {
        
        for(final ManageableEntityAttribute attribute: getManageableAttributes())
        {
            if(BooleanUtils.toBoolean(ObjectUtils.toString(attribute.findTaggedValue(ThymeleafProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_DISPLAY))))
            {
                return attribute;
            }
        }
        
        //associations ???
//        for(final ManageableEntityAssociationEnd associationEnd: getManageableAssociationEnds())
//        {
//            if(BooleanUtils.toBoolean(ObjectUtils.toString(associationEnd.findTaggedValue("andromda_manageable_display"))))
//            {
//                return associationEnd;
//            }
//        }
        
        return super.getDisplayAttribute();
    }

    /**
     * @return needsImplementation
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#isNeedsImplementation
     */
    @Override
    protected boolean handleIsNeedsImplementation() {
        final Object generateCrudImpls=this.getConfiguredProperty(ThymeleafGlobals.GENERATE_CRUD_IMPLS);
        if(generateCrudImpls != null && Boolean.parseBoolean(generateCrudImpls.toString()))
        {
            return true;
        }
        else
        {
            final Object taggedValue = this.findTaggedValue(ThymeleafProfile.ANDROMDA_MANAGEABLE_IMPLEMENTATION);
            return (taggedValue == null)
                ? ThymeleafProfile.TAGGEDVALUE_MANAGEABLE_IMPLEMENTATION_DEFAULT_VALUE
                : Boolean.valueOf(String.valueOf(taggedValue)).booleanValue();
        }
    }

    @Override
    protected String handleGetSearchFilterFullPath() {
        return '/' + StringUtils.replace(this.getManageablePackageName(), this.getNamespaceProperty(), "/") + '/' + getSearchFilterName();
    }

    @Override
    protected String handleGetSearchFilterName() {
        return this.getName() + "SearchFilter";
    }

    @Override
    protected String handleGetSearchFilterSerialVersionUID() {
        String serialVersionUID = String.valueOf(0L);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(getSearchFilterFullPath().getBytes());

            long hash = 0;
            for (int ctr = Math.min(
                        hashBytes.length,
                        8) - 1; ctr >= 0; ctr--)
            {
                hash = (hash << 8) | (hashBytes[ctr] & 0xFF);
            }
            serialVersionUID = String.valueOf(hash);
        }
        catch (final NoSuchAlgorithmException exception)
        {
            final String message = "Error performing ManageableEntity.getSearchFilterSerialVersionUID";
            LOGGER.error(
                message,
                exception);
        }
        return serialVersionUID;
    }

    /**
     * @return manageableEditAttributes
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getManageableEditAttributes()
     */
    @Override
    protected Collection<ThymeleafManageableEntityAttribute> handleGetManageableEditAttributes() {
        final Collection<ThymeleafManageableEntityAttribute> editAttributes=new ArrayList<ThymeleafManageableEntityAttribute>();
        
        for(final ManageableEntityAttribute attr: getManageableAttributes())
        {
            final ThymeleafManageableEntityAttribute thymeleafAttr=(ThymeleafManageableEntityAttribute)attr;
            if(thymeleafAttr.isEditable())
            {
                editAttributes.add(thymeleafAttr);
            }
        }
        return editAttributes;
    }
    
    /**
     * @return manageableDetailsAssociations
     * @see org.andromda.cartridges.thymeleaf.metafacades.ThymeleafManageableEntity#getManageableDetailsAssociations()
     */
    @Override
    protected Collection<ManageableEntityAssociationEnd> handleGetManageableDetailsAssociationsEnds()
    {
        final Collection<ManageableEntityAssociationEnd> manageableDetailsAssociationsEnds = new ArrayList<ManageableEntityAssociationEnd>();
        
        for(ManageableEntityAssociationEnd associationEnd: (Collection<ManageableEntityAssociationEnd>)this.getManageableAssociationEnds())
        {
            if(associationEnd.isMany() && associationEnd.getType().hasStereotype(UMLProfile.STEREOTYPE_MANAGEABLE) && 
               associationEnd.getOtherEnd().isNavigable() && associationEnd.getOtherEnd().isComposition())
            {
                manageableDetailsAssociationsEnds.add(associationEnd);
            }
        }

        return manageableDetailsAssociationsEnds;
    }
}
