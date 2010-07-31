// Name:   DbSupport.cs
// Author: Naresh Bhatia
using System;
using System.Configuration;
using System.Data;
using System.IO;
using log4net;
using NHibernate;

namespace AndroMDA.NHibernateSupport
{
    /// <summary>
    /// Contains methods supporting the generation of tables based on NHibernate mappings.
    /// </summary>
    public class DbSupport : IDisposable
    {
        private static readonly ILog log = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        
        #region Public static methods

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

        #endregion

        #region Private static methods
        /// <summary>
        /// Executes the given array of sql commands.
        /// </summary>
        /// <param name="sqlCmds">Array of sql commands.</param>
        /// <returns>Flag whether transaction was successful.</returns>
        private static bool ExecuteSqlCommands(string[] sqlCmds)
        {
            // create command for this transaction:
            ISession session = SessionManagerFactory.SessionManager.Session;
            IDbCommand command = session.Connection.CreateCommand();

            SessionManagerFactory.SessionManager.BeginTransaction();
            try
            {
                foreach (string sql in sqlCmds)
                {
                    command.CommandText = sql;
                    command.CommandType = CommandType.Text;
                    session.Transaction.Enlist(command);
                    command.ExecuteNonQuery();
                }

                // all worked well:
                SessionManagerFactory.SessionManager.CommitTransaction();
                return true;
            }
            catch (Exception ex)
            {
                SessionManagerFactory.SessionManager.RollbackTransaction(); 
                log.Error("An error occured while running SQL script.", ex);
                return false;
            }
        }

        #endregion

        #region Public instance methods
        /// <summary>
        /// Generates DDL for the target database to create the tables.
        /// </summary>
        /// <returns>An array of strings containing the DDL.</returns>
        public string[] GenerateCreateSQL() 
        {
            NHibernate.Dialect.Dialect dialect = NHibernate.Dialect.Dialect.GetDialect(SessionManagerFactory.SessionManager.Config.Properties);
            string[] createSql = SessionManagerFactory.SessionManager.Config.GenerateSchemaCreationScript(dialect);
            return createSql;
        }

        /// <summary>
        /// Generates DDL for the target database to drop the tables.
        /// </summary>
        /// <returns>An array of strings containing the DDL.</returns>
        public string[] GenerateDropSQL() 
        {
            NHibernate.Dialect.Dialect dialect = NHibernate.Dialect.Dialect.GetDialect(SessionManagerFactory.SessionManager.Config.Properties);
            string[] dropSql = SessionManagerFactory.SessionManager.Config.GenerateDropSchemaScript(dialect);
            return dropSql;
        }

        /// <summary>
        /// Regenerates the database.
        /// </summary>
        /// <param name="createFile">The create file.</param>
        /// <param name="dropFile">The drop file.</param>
        /// <returns></returns>
        public string RegenerateDatabase(string createFile, string dropFile)
        {
            return RegenerateDatabase(new FileInfo(createFile), new FileInfo(dropFile));
        }

        /// <summary>
        /// This method can be used to regenerate a database schema using NHibernate. 
        /// It can be run multiple times and can delete the old schema, even if your
        /// mapping files have changed the schema since the last time it was run. 
        /// </summary>
        /// <param name="createFile">The <see cref="FileInfo"/> object containing the 
        /// 'create schema' DDL commands. This file does not have to exist.</param>
        /// <param name="dropFile">The <see cref="FileInfo"/> object containing the 
        /// previously generated 'drop schema' DDL commands. This file does not 
        /// have to exist.</param>
        /// <returns>The DDL used to create the tables.</returns>
        public string RegenerateDatabase(System.IO.FileInfo createFile, System.IO.FileInfo dropFile) 
        {
            if (createFile == null)
                throw new ArgumentNullException("createFile", "The create FileInfo object can't be null.");

            if (dropFile == null)
                throw new ArgumentNullException("dropFile", "The drop FileInfo object can't be null.");

            // read in the "Delete Statements SQL" script, if it exists.
            if (dropFile.Exists) 
            {
                // First, copy this to the 'old drop' file
                string oldFile = Path.GetFileNameWithoutExtension(dropFile.Name) + "-old" + dropFile.Extension;
                File.Copy(dropFile.FullName, oldFile, true);

                // read and execute drop script:
                try
                {
                    ExecuteSqlCommands(File.ReadAllLines(dropFile.FullName));
                }
                catch (Exception ex)
                {
                    string errMsg = "An error occurred while opening the delete script. Continuing the schema export process anyway.";
                    log.Error(errMsg, ex);
                }
            }
            else 
            {
                // Create an empty drop file since we are continuing, but we did 
                // not drop anything
                FileStream fs = File.Create(dropFile.FullName);
                fs.Close();
                fs = null;
            }

            // get new create and drop scripts:
            string[] dropSQLArray = GenerateDropSQL();
            string[] createDBArray = GenerateCreateSQL();

            // create new database and save ddl:
            bool errorWhileExporting = false;
            try
            {
                errorWhileExporting = !ExecuteSqlCommands(createDBArray);
                File.WriteAllLines(createFile.FullName, createDBArray);
            }
            catch (Exception ex) 
            {
                log.Error(String.Format("A problem occurred while creating the CREATE DDL file '{0}'.", createFile.FullName), ex);
            }

            // Save the delete script so we can use it the next time
            string dropScriptFileName = dropFile.FullName;
            try
            {
                // if we had errors while creating the schema, then save the new
                // 'drop schema' sql script to a new file:
                if (errorWhileExporting && dropFile != null) 
                {
                    //otherwise, save the old 'Delete' script and create the delete script with a new name.
                    dropScriptFileName = System.IO.Path.GetFileNameWithoutExtension(dropFile.FullName) + ".failure" + dropFile.Extension;
                }

                // don't need this any longer:
                dropFile = null;

                // store drop commands:
                File.WriteAllLines(dropScriptFileName, dropSQLArray);
                log.Info(String.Format("The delete DDL was written to {0}", dropScriptFileName));

                if (errorWhileExporting)
                {
                    throw new ApplicationException("There were errors while exporting the schema to the database. Please review the log for more detailed error information.");
                }
            }
            catch (Exception ex)
            {
                string msg = "Unable to create the '{0}' "+
                    "removal script. This means that, if the schema changes the next time you "+
                    "run SchemaExport, it's likely we won't be able to remove the existing tables  " +
                    "because not all the constraints will be removed.";
                log.Error(String.Format(msg, dropScriptFileName), ex);
                throw new ApplicationException(String.Format(msg, dropScriptFileName), ex);
            }

            return String.Join(Environment.NewLine, createDBArray);
        }

        #endregion

        #region Constructor

        public DbSupport() 
        {
            log4net.Config.XmlConfigurator.Configure();

            // Start NHibernate
            SessionManagerFactory.SessionManager.HandleApplicationStart();
            SessionManagerFactory.SessionManager.HandleSessionStart();
        }

        #endregion

        #region IDisposable Members

        public void Dispose()
        {
            //Stop NHibernate
            SessionManagerFactory.SessionManager.HandleSessionEnd();
            SessionManagerFactory.SessionManager.HandleApplicationEnd();
        }

        #endregion

    }
}