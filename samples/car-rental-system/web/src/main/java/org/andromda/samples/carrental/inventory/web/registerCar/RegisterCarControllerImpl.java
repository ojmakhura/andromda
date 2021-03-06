// license-header java merge-point
package org.andromda.samples.carrental.inventory.web.registerCar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import org.andromda.samples.carrental.inventory.CarType;

/**
 * @see org.andromda.samples.carrental.inventory.web.registerCar.RegisterCarController
 */
public class RegisterCarControllerImpl extends RegisterCarController
{

	/**
     * 
	 * @see org.andromda.samples.carrental.inventory.web.registerCar.RegisterCarController#createCar(org.andromda.samples.carrental.inventory.web.registerCar.CreateCarForm)
	 */
	@Override
	public String createCar(CreateCarForm form) throws Throwable {
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setCarTypeId("carTypeId-test");
		List<SelectItem> carTypes = new ArrayList<SelectItem>();
		CarType carType0 = CarType.Factory.newInstance(null, "carTypeId-1",
				null, null);
		CarType carType1 = CarType.Factory.newInstance(null, "carTypeId-2",
				null, null);
		CarType carType2 = CarType.Factory.newInstance(null, "carTypeId-3",
				null, null);
		CarType carType3 = CarType.Factory.newInstance(null, "carTypeId-4",
				null, null);
		CarType carType4 = CarType.Factory.newInstance(null, "carTypeId-5",
				null, null);
		carTypes.add(new SelectItem(carType0, carType0.getIdentifier()));
		carTypes.add(new SelectItem(carType1, carType1.getIdentifier()));
		carTypes.add(new SelectItem(carType2, carType2.getIdentifier()));
		carTypes.add(new SelectItem(carType3, carType3.getIdentifier()));
		carTypes.add(new SelectItem(carType4, carType4.getIdentifier()));
		form.setCarTypeIdBackingList(carTypes);
		return null;
	}

	/**
	 * @see org.andromda.samples.carrental.inventory.web.registerCar.RegisterCarController#searchAllCars(org.andromda.samples.carrental.inventory.web.registerCar.SearchAllCarsForm)
	 */
	@Override
	public void searchAllCars(SearchAllCarsForm form) throws Throwable {
		// this property receives a default value, just to have the application
		// running on dummy data
		form.setCarTypeId("carTypeId-test");
		// form.setCarTypeIdValueList(new Object[] {"carTypeId-1",
		// "carTypeId-2", "carTypeId-3", "carTypeId-4", "carTypeId-5"});
		List<SelectItem> carTypes = new ArrayList<SelectItem>();
		CarType carType0 = CarType.Factory.newInstance(null, "carTypeId-1",
				null, null);
		CarType carType1 = CarType.Factory.newInstance(null, "carTypeId-2",
				null, null);
		CarType carType2 = CarType.Factory.newInstance(null, "carTypeId-3",
				null, null);
		CarType carType3 = CarType.Factory.newInstance(null, "carTypeId-4",
				null, null);
		CarType carType4 = CarType.Factory.newInstance(null, "carTypeId-5",
				null, null);
		carTypes.add(new SelectItem(carType0, carType0.getIdentifier()));
		carTypes.add(new SelectItem(carType1, carType1.getIdentifier()));
		carTypes.add(new SelectItem(carType2, carType2.getIdentifier()));
		carTypes.add(new SelectItem(carType3, carType3.getIdentifier()));
		carTypes.add(new SelectItem(carType4, carType4.getIdentifier()));
		form.setCarTypeIdBackingList(carTypes);
		// populating the table with a dummy list
		form.setExistingcars(existingcarsDummyList);
	}
	
    /**
     * This dummy variable is used to populate the "existingcars" table.
     * You may delete it when you add you own code in this controller.
     */
    private static final Collection existingcarsDummyList =
        Arrays.asList(new ExistingcarsDummy("inventoryNo-1", "registrationNo-1", "id-1"),
            new ExistingcarsDummy("inventoryNo-2", "registrationNo-2", "id-2"),
            new ExistingcarsDummy("inventoryNo-3", "registrationNo-3", "id-3"),
            new ExistingcarsDummy("inventoryNo-4", "registrationNo-4", "id-4"),
            new ExistingcarsDummy("inventoryNo-5", "registrationNo-5", "id-5"));

    /**
     * This inner class is used in the dummy implementation in order to get the web application
     * running without any manual programming.
     * You may delete this class when you add you own code in this controller.
     */
    public static final class ExistingcarsDummy implements Serializable
    {
        private static final long serialVersionUID = -2347126790688719540L;

        private String inventoryNo = null;
        private String registrationNo = null;
        private String id = null;

        public ExistingcarsDummy(String inventoryNo, String registrationNo, String id)
        {
            this.inventoryNo = inventoryNo;
            this.registrationNo = registrationNo;
            this.id = id;
        }

        public void setInventoryNo(String inventoryNo)
        {
            this.inventoryNo = inventoryNo;
        }

        public String getInventoryNo()
        {
            return this.inventoryNo;
        }

        public void setRegistrationNo(String registrationNo)
        {
            this.registrationNo = registrationNo;
        }

        public String getRegistrationNo()
        {
            return this.registrationNo;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getId()
        {
            return this.id;
        }

    }
}
