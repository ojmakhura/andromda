// Name:   NHibernateDaoSupport.cs
// Author: Naresh Bhatia

using System;
using NHibernate;

namespace AndroMDA.NHibernateSupport
{
    public abstract class NHibernateDaoSupport : INHibernateDaoSupport
    {
        protected ISession Session
        {
            get { return SessionManagerFactory.SessionManager.Session; }
        }

        public void BeginTransaction()
        {
            SessionManagerFactory.SessionManager.BeginTransaction();
        }

        public void CommitTransaction()
        {
            SessionManagerFactory.SessionManager.CommitTransaction();
        }

        public void RollbackTransaction()
        {
            SessionManagerFactory.SessionManager.RollbackTransaction();
        }
    }
}