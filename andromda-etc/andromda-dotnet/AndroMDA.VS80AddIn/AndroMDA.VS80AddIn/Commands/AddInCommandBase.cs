
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;

using Microsoft.VisualStudio.CommandBars;

using EnvDTE;
using EnvDTE80;

#endregion

namespace AndroMDA.VS80AddIn
{
    public abstract class AddInCommandBase
    {

        #region Enumerations

        public enum VisibilityType { AlwaysEnabled, AlwaysDisabled, EnabledWhenMdaInactive, EnabledWhenMdaActiveButDisabled, EnabledWhenMdaActive, EnabledWhenMdaActiveAndMavenNotRunning, EnabledWhenMdaActiveAndMavenRunning }

        #endregion

        #region Member variables

        protected bool m_isStartOfGroup = false;
		protected bool m_autoAddToToolbar = true;
		protected bool m_autoAddToCodeContextMenu = false;
        protected string m_name = string.Empty;
        protected string m_fullName = string.Empty;
        protected string m_buttonText = string.Empty;
        protected string m_toolTip = string.Empty;
        protected int m_bitmapNumber = 0;
        protected bool m_commandIsOnCodeContextMenu = false;

        protected DTE m_application = null;
        protected AddIn m_addIn = null;
        protected MDASolutionManager m_solutionManager = null;
        protected AddInSettings m_addInSettings = null;

        protected Command m_command = null;
        protected CommandBarButton m_commandBarButton = null;

        protected VisibilityType m_visibilityType = VisibilityType.AlwaysEnabled;

		protected vsCommandStatus m_lastStatus = vsCommandStatus.vsCommandStatusUnsupported;

        #endregion

        #region Properties

        public string FullName
        {
            get { return m_fullName;  }
        }

        public string Name
        {
            get { return m_name; }
        }

		public CommandBarControl CommandBarControl
		{
			get { return m_commandBarButton; }
		}

		public virtual bool IsToolbarButtonVisible
		{
			get
			{
				bool enabled = (m_lastStatus & vsCommandStatus.vsCommandStatusEnabled) > 0;
				bool supported = (m_lastStatus & vsCommandStatus.vsCommandStatusSupported) > 0;
				return enabled && supported;
			}
		}

		public bool AutoAddToToolbar
		{
			get { return m_autoAddToToolbar; }
			set { m_autoAddToToolbar = value; }
		}

		public bool AutoAddToCodeWindowContextMenu
		{
			get { return m_autoAddToCodeContextMenu; }
			set { m_autoAddToCodeContextMenu = value; }
		}

		#endregion

		public AddInCommandBase()
		{
		}

        public AddInCommandBase(string name, string buttonText, string toolTip, int bitmapNumber, VisibilityType commandType)
        {
            Init(name, buttonText, toolTip, bitmapNumber, commandType);
        }

        public void Init(string name, string buttonText, string toolTip, int bitmapNumber, VisibilityType commandType)
        {
            m_name = name;
            m_buttonText = buttonText;
            m_toolTip = toolTip;
            m_bitmapNumber = bitmapNumber;
            m_visibilityType = commandType;
            m_fullName = "AndroMDA.VS80AddIn.Connect." + name;
        }

        public void SetContext(DTE application, AddIn addIn, MDASolutionManager solutionManager, AddInSettings addInSettings)
        {
            m_application = application;
            m_addIn = addIn;
            m_solutionManager = solutionManager;
            m_addInSettings = addInSettings;
        }

		public void InitToolbars(CommandBar toolBar)
		{
			if (this.AutoAddToToolbar)
			{
				AddToToolbar(toolBar);
			}
			if (this.AutoAddToCodeWindowContextMenu)
			{
				AddToCodeWindowContextMenu();
			}
		}

		public void AddToCodeWindowContextMenu()
		{
			if (m_command != null)
			{
				_CommandBars cmdBars = (_CommandBars)m_application.CommandBars;
				CommandBar cmdBar = cmdBars["Code Window"];
				foreach(CommandBarControl c in cmdBar.Controls)
				{
					if (c.Caption == m_buttonText)
					{
						return;
					}
				}
				m_command.AddControl(cmdBar, 1);
                m_commandIsOnCodeContextMenu = true;
			}
		}

		public void AddToToolbar(CommandBar toolbar)
        {
            AddToToolbar(toolbar, toolbar.Controls.Count + 1);
        }

