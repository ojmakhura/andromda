namespace AndroMDA.VS80AddIn.Dialogs
{
    partial class MDAProjectSetupControl
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
			this.ddlExistingProject = new System.Windows.Forms.ComboBox();
			this.txtNewProject = new System.Windows.Forms.TextBox();
			this.rbExisting = new System.Windows.Forms.RadioButton();
			this.rbCreate = new System.Windows.Forms.RadioButton();
			this.rbDontAddSupport = new System.Windows.Forms.RadioButton();
			this.SuspendLayout();
			// 
			// ddlExistingProject
			// 
			this.ddlExistingProject.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.ddlExistingProject.Enabled = false;
			this.ddlExistingProject.FormattingEnabled = true;
			this.ddlExistingProject.Location = new System.Drawing.Point(18, 72);
			this.ddlExistingProject.Name = "ddlExistingProject";
			this.ddlExistingProject.Size = new System.Drawing.Size(394, 21);
			this.ddlExistingProject.TabIndex = 16;
			// 
			// txtNewProject
			// 
			this.txtNewProject.Location = new System.Drawing.Point(18, 23);
			this.txtNewProject.Name = "txtNewProject";
			this.txtNewProject.Size = new System.Drawing.Size(394, 20);
			this.txtNewProject.TabIndex = 13;
			// 
			// rbExisting
			// 
			this.rbExisting.AutoSize = true;
			this.rbExisting.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Bold);
			this.rbExisting.Location = new System.Drawing.Point(0, 49);
			this.rbExisting.Name = "rbExisting";
			this.rbExisting.Size = new System.Drawing.Size(138, 17);
			this.rbExisting.TabIndex = 15;
			this.rbExisting.Text = "Use existing project";
			this.rbExisting.UseVisualStyleBackColor = true;
			this.rbExisting.CheckedChanged += new System.EventHandler(this.checkChanged);
			// 
			// rbCreate
			// 
			this.rbCreate.AutoSize = true;
			this.rbCreate.Checked = true;
			this.rbCreate.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Bold);
			this.rbCreate.Location = new System.Drawing.Point(0, 0);
			this.rbCreate.Name = "rbCreate";
			this.rbCreate.Size = new System.Drawing.Size(133, 17);
			this.rbCreate.TabIndex = 12;
			this.rbCreate.TabStop = true;
			this.rbCreate.Text = "Create new project";
			this.rbCreate.UseVisualStyleBackColor = true;
			this.rbCreate.CheckedChanged += new System.EventHandler(this.checkChanged);
			// 
			// rbDontAddSupport
			// 
			this.rbDontAddSupport.AutoSize = true;
			this.rbDontAddSupport.Enabled = false;
			this.rbDontAddSupport.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Bold);
			this.rbDontAddSupport.Location = new System.Drawing.Point(0, 0);
			this.rbDontAddSupport.Name = "rbDontAddSupport";
			this.rbDontAddSupport.Size = new System.Drawing.Size(203, 17);
			this.rbDontAddSupport.TabIndex = 14;
			this.rbDontAddSupport.Text = "Do not add web project support";
			this.rbDontAddSupport.UseVisualStyleBackColor = true;
			this.rbDontAddSupport.Visible = false;
			// 
			// MDAProjectSetupControl
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.Controls.Add(this.ddlExistingProject);
			this.Controls.Add(this.txtNewProject);
			this.Controls.Add(this.rbExisting);
			this.Controls.Add(this.rbCreate);
			this.Controls.Add(this.rbDontAddSupport);
			this.Name = "MDAProjectSetupControl";
			this.Size = new System.Drawing.Size(434, 138);
			this.ResumeLayout(false);
			this.PerformLayout();

        }

        #endregion

		private System.Windows.Forms.ComboBox ddlExistingProject;
		private System.Windows.Forms.TextBox txtNewProject;
		private System.Windows.Forms.RadioButton rbExisting;
		private System.Windows.Forms.RadioButton rbCreate;
		private System.Windows.Forms.RadioButton rbDontAddSupport;


	}
}
