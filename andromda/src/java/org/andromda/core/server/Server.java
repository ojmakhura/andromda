package org.andromda.core.server;

import org.andromda.core.configuration.Configuration;
import org.andromda.core.engine.Engine;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;


/**
 * The AndroMDA server instance.  This server
 * is configured from a AndroMDA {@link Configuration}
 * instance.  This server initializes the AndroMDA {@link Engine}
 * and listens for requests made from the {@link Client}.
 *
 * @author Chad Brandon
 */
public class Server
{
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(Server.class);
    
    /**
     * Creates a new instance of class.
     *
     * @return the new server instance.
     */
    public static Server newInstance()
    {
        return new Server();
    }
    
    private Server()
    {
        // can not be instantiated
    }    

    /**
     * The message sent to the client when AndroMDA processing
     * has completed.
     */
    private static final String COMPLETE = "complete";

    /**
     * The server listener.
     */
    private ServerSocket listener = null;
    
    /**
     * The AndroMDA Engine instance.
     */
    private Engine engine = Engine.newInstance();

    /**
     * Starts the server instance listening for requests with the given
     * configuration.
     *
     * @throws Exception
     */
    public void start(final Configuration configuration)
    {
        engine.initialize();
        final org.andromda.core.configuration.Server server = configuration.getServer();
        if (server != null)
        {
            try
            {
                try
                {
                    this.listener = new ServerSocket(server.getPort());
                }
                catch (final IOException exception)
                {
                    throw new ServerException(
                        "Could not listen on port '" + server.getPort() + "', change the port in your configuration");
                }
                while (true)
                {
                    final Socket client = this.listener.accept();
                    PrintWriter serverOutput = new PrintWriter(
                            client.getOutputStream(),
                            true);
                    ObjectInputStream objectInput = new ObjectInputStream(new DataInputStream(client.getInputStream()));
                    try
                    {
                        this.engine.run((Configuration)objectInput.readObject());
                    }
                    catch (final Throwable throwable)
                    {
                        logger.error(throwable);
                    }

                    // signal to the client, it can stop waiting
                    serverOutput.write(COMPLETE);
                    serverOutput.flush();
                    serverOutput.close();
                    objectInput.close();
                    client.close();
                }
            }
            catch (final Throwable throwable)
            {
                throw new ServerException(throwable);
            }
        }
    }

    /**
     * Shuts down this server and releases
     * any resources.
     */
    public void shutdown()
    {
        try
        {
            this.listener.close();
        }
        catch (final IOException exception)
        {
            // ignore exception
        }
        this.listener = null;
        this.engine.shutdown();
    }
}