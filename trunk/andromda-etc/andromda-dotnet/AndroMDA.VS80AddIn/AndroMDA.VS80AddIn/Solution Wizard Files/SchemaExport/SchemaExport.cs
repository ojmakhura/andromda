//
// SchemaExport
// 

#region Using Statements

using System;
using System.IO;
using System.Configuration;
using NHibernate;
using AndroMDA.NHibernateSupport;

#endregion

namespace ${wizard.solution.name}.SchemaExport
{
	class Program
	{

		public static void Main(string[] args)
		{

			// Initialize Log4Net
			log4net.Config.XmlConfigurator.Configure();
			
			// Get the schema files
			string createSchemaFile = ConfigurationManager.AppSettings["schema.create.file"];
			string dropSchemaFile = ConfigurationManager.AppSettings["schema.drop.file"];
			if (createSchemaFile == null || createSchemaFile.Trim().Length == 0)
			{
				createSchemaFile = "schema-create.sql";
			}
			if (dropSchemaFile == null || dropSchemaFile.Trim().Length == 0)
			{
				dropSchemaFile = "schema-drop.sql";
			}

			// Show usage information
			string usage = 
@"Usage:

	SchemaExport [-o|-e] 
		-o = outputs DDL to the console
		-e = exports schema to the database
	The following example will display the DDL, AND change the database schema:
		SchemaExport -o -e
	The following example will display the DDL, but NOT change the database schema:
		SchemaExport -o        
";

			string msg = String.Empty;

			// Initialize parameters for SchemaExport
			bool outputSchemaScriipt = false;
			bool exportToDatabase = false;

			foreach (string arg in args)
			{
				switch (arg.ToLower().Trim())
				{
					case "-o":
						outputSchemaScriipt = true;
						break;
					case "-e":
						exportToDatabase = true;
						break;
				}
			}

			//They need to enter one of the switches, otherwise we'll kick them out.
			if (!exportToDatabase && !outputSchemaScriipt)
			{
				Console.WriteLine(usage);
				return;
			}

			string outScript = String.Empty;

			if (exportToDatabase)
			{
				try
				{
					using (DbSupport dbSupport = new DbSupport())
					{
		 					outScript = dbSupport.RegenerateDatabase(createSchemaFile, dropSchemaFile);
					}

					TestDataManager.InsertTestData();

					Console.WriteLine("SchemaExport: Export successful.  Schema create and drop files saved to '{0}' and '{1}'.", createSchemaFile, dropSchemaFile);
				}
				catch (Exception e)
				{
					Console.WriteLine("SchemaExport: Export failed (" + e.Message + ")\n");
					throw (e);
				}
			}	
			else
			{
				// Get the create and drop scripts so we can output them to the console
				using (DbSupport dbSupport = new DbSupport())
				{
					string[] dropSql = dbSupport.GenerateDropSQL();
					string[] createSql = dbSupport.GenerateCreateSQL();
					outScript += String.Join(Environment.NewLine, dropSql);
					outScript += String.Join(Environment.NewLine, createSql);

                    File.WriteAllLines(createSchemaFile, createSql);
                    File.WriteAllLines(dropSchemaFile, dropSql);
				}
			}

			if (outputSchemaScriipt)
			{
				Console.WriteLine(outScript);
			}
		}
	}
}
