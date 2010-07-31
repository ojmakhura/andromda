
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;
using System.Threading;
using Extensibility;
using EnvDTE;
using EnvDTE80;
using Microsoft.VisualStudio.CommandBars;
using Microsoft.VisualStudio.VCProject;
using System.Resources;
using System.Reflection;
using System.Globalization;
using System.ComponentModel;

#endregion

namespace AndroMDA.VS80AddIn
{
    /// <summary>The object for implementing an Add-in.</summary>
    /// <seealso class='IDTExtensibility2' />
    public class MDASolutionManager
    {

        #region Member variables

        private bool m_solutionIsUsingMDA = false;
		private bool m_enabled = false;
        private DTE m_applicationObject = null;

        private ConfigFile m_projectProperties = new ConfigFile();
        private ConfigFile m_solutionSettings = null;

        private AddInSettings m_addInSettings = null;

        //private MDAProject m_coreProject = null;
        //private MDAProject m_commonProject = null;
		private MDAProject m_schemaExportProject = null;
        
        private bool m_restartBuild = false;
        private Mutex m_restartBuildMutex = new Mutex(false);

        //private MavenProxy m_mavenProxy = null;
		private MavenProxy m_mavenProxy = null;

        #endregion

        #region Properties

        public bool IsMavenRunning
        {
            get { return m_mavenProxy.IsRunning; }
        }

        public bool RestartBuild
        {
            get { return m_restartBuild; }
            set
            {
                m_restartBuildMutex.WaitOne();
                m_restartBuild = value;
                m_restartBuildMutex.ReleaseMutex();
            }
        }

        /*      
        public MDAProject CoreProject
        {
            get { return m_coreProject; }
        }

        public MDAProject CommonProject
        {
            get { return m_commonProject; }
        }
        */

		public MDAProject SchemaExportProject
		{
			get { return m_schemaExportProject; }
		}

        public bool IsEnabled
        {
            get { return m_enabled && m_applicationObject.Solution.IsOpen; }
        }

		public bool IsSolutionUsingMDA
		{
			get { return m_solutionIsUsingMDA; }
		}

        public DateTime? MavenLastRunDateTime
        {
            get 
            {
                string lastRun = m_solutionSettings["android.lastgenerated"];
                if (lastRun == string.Empty)
                    return null;
                else
                    return DateTime.Parse(lastRun);
            }
            set
            {
                try
                {
                    m_solutionSettings["android.lastgenerated"] = value.ToString();
                    m_solutionSettings.Save();
                }
                catch (Exception e)
                {
                    AddInUtils.ShowError("Unable to save last-generation date: " + e.Message);
                }
            }
        }

        public bool AreGeneratedFilesUpToDate
        {
            get
            {
                if (!IsEnabled)
                {
                    return false;
                }
                DateTime? lastGen = MavenLastRunDateTime;
                if (!lastGen.HasValue || IsDateNewer(ModelFileDateTime, lastGen.Value))
                {
                    return false;
                }
                if (m_addInSettings.PromptToGenerateOnBuildIfSolutionNotSynched)
                {
                    return IsSolutionSynchronizedWithFileSystem();
                }
                return true;
            }
        }

        public string SolutionPath
        {
            get { return VSSolutionUtils.GetSolutionPath(m_applicationObject.Solution); }
        }

        public string ModelFilePath
        {
            get { return m_projectProperties.GetPath("maven.andromda.model.uri"); }
        }

        public DateTime ModelFileDateTime
        {
            get
            {
                System.IO.FileInfo f = new System.IO.FileInfo(m_projectProperties.GetPath("maven.andromda.model.uri"));
                return f.LastWriteTime;
            }
        }

		public bool IsSchemaExportProjectAvailable
		{
			get
			{
				return m_projectProperties["maven.andromda.schemaexport.available"].ToLower().Trim() == "true";
			}
		}

        #endregion

        #region Constructors

        public MDASolutionManager(DTE applicationObject, AddInSettings addInSettings)
        {
            m_applicationObject = applicationObject;
            m_addInSettings = addInSettings;
            m_mavenProxy = new Maven2Proxy(m_applicationObject, m_addInSettings);
            m_mavenProxy.Completed += new EventHandler(m_mavenProxy_Completed);
        }

        #endregion

        #region Initialization

