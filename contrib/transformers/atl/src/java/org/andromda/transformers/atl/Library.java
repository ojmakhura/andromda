package org.andromda.transformers.atl;

/**
 * Stores information about an ATL library used within
 * an ATL source file.
 * 
 * @author Chad Brandon
 */
public class Library
{
    /**
     * The name of the library
     */
    private String name;
    
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * The physical path (a URI) to the library.
     */
    private String path;
    
    /**
     * @return Returns the path.
     */
    public String getPath()
    {
        return path;
    }
    
    /**
     * @param path The path to set.
     */
    public void setPath(String path)
    {
        this.path = path;
    }
}
