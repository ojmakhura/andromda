package org.andromda.core.server;

import org.andromda.core.configuration.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * A client used to communicate to the the AndroMDA
 * {@link Server} instance.
 *
 * @author Chad Brandon
 */
public class Client
{
    /**
     * Creates a new instance of class.
     *
     * @return the new client instance.
     */
    public static Client newInstance()
    {
        return new Client();
    }

    private Client()
    {
        // can not be instantiated
    }

    /**
     * Runs the client with the given <code>configuration</code>.
     *
     * @param configuration the AndroMDA configuration instance.
     * @throws ConnectException if the client can not connect to an
     *         AndroMDA server instance.
     */
    public void run(final Configuration configuration)
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
                    ObjectOutputStream out = null;
                    BufferedReader serverInput = null;
                    final String host = serverConfiguration.getHost();
                    try
                    {
                        server = new Socket(
                                host,
                                serverConfiguration.getPort());
                        out = new ObjectOutputStream(server.getOutputStream());
                        serverInput = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    }
                    catch (final UnknownHostException exception)
                    {
                        throw new RuntimeException("Can't connect to host '" + host + "'");
                    }
                    out.writeObject(configuration);
                    String inputString = serverInput.readLine();
                    while (inputString == null)
                    {
                        inputString = serverInput.readLine();
                    }
                    out.flush();
                    out.close();
                    serverInput.close();
                    server.close();
                }
                catch (final ConnectException exception)
                {
                    // just re-throw since we need to detect when
                    // we can't communicate with the server
                    throw exception;
                }
                catch (Throwable throwable)
                {
                    throw new ClientException(throwable);
                }
            }
        }
    }
}