        public void InitializeSolution()
        {
            m_solutionIsUsingMDA = MDAConfigFilesExist(m_applicationObject.Solution);
            m_enabled = false;
            if (m_solutionIsUsingMDA)
            {
                try
                {

                    // Notify the user that the solution is using MDA
                    m_applicationObject.StatusBar.Text = "Android/VS: Solution is AndroMDA-enabled.  Loading configuration...";
                    m_applicationObject.StatusBar.Highlight(true);

                    // Get the solution path
                    string solutionPath = VSSolutionUtils.GetSolutionPath(m_applicationObject.Solution);

                    /// Load the properties file and set the the root directory
                    if (Maven1PropertiesExist(m_applicationObject.Solution))
                    {
                        // Load the properties directly from the project.properties file
                        m_projectProperties.LoadFromFile(GetMaven1ProjectPropertiesPath(m_applicationObject.Solution));
                        m_projectProperties["maven.version"] = "1";
                    }
                    else if (Maven2PropertiesExist(m_applicationObject.Solution))
                    {
                        // Load the properties from the pom.xml
                        m_projectProperties.LoadFromXML(GetMaven2ProjectPropertiesPath(m_applicationObject.Solution), "maven.andromda.");
                        m_projectProperties["maven.version"] = "2";
                    }
                    else
                    {
                        throw new Exception("Unable to locate the project's properties file.");
                    }
                    m_projectProperties["maven.src.dir"] = solutionPath + "\\mda\\src";
                    m_projectProperties["project.build.sourceDirectory"] = solutionPath + "\\mda\\src\\uml";
                    m_projectProperties["pom.basedir"] = solutionPath + "\\mda";

                    if (m_projectProperties["maven.version"] == "2" && m_mavenProxy is Maven1Proxy)
                    {
                        m_mavenProxy = new Maven2Proxy(m_applicationObject, m_addInSettings);
                    }
                    else if (m_projectProperties["maven.version"] == "1" && m_mavenProxy is Maven2Proxy)
                    {
                        m_mavenProxy = new Maven1Proxy(m_applicationObject, m_addInSettings);
                    }

                    m_mavenProxy.Completed += new EventHandler(m_mavenProxy_Completed);

                    ////
                    ////  Resolve the core and common projects
                    ////

                    //m_coreProject = null;
                    //m_commonProject = null;
                    m_schemaExportProject = null;

                    // Iterate through the projects and search for the core and common directories
                    foreach (Project p in m_applicationObject.Solution.Projects)
                    {
                        try
                        {
                            // Get the project's directory
                            string projectPath = FileUtils.GetPathFromFilename(p.FileName);

                            /*
                            // Check if the current (core) directory is the same as the project directory
                            if (FileUtils.CompareDirectories(projectPath, m_projectProperties.GetPath("maven.andromda.core.dir")))
                            {
                                // if so this is the core project
                                string generatedPath = m_projectProperties.GetPath("maven.andromda.core.generated.dir");
                                string manualPath = m_projectProperties.GetPath("maven.andromda.core.manual.dir");
                                m_coreProject = new MDAProject(p, FileUtils.GetFilename(projectPath), projectPath, generatedPath, manualPath);

                            }

                            // Check if the current (common) directory is the same as the project directory
                            if (FileUtils.CompareDirectories(projectPath, m_projectProperties.GetPath("maven.andromda.common.dir")))
                            {
                                // if so this is the common project
                                string generatedPath = m_projectProperties.GetPath("maven.andromda.common.generated.dir");
                                m_commonProject = new MDAProject(p, FileUtils.GetFilename(projectPath), projectPath, generatedPath, string.Empty);
                            }
                            */

                            // Check if the current (schemaexport) directory is the same as the project directory
                            if (IsSchemaExportProjectAvailable && FileUtils.CompareDirectories(projectPath, m_projectProperties.GetPath("maven.andromda.schemaexport.dir")))
                            {
                                // if so this is the common project
                                m_schemaExportProject = new MDAProject(p, FileUtils.GetFilename(projectPath), projectPath, string.Empty, string.Empty);
                            }

                        }
                        catch (NotImplementedException)
                        {
                            // Swallow this exception (it means the project was not loaded for some reason)
                        }
                    }
                    /*
                    // Show an error message if either the core or common projects could not be found
                    if (m_coreProject == null || m_commonProject == null)
                    {
                        string errorMessage = "The AndroMDA configuration was loaded, but the ";
                        if (m_coreProject == null)
                        {
                            errorMessage += "core (" + m_projectProperties["maven.andromda.core.assembly.name"] + ") ";
                            if (m_commonProject == null) { errorMessage += "and "; }
                        }
                        if (m_commonProject == null) { errorMessage += "common (" + m_projectProperties["maven.andromda.common.assembly.name"] + ") "; }
                        errorMessage += "project";
                        if (m_commonProject == null && m_coreProject == null) errorMessage += "s";
                        errorMessage += " could not be found in the solution.";
                        throw new Exception(errorMessage);
                    }
                    */
                    m_solutionSettings = new ConfigFile(solutionPath + "\\mda\\android.user.properties");
                    m_applicationObject.StatusBar.Highlight(false);
                    m_applicationObject.StatusBar.Text = "Android/VS: AndroMDA configuration loaded.";
                    m_enabled = true;
                }
                catch (Exception e)
                {
                    m_enabled = false;
                    AddInUtils.ShowWarning("An error occured while trying to load the AndroMDA configuration.  When you fix the problem click the 'Reload MDA Config' button.\n\n" + e.Message);
                }
            }
        }

