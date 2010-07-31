namespace AndroMDA.VS80AddIn.Dialogs
{
    partial class ExternalToolsOptionsPage
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ExternalToolsOptionsPage));
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.cbUseClean = new System.Windows.Forms.CheckBox();
            this.cbUseCustomCommandLine = new System.Windows.Forms.CheckBox();
            this.cbUseOfflineMode = new System.Windows.Forms.CheckBox();
            this.txtCustomCommandLine = new System.Windows.Forms.TextBox();
            this.imageList1 = new System.Windows.Forms.ImageList(this.components);
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.button2 = new System.Windows.Forms.Button();
            this.label3 = new System.Windows.Forms.Label();
            this.txtMagicDrawPath = new System.Windows.Forms.TextBox();
            this.cbMakeModelWritable = new System.Windows.Forms.CheckBox();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.txtSchemaExportCommandLine = new System.Windows.Forms.TextBox();
            this.cbPassSchemaExportArguments = new System.Windows.Forms.CheckBox();
            this.panel1 = new System.Windows.Forms.Panel();
            this.panel2 = new System.Windows.Forms.Panel();
            this.panel3 = new System.Windows.Forms.Panel();
            this.groupBox2.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.panel1.SuspendLayout();
            this.panel2.SuspendLayout();
            this.panel3.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.cbUseClean);
            this.groupBox2.Controls.Add(this.cbUseCustomCommandLine);
            this.groupBox2.Controls.Add(this.cbUseOfflineMode);
            this.groupBox2.Controls.Add(this.txtCustomCommandLine);
            this.groupBox2.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox2.Location = new System.Drawing.Point(0, 0);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(395, 72);
            this.groupBox2.TabIndex = 1;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Maven";
            // 
            // cbUseClean
            // 
            this.cbUseClean.AutoSize = true;
            this.cbUseClean.Location = new System.Drawing.Point(168, 19);
            this.cbUseClean.Name = "cbUseClean";
            this.cbUseClean.Size = new System.Drawing.Size(166, 17);
            this.cbUseClean.TabIndex = 2;
            this.cbUseClean.Text = "&Clean before generate (clean)";
            this.cbUseClean.UseVisualStyleBackColor = true;
            // 
            // cbUseCustomCommandLine
            // 
            this.cbUseCustomCommandLine.AutoSize = true;
            this.cbUseCustomCommandLine.Location = new System.Drawing.Point(11, 42);
            this.cbUseCustomCommandLine.Name = "cbUseCustomCommandLine";
            this.cbUseCustomCommandLine.Size = new System.Drawing.Size(153, 17);
            this.cbUseCustomCommandLine.TabIndex = 3;
            this.cbUseCustomCommandLine.Text = "Use custom c&ommand line:";
            this.cbUseCustomCommandLine.UseVisualStyleBackColor = true;
            this.cbUseCustomCommandLine.CheckedChanged += new System.EventHandler(this.cbUseCustomCommandLine_CheckedChanged);
            // 
            // cbUseOfflineMode
            // 
            this.cbUseOfflineMode.AutoSize = true;
            this.cbUseOfflineMode.Location = new System.Drawing.Point(11, 19);
            this.cbUseOfflineMode.Name = "cbUseOfflineMode";
            this.cbUseOfflineMode.Size = new System.Drawing.Size(123, 17);
            this.cbUseOfflineMode.TabIndex = 1;
            this.cbUseOfflineMode.Text = "Use &offline mode (-o)";
            this.cbUseOfflineMode.UseVisualStyleBackColor = true;
            // 
            // txtCustomCommandLine
            // 
            this.txtCustomCommandLine.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.txtCustomCommandLine.Location = new System.Drawing.Point(168, 40);
            this.txtCustomCommandLine.Name = "txtCustomCommandLine";
            this.txtCustomCommandLine.Size = new System.Drawing.Size(216, 20);
            this.txtCustomCommandLine.TabIndex = 4;
            // 
            // imageList1
            // 
            this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
            this.imageList1.TransparentColor = System.Drawing.Color.Transparent;
            this.imageList1.Images.SetKeyName(0, "search.ico");
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.button2);
            this.groupBox3.Controls.Add(this.label3);
            this.groupBox3.Controls.Add(this.txtMagicDrawPath);
            this.groupBox3.Controls.Add(this.cbMakeModelWritable);
            this.groupBox3.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox3.Location = new System.Drawing.Point(0, 0);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(395, 87);
            this.groupBox3.TabIndex = 1;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "UML Modeling Tool";
            // 
            // button2
            // 
            this.button2.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.button2.ImageIndex = 0;
            this.button2.ImageList = this.imageList1;
            this.button2.Location = new System.Drawing.Point(357, 34);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(27, 20);
            this.button2.TabIndex = 3;
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(8, 18);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(172, 13);
            this.label3.TabIndex = 1;
            this.label3.Text = "Path to external UML modeling tool";
            // 
            // txtMagicDrawPath
            // 
            this.txtMagicDrawPath.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.txtMagicDrawPath.Location = new System.Drawing.Point(11, 34);
            this.txtMagicDrawPath.Name = "txtMagicDrawPath";
            this.txtMagicDrawPath.Size = new System.Drawing.Size(340, 20);
            this.txtMagicDrawPath.TabIndex = 0;
            // 
            // cbMakeModelWritable
            // 
            this.cbMakeModelWritable.AutoSize = true;
            this.cbMakeModelWritable.Location = new System.Drawing.Point(11, 60);
            this.cbMakeModelWritable.Name = "cbMakeModelWritable";
            this.cbMakeModelWritable.Size = new System.Drawing.Size(266, 17);
            this.cbMakeModelWritable.TabIndex = 1;
            this.cbMakeModelWritable.Text = "Make model file writable when Open Model clicked";
            this.cbMakeModelWritable.UseVisualStyleBackColor = true;
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.FileName = "openFileDialog1";
            this.openFileDialog1.Filter = "Executables (*.exe; *.bat)|*.exe;*.bat";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.txtSchemaExportCommandLine);
            this.groupBox1.Controls.Add(this.cbPassSchemaExportArguments);
            this.groupBox1.Dock = System.Windows.Forms.DockStyle.Top;
            this.groupBox1.Location = new System.Drawing.Point(0, 0);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(395, 49);
            this.groupBox1.TabIndex = 2;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = ":";
            // 
            // txtSchemaExportCommandLine
            // 
            this.txtSchemaExportCommandLine.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.txtSchemaExportCommandLine.Location = new System.Drawing.Point(234, 17);
            this.txtSchemaExportCommandLine.Name = "txtSchemaExportCommandLine";
            this.txtSchemaExportCommandLine.Size = new System.Drawing.Size(150, 20);
            this.txtSchemaExportCommandLine.TabIndex = 0;
            // 
            // cbPassSchemaExportArguments
            // 
            this.cbPassSchemaExportArguments.AutoSize = true;
            this.cbPassSchemaExportArguments.Location = new System.Drawing.Point(11, 19);
            this.cbPassSchemaExportArguments.Name = "cbPassSchemaExportArguments";
            this.cbPassSchemaExportArguments.Size = new System.Drawing.Size(217, 17);
            this.cbPassSchemaExportArguments.TabIndex = 1;
            this.cbPassSchemaExportArguments.Text = "Pass arguments on Run Schema Export:";
            this.cbPassSchemaExportArguments.UseVisualStyleBackColor = true;
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.groupBox2);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel1.Location = new System.Drawing.Point(0, 0);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(395, 81);
            this.panel1.TabIndex = 3;
            // 
            // panel2
            // 
            this.panel2.Controls.Add(this.groupBox3);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel2.Location = new System.Drawing.Point(0, 81);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(395, 94);
            this.panel2.TabIndex = 4;
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.groupBox1);
            this.panel3.Dock = System.Windows.Forms.DockStyle.Top;
            this.panel3.Location = new System.Drawing.Point(0, 175);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(395, 83);
            this.panel3.TabIndex = 5;
            // 
            // ExternalToolsOptionsPage
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.panel3);
            this.Controls.Add(this.panel2);
            this.Controls.Add(this.panel1);
            this.Name = "ExternalToolsOptionsPage";
            this.Size = new System.Drawing.Size(395, 289);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.groupBox3.ResumeLayout(false);
            this.groupBox3.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.panel1.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.panel3.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

		private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.CheckBox cbUseOfflineMode;
        private System.Windows.Forms.CheckBox cbUseClean;
        private System.Windows.Forms.CheckBox cbUseCustomCommandLine;
        private System.Windows.Forms.TextBox txtCustomCommandLine;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.Label label3;
		private System.Windows.Forms.TextBox txtMagicDrawPath;
        private System.Windows.Forms.ImageList imageList1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.OpenFileDialog openFileDialog1;
        private System.Windows.Forms.CheckBox cbMakeModelWritable;
		private System.Windows.Forms.GroupBox groupBox1;
		private System.Windows.Forms.TextBox txtSchemaExportCommandLine;
		private System.Windows.Forms.CheckBox cbPassSchemaExportArguments;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Panel panel3;



    }
}
