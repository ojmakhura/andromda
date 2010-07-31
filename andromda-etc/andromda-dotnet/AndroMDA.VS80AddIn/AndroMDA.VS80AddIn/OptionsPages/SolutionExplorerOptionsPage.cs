
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
    public partial class SolutionExplorerOptionsPage : UserControl, EnvDTE.IDTToolsOptionsPage
    {

        #region Member variables

        static MDAOptionPageProperties m_properties = new MDAOptionPageProperties();
        static AddInSettings m_settings = null;

        #endregion

        public SolutionExplorerOptionsPage()
        {
            InitializeComponent();
        }

        #region IDTToolsOptionsPage Members

        public void OnAfterCreated(DTE DTEObject)
        {
            m_settings = new AddInSettings(DTEObject);

            lstResyncIgnoreList.Items.Clear();
            if (m_settings.ResyncIgnoreList.Length > 0)
            {
                string[] list = m_settings.ResyncIgnoreList.Split(new char[] { ';' });
                foreach (string item in list)
                {
                    if (item.Length > 0) lstResyncIgnoreList.Items.Add(item);
                }
            }
            txtResyncIgnoreList.Text = string.Empty;
            btnAddResyncIgnoreItem.Enabled = false;
            btnDeleteResyncIgnoreItem.Enabled = false;

            cbIgnoreHiddenFiles.Checked = m_settings.ResyncIgnoreHiddenFiles;

            lstSyncFolders.Items.Clear();
            if (m_settings.SyncFolderList.Length > 0)
            {
                string[] list = m_settings.SyncFolderList.Split(new char[] { ';' });
                foreach (string item in list)
                {
                    if (item.Length > 0) lstSyncFolders.Items.Add(item);
                }
            }
            txtSyncFolder.Text = string.Empty;
            btnAddSyncFolderItem.Enabled = false;
            btnDeleteSyncFolderItem.Enabled = false;

        }

        public void OnOK()
        {
            string ignoreList = string.Empty;
            foreach (string item in lstResyncIgnoreList.Items)
            {
                ignoreList = ignoreList + item + ";";
            }
            m_settings.ResyncIgnoreList = ignoreList;
            m_settings.ResyncIgnoreHiddenFiles = cbIgnoreHiddenFiles.Checked;
            string syncFolderList = string.Empty;
            foreach (string item in lstSyncFolders.Items)
            {
                syncFolderList = syncFolderList + item + ";";
            }
            m_settings.SyncFolderList = syncFolderList;
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

        // Ignore items

        private void lstResyncIgnoreList_SelectedIndexChanged(object sender, EventArgs e)
        {
            btnDeleteResyncIgnoreItem.Enabled = lstResyncIgnoreList.SelectedIndex != -1;
        }

        private void btnAddResyncIgnoreItem_Click(object sender, EventArgs e)
        {
            lstResyncIgnoreList.Items.Add(txtResyncIgnoreList.Text);
            txtResyncIgnoreList.Text = string.Empty;
        }

        private void txtResyncIgnoreList_TextChanged(object sender, EventArgs e)
        {
            btnAddResyncIgnoreItem.Enabled = txtResyncIgnoreList.Text.Length > 0;
        }

        private void btnDeleteResyncIgnoreItem_Click(object sender, EventArgs e)
        {
            lstResyncIgnoreList.Items.Remove(lstResyncIgnoreList.SelectedItem);
            lstResyncIgnoreList.SelectedIndex = -1;
        }

        // Sync folders

        private void lstSyncFolders_SelectedIndexChanged(object sender, EventArgs e)
        {
            btnDeleteSyncFolderItem.Enabled = lstSyncFolders.SelectedIndex != -1;
        }

        private void btnAddSyncFolderItem_Click(object sender, EventArgs e)
        {
            lstSyncFolders.Items.Add(txtSyncFolder.Text);
            txtSyncFolder.Text = string.Empty;
        }

        private void txtSyncFolder_TextChanged(object sender, EventArgs e)
        {
            btnAddSyncFolderItem.Enabled = txtSyncFolder.Text.Length > 0;
        }

        private void btnDeleteSyncFolderItem_Click(object sender, EventArgs e)
        {
            lstSyncFolders.Items.Remove(lstSyncFolders.SelectedItem);
            lstSyncFolders.SelectedIndex = -1;
        }

    }
}
