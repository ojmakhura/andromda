package my.onlinestore.purchase;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class PurchaseController extends PurchaseControllerInterface
{

    /**
     * 
     */
    public final void addItemsToBasket(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        /*
         * By default this method populates the complete form, it is up to you to replace this
         * by those fields that are required (this cannot be determined here because it might be
         * the case that many action call this controller method, each with their own set of
         * parameters)
         */
        populateForm(form);
    }

    /**
     * 
     */
    public final void closeUserSession(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        /*
         * By default this method populates the complete form, it is up to you to replace this
         * by those fields that are required (this cannot be determined here because it might be
         * the case that many action call this controller method, each with their own set of
         * parameters)
         */
        populateForm(form);
    }

    /**
     * 
     */
    public final void loadItems(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        /*
         * By default this method populates the complete form, it is up to you to replace this
         * by those fields that are required (this cannot be determined here because it might be
         * the case that many action call this controller method, each with their own set of
         * parameters)
         */
        populateForm(form);
    }

    /**
     * 
     */
    public final void openUserSession(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        /*
         * By default this method populates the complete form, it is up to you to replace this
         * by those fields that are required (this cannot be determined here because it might be
         * the case that many action call this controller method, each with their own set of
         * parameters)
         */
        populateForm(form);
    }

    /**
     * 
     */
    public final void prepareForShipping(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        /*
         * By default this method populates the complete form, it is up to you to replace this
         * by those fields that are required (this cannot be determined here because it might be
         * the case that many action call this controller method, each with their own set of
         * parameters)
         */
        populateForm(form);
    }

    /**
     * This method exists solely to make the application work at runtime by populating
     * the complete form with default values.
     * <p/>
     * You may remove everything under here, including this comment. Simply make sure
     * you properly populate the form when implementing the operations.
     */
    private void populateForm(PurchaseItemsForm form)
    {
        form.setProceedName("proceedName-test");
        form.setProceedPassword("proceedPassword-test");
        form.setItemList(itemListDummyList);
        form.setProceedSelectedItems(java.util.Arrays.asList(new Object[]{"proceedSelectedItems-1", "proceedSelectedItems-2", "proceedSelectedItems-3", "proceedSelectedItems-4", "proceedSelectedItems-5"}));
        form.setProceedSelectedItemsBackingList(new Object[]{"proceedSelectedItems-1", "proceedSelectedItems-2", "proceedSelectedItems-3", "proceedSelectedItems-4", "proceedSelectedItems-5"});
        form.setProceedConfirmedLicence(false);
        form.setBuyThisItemId("buyThisItemId-test");
        form.setPurchaseEmail("purchaseEmail-test");
        form.setPurchaseCreditCard("purchaseCreditCard-test");
        form.setPurchaseAge((int) 633229374);
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
