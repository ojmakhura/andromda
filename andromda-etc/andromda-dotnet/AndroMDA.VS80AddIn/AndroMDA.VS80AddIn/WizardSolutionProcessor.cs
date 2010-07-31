
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Configuration;
using System.IO;
using System.Text;
using System.ComponentModel;
using System.Threading;
using System.Xml;

using EnvDTE;
using EnvDTE80;
using VSLangProj;

using ICSharpCode.SharpZipLib.Checksums;
using ICSharpCode.SharpZipLib.Zip;

using Velocity = NVelocity.App.Velocity;
using VelocityContext = NVelocity.VelocityContext;
using Template = NVelocity.Template;

#endregion

namespace AndroMDA.VS80AddIn
{
	/// <summary>
	/// This class will process an existing solution and add
	/// AndroMDA support.  This is used solely by the MDA Solution Wizard.
	/// </summary>
    public class WizardSolutionProcessor : AsyncOperation
    {

        #region Member variables

        private DTE m_applicationObject = null;
        private AddInSettings m_settings = null;

        private string m_srcPath = string.Empty;
        private string m_dstPath = string.Empty;

        public delegate void SolutionProcessingCompleteHandler(bool success, string errorMessage);
        public delegate void SolutionProcessingStatusHandler(string status);

        public event SolutionProcessingCompleteHandler OnSolutionProcessingComplete;
        public event SolutionProcessingStatusHandler OnSolutionProcessingStatus;

        private ConfigFile m_configuration = null;

	    // lets make a Context and put data into it
        VelocityContext nvelocityContext;

        #endregion

		#region Constructors

		public WizardSolutionProcessor(ConfigFile config, DTE applicationObject, AddInSettings settings) : base()
        {
            m_configuration = config;
            m_applicationObject = applicationObject;
            m_settings = settings;
        }

		public WizardSolutionProcessor(ISynchronizeInvoke target, ConfigFile config, DTE applicationObject, AddInSettings settings) : base(target)
		{
			m_configuration = config;
			m_applicationObject = applicationObject;
            m_settings = settings;
		}

		#endregion

		/// <summary>
		/// The base class calls this method on a worker
		/// thread when the Start method is called.
		/// </summary>
		protected override void DoWork()
        {
            bool success = false;
            string errorMessage = string.Empty;
            try
            {
                UpdateSolution();
                success = true;
            }
            catch (Exception e)
            {
                errorMessage = "An error occured while attempting to update the solution: " + e.ToString();
            }
            finally
            {
				FireAsync(OnSolutionProcessingComplete, new Object[] { success, errorMessage });
            }
        }

