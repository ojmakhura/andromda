// license-header java merge-point
package org.andromda.demo.ejb3.animal;

/**
 * Callback Listener for Entity POJO EJB org.andromda.demo.ejb3.animal.Animal
 *
 * @see org.andromda.demo.ejb3.animal.Animal
 */
public class AnimalListener 
{
    /**
     * Default public no-args constructor
     */
    public AnimalListener() 
    {
        // empty constructor
    }
    
    @javax.persistence.PrePersist
    public void prePersist(org.andromda.demo.ejb3.animal.Animal animal) 
    {
		// pre persist implementation
	}
	
	@javax.persistence.PostPersist
	public void postPersist(org.andromda.demo.ejb3.animal.Animal animal) 
	{
		// post persist implementation
	}
	
	@javax.persistence.PreRemove
	public void preRemove(org.andromda.demo.ejb3.animal.Animal animal) 
	{
		// pre remove implementation
	}
	
	@javax.persistence.PostRemove
	public void postRemove(org.andromda.demo.ejb3.animal.Animal animal) 
	{
		// post remove implementation
	}
	
	@javax.persistence.PreUpdate
	public void preUpdate(org.andromda.demo.ejb3.animal.Animal animal) {
		// pre update implementation
	}
	
	@javax.persistence.PostUpdate
	public void postUpdate(org.andromda.demo.ejb3.animal.Animal animal) 
	{
		// post update implementation
	}
	
	@javax.persistence.PostLoad
	public void postLoad(org.andromda.demo.ejb3.animal.Animal animal) 
	{
		// post load implementation
	}
}
