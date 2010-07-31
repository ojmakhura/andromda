
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;

using Microsoft.VisualStudio.CommandBars;

using EnvDTE;
using EnvDTE80;
using System.Collections;

#endregion

namespace AndroMDA.VS80AddIn
{
    [System.Runtime.InteropServices.ComVisible(true)]
    [System.Runtime.InteropServices.ClassInterface(System.Runtime.InteropServices.ClassInterfaceType.AutoDual)]
    public class AddInSettings
    {

        public const string SETTING_NAME_PREFIX = "ARDV";

        public const int ADDIN_SETTINGS_VERSION = 10;

        private const string WELCOME_WIZARD_RESOURCES_FOLDER_NAME = "Welcome Wizard Files";
        private const string SOLUTION_WIZARD_RESOURCES_FOLDER_NAME = "Solution Wizard Files";

        #region Member variables

        private SettingsManager m_settings = null;
        private DTE m_applicationObject = null;
        private string m_resourcesLocation = string.Empty;

        #endregion

		#region Constructors

		public AddInSettings(DTE applicationObject)
        {
            m_applicationObject = applicationObject;
            m_settings = new SettingsManager(m_applicationObject);
            m_resourcesLocation = System.Reflection.Assembly.GetCallingAssembly().Location;
            m_resourcesLocation = m_resourcesLocation.Replace(System.Reflection.Assembly.GetCallingAssembly().ManifestModule.Name, string.Empty);
            m_resourcesLocation = m_resourcesLocation.TrimEnd('\\');
            if (FirstRun)
            {
                FirstRun = false;
                AddInBuild = ADDIN_SETTINGS_VERSION;
                ResyncIgnoreList = "CVS;.cvsignore;.svn;.svnignore;_svn";
                SyncFolderList = "src;target";
                ResyncIgnoreHiddenFiles = false;
				MavenUseOfflineMode = true;
                MavenCleanFirst = true;
                MavenUseCustomCommandLine = false;
                MavenCustomCommandLine = string.Empty;
                UMLModellerPath = "C:\\Program Files\\MagicDraw UML Community Edition\\bin\\mduml.exe";
				PassSchemaExportArguments = true;
				SchemaExportCommandLine = "-o -e";
            }
            if (AddInBuild < ADDIN_SETTINGS_VERSION)
            {
                SyncFolderList = "src;target";
                AddInBuild = ADDIN_SETTINGS_VERSION;
            }
		}

		#endregion

		#region General settings

        public string ResourcesLocation
        {
            get { return m_resourcesLocation; }
        }

        public string WelcomeWizardResourcesLocation
        {
            get { return m_resourcesLocation + "\\" + WELCOME_WIZARD_RESOURCES_FOLDER_NAME; }
        }

        public string SolutionWizardResourcesLocation
        {
            get { return m_resourcesLocation + "\\" + SOLUTION_WIZARD_RESOURCES_FOLDER_NAME; }
        }