		/// <summary>
		/// The actually processing happens in this method
		/// </summary>
        private void UpdateSolution()
        {

            AddToStatus("Adding AndroMDA support to solution...");

            Project commonProject = null;
            Project coreProject = null;
            Project schemaExportProject = null;
            Project testProject = null;
			Project webProject = null;
			Project webCommonProject = null;

            m_srcPath = m_settings.SolutionWizardResourcesLocation + "\\";
            m_dstPath = m_configuration["solution.path"] + "\\";

			string versionControlType = m_configuration["application.versioncontrol"];
			bool versionControl =  versionControlType != "None";
			string ignoreFile = string.Empty;
			switch (versionControlType)
			{
				case "CVS":
					ignoreFile = ".cvsignore";
					break;
			}
			
            nvelocityContext = new VelocityContext();
            foreach (string key in m_configuration.Keys)
            {
                object value = m_configuration[key];
                if (m_configuration[key] == "true" || m_configuration[key] == "false")
                {
                    value = GetConfigSettingBool(key);
                }

                nvelocityContext.Put("wizard." + key, value);
                nvelocityContext.Put("wizard_" + key.Replace('.', '_'), value);
            }
            
			AddToStatus("Creating AndroMDA configuration files...");

            // Create mda directory
            CreateDirectory("mda");
            // Create mda/conf directory
            CreateDirectory("mda/conf");

            // Render /cvsignore
            if (versionControl)
            {
                WriteFile("cvsignore", ignoreFile);
            }

            if (m_configuration["application.andromda.bootstrap"] == "Apache Maven 2.x")
            {

                // Render /pom.xml
                WriteFile("pom.xml", WriteFileProperties.RenderTemplate | WriteFileProperties.ParseVariables);

                // Render /mda/pom.xml
                WriteFile("mda/pom.xml", WriteFileProperties.RenderTemplate | WriteFileProperties.ParseVariables);

                // Render /mda/conf/andromda.xml                
                WriteFile("mda/conf/andromda.xml", WriteFileProperties.RenderTemplate | WriteFileProperties.ParseVariables);

                // Create mda/conf/mappings directory
                CreateDirectory("mda/conf/mappings");

                // Render /mda/conf/mappings/MergeMappings.xml
                WriteFile("mda/conf/mappings/MergeMappings.xml");

                // Render /mda/conf/mappings/NHibernateTypeMappings.xml (Required for NHibernate 1.2)
                WriteFile("mda/conf/mappings/NHibernateTypeMappings.xml");

                // Create mda/resources
                CreateDirectory("mda/resources");

                // Create mda/resources
                CreateDirectory("mda/resources/templates");

                // Create mda/resources
                CreateDirectory("mda/resources/templates/cs");

                // Create mda/resources
                CreateDirectory("mda/resources/templates/nspring");

                // Create mda/resources
                CreateDirectory("mda/resources/templates/nhibernate");

            }

			// Write mda/cvsignore
			if (versionControl)
			{
                WriteFile("mda/cvsignore", "mda/" + ignoreFile);
			}


			AddToStatus("Creating empty model file...");
			// Create mda/src directory
            CreateDirectory("mda/src");
            // Create mda/src/uml directory
            CreateDirectory("mda/src/uml");

            {
                string modelPackageXML = "<UML:Model xmi.id='_9_5_1_874026a_1149883877463_480535_0' name='" + m_configuration["solution.name"] + "'><UML:Namespace.ownedElement>";
                string xmiIdBase = "_9_5_1_874026a_" + DateTime.Now.Ticks.ToString();
                modelPackageXML += GetXMI(m_configuration["solution.name"].Split('.'), xmiIdBase);
                modelPackageXML += "</UML:Namespace.ownedElement></UML:Model>";

                string emptyModelFileData = ReadFile("mda/src/uml/empty-model.xmi");

                emptyModelFileData = emptyModelFileData.Replace("${wizard.model.packagestructure.xml}", modelPackageXML);

                WriteFileProperties modelProperties = WriteFileProperties.SourceFileIsContent | WriteFileProperties.RenderTemplate | WriteFileProperties.ParseVariables;
                if (GetConfigSettingBool("application.model.zipped"))
                {
                    modelProperties |= WriteFileProperties.Compressed;
                }
                WriteFile(emptyModelFileData, "mda/src/uml/" + m_configuration["application.model.filename"], modelProperties);
            }



            // Create the AndroMDA solution folder
            AddToStatus("Adding solution folder...");
            Solution2 sol = m_applicationObject.Solution as Solution2;
            Project solutionFolder = null;
            try
            {
                solutionFolder = sol.AddSolutionFolder("AndroMDA");
                if (m_configuration["application.andromda.bootstrap"] == "Apache Maven 2.x")
                {

                    solutionFolder.ProjectItems.AddFromFile(m_dstPath + "mda\\pom.xml");
                }
                solutionFolder.ProjectItems.AddFromFile(m_dstPath + "mda\\conf\\andromda.xml");
                solutionFolder.ProjectItems.AddFromFile(m_dstPath + "mda\\conf\\mappings\\MergeMappings.xml");
                solutionFolder.ProjectItems.AddFromFile(m_dstPath + "mda\\conf\\mappings\\NHibernateTypeMappings.xml");
                //solutionFolder.ProjectItems.AddFromFile(m_dstPath + "mda\\src\\uml\\" + m_configuration["application.model.filename"]);
            }
            catch { }

            //////////////////////////////////
            // Create/find the common project
            if (GetConfigSettingBool("projects.common.create"))
            {
                AddToStatus("Creating common project " + m_configuration["projects.common.name"] + "...");
                commonProject = VSSolutionUtils.AddClassLibraryProjectToSolution(m_configuration["projects.common.name"], (Solution2)m_applicationObject.Solution);
            }
            else
            {
                AddToStatus("Using existing common project " + m_configuration["projects.common.name"] + "...");
                commonProject = VSSolutionUtils.FindProjectByName(m_configuration["projects.common.name"], m_applicationObject.Solution);
            }

			try { commonProject.ProjectItems.AddFolder("src", Constants.vsProjectItemKindPhysicalFolder); } catch { }
			try { commonProject.ProjectItems.AddFolder("target", Constants.vsProjectItemKindPhysicalFolder); } catch { }

			// Write common/cvsignore
			if (versionControl)
			{
                WriteFile("Common/cvsignore", m_configuration["projects.common.dir"] + "/" + ignoreFile);
			}


			//////////////////////////////////
			// Create/find the core project
            if (GetConfigSettingBool("projects.core.create"))
            {
                AddToStatus("Creating core project " + m_configuration["projects.core.name"] + "...");
                coreProject = VSSolutionUtils.AddClassLibraryProjectToSolution(m_configuration["projects.core.name"], (Solution2)m_applicationObject.Solution);
            }
            else
            {
                AddToStatus("Using existing core project " + m_configuration["projects.core.name"] + "...");
                coreProject = VSSolutionUtils.FindProjectByName(m_configuration["projects.core.name"], m_applicationObject.Solution);
            }
			try { coreProject.ProjectItems.AddFolder("src", Constants.vsProjectItemKindPhysicalFolder); } catch {}
			try { coreProject.ProjectItems.AddFolder("target", Constants.vsProjectItemKindPhysicalFolder); } catch { }

			// Write core/cvsignore
			if (versionControl)
			{
                WriteFile("Core/cvsignore", m_configuration["projects.core.dir"] + "/" + ignoreFile);
            }


			//////////////////////////////////
			// Create the schema export project
            if (GetConfigSettingBool("projects.schemaexport.create"))
            {
                AddToStatus("Creating schema export project " + m_configuration["projects.schemaexport.name"] + "...");
				schemaExportProject = VSSolutionUtils.AddConsoleAppProjectToSolution(m_configuration["projects.schemaexport.name"], (Solution2)m_applicationObject.Solution);

                WriteFile("SchemaExport/App.config", m_configuration["projects.schemaexport.dir"] + "/App.config");
                WriteFile("SchemaExport/nhibernate.config", m_configuration["projects.schemaexport.dir"] + "/nhibernate.config");
                WriteFile("SchemaExport/SchemaExport.cs", m_configuration["projects.schemaexport.dir"] + "/SchemaExport.cs");
                WriteFile("SchemaExport/TestDataManager.cs", m_configuration["projects.schemaexport.dir"] + "/TestDataManager.cs");
                                
                ProjectItem appConfig = schemaExportProject.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.schemaexport.dir"] + "\\App.config");
                ProjectItem nhibernateConfig = schemaExportProject.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.schemaexport.dir"] + "\\nhibernate.config");

                // Set the config files 'Copy to Output Directory' property to 'Copy if Newer'
                appConfig.Properties.Item("CopyToOutputDirectory").Value = 2;
                appConfig.Properties.Item("BuildAction").Value = VSLangProj.prjBuildAction.prjBuildActionContent;
                nhibernateConfig.Properties.Item("CopyToOutputDirectory").Value = 2;
                nhibernateConfig.Properties.Item("BuildAction").Value = VSLangProj.prjBuildAction.prjBuildActionContent;

                schemaExportProject.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.schemaexport.dir"] + "\\SchemaExport.cs");
                schemaExportProject.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.schemaexport.dir"] + "\\TestDataManager.cs");
			
				// Write SchemaExport/cvsignore
				if (versionControl)
				{
                    WriteFile("SchemaExport/cvsignore", m_configuration["projects.schemaexport.dir"] + "/" + ignoreFile);
                }
			}


			//////////////////////////////////
			// Configure the web project
			if (GetConfigSettingBool("projects.web.configure"))
			{

				//////////////////////////////////
				// Create/find the web project
				if (GetConfigSettingBool("projects.web.create"))
				{
					AddToStatus("Creating web project " + m_configuration["projects.web.name"] + "...");
					webProject = VSSolutionUtils.AddWebProjectToSolution(m_configuration["projects.web.name"], (Solution2)m_applicationObject.Solution);
				}
				else
				{
					AddToStatus("Using existing web project " + m_configuration["projects.web.name"] + "...");
					webProject = VSSolutionUtils.FindProjectByName(m_configuration["projects.web.dir"], m_applicationObject.Solution);
				}

				// Write the nhibernate.config if required
				if (GetConfigSettingBool("projects.web.usenhibernateconfig"))
				{
                    WriteFile("Web/nhibernate.config", webProject.Name + "nhibernate.config", WriteFileProperties.DestinationPathAlreadyComplete | WriteFileProperties.ParseVariables);
				}

				string webConfigDstFile = webProject.Name + "Web.config";

				if (!System.IO.File.Exists(webConfigDstFile))
				{
					// Write out the web.config file
                    WriteFile("Web/web.config", webConfigDstFile, WriteFileProperties.DestinationPathAlreadyComplete | WriteFileProperties.ParseVariables);
					webProject.ProjectItems.AddFromFile(webConfigDstFile);
				}

				// Open the web.config file
				System.Configuration.Configuration webconfig = OpenWebConfig(webConfigDstFile);

				// If the nhibernate settings are stored in nhibernate.config
				if (GetConfigSettingBool("projects.web.usenhibernateconfig"))
				{
					// Add that to the app settings
					if (webconfig.AppSettings.Settings["nhibernate.config"] == null)
					{
						webconfig.AppSettings.Settings.Add("nhibernate.config", "~/nhibernate.config");
					}
					else
					{
						webconfig.AppSettings.Settings.Add("nhibernate.config", "~/nhibernate.config");
					}
					// Remove the nhibernate section if it exists
					if (webconfig.Sections.Get("nhibernate") != null)
					{
						webconfig.Sections.Remove("nhibernate");
					}
				}
				else
				{
					// Remove the nhibernate.config app setting if it exists
					if (webconfig.AppSettings.Settings["nhibernate.config"] != null)
					{
						webconfig.AppSettings.Settings.Remove("nhibernate.config");
					}

					// Add the nhibernate config to the web.config
					DefaultSection nhibernateSection = new DefaultSection();
					nhibernateSection.SectionInformation.Type = "System.Configuration.NameValueSectionHandler, System, Version=1.0.5000.0,Culture=neutral, PublicKeyToken=b77a5c561934e089";
					
                    nhibernateSection.SectionInformation.SetRawXml(ParseVariables(ReadFile("web/web.config.nhibernate")));

                    webconfig.Sections.Add("nhibernate", nhibernateSection);
					
				}				

				// Write Web/cvsignore and Web\Bin\cvsignore
				if (versionControl)
				{
                    WriteFile("web/cvsignore", webProject.Name + ignoreFile, WriteFileProperties.DestinationPathAlreadyComplete);
					CreateDirectory(webProject.Name + "Bin", false);
                    WriteFile("web/bin/cvsignore", webProject.Name + "Bin\\" + ignoreFile, WriteFileProperties.DestinationPathAlreadyComplete);
				}

				if (GetConfigSettingBool("projects.web.common.configure"))
				{
					// Create/find the core project
					if (GetConfigSettingBool("projects.web.common.create"))
					{
						AddToStatus("Creating web common project " + m_configuration["projects.web.common.name"] + "...");
						webCommonProject = VSSolutionUtils.AddClassLibraryProjectToSolution(m_configuration["projects.web.common.name"], (Solution2)m_applicationObject.Solution);
					}
					else
					{
						AddToStatus("Using existing web common project " + m_configuration["projects.web.common.name"] + "...");
						webCommonProject = VSSolutionUtils.FindProjectByName(m_configuration["projects.web.common.name"], m_applicationObject.Solution);
					}
					
				}

				// Get the web site object
				VsWebSite.VSWebSite webSite = webProject.Object as VsWebSite.VSWebSite;

				// Refresh folder view
				webSite.Refresh();

				// Add Membership support
				if (GetConfigSettingBool("projects.web.addmembership"))
				{
					AddToStatus("Adding membership support...");

					string file;
					ProjectItem membershipFolder = VSSolutionUtils.FindProjectFolder(webCommonProject, "Membership");
					if (membershipFolder == null)
					{						
						membershipFolder = webCommonProject.ProjectItems.AddFolder("Membership", Constants.vsProjectItemKindPhysicalFolder);
					}

					// Render DomainMembershipProvider.cs
                    WriteFile("Membership/DomainMembershipProvider.cs", m_configuration["projects.web.common.dir"] + "/Membership/DomainMembershipProvider.cs");
                    membershipFolder.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.web.common.dir"] + "\\Membership\\DomainMembershipProvider.cs");

					// Render DomainRoleProvider.cs
                    WriteFile("Membership/DomainRoleProvider.cs", m_configuration["projects.web.common.dir"] + "/Membership/DomainRoleProvider.cs");
                    membershipFolder.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.web.common.dir"] + "\\Membership\\DomainRoleProvider.cs");

					// Render DomainMembershipUser.cs
                    WriteFile("Membership/DomainMembershipUser.cs", m_configuration["projects.web.common.dir"] + "/Membership/DomainMembershipUser.cs");
                    membershipFolder.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.web.common.dir"] + "\\Membership\\DomainMembershipUser.cs");

					// Create core/src/* folder tree from namespace
					string solutionName = m_configuration["solution.name"];
					string[] namespaces = solutionName.Split('.');
					file = m_configuration["projects.core.dir"] + "\\src\\";
					foreach (string folder in namespaces)
					{
						file = file + folder;
						CreateDirectory(file);
						file = file + "\\";
					}
					CreateDirectory(file + "Domain");
					CreateDirectory(file + "Service");

					// Render UserDao.cs
                    WriteFile("Membership/UserDao.cs", file + "Domain/UserDao.cs");
                    coreProject.ProjectItems.AddFromFile(m_dstPath + file + "Domain\\UserDao.cs");

					// Render MembershipService.cs
                    WriteFile("Membership/MembershipService.cs", file + "Service/MembershipService.cs");
                    coreProject.ProjectItems.AddFromFile(m_dstPath + file + "Service\\MembershipService.cs");
					
					ConfigurationSectionGroup systemweb = webconfig.SectionGroups["system.web"];

                    systemweb.Sections["membership"].SectionInformation.SetRawXml(ParseVariables(ReadFile("web/web.config.membershipsection")));
                    systemweb.Sections["roleManager"].SectionInformation.SetRawXml(ParseVariables(ReadFile("web/web.config.rolesection")));
					
                    systemweb.Sections["membership"].SectionInformation.ForceSave = true;
					systemweb.Sections["roleManager"].SectionInformation.ForceSave = true;
					
					// Turn on authentication
					systemweb.Sections["authentication"].SectionInformation.SetRawXml("<authentication mode=\"Forms\"> <forms name=\"FormsAuth\" timeout=\"30\" loginUrl=\"~/Public/Login.aspx\" defaultUrl=\"~/Default.aspx\" path=\"/\" protection=\"All\"/></authentication>");

				}

				// Save the changes to the web.config
				webconfig.Save();

			}

            // Create the lib directory
            CreateDirectory("Lib");

            // Write the libraries out
            WriteBinaryFile("Lib/AndroMDA.NHibernateSupport.dll");
            WriteBinaryFile("Lib/Bamboo.Prevalence.dll");
            WriteBinaryFile("Lib/Castle.DynamicProxy.dll");
            //WriteBinaryFile("Lib/HashCodeProvider.dll");
            WriteBinaryFile("Lib/Iesi.Collections.dll");
            WriteBinaryFile("Lib/log4net.dll");
            WriteBinaryFile("Lib/NHibernate.Caches.Prevalence.dll");
            WriteBinaryFile("Lib/NHibernate.Caches.SysCache.dll");
            WriteBinaryFile("Lib/NHibernate.dll");
            //WriteBinaryFile("Lib/NHibernate.Nullables2.dll");
            WriteBinaryFile("Lib/Nullables.dll");
            WriteBinaryFile("Lib/Nullables.NHibernate.dll");


            //////////////////////////////////
            // Configure the tests project
            if (GetConfigSettingBool("projects.tests.configure"))
            {
                // Create/find the core project
                if (GetConfigSettingBool("projects.tests.create"))
                {
                    AddToStatus("Creating testing project " + m_configuration["projects.tests.name"] + "...");
                    testProject = VSSolutionUtils.AddClassLibraryProjectToSolution(m_configuration["projects.tests.name"], (Solution2)m_applicationObject.Solution);
                }
                else
                {
                    AddToStatus("Using existing testing project " + m_configuration["projects.tests.name"] + "...");
                    testProject = VSSolutionUtils.FindProjectByName(m_configuration["projects.tests.name"], m_applicationObject.Solution);
                }
                WriteFile("Tests/App.config", m_configuration["projects.tests.dir"] + "/App.config", WriteFileProperties.RenderTemplate | WriteFileProperties.ParseVariables);
                WriteFile("Tests/nhibernate.config", m_configuration["projects.tests.dir"] + "/nhibernate.config");

                ProjectItem appConfig = testProject.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.tests.dir"] + "\\App.config");
                ProjectItem nhibernateConfig = testProject.ProjectItems.AddFromFile(m_dstPath + m_configuration["projects.tests.dir"] + "\\nhibernate.config");

                // Set the config files 'Copy to Output Directory' property to 'Copy if Newer'
                appConfig.Properties.Item("CopyToOutputDirectory").Value = 2;
                appConfig.Properties.Item("BuildAction").Value = VSLangProj.prjBuildAction.prjBuildActionContent;
                nhibernateConfig.Properties.Item("CopyToOutputDirectory").Value = 2;
                nhibernateConfig.Properties.Item("BuildAction").Value = VSLangProj.prjBuildAction.prjBuildActionContent;

                AddToStatus("Writing NUnit assemblies...");

                WriteBinaryFile("Lib/nunit.core.dll");
                WriteBinaryFile("Lib/nunit.framework.dll");

                AddToStatus("Adding NUnit references...");

                VSProject vsTestProj = (VSProject)testProject.Object;
                vsTestProj.References.Add(m_dstPath + "Lib\\nunit.core.dll");
                vsTestProj.References.Add(m_dstPath + "Lib\\nunit.framework.dll");

                if (GetConfigSettingBool("projects.tests.scenariounit"))
                {
                    AddToStatus("Writing ScenarioUnit assemblies...");
                    WriteBinaryFile("Lib/NXUnit.Framework.dll");
                    WriteBinaryFile("Lib/AndroMDA.ScenarioUnit.dll");
                    AddToStatus("Adding ScenarioUnit references...");
                    vsTestProj.References.Add(m_dstPath + "Lib\\NXUnit.Framework.dll");
                    vsTestProj.References.Add(m_dstPath + "Lib\\AndroMDA.ScenarioUnit.dll");
                    ProjectItem testDataFolder = testProject.ProjectItems.AddFolder("TestData", Constants.vsProjectItemKindPhysicalFolder);
                    testDataFolder.ProjectItems.AddFolder("actual_output", Constants.vsProjectItemKindPhysicalFolder);
                    testDataFolder.ProjectItems.AddFolder("expected_output", Constants.vsProjectItemKindPhysicalFolder);
                    testDataFolder.ProjectItems.AddFolder("rules", Constants.vsProjectItemKindPhysicalFolder);
                    testDataFolder.ProjectItems.AddFolder("input", Constants.vsProjectItemKindPhysicalFolder);
                }
            }

            // Add the references to all the DLLs we just put in /Lib
			AddToStatus("Adding project references to common project...");
			AddBinaryReferences(commonProject);
			AddToStatus("Adding project references to core project...");
			AddBinaryReferences(coreProject);

            // Add a reference from the core project to the common project
            AddProjectReference(coreProject, commonProject);

            // If we created the schema export project
            if (schemaExportProject != null)
            {
                VSProject proj = schemaExportProject.Object as VSProject;
                AddToStatus("Adding references to schema export project...");
				// Add the references to the DLLs
                AddBinaryReferences(schemaExportProject);
                
                // Add references to the core and common projects
                AddProjectReference(schemaExportProject, commonProject);
                AddProjectReference(schemaExportProject, coreProject);

                AddToStatus("Adding System.Configuration reference to schema export project...");
                try { proj.References.Add("System.Configuration"); } catch { } 
            }

            // If we created the schema export project
            if (testProject != null)
            {
                AddToStatus("Adding references to testing project...");
                // Add the references to the DLLs
                AddBinaryReferences(testProject);

                // Add references to the core and common projects
                AddProjectReference(testProject, commonProject);
                AddProjectReference(testProject, coreProject);
            }


            if (webProject != null)
			{
				AddToStatus("Adding project references to web project...");

				VsWebSite.VSWebSite proj = webProject.Object as VsWebSite.VSWebSite;

                proj.References.AddFromFile(m_dstPath + "Lib\\AndroMDA.NHibernateSupport.dll");

				try { proj.References.AddFromProject(commonProject); } catch {}
				try { proj.References.AddFromProject(coreProject); } catch {}
				if (webCommonProject != null)
				{
					try { proj.References.AddFromProject(webCommonProject); } catch { }
				}
                //m_applicationObject.Solution.Properties.Item("StartupProject").Value = webProject.Name;
			}

			if (webCommonProject != null)
			{
				VSProject proj = webCommonProject.Object as VSProject;
				AddToStatus("Adding common project reference to web common project...");
				try { proj.References.AddProject(commonProject); } catch { }
				AddToStatus("Adding core project reference to web common project...");
				try { proj.References.AddProject(coreProject); } catch { }
				AddToStatus("Adding System.Configuration reference to web common project...");
				try { proj.References.Add("System.Configuration"); } catch { }
				AddToStatus("Adding System.Web reference to web common project...");
				try { proj.References.Add("System.Web"); } catch { }
			}

			AddToStatus("Processing complete, cleaning up...");


		}

