package org.andromda.core.repository;

import java.io.InputStream;

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
     * Opens and initialize the repository.
     */
    public void open();

    /**
     * Closes the repository and reclaims all resources.
     */
    public void close();

    /**
     * <p>
     * Reads the object model into the repository from one or more model URIs. If uris is
     * <strong>null </strong> or zero length, then an empty model will be created in the repository
     * and can be retrieved from {@link #getModel()}.
     * </p>
     *
     * @param uris a list of modelUrls from which to load each URL of the model.
     * @param moduleSearchPath a list of paths from which to search for module models (i.e. models that can be referenced from
     *                         within other models).
     */
    public void readModel(
        String[] uris,
        String[] moduleSearchPath);

    /**
     * Reads the object model into the repository from the given model streams. If the streams is
     * <strong>null </strong> then an empty model will be created in the repository and can be retrieved from {@link #getModel()}.
     * <p/>
     *
     * @param streams a list of InputStream instances containing a model.
     * @param uris a list of URIs from which each stream in the <code>streams</code> parameter was loaded (note that the size
     *             and order of this list must match the order of the streams list).
     * @param moduleSearchPath a list of paths from which to search for module models (i.e. models that can be referenced from
     *                         within other models).
     */
    public void readModel(
        InputStream[] streams,
        String[] uris,
        String[] moduleSearchPath);

    /**
     * Writes the given <code>model</code> to the specified <code>outputLocation</code>.
     *
     * @param model the <code>model</code> to write.
     * @param outputLocation the location to write the model file.
     * @param version the <code>version</code> of the model to be written (i.e. could be XMI version if the
     *                repository writes XMI files).
     * @param encoding the encoding of the file to be written.
     */
    public void writeModel(
        Object model,
        String outputLocation,
        String version,
        String encoding);

    /**
     * Writes the given <code>model</code> to the specified <code>outputLocation</code> using the default encoding
     * provided by the model writer.
     *
     * @param model the <code>model</code> to write.
     * @param outputLocation the location to write the model file.
     * @param version the <code>version</code> of the model to be written (i.e. could be XMI version if the
     *                repository writes XMI files).
     */
    public void writeModel(
        Object model,
        String outputLocation,
        String version);
    
    /**
     * Returns the facade as the given <code>type</code> for the top-level model object from the repository. This model object
     * contains all models <code>read</code> into the repository.  If the type is not specified, the default model access
     * facade will be used.
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