		public bool FirstRun
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "FirstRun", true); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "FirstRun", value); }
        }

        public bool ShowWelcomeWizard
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "ShowWiz", true); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "ShowWiz", value); }
        }

        public bool PromptToGenerateOnBuildIfFilesOutOfDate
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "BdPrmptO", true); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "BdPrmptO", value); }
        }

        public bool PromptToGenerateOnBuildIfSolutionNotSynched
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "BdPrmptS", false); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "BdPrmptS", value); }
        }

        public int AddInBuild
        {
            get 
            { 
                int? build = m_settings.GetInt(SETTING_NAME_PREFIX + "Build");
                if (build.HasValue)
                {
                    return build.Value;
                }
                else
                {
                    return -1;
                }
            }
            set { m_settings.SetInt(SETTING_NAME_PREFIX + "Build", value); }
        }

		#endregion

		#region Resync settings

		public string ResyncIgnoreList
		{
			get { return m_settings[SETTING_NAME_PREFIX + "IgnoreLst"]; }
			set { m_settings[SETTING_NAME_PREFIX + "IgnoreLst"] = value; }
		}

        public string SyncFolderList
        {
            get { return m_settings[SETTING_NAME_PREFIX + "SyncLst"]; }
            set { m_settings[SETTING_NAME_PREFIX + "SyncLst"] = value; }
        }

		public bool ResyncIgnoreHiddenFiles
		{
			get { return m_settings.GetBool(SETTING_NAME_PREFIX + "RIHdnFl", false); }
			set { m_settings.SetBool(SETTING_NAME_PREFIX + "RIHdnFl", value); }
		}

        public bool ShowResyncButton
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "RShBtn", false); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "RShBtn", value); }
        }

		#endregion

		#region External tool settings

		public string UMLModellerPath
        {
            get { return m_settings[SETTING_NAME_PREFIX + "MgcDrawPth"]; }
            set { m_settings[SETTING_NAME_PREFIX + "MgcDrawPth"] = value; }
        }

		public string SchemaExportCommandLine
		{
			get { return m_settings[SETTING_NAME_PREFIX + "ScExCmdLn"]; }
			set { m_settings[SETTING_NAME_PREFIX + "ScExCmdLn"] = value; }
		}

		public bool PassSchemaExportArguments
		{
			get { return m_settings.GetBool(SETTING_NAME_PREFIX + "ScExPass", true); }
			set { m_settings.SetBool(SETTING_NAME_PREFIX + "ScExPass", value); }
		}
		
		public bool AutoMakeModelFileWritable
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "MdlWritbl", false); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "MdlWritbl", value); }
        }

        #endregion

        #region Maven settings

        public bool MavenUseOfflineMode
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "MavenOffln"); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "MavenOffln", value); }
        }

        public bool MavenCleanFirst
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "MavenClean"); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "MavenClean", value); }
        }

        public bool MavenUseCustomCommandLine
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "MavenUseCC"); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "MavenUseCC", value); }
        }

        public string MavenCustomCommandLine
        {
            get { return m_settings[SETTING_NAME_PREFIX + "MavenCCLN"]; }
            set { m_settings[SETTING_NAME_PREFIX + "MavenCCLN"] = value; }
        }

        #endregion

        #region Tool bar settings

        public bool ToolBarVisible
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "TBVisible", true); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "TBVisible", value); }
        }

        public MsoBarPosition ToolBarPosition
        {
            get 
            { 
                string pos = m_settings[SETTING_NAME_PREFIX + "TBPos"];
                if (pos == string.Empty)
                {
                    return MsoBarPosition.msoBarTop;
                }
                else
                {
                    return (MsoBarPosition)Enum.Parse(typeof(MsoBarPosition), pos);
                }
            }
            set 
            { 
                m_settings[SETTING_NAME_PREFIX + "TBPos"] = value.ToString(); 
            }
        }

        public int? ToolBarTop
        {
            get { return m_settings.GetInt(SETTING_NAME_PREFIX + "TBTop"); }
            set { m_settings.SetInt(SETTING_NAME_PREFIX + "TBTop", value); }
        }

        public int? ToolBarLeft
        {
            get { return m_settings.GetInt(SETTING_NAME_PREFIX + "TBLeft"); }
            set { m_settings.SetInt(SETTING_NAME_PREFIX + "TBLeft", value); }
        }

        public int? ToolBarRowIndex
        {
            get { return m_settings.GetInt(SETTING_NAME_PREFIX + "TBRowIdx"); }
            set { m_settings.SetInt(SETTING_NAME_PREFIX + "TBRowIdx", value); }
        }

        #endregion

        #region Tool bar button settings

        public bool ShowAboutButton
        {
            get { return m_settings.GetBool("ADMAShowAbout", true); }
            set { m_settings.SetBool("ADMAShowAbout", value); }
        }

        public bool ShowOpenModelButton
        {
            get { return m_settings.GetBool("ADMAShowOMdl", true); }
            set { m_settings.SetBool("ADMAShowOMdl", value); }
        }

		public bool ShowSchemaExportButton
		{
			get { return m_settings.GetBool("ADMAShowSCEX", true); }
			set { m_settings.SetBool("ADMAShowSCEX", value); }
		}

		public bool HideSchemaExportButtonIfProjectUnavailable
		{
			get { return m_settings.GetBool("ADMAHideSCEX", false); }
			set { m_settings.SetBool("ADMAHideSCEX", value); }
		}

        #endregion

        #region Context menu settings

        public bool ShowPropertyMapper
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "PrpMapE", true); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "PrpMapE", value); }
        }

        public bool ShowInsertEntityConversion
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "EntConS", true); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "EntConS", value); }
        }

        public bool OnlyEnableCodeGenOnConversionMethods
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "CdeGenE", true); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "CdeGenE", value); }
        }

        public bool OnlyEnableCodeGenInDaoImpl
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "CdeGenD", true); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "CdeGenD", value); }
        }

        #endregion

        #region Property mapper settings

        public bool PropertyMapperAutoMapOnOpen
        {
            get { return m_settings.GetBool(SETTING_NAME_PREFIX + "PMAMOO", false); }
            set { m_settings.SetBool(SETTING_NAME_PREFIX + "PMAMOO", value); }
        }

        #endregion

        #region Methods

        public void EraseAllSettings()
        {
            EraseAllSettings(SETTING_NAME_PREFIX);
        }

        public void EraseAllSettings(string prefix)
        {
            IList variables = (IList)m_applicationObject.Globals.VariableNames;
            foreach (string varname in variables)
            {
                if (varname.StartsWith(prefix))
                {
                    m_applicationObject.Globals[varname] = string.Empty;
                    m_applicationObject.Globals.set_VariablePersists(varname, false);
                }
            }
        }

        public bool IsInIgnoreList(string fileToTest)
        {
			return IsInIgnoreList(fileToTest, true);
        }

        public bool IsInIgnoreList(string fileToTest, bool stripPath)
        {
            // Get the list of files to ignore
            string[] filesIgnored = ResyncIgnoreList.Split(new char[] { ';' });
			
			// Strip the path of the input file
			if (stripPath)
			{
				fileToTest = FileUtils.GetFilename(fileToTest);
			}
			// Make the file name lowercase and trim space off the ends
			fileToTest = fileToTest.ToLower().Trim();

			// Iterate through the ignored files
			foreach (string ignoredFile in filesIgnored)
            {
				string ignoredFileCleaned = ignoredFile.ToLower().Trim();
				// TODO: Support regular expressions
				if (ignoredFileCleaned == fileToTest)
                {
                    return true;
                }
            }
            return false;
		}

        public bool IsInSyncFoldersList(string folderPath)
        {
            return IsInSyncFoldersList(folderPath, true);
        }

        public bool IsInSyncFoldersList(string folderPath, bool stripPath)
        {
            // Get the list of files to ignore
            string[] syncFolders = SyncFolderList.Split(new char[] { ';' });

            // Make the file name lowercase and trim space off the ends
            folderPath = folderPath.Trim().ToLower().Replace('\\', '/').Trim('/');

            // Iterate through the ignored files
            foreach (string folder in syncFolders)
            {
                string folderCleaned = folder.Trim().ToLower().Replace('\\', '/').Trim('/');
                if (folderCleaned.Length > 0)
                {
                    if (folderPath.EndsWith(folderCleaned))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
		#endregion

	}
}