		#region Helper methods

		private bool GetConfigSettingBool(string key)
		{
			return m_configuration[key] == "true";
		}

		private string GetXMI(string[] namespaces, string xmlIdBase)
		{
			return GetXMI(namespaces, xmlIdBase, 0);
		}

		private string GetXMI(string[] namespaces, string xmlIdBase, int index)
		{			
			string name = namespaces[index];
			if (index == namespaces.Length - 1)
			{
				if (m_configuration["projects.web.addmembership"] == "true")
				{
                    string emptyModelMembershipXMI = ReadFile("mda/src/uml/empty-model.membership.xmi");
                    return "<UML:Package xmi.id='" + xmlIdBase + "_" + index + "' name='" + name + "'><UML:Namespace.ownedElement>\n" + emptyModelMembershipXMI + "\n</UML:Namespace.ownedElement></UML:Package>";
				}
				else
				{
					return "<UML:Package xmi.id='" + xmlIdBase + "_" + index + "' name='" + name + "'/>";
				}
			}
			else
			{
				return "<UML:Package xmi.id='" + xmlIdBase + "_" + index + "' name='" + name + "'><UML:Namespace.ownedElement>" + GetXMI(namespaces, xmlIdBase, index + 1) + "</UML:Namespace.ownedElement></UML:Package>";
			}
		}

