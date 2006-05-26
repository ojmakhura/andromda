// Name:   DefaultSessionManager.cs
// Author: Naresh Bhatia

using System;
using System.Collections;
using System.Web;
using NHibernate;
using NHibernate.Cfg;

namespace AndroMDA.NHibernateSupport
{
    /// <summary>
    /// <p>DefaultSessionManager stores the NHibernate session in HttpContext. However,
    /// if HttpContext is not available it will fall back to ThreadLocal variables.
    /// Thus the DefaultSessionManager is usable in ASP.NET, Console and Windows Forms
    /// applications.</p>
    /// 
    /// <p>In ASP.NET applications one HttpRequest is not guaranteed to be serviced by
    /// the same thread. HttpContext is a much more reliable place to store the session
    /// in these applications. That's what the DefaultSessionManager does. However if the
    /// ASP.NET application spawns new threads that do not have an HttpContext, then
    /// this SessionManager will dynamically switch to ThreadLocal variables for such threads.
    /// Of course in Console and Windows Forms applications there is no HttpContext. In such
    /// applications the DefaultSessionManager quitely switches to ThreadLocal variables.
    /// You just pay a minor penalty to check for HttpContext every time you need to access
    /// the Session.</p>
    ///
    /// <p>To use the DefaultSessionManager you must store the NHibernate configuration
    /// in an XML file. For an ASP.NET application, add the name of this file to your
    /// Web.config as follows. The key must be "nhibernate.config" and the value must
    /// be the path of your NHibernate configuration file starting from the web root.</p>
    /// 
    /// <pre>
    ///   <appSettings>
    ///       <add key="nhibernate.config" value="~/nhibernate.config" />
    ///   </appSettings>
    /// </pre>
    /// 
    /// <p>For a Console or Windows Forms application, add the name of the XML configuration
    /// file to your App.config as follows. The key must be "nhibernate.config" and the value
    /// must be the path of your NHibernate configuration file.</p>
    /// 
    /// <pre>
    ///   <appSettings>
    ///       <add key="nhibernate.config" value="nhibernate.config" />
    ///   </appSettings>
    /// </pre>
    /// </summary>
    public class DefaultSessionManager : ISessionManager
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(typeof(DefaultSessionManager));

        /// <summary>
        /// NHibernate configuration
        /// </summary>
        private Configuration config = null;
        public Configuration Config
        {
            get { return config; }
        }

        /// <summary>
        /// NHibernate factory for creating sessions
        /// </summary>
        private ISessionFactory sessionFactory = null;
        public ISessionFactory SessionFactory
        {
            get 
			{
				if (sessionFactory == null)
				{
					BuildSessionFactory();
				}
				return sessionFactory; 
			}
        }

        /// <summary>
        /// NHibernate session
        /// </summary>
        [ThreadStatic]
        private static ISession threadStaticSession;
        public ISession Session
        {
            get
            {
                ISession session = GetSession();
                if (session == null)
                {
                    if (isSessionInitializedLazily)
                        session = OpenSession();
                    else
                        throw new Exception("Session does not exist.");
                }

                return session;
            }
        }

        // Key used to store the NHibernate session in HttpContext.
        private const string NHibernateSessionKey = "NHibernate.Session";


        private ISession GetSession()
        {
            return (HttpContext.Current != null) ?
                (ISession)HttpContext.Current.Items[NHibernateSessionKey] : threadStaticSession;
        }

        private void SetSession(ISession session)
        {
            if (HttpContext.Current != null)
                HttpContext.Current.Items[NHibernateSessionKey] = session;
            else
                threadStaticSession = session;
        }

        private ISession OpenSession()
        {
            ISession session = sessionFactory.OpenSession();
            SetSession(session);
            if (log.IsDebugEnabled)
                log.Debug("Opened hibernate session");
            return session;
        }

        private void CloseSession()
        {
            ISession session = GetSession();
            if (session != null)
            {
                session.Close();
                SetSession(null);
                if (log.IsDebugEnabled)
                    log.Debug("Closed hibernate session");
            }
        }

        /// <summary>
        /// NHibernate transaction
        /// </summary>
        [ThreadStatic]
        private static ITransaction threadStaticTransaction;

        // Key used to store the NHibernate transaction in HttpContext.
        private const string NHibernateTransactionKey = "NHibernate.Transaction";

        private ITransaction GetTransaction()
        {
            return (HttpContext.Current != null) ?
                (ITransaction)HttpContext.Current.Items[NHibernateTransactionKey] : threadStaticTransaction;
        }

        private void SetTransaction(ITransaction transaction)
        {
            if (HttpContext.Current != null)
                HttpContext.Current.Items[NHibernateTransactionKey] = transaction;
            else
                threadStaticTransaction = transaction;
        }

        /// <summary>
        /// Boolean that controls whether sessions will be created lazily or not
        /// </summary>
        private bool isSessionInitializedLazily = true;
        public bool IsSessionInitializedLazily
        {
            get { return isSessionInitializedLazily; }
            set { isSessionInitializedLazily = value; }
        }

        public void HandleApplicationStart()
        {
			if (sessionFactory != null)
			{
				throw new Exception("A SessionFactory already exists.  (Was HandleApplicationStart called twice?)");
			}
			BuildSessionFactory();
        }

        public void HandleApplicationEnd()
        {
            config = null;
            sessionFactory = null;
        }

