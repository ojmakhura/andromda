
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.IO;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

using EnvDTE;
using EnvDTE80;

using Microsoft.VisualStudio;

using ICSharpCode.SharpZipLib.Checksums;
using ICSharpCode.SharpZipLib.Zip;

#endregion

namespace AndroMDA.VS80AddIn.Dialogs
{
    public partial class MDASolutionWizard : Form
    {

        #region Member variables

        private DTE m_applicationObject = null;
        private MDASolutionManager m_solutionManager = null;
        private AddInSettings m_settings = null;

        #endregion

        #region Properties

        public DTE ApplicationObject
        {
            get { return m_applicationObject; }
            set { m_applicationObject = value; }
        }

        #endregion

		#region Constructors

		public MDASolutionWizard(DTE applicationObject, MDASolutionManager solutionManager, AddInSettings settings)
        {
            m_applicationObject = applicationObject;
            m_solutionManager = solutionManager;
            m_settings = settings;
            InitializeComponent();
		}

		#endregion

		#region Events

		/// <summary>
        /// Called when the wizard is opened.  Sets defaults for the input fields.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void wizard1_Load(object sender, EventArgs e)
        {
            InitializeDefaults();
			// Give the wizard a reference to this form so it can
            // set this.AcceptButton to the Next button
            wizard1.ContainingForm = this;
        }


        /// <summary>
        /// Handles the CheckedChanged event of the cbCreateSchemaExport control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void cbCreateSchemaExport_CheckedChanged(object sender, EventArgs e)
        {
            txtSchemaExportProject.Enabled = cbCreateSchemaExport.Checked;
        }

		private void cbCreateWebProject_CheckedChanged(object sender, EventArgs e)
		{
			//txtWebProject.Enabled = cbCreateWebProject.Checked;
			//rbNHibernateConfig.Enabled = cbCreateWebProject.Checked;
			//rbWebConfig.Enabled = cbCreateWebProject.Checked;
		}

		private void cbAddWebProjectSupport_CheckedChanged(object sender, EventArgs e)
		{
			usrWebProject.Enabled = cbConfigureWebProject.Checked;
            cbUseNHibernateConfig.Enabled = cbConfigureWebProject.Checked;
			//rbWebConfig.Enabled = cbConfigureWebProject.Checked;
			cbAddMembershipSupport.Enabled = cbConfigureWebProject.Checked;
            cbConfigureASPNETCartridge.Enabled = cbConfigureWebProject.Checked;
			lblAddMembershipSupportText.Enabled = cbConfigureWebProject.Checked;
            lblStoreSettingsInNHibernateConfigDescription.Enabled = cbConfigureWebProject.Checked;
            lblASPNETCartridge.Enabled = cbConfigureWebProject.Checked;
		}

		private void cbEnableWebCommonProject_CheckedChanged(object sender, EventArgs e)
		{
			usrWebCommonProject.Enabled = cbConfigureWebCommonProject.Checked;
		}

		private void cbAddMembershipSupport_CheckedChanged(object sender, EventArgs e)
		{
			lblMembershipSelected.Visible = cbAddMembershipSupport.Checked;
			lblMembershipSelectedImage.Visible = cbAddMembershipSupport.Checked;
			cbConfigureWebCommonProject.Checked = cbAddMembershipSupport.Checked;
			cbConfigureWebCommonProject.Enabled = !cbAddMembershipSupport.Checked;
		}

        private void cbConfigureUnitTestingProject_CheckedChanged(object sender, EventArgs e)
        {
            usrTestProject.Enabled = cbConfigureUnitTestingProject.Checked;
            cbScenarioUnitSupport.Enabled = cbConfigureUnitTestingProject.Checked;
            lblScenarioUnit.Enabled = cbConfigureUnitTestingProject.Checked;
        }