        #endregion

        #region Build events

        //BuildEvents
        public void OnBuildBegin(EnvDTE.vsBuildScope scope, EnvDTE.vsBuildAction action)
        {
			if (IsEnabled)
			{
				if (this.IsMavenRunning)
				{
					m_applicationObject.ExecuteCommand("Build.Cancel", string.Empty);
                    m_applicationObject.StatusBar.Highlight(true);
                    m_applicationObject.StatusBar.Text = "Build canceled.  Please stop AndroMDA before building.";
				}
				else if (action != vsBuildAction.vsBuildActionClean)
				{
                    if (!AreGeneratedFilesUpToDate && m_addInSettings.PromptToGenerateOnBuildIfFilesOutOfDate)
					{
						System.Windows.Forms.DialogResult result = System.Windows.Forms.MessageBox.Show("The UML model has been updated since the last time code was generated.\nWould you like to regenerate code from the model?", "Android/VS", System.Windows.Forms.MessageBoxButtons.YesNoCancel, System.Windows.Forms.MessageBoxIcon.Question);
						if (result == System.Windows.Forms.DialogResult.Yes)
						{
							m_restartBuild = true;
							m_applicationObject.ExecuteCommand("Build.Cancel", string.Empty);
							RunMaven();
						}
						if (result == System.Windows.Forms.DialogResult.Cancel)
						{
							m_applicationObject.ExecuteCommand("Build.Cancel", string.Empty);
						}
					}
				}
			}
        }

        public void OnBuildDone(EnvDTE.vsBuildScope Scope, EnvDTE.vsBuildAction Action)
        {
        }

        public void OnBuildProjConfigBegin(string project, string projectConfig, string platform, string solutionConfig)
        {
        }

        public void OnBuildProjConfigDone(string project, string projectConfig, string platform, string solutionConfig, bool success)
        {
        }

        #endregion

		#region Run schema export methods

		public void RunSchemaExport()
		{
			try
			{
				if (IsSchemaExportProjectAvailable && m_schemaExportProject != null)
				{
					if (m_addInSettings.PassSchemaExportArguments)
					{
						Property prop = m_schemaExportProject.Project.ConfigurationManager.ActiveConfiguration.Properties.Item("StartArguments");
						prop.Value = m_addInSettings.SchemaExportCommandLine;
					}

					string solutionName = VSSolutionUtils.GetSolutionName(m_applicationObject.Solution);
					// Activate the solution explorer window
					m_applicationObject.Windows.Item(Constants.vsWindowKindSolutionExplorer).Activate();
					// Grab the UIHierarchy object for the schema export project
					UIHierarchyItem item = ((UIHierarchy)m_applicationObject.ActiveWindow.Object).GetItem(solutionName + "\\" + m_schemaExportProject.Name);
					// Select the project
					item.Select(vsUISelectionType.vsUISelectionTypeSelect);
					// Execute the Debug.StartNewInstance command
					m_applicationObject.ExecuteCommand("ClassViewContextMenus.ClassViewProject.Debug.Startnewinstance", string.Empty);
				}
				else
				{
					AddInUtils.ShowWarning("The schema export project for this solution could not be found.\nPlease ensure the following variables are set correctly in mda\\project.properties\n\nmaven.andromda.schemaexport.available=true\nmaven.andromda.schemaexport.dir=<path to schema export project>");
				}
			}
			catch (Exception e)
			{
				AddInUtils.ShowError(e.Message);
			}

		}

