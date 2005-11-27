// Name:   ThreadLocalSessionManager.cs
// Author: Naresh Bhatia

using System;
using NHibernate;

namespace AndroMDA.NHibernateSupport
{
    /// <summary>
    /// The ThreadLocalSessionManager creates a separate session for each thread. It uses
    /// thread local storage for this purpose. Use this SessionManager when YOU control
    /// the ThreadPool (and the lifecycle of the threads), i.e. you can guarantee that
    /// your use of the session will not span across multiple threads. This is possible
    /// for example in a Console application or a Windows Forms application. Do NOT use
    /// this SessionManager in an ASP.NET application because one HttpRequest is not
    /// guaranteed to be serviced by the same thread.
    /// 
    /// To use the ThreadLocalSessionManager you must store the NHibernate configuration
    /// in an XML file. Then add the name of this file to your App.config as follows. The
    /// key must be "nhibernate.config" and the value must be the name of your NHibernate
    /// configuration file.
    ///   <appSettings>
    ///       <add key="nhibernate.config" value="nhibernate.config" />
    ///   </appSettings>
    /// 
    /// Note that the ThreadLocalSessionManager creates the NHibernate session lazily
    /// when the very first request for a session is received.
    /// </summary>
    public class ThreadLocalSessionManager : AbstractSessionManager
    {
        [ThreadStatic]
        private ISession m_session = null;

        [ThreadStatic]
        private ITransaction m_transaction = null;

        public override void HandleSessionStart()
        {
            if (m_sessionFactory == null)
                { throw new Exception("Session factory does not exist."); }
            if (m_session != null)
                { throw new Exception("A Session already exists."); }
        }

        public override void HandleSessionEnd()
        {
            if (m_session != null)
            {
                m_session.Close();
                m_session = null;
            }
        }

        public override ISession Session
        {
            get
            {
                if (m_session == null)
                {
                    m_session = m_sessionFactory.OpenSession();
                }
                return m_session;
            }
        }

        // Transaction management
        public override void BeginTransaction()
        {
            if (m_transaction != null)
                { throw new Exception("A transaction already exists."); }
            m_transaction = Session.BeginTransaction();
        }

        public override void CommitTransaction()
        {
            if (m_transaction != null)
            {
                m_transaction.Commit();
                m_transaction = null;
            }
        }

        public override void RollbackTransaction()
        {
            if (m_transaction != null)
            {
                m_transaction.Rollback();
                m_transaction = null;
            }
        }

        // NHibernate config path translation
        public override String TranslateConfigPath(String virtualPath)
        {
            return virtualPath;
        }
    }
}