        /// <summary>
        /// Fired when the confirm choices screen of the wizard is shown.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void wizardPageConfirmChoices_ShowFromNext(object sender, EventArgs e)
        {
			lstChoiceOverview.Items.Clear();
            
            AddInUtils.InsertListViewItem(lstChoiceOverview, "Application Name", txtApplicationName.Text, "General Settings");
			AddInUtils.InsertListViewItem(lstChoiceOverview, "Database Type", ddlDatabaseType.SelectedItem.ToString(), "General Settings");

            if (cbShowAdvancedOptions.Checked)
            {
                AddInUtils.InsertListViewItem(lstChoiceOverview, "AndroMDA Launcher", ddlAndroMDABootstrap.SelectedItem.ToString(), "General Settings");
                AddInUtils.InsertListViewItem(lstChoiceOverview, "AndroMDA Version", txtAndroMDAVersion.Text, "General Settings");
                AddInUtils.InsertListViewItem(lstChoiceOverview, "Use zipped model file", cbUseZippedModel.Checked ? "Yes" : "No", "General Settings");
            }

			AddInUtils.InsertListViewItem(lstChoiceOverview, "Common Project", usrCommonProject.ProjectName, "Project Settings");
			AddInUtils.InsertListViewItem(lstChoiceOverview, "Core Project", usrCoreProject.ProjectName, "Project Settings");

            if (cbCreateSchemaExport.Checked)
            {
                AddInUtils.InsertListViewItem(lstChoiceOverview, "Schema Export Project", txtSchemaExportProject.Text, "Project Settings");
            }
            else
            {
                AddInUtils.InsertListViewItem(lstChoiceOverview, "Generate Schema Export Project", cbCreateSchemaExport.Checked ? "Yes" : "No", "Project Settings");
            }

            if (cbConfigureUnitTestingProject.Checked)
            {
                AddInUtils.InsertListViewItem(lstChoiceOverview, "Testing Project", usrTestProject.ProjectName, "Project Settings");
            }
            else
            {
                AddInUtils.InsertListViewItem(lstChoiceOverview, "Generate Testing Project", cbConfigureUnitTestingProject.Checked ? "Yes" : "No", "Project Settings");
            }

            if (cbConfigureWebProject.Checked)
            {
                AddInUtils.InsertListViewItem(lstChoiceOverview, "Web Project", usrWebProject.ProjectName, "Web Settings");
                if (cbConfigureWebCommonProject.Checked)
                {
                    AddInUtils.InsertListViewItem(lstChoiceOverview, "Web Common Project", usrWebCommonProject.ProjectName, "Web Settings");
                }
                else
                {
                    AddInUtils.InsertListViewItem(lstChoiceOverview, "Configure Web Common Project", cbConfigureWebCommonProject.Checked ? "Yes" : "No", "Web Settings");
                }
                AddInUtils.InsertListViewItem(lstChoiceOverview, "NHibernate Configuration", cbUseNHibernateConfig.Checked ? "Stored in nhibernate.config" : "Stored in web.config", "Web Settings");
                AddInUtils.InsertListViewItem(lstChoiceOverview, "Membership Support", cbAddMembershipSupport.Checked ? "Yes" : "No", "Web Settings");
            }
            else
            {
                AddInUtils.InsertListViewItem(lstChoiceOverview, "Configure Web Project", cbConfigureWebProject.Checked ? "Yes" : "No", "Web Settings");
            }
			
		}

        /// <summary>
        /// Fired when the last screen of the wizard is shown.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void wizardPageComplete_ShowFromNext(object sender, EventArgs e)
        {
            wizard1.BackEnabled = false;
        }

        /// <summary>
        /// Fired when the processing screen of the wizard is shown.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void wizardPageProcessing_ShowFromNext(object sender, EventArgs e)
        {
            wizard1.NextEnabled = false;
            wizard1.BackEnabled = false;
            wizard1.CancelEnabled = false;
            pictureBoxThrobber.BringToFront();
            UpdateSolution();
        }

        /// <summary>
        /// Handles the ShowFromNext event of the wizardPageCommonProject control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void wizardPageCommonProject_ShowFromNext(object sender, EventArgs e)
        {
            usrCommonProject.OnEnter(sender, e, m_applicationObject);
        }

        /// <summary>
        /// Handles the ShowFromNext event of the wizardPageCoreProject control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void wizardPageCoreProject_ShowFromNext(object sender, EventArgs e)
        {
            usrCoreProject.OnEnter(sender, e, m_applicationObject);
        }

