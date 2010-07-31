namespace AndroMDA.VS80AddIn.Dialogs
{
    partial class SolutionExplorerOptionsPage
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(SolutionExplorerOptionsPage));
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.label1 = new System.Windows.Forms.Label();
            this.cbIgnoreHiddenFiles = new System.Windows.Forms.CheckBox();
            this.btnDeleteResyncIgnoreItem = new System.Windows.Forms.Button();
            this.btnAddResyncIgnoreItem = new System.Windows.Forms.Button();
            this.lstResyncIgnoreList = new System.Windows.Forms.ListBox();
            this.txtResyncIgnoreList = new System.Windows.Forms.TextBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.label2 = new System.Windows.Forms.Label();
            this.btnDeleteSyncFolderItem = new System.Windows.Forms.Button();
            this.btnAddSyncFolderItem = new System.Windows.Forms.Button();
            this.lstSyncFolders = new System.Windows.Forms.ListBox();
            this.txtSyncFolder = new System.Windows.Forms.TextBox();
            this.groupBox2.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.label1);
            this.groupBox2.Controls.Add(this.cbIgnoreHiddenFiles);
            this.groupBox2.Controls.Add(this.btnDeleteResyncIgnoreItem);
            this.groupBox2.Controls.Add(this.btnAddResyncIgnoreItem);
            this.groupBox2.Controls.Add(this.lstResyncIgnoreList);
            this.groupBox2.Controls.Add(this.txtResyncIgnoreList);
            this.groupBox2.Location = new System.Drawing.Point(0, 145);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(395, 135);
            this.groupBox2.TabIndex = 3;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Folders and files to exclude";
            // 
            // label1
            // 
            this.label1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.label1.Location = new System.Drawing.Point(178, 71);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(205, 30);
            this.label1.TabIndex = 5;
            this.label1.Text = "Note:  Items added to this list are case insensitive";
            // 
            // cbIgnoreHiddenFiles
            // 
            this.cbIgnoreHiddenFiles.AutoSize = true;
            this.cbIgnoreHiddenFiles.Location = new System.Drawing.Point(181, 110);
            this.cbIgnoreHiddenFiles.Name = "cbIgnoreHiddenFiles";
            this.cbIgnoreHiddenFiles.Size = new System.Drawing.Size(188, 17);
            this.cbIgnoreHiddenFiles.TabIndex = 4;
            this.cbIgnoreHiddenFiles.Text = "E&xclude all hidden folders and files";
            this.cbIgnoreHiddenFiles.UseVisualStyleBackColor = true;
            // 
            // btnDeleteResyncIgnoreItem
            // 
            this.btnDeleteResyncIgnoreItem.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnDeleteResyncIgnoreItem.Enabled = false;
            this.btnDeleteResyncIgnoreItem.Image = ((System.Drawing.Image)(resources.GetObject("btnDeleteResyncIgnoreItem.Image")));
            this.btnDeleteResyncIgnoreItem.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnDeleteResyncIgnoreItem.Location = new System.Drawing.Point(242, 45);
            this.btnDeleteResyncIgnoreItem.Name = "btnDeleteResyncIgnoreItem";
            this.btnDeleteResyncIgnoreItem.Size = new System.Drawing.Size(65, 23);
            this.btnDeleteResyncIgnoreItem.TabIndex = 3;
            this.btnDeleteResyncIgnoreItem.Text = "&Delete";
            this.btnDeleteResyncIgnoreItem.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.btnDeleteResyncIgnoreItem.UseVisualStyleBackColor = true;
            this.btnDeleteResyncIgnoreItem.Click += new System.EventHandler(this.btnDeleteResyncIgnoreItem_Click);
            // 
            // btnAddResyncIgnoreItem
            // 
            this.btnAddResyncIgnoreItem.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnAddResyncIgnoreItem.Image = ((System.Drawing.Image)(resources.GetObject("btnAddResyncIgnoreItem.Image")));
            this.btnAddResyncIgnoreItem.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnAddResyncIgnoreItem.Location = new System.Drawing.Point(181, 45);
            this.btnAddResyncIgnoreItem.Name = "btnAddResyncIgnoreItem";
            this.btnAddResyncIgnoreItem.Size = new System.Drawing.Size(55, 23);
            this.btnAddResyncIgnoreItem.TabIndex = 2;
            this.btnAddResyncIgnoreItem.Text = "&Add";
            this.btnAddResyncIgnoreItem.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.btnAddResyncIgnoreItem.UseVisualStyleBackColor = true;
            this.btnAddResyncIgnoreItem.Click += new System.EventHandler(this.btnAddResyncIgnoreItem_Click);
            // 
            // lstResyncIgnoreList
            // 
            this.lstResyncIgnoreList.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.lstResyncIgnoreList.FormattingEnabled = true;
            this.lstResyncIgnoreList.Location = new System.Drawing.Point(6, 19);
            this.lstResyncIgnoreList.Name = "lstResyncIgnoreList";
            this.lstResyncIgnoreList.Size = new System.Drawing.Size(166, 108);
            this.lstResyncIgnoreList.TabIndex = 0;
            this.lstResyncIgnoreList.SelectedIndexChanged += new System.EventHandler(this.lstResyncIgnoreList_SelectedIndexChanged);
            // 
            // txtResyncIgnoreList
            // 
            this.txtResyncIgnoreList.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.txtResyncIgnoreList.Location = new System.Drawing.Point(181, 19);
            this.txtResyncIgnoreList.Name = "txtResyncIgnoreList";
            this.txtResyncIgnoreList.Size = new System.Drawing.Size(202, 20);
            this.txtResyncIgnoreList.TabIndex = 1;
            this.txtResyncIgnoreList.TextChanged += new System.EventHandler(this.txtResyncIgnoreList_TextChanged);
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.label2);
            this.groupBox1.Controls.Add(this.btnDeleteSyncFolderItem);
            this.groupBox1.Controls.Add(this.btnAddSyncFolderItem);
            this.groupBox1.Controls.Add(this.lstSyncFolders);
            this.groupBox1.Controls.Add(this.txtSyncFolder);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(395, 136);
            this.groupBox1.TabIndex = 4;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Folders to auto-synchronize";
            // 
            // label2
            // 
            this.label2.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.label2.Location = new System.Drawing.Point(178, 71);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(205, 30);
            this.label2.TabIndex = 5;
            this.label2.Text = "Note:  Items added to this list are case insensitive";
            // 
            // btnDeleteSyncFolderItem
            // 
            this.btnDeleteSyncFolderItem.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnDeleteSyncFolderItem.Enabled = false;
            this.btnDeleteSyncFolderItem.Image = ((System.Drawing.Image)(resources.GetObject("btnDeleteSyncFolderItem.Image")));
            this.btnDeleteSyncFolderItem.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnDeleteSyncFolderItem.Location = new System.Drawing.Point(242, 45);
            this.btnDeleteSyncFolderItem.Name = "btnDeleteSyncFolderItem";
            this.btnDeleteSyncFolderItem.Size = new System.Drawing.Size(65, 23);
            this.btnDeleteSyncFolderItem.TabIndex = 3;
            this.btnDeleteSyncFolderItem.Text = "&Delete";
            this.btnDeleteSyncFolderItem.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.btnDeleteSyncFolderItem.UseVisualStyleBackColor = true;
            this.btnDeleteSyncFolderItem.Click += new System.EventHandler(this.btnDeleteSyncFolderItem_Click);
            // 
            // btnAddSyncFolderItem
            // 
            this.btnAddSyncFolderItem.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnAddSyncFolderItem.Image = ((System.Drawing.Image)(resources.GetObject("btnAddSyncFolderItem.Image")));
            this.btnAddSyncFolderItem.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.btnAddSyncFolderItem.Location = new System.Drawing.Point(181, 45);
            this.btnAddSyncFolderItem.Name = "btnAddSyncFolderItem";
            this.btnAddSyncFolderItem.Size = new System.Drawing.Size(55, 23);
            this.btnAddSyncFolderItem.TabIndex = 2;
            this.btnAddSyncFolderItem.Text = "&Add";
            this.btnAddSyncFolderItem.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            this.btnAddSyncFolderItem.UseVisualStyleBackColor = true;
            this.btnAddSyncFolderItem.Click += new System.EventHandler(this.btnAddSyncFolderItem_Click);
            // 
            // lstSyncFolders
            // 
            this.lstSyncFolders.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.lstSyncFolders.FormattingEnabled = true;
            this.lstSyncFolders.Location = new System.Drawing.Point(6, 19);
            this.lstSyncFolders.Name = "lstSyncFolders";
            this.lstSyncFolders.Size = new System.Drawing.Size(166, 108);
            this.lstSyncFolders.TabIndex = 0;
            this.lstSyncFolders.SelectedIndexChanged += new System.EventHandler(this.lstSyncFolders_SelectedIndexChanged);
            // 
            // txtSyncFolder
            // 
            this.txtSyncFolder.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.txtSyncFolder.Location = new System.Drawing.Point(181, 19);
            this.txtSyncFolder.Name = "txtSyncFolder";
            this.txtSyncFolder.Size = new System.Drawing.Size(202, 20);
            this.txtSyncFolder.TabIndex = 1;
            this.txtSyncFolder.TextChanged += new System.EventHandler(this.txtSyncFolder_TextChanged);
            // 
            // SolutionExplorerOptionsPage
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.groupBox1);
            this.Name = "SolutionExplorerOptionsPage";
            this.Size = new System.Drawing.Size(395, 289);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.CheckBox cbIgnoreHiddenFiles;
        private System.Windows.Forms.Button btnDeleteResyncIgnoreItem;
        private System.Windows.Forms.Button btnAddResyncIgnoreItem;
        private System.Windows.Forms.ListBox lstResyncIgnoreList;
        private System.Windows.Forms.TextBox txtResyncIgnoreList;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button btnDeleteSyncFolderItem;
        private System.Windows.Forms.Button btnAddSyncFolderItem;
        private System.Windows.Forms.ListBox lstSyncFolders;
        private System.Windows.Forms.TextBox txtSyncFolder;

    }
}