		#endregion

		#region Run maven methods

		public void StopMaven()
        {
			m_mavenProxy.Stop();
			MavenLastRunDateTime = null;
        }

		public void RunMaven(bool forceOnlineMode)
		{
			m_mavenProxy.ForceOnlineMode = forceOnlineMode;
			RunMaven();
		}

        public void RunMaven()
        {
            if (IsEnabled)
            {
				if (m_mavenProxy.OutputWindow == null)
				{
					OutputWindowPane owp = GetOutputWindowPane("AndroMDA");
					m_mavenProxy.OutputWindow = m_applicationObject.Windows.Item(EnvDTE.Constants.vsWindowKindOutput);
					m_mavenProxy.OutputWindowPane = owp;
				}
				m_mavenProxy.Start();
            }
            else
            {
                AddInUtils.ShowError("You must have an MDA-enabled solution loaded to run Maven.");
            }
        }

		void m_mavenProxy_Completed(object sender, EventArgs e)
		{
			VSExternalToolEventArgs args = e as VSExternalToolEventArgs;
            bool restartBuild = m_restartBuild;
			RestartBuild = false;
			switch (args.ExitStatus)
			{
				case VSExternalToolProxy.ToolExitStatus.Success:
					MavenLastRunDateTime = DateTime.Now;
					m_applicationObject.StatusBar.Text = "AndroMDA: Generation successful.";
					m_applicationObject.StatusBar.Highlight(false);
					RefreshGeneratedFiles();
					if (restartBuild)
					{
						m_applicationObject.ExecuteCommand("Build.BuildSolution", string.Empty);
					}
					break;
				case VSExternalToolProxy.ToolExitStatus.Error:					
					m_applicationObject.StatusBar.Text = "AndroMDA: Generation failed";
					if (m_mavenProxy.Status == MavenProxy.MavenStatus.UnsatisfiedDependency && 
						(m_addInSettings.MavenUseOfflineMode || (m_addInSettings.MavenUseCustomCommandLine && m_addInSettings.MavenCustomCommandLine.ToLower().Contains("-o")))
						)
					{
						if (			System.Windows.Forms.MessageBox.Show("It appears that AndroMDA is missing one or more packages it needs to run.  Would you like to download the missing files?", "AndroMDA Generate Failed", System.Windows.Forms.MessageBoxButtons.YesNo, System.Windows.Forms.MessageBoxIcon.Question, System.Windows.Forms.MessageBoxDefaultButton.Button1) == System.Windows.Forms.DialogResult.Yes)
						{
							// Rerun maven without the -o option
							RunMaven(true);
						}												
					}
					break;
			}
		}

        #endregion

        #region Resync methods

        public void RefreshGeneratedFiles()
        {
            if (IsEnabled)
            {
                try
                {                    
                    DoRefreshGeneratedFiles();
                    m_applicationObject.StatusBar.Progress(false, string.Empty, 0, 0);
                    m_applicationObject.StatusBar.Text = "Android/VS: Solution resync complete";
                    }
                catch (Exception e)
                {
                    AddInUtils.ShowError("An error occured while trying to resynchronize the project tree: " + e.Message);
                }
            }
            else
            {
                AddInUtils.ShowError("You must have an MDA-enabled solution loaded to resync generated files.");
            }
        }

