package my.onlinestore.purchase;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class PurchaseController extends PurchaseControllerInterface
{
    /**
     * 
     */
    public final void addItemsToBasket(ActionMapping mapping, PurchaseControllerAddItemsToBasketInterface form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // nothing to be done for this operation, there are not properties that can be set
    }

    /**
     * 
     */
    public final void closeUserSession(ActionMapping mapping, PurchaseControllerCloseUserSessionInterface form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setPurchaseEmail("purchaseEmail-test");
        form.setPurchaseAge((int) 633229374);
        form.setPurchaseCreditCard("purchaseCreditCard-test");
    }

    /**
     * 
     */
    public final void loadItems(ActionMapping mapping, PurchaseControllerLoadItemsInterface form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setProceedName("proceedName-test");
        form.setBuyThisItemId("buyThisItemId-test");
        form.setProceedPassword("proceedPassword-test");
        form.setProceedSelectedItems(java.util.Arrays.asList(new Object[]{"proceedSelectedItems-1", "proceedSelectedItems-2", "proceedSelectedItems-3", "proceedSelectedItems-4", "proceedSelectedItems-5"}));
        form.setProceedConfirmedLicence(false);
        form.setItemList(itemListDummyList);
    }

    /**
     * 
     */
    public final void openUserSession(ActionMapping mapping, PurchaseControllerOpenUserSessionInterface form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setProceedName("proceedName-test");
        form.setBuyThisItemId("buyThisItemId-test");
        form.setProceedPassword("proceedPassword-test");
        form.setProceedSelectedItems(java.util.Arrays.asList(new Object[]{"proceedSelectedItems-1", "proceedSelectedItems-2", "proceedSelectedItems-3", "proceedSelectedItems-4", "proceedSelectedItems-5"}));
        form.setProceedConfirmedLicence(false);
        form.setItemList(itemListDummyList);
    }

    /**
     * 
     */
    public final void prepareForShipping(ActionMapping mapping, PurchaseControllerPrepareForShippingInterface form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // all properties receive a default value, just to have the application running properly
        form.setPurchaseEmail("purchaseEmail-test");
        form.setPurchaseAge((int) 633229374);
        form.setPurchaseCreditCard("purchaseCreditCard-test");
    }


    private final java.util.Collection itemListDummyList =
            java.util.Arrays.asList(new Object[]{
                new ItemListDummyItem("id-1", "name-1", "address-1"),
                new ItemListDummyItem("id-2", "name-2", "address-2"),
                new ItemListDummyItem("id-3", "name-3", "address-3"),
                new ItemListDummyItem("id-4", "name-4", "address-4"),
                new ItemListDummyItem("id-5", "name-5", "address-5")
            });

    public final class ItemListDummyItem implements java.io.Serializable
    {
        private String id = null;
        private String name = null;
        private String address = null;

        public ItemListDummyItem(String id, String name, String address)
        {
            this.id = id;
            this.name = name;
            this.address = address;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getId()
        {
            return this.id;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }

        public void setAddress(String address)
        {
            this.address = address;
        }

        public String getAddress()
        {
            return this.address;
        }
    }


}
