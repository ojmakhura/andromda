// Name:   AbstractSessionManager.cs
// Author: Naresh Bhatia

using System;
using NHibernate;
using NHibernate.Cfg;

namespace AndroMDA.NHibernateSupport
{
    /// The AbstractSessionManager implements a significant portion of the ISessionManager
    /// interface. The only thing it does not implement is the act of acquiring and storing
    /// sessions.
    public abstract class AbstractSessionManager : ISessionManager
    {
        protected Configuration m_config = null;
        protected ISessionFactory m_sessionFactory = null;

        public void HandleApplicationStart()
        {
            if (m_sessionFactory != null)
            { throw new Exception("A SessionFactory already exists."); }

            m_config = new Configuration();
            m_config.Configure(
                TranslateConfigPath(
                System.Configuration.ConfigurationSettings.AppSettings["nhibernate.config"]));
            m_sessionFactory = m_config.BuildSessionFactory();
        }

        public void HandleApplicationEnd()
        {
            m_config = null;
            m_sessionFactory = null;
        }

        public Configuration Config
        {
            get { return m_config; }
        }

        public abstract void HandleSessionStart();
        public abstract void HandleSessionEnd();
        public abstract ISession Session { get; }

        // Transaction management
        public abstract void BeginTransaction();
        public abstract void CommitTransaction();
        public abstract void RollbackTransaction();

        /// <summary>
        /// Translates the specified virtual path for the NHibernate config file to a
        /// physical path. This method should return the physical path based on the
        /// SessionManager implementation. For example, in a web server environment
        /// this method should accept a URL and return the absolute file path of the
        /// config file.
        /// </summary>
        /// <param name="virtualPath">
        /// The virtual path (absolute or relative) of the NHibernate config file.
        /// </param>
        /// <returns>The physical path on the server specified by virtualPath.</returns>
        public abstract String TranslateConfigPath(String virtualPath);
    }
}