		/// <summary>
		/// Adds a line of text to the status window
		/// </summary>
		/// <param name="message">The message.</param>
		private void AddToStatus(string message)
		{
			FireAsync(OnSolutionProcessingStatus, new Object[] { message });
			System.Threading.Thread.Sleep(0);
		}

		/// <summary>
		/// Adds a project reference to a project in the solution
		/// </summary>
		/// <param name="destination">The destination project.</param>
		/// <param name="projectToReference">The project to reference.</param>
        private void AddProjectReference(Project destination, Project projectToReference)
        {
            VSProject proj = destination.Object as VSProject;
			try { proj.References.AddProject(projectToReference); } catch { }
        }

		/// <summary>
		/// Adds a binary reference to a project in the solution.
		/// </summary>
		/// <param name="p">The destination project</param>
		/// <param name="basePath">The base path.</param>
        private void AddBinaryReferences(Project destination)
        {
            VSProject proj = destination.Object as VSProject;
			AddReference(proj, m_dstPath + "Lib\\AndroMDA.NHibernateSupport.dll");
            AddReference(proj, m_dstPath + "Lib\\Castle.DynamicProxy.dll");
            //AddReference(proj, m_dstPath + "Lib\\HashCodeProvider.dll");
            AddReference(proj, m_dstPath + "Lib\\Iesi.Collections.dll");
            AddReference(proj, m_dstPath + "Lib\\log4net.dll");
            AddReference(proj, m_dstPath + "Lib\\NHibernate.Caches.SysCache.dll");
            AddReference(proj, m_dstPath + "Lib\\NHibernate.dll");
            //AddReference(proj, m_dstPath + "Lib\\NHibernate.Nullables2.dll");
            AddReference(proj, m_dstPath + "Lib\\Nullables.dll");
            AddReference(proj, m_dstPath + "Lib\\Nullables.NHibernate.dll");
        }

