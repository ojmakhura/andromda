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
    /// that this SessionManager cannot be used in Console or Windows Forms applications
    /// because no HttpContext exists.
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

        private ISession GetSession()
        {
            return (ISession)HttpContext.Current.Items[NHibernateSessionKey];
        }

        private void SetSession(ISession session)
        {
            HttpContext.Current.Items[NHibernateSessionKey] = session;
        }

        /// <summary> 
        /// Key used to store the NHibernate transaction in HttpContext. 
        /// </summary> 
        private const string NHibernateTransactionKey = "NHibernate.Transaction";

        private ITransaction GetTransaction()
        {
            return (ITransaction)HttpContext.Current.Items[NHibernateTransactionKey];
        }

        private void SetTransaction(ITransaction transaction)
        {
            HttpContext.Current.Items[NHibernateTransactionKey] = transaction;
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