        public void HandleSessionStart()
        {
			// This uses the SessionFactory property, so if the factory
			// will attempt to be initialized if it is null
			if (SessionFactory == null)
			{
				throw new Exception("The nhibernate session factory has not been initialized.");
			}

			if (GetSession() != null)
			{
				throw new Exception("A Session already exists.");
			}
			if (!isSessionInitializedLazily)
			{
				OpenSession();
			}
        }

        public void HandleSessionEnd()
        {
            if (GetTransaction() != null)
                throw new Exception("A transaction still exists.");
            CloseSession();
        }

        public void BeginTransaction()
        {
            if (GetTransaction() != null)
                throw new Exception("A transaction already exists.");

            ITransaction transaction = Session.BeginTransaction();
            SetTransaction(transaction);
        }

        public void CommitTransaction()
        {
            ITransaction transaction = GetTransaction();
            if (transaction != null)
            {
                transaction.Commit();
                SetTransaction(null);
            }
        }

        public void RollbackTransaction()
        {
            ITransaction transaction = GetTransaction();
            if (transaction != null)
            {
                transaction.Rollback();
                SetTransaction(null);
            }
		}

		private void BuildSessionFactory()
		{
			config = new Configuration();
			System.Collections.Specialized.NameValueCollection nhibernateConfig = System.Configuration.ConfigurationSettings.GetConfig("nhibernate") as System.Collections.Specialized.NameValueCollection;
			
			if (SettingExists("nhibernate.config"))
			{
				try
				{
					// Load specified nhibernate.config file
					config.Configure(TranslateConfigPath(GetSetting("nhibernate.config")));
				}
				catch (Exception e)
				{
					throw new Exception("An error occured while attempting to load nhibernate.cfg", e);
				}
			}
			else if (SettingExists("hibernate.dialect", nhibernateConfig))
			{

				// Load embedded hibernate configuration in web.config

				AddSettingToConfig("hibernate.connection.provider", nhibernateConfig);
				AddSettingToConfig("hibernate.connection.driver_class", nhibernateConfig);
				AddSettingToConfig("hibernate.connection.connection_string", nhibernateConfig);
				AddSettingToConfig("hibernate.connection.isolation", nhibernateConfig);
				AddSettingToConfig("hibernate.dialect", nhibernateConfig);

				AddSettingToConfig("hibernate.default_schema", nhibernateConfig);
				AddSettingToConfig("hibernate.use_outer_join", nhibernateConfig);
				AddSettingToConfig("hibernate.max_fetch_depth", nhibernateConfig);
				AddSettingToConfig("hibernate.use_reflection_optimizer", nhibernateConfig);
				AddSettingToConfig("hibernate.cache.provider_class", nhibernateConfig);
				AddSettingToConfig("hibernate.cache.use_minimal_puts", nhibernateConfig);
				AddSettingToConfig("hibernate.cache.use_query_cache", nhibernateConfig);
				AddSettingToConfig("hibernate.cache.query_cache_factory", nhibernateConfig);
				AddSettingToConfig("hibernate.cache.region_prefix", nhibernateConfig);
				AddSettingToConfig("hibernate.query.substitutions", nhibernateConfig);
				AddSettingToConfig("hibernate.show_sql", nhibernateConfig);
				AddSettingToConfig("hibernate.hbm2ddl.auto", nhibernateConfig);

				if (SettingExists("nhibernate.mapping_assembly", nhibernateConfig))
				{
					string mapping_assembly = GetSetting("nhibernate.mapping_assembly", nhibernateConfig);
					config.AddAssembly(mapping_assembly);
				}

			}
			else
			{
				// Attempt to configure with the default settings
				// Usually this attempts to load hibernate.xml.cfg
				config.Configure();
			}

			sessionFactory = config.BuildSessionFactory();
		}

        /// <summary>
        /// Translates the specified virtual path for the NHibernate config file to a
        /// physical path. This method returns the physical path based the type of
        /// the application. For an ASP.NET application it accepts a URL and returns
        /// the absolute file path of the config file. For Console and Windows applications
        /// it returns the supplied path without any change.
        /// </summary>
        /// <param name="virtualPath">
        /// The virtual path (absolute or relative) of the NHibernate config file.
        /// </param>
        /// <returns>The physical path on the server specified by virtualPath.</returns>
        public String TranslateConfigPath(String virtualPath)
        {
            if (HttpContext.Current != null)
                return HttpContext.Current.Request.MapPath(virtualPath);
            else
                return virtualPath;
        }

		#region Setting utilities

		private string GetSetting(string key)
		{
			return GetSetting(key, System.Configuration.ConfigurationSettings.AppSettings);
		}

		private string GetSetting(string key, System.Collections.Specialized.NameValueCollection sourceConfig)
		{
			string setting = sourceConfig[key];
			if (setting == null) setting = string.Empty;
			return setting;
		}

		private bool SettingExists(string key)
		{
			return SettingExists(key, System.Configuration.ConfigurationSettings.AppSettings);
		}

		private bool SettingExists(string key, System.Collections.Specialized.NameValueCollection sourceConfig)
		{
			string setting = sourceConfig[key];
			return !(setting == null || setting == string.Empty);
		}

		private void AddSettingToConfig(string key)
		{
			AddSettingToConfig(key, System.Configuration.ConfigurationSettings.AppSettings);
		}

		private void AddSettingToConfig(string key, System.Collections.Specialized.NameValueCollection sourceConfig)
		{
			if (SettingExists(key, sourceConfig))
			{
				config.SetProperty(key, GetSetting(key, sourceConfig));
			}
		}

		#endregion

    }
}