        public virtual void AddToToolbar(CommandBar toolbar, int position)
        {
            if (m_command != null)
            {
                m_commandBarButton = (CommandBarButton)m_command.AddControl(toolbar, position);
				m_commandBarButton.TooltipText = m_toolTip;
				if (m_bitmapNumber == -1)
				{
					m_commandBarButton.Style = MsoButtonStyle.msoButtonCaption;
				}
				else
				{
					m_commandBarButton.Style = MsoButtonStyle.msoButtonIconAndCaption;
				}
                if (m_isStartOfGroup)
                {
                    m_commandBarButton.BeginGroup = true;
                }
            }
        }

        public void RegisterCommand()
        {
            Commands2 commands = (Commands2)m_application.Commands;
            object[] context_GUIDS = new object[0];
            try
            {
                m_command = commands.Item(m_fullName, 0);
            }
            catch
            {
                m_command = commands.AddNamedCommand2(m_addIn, m_name, m_buttonText, m_toolTip, true, m_bitmapNumber, ref context_GUIDS, (int)vsCommandStatus.vsCommandStatusSupported + (int)vsCommandStatus.vsCommandStatusEnabled, (int)vsCommandStyle.vsCommandStylePictAndText, vsCommandControlType.vsCommandControlTypeButton);
            }
        }

        public void UnregisterCommand()
        {
            if (m_command != null)
            {
                if (m_commandIsOnCodeContextMenu)
                {
                    _CommandBars cmdBars = (_CommandBars)m_application.CommandBars;
                    CommandBar cmdBar = cmdBars["Code Window"];
                    foreach (CommandBarControl c in cmdBar.Controls)
                    {
                        if (c.Caption == m_buttonText)
                        {
                            //return;
                            // Do something
                        }
                    }
                }
                m_command.Delete();
            }
        }

        public abstract void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut);

        /// <summary>
        /// Called to query the status of the buttons.
        /// </summary>
        /// <param name="commandName">Name of the command.</param>
        /// <param name="neededText">The needed text.</param>
        /// <param name="status">The status.</param>
        /// <param name="commandText">The command text.</param>
        public virtual void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
        {
            if (commandName == m_fullName && neededText == vsCommandStatusTextWanted.vsCommandStatusTextWantedNone)
            {

                switch (m_visibilityType)
                {

					case VisibilityType.AlwaysDisabled:
						status = (vsCommandStatus)vsCommandStatus.vsCommandStatusSupported;
						break;
					case VisibilityType.AlwaysEnabled:
                        status = (vsCommandStatus)vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusEnabled;
                        break;
                    case VisibilityType.EnabledWhenMdaActiveButDisabled:
						if (!m_solutionManager.IsEnabled && m_solutionManager.IsSolutionUsingMDA && m_application.Solution.IsOpen)
						{
							status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusEnabled;
						}
						else
						{
							status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusInvisible;
						}
						break;
                    case VisibilityType.EnabledWhenMdaInactive:
						if (!m_solutionManager.IsEnabled && !m_solutionManager.IsSolutionUsingMDA && m_application.Solution.IsOpen)
                        {
                            status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusEnabled;
                        }
                        else
                        {
                            status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusInvisible;
                        }
                        break;
                    
                    case VisibilityType.EnabledWhenMdaActive:
                        if (m_solutionManager.IsEnabled && m_application.Solution.IsOpen)
                        {
                            status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusEnabled;
                        }
                        else
                        {
                            status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusInvisible;
                        }
                        break;
                    
                    case VisibilityType.EnabledWhenMdaActiveAndMavenNotRunning:
                        if (m_solutionManager.IsEnabled && m_application.Solution.IsOpen && !m_solutionManager.IsMavenRunning)
                        {
                            status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusEnabled;
                        }
                        else if (!m_solutionManager.IsEnabled)
                        {
                            status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusInvisible;
                        }
                        else
                        {
                            status = vsCommandStatus.vsCommandStatusSupported;
                        }
                        break;
                    
                    case VisibilityType.EnabledWhenMdaActiveAndMavenRunning:
                        if (m_solutionManager.IsEnabled && m_application.Solution.IsOpen && m_solutionManager.IsMavenRunning)
                        {
                            status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusEnabled;
                        }
                        else if (!m_solutionManager.IsEnabled)
                        {
                            status = vsCommandStatus.vsCommandStatusSupported | vsCommandStatus.vsCommandStatusInvisible;
                        }
                        else
                        {
                            status = vsCommandStatus.vsCommandStatusSupported;
                        }
                        break;
                }
				m_lastStatus = status;
			}
        }

    }
}
