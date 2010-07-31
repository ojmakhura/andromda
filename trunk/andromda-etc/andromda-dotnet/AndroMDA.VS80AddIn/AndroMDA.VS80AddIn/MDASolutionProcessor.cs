
// AndroMDA Visual Studio 2005 Add-In
// (c)2006 Sapient Corporation

#region Using statements

using System;
using System.IO;
using System.Collections.Generic;
using System.Text;
using System.Threading;

using EnvDTE;
using EnvDTE80;
using VSLangProj;

using ICSharpCode.SharpZipLib.Checksums;
using ICSharpCode.SharpZipLib.Zip;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class MDASolutionProcessor
    {

        #region Member variables

        private DTE m_applicationObject = null;
        private System.Threading.Thread m_thread = null;

        public delegate void SolutionProcessingCompleteHandler(bool success, string errorMessage);
        public delegate void SolutionProcessingStatusHandler(string status);

        public event SolutionProcessingCompleteHandler OnSolutionProcessingComplete;
        public event SolutionProcessingStatusHandler OnSolutionProcessingStatus;

        private ConfigFile m_configuration = null;

        #endregion

        public MDASolutionProcessor(ConfigFile config, DTE applicationObject)
        {
            m_configuration = config;
            m_applicationObject = applicationObject;
        }

        private void AddToStatus(string message)
        {
            if (OnSolutionProcessingStatus != null)
            {
                if (OnSolutionProcessingStatus.Target is System.Windows.Forms.Control)
                {
                    System.Windows.Forms.Control target = OnSolutionProcessingStatus.Target as System.Windows.Forms.Control;
                    target.BeginInvoke(OnSolutionProcessingStatus, new Object[] { message });
                }
                else
                {
                    OnSolutionProcessingStatus(message);
                }
            }
            System.Threading.Thread.Sleep(100);
        }

        public void EnableSolutionForMDA()
        {
            m_thread = new System.Threading.Thread(new ThreadStart(this.UpdateSolutionThread));
            m_thread.Start();
        }

        private void UpdateSolutionThread()
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
                // Fire the processing complete event
                if (OnSolutionProcessingComplete != null)
                {
                    if (OnSolutionProcessingComplete.Target is System.Windows.Forms.Control)
                    {
                        System.Windows.Forms.Control target = OnSolutionProcessingComplete.Target as System.Windows.Forms.Control;
                        target.BeginInvoke(OnSolutionProcessingComplete, new Object[] { success,  errorMessage });
                    }
                    else
                    {
                        OnSolutionProcessingComplete(success,  errorMessage);
                    }
                }
            }
        }


        private void UpdateSolution()
        {
            string basePath = m_configuration["solution.path"];
            AddToStatus("Adding AndroMDA support to solution...");

            Project commonProject = null;
            Project coreProject = null;
            Project schemaExportProject = null;

            // Create/find the common project
            if (m_configuration["projects.common.create"] == "true")
            {
                AddToStatus("Creating common project " + m_configuration["projects.common.name"] + "...");
                commonProject = VSSolutionUtils.AddClassLibraryProjectToSolution(m_configuration["projects.common.name"], (Solution2)m_applicationObject.Solution);
            }
            else
            {
                AddToStatus("Using existing common project " + m_configuration["projects.common.name"] + "...");
                commonProject = VSSolutionUtils.FindProjectByName(m_configuration["projects.common.name"], m_applicationObject.Solution);
            }
            //CreateDirectory(basePath + "\\" + m_configuration["projects.common.dir"] + "\\src");
            //CreateDirectory(basePath + "\\" + m_configuration["projects.common.dir"] + "\\target");
            commonProject.ProjectItems.AddFolder("src", Constants.vsProjectItemKindPhysicalFolder);
            commonProject.ProjectItems.AddFolder("target", Constants.vsProjectItemKindPhysicalFolder);

            WriteFile(basePath + "\\" + m_configuration["projects.common.dir"] + "\\project.xml", ParseVariables(Resource1.common_project_xml));

            // Create/find the common project
            if (m_configuration["projects.core.create"] == "true")
            {
                AddToStatus("Creating core project " + m_configuration["projects.core.name"] + "...");
                coreProject = VSSolutionUtils.AddClassLibraryProjectToSolution(m_configuration["projects.core.name"], (Solution2)m_applicationObject.Solution);
            }
            else
            {
                AddToStatus("Using existing core project " + m_configuration["projects.core.name"] + "...");
                coreProject = VSSolutionUtils.FindProjectByName(m_configuration["projects.core.name"], m_applicationObject.Solution);
            }
            //CreateDirectory(basePath + "\\" + m_configuration["projects.core.dir"] + "\\src");
            //CreateDirectory(basePath + "\\" + m_configuration["projects.core.dir"] + "\\target");
            coreProject.ProjectItems.AddFolder("src", Constants.vsProjectItemKindPhysicalFolder);
            coreProject.ProjectItems.AddFolder("target", Constants.vsProjectItemKindPhysicalFolder);
            WriteFile(basePath + "\\" + m_configuration["projects.core.dir"] + "\\project.xml", ParseVariables(Resource1.core_project_xml));

            // Create the schema export project
            if (m_configuration["projects.schemaexport.create"] == "true")
            {
                AddToStatus("Creating schema export project " + m_configuration["projects.schemaexport.name"] + "...");
                schemaExportProject = VSSolutionUtils.AddConsoleAppProjectToSolution(m_configuration["projects.schemaexport.name"], (Solution2)m_applicationObject.Solution);
                WriteFile(basePath + "\\" + m_configuration["projects.schemaexport.dir"] + "\\App.config", ParseVariables(Resource1.SchemaExport_App_config));
                WriteFile(basePath + "\\" + m_configuration["projects.schemaexport.dir"] + "\\nhibernate.config", ParseVariables(Resource1.SchemaExport_nhibernate_config));
                WriteFile(basePath + "\\" + m_configuration["projects.schemaexport.dir"] + "\\Program.cs", ParseVariables(Resource1.SchemaExport_Program_cs));
                ProjectItem appConfig = schemaExportProject.ProjectItems.AddFromFile(basePath + "\\" + m_configuration["projects.schemaexport.dir"] + "\\App.config");
                ProjectItem nhibernateConfig = schemaExportProject.ProjectItems.AddFromFile(basePath + "\\" + m_configuration["projects.schemaexport.dir"] + "\\nhibernate.config");

                // Set the config files 'Copy to Output Directory' property to 'Copy if Newer'
                appConfig.Properties.Item("CopyToOutputDirectory").Value = 2;
                appConfig.Properties.Item("BuildAction").Value = VSLangProj.prjBuildAction.prjBuildActionContent;
                nhibernateConfig.Properties.Item("CopyToOutputDirectory").Value = 2;
                nhibernateConfig.Properties.Item("BuildAction").Value = VSLangProj.prjBuildAction.prjBuildActionContent;

                schemaExportProject.ProjectItems.AddFromFile(basePath + "\\" + m_configuration["projects.schemaexport.dir"] + "\\Program.cs");
            }

            // Render /maven.xml
            WriteFile(basePath + "\\maven.xml", ParseVariables(Resource1.maven_xml));
            // Render /project.properties
            WriteFile(basePath + "\\project.properties", ParseVariables(Resource1.project_properties));
            // Render /project.xml
            WriteFile(basePath + "\\project.xml", ParseVariables(Resource1.project_xml));

            // Create mda directory
            CreateDirectory(basePath + "\\mda");
            // Render /mda/maven.xml
            WriteFile(basePath + "\\mda\\maven.xml", ParseVariables(Resource1.mda_maven_xml));
            // Render /mda/project.properties
            WriteFile(basePath + "\\mda\\project.properties", ParseVariables(Resource1.mda_project_properties));
            // Render /mda/project.xml
            WriteFile(basePath + "\\mda\\project.xml", ParseVariables(Resource1.mda_project_xml));

            // Create mda/conf directory
            CreateDirectory(basePath + "\\mda\\conf");
            // Render /mda/conf/andromda.xml
            WriteFile(basePath + "\\mda\\conf\\andromda.xml", ParseVariables(Resource1.mda_conf_andromda_xml));

            // Create mda/src directory
            System.IO.Directory.CreateDirectory(basePath + "\\mda\\src");
            // Create mda/uml directory
            System.IO.Directory.CreateDirectory(basePath + "\\mda\\src\\uml");
            // Create the empty model file
            WriteCompressedFile(m_configuration["application.model.filename.unzipped"], basePath + "\\mda\\src\\uml\\" + m_configuration["application.model.filename"], Resource1.mda_src_uml_empty_model_xml);

            // Create the lib directory
            CreateDirectory(basePath + "\\Lib");
            // Write the libraries out
            WriteFile(basePath + "\\Lib\\AndroMDA.NHibernateSupport.dll", Resource1.lib_AndroMDA_NHibernateSupport_dll);
            WriteFile(basePath + "\\Lib\\Bamboo.Prevalence.dll", Resource1.lib_Bamboo_Prevalence_dll);
            WriteFile(basePath + "\\Lib\\Castle.DynamicProxy.dll", Resource1.lib_Castle_DynamicProxy_dll);
            WriteFile(basePath + "\\Lib\\HashCodeProvider.dll", Resource1.lib_HashCodeProvider_dll);
            WriteFile(basePath + "\\Lib\\Iesi.Collections.dll", Resource1.lib_Iesi_Collections_dll);
            WriteFile(basePath + "\\Lib\\log4net.dll", Resource1.lib_log4net_dll);
            WriteFile(basePath + "\\Lib\\NHibernate.Caches.Prevalence.dll", Resource1.lib_NHibernate_Caches_Prevalence_dll);
            WriteFile(basePath + "\\Lib\\NHibernate.Caches.SysCache.dll", Resource1.lib_NHibernate_Caches_SysCache_dll);
            WriteFile(basePath + "\\Lib\\NHibernate.dll", Resource1.lib_NHibernate_dll);
            WriteFile(basePath + "\\Lib\\NHibernate.Nullables2.dll", Resource1.lib_NHibernate_Nullables2_dll);

            AddToStatus("Adding project references...");

            // Add the references to all the DLLs we just put in /Lib
            AddBinaryReferences(commonProject, basePath);
            AddBinaryReferences(coreProject, basePath);

            // Add a reference from the core project to the common project
            AddProjectReference(coreProject, commonProject);

            // If we created the schema export project
            if (schemaExportProject != null)
            {
                // Add the references to the DLLs
                AddBinaryReferences(schemaExportProject, basePath);
                
                // Add references to the core and common projects
                AddProjectReference(schemaExportProject, commonProject);
                AddProjectReference(schemaExportProject, coreProject);
            }

        }

        private void AddProjectReference(Project destination, Project projectToReference)
        {
            VSProject proj = destination.Object as VSProject;
            proj.References.AddProject(projectToReference);
        }

        private void AddBinaryReferences(Project p, string basePath)
        {
            VSProject proj = p.Object as VSProject;
            proj.References.Add(basePath + "\\Lib\\AndroMDA.NHibernateSupport.dll");
            proj.References.Add(basePath + "\\Lib\\Castle.DynamicProxy.dll");
            proj.References.Add(basePath + "\\Lib\\HashCodeProvider.dll");
            proj.References.Add(basePath + "\\Lib\\Iesi.Collections.dll");
            proj.References.Add(basePath + "\\Lib\\log4net.dll");
            proj.References.Add(basePath + "\\Lib\\NHibernate.Caches.SysCache.dll");
            proj.References.Add(basePath + "\\Lib\\NHibernate.dll");
            proj.References.Add(basePath + "\\Lib\\NHibernate.Nullables2.dll");
        }

        private void CreateDirectory(string directoryName)
        {
            if (System.IO.Directory.Exists(directoryName))
            {
                AddToStatus("Skipping " + directoryName + " (directory already exists)...");
            }
            else
            {
                AddToStatus("Creating directory " + directoryName + "...");
            }
            AddToStatus("Creating directory " + directoryName + "...");
            try
            {
                System.IO.Directory.CreateDirectory(directoryName);
            }
            catch
            {
            }
        }

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

        private void WriteFile(string filePath, string fileContent)
        {
            if (System.IO.File.Exists(filePath))
            {
                AddToStatus("Skipping " + filePath + " (file already exists)...");
            }
            else
            {
                AddToStatus("Creating " + filePath + "...");
            }
            StreamWriter sw = new StreamWriter(filePath, false);
            sw.Write(ParseVariables(fileContent));
            sw.Close();
        }

        private void WriteCompressedFile(string fileNameInZip, string destinationFile, string fileContent)
        {
            if (System.IO.File.Exists(destinationFile))
            {
                AddToStatus("Skipping " + fileNameInZip + " (file already exists)...");
            }
            else
            {
                AddToStatus("Compressing " + fileNameInZip + "...");
            }

            ZipOutputStream zip = new ZipOutputStream(File.Create(destinationFile));
            zip.SetLevel(6);

            System.Text.ASCIIEncoding encoding = new System.Text.ASCIIEncoding();
            byte[] modelData = encoding.GetBytes(Resource1.mda_src_uml_empty_model_xml);

            ZipEntry entry = new ZipEntry(fileNameInZip);
            entry.DateTime = DateTime.Now;
            entry.Size = modelData.Length;

            Crc32 crc = new Crc32();
            crc.Reset();
            crc.Update(modelData);
            entry.Crc = crc.Value;

            zip.PutNextEntry(entry);
            zip.Write(modelData, 0, modelData.Length);

            zip.Finish();
            zip.Close();
        }

        private void WriteFile(string filePath, Byte[] fileContent)
        {
            if (System.IO.File.Exists(filePath))
            {
                AddToStatus("Skipping " + filePath + " (file already exists)...");
            }
            else
            {
                AddToStatus("Creating " + filePath + "...");
            }
            FileStream fs = new FileStream(filePath, FileMode.Create);
            fs.Write(fileContent, 0, fileContent.Length);
            fs.Close();
        }


    }
}
