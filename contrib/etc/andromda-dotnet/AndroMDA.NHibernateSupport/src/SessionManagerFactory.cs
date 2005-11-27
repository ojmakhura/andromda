// Name:   SessionManagerFactory.cs
// Author: Naresh Bhatia

using System;

namespace AndroMDA.NHibernateSupport
{
    public class SessionManagerFactory
    {
        private static ISessionManager m_sessionManager = null;

        // DO not allow instantiation of this class
        private SessionManagerFactory()
        {
        }

        public static ISessionManager SessionManager
        {
            get { return m_sessionManager; }
            set { m_sessionManager = value; }
        }
    }
}