		private void AddReference(VSProject proj, string path)
		{
			try { proj.References.Add(path); } catch { }
		}

        public string ReadFile(string srcFile)
        {
            string srcFullPath = m_srcPath + srcFile;
            srcFullPath = srcFullPath.Replace('/', '\\');
            return System.IO.File.ReadAllText(srcFullPath);
        }

        private void CreateDirectory(string directoryName)
        {
            CreateDirectory(directoryName, true);
        }

		/// <summary>
		/// Creates a directory
		/// </summary>
		/// <param name="directoryName">Name of the directory.</param>
        private void CreateDirectory(string directoryName, bool addDestPath)
        {
            string targetPath = directoryName;
            if (addDestPath)
            {
                targetPath = m_dstPath + directoryName;
            }
            targetPath = targetPath.Replace('/', '\\');
            if (System.IO.Directory.Exists(targetPath))
            {
                AddToStatus("Skipping " + directoryName + " (directory already exists)...");
                return;
            }
            AddToStatus("Creating directory " + directoryName + "...");
            try
            {
                System.IO.Directory.CreateDirectory(targetPath);
            }
            catch
            {
            }
        }

		/// <summary>
		/// Parses and resolves all variables in a string
		/// </summary>
		/// <param name="content">The string to parse.</param>
		/// <returns></returns>
        private string ParseVariables(string content)
        {
            foreach (string key in m_configuration.Keys)
            {
                string variableKey = "${wizard." + key + "}";
                if (content.Contains(variableKey))
                {
                    content = content.Replace(variableKey, m_configuration[key]);
                }
            }
            return content;
        }

