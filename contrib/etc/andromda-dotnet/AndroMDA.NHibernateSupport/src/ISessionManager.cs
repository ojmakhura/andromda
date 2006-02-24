// Name:   ISessionManager.cs
// Author: Naresh Bhatia

using System;
using NHibernate;
using NHibernate.Cfg;

namespace AndroMDA.NHibernateSupport
{
    /// <summary>
    /// ISessionManager is an interface to manage NHibernate sessions. It defines
    /// a contract between the application and itself to guarantee clean and efficient
    /// session management. Impementations may choose to optimize this this task
    /// based on various criteria.
    /// 
    /// The details of the contract are as follows:
    /// 1. When the application starts, it must call ISessionManager.HandleApplicationStart().
    /// 2. When the application ends, it must call ISessionManager.HandleApplicationEnd().
    /// 3. When the application needs to start using an NHibernate session, for example when
    ///    it receives a request from a user, it must call ISessionManager.HandleSessionStart().
    /// 4. When the application is done using the session, for example it has completely
    ///    processed the user's request, it must call ISessionManager.HandleSessionEnd().
    /// 
    /// If the application follows the above protocol, then ISessionManager guarantees the following:
    /// 1. The NHibernate configuration can be accessed anytime between the HandleApplicationStart()
    ///    and HandleApplicationEnd() calls. Use the Config property to access the configuration.
    /// 2. The NHibernate session can be accessed anytime between the HandleSessionStart()
    ///    and HandleSessionEnd() calls. Use the Session property to access the session.
    /// </summary>
    public interface ISessionManager
    {
        // Application start and end
        void HandleApplicationStart();
        void HandleApplicationEnd();

        // Session management
        void HandleSessionStart();
        void HandleSessionEnd();

        // Transaction management
        void BeginTransaction();
        void CommitTransaction();
        void RollbackTransaction();

        // Exposed properties
        Configuration Config { get; }
        ISessionFactory SessionFactory { get; }
        ISession Session { get; }
		bool IsSessionInitializedLazily { get; set; }
	}
}