        private void DoRefreshGeneratedFiles()
        {
            UpdateResyncProgressBar();

            string[] syncFolders = m_addInSettings.SyncFolderList.Split(new char[] { ';' });
            
            foreach (Project proj in m_applicationObject.Solution.Projects)
            {
                if (proj.Kind == VSSolutionUtils.ProjectKindSolutionFolder || proj.Object == null)
                {
                    // The project is either unavailable or a solution folder
                }
                else if (proj.Kind == VSSolutionUtils.ProjectKindWeb)
                {
                    VsWebSite.VSWebSite webSite = proj.Object as VsWebSite.VSWebSite;
                    webSite.Refresh();
                }
                else
                {
                    UpdateResyncProgressBar(proj.Name);
                    // Search for the existing 
                    foreach (string folder in syncFolders)
                    {
                        string folderCleaned = folder.Replace('/', '\\').Trim('\\');
                        if (folderCleaned.Length > 0)
                        {
                            string folderPath = proj.Properties.Item("FullPath").Value.ToString().Trim('\\') + "\\" + folderCleaned;

                            ProjectItem folderItem = null;
                            foreach (ProjectItem item in proj.ProjectItems)
                            {
                                if (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFolder &&
                                    folderPath == item.Properties.Item("FullPath").Value.ToString().Trim('\\'))
                                {
                                    folderItem = item;
                                }
                            }

                            if (System.IO.Directory.Exists(folderPath))
                            {
                                bool skip = false;

                                // Get the list of files in the filesystem
                                ArrayList filesystemFileList = GetFileListFromFilesystem(folderPath);
                                
                                if (folderItem != null)
                                {
                                    // Get the list of files in the solution tree
                                    ArrayList solutionTreeFileList = GetFileListFromProjectTree(folderItem);
                                    // Generate a hash for the filesystem list
                                    int filesHash = GetArrayListHashCode(filesystemFileList);
                                    // Generate a hash for the solution tree list
                                    int solutionHash = GetArrayListHashCode(solutionTreeFileList);

                                    // If the hash codes are the same, we don't need to do a resync
                                    if (filesHash == solutionHash)
                                    {
                                        UpdateResyncProgressBar("Files have not changed since last resync: " + proj.Name + "/" + folderItem.Name, 1, 1);
                                        skip = true;
                                    }
                                }
                                if (!skip)
                                {
                                    if (folderItem != null)
                                    {
                                        folderItem.Remove();
                                    }
                                    UpdateResyncProgressBar(folderCleaned, 0, filesystemFileList.Count);
                                    //folderItem = proj.ProjectItems.
                                    ((VSLangProj.VSProject)proj.Object).Refresh();
                                    
                                    folderItem = proj.ProjectItems.AddFromDirectory(folderPath);
                                    CollapseProject(proj.Name);
                                    RemoveIgnoredItems(folderItem.ProjectItems, filesystemFileList.Count);
                                }
                            }
                            else
                            {
                                if (folderItem != null)
                                {
                                    folderItem.Remove();
                                }
                            }
                        }
                    }
                }
            }
		}
        
        private void RemoveIgnoredItems(ProjectItems items, int totalItems)
        {
            RemoveIgnoredItems(items, 0, totalItems);
        }

        private int RemoveIgnoredItems(ProjectItems items, int currentItem, int totalItems)
        {
            foreach (ProjectItem item in items)
            {
                if (m_addInSettings.IsInIgnoreList(item.Name) ||
                    (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFile && !System.IO.File.Exists(item.Properties.Item("FullPath").Value.ToString()))
                    )
                {
                    item.Remove();
                }
                else
                {
                    if (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFolder)
                    {
                        currentItem++;
                        currentItem = RemoveIgnoredItems(item.ProjectItems, currentItem, totalItems);
                    }
                    else if (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFile)
                    {
                        UpdateResyncProgressBar(item.Name, currentItem++, totalItems);
                        string file = item.Name;
                        Property prop = item.Properties.Item("BuildAction");
                        if (file.ToLower().Trim().EndsWith(".hbm.xml"))
                        {
                            prop.Value = VSLangProj.prjBuildAction.prjBuildActionEmbeddedResource;
                        }
                        else if (file.EndsWith(".cs"))
                        {
                            prop.Value = VSLangProj.prjBuildAction.prjBuildActionCompile;
                        }
                    }
                }
            }
            return currentItem;
        }

        private bool IsSolutionSynchronizedWithFileSystem()
        {
            string[] syncFolders = m_addInSettings.SyncFolderList.Split(new char[] { ';' });
            foreach (Project proj in m_applicationObject.Solution.Projects)
            {
                if (proj.Kind != VSSolutionUtils.ProjectKindSolutionFolder &&
                    proj.Kind != VSSolutionUtils.ProjectKindWeb)
                {
                    UpdateResyncProgressBar(proj.Name);
                    // Search for the existing 
                    foreach (string folder in syncFolders)
                    {
                        string folderPath = proj.Properties.Item("FullPath").Value.ToString().Trim('\\');
                        folderPath = folderPath + "\\" + folder.Replace('/', '\\').Trim('\\');
                        if (System.IO.Directory.Exists(folderPath))
                        {
                            proj.ProjectItems.AddFromDirectory(folderPath);                            
                        }
                    }
                    foreach (ProjectItem item in proj.ProjectItems)
                    {
                        if (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFolder)
                        {
                            string folderPath = item.Properties.Item("FullPath").Value.ToString();
                            if (m_addInSettings.IsInSyncFoldersList(folderPath))
                            {
                                if (!IsSolutionSynchronizedWithFileSystem(item))
                                    return false;
                            }
                        }
                    }
                }
            }
            return true;
        }

