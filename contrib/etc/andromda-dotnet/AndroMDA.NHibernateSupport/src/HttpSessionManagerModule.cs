using System;
using System.Web;

namespace AndroMDA.NHibernateSupport
{
    /// <summary> 
    /// HttpModule to call SessionManager at the end of an HttpRequest. 
    /// </summary> 
    public sealed class HttpSessionManagerModule : IHttpModule
    {
        public void Init(HttpApplication context)
        {
            context.BeginRequest += new EventHandler(BeginRequest);
            context.EndRequest += new EventHandler(EndRequest);
        }

        public void BeginRequest(Object sender, EventArgs e)
        {
            SessionManagerFactory.SessionManager.HandleSessionStart();
        }

        public void EndRequest(Object sender, EventArgs e)
        {
            SessionManagerFactory.SessionManager.HandleSessionEnd();
        }

        public void Dispose() { }
    }
}