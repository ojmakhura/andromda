package org.andromda.transformers.atl;

/**
 * Stores the information for target, source and metamodels
 * used within the transformation process.
 * 
 * @author Chad Brandon
 */
public class Model
{
    /**
     * The name of the model.
     */
    private String name;
                
    /**
     * Gets the unique name of the model.
     * 
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the unique name of the model.
     * 
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * The name of the repository to use for loading/transforming this model.
     */
    private String repository;
    
    /**
     * Gets the name of the repository used to transform
     * this model.
     * 
     * @return Returns the repository.
     */
    public String getRepository()
    {
        return repository;
    }

    /**
     * Sets the name of the repository to use for transformation.
     * 
     * @param repository the name of the repository.
     */
    public void setRepository(final String repository)
    {
        this.repository = repository;
    }
    
    /**
     * The physical path to the model.
     */
    private String path;
    
    /**
     * Gets the physical path (a URI) to the model.
     * 
     * @return The physical path (a URI) to the model.
     */
    public String getPath()
    {
        return path;
    }
    
    /**
     * Sets the physical path (a URI) to the model.
     * 
     * @param path The path to set.
     */
    public void setPath(final String path)
    {
        this.path = path;
    }

    /**
     * The name of the metamodel (which must be another model).
     */
    private String metamodel;
    
    /**
     * Gets the name of this model's metamodel.
     * 
     * @return Returns the name metamodel for this model.
     */
    public String getMetamodel()
    {
        return this.metamodel;
    }

    /**
     * Sets the name of this model's metamodel.
     * 
     * @param metamodel The metamodel to set.
     */
    public void setMetamodel(final String metamodel)
    {
        this.metamodel = metamodel;
    }
}