        private bool IsSolutionSynchronizedWithFileSystem(ProjectItem folderProjectItem)
        {
            string folderPath = folderProjectItem.Properties.Item("FullPath").Value.ToString();
            // Get the list of files in the filesystem
            ArrayList filesystemFileList = GetFileListFromFilesystem(folderPath);
			// Get the list of files in the solution tree
            ArrayList solutionTreeFileList = GetFileListFromProjectTree(folderProjectItem);
            // Generate a hash for the filesystem list
            int filesHash = GetArrayListHashCode(filesystemFileList);
            // Generate a hash for the solution tree list
			int solutionHash = GetArrayListHashCode(solutionTreeFileList);

            // If the hash codes are the same, we don't need to do a resync
            return filesHash == solutionHash;
        }

        private int GetArrayListHashCode(ArrayList list)
        {
            int hashCode = 0;
            foreach (object o in list)
            {
                hashCode += o.GetHashCode();
            }
            return hashCode;
        }

        private ArrayList GetFileListFromFilesystem(string directory)
        {
            if (System.IO.Directory.Exists(directory))
            {
                string[] files = System.IO.Directory.GetFiles(directory);
                ArrayList filesList = new ArrayList(files.Length);
                foreach (string file in files)
                {
					bool excludeFile = m_addInSettings.IsInIgnoreList(file);
					// If we should ignore hidden files
					if (m_addInSettings.ResyncIgnoreHiddenFiles)
					{
						// Get the file attributes
						System.IO.FileAttributes attr = System.IO.File.GetAttributes(file);
						// If it's hidden, exclude it
						if ((attr & System.IO.FileAttributes.Hidden) > 0)
						{
							excludeFile = true;
						}
					}
                    if (!excludeFile)
                    {
                        filesList.Add(file);
                    }
                }
                string[] directories = System.IO.Directory.GetDirectories(directory);
                foreach (string dir in directories)
                {
					bool excludeDirectory = m_addInSettings.IsInIgnoreList(dir);
					// If we should ignore hidden files
					if (m_addInSettings.ResyncIgnoreHiddenFiles)
					{
						// Get the file attributes
						System.IO.FileAttributes attr = System.IO.File.GetAttributes(dir);						
						// If it's hidden, exclude it
						if ((attr & System.IO.FileAttributes.Hidden) > 0)
						{
							excludeDirectory = true;
						}
					}
					if (!excludeDirectory)
                    {
                        ArrayList newFiles = GetFileListFromFilesystem(dir);
						filesList.AddRange(newFiles);
                    }
                }
                return filesList;
            }
            else
            {
                return new ArrayList();
            }
        }

		private ArrayList GetFileListFromProjectTree(ProjectItem item)
		{
			ArrayList filesList = new ArrayList();
            if (item == null) return filesList;
			if (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFile)
			{
				for (short i = 1; i <= item.FileCount; i++)
				{
					string filename = item.get_FileNames(i);
					filesList.Add(filename);
				}
			}
			else if (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFolder)
			{
				foreach (ProjectItem childItem in item.ProjectItems)
				{
					filesList.AddRange(GetFileListFromProjectTree(childItem));
				}
			}
			return filesList;
		}

        private void CollapseProject(string projectName)
        {
            UIHierarchy solutionExplorer = (UIHierarchy)m_applicationObject.Windows.Item(Constants.vsext_wk_SProjectWindow).Object;
            foreach (UIHierarchyItem rootUIItems in solutionExplorer.UIHierarchyItems)
            {
                foreach (UIHierarchyItem uiItem in rootUIItems.UIHierarchyItems)
                {
                    if (uiItem.Name == projectName) { CollapseProject(uiItem); break; }
                }
            }
        }

        private void CollapseProject(UIHierarchyItem projectUIItem)
        {
            foreach (UIHierarchyItem uiItem in projectUIItem.UIHierarchyItems)
            {
                uiItem.UIHierarchyItems.Expanded = false;
            }
        }

        #endregion

        #region Utility methods

        private OutputWindowPane GetOutputWindowPane(string panelName)
        {
            DTE2 app = (DTE2)m_applicationObject;
            foreach (OutputWindowPane owp in app.ToolWindows.OutputWindow.OutputWindowPanes)
            {
                if (owp.Name == panelName)
                {
                    return owp;
                }
            }
            return app.ToolWindows.OutputWindow.OutputWindowPanes.Add(panelName);
        }

