package org.andromda.core.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.configuration.Configuration;
import org.andromda.core.engine.Engine;


/**
 * The default AndroMDA {@link Server instance}.
 *
 * @author Chad Brandon
 */
public class DefaultServer
    implements Server
{
    /**
     * The message sent to the client when AndroMDA processing has completed.
     */
    private static final String COMPLETE = "complete";

    /**
     * The command sent to the server that indicates it
     * should stop.
     */
    static final String STOP = "stop";

    /**
     * The server listener.
     */
    private ServerSocket listener = null;

    /**
     * The AndroMDA Engine instance.
     */
    private Engine engine = Engine.newInstance();

    /**
     * @see org.andromda.core.server.Server#start(org.andromda.core.configuration.Server)
     */
    public void start(final Configuration configuration)
    {
        engine.initialize();
        if (configuration != null)
        {
            final org.andromda.core.configuration.Server serverConfiguration = configuration.getServer();
            if (serverConfiguration != null)
            {
                try
                {
                    try
                    {
                        this.listener = new ServerSocket(serverConfiguration.getPort());
                        final int modelLoadInterval = serverConfiguration.getLoadInterval();
                        if (modelLoadInterval > 0)
                        {
                            this.listener.setSoTimeout(serverConfiguration.getLoadInterval());
                        }
                    }
                    catch (final IOException exception)
                    {
                        throw new ServerException(
                            "Could not listen on port '" + serverConfiguration.getPort() +
                            "', change the port in your configuration");
                    }
                    while (true)
                    {
                        try
                        {
                            final Socket client = this.listener.accept();
                            if (client != null)
                            {
                                final ObjectOutputStream serverOutput =
                                    new ObjectOutputStream(client.getOutputStream());
                                final ObjectInputStream objectInput =
                                    new ObjectInputStream(new DataInputStream(client.getInputStream()));
                                try
                                {
                                    final Object object = objectInput.readObject();
                                    if (object instanceof Configuration)
                                    {
                                        this.engine.run((Configuration)object);
                                    }
                                    else if (object instanceof String)
                                    {
                                        final String string = (String)object;
                                        if (string.equals(STOP))
                                        {
                                            break;
                                        }
                                    }
                                }
                                catch (final Throwable throwable)
                                {
                                    AndroMDALogger.error(throwable);

                                    // pass the exception to the client
                                    serverOutput.writeObject(throwable);
                                }

                                // signal to the client, it can stop waiting
                                serverOutput.writeObject(COMPLETE);
                                serverOutput.flush();
                                serverOutput.close();
                                objectInput.close();
                                client.close();
                            }
                        }
                        catch (final SocketTimeoutException exception)
                        {
                            try
                            {
                                this.engine.loadModelsIfNecessary(configuration);
                                this.resetFailedLoadAttempts();
                            }
                            catch (final Throwable throwable)
                            {
                                this.incrementFailedLoadAttempts();

                                // only fail if the failed load attempts is greater than the maximum
                                if (this.failedLoadAttempts > serverConfiguration.getMaximumFailedLoadAttempts())
                                {
                                    throw throwable;
                                }
                            }
                        }
                    }
                    this.shutdown();
                }
                catch (final Throwable throwable)
                {
                    throw new ServerException(throwable);
                }
            }
        }
    }

    /**
     * Stores the failed load attempts.
     */
    private int failedLoadAttempts;

    /**
     * Resets the failed load attempt counter.
     */
    private void resetFailedLoadAttempts()
    {
        this.failedLoadAttempts = 0;
    }

    /**
     * Increments the failed load attempt counter.
     */
    private void incrementFailedLoadAttempts()
    {
        this.failedLoadAttempts++;
    }

    /**
     * Shuts the server down.
     */
    public void shutdown()
    {
        try
        {
            this.listener.close();
            this.listener = null;
            this.engine.shutdown();
        }
        catch (final IOException exception)
        {
            // ignore exception
        }
    }
}