using System;
using NHibernate;
using AndroMDA.NHibernateSupport;

namespace SchemaExport
{
    class Program
    {
        public static void Main(string[] args)
        {
            // Show usage information
            Console.WriteLine();
            Console.WriteLine("Usage:");
            Console.WriteLine("  SchemaExport [script] [export]");
            Console.WriteLine("      script=t outputs DDL to the console");
            Console.WriteLine("      export=t exports schema to the database");
            Console.WriteLine("Example:");
            Console.WriteLine("  SchemaExport t f");
            Console.WriteLine();

            // Initialize Log4Net
            log4net.Config.XmlConfigurator.Configure();

            // Start NHibernate
            SessionManagerFactory.SessionManager = new ThreadLocalSessionManager();
            SessionManagerFactory.SessionManager.HandleApplicationStart();

            // Initialize parameters for SchemaExport
            bool script = true;
            bool export = false;
            if (args.Length >= 1)
                { script = args[0].ToLower().Equals("t"); }
            if (args.Length >= 2)
                { export = args[1].ToLower().Equals("t"); }

            // Call CreateDatabase with the specified parameters
            Console.WriteLine(
                "Calling CreateDatabase with script = " + script +
                " and export = " + export + "...");
            Console.WriteLine();
            DbSupport.CreateDatabase(script, export);

            // Stop NHibernate
            SessionManagerFactory.SessionManager.HandleApplicationEnd();
        }
    }
}