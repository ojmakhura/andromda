namespace AndroMDA.VS80AddIn.Dialogs
{
    partial class PropertyMapperDialog
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

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(PropertyMapperDialog));
            this.pnlHeader = new System.Windows.Forms.Panel();
            this.lblToClass = new System.Windows.Forms.Label();
            this.lblFromClass = new System.Windows.Forms.Label();
            this.imageList1 = new System.Windows.Forms.ImageList(this.components);
            this.pnlFooter = new System.Windows.Forms.Panel();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.btnAutoMap = new System.Windows.Forms.Button();
            this.btnOk = new System.Windows.Forms.Button();
            this.btnCancel = new System.Windows.Forms.Button();
            this.pnlMiddle = new System.Windows.Forms.Panel();
            this.splitContainerLeft = new System.Windows.Forms.SplitContainer();
            this.lstToProperties = new AndroMDA.VS80AddIn.Dialogs.ScrollableListView();
            this.columnHeader3 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader4 = new System.Windows.Forms.ColumnHeader();
            this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.expandToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.expandAllToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.mapToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.panel1 = new System.Windows.Forms.Panel();
            this.panel3 = new System.Windows.Forms.Panel();
            this.lstFromProperties = new AndroMDA.VS80AddIn.Dialogs.ScrollableListView();
            this.columnHeader2 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader1 = new System.Windows.Forms.ColumnHeader();
            this.panel2 = new System.Windows.Forms.Panel();
            this.pnlConnections = new System.Windows.Forms.Panel();
            this.lblMapped = new System.Windows.Forms.Label();
            this.pnlHeader.SuspendLayout();
            this.pnlFooter.SuspendLayout();
            this.pnlMiddle.SuspendLayout();
            this.splitContainerLeft.Panel1.SuspendLayout();
            this.splitContainerLeft.Panel2.SuspendLayout();
            this.splitContainerLeft.SuspendLayout();
            this.contextMenuStrip1.SuspendLayout();
            this.panel1.SuspendLayout();
            this.panel3.SuspendLayout();
            this.panel2.SuspendLayout();
            this.pnlConnections.SuspendLayout();
            this.SuspendLayout();
            // 
            // pnlHeader
            // 
            this.pnlHeader.BackColor = System.Drawing.SystemColors.Control;
            this.pnlHeader.Controls.Add(this.lblToClass);
            this.pnlHeader.Controls.Add(this.lblFromClass);
            this.pnlHeader.Dock = System.Windows.Forms.DockStyle.Top;
            this.pnlHeader.Location = new System.Drawing.Point(0, 0);
            this.pnlHeader.Name = "pnlHeader";
            this.pnlHeader.Size = new System.Drawing.Size(716, 32);
            this.pnlHeader.TabIndex = 2;
            // 
            // lblToClass
            // 
            this.lblToClass.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.lblToClass.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblToClass.Location = new System.Drawing.Point(10, 9);
            this.lblToClass.Name = "lblToClass";
            this.lblToClass.Size = new System.Drawing.Size(445, 13);
            this.lblToClass.TabIndex = 0;
            this.lblToClass.Text = "label1";
            this.lblToClass.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // lblFromClass
            // 
            this.lblFromClass.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.lblFromClass.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblFromClass.Location = new System.Drawing.Point(266, 9);
            this.lblFromClass.Name = "lblFromClass";
            this.lblFromClass.Size = new System.Drawing.Size(437, 13);
            this.lblFromClass.TabIndex = 0;
            this.lblFromClass.Text = "label1";
            this.lblFromClass.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // imageList1
            // 
            this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
            this.imageList1.TransparentColor = System.Drawing.Color.Fuchsia;
            this.imageList1.Images.SetKeyName(0, "property.gif");
            this.imageList1.Images.SetKeyName(1, "property_link.gif");
            this.imageList1.Images.SetKeyName(2, "bullet_add.gif");
            this.imageList1.Images.SetKeyName(3, "link.gif");
            this.imageList1.Images.SetKeyName(4, "link_broken.gif");
            // 
            // pnlFooter
            // 
            this.pnlFooter.Controls.Add(this.groupBox1);
            this.pnlFooter.Controls.Add(this.btnAutoMap);
            this.pnlFooter.Controls.Add(this.btnOk);
            this.pnlFooter.Controls.Add(this.btnCancel);
            this.pnlFooter.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.pnlFooter.Location = new System.Drawing.Point(0, 347);
            this.pnlFooter.Name = "pnlFooter";
            this.pnlFooter.Size = new System.Drawing.Size(716, 45);
            this.pnlFooter.TabIndex = 5;
            // 
            // groupBox1
            // 
            this.groupBox1.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBox1.Location = new System.Drawing.Point(13, 5);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(690, 2);
            this.groupBox1.TabIndex = 4;
            this.groupBox1.TabStop = false;
            // 
            // btnAutoMap
            // 
            this.btnAutoMap.Location = new System.Drawing.Point(13, 13);
            this.btnAutoMap.Name = "btnAutoMap";
            this.btnAutoMap.Size = new System.Drawing.Size(75, 23);
            this.btnAutoMap.TabIndex = 0;
            this.btnAutoMap.Text = "&Auto Map";
            this.btnAutoMap.UseVisualStyleBackColor = true;
            this.btnAutoMap.Click += new System.EventHandler(this.btnAutoMap_Click);
            // 
            // btnOk
            // 
            this.btnOk.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnOk.Location = new System.Drawing.Point(547, 13);
            this.btnOk.Name = "btnOk";
            this.btnOk.Size = new System.Drawing.Size(75, 23);
            this.btnOk.TabIndex = 1;
            this.btnOk.Text = "&Ok";
            this.btnOk.UseVisualStyleBackColor = true;
            this.btnOk.Click += new System.EventHandler(this.btnOk_Click);
            // 
            // btnCancel
            // 
            this.btnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.btnCancel.Location = new System.Drawing.Point(628, 13);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(75, 23);
            this.btnCancel.TabIndex = 2;
            this.btnCancel.Text = "&Cancel";
            this.btnCancel.UseVisualStyleBackColor = true;
            this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
            // 
            // pnlMiddle
            // 
            this.pnlMiddle.Controls.Add(this.splitContainerLeft);
            this.pnlMiddle.Dock = System.Windows.Forms.DockStyle.Fill;
            this.pnlMiddle.Location = new System.Drawing.Point(0, 32);
            this.pnlMiddle.Name = "pnlMiddle";
            this.pnlMiddle.Size = new System.Drawing.Size(716, 315);
            this.pnlMiddle.TabIndex = 6;
            // 
            // splitContainerLeft
            // 
            this.splitContainerLeft.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainerLeft.Location = new System.Drawing.Point(0, 0);
            this.splitContainerLeft.Name = "splitContainerLeft";
            // 
            // splitContainerLeft.Panel1
            // 
            this.splitContainerLeft.Panel1.Controls.Add(this.lstToProperties);
            // 
            // splitContainerLeft.Panel2
            // 
            this.splitContainerLeft.Panel2.Controls.Add(this.panel1);
            this.splitContainerLeft.Size = new System.Drawing.Size(716, 315);
            this.splitContainerLeft.SplitterDistance = 277;
            this.splitContainerLeft.TabIndex = 0;
            // 
            // lstToProperties
            // 
            this.lstToProperties.Activation = System.Windows.Forms.ItemActivation.TwoClick;
            this.lstToProperties.AllowColumnReorder = true;
            this.lstToProperties.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.lstToProperties.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader3,
            this.columnHeader4});
            this.lstToProperties.ContextMenuStrip = this.contextMenuStrip1;
            this.lstToProperties.FullRowSelect = true;
            this.lstToProperties.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
            this.lstToProperties.HideSelection = false;
            this.lstToProperties.Location = new System.Drawing.Point(13, 0);
            this.lstToProperties.MultiSelect = false;
            this.lstToProperties.Name = "lstToProperties";
            this.lstToProperties.ShowGroups = false;
            this.lstToProperties.ShowItemToolTips = true;
            this.lstToProperties.Size = new System.Drawing.Size(264, 315);
            this.lstToProperties.SmallImageList = this.imageList1;
            this.lstToProperties.TabIndex = 0;
            this.lstToProperties.UseCompatibleStateImageBehavior = false;
            this.lstToProperties.View = System.Windows.Forms.View.Details;
            this.lstToProperties.ItemActivate += new System.EventHandler(this.lstToProperties_ItemActivate);
            this.lstToProperties.OnScroll += new AndroMDA.VS80AddIn.Dialogs.ScrollableListView.MyEventHandler(this.lstFromProperties_OnScroll);
            this.lstToProperties.SelectedIndexChanged += new System.EventHandler(this.lstToProperties_SelectedIndexChanged);
            // 
            // columnHeader3
            // 
            this.columnHeader3.Text = "Property";
            this.columnHeader3.Width = 147;
            // 
            // columnHeader4
            // 
            this.columnHeader4.Text = "Type";
            this.columnHeader4.Width = 263;
            // 
            // contextMenuStrip1
            // 
            this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.expandToolStripMenuItem,
            this.expandAllToolStripMenuItem,
            this.mapToolStripMenuItem});
            this.contextMenuStrip1.Name = "contextMenuStrip1";
            this.contextMenuStrip1.Size = new System.Drawing.Size(136, 70);
            this.contextMenuStrip1.Opening += new System.ComponentModel.CancelEventHandler(this.contextMenuStrip1_Opening);
            // 
            // expandToolStripMenuItem
            // 
            this.expandToolStripMenuItem.Image = global::AndroMDA.VS80AddIn.Resource1.expand;
            this.expandToolStripMenuItem.Name = "expandToolStripMenuItem";
            this.expandToolStripMenuItem.Size = new System.Drawing.Size(135, 22);
            this.expandToolStripMenuItem.Text = "&Expand";
            this.expandToolStripMenuItem.Click += new System.EventHandler(this.expandToolStripMenuItem_Click);
            // 
            // expandAllToolStripMenuItem
            // 
            this.expandAllToolStripMenuItem.Image = global::AndroMDA.VS80AddIn.Resource1.expandall;
            this.expandAllToolStripMenuItem.Name = "expandAllToolStripMenuItem";
            this.expandAllToolStripMenuItem.Size = new System.Drawing.Size(135, 22);
            this.expandAllToolStripMenuItem.Text = "Expand &All";
            this.expandAllToolStripMenuItem.Visible = false;
            this.expandAllToolStripMenuItem.Click += new System.EventHandler(this.expandAllToolStripMenuItem_Click);
            // 
            // mapToolStripMenuItem
            // 
            this.mapToolStripMenuItem.Image = global::AndroMDA.VS80AddIn.Resource1.link_add;
            this.mapToolStripMenuItem.Name = "mapToolStripMenuItem";
            this.mapToolStripMenuItem.Size = new System.Drawing.Size(135, 22);
            this.mapToolStripMenuItem.Text = "&Map";
            this.mapToolStripMenuItem.Click += new System.EventHandler(this.mapToolStripMenuItem_Click);
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.panel3);
            this.panel1.Controls.Add(this.panel2);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel1.Location = new System.Drawing.Point(0, 0);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(435, 315);
            this.panel1.TabIndex = 4;
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.lstFromProperties);
            this.panel3.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel3.Location = new System.Drawing.Point(128, 0);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(307, 315);
            this.panel3.TabIndex = 9;
            // 
            // lstFromProperties
            // 
            this.lstFromProperties.Alignment = System.Windows.Forms.ListViewAlignment.Left;
            this.lstFromProperties.AllowColumnReorder = true;
            this.lstFromProperties.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.lstFromProperties.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader2,
            this.columnHeader1});
            this.lstFromProperties.ContextMenuStrip = this.contextMenuStrip1;
            this.lstFromProperties.FullRowSelect = true;
            this.lstFromProperties.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
            this.lstFromProperties.HideSelection = false;
            this.lstFromProperties.Location = new System.Drawing.Point(0, 0);
            this.lstFromProperties.MultiSelect = false;
            this.lstFromProperties.Name = "lstFromProperties";
            this.lstFromProperties.ShowGroups = false;
            this.lstFromProperties.ShowItemToolTips = true;
            this.lstFromProperties.Size = new System.Drawing.Size(294, 315);
            this.lstFromProperties.SmallImageList = this.imageList1;
            this.lstFromProperties.TabIndex = 0;
            this.lstFromProperties.UseCompatibleStateImageBehavior = false;
            this.lstFromProperties.View = System.Windows.Forms.View.Details;
            this.lstFromProperties.ItemActivate += new System.EventHandler(this.lstFromProperties_ItemActivate);
            this.lstFromProperties.OnScroll += new AndroMDA.VS80AddIn.Dialogs.ScrollableListView.MyEventHandler(this.lstFromProperties_OnScroll);
            this.lstFromProperties.SelectedIndexChanged += new System.EventHandler(this.lstFromProperties_SelectedIndexChanged);
            // 
            // columnHeader2
            // 
            this.columnHeader2.Text = "Property";
            this.columnHeader2.Width = 144;
            // 
            // columnHeader1
            // 
            this.columnHeader1.Text = "Type";
            this.columnHeader1.Width = 251;
            // 
            // panel2
            // 
            this.panel2.BackColor = System.Drawing.SystemColors.Control;
            this.panel2.Controls.Add(this.pnlConnections);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Left;
            this.panel2.Location = new System.Drawing.Point(0, 0);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(128, 315);
            this.panel2.TabIndex = 8;
            // 
            // pnlConnections
            // 
            this.pnlConnections.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)));
            this.pnlConnections.BackColor = System.Drawing.SystemColors.Control;
            this.pnlConnections.Controls.Add(this.lblMapped);
            this.pnlConnections.Location = new System.Drawing.Point(0, 0);
            this.pnlConnections.Name = "pnlConnections";
            this.pnlConnections.Size = new System.Drawing.Size(127, 316);
            this.pnlConnections.TabIndex = 0;
            this.pnlConnections.Paint += new System.Windows.Forms.PaintEventHandler(this.pnlConnections_Paint);
            // 
            // lblMapped
            // 
            this.lblMapped.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.lblMapped.BackColor = System.Drawing.Color.White;
            this.lblMapped.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.lblMapped.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.lblMapped.ImageIndex = 3;
            this.lblMapped.ImageList = this.imageList1;
            this.lblMapped.Location = new System.Drawing.Point(10, 0);
            this.lblMapped.Name = "lblMapped";
            this.lblMapped.Padding = new System.Windows.Forms.Padding(8, 4, 4, 4);
            this.lblMapped.Size = new System.Drawing.Size(107, 21);
            this.lblMapped.TabIndex = 1;
            this.lblMapped.Text = "     UnMapped";
            this.lblMapped.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.lblMapped.Visible = false;
            // 
            // PropertyMapperDialog
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(716, 392);
            this.Controls.Add(this.pnlMiddle);
            this.Controls.Add(this.pnlFooter);
            this.Controls.Add(this.pnlHeader);
            this.DoubleBuffered = true;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "PropertyMapperDialog";
            this.Text = "Property Mapper";
            this.Shown += new System.EventHandler(this.PropertyMapperDialog_Shown);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.PropertyMapperDialog_KeyDown);
            this.pnlHeader.ResumeLayout(false);
            this.pnlFooter.ResumeLayout(false);
            this.pnlMiddle.ResumeLayout(false);
            this.splitContainerLeft.Panel1.ResumeLayout(false);
            this.splitContainerLeft.Panel2.ResumeLayout(false);
            this.splitContainerLeft.ResumeLayout(false);
            this.contextMenuStrip1.ResumeLayout(false);
            this.panel1.ResumeLayout(false);
            this.panel3.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.pnlConnections.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel pnlHeader;
        private System.Windows.Forms.Label lblFromClass;
        private System.Windows.Forms.Label lblToClass;
        private System.Windows.Forms.ImageList imageList1;
        private System.Windows.Forms.Panel pnlFooter;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Button btnOk;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.Panel pnlMiddle;
        private System.Windows.Forms.SplitContainer splitContainerLeft;
        private AndroMDA.VS80AddIn.Dialogs.ScrollableListView lstFromProperties;
        private System.Windows.Forms.ColumnHeader columnHeader2;
        private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.Button btnAutoMap;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Panel panel3;
        private AndroMDA.VS80AddIn.Dialogs.ScrollableListView lstToProperties;
        private System.Windows.Forms.ColumnHeader columnHeader3;
        private System.Windows.Forms.ColumnHeader columnHeader4;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Panel pnlConnections;
        private System.Windows.Forms.Label lblMapped;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
        private System.Windows.Forms.ToolStripMenuItem expandToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem expandAllToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem mapToolStripMenuItem;
    }
}