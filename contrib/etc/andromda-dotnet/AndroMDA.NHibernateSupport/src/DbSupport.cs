// Name:   DbSupport.cs
// Author: Naresh Bhatia

using System;

namespace AndroMDA.NHibernateSupport
{
    public class DbSupport
    {
        /// <summary>
        /// Creates ddl and/or runs it against the database.
        /// </summary>
        /// <param name="script">true if the ddl should be outputted in the Console.</param>
        /// <param name="export">true if the ddl should be executed against the Database.</param>
        public static void CreateDatabase(bool script, bool export)
        {
            NHibernate.Tool.hbm2ddl.SchemaExport schemaExport =
                new NHibernate.Tool.hbm2ddl.SchemaExport(SessionManagerFactory.SessionManager.Config);
            schemaExport.Create(script, export);
        }
    }
}