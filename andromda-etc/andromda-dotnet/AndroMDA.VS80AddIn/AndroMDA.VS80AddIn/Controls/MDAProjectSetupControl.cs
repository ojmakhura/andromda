
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;

using EnvDTE;
using VSLangProj;

#endregion

namespace AndroMDA.VS80AddIn.Dialogs
{
    public partial class MDAProjectSetupControl : UserControl
    {

        #region Member variables

        private bool m_firstEnter = true;

        #endregion

        #region Properties
		/*
        [Category("Appearance")]
        [LocalizableAttribute(true)]
        [BindableAttribute(true)]
        public string Title
        {
            get { return lblTitle.Text; }
            set { lblTitle.Text = value; }
        }

        [Category("Appearance")]
        [LocalizableAttribute(true)]
        [BindableAttribute(true)]
        public string Description
        {
            get { return lblDescription.Text; }
            set { lblDescription.Text = value; }
        }
		*/

		private bool m_webProjectMode = false;

		[Category("Behavior")]
		[LocalizableAttribute(true)]
		[BindableAttribute(true)]
		[DefaultValue(false)]
		public bool WebProjectMode
		{
			get { return m_webProjectMode; }
			set 
			{ 
				/*
				rbDontAddSupport.Enabled = rbDontAddSupport.Visible = value;
				int baseY = value ? 23 : 0;
				rbCreate.Top = baseY + 0;
				txtNewProject.Top = baseY + 20;
				rbExisting.Top = baseY + 45;
				ddlExistingProject.Top = baseY + 66;
				*/
				m_webProjectMode = value;
				if (m_webProjectMode)
				{
					rbExisting.Text = "Use existing web site";
					rbCreate.Text = "Create new web site";
				}
				else
				{
					rbExisting.Text = "Use existing project";
					rbCreate.Text = "Create new project";
				}
			}
		}

		public bool DontAddSupport
		{
			get { return rbDontAddSupport.Checked; }
		}

        public bool CreateNewProject
        {
            get { return rbCreate.Checked; }
        }

        public string ProjectName
        {
            get
            {
                if (CreateNewProject)
                {
                    return txtNewProject.Text;
                }
                else
                {
                    return ddlExistingProject.SelectedItem.ToString();
                }
            }
            set
            {
                rbCreate.Checked = true;
                txtNewProject.Text = value;
            }
        }

        #endregion

        public MDAProjectSetupControl()
        {
            InitializeComponent();
        }

        public void OnEnter(object sender, EventArgs e, DTE applicationObject)
        {
            if (m_firstEnter)
            {
                m_firstEnter = false;
                ddlExistingProject.Items.Clear();
				string itemToSelect = string.Empty;
                foreach (Project p in applicationObject.Solution.Projects)
                {
					if (
						(m_webProjectMode && p.Kind == VSSolutionUtils.ProjectKindWeb) ||
                        (!m_webProjectMode && p.Kind != VSSolutionUtils.ProjectKindWeb)
						)
					{
						
						ddlExistingProject.Items.Add(p.Name);

						// If there is a project in the solution with the same
						// name as the pre-generated project
						if (!m_webProjectMode && p.Name == txtNewProject.Text)
						{
							// Select it by default
							itemToSelect = p.Name;
						}
						// Or if we are in web project mode select the first web site automatically
						else if (m_webProjectMode && itemToSelect == string.Empty)
						{
							itemToSelect = p.Name;
						}
					}
                }
                if (ddlExistingProject.Items.Count > 0)
                {
					if (itemToSelect != string.Empty)
					{
						ddlExistingProject.SelectedIndex = ddlExistingProject.Items.IndexOf(itemToSelect);
						rbExisting.Checked = true;
					}
					else
					{
						ddlExistingProject.SelectedIndex = 0;
						rbExisting.Checked = false;
					}
                }
                else
                {
                    rbExisting.Enabled = false;
                }
            }
            if (rbExisting.Checked)
            {
                ddlExistingProject.Focus();
            }
            else
            {
                txtNewProject.Focus();
            }
        }

        public bool ValidateControl()
        {
            if (rbExisting.Checked)
            {
                return ddlExistingProject.SelectedItem.ToString() != string.Empty;
            }
            else
            {
                return ValidationUtils.ValidateRequiredTextBox(txtNewProject);
            }
        }

        private void checkChanged(object sender, EventArgs e)
        {
            if (txtNewProject.Enabled)
            {
                txtNewProject.BackColor = SystemColors.Window;
            }
            else
            {
                txtNewProject.BackColor = SystemColors.Control;
            }
            txtNewProject.Enabled = rbCreate.Checked;
            ddlExistingProject.Enabled = rbExisting.Checked;
        }

    }
}
