// Name:   INHibernateDaoSupport.cs
// Author: Naresh Bhatia

using System;

namespace AndroMDA.NHibernateSupport
{
    public interface INHibernateDaoSupport
    {
        void BeginTransaction();
        void CommitTransaction();
        void RollbackTransaction();
    }
}