        private void UpdateResyncProgressBar()
        {
            UpdateResyncProgressBar(string.Empty);
        }

        private void UpdateResyncProgressBar(string detail)
        {
            UpdateResyncProgressBar(detail, 0, 1);
        }

        private void UpdateResyncProgressBar(string detail, int currentProgress, int totalProgress)
        {
            if (detail != string.Empty)
            {
                detail = "(" + detail + ")";
            }
            if (currentProgress > totalProgress) { currentProgress = totalProgress; }
            if (totalProgress == 0) { totalProgress++; }
            m_applicationObject.StatusBar.Progress(true, "Android/VS: Refreshing generated files... " + detail, currentProgress, totalProgress);
        }

        private void UpdateProgressBar(string text, int currentProgress, int totalProgress)
        {
            m_applicationObject.StatusBar.Progress(true, text, currentProgress, totalProgress);
        }

        private static bool IsDateNewer(DateTime d1, DateTime d2)
        {
            d1 = new DateTime(d1.Year, d1.Month, d1.Day, d1.Hour, d1.Minute, d1.Second);
            d2 = new DateTime(d2.Year, d2.Month, d2.Day, d2.Hour, d2.Minute, d2.Second);
            return d1 > d2;
        }

        private static bool MDAConfigFilesExist(Solution currentSolution)
        {
            bool exists = Maven2PropertiesExist(currentSolution);
            if (!exists)
            {
                exists = Maven1PropertiesExist(currentSolution);
            }
            return exists;
        }

        private static bool Maven2PropertiesExist(Solution currentSolution)
        {
            return System.IO.File.Exists(GetMaven2ProjectPropertiesPath(currentSolution));
        }

        private static bool Maven1PropertiesExist(Solution currentSolution)
        {
            return System.IO.File.Exists(GetMaven1ProjectPropertiesPath(currentSolution));
        }

        private static string GetMaven1ProjectPropertiesPath(Solution currentSolution)
        {
            return VSSolutionUtils.GetSolutionPath(currentSolution) + "\\mda\\project.properties";
        }

        private static string GetMaven2ProjectPropertiesPath(Solution currentSolution)
        {
            return VSSolutionUtils.GetSolutionPath(currentSolution) + "\\mda\\pom.xml";
        }

        #endregion

		#region Statistics methods

		public ArrayList GetSolutionStatistics()
		{
			ArrayList stats = new ArrayList();
			if (IsEnabled)
			{
                Hashtable files = new Hashtable();// CollectFiles(m_coreProject.GeneratedPathProjectItem);
                foreach (Project proj in m_applicationObject.Solution.Projects)
                {
                    if (proj.Object != null &&
                        proj.Kind != VSSolutionUtils.ProjectKindSolutionFolder)
                    {
                        foreach (ProjectItem item in proj.ProjectItems)
                        {
                            if (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFolder)
                            {
                                string folderPath = item.Properties.Item("FullPath").Value.ToString();
                                if (m_addInSettings.IsInSyncFoldersList(folderPath))
                                {
                                    MergeHashtable(files, CollectFiles(item));
                                }
                            }
                        }
                    }
                }

				stats.Add(new SolutionStatistic("Files Generated", files.Count.ToString("N0"), SolutionStatistic.Group.FileStatistics));
				stats.Add(new SolutionStatistic("Lines of Code Generated", GetTotalLOC(files).ToString("N0"), SolutionStatistic.Group.FileStatistics));
				stats.Add(new SolutionStatistic("Code Generated", FormatFileSize(GetTotalSize(files)), SolutionStatistic.Group.FileStatistics));

				stats.Add(new SolutionStatistic("Entities", NumberOfItemsByType(files, StatData.FileType.Entity).ToString("N0"), SolutionStatistic.Group.CodeStatistics));
				stats.Add(new SolutionStatistic("Value Objects", NumberOfItemsByType(files, StatData.FileType.ValueObject).ToString("N0"), SolutionStatistic.Group.CodeStatistics));
				stats.Add(new SolutionStatistic("Services", NumberOfItemsByType(files, StatData.FileType.Service).ToString("N0"), SolutionStatistic.Group.CodeStatistics));
				stats.Add(new SolutionStatistic("Enumerations", NumberOfItemsByType(files, StatData.FileType.Enumeration).ToString("N0"), SolutionStatistic.Group.CodeStatistics));
			}
			return stats;
		}

