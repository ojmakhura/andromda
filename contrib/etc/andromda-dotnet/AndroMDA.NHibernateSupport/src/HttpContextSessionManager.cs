// Name:   HttpContextSessionManager.cs
// Author: Naresh Bhatia

using System;
using System.Web;
using NHibernate;

namespace AndroMDA.NHibernateSupport
{
    /// <summary>
    /// The HttpContextSessionManager stores the NHibernate session in HttpContext. Use
    /// this SessionManager in ASP.NET applications instead of ThreadLocalSessionManager
    /// because one HttpRequest is not guaranteed to be serviced by the same thread. Note
    /// that HttpContextSessionManager will fall back on ThreadLocal variables if it does
    /// not find an HttpContext. This is a useful feature if your ASP.NET application
    /// spawns new threads that do not have an HttpContext. This SessionManager will
    /// switch to ThreadLocal variables for such threads. Note that this feature also
    /// allows HttpContextSessionManager to be used in Console or Windows Forms
    /// applications. You just pay a minor penalty to check for HttpContext every time
    /// you need to access the Session or the Transaction.
    ///
    /// To use the HttpContextSessionManager you must store the NHibernate configuration
    /// in an XML file. Then add the name of this file to your Web.config as follows. The
    /// key must be "nhibernate.config" and the value must be the name of your NHibernate
    /// configuration file.
    ///   <appSettings>
    ///       <add key="nhibernate.config" value="nhibernate.config" />
    ///   </appSettings>
    ///
    /// Note that the HttpContextSessionManager creates the NHibernate session lazily
    /// when the very first request for a session is received.
    /// </summary>
    public class HttpContextSessionManager : AbstractSessionManager
    {
        /// <summary>
        /// Key used to store the NHibernate session in HttpContext.
        /// </summary>
        private const string NHibernateSessionKey = "NHibernate.Session";

        /// <summary>
        /// Key used to store the NHibernate transaction in HttpContext.
        /// </summary>
        private const string NHibernateTransactionKey = "NHibernate.Transaction";

        [ThreadStatic]
        private ISession m_session = null;

        [ThreadStatic]
        private ITransaction m_transaction = null;

        private ISession GetSession()
        {
            return (HttpContext.Current != null) ?
                (ISession)HttpContext.Current.Items[NHibernateSessionKey] : m_session;
        }

        private void SetSession(ISession session)
        {
            if (HttpContext.Current != null)
                HttpContext.Current.Items[NHibernateSessionKey] = session;
            else
                m_session = session;
        }

        private ITransaction GetTransaction()
        {
            return (HttpContext.Current != null) ?
                (ITransaction)HttpContext.Current.Items[NHibernateTransactionKey] : m_transaction;
        }

        private void SetTransaction(ITransaction transaction)
        {
            if (HttpContext.Current != null)
                HttpContext.Current.Items[NHibernateTransactionKey] = transaction;
            else
                m_transaction = transaction;
        }

        public override void HandleSessionStart()
        {
            if (m_sessionFactory == null)
                { throw new Exception("Session factory does not exist."); }
            if (GetSession() != null)
                { throw new Exception("A Session already exists."); }
        }

        public override void HandleSessionEnd()
        {
            if (GetTransaction() != null)
                { throw new Exception("A transaction still exists."); }

            ISession session = GetSession();
            if (session != null)
            {
                session.Close();
                SetSession(null);
            }
        }

        public override ISession Session
        {
            get
            {
                ISession session = GetSession();
                if (session == null)
                {
                    session = m_sessionFactory.OpenSession();
                    SetSession(session);
                }
                return session;
            }
        }

        // Transaction management
        public override void BeginTransaction()
        {
            if (GetTransaction() != null)
                { throw new Exception("A transaction already exists."); }

            ITransaction transaction = Session.BeginTransaction();
            SetTransaction(transaction);
        }

        public override void CommitTransaction()
        {
            ITransaction transaction = GetTransaction();
            if (transaction != null)
            {
                transaction.Commit();
                SetTransaction(null);
            }
        }

        public override void RollbackTransaction()
        {
            ITransaction transaction = GetTransaction();
            if (transaction != null)
            {
                transaction.Rollback();
                SetTransaction(null);
            }
        }

        // NHibernate config path translation
        public override String TranslateConfigPath(String virtualPath)
        {
            return HttpContext.Current.Request.MapPath(virtualPath);
        }
    }
}