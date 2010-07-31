
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using EnvDTE;
using EnvDTE80;

#endregion

namespace AndroMDA.VS80AddIn.Dialogs
{
    public partial class GeneralOptionsPage : UserControl, EnvDTE.IDTToolsOptionsPage
	{

		#region Member variables

		static MDAOptionPageProperties m_properties = new MDAOptionPageProperties();
        static AddInSettings m_settings = null;

		#endregion

        #region Check Options

        // Groups
        private static ListViewGroup m_generalGroup = new ListViewGroup("General");
        private static ListViewGroup m_contextGroup = new ListViewGroup("Context Menus");
        private static ListViewGroup m_toolbarGroup= new ListViewGroup("Toolbar");

        // Options
        private ListViewItem m_showWelcomeDialog = new ListViewItem("Show welcome dialog when Visual Studio starts", m_generalGroup);
        private ListViewItem m_promptOnBuildOutOfDate = new ListViewItem("Prompt to generate on build if generated code is out of date", m_generalGroup);
        private ListViewItem m_promptOnBuildSynched = new ListViewItem("Prompt to generate on build if solution explorer is different than filesystem", m_generalGroup);
        
        private ListViewItem m_showAboutButton = new ListViewItem("Show about button on toolbar", m_toolbarGroup);
        private ListViewItem m_showSchemaExportButton = new ListViewItem("Show schema export button on toolbar", m_toolbarGroup);
        private ListViewItem m_hideSchemaExportButton = new ListViewItem("Hide schema export button if export project unavailable", m_toolbarGroup);
        private ListViewItem m_showOpenModelButton = new ListViewItem("Show open model button on toolbar", m_toolbarGroup);
        private ListViewItem m_showResyncButton = new ListViewItem("Show resync solution explorer button on toolbar", m_toolbarGroup);

        private ListViewItem m_showPropertyMapper = new ListViewItem("Show property mapper on code context menu", m_contextGroup);
        private ListViewItem m_showInsertConversion = new ListViewItem("Show insert conversion code on code context menu", m_contextGroup);
        private ListViewItem m_enableCodeGenOnConversionMethods = new ListViewItem("Restrict code gen to methods starting with 'To' or ending with 'ToEntity'", m_contextGroup);
        private ListViewItem m_enableCodeGenInDaoImpl = new ListViewItem("Restrict code gen to classes with 'DaoImpl' in their names", m_contextGroup);
        private ListViewItem m_autoMapOnOpenMapper = new ListViewItem("Auto map when property mapper is opened", m_contextGroup);

        #endregion

        public GeneralOptionsPage()
        {
            InitializeComponent();
        }

        #region IDTToolsOptionsPage Members

        public void OnAfterCreated(DTE DTEObject)
        {
            m_settings = new AddInSettings(DTEObject);
            
            m_showAboutButton.Checked = m_settings.ShowAboutButton;;
            m_showSchemaExportButton.Checked = m_settings.ShowOpenModelButton;
            m_hideSchemaExportButton.Checked = m_settings.ShowSchemaExportButton;
            m_showOpenModelButton.Checked = m_settings.HideSchemaExportButtonIfProjectUnavailable;
            m_showResyncButton.Checked = m_settings.ShowResyncButton;
            
            m_promptOnBuildOutOfDate.Checked = m_settings.PromptToGenerateOnBuildIfFilesOutOfDate;
            m_promptOnBuildSynched.Checked = m_settings.PromptToGenerateOnBuildIfSolutionNotSynched;
            m_showWelcomeDialog.Checked = m_settings.ShowWelcomeWizard;

            m_enableCodeGenOnConversionMethods.Checked = m_settings.OnlyEnableCodeGenOnConversionMethods;
            m_enableCodeGenInDaoImpl.Checked = m_settings.OnlyEnableCodeGenInDaoImpl;
            m_showInsertConversion.Checked = m_settings.ShowInsertEntityConversion;
            m_showPropertyMapper.Checked = m_settings.ShowPropertyMapper;
            m_autoMapOnOpenMapper.Checked = m_settings.PropertyMapperAutoMapOnOpen;

            lstOptions.Groups.Add(m_generalGroup);
            lstOptions.Groups.Add(m_toolbarGroup);
            lstOptions.Groups.Add(m_contextGroup);

            lstOptions.Items.AddRange(new ListViewItem[] {      
                m_promptOnBuildOutOfDate,
                m_promptOnBuildSynched,
                m_showWelcomeDialog,
                m_showAboutButton,
                m_showSchemaExportButton,
                m_hideSchemaExportButton,
                m_showOpenModelButton,
                m_showResyncButton,
                m_enableCodeGenOnConversionMethods,
                m_enableCodeGenInDaoImpl,
                m_showPropertyMapper,
                m_showInsertConversion,
                m_autoMapOnOpenMapper
                }
                );

            // Set the tooltips
            foreach (ListViewItem item in lstOptions.Items)
            {
                item.ToolTipText = item.Name;
            }
          
            UpdateState();
        }

        public void OnOK()
        {

            m_settings.ShowAboutButton = m_showAboutButton.Checked;
            m_settings.ShowOpenModelButton = m_showSchemaExportButton.Checked;
            m_settings.ShowSchemaExportButton = m_hideSchemaExportButton.Checked;
            m_settings.HideSchemaExportButtonIfProjectUnavailable = m_showOpenModelButton.Checked;
            m_settings.ShowResyncButton = m_showResyncButton.Checked;

            m_settings.PromptToGenerateOnBuildIfFilesOutOfDate = m_promptOnBuildOutOfDate.Checked;
            m_settings.PromptToGenerateOnBuildIfSolutionNotSynched = m_promptOnBuildSynched.Checked;
            m_settings.ShowWelcomeWizard = m_showWelcomeDialog.Checked;

            m_settings.OnlyEnableCodeGenOnConversionMethods = m_enableCodeGenOnConversionMethods.Checked;
            m_settings.OnlyEnableCodeGenInDaoImpl = m_enableCodeGenInDaoImpl.Checked;
            m_settings.ShowInsertEntityConversion = m_showInsertConversion.Checked;
            m_settings.ShowPropertyMapper = m_showPropertyMapper.Checked;
            m_settings.PropertyMapperAutoMapOnOpen = m_autoMapOnOpenMapper.Checked;
		}

		public void OnEnter()
		{
		}

		public void OnCancel()
		{
		}

		public void OnHelp()
		{
		}

		public void GetProperties(ref object PropertiesObject)
		{
			PropertiesObject = m_properties;
		}

        #endregion

        protected void UpdateCheckedListBox()
        {

        }

		protected void UpdateState()
		{
			//cbHideSchemaExportButton.Enabled = cbShowSchemaExportButton.Checked;
		}


    }
}
