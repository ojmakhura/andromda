package my.onlinestore.purchase;

import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This controller manages the operations of the 'Purchase' use-case. It is not implemented, this is what is generated
 * by AndroMDA.
 *
 * @author <a href="mailto:draftdog@users.sourceforge.net">Wouter Zoons</a>
 */
class PurchaseController implements PurchaseControllerInterface
{
    private final List dummyItemList = new LinkedList();

    /**
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
        form.setAge(96511);
        form.setEmail("email-test");
        form.setCreditCard("<change-me>");
        form.setName("Name-test");
        form.setConfirmedLicence(false);
        form.setSelectedItems(java.util.Arrays.asList(new Object[]{"selectedItems-1", "selectedItems-2", "selectedItems-3", "selectedItems-4", "selectedItems-5"}));
        form.setSelectedItemsBackingList(new Object[]{"selectedItems-1", "selectedItems-2", "selectedItems-3", "selectedItems-4", "selectedItems-5"});

        form.setItemList(dummyItemList);
    }

    public final class DummyItem implements Serializable
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