        /// <summary>
        /// Parses and resolves all variables in a string
        /// </summary>
        /// <param name="content">The string to parse.</param>
        /// <returns></returns>
        private string RenderTemplate(string content, string filename)
        {
            StringWriter writer = new StringWriter();
            Velocity.Evaluate(nvelocityContext, writer, filename, content);
            return writer.GetStringBuilder().ToString();
        }

        [Flags]
        private enum WriteFileProperties
        { 
            ParseVariables = 1, 
            RenderTemplate = 2,
            Compressed = 4,
            DoNotOverwrite = 8,
            SourceFileIsContent = 16,
            DestinationPathAlreadyComplete = 32,
            Normal = 64
        }

        private bool PropSet(WriteFileProperties flags, WriteFileProperties flagToTest)
        {
            return (flags & flagToTest) == flagToTest;
        }

        private void WriteFile(string srcFile)
        {
            WriteFile(srcFile, srcFile);
        }

        private void WriteFile(string srcFile, WriteFileProperties properties)
        {
            WriteFile(srcFile, srcFile, properties);
        }

        private void WriteFile(string srcFile, string dstFile)
        {
            WriteFile(srcFile, dstFile, WriteFileProperties.ParseVariables);
        }

        /// <summary>
		/// Writes a file to disk.  This will call ParseVariables() on the content.
		/// </summary>
		/// <param name="filePath">The file to which to write.</param>
		/// <param name="fileContent">The content to write to the file.</param>
        private void WriteFile(string srcFile, string dstFile, WriteFileProperties properties)
        {
            string dstFullPath;
            if (PropSet(properties, WriteFileProperties.DestinationPathAlreadyComplete))
            {
                dstFullPath = dstFile;
                dstFile = dstFile.Substring(dstFile.LastIndexOf('\\')+1);
            }
            else
            {
                dstFullPath = m_dstPath + dstFile;
            }
            dstFullPath = dstFullPath.Replace('/', '\\');

            if (System.IO.File.Exists(dstFile) && PropSet(properties, WriteFileProperties.DoNotOverwrite))
            {
                AddToStatus("Skipping " + dstFile + " (file already exists)...");
                return;
            }

            AddToStatus("Creating " + dstFile + "...");

            string fileContent;
            if (PropSet(properties, WriteFileProperties.SourceFileIsContent))
            {
                fileContent = srcFile;
            }
            else
            {
                string srcFullPath = m_srcPath + srcFile;
                srcFullPath = srcFullPath.Replace('/', '\\');
                fileContent = System.IO.File.ReadAllText(srcFullPath);
            }

            if (PropSet(properties, WriteFileProperties.ParseVariables))
            {
                fileContent = ParseVariables(fileContent);
            }
            if (PropSet(properties, WriteFileProperties.RenderTemplate))
            {
                fileContent = RenderTemplate(fileContent, srcFile);
            }
            if (PropSet(properties, WriteFileProperties.Compressed))
            {
                ZipOutputStream zip = new ZipOutputStream(File.Create(dstFullPath + ".zip"));
                zip.SetLevel(6);

                System.Text.ASCIIEncoding encoding = new System.Text.ASCIIEncoding();
                byte[] fileData = encoding.GetBytes(fileContent);

                if (dstFile.LastIndexOf('/') > 0)
                {
                    dstFile = dstFile.Substring(dstFile.LastIndexOf('/'));
                }
                ZipEntry entry = new ZipEntry(dstFile);
                entry.DateTime = DateTime.Now;
                entry.Size = fileData.Length;

                Crc32 crc = new Crc32();
                crc.Reset();
                crc.Update(fileData);
                entry.Crc = crc.Value;

                zip.PutNextEntry(entry);
                zip.Write(fileData, 0, fileData.Length);

                zip.Finish();
                zip.Close();
            }
            else
            {
                StreamWriter sw = new StreamWriter(dstFullPath, false);
                sw.Write(fileContent);
                sw.Close();
            }
        }

