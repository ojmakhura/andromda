package org.andromda.modules.xmilink;

import java.beans.Beans;
import java.util.HashMap;

/**
 * TODO Specify purpose, please.
 * 
 * @author Peter Friese
 * @version 1.0
 * @since 17.09.2004
 */
public class ExportStrategyFactory
{

    /**
     * @stereotype creates
     */
    /* #org.andromda.modules.xmilink.IExportStrategy Dependency_Link */

    /**
     * Stores information about an export strategy.
     * 
     * @author Peter Friese
     * @version 1.0
     * @since 17.09.2004
     */
    private final class StrategyInfo
    {

        private Class clazz;

        private IExportStrategy instance;

        /**
         * Create a new StrategyInfo instance.
         */
        public StrategyInfo(Class clazz)
        {
            this.clazz = clazz;
            this.instance = null;
        }

        /**
         * Returns the class of the export strategy.
         * 
         * @return Returns the clazz.
         */
        public Class getClazz()
        {
            return this.clazz;
        }

        /**
         * Returns a singleton instance of the export strategy. The instance is
         * instanciated lazily and cached locally.
         * 
         * @return Returns the instance.
         */
        public IExportStrategy getInstance()
        {
            if (this.instance == null)
            {
                try
                {
                    Logger.info("Producing export strategy for " + clazz.getName());
                    this.instance = (IExportStrategy)Beans.instantiate(this.getClass()
                            .getClassLoader(), clazz.getName());
                    Logger.info("Produced export strategy for " + clazz.getName());
                }
                catch (Exception e)
                {
                    Logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }
            else
            {
                Logger.info("Already have export strategy for " + clazz.getName() + ": "
                        + this.instance.getClass().toString());
            }
            return this.instance;
        }

    }

    /** Singleton instance of the factory. */
    private static ExportStrategyFactory singleton = new ExportStrategyFactory();

    /** This map contains a list of all strategy classes. */
    private HashMap strategies = new HashMap();

    /**
     * Hide constructor, since we are a singleton.
     */
    private ExportStrategyFactory()
    {
    }

    /**
     * Return the singleton instance of the factory.
     * 
     * @return A reference to the factory.
     */
    public static ExportStrategyFactory getInstance()
    {
        if (singleton == null)
        {
            singleton = new ExportStrategyFactory();
        }
        return singleton;
    }

    /**
     * Export strategies must register themselves using this method.
     * 
     * @param metaClass
     *            The name of the metaclass the export strategy can handle.
     * @param clazz
     *            The class of the export strategy.
     */
    public void registerStrategy(String metaClass, Class clazz)
    {
        Logger.info("Registering export strategy for metaclass " + metaClass);
        strategies.put(metaClass, new StrategyInfo(clazz));
    }

    /**
     * Retrieves an initialized instance of the export strategy for the given
     * metaclass.
     * 
     * @param metaClass
     *            The metclass to retrieve the export strategy for.
     * @return An instance of the correct export strategy.
     */
    public IExportStrategy getStrategy(String metaClass)
    {
        Logger.info("Obtaining export strategy for " + metaClass);
        StrategyInfo info = (StrategyInfo)strategies.get(metaClass);
        if (info != null)
        {
            Logger.info("Found export strategy for " + metaClass);
            return info.getInstance();
        }
        else
        {
            Logger.error("NO EXPORT STRATEGY FOUND FOR METACLASS " + metaClass);
            return null;
        }
    }

}
