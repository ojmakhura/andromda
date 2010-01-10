package org.andromda.cartridges.jsf.metafacades;

import org.andromda.cartridges.jsf.JSFGlobals;
import org.andromda.cartridges.jsf.JSFUtils;
import org.andromda.cartridges.jsf.JSFProfile;
import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.Role;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.AttributeFacade;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.apache.commons.lang.ObjectUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.jsf.metafacades.JSFManageableEntity.
 *
 * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity
 */
public class JSFManageableEntityLogicImpl
    extends JSFManageableEntityLogic
{

    public JSFManageableEntityLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getViewName()
     */
    protected java.lang.String handleGetViewName()
       {
           return this.getName().toLowerCase() + "-crud";
       }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getViewTitleKey()
     */
    protected java.lang.String handleGetViewTitleKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".view.title";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getViewTitleValue()
     */
    protected java.lang.String handleGetViewTitleValue()
    {
        return StringUtilsHelper.toPhrase(getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getListName()
     */
    protected java.lang.String handleGetListName()
    {
        return "manageableList";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getFormBeanType()
     */
    protected java.lang.String handleGetFormBeanType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getFormBeanClassName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getFormBeanName()
     */
    protected java.lang.String handleGetFormBeanName()
    {
        return "manage" + this.getName() + JSFGlobals.FORM_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getExceptionKey()
     */
    protected java.lang.String handleGetExceptionKey()
    {
    	return StringUtilsHelper.toResourceMessageKey(this.getName()) + ".exception";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getActionType()
     */
    protected java.lang.String handleGetActionType()
    {
        return this.getManageablePackageName() + this.getNamespaceProperty() + this.getActionClassName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getActionFullPath()
     */
    protected java.lang.String handleGetActionFullPath()
    {
        return '/' + StringUtils.replace(this.getActionType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getActionPath()
     */
    protected java.lang.String handleGetActionPath()
    {
        return '/' + this.getName() + "/Manage";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getActionClassName()
     */
    protected java.lang.String handleGetActionClassName()
    {
        return "Manage" + getName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getExceptionPath()
     */
    protected java.lang.String handleGetExceptionPath()
    {
        return this.getViewFullPath();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#isPreload()
     */
    protected boolean handleIsPreload()
    {
    	return false; //TODO think about...
//        return this.isCreate() || this.isRead() || this.isUpdate() || this.isDelete();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getFormBeanClassName()
     */
    protected java.lang.String handleGetFormBeanClassName()
    {
        return this.getName() + JSFGlobals.FORM_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getFormBeanFullPath()
     */
    protected java.lang.String handleGetFormBeanFullPath()
    {
        return StringUtils.replace(this.getFormBeanType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getListGetterName()
     */
    protected java.lang.String handleGetListGetterName()
    {
        return "getManageableList";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getListSetterName()
     */
    protected java.lang.String handleGetListSetterName()
    {
        return "setManageableList";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getMessageKey()
     */
    protected java.lang.String handleGetMessageKey()
    {
        return StringUtilsHelper.toResourceMessageKey(this.getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getMessageValue()
     */
    protected java.lang.String handleGetMessageValue()
    {
        return StringUtilsHelper.toPhrase(this.getName());
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getOnlineHelpKey()
     */
    protected java.lang.String handleGetOnlineHelpKey()
    {
        return this.getMessageKey() + ".online.help";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getOnlineHelpValue()
     */
    protected java.lang.String handleGetOnlineHelpValue()
    {
        final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
        return (value == null) ? "No entity documentation has been specified" : value;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getOnlineHelpActionPath()
     */
    protected java.lang.String handleGetOnlineHelpActionPath()
    {
        return this.getActionPath() + "Help";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getOnlineHelpPagePath()
     */
    protected java.lang.String handleGetOnlineHelpPagePath()
    {
        return '/' + this.getManageablePackagePath() + '/' + this.getName().toLowerCase() + "_help";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#isTableExportable()
     */
    protected boolean handleIsTableExportable()
    {
        return this.getTableExportTypes().indexOf("none") == -1;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getTableExportTypes()
     */
    protected java.lang.String handleGetTableExportTypes()
    {
    	return null;
    	//TODO a resolver
//        return JSFUtils.getDisplayTagExportTypes(
//            this.findTaggedValues(JSFProfile.TAGGEDVALUE_TABLE_EXPORT),
//            (String)getConfiguredProperty(JSFGlobals.PROPERTY_DEFAULT_TABLE_EXPORT_TYPES) );
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getTableMaxRows()
     */
    protected int handleGetTableMaxRows()
    {
        final Object taggedValue = this.findTaggedValue(JSFProfile.TAGGEDVALUE_TABLE_MAXROWS);
        int pageSize;

        try
        {
            pageSize = Integer.parseInt(String.valueOf(taggedValue));
        }
        catch (Exception e)
        {
            pageSize = JSFProfile.TAGGEDVALUE_TABLE_MAXROWS_DEFAULT_COUNT;
        }

        return pageSize;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#isTableSortable()
     */
    protected boolean handleIsTableSortable()
    {
        final Object taggedValue = this.findTaggedValue(JSFProfile.TAGGEDVALUE_TABLE_SORTABLE);
        return (taggedValue == null)
            ? JSFProfile.TAGGEDVALUE_TABLE_SORTABLE_DEFAULT_VALUE
            : Boolean.valueOf(String.valueOf(taggedValue)).booleanValue();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getControllerType()
     */
     protected java.lang.String handleGetControllerType(){
         return this.getManageablePackageName() + this.getNamespaceProperty() + this.getControllerName();
     }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getControllerBeanName()
     */
    protected java.lang.String handleGetControllerBeanName()
    {
    	  return StringUtils.uncapitalize(this.getName()) + "Controller";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getControllerFullPath()
     */
    protected java.lang.String handleGetControllerFullPath()
    {
           return "/" + StringUtils.replace(this.getControllerType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getControllerName()
     */
    protected java.lang.String handleGetControllerName()
    {
           return this.getName() + "Controller";
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getValueObjectClassName()
     */
    protected java.lang.String handleGetValueObjectClassName()
    {
    	   return getName() + this.getConfiguredProperty(JSFGlobals.CRUD_VALUE_OBJECT_SUFFIX);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getFormSerialVersionUID()
     */
    protected java.lang.String handleGetFormSerialVersionUID()
    {
           final StringBuilder buffer = new StringBuilder();

           buffer.append(this.getFormBeanType());

           addSerialUIDData(buffer);
           
           return JSFUtils.calcSerialVersionUID(buffer);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getActionSerialVersionUID()
     */
    protected java.lang.String handleGetActionSerialVersionUID()
    {
           final StringBuilder buffer = new StringBuilder();

           buffer.append(this.getActionFullPath());

           addSerialUIDData(buffer);
           
           return JSFUtils.calcSerialVersionUID(buffer);
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getPopulatorName()
     */
    protected java.lang.String handleGetPopulatorName()
    {
           return ObjectUtils.toString(this.getConfiguredProperty(JSFGlobals.VIEW_POPULATOR_PATTERN)).replaceAll(
               "\\{0\\}",
               StringUtilsHelper.upperCamelCaseName(this.getFormBeanClassName()));
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getPopulatorFullPath()
     */
    protected java.lang.String handleGetPopulatorFullPath()
    {
           return "/" + StringUtils.replace(this.getPopulatorType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getPopulatorType()
     */
    protected java.lang.String handleGetPopulatorType()
    {
           return this.getManageablePackageName() + this.getNamespaceProperty() + this.getPopulatorName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getViewFullPath()
     */
    protected java.lang.String handleGetViewFullPath()
    {
           return '/' + this.getManageablePackagePath() + '/' + this.getViewName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#isValidationRequired()
     */
    protected boolean handleIsValidationRequired()
    {
           for (final Iterator iterator = this.getManageableAttributes().iterator(); iterator.hasNext();)
           {
               final JSFManageableEntityAttribute attribute = (JSFManageableEntityAttribute)iterator.next();
               if (attribute.isValidationRequired())
               {
                   return true;
               }
           }
           return false;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getSearchFormBeanName()
     */
    protected java.lang.String handleGetSearchFormBeanName()
    {
           return "manage" + this.getName() + "Search" + JSFGlobals.FORM_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getSearchFormBeanType()
     */
    protected java.lang.String handleGetSearchFormBeanType()
    {
           return this.getManageablePackageName() + this.getNamespaceProperty() + this.getSearchFormBeanClassName();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getSearchFormBeanFullPath()
     */
    protected java.lang.String handleGetSearchFormBeanFullPath()
    {
           return StringUtils.replace(this.getSearchFormBeanType(), this.getNamespaceProperty(), "/");
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getSearchFormBeanClassName()
     */
    protected java.lang.String handleGetSearchFormBeanClassName()
    {
           return this.getName() + "Search" + JSFGlobals.FORM_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getManageableSearchAttributes()
     */
    protected java.util.Collection handleGetManageableSearchAttributes()
    {
    	   final Collection coll=new java.util.ArrayList();
    	   for(final java.util.Iterator it=getManageableAttributes().iterator(); it.hasNext(); ){
               Object next = it.next();
               if(next instanceof JSFManageableEntityAttribute){
                   final JSFManageableEntityAttribute attr=(JSFManageableEntityAttribute)next;
                   if(!attr.isHidden())
                       coll.add(attr);
               }
    	   }
    	   return coll;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getManageableSearchAssociationEnds()
     */
    protected java.util.Collection handleGetManageableSearchAssociationEnds()
    {
    	   return getManageableAssociationEnds();
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#isSearchable(java.lang.Object)
     */
    protected boolean handleIsSearchable(java.lang.Object element)
    {
//    	   if(element instanceof JSFManageableEntityAttribute)
//    		   return getManageableSearchAttributes().contains(element);
//    	   else
//    		   return getManageableSearchAssociationEnds().contains(element);
    	   
    	   //TODO corrigir
    	   
    	   if(element instanceof JSFManageableEntityAttribute)
    		   return !((JSFManageableEntityAttribute)element).isHidden();
    	   else
    		   return true;
    }

    /**
     * @return the configured property denoting the character sequence to use for the separation of namespaces
     */
    private String getNamespaceProperty()
    {
        return (String)this.getConfiguredProperty(UMLMetafacadeProperties.NAMESPACE_SEPARATOR);
    }

   private void addSerialUIDData(StringBuilder buffer){
       for (final Iterator iterator = this.getManageableAttributes().iterator(); iterator.hasNext();)
       {
           final ModelElementFacade parameter = (ModelElementFacade)iterator.next();
           buffer.append(parameter.getName());
       }
       for (final Iterator iterator = this.getManageableAssociationEnds().iterator(); iterator.hasNext();)
       {
           final ModelElementFacade parameter = (ModelElementFacade)iterator.next();
           buffer.append(parameter.getName());
       }
       
   }
          
   /**
    * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getRoles()
    */
   protected Collection handleGetRoles()
   {
	   //copied form the Service <<Metafacade>>
       final Collection roles = new ArrayList(this.getTargetDependencies());
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
       final Collection allRoles = new LinkedHashSet(roles);
       // add all roles which are generalizations of this one
       CollectionUtils.forAllDo(roles, new Closure()
       {
           public void execute(final Object object)
           {
               allRoles.addAll(((Role)object).getAllSpecializations());
           }
       });
       return allRoles;
   }

   /**
    * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#getActionRoles()
    */
   protected String handleGetActionRoles()
   {
	   //copied from JSFUseCaseLogicImpl
       final Collection users = this.getRoles();
       final StringBuilder rolesBuffer = new StringBuilder();
       boolean first = true;
       for (final Iterator userIterator = users.iterator(); userIterator.hasNext();)
       {
           if (first)
           {
               first = false;
           }
           else
           {
               rolesBuffer.append(',');
           }
           final Role role = (Role)userIterator.next();
           rolesBuffer.append(role.getName());
       }
       return rolesBuffer.toString();
   }

   /**
    * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#isNeedsFileUpload()
    */
   protected boolean handleIsNeedsFileUpload()
   {
       for (final Iterator iterator = this.getManageableAttributes().iterator(); iterator.hasNext();)
       {
           final JSFManageableEntityAttribute attribute = (JSFManageableEntityAttribute)iterator.next();
           if(attribute.isNeedsFileUpload())
        	   return true;
       }
       return false;
   }
   
   /**
    * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#converterClassName
    */
    public java.lang.String handleGetConverterClassName(){
        return this.getName() + JSFGlobals.CONVERTER_SUFFIX;
    }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#converterType
     */
     public java.lang.String handleGetConverterType(){
         return this.getManageablePackageName() + this.getNamespaceProperty() + this.getConverterClassName();
     }

    /**
     * @see org.andromda.cartridges.jsf.metafacades.JSFManageableEntity#converterFullPath
     */
     public java.lang.String handleGetConverterFullPath(){
         return StringUtils.replace(this.getConverterType(), this.getNamespaceProperty(), "/");
     }

}