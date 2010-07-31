
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;
using System.Text;
using EnvDTE;
using Extensibility;
using System.Windows.Forms;
using System.IO;
using Microsoft.VisualStudio.CommandBars;

using Velocity = NVelocity.App.Velocity;
using VelocityContext = NVelocity.VelocityContext;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class VS80AddIn
    {

		public const string ADD_IN_VERSION = "1.7";

        #region Constants

        private static string TOOL_BAR_NAME = "AndroMDA";

        #endregion

        #region Member variables

        private bool m_connected = false;

        private bool m_debugMode = false;
        private bool m_isBuilding = false;

        private DTE m_applicationObject = null;
        private AddIn m_addInInstance = null;

        private EnvDTE.SolutionEvents m_solutionEvents = null;
        private EnvDTE.BuildEvents m_buildEvents = null;
        private EnvDTE.DTEEvents m_dteEvents = null;
        private EnvDTE.DebuggerEvents m_debuggerEvents = null;

        private MDASolutionManager m_solutionManager = null;
        private AddInSettings m_addInSettings = null;

        private CommandBar m_toolBar = null;

        private ArrayList m_commands = null;

		private AndroMDALogoCommand m_logoCommand = null;
        #endregion

        #region Properties

        public bool IsAvailable
        {
            get { return !m_isBuilding && !m_debugMode; }
        }

        public bool IsConnected
        {
            get { return m_connected; }
        }

        #endregion

        public VS80AddIn()
        {
            m_commands = new ArrayList();
        }

        public void CreateCommands()
        {
            m_commands.Clear();

            // Add commands to the add-in
			m_logoCommand = new AndroMDALogoCommand();
			m_commands.Add(m_logoCommand);
			m_commands.Add(new ReloadMDAConfigCommand());
			m_commands.Add(new RunSolutionWizardCommand());
            m_commands.Add(new RunMavenCommand());
            m_commands.Add(new ResyncCommand());
            m_commands.Add(new StopCommand());
			m_commands.Add(new RunSchemaExportCommand());
			m_commands.Add(new OpenModelCommand());
            m_commands.Add(new AboutCommand());
			m_commands.Add(new CreateEntityVOConvertorCommand());
            m_commands.Add(new PropertyMapperCommand());
            // Set the context for each command
			foreach (AddInCommandBase cmd in m_commands)
			{
				cmd.SetContext(m_applicationObject, m_addInInstance, m_solutionManager, m_addInSettings);
			}

        }

        /// <summary>
        /// Called when the add-in is loaded
        /// </summary>
        /// <param name="application">The application.</param>
        /// <param name="connectMode">The connect mode.</param>
        /// <param name="addInInst">The add in inst.</param>
        /// <param name="custom">The custom.</param>
        public void OnConnection(object application, ext_ConnectMode connectMode, object addInInst, ref Array custom)
        {
            try
            {
                if (connectMode == ext_ConnectMode.ext_cm_UISetup || connectMode == ext_ConnectMode.ext_cm_Solution)
                {
                    return;
                }

                // If the Add-In is already connected return
                if (m_connected)
                {
                    return;
                }

                m_applicationObject = (DTE)application;
                m_addInInstance = (AddIn)addInInst;

                // Don't load the add-in if the following parameters were passed to the app                
                string command_line = m_applicationObject.CommandLineArguments.ToLower();
                foreach (string sw in new string[] { "/build", "/rebuild", "/clean", "/deploy" }) 
                { 
                    if (command_line.IndexOf(sw) != -1) 
                        return; 
                }

                m_addInSettings = new AddInSettings(m_applicationObject);

                if (m_addInSettings.ShowWelcomeWizard)
                {
                    Dialogs.WelcomeWizard welcome = new Dialogs.WelcomeWizard(m_addInSettings);
                    welcome.ShowDialog();
                    m_addInSettings.ShowWelcomeWizard = welcome.ShowWizardAgain;
                }

				m_solutionManager = new MDASolutionManager(m_applicationObject, m_addInSettings);
				BindEvents();
				CreateCommands();
				RegisterCommands();
                RegisterToolbar();
                Velocity.Init();
				m_connected = true;
            }
            catch (Exception e)
            {
                AddInUtils.ShowError("An exception occured while trying to instantiate the add-in: " + e.ToString());
            }
        }

        public void OnDisconnection(ext_DisconnectMode disconnectMode, ref Array custom)
        {
            switch (disconnectMode)
            {
                case ext_DisconnectMode.ext_dm_HostShutdown:
                case ext_DisconnectMode.ext_dm_UserClosed:
                    break;
                case ext_DisconnectMode.ext_dm_SolutionClosed:
                case ext_DisconnectMode.ext_dm_UISetupComplete:
                    return;
            }

            // If the add-in is not active, do not disconnect
            if (!m_connected)
            {
                return;
            }

            try
            {
                // Stop maven if it is runing
                if (m_solutionManager.IsMavenRunning)
                {
                    m_solutionManager.StopMaven();
                }
                // Save the position/state of the toolbar
                SaveToolbarSettings();
                UnregisterCommands();
                UnbindEvents();
                m_solutionManager = null;
            }
            catch (Exception e)
            {
                AddInUtils.ShowError("An exception occured in OnDisconnect(): " + e.ToString());
            }
            finally
            {
                m_connected = false;
            }

        }

        public void RegisterCommands()
        {
            foreach (AddInCommandBase cmd in m_commands)
            {
                cmd.RegisterCommand();
            }
        }

        public void UnregisterCommands()
        {
            foreach (AddInCommandBase cmd in m_commands)
            {
                cmd.UnregisterCommand();
            }
        }


        public void RegisterToolbar()
        {
            m_toolBar = null;

            // Remove the toolbar if it exists
            try
            {
                CommandBars bars = (CommandBars)m_applicationObject.CommandBars;
                m_toolBar = bars[TOOL_BAR_NAME];		// it seems this will never execute
                if (m_toolBar != null)
                {
                    m_toolBar.Delete();
                    m_toolBar = null;
                }
            } catch { }

            // Create the toolbar
            if (m_toolBar == null)
            {
                CommandBars bars = (CommandBars)m_applicationObject.CommandBars;
                m_toolBar = bars.Add(TOOL_BAR_NAME, MsoBarPosition.msoBarTop, System.Type.Missing, true);
                
                // Load saved toolbar settings
                m_toolBar.Visible = m_addInSettings.ToolBarVisible;
                m_toolBar.Position = m_addInSettings.ToolBarPosition;
                if (m_addInSettings.ToolBarTop.HasValue)
                {
                    m_toolBar.Top = m_addInSettings.ToolBarTop.Value;
                }
                if (m_addInSettings.ToolBarLeft.HasValue)
                {
                    m_toolBar.Left = m_addInSettings.ToolBarLeft.Value;
                }
                if (m_addInSettings.ToolBarRowIndex.HasValue)
                {
                    m_toolBar.RowIndex = m_addInSettings.ToolBarRowIndex.Value;
                }
            }

            // Add the buttons to the toolbar
            foreach (AddInCommandBase cmd in m_commands)
            {
				cmd.InitToolbars(m_toolBar);
            }

        }

        private void SaveToolbarSettings()
        {
            m_addInSettings.ToolBarVisible = m_toolBar.Visible;
            m_addInSettings.ToolBarPosition = m_toolBar.Position;
            m_addInSettings.ToolBarTop = m_toolBar.Top;
            m_addInSettings.ToolBarLeft = m_toolBar.Left;
            m_addInSettings.ToolBarRowIndex = m_toolBar.RowIndex;
        }

        public void BindEvents()
        {
            EnvDTE.Events events = m_applicationObject.Events;

            m_solutionEvents = (EnvDTE.SolutionEvents)events.SolutionEvents;
            m_buildEvents = (EnvDTE.BuildEvents)events.BuildEvents;
            m_dteEvents = (EnvDTE.DTEEvents)events.DTEEvents;
            m_debuggerEvents = (EnvDTE.DebuggerEvents)events.DebuggerEvents;

            m_solutionEvents.AfterClosing += new _dispSolutionEvents_AfterClosingEventHandler(this.AfterClosing);
            m_solutionEvents.BeforeClosing += new _dispSolutionEvents_BeforeClosingEventHandler(this.BeforeClosing);
            m_solutionEvents.Opened += new _dispSolutionEvents_OpenedEventHandler(this.Opened);
            m_solutionEvents.ProjectAdded += new _dispSolutionEvents_ProjectAddedEventHandler(this.ProjectAdded);
            m_solutionEvents.ProjectRemoved += new _dispSolutionEvents_ProjectRemovedEventHandler(this.ProjectRemoved);
            m_solutionEvents.ProjectRenamed += new _dispSolutionEvents_ProjectRenamedEventHandler(this.ProjectRenamed);
            m_solutionEvents.QueryCloseSolution += new _dispSolutionEvents_QueryCloseSolutionEventHandler(this.QueryCloseSolution);
            m_solutionEvents.Renamed += new _dispSolutionEvents_RenamedEventHandler(this.Renamed);

            m_buildEvents.OnBuildBegin += new _dispBuildEvents_OnBuildBeginEventHandler(this.OnBuildBegin);
            m_buildEvents.OnBuildDone += new _dispBuildEvents_OnBuildDoneEventHandler(this.OnBuildDone);
            m_buildEvents.OnBuildProjConfigBegin += new _dispBuildEvents_OnBuildProjConfigBeginEventHandler(this.OnBuildProjConfigBegin);
            m_buildEvents.OnBuildProjConfigDone += new _dispBuildEvents_OnBuildProjConfigDoneEventHandler(this.OnBuildProjConfigDone);

            m_dteEvents.ModeChanged += new _dispDTEEvents_ModeChangedEventHandler(this.DTEModeChanged);

            m_debuggerEvents.OnEnterRunMode += new _dispDebuggerEvents_OnEnterRunModeEventHandler(DebuggerOnEnterRunMode);
            m_debuggerEvents.OnEnterDesignMode += new _dispDebuggerEvents_OnEnterDesignModeEventHandler(DebuggerOnEnterDesignMode);
        }

        public void UnbindEvents()
        {
            if (m_solutionEvents != null)
            {
                m_solutionEvents.AfterClosing -= new _dispSolutionEvents_AfterClosingEventHandler(this.AfterClosing);
                m_solutionEvents.BeforeClosing -= new _dispSolutionEvents_BeforeClosingEventHandler(this.BeforeClosing);
                m_solutionEvents.Opened -= new _dispSolutionEvents_OpenedEventHandler(this.Opened);
                m_solutionEvents.ProjectAdded -= new _dispSolutionEvents_ProjectAddedEventHandler(this.ProjectAdded);
                m_solutionEvents.ProjectRemoved -= new _dispSolutionEvents_ProjectRemovedEventHandler(this.ProjectRemoved);
                m_solutionEvents.ProjectRenamed -= new _dispSolutionEvents_ProjectRenamedEventHandler(this.ProjectRenamed);
                m_solutionEvents.QueryCloseSolution -= new _dispSolutionEvents_QueryCloseSolutionEventHandler(this.QueryCloseSolution);
                m_solutionEvents.Renamed -= new _dispSolutionEvents_RenamedEventHandler(this.Renamed);
                m_solutionEvents = null;
            }
            if (m_buildEvents != null)
            {
                m_buildEvents.OnBuildBegin -= new _dispBuildEvents_OnBuildBeginEventHandler(this.OnBuildBegin);
                m_buildEvents.OnBuildDone -= new _dispBuildEvents_OnBuildDoneEventHandler(this.OnBuildDone);
                m_buildEvents.OnBuildProjConfigBegin -= new _dispBuildEvents_OnBuildProjConfigBeginEventHandler(this.OnBuildProjConfigBegin);
                m_buildEvents.OnBuildProjConfigDone -= new _dispBuildEvents_OnBuildProjConfigDoneEventHandler(this.OnBuildProjConfigDone);
                m_buildEvents = null;
            }
            if (m_dteEvents != null)
            {
                m_dteEvents.ModeChanged -= new _dispDTEEvents_ModeChangedEventHandler(this.DTEModeChanged);
                m_dteEvents = null;
            }
            if (m_debuggerEvents != null)
            {
                m_debuggerEvents.OnEnterRunMode -= new _dispDebuggerEvents_OnEnterRunModeEventHandler(DebuggerOnEnterRunMode);
                m_debuggerEvents.OnEnterDesignMode -= new _dispDebuggerEvents_OnEnterDesignModeEventHandler(DebuggerOnEnterDesignMode);
                m_debuggerEvents = null;
            }
        }

		public AddInCommandBase GetCommand(string commandName)
		{
			foreach (AddInCommandBase cmd in m_commands)
			{
				if (cmd.FullName == commandName)
				{
					return cmd;
				}
			}
			return null;
		}

        public bool AreAnyToolbarButtonsVisible()
        {
            foreach (AddInCommandBase cmd in m_commands)
            {
				if (cmd.IsToolbarButtonVisible)
				{
					return true;
				}
			}
			return false;
        }

		public void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
        {
            if (m_connected && neededText == vsCommandStatusTextWanted.vsCommandStatusTextWantedNone)
            {
                AddInCommandBase cmd = GetCommand(commandName);
                if (cmd != null)
                {
					m_logoCommand.Visible = !AreAnyToolbarButtonsVisible();
					cmd.QueryStatus(commandName, neededText, ref status, ref commandText);
                    if (!IsAvailable)
                    {
                        if ((status & vsCommandStatus.vsCommandStatusEnabled) > 0)
                        {
                            status -= vsCommandStatus.vsCommandStatusEnabled;
                        }
                    }
                }
                else
                {
                    status = vsCommandStatus.vsCommandStatusUnsupported | vsCommandStatus.vsCommandStatusInvisible;
                }
            }
        }

        public void Exec(string commandName, vsCommandExecOption executeOption, ref object varIn, ref object varOut, ref bool handled)
        {
            if (m_connected && IsAvailable && executeOption == vsCommandExecOption.vsCommandExecOptionDoDefault)
            {
                AddInCommandBase cmd = GetCommand(commandName);
                if (cmd != null)
                {
                    cmd.Exec(executeOption, ref varIn, ref varOut);
                    handled = true;
                }
            }
        }

        void DebuggerOnEnterDesignMode(dbgEventReason Reason)
        {
            m_debugMode = false;
        }

        void DebuggerOnEnterRunMode(dbgEventReason Reason)
        {
            m_debugMode = true;
        }

        //DTEEvents
        public void DTEModeChanged(vsIDEMode ideMode)
        {
            m_debugMode = (ideMode == vsIDEMode.vsIDEModeDebug);
        }

        //SolutionEvents
        public void AfterClosing()
        {
        }

        public void BeforeClosing()
        {
        }

        public void Opened()
        {
            if (m_solutionManager != null)
            {
                m_solutionManager.InitializeSolution();
            }
        }

        public void ProjectAdded(EnvDTE.Project project)
        {
        }

        public void ProjectRemoved(EnvDTE.Project project)
        {
        }

        public void ProjectRenamed(EnvDTE.Project project, string oldName)
        {
        }

        public void QueryCloseSolution(ref bool cancel)
        {
        }

        public void Renamed(string oldName)
        {
        }

        //BuildEvents
        public void OnBuildBegin(EnvDTE.vsBuildScope scope, EnvDTE.vsBuildAction action)
        {
            m_isBuilding = true;
            if (m_solutionManager != null)
            {
                m_solutionManager.OnBuildBegin(scope, action);
            }
        }

        public void OnBuildDone(EnvDTE.vsBuildScope scope, EnvDTE.vsBuildAction action)
        {
            if (m_solutionManager != null)
            {
                m_solutionManager.OnBuildDone(scope, action);
            }
            m_isBuilding = false;
        }

        public void OnBuildProjConfigBegin(string project, string projectConfig, string platform, string solutionConfig)
        {
            if (m_solutionManager != null)
            {
                m_solutionManager.OnBuildProjConfigBegin(project, projectConfig, platform, solutionConfig);
            }
        }

        public void OnBuildProjConfigDone(string project, string projectConfig, string platform, string solutionConfig, bool success)
        {
            if (m_solutionManager != null)
            {
                m_solutionManager.OnBuildProjConfigDone(project, projectConfig, platform, solutionConfig, success);
            }
        }
 

    }
}
