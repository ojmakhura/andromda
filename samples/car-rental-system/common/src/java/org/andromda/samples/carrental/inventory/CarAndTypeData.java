package org.andromda.samples.carrental.inventory;

/**
 * Represents a pair of CarData and CarTypeData value objects
 * to be used in InventoryServiceBeanImpl.searchAllCars().
 * 
 * @author Matthias Bohlen
 *
 */
public class CarAndTypeData
{
    private CarData carData;
    private CarTypeData carTypeData;
    
    public CarAndTypeData (CarData cd, CarTypeData ctd)
    {
        this.carData = cd;
        this.carTypeData = ctd;
    }
    
    /**
     * Returns the carData.
     * @return CarData
     */
    public CarData getCarData()
    {
        return carData;
    }

    /**
     * Returns the carTypeData.
     * @return CarTypeData
     */
    public CarTypeData getCarTypeData()
    {
        return carTypeData;
    }
}
