package org.andromda.core.server;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.andromda.core.configuration.Configuration;


/**
 * The default AndroMDA {@link Client} instance.
 *
 * @author Chad Brandon
 */
public class DefaultClient
    implements Client
{
    /**
     * @see org.andromda.core.server.Client#run(org.andromda.core.configuration.Configuration)
     */
    public void start(final Configuration configuration)
        throws ConnectException
    {
        this.performServerOperation(configuration, configuration);
    }

    /**
     * @see org.andromda.core.server.Client#stop()
     */
    public void stop(final Configuration configuration)
        throws ConnectException
    {
        this.performServerOperation(configuration, DefaultServer.STOP);
    }

    /**
     * Connects to the server and passes the <code>object</code>
     * to the server.  The server will take the appropriate action based
     * on the value of the <code>object</code>
     * @param configuration the AndroMDA configuration (contains the server information).
     * @param object the object to pass to the server.
     * @throws ConnectException if an error occurs while attempting to connect to the server.
     */
    private void performServerOperation(
        final Configuration configuration,
        final Object object)
        throws ConnectException
    {
        if (configuration != null && configuration.getServer() != null)
        {
            final org.andromda.core.configuration.Server serverConfiguration = configuration.getServer();
            if (serverConfiguration != null)
            {
                try
                {
                    Socket server = null;
                    ObjectInputStream objectInput = null;
                    ObjectOutputStream out = null;
                    final String host = serverConfiguration.getHost();
                    try
                    {
                        server = new Socket(
                                host,
                                serverConfiguration.getPort());
                        objectInput = new ObjectInputStream(new DataInputStream(server.getInputStream()));
                        out = new ObjectOutputStream(server.getOutputStream());
                    }
                    catch (final UnknownHostException exception)
                    {
                        throw new ClientException("Can't connect to host '" + host + "'");
                    }
                    out.writeObject(object);
                    if (object instanceof Configuration)
                    {
                        final Object input = objectInput.readObject();
                        if (input instanceof Throwable)
                        {
                            throw new ClientException((Throwable)input);
                        }
                    }
                    objectInput.close();
                    server.close();
                    out.flush();
                    out.close();
                }
                catch (final ConnectException exception)
                {
                    // just re-throw since we need to detect when
                    // we can't communicate with the server
                    throw exception;
                }
                catch (final Throwable throwable)
                {
                    throw new ClientException(throwable);
                }
            }
        }
    }
}