		public static string FormatFileSize(long fileSize)
		{
			if (fileSize < 0) throw new ArgumentOutOfRangeException("fileSize");

			if (fileSize >= 1024 * 1024 * 1024) return string.Format("{0:########0.00} GB", ((double)fileSize) / (1024 * 1024 * 1024));
			else if (fileSize >= 1024 * 1024) return string.Format("{0:####0.00} MB", ((double)fileSize) / (1024 * 1024));
			else if (fileSize >= 1024) return string.Format("{0:####0.00} KB", ((double)fileSize) / 1024);
			else return string.Format("{0} bytes", fileSize);
		}

		private long NumberOfItemsByType(Hashtable list, StatData.FileType type)
		{
			long items = 0;
			foreach (string key in list.Keys)
			{
				StatData data = (StatData)list[key];
				if (data.type == type)
				{
					items++;
				}
			}
			return items;
		}

		private long NumberOfItemsContainingText(Hashtable list, string text, bool caseSensative)
		{
			long count = 0;
			if (!caseSensative) text = text.ToLower();
			foreach(string key in list.Keys)
			{
				string item = key;
				if (!caseSensative)
				{
					item = item.ToLower();
				}
				if (item.Contains(text)) count++;
			}
			return count;
		}

		private long GetTotalSize(Hashtable list)
		{
			long size = 0;
			foreach(string key in list.Keys)
			{
				StatData data = (StatData)list[key];
				size += data.fileSize;
			}
			return size;
		}

		private long GetTotalLOC(Hashtable list)
		{
			long size = 0;
			foreach (string key in list.Keys)
			{
				StatData data = (StatData)list[key];
				size += data.numberOfLines;
			}
			return size;
		}

		private Hashtable CollectFiles(ProjectItem item)
		{
			Hashtable list = new Hashtable();
			if (item == null) return list;
			for(short i = 1; i <= item.FileCount; i++)
			{
				if (item.Kind == EnvDTE.Constants.vsProjectItemKindPhysicalFile)
				{
					string filename = item.get_FileNames(i);
					System.IO.FileInfo info = new System.IO.FileInfo(filename);
					StatData data = new StatData();
					data.type = StatData.FileType.Unknown;
					data.fileSize = info.Length;
					using (System.IO.StreamReader sr = info.OpenText())
					{
						string s = sr.ReadToEnd();
						data.numberOfLines = System.Text.RegularExpressions.Regex.Matches(s, "\n").Count;
						if (s.Contains("HibernateEntity.vsl"))
						{
							data.type = StatData.FileType.Entity;
						}
						else if (s.Contains("ValueObject.vsl"))
						{
							data.type = StatData.FileType.ValueObject;
						}
						else if (s.Contains("NSpringServiceBase.vsl"))
						{
							data.type = StatData.FileType.Service;
						}
						else if (s.Contains("Enumeration.vsl"))
						{
							data.type = StatData.FileType.Enumeration;
						}
						sr.Close();
					}
					list.Add(filename, data);
				}
			}
			foreach(ProjectItem subitem in item.ProjectItems)
			{
				MergeHashtable(list, CollectFiles(subitem));
			}
			return list;
		}

		private void MergeHashtable(Hashtable dst, Hashtable src)
		{
		    foreach (string key in src.Keys)
		    {
			    dst.Add(key, src[key]);
		    }
		}

		private struct StatData
		{
			public enum FileType { Unknown, Entity, ValueObject, Service, Enumeration };

			public StatData(long lines, long size, FileType typeOfFile)
			{
				numberOfLines = lines;
				fileSize = size;
				type = typeOfFile;
			}

			public long numberOfLines;
			public long fileSize;
			public FileType type;
		}

        public class SolutionStatistic
        {
            public enum Group { CodeStatistics, FileStatistics };
            private string m_name;
            private string m_value;
            private Group m_group;

            public Group StatGroup
            {
                get { return m_group; }
                set { m_group = value; }
            }

            public string Value
            {
                get { return m_value; }
                set { m_value = value; }
            }

            public string Name
            {
                get { return m_name; }
                set { m_name = value; }
            }
            public SolutionStatistic(string name, string value, Group group)
            {
                m_name = name;
                m_value = value;
                m_group = group;
            }
        }

		#endregion

	}
}