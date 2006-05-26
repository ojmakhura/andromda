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
            get 
			{
				// If the session manager has not been set yet
				if (m_sessionManager == null)
				{
					// Create a new default session manager
					m_sessionManager = new DefaultSessionManager();
				}
				return m_sessionManager; 
			}
            set { m_sessionManager = value; }
        }
    }
}