        /// <summary>
        /// Handles the CloseFromNext event of the wizardPageSolutionInfo control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:Gui.Wizard.PageEventArgs"/> instance containing the event data.</param>
        private void wizardPageSolutionInfo_CloseFromNext(object sender, Gui.Wizard.PageEventArgs e)
        {
            bool v1 = ValidationUtils.ValidateRequiredTextBox(txtApplicationName);
            bool pageValid = v1;
            if (!pageValid)
            {
                e.Page = wizardPageSolutionInfo;
            }
        }

        /// <summary>
        /// Handles the CloseFromNext event of the wizardPageCommonProject control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:Gui.Wizard.PageEventArgs"/> instance containing the event data.</param>
        private void wizardPageCommonProject_CloseFromNext(object sender, Gui.Wizard.PageEventArgs e)
        {
            bool v1 = usrCommonProject.ValidateControl();
            bool pageValid = v1;
            if (!pageValid)
            {
                e.Page = wizardPageCommonProject;
            }
        }

        /// <summary>
        /// Handles the CloseFromNext event of the wizardPageCoreProject control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:Gui.Wizard.PageEventArgs"/> instance containing the event data.</param>
        private void wizardPageCoreProject_CloseFromNext(object sender, Gui.Wizard.PageEventArgs e)
        {
            bool v1 = usrCoreProject.ValidateControl();
            bool pageValid = v1;
            if (!pageValid)
            {
                e.Page = wizardPageCoreProject;
            }
        }

        /// <summary>
        /// Handles the CloseFromNext event of the wizardPageSchemaExportProject control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:Gui.Wizard.PageEventArgs"/> instance containing the event data.</param>
        private void wizardPageSchemaExportProject_CloseFromNext(object sender, Gui.Wizard.PageEventArgs e)
        {
            bool v1 = true;
            if (cbCreateSchemaExport.Checked)
            {
                v1 = ValidationUtils.ValidateRequiredTextBox(txtSchemaExportProject);
            }
            bool pageValid = v1;
            if (!pageValid)
            {
                e.Page = wizardPageSchemaExportProject;
            }
        }

        /// <summary>
        /// Handles the ShowFromNext event of the wizardPageSchemaExportProject control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void wizardPageSchemaExportProject_ShowFromNext(object sender, EventArgs e)
        {
            if (cbCreateSchemaExport.Checked)
            {
                txtSchemaExportProject.Focus();
            }
            else
            {
                cbCreateSchemaExport.Focus();
            }
        }

		/// <summary>
		/// Handles the CloseFromNext event of the wizardPageWebProject control.
		/// </summary>
		/// <param name="sender">The source of the event.</param>
		/// <param name="e">The <see cref="T:Gui.Wizard.PageEventArgs"/> instance containing the event data.</param>
		private void wizardPageWebProject_CloseFromNext(object sender, Gui.Wizard.PageEventArgs e)
		{
			bool v1 = true;
			if (cbConfigureWebProject.Checked)
			{
				v1 = usrWebProject.ValidateControl();
			}
			bool pageValid = v1;
			if (!pageValid)
			{
				e.Page = wizardPageWebProject;
			}
		}

		/// <summary>
		/// Handles the ShowFromNext event of the wizardPageWebProject control.
		/// </summary>
		/// <param name="sender">The source of the event.</param>
		/// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
		private void wizardPageWebProject_ShowFromNext(object sender, EventArgs e)
		{
			usrWebProject.OnEnter(sender, e, m_applicationObject);
			if (cbConfigureWebProject.Checked == false)
			{
				cbConfigureWebProject.Focus();
			}
		}


		private void wizardPageWebCommonProject_CloseFromNext(object sender, Gui.Wizard.PageEventArgs e)
		{
		}

		private void wizardPageWebCommonProject_ShowFromBack(object sender, EventArgs e)
		{
			if (cbConfigureWebProject.Checked)
			{
			}
			else
			{
				wizard1.Back();
			}
		}

		private void wizardPageWebCommonProject_ShowFromNext(object sender, EventArgs e)
		{
			if (cbConfigureWebProject.Checked)
			{
				usrWebCommonProject.OnEnter(sender, e, m_applicationObject);
			}
			else
			{
				wizard1.Next();
			}
		}

