package org.andromda.core.repository;

import java.io.InputStream;
import java.net.URL;

import org.andromda.core.metafacade.ModelAccessFacade;

/**
 * An interface for objects responsible for being a repository into which an object model can be loaded.
 * <p/>
 * AndroMDA does code generation from an object model. There must exist a repository in which the model can be loaded.
 * The repository must be able to load the object model given a URL. Any repository that supports this API can be used
 * by AndroMDA. </p>
 *
 * @author <A HREF="http://www.amowers.com">Anthony Mowers </A>
 * @author Chad Brandon
 */
public interface RepositoryFacade
{
    /**
     * open and initialize the repository.
     */
    public void open();

    /**
     * close the repository and reclaim all resources
     */
    public void close();

    /**
     * Reads the object model into the repository from the given URL. If the URL is <strong>null </strong> then an empty
     * model will be created in the repository and can be retrieved from {@link #getModel()}.
     * <p/>
     * URLs can be used to point to files on the filesystem, a file in a jar file, a file from a website, data from a
     * database, etc... </p>
     *
     * @param modelURL url of model <strong>NOTE: </strong> if the url of the model isn't specified, then an
     *                 empty model will be created and can be retrieved from {@link #getModel()}
     * @param moduleSearchPath a list of paths from which to search for module models (i.e. models that can be used from
     *                         within other models).
     */
    public void readModel(URL modelURL, String[] moduleSearchPath);
    
    /**
     * Reads the object model into the repository from the given stream. If the stream is <strong>null </strong> then an empty
     * model will be created in the repository and can be retrieved from {@link #getModel()}.
     * <p/>
     *
     * @param stream an InputStream containing a model.
     * @param uri URI of the model that was read into the stream.
     * @param moduleSearchPath a list of paths from which to search for module models (i.e. models that can be used from
     *                         within other models).
     */
    public void readModel(InputStream stream, String uri, String[] moduleSearchPath);

    /**
     * Writes the given <code>model</code> to the specified <code>outputLocation</code>.
     *
     * @param model the <code>model</code> to write.
     * @param outputLocation the location to write the model file.
     * @param version the <code>version</code> of the model to be written (i.e. could be XMI version if the
     *                repository writes XMI files).
     * @param encoding the encoding of the file to be written.
     */
    public void writeModel(Object model, String outputLocation, String version, String encoding);

    /**
     * Writes the given <code>model</code> to the specified <code>outputLocation</code> using the default encoding
     * provided by the model writer.
     *
     * @param model the <code>model</code> to write.
     * @param outputLocation the location to write the model file.
     * @param version the <code>version</code> of the model to be written (i.e. could be XMI version if the
     *                repository writes XMI files).
     */
    public void writeModel(Object model, String outputLocation, String version);

    /**
     * Returns a facade for the top-level model object from the repository. This model object contains all models
     * <code>read</code> into the repository.
     *
     * @return the model value (or <code>null</code> if no models exist in the repository).
     */
    public ModelAccessFacade getModel();
    
    /**
     * Clears the repository of any model(s)
     * (without shutting it down).
     */
    public void clear();
}