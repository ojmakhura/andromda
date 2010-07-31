
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

#endregion

namespace AndroMDA.VS80AddIn.Dialogs
{
    public partial class AboutDialog : Form
	{

		#region Member variables

		private MDASolutionManager m_solutionManager;

		#endregion

		#region Properties

		public MDASolutionManager MDASolutionManager
        {
            get { return m_solutionManager; }
            set { m_solutionManager = value; }
		}

		#endregion

		public AboutDialog()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void AboutDialog_Shown(object sender, EventArgs e)
        {
            button1.Focus();
            this.AcceptButton = button1;
			aboutOptionsPage1.SolutionManager = m_solutionManager;
            if (m_solutionManager.IsEnabled)
            {
				aboutOptionsPage1.Reset();
                aboutOptionsPage1.SolutionUsingMDA = "Yes";
                /*
				if (m_solutionManager.CommonProject != null)
				{
					aboutOptionsPage1.CommonProject = m_solutionManager.CommonProject.Name;
				}
				if (m_solutionManager.CoreProject != null)
				{
					aboutOptionsPage1.CoreProject = m_solutionManager.CoreProject.Name;
				}
				if (m_solutionManager.SchemaExportProject != null)
				{
					aboutOptionsPage1.SchemaExportProject = m_solutionManager.SchemaExportProject.Name;
				}
                */
                string modelFile = m_solutionManager.ModelFilePath;
                modelFile = modelFile.Replace(m_solutionManager.SolutionPath, string.Empty);
                int position = modelFile.LastIndexOf('!');
                if (position != -1)
                {
                    modelFile = modelFile.Substring(0, position);
                }
				aboutOptionsPage1.ModelFile = FileUtils.GetFilename(modelFile);
				if (m_solutionManager.MavenLastRunDateTime.HasValue)
				{
					aboutOptionsPage1.LastGenerated = m_solutionManager.MavenLastRunDateTime.ToString();
				}
			}
            else
            {
                aboutOptionsPage1.SolutionUsingMDA = "No";
            }
        }

        private void AboutDialog_Load(object sender, EventArgs e)
        {
            button1.Focus();
        }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {

        }

    }
}