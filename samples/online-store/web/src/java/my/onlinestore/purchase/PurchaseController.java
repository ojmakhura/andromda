package my.onlinestore.purchase;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PurchaseController implements PurchaseControllerInterface
{
    private static PurchaseController instance = null;

    /**
     * Singleton constructor
     */
    private PurchaseController()
    {
    }

    /**
     * Convenient constructor for child classes. It allows classes
     * that extend this one to set their own static instance without
     * the need for a protected variable or overwriting the
     * <code>getInstance()</code> method.
     */
    protected PurchaseController(PurchaseController instance)
    {
        PurchaseController.instance = instance;
    }

    /**
     * Singleton instance accessor
     */
    public static synchronized PurchaseController getInstance()
    {
        if (PurchaseController.instance == null)
        {
            PurchaseController.instance = new PurchaseController();
        }
        return PurchaseController.instance;
    }

    /**
     *
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void addItemsToBasket(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse reponse) throws Exception
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
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void closeUserSession(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse reponse) throws Exception
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
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void loadItems(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse reponse) throws Exception
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
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void openUserSession(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse reponse) throws Exception
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
     * <p/>
     * This method does not receive any parameters through the form bean.
     */
    public void prepareForShipping(ActionMapping mapping, PurchaseItemsForm form, HttpServletRequest request, HttpServletResponse reponse) throws Exception
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
     * You may remove this method if you want.
     */
    private void populateForm(PurchaseItemsForm form)
    {
        form.setPassword("password-test");
        form.setAge((int)96511);
        form.setEmail("email-test");
        form.setCreditCard("creditCard-test");
        form.setName("name-test");
        form.setConfirmedLicence(false);
        form.setSelectedItems(java.util.Arrays.asList(new Object[]{"selectedItems-1", "selectedItems-2", "selectedItems-3", "selectedItems-4", "selectedItems-5"}));
        form.setSelectedItemsBackingList(new Object[]{"selectedItems-1", "selectedItems-2", "selectedItems-3", "selectedItems-4", "selectedItems-5"});

        DummyItem item1 = new DummyItem("address-1", "id-1", "name-1");
        DummyItem item2 = new DummyItem("address-2", "id-2", "name-2");
        DummyItem item3 = new DummyItem("address-3", "id-3", "name-3");
        DummyItem item4 = new DummyItem("address-4", "id-4", "name-4");
        DummyItem item5 = new DummyItem("address-5", "id-5", "name-5");
        List dummyItemList = java.util.Arrays.asList(new Object[]{item1, item2, item3, item4, item5});

        form.setItemList(dummyItemList);
        form.setItemListBackingList(dummyItemList.toArray());
    }

    public class DummyItem
    {
        private String id = null;
        private String name = null;
        private String address = null;

        public DummyItem(String address, String id, String name)
        {
            this.address = address;
            this.id = id;
            this.name = name;
        }

        public String getAddress()
        {
            return address;
        }

        public void setAddress(String address)
        {
            this.address = address;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }
}

