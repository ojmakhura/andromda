
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
    public partial class ExternalToolsOptionsPage : UserControl, EnvDTE.IDTToolsOptionsPage
	{

		#region Member variables

		static MDAOptionPageProperties m_properties = new MDAOptionPageProperties();
        static AddInSettings m_settings = null;

		#endregion

		public ExternalToolsOptionsPage()
        {
            InitializeComponent();
        }

        #region IDTToolsOptionsPage Members

        
        public void OnAfterCreated(DTE DTEObject)
        {
            m_settings = new AddInSettings(DTEObject);
            cbUseOfflineMode.Checked = m_settings.MavenUseOfflineMode;
            cbUseClean.Checked = m_settings.MavenCleanFirst;
            cbUseCustomCommandLine.Checked = m_settings.MavenUseCustomCommandLine;
            txtCustomCommandLine.Text = m_settings.MavenCustomCommandLine;
            txtMagicDrawPath.Text = m_settings.UMLModellerPath;
            cbMakeModelWritable.Checked = m_settings.AutoMakeModelFileWritable;
			cbPassSchemaExportArguments.Checked = m_settings.PassSchemaExportArguments;
			txtSchemaExportCommandLine.Text = m_settings.SchemaExportCommandLine;
            UpdateState();
        }

        public void OnOK()
        {
            m_settings.MavenUseOfflineMode = cbUseOfflineMode.Checked;
            m_settings.MavenCleanFirst = cbUseClean.Checked;
            m_settings.MavenUseCustomCommandLine = cbUseCustomCommandLine.Checked;
            m_settings.MavenCustomCommandLine = txtCustomCommandLine.Text;
            m_settings.UMLModellerPath = txtMagicDrawPath.Text;
            m_settings.AutoMakeModelFileWritable = cbMakeModelWritable.Checked;
			m_settings.PassSchemaExportArguments = cbPassSchemaExportArguments.Checked;
			m_settings.SchemaExportCommandLine = txtSchemaExportCommandLine.Text;
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

		protected void UpdateState()
		{
			cbUseClean.Enabled = !cbUseCustomCommandLine.Checked;
			cbUseOfflineMode.Enabled = !cbUseCustomCommandLine.Checked;
			txtCustomCommandLine.Enabled = cbUseCustomCommandLine.Checked;
		}
		
		private void cbUseCustomCommandLine_CheckedChanged(object sender, EventArgs e)
        {
            UpdateState();
        }

		private void button2_Click(object sender, EventArgs e)
        {
            string fileName = FileUtils.GetFilename(txtMagicDrawPath.Text);
            string initialPath = FileUtils.GetPathFromFilename(txtMagicDrawPath.Text);
            openFileDialog1.InitialDirectory = initialPath;
            openFileDialog1.FileName = fileName;
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                txtMagicDrawPath.Text = openFileDialog1.FileName;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            m_settings.EraseAllSettings();
        }
    }


}