        /// <summary>
        /// Handles the ShowFromNext event of the wizardPageSolutionInfo control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="T:System.EventArgs"/> instance containing the event data.</param>
        private void wizardPageSolutionInfo_ShowFromNext(object sender, EventArgs e)
        {
            txtApplicationName.Focus();
        }

        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            lstLog.Items.AddRange(lstStatus.Items);
            lstStatus.Items.Clear();
            lblFinishPageDescription.Visible = false;
            lnkViewLog.Visible = false;
            lstLog.Visible = true;
        }

        private void wizardPageAdvancedSettings_ShowFromNext(object sender, EventArgs e)
        {
            if (!cbShowAdvancedOptions.Checked)
            {
                wizard1.Next();
            }
        }

        private void wizardPageAdvancedSettings_ShowFromBack(object sender, EventArgs e)
        {
            if (!cbShowAdvancedOptions.Checked)
            {
                wizard1.Back();
            }
        }

        private void ddlAndroMDABootstrap_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (ddlAndroMDABootstrap.SelectedItem.ToString() == "Apache Maven 2.x")
            {
                cbUseZippedModel.Checked = false;
                cbUseZippedModel.Enabled = false;
                lblUseZippedModel.Enabled = false;
                lblUseZippedModelDescription.Enabled = false;
            }
            else
            {
                cbUseZippedModel.Enabled = true;
                lblUseZippedModel.Enabled = true;
                lblUseZippedModelDescription.Enabled = true;
            }
        }

        private void lblStoreSettingsInNHibernateConfigDescription_Click(object sender, EventArgs e)
        {

        }

        private void lblAddMembershipSupportText_Click(object sender, EventArgs e)
        {

        }

        #endregion

        private void InitializeDefaults()
        {
            // Reset the database type dropdown
            ddlDatabaseType.SelectedIndex = 0;

            // Set sensible defaults
            string solutionName = VSSolutionUtils.GetSolutionName(m_applicationObject.Solution);
            txtApplicationName.Text = solutionName;
            
            ddlVersionControl.SelectedIndex = 0;
            ddlAndroMDABootstrap.SelectedIndex = 0;

            usrCommonProject.ProjectName = solutionName + ".Common";
            usrCoreProject.ProjectName = solutionName + ".Core";
            txtSchemaExportProject.Text = solutionName + ".SchemaExport";
            usrTestProject.ProjectName = solutionName + ".Tests";
            usrWebProject.ProjectName = solutionName + ".Web";
            usrWebCommonProject.ProjectName = solutionName + ".Web.Common";
            
            cbCreateSchemaExport.Checked = true;

            cbConfigureWebProject.Checked = true;
            cbConfigureWebProject.Checked = false;
            
            cbConfigureWebCommonProject.Checked = true;
            cbConfigureWebCommonProject.Checked = false;

            cbConfigureUnitTestingProject.Checked = true;
            cbConfigureUnitTestingProject.Checked = false;

            txtAndroMDAVersion.Text = "3.3-SNAPSHOT";
            txtDotNetCartridgesVersion.Text = "1.2-SNAPSHOT";
        }

        /// <summary>
        /// Creates a ConfigFile with all the settings that will be needed for parsing the
        /// variables in the files that will be written out by the solution wizard.
        /// </summary>
        /// <returns></returns>
        private ConfigFile CreateSolutionWizardConfiguration()
        {
            ConfigFile config = new ConfigFile();

            config.Clear();

            try
            {
                config["sysinfo.currentuser"] = System.Security.Principal.WindowsIdentity.GetCurrent().Name;
            }
            catch { }
            config["sysinfo.currentdate"] = DateTime.Now.ToLongDateString();

            config["android.apptitle"] = "Android/VS v" + AndroMDA.VS80AddIn.VS80AddIn.ADD_IN_VERSION;
            config["android.version"] = AndroMDA.VS80AddIn.VS80AddIn.ADD_IN_VERSION;

            config["solution.name"] = VSSolutionUtils.GetSolutionName(m_applicationObject.Solution);
            config["solution.path"] = VSSolutionUtils.GetSolutionPath(m_applicationObject.Solution);
            
            config["application.name"] = txtApplicationName.Text;
            config["application.name.nospaces"] = txtApplicationName.Text.Replace(" ", string.Empty);
			config["application.version"] = "1.0";
			config["application.versioncontrol"] = ddlVersionControl.SelectedItem.ToString();

            config["application.andromda.bootstrap"] = ddlAndroMDABootstrap.SelectedItem.ToString();
            config["application.andromda.version"] = txtAndroMDAVersion.Text;

            config["application.dotnetcartridges.version"] = txtDotNetCartridgesVersion.Text;

            config["application.model.filename.unzipped"] = config["application.name.nospaces"] + ".Model.xmi";
            if (cbUseZippedModel.Checked)
            {
                config["application.model.filename"] = config["application.name.nospaces"] + ".Model.xmi.zip";
                config["application.model.uri"] = config["application.name.nospaces"] + ".Model.xmi.zip!" + config["application.name.nospaces"] + ".Model.xmi";
            }
            else
            {
                config["application.model.filename"] = config["application.name.nospaces"] + ".Model.xmi";
                config["application.model.uri"] = config["application.name.nospaces"] + ".Model.xmi";
            }
            config["application.model.zipped"] = BoolToString(cbUseZippedModel.Checked);
            
            config["projects.common.name"] = usrCommonProject.ProjectName;
            config["projects.common.dir"] = usrCommonProject.ProjectName;
			config["projects.common.create"] = BoolToString(usrCommonProject.CreateNewProject);
            
            config["projects.core.name"] = usrCoreProject.ProjectName;
            config["projects.core.dir"] = usrCoreProject.ProjectName;
			config["projects.core.create"] = BoolToString(usrCoreProject.CreateNewProject);

            config["projects.schemaexport.name"] = txtSchemaExportProject.Text;
            config["projects.schemaexport.dir"] = txtSchemaExportProject.Text;
            config["projects.schemaexport.create"] = BoolToString(cbCreateSchemaExport.Checked);

            config["projects.tests.configure"] = BoolToString(cbConfigureUnitTestingProject.Checked);
            config["projects.tests.name"] = usrTestProject.ProjectName;
            config["projects.tests.dir"] = usrTestProject.ProjectName;
            config["projects.tests.create"] = BoolToString(usrTestProject.CreateNewProject);
            config["projects.tests.scenariounit"] = BoolToString(cbScenarioUnitSupport.Checked);

			config["projects.web.configure"] = BoolToString(cbConfigureWebProject.Checked);
			string webname = usrWebProject.ProjectName.Trim('\\');
			if (!usrWebProject.CreateNewProject)
			{
				webname = webname.Substring(webname.LastIndexOf('\\') + 1);
			}
			config["projects.web.name"] = webname;
			config["projects.web.dir"] = usrWebProject.ProjectName;
			config["projects.web.create"] = BoolToString(usrWebProject.CreateNewProject);
			config["projects.web.usenhibernateconfig"] = BoolToString(cbUseNHibernateConfig.Checked);
			config["projects.web.addmembership"] = BoolToString(cbAddMembershipSupport.Checked);
            config["cartridges.aspnet.configure"] = BoolToString(cbConfigureASPNETCartridge.Checked);

			config["projects.web.common.configure"] = BoolToString(cbConfigureWebCommonProject.Checked);
			config["projects.web.common.name"] = usrWebCommonProject.ProjectName;
			config["projects.web.common.dir"] = usrWebCommonProject.ProjectName;
			config["projects.web.common.create"] = BoolToString(usrWebCommonProject.CreateNewProject);
			
            config["database.name"] = txtApplicationName.Text.Replace(" ", string.Empty).Replace(".", string.Empty);

			config["solution.path"] = VSSolutionUtils.GetSolutionPath(m_applicationObject.Solution);

            // TODO: Support all database types that NHibernate supports
            switch (ddlDatabaseType.SelectedItem.ToString())
            {
                case "Microsoft SQL Server 2005":
                    config["database.mappingtype"] = "MSSQL";
                    config["database.hibernatedialect"] = "net.sf.hibernate.dialect.SQLServerDialect";
                    config["database.nhibernatedialect"] = "NHibernate.Dialect.MsSql2005Dialect";
                    config["hibernate.connection.driver_class"] = "NHibernate.Driver.SqlClientDriver";
                    break;
                case "Microsoft SQL Server 2000":
                    config["database.mappingtype"] = "MSSQL";
                    config["database.hibernatedialect"] = "net.sf.hibernate.dialect.SQLServerDialect";
                    config["database.nhibernatedialect"] = "NHibernate.Dialect.MsSql2000Dialect";
                    config["hibernate.connection.driver_class"] = "NHibernate.Driver.SqlClientDriver";
                    break;
                case "MySQL":
                    config["database.mappingtype"] = "MySQL";
                    config["database.hibernatedialect"] = "net.sf.hibernate.dialect.MySQLDialect";
                    config["database.nhibernatedialect"] = "NHibernate.Dialect.MySQLDialect";
                    config["hibernate.connection.driver_class"] = "NHibernate.Driver.MySqlDataDriver";
                    break;
                case "PostgreSQL":
                    config["database.mappingtype"] = "PostgreSQL";
                    config["database.hibernatedialect"] = "net.sf.hibernate.dialect.PostgreSQLDialect";
                    config["database.nhibernatedialect"] = "NHibernate.Dialect.PostgreSQLDialect";
                    config["hibernate.connection.driver_class"] = "NHibernate.Driver.NpgsqlDriver";
                    break;
                case "Oracle 9i":
                    config["database.mappingtype"] = "Oracle9i";
                    config["database.hibernatedialect"] = "net.sf.hibernate.dialect.Oracle9Dialect";
                    config["database.nhibernatedialect"] = "NHibernate.Dialect.Oracle9Dialect";
                    config["hibernate.connection.driver_class"] = "NHibernate.Driver.OracleClientDriver";
                    break;
                case "Hypersonic":
                    config["database.mappingtype"] = "HypersonicSql";
                    config["database.hibernatedialect"] = "net.sf.hibernate.dialect.HSQLDialect";
                    config["database.nhibernatedialect"] = "NHibernate.Dialect.GenericDialect";
                    config["hibernate.connection.driver_class"] = "NHibernate.Driver.OdbcDriver";
                    break;
            }

            return config;

        }

        /// <summary>
        /// Run the MDA update on the solution
        /// </summary>
        private void UpdateSolution()
        {
            WizardSolutionProcessor processor = new WizardSolutionProcessor(this, CreateSolutionWizardConfiguration(), m_applicationObject, m_settings);
            processor.OnSolutionProcessingComplete += new WizardSolutionProcessor.SolutionProcessingCompleteHandler(processor_OnSolutionProcessingComplete);
            processor.OnSolutionProcessingStatus += new WizardSolutionProcessor.SolutionProcessingStatusHandler(processor_OnSolutionProcessingStatus);
			processor.Start();
        }

        /// <summary>
        /// Fired when the solution processor wants to print status to the window
        /// </summary>
        /// <param name="status">The status.</param>
        void processor_OnSolutionProcessingStatus(string status)
        {
            if (wizard1.BackEnabled)
            {
                wizard1.BackEnabled = false;
            }
            if (wizard1.NextEnabled)
            {
                wizard1.NextEnabled = false;
            }
            AddStatusText(status);
        }

        /// <summary>
        /// Fired when the solution processor is complete
        /// </summary>
        /// <param name="success">if set to <c>true</c> [success].</param>
        /// <param name="errorMessage">The error message.</param>
        void processor_OnSolutionProcessingComplete(bool success, string errorMessage)
        {
            if (success)
            {
                AddStatusText("Loading solution mda settings...");
                m_solutionManager.InitializeSolution();
                AddStatusText("Processing complete.");
                txtErrorMessage.Visible = false;
				lnkViewLog.Visible = true;
				lblFinishPageTitleImage.ImageIndex = 0;
            }
            else
            {
                lblFinishPageTitle.Text = "Solution Update Error";
				lblFinishPageDescription.Visible = false;
				lblFinishPageTitleImage.ImageIndex = 1;
                txtErrorMessage.Visible = true;
				lnkViewLog.Visible = false;
				txtErrorMessage.Text = errorMessage;
			}
            wizard1.NextEnabled = true;
            wizard1.Next();
        }

        /// <summary>
        /// Adds status text to the 
        /// </summary>
        /// <param name="message">The message.</param>
        private void AddStatusText(string message)
        {
            lstStatus.Items.Add(message);
            if (lstStatus.Items.Count > 13)
            {
                lstStatus.TopIndex = lstStatus.Items.Count - 13;
            }
            System.Threading.Thread.Sleep(0);
        }

        private static string BoolToString(bool condition)
        {
            return condition ? "true" : "false";
        }

    }
}