        private void WriteBinaryFile(string srcFile)
        {
            WriteBinaryFile(srcFile, srcFile, WriteFileProperties.Normal);
        }

        private void WriteBinaryFile(string srcFile, string dstFile, WriteFileProperties properties)
        {
            string dstFullPath = m_dstPath + dstFile;
            dstFullPath = dstFullPath.Replace('/', '\\');
            string srcFullPath = m_srcPath + srcFile;
            srcFullPath = srcFullPath.Replace('/', '\\');
            if (System.IO.File.Exists(dstFullPath))
            {
                AddToStatus("Skipping " + dstFile + " (file already exists)...");
                return;
            }
            AddToStatus("Creating " + dstFile + "...");
            System.IO.File.Copy(srcFullPath, dstFullPath);
        }

		private System.Configuration.Configuration OpenWebConfig(string path)
		{
			string filename = FileUtils.GetFilename(path);
			path = FileUtils.GetPathFromFilename(path);

			string currentDirectory = System.IO.Directory.GetCurrentDirectory();
            System.IO.Directory.SetCurrentDirectory(path);

			ExeConfigurationFileMap map = new ExeConfigurationFileMap();
			map.ExeConfigFilename = filename;
			System.Configuration.Configuration config = System.Configuration.ConfigurationManager.OpenMappedExeConfiguration(map, ConfigurationUserLevel.None);
                
            System.IO.Directory.SetCurrentDirectory(currentDirectory);

			return config;
		}

		#endregion

	}
}
