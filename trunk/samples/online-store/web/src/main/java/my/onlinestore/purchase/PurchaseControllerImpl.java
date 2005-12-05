// license-header java merge-point
package my.onlinestore.purchase;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see my.onlinestore.purchase.PurchaseController
 */
public class PurchaseControllerImpl extends PurchaseController
{
    /**
     * @see my.onlinestore.purchase.PurchaseController#addItemsToBasket(org.apache.struts.action.ActionMapping, my.onlinestore.purchase.AddItemsToBasketForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void addItemsToBasket(ActionMapping mapping, my.onlinestore.purchase.AddItemsToBasketForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // nothing to be done for this operation, there are no properties that can be set
    }

    /**
     * @see my.onlinestore.purchase.PurchaseController#closeUserSession(org.apache.struts.action.ActionMapping, my.onlinestore.purchase.CloseUserSessionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void closeUserSession(ActionMapping mapping, my.onlinestore.purchase.CloseUserSessionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // nothing to be done for this operation, there are no properties that can be set
    }

    /**
     * @see my.onlinestore.purchase.PurchaseController#loadItems(org.apache.struts.action.ActionMapping, my.onlinestore.purchase.LoadItemsForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void loadItems(ActionMapping mapping, my.onlinestore.purchase.LoadItemsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setAvailableItems(java.util.Arrays.asList(new Object[] {"availableItems-1", "availableItems-2", "availableItems-3", "availableItems-4", "availableItems-5"}));
        // this property receives a default value, just to have the application running on dummy data
        form.setAvailable("available-test");
        // populating the table with a dummy list
        form.setItemList(itemListDummyList);
        // this property receives a default value, just to have the application running on dummy data
        form.setSelectedItems(java.util.Arrays.asList(new Object[] {"selectedItems-1", "selectedItems-2", "selectedItems-3", "selectedItems-4", "selectedItems-5"}));
        form.setSelectedItemsValueList(new Object[] {"selectedItems-1", "selectedItems-2", "selectedItems-3", "selectedItems-4", "selectedItems-5"});
        form.setSelectedItemsLabelList(form.getSelectedItemsValueList());
    }

    /**
     * @see my.onlinestore.purchase.PurchaseController#openUserSession(org.apache.struts.action.ActionMapping, my.onlinestore.purchase.OpenUserSessionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void openUserSession(ActionMapping mapping, my.onlinestore.purchase.OpenUserSessionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // nothing to be done for this operation, there are no properties that can be set
    }

    /**
     * @see my.onlinestore.purchase.PurchaseController#prepareForShipping(org.apache.struts.action.ActionMapping, my.onlinestore.purchase.PrepareForShippingForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void prepareForShipping(ActionMapping mapping, my.onlinestore.purchase.PrepareForShippingForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // nothing to be done for this operation, there are no properties that can be set
    }

    /**
     * @see my.onlinestore.purchase.PurchaseController#loadAvailableItems(org.apache.struts.action.ActionMapping, my.onlinestore.purchase.LoadAvailableItemsForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void loadAvailableItems(ActionMapping mapping, my.onlinestore.purchase.LoadAvailableItemsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setAvailable("available-test");
    }

    /**
     * @see my.onlinestore.purchase.PurchaseController#rememberSelection(org.apache.struts.action.ActionMapping, my.onlinestore.purchase.RememberSelectionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void rememberSelection(ActionMapping mapping, my.onlinestore.purchase.RememberSelectionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // we don't set row selection parameters such as idRowSelection
    }

    /**
     * @see my.onlinestore.purchase.PurchaseController#loadLanguages(org.apache.struts.action.ActionMapping, my.onlinestore.purchase.LoadLanguagesForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final void loadLanguages(ActionMapping mapping, my.onlinestore.purchase.LoadLanguagesForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // this property receives a default value, just to have the application running on dummy data
        form.setLanguage("language-test");
        form.setLanguageValueList(new Object[] {"language-1", "language-2", "language-3", "language-4", "language-5"});
        form.setLanguageLabelList(form.getLanguageValueList());
    }

    /**
     * This dummy variable is used to populate the "itemList" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final my.onlinestore.purchase.StoreItem[] itemListDummyList =
        new my.onlinestore.purchase.StoreItem[]
        {
            new my.onlinestore.purchase.StoreItem("title-1", "publisher-1", false, "id-1", "hiddenColumn-1", new java.util.Date()),
            new my.onlinestore.purchase.StoreItem("title-2", "publisher-2", false, "id-2", "hiddenColumn-2", new java.util.Date()),
            new my.onlinestore.purchase.StoreItem("title-3", "publisher-3", false, "id-3", "hiddenColumn-3", new java.util.Date()),
            new my.onlinestore.purchase.StoreItem("title-4", "publisher-4", false, "id-4", "hiddenColumn-4", new java.util.Date()),
            new my.onlinestore.purchase.StoreItem("title-5", "publisher-5", false, "id-5", "hiddenColumn-5", new java.util.Date())
        };
}