// license-header java merge-point
//
// Generated by: MetafacadeLogicImpl.vsl in andromda-meta-cartridge.
package org.andromda.cartridges.angular.metafacades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;

import org.andromda.cartridges.angular.AngularGlobals;
import org.andromda.cartridges.angular.AngularProfile;
import org.andromda.cartridges.angular.AngularUtils;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ManageableEntityAssociationEnd;
import org.andromda.metafacades.uml.ManageableEntityAttribute;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * TODO: Model Documentation for org.andromda.cartridges.angular.metafacades.AngularManageableEntity
 * MetafacadeLogic implementation for org.andromda.cartridges.angular.metafacades.AngularManageableEntity.
 *
 * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity
 */
public class AngularManageableEntityLogicImpl
    extends AngularManageableEntityLogic
{
    private static final long serialVersionUID = 34L;
    
    /**
     * The logger instance.
     */
    private static final Logger LOGGER = Logger.getLogger(AngularManageableEntityLogicImpl.class);

    /**
     * Public constructor for AngularManageableEntityLogicImpl
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity
     */
    public AngularManageableEntityLogicImpl (Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getName().toLowerCase() + "-crud"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getViewName()
     */
    protected String handleGetViewName()
    {
        return this.getName().toLowerCase() + "-crud";
    }

    /**
     * @return toResourceMessageKey(this.getName()) + ".view.title"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getViewTitleKey()
     */
    protected String handleGetViewTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".view.title";
    }

    /**
     * @return StringUtilsHelper.toPhrase(getName())
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getViewTitleValue()
     */
    protected String handleGetViewTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @return "manageableList"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getListName()
     */
    protected String handleGetListName()
    {
        return "manageableList";
    }

    /**
     * @return getManageablePackageName() + getNamespaceProperty() + this.getFormBeanClassName()
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getFormBeanType()
     */
    protected String handleGetFormBeanType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getFormBeanClassName();
    }

    /**
     * @return formBeanName
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getFormBeanName()
     */
    protected String handleGetFormBeanName()
    {
        final String pattern = Objects.toString(this.getConfiguredProperty(AngularGlobals.FORM_BEAN_PATTERN), "");
        final String formBeanName = pattern.replaceFirst("\\{0\\}", "manage");
        return formBeanName.replaceFirst("\\{1\\}", this.getName());
    }

    /**
     * @return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".exception"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getExceptionKey()
     */
    protected String handleGetExceptionKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".exception";
    }

    /**
     * @return getManageablePackageName() + getNamespaceProperty() + getActionClassName()
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getActionType()
     */
    protected String handleGetActionType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getActionClassName();
    }

    /**
     * @return '/' + StringUtils.replace(this.getActionType(), getNamespaceProperty(), "/")
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getActionFullPath()
     */
    protected String handleGetActionFullPath()
    {
        return '/' + StringUtils.replace(this.getActionType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return '/' + getName() + "/Manage"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getActionPath()
     */
    protected String handleGetActionPath()
    {
        return super.getActionFullPath();
    }

    /**
     * @return "Manage" + getName()
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getActionClassName()
     */
    protected String handleGetActionClassName()
    {
        return getName();
    }

    /**
     * @return getViewFullPath()
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getExceptionPath()
     */
    protected String handleGetExceptionPath()
    {
        return this.getViewFullPath();
    }

    /**
     * @return false
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#isPreload()
     */
    protected boolean handleIsPreload()
    {
        return false; //TODO think about...
//        return this.isCreate() || this.isRead() || this.isUpdate() || this.isDelete();
    }

    /**
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntityLogic#getManageableIdentifier()
     */
    @Override
    public org.andromda.metafacades.uml.ManageableEntityAttribute getManageableIdentifier()
    {
        return super.getManageableIdentifier();
    }

    /**
     * @return StringUtils.capitalize(this.getFormBeanName())
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getFormBeanClassName()
     */
    protected String handleGetFormBeanClassName()
    {
        return StringUtils.capitalize(this.getFormBeanName());
    }

    /**
     * @return StringUtils.replace(getFormBeanType(), getNamespaceProperty(), "/")
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getFormBeanFullPath()
     */
    protected String handleGetFormBeanFullPath()
    {
        return StringUtils.replace(this.getFormBeanType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return "getManageableList"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getListGetterName()
     */
    protected String handleGetListGetterName()
    {
        return "getManageableList";
    }

    /**
     * @return "setManageableList"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getListSetterName()
     */
    protected String handleGetListSetterName()
    {
        return "setManageableList";
    }

    /**
     * @return StringUtilsHelper.toResourceMessageKey(this.getName())
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getMessageKey()
     */
    protected String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName());
    }

    /**
     * @return StringUtilsHelper.toPhrase(this.getName())
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getMessageValue()
     */
    protected String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    /**
     * @return getMessageKey() + ".online.help"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getOnlineHelpKey()
     */
    protected String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    /**
     * @return onlineHelpValue
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getOnlineHelpValue()
     */
    protected String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No entity documentation has been specified" : value;
    }

    /**
     * @return getActionPath() + "Help"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getOnlineHelpActionPath()
     */
    protected String handleGetOnlineHelpActionPath()
    {
        return this.getActionPath() + "Help";
    }

    /**
     * @return '/' + getManageablePackagePath() + '/' + getName().toLowerCase() + "_help"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getOnlineHelpPagePath()
     */
    protected String handleGetOnlineHelpPagePath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getName().toLowerCase() + "_help";
    }

    /**
     * @return getTableExportTypes().indexOf("none") == -1
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#isTableExportable()
     */
    protected boolean handleIsTableExportable()
    {
        return this.getTableExportTypes().indexOf("none") == -1;
    }

    /**
     * @return null
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getTableExportTypes()
     */
    protected String handleGetTableExportTypes()
    {
        return null;
        //TODO a resolver
//        return AngularUtils.getDisplayTagExportTypes(
//            this.findTaggedValues(AngularProfile.TAGGEDVALUE_TABLE_EXPORT),
//            (String)getConfiguredProperty(AngularGlobals.PROPERTY_DEFAULT_TABLE_EXPORT_TYPES) );
    }

    /**
     * @return tableMaxRows
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getTableMaxRows()
     */
    protected int handleGetTableMaxRows()
    {
        final Object taggedValue = this.findTaggedValue(AngularProfile.TAGGEDVALUE_TABLE_MAXROWS);
        int pageSize;

        try
        {
            pageSize = Integer.parseInt(String.valueOf(taggedValue));
        }
        catch (Exception e)
        {
            pageSize = AngularProfile.TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT;
        }

        return pageSize;
    }

    /**
     * @return tableSortable
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#isTableSortable()
     */
    protected boolean handleIsTableSortable()
    {
        final Object taggedValue = this.findTaggedValue(AngularProfile.TAGGEDVALUE_TABLE_SORTABLE);
        return (taggedValue == null)
            ? AngularProfile.TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE
            : Boolean.valueOf(String.valueOf(taggedValue)).booleanValue();
    }

    /**
     * @return controllerType
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getControllerType()
     */
     protected String handleGetControllerType()
     {
         return this.getManageablePackageName() + this.getNamespaceProperty() + this.getControllerName();
     }

    /**
     * @return StringUtils.uncapitalize(this.getName()) + "Controller"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getControllerBeanName()
     */
    protected String handleGetControllerBeanName()
    {
        return StringUtils.uncapitalize(this.getName()) + "Controller";
    }

    /**
     * @return '/' + StringUtils.replace(getControllerType(), getNamespaceProperty(), "/")
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getControllerFullPath()
     */
    protected String handleGetControllerFullPath()
    {
        return '/' + StringUtils.replace(this.getControllerType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return getName() + "Controller"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getControllerName()
     */
    protected String handleGetControllerName()
    {
        return this.getName() + "Controller";
    }

    /**
     * @return getName() + this.getConfiguredProperty(AngularGlobals.CRUD_VALUE_OBJECT_SUFFIX)
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getValueObjectClassName()
     */
    protected String handleGetValueObjectClassName()
    {
        return getName() + this.getConfiguredProperty(AngularGlobals.CRUD_VALUE_OBJECT_SUFFIX);
    }

    /**
     * @return formSerialVersionUID
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getFormSerialVersionUID()
     */
    protected String handleGetFormSerialVersionUID()
    {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.getFormBeanType());
        addSerialUIDData(buffer);
        return AngularUtils.calcSerialVersionUID(buffer);
    }

    /**
     * @return actionSerialVersionUID
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getActionSerialVersionUID()
     */
    protected String handleGetActionSerialVersionUID()
    {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(this.getActionFullPath());
        addSerialUIDData(buffer);
        return AngularUtils.calcSerialVersionUID(buffer);
    }

    /**
     * @return populatorName
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getPopulatorName()
     */
    protected String handleGetPopulatorName()
    {
        return Objects.toString(this.getConfiguredProperty(AngularGlobals.VIEW_POPULATOR_PATTERN), "").replaceAll(
            "\\{0\\}",
            StringUtilsHelper.upperCamelCaseName(this.getFormBeanClassName()));
    }

    /**
     * @return '/' + StringUtils.replace(getPopulatorType(), getNamespaceProperty(), "/")
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getPopulatorFullPath()
     */
    protected String handleGetPopulatorFullPath()
    {
        return '/' + StringUtils.replace(this.getPopulatorType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return getManageablePackageName() + getNamespaceProperty() + getPopulatorName()
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getPopulatorType()
     */
    protected String handleGetPopulatorType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getPopulatorName();
    }

    /**
     * @return '/' + getManageablePackagePath() + '/' + getViewName()
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getViewFullPath()
     */
    protected String handleGetViewFullPath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getViewName();
    }

    /**
     * @return '/' + getManageablePackagePath() + '/' + getName().toLowerCase() + "-ods-export"
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getViewFullPath()
     */
    protected String handleGetOdsExportFullPath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getName().toLowerCase()+".ods-export";
    }

    /**
     * @return isValidationRequired
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
        for (final ManageableEntityAttribute attribute : this.getManageableAttributes())
        {
            if(attribute instanceof AngularManageableEntityAttribute)
            {
                final AngularManageableEntityAttribute AngularAttribute = (AngularManageableEntityAttribute)attribute;
                if (AngularAttribute.isValidationRequired())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return searchFormBeanName
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getSearchFormBeanName()
     */
    protected String handleGetSearchFormBeanName()
    {
        final String pattern = Objects.toString(this.getConfiguredProperty(AngularGlobals.FORM_BEAN_PATTERN), "");
        final String formBeanName = pattern.replaceFirst("\\{0\\}", "manage");
        return formBeanName.replaceFirst("\\{1\\}",this.getName() + "Search");
    }

    /**
     * @return searchFormBeanType
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getSearchFormBeanType()
     */
    protected String handleGetSearchFormBeanType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getSearchFormBeanClassName();
    }

    /**
     * @return searchFormBeanFullPath
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getSearchFormBeanFullPath()
     */
    protected String handleGetSearchFormBeanFullPath()
    {
        return StringUtils.replace(this.getSearchFormBeanType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @return StringUtils.capitalize(this.getSearchFormBeanName())
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getSearchFormBeanClassName()
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
                if(BooleanUtils.toBoolean(ObjectUtils.toString(attr.findTaggedValue(AngularProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE))))
                {
                    usingManageableSearchable=true;
                    break;
                }
            }
            if(usingManageableSearchable == null)
            {
                for(final ManageableEntityAssociationEnd end: getManageableAssociationEnds())
                {
                    //TODO constant should go to AngularProfile of UMLPROFILE
                    if(BooleanUtils.toBoolean(ObjectUtils.toString(end.findTaggedValue(AngularProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE))))
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
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getManageableSearchAttributes()
     */
    protected Collection<AngularManageableEntityAttribute> handleGetManageableSearchAttributes()
    {
        final Collection<AngularManageableEntityAttribute> searchAttributes=new ArrayList<AngularManageableEntityAttribute>();
        
        if(isUsingManageableSearchable())
        {
            for(final ManageableEntityAttribute attr: getManageableAttributes())
            {
                if(BooleanUtils.toBoolean(ObjectUtils.toString(attr.findTaggedValue(AngularProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE))))
                {
                    searchAttributes.add((AngularManageableEntityAttribute)attr);
                }
            }
        }
        else
        {
            for(ManageableEntityAttribute attr: getManageableAttributes())
            {
                if(attr.isDisplay() && !attr.getType().isBlobType() && !attr.getType().isClobType())
                {
                    searchAttributes.add((AngularManageableEntityAttribute)attr);
                }
            }
        }

        return searchAttributes;
    }

    /**
     * @return getManageableAssociationEnds()
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getManageableSearchAssociationEnds()
     */
    protected Collection<ManageableEntityAssociationEnd> handleGetManageableSearchAssociationEnds()
    {
        final Collection<ManageableEntityAssociationEnd> searchAssociationEnds=new ArrayList<ManageableEntityAssociationEnd>();
        
        if(isUsingManageableSearchable())
        {
            for(final ManageableEntityAssociationEnd end: getManageableAssociationEnds())
            {
                if(BooleanUtils.toBoolean(ObjectUtils.toString(end.findTaggedValue(AngularProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_SEARCHABLE)))
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
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#isSearchable(Object)
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
    * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getRoles()
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
    * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getActionRoles()
    */
   protected String handleGetActionRoles()
   {
       //copied from AngularUseCaseLogicImpl
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
    * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#isNeedsFileUpload()
    */
   protected boolean handleIsNeedsFileUpload()
   {
       for (final ManageableEntityAttribute attribute : this.getManageableAttributes())
       {
           if(attribute instanceof AngularManageableEntityAttribute)
           {
               final AngularManageableEntityAttribute AngularAttribute = (AngularManageableEntityAttribute)attribute;
               if(AngularAttribute.isNeedsFileUpload())
               {
                   return true;
               }
           }
       }
       return false;
   }

   /**
    * @return needsUserInterface
    * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#isNeedsUserInterface()
    */
   protected boolean handleIsNeedsUserInterface()
   {
       if(isAbstract())
       {
           return false;
       }
       for (final ManageableEntityAttribute attribute : this.getManageableAttributes())
       {
           if(attribute instanceof AngularManageableEntityAttribute)
           {
               final AngularManageableEntityAttribute AngularAttribute = (AngularManageableEntityAttribute)attribute;
               if(!AngularAttribute.isHidden())
               {
                   return true;
               }
           }
       }
       for (final ManageableEntityAssociationEnd associationEnd : this.getManageableAssociationEnds())
       {
           if(associationEnd instanceof AngularManageableEntityAssociationEnd)
           {
               final AngularManageableEntityAssociationEnd AngularAssociationEnd = (AngularManageableEntityAssociationEnd)associationEnd;
               if(!AngularAssociationEnd.isDisplay())
               {
                   return true;
               }
           }
       }
       return false;
   }

   /**
    * @return converterClassName
    * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getConverterClassName
    */
    public String handleGetConverterClassName()
    {
        return StringUtils.replace(
            ObjectUtils.toString(this.getConfiguredProperty(AngularGlobals.CONVERTER_PATTERN)),
            "{0}",
            this.getName());
    }

    /**
     * @return converterType
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getConverterType
     */
     public String handleGetConverterType()
     {
         return this.getManageablePackageName() + this.getNamespaceProperty() + this.getConverterClassName();
     }

    /**
     * @return converterFullPath
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getConverterFullPath
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
            if(BooleanUtils.toBoolean(ObjectUtils.toString(attribute.findTaggedValue(AngularProfile.ANDROMDA_MANAGEABLE_ATTRIBUTE_DISPLAY))))
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
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#isNeedsImplementation
     */
    @Override
    protected boolean handleIsNeedsImplementation() {
        final Object generateCrudImpls=this.getConfiguredProperty(AngularGlobals.GENERATE_CRUD_IMPLS);
        if(generateCrudImpls != null && Boolean.parseBoolean(generateCrudImpls.toString()))
        {
            return true;
        }
        else
        {
            final Object taggedValue = this.findTaggedValue(AngularProfile.ANDROMDA_MANAGEABLE_IMPLEMENTATION);
            return (taggedValue == null)
                ? AngularProfile.TAGGEDVALUE_MANAGEABLE_IMPLEMENTATION_DEFAULT_VALUE
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
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getManageableEditAttributes()
     */
    @Override
    protected Collection<AngularManageableEntityAttribute> handleGetManageableEditAttributes() {
        final Collection<AngularManageableEntityAttribute> editAttributes=new ArrayList<AngularManageableEntityAttribute>();
        
        for(final ManageableEntityAttribute attr: getManageableAttributes())
        {
            final AngularManageableEntityAttribute AngularAttr=(AngularManageableEntityAttribute)attr;
            if(AngularAttr.isEditable())
            {
                editAttributes.add(AngularAttr);
            }
        }
        return editAttributes;
    }
    
    /**
     * @return manageableDetailsAssociations
     * @see org.andromda.cartridges.angular.metafacades.AngularManageableEntity#getManageableDetailsAssociations()
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