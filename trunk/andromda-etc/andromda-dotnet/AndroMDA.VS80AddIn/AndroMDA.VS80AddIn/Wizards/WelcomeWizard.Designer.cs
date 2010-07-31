namespace AndroMDA.VS80AddIn.Dialogs
{
    partial class WelcomeWizard
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(WelcomeWizard));
            System.Windows.Forms.ListViewGroup listViewGroup5 = new System.Windows.Forms.ListViewGroup("Maven 2", System.Windows.Forms.HorizontalAlignment.Left);
            System.Windows.Forms.ListViewGroup listViewGroup6 = new System.Windows.Forms.ListViewGroup("Java 5", System.Windows.Forms.HorizontalAlignment.Left);
            System.Windows.Forms.ListViewGroup listViewGroup7 = new System.Windows.Forms.ListViewGroup("UML Tools", System.Windows.Forms.HorizontalAlignment.Left);
            System.Windows.Forms.ListViewGroup listViewGroup8 = new System.Windows.Forms.ListViewGroup("Maven 1 (optional)", System.Windows.Forms.HorizontalAlignment.Left);
            this.imageList1 = new System.Windows.Forms.ImageList(this.components);
            this.wizard1 = new Gui.Wizard.Wizard();
            this.pageIssues = new Gui.Wizard.WizardPage();
            this.panel1 = new System.Windows.Forms.Panel();
            this.lblFixDescription = new System.Windows.Forms.Label();
            this.btnFix = new System.Windows.Forms.Button();
            this.lblIssueDescription = new System.Windows.Forms.RichTextBox();
            this.pnlButtonBright3d = new System.Windows.Forms.Panel();
            this.pnlButtonDark3d = new System.Windows.Forms.Panel();
            this.lstFailureDetails = new System.Windows.Forms.ListView();
            this.columnHeader3 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader4 = new System.Windows.Forms.ColumnHeader();
            this.header1 = new Gui.Wizard.Header();
            this.pageEnvironment = new Gui.Wizard.WizardPage();
            this.lstEnvironment = new System.Windows.Forms.ListView();
            this.columnHeader1 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader2 = new System.Windows.Forms.ColumnHeader();
            this.header6 = new Gui.Wizard.Header();
            this.pageWelcome = new Gui.Wizard.WizardPage();
            this.label7 = new System.Windows.Forms.Label();
            this.linkLabel3 = new System.Windows.Forms.LinkLabel();
            this.linkLabel2 = new System.Windows.Forms.LinkLabel();
            this.linkLabel1 = new System.Windows.Forms.LinkLabel();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.label6 = new System.Windows.Forms.Label();
            this.label9 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.pictureBoxWelcome = new System.Windows.Forms.PictureBox();
            this.pageComplete = new Gui.Wizard.WizardPage();
            this.cbDontShowWizard = new System.Windows.Forms.CheckBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.btnRetest = new System.Windows.Forms.Button();
            this.wizard1.SuspendLayout();
            this.pageIssues.SuspendLayout();
            this.panel1.SuspendLayout();
            this.pageEnvironment.SuspendLayout();
            this.pageWelcome.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBoxWelcome)).BeginInit();
            this.pageComplete.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            this.SuspendLayout();
            // 
            // imageList1
            // 
            this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
            this.imageList1.TransparentColor = System.Drawing.Color.Transparent;
            this.imageList1.Images.SetKeyName(0, "info.gif");
            this.imageList1.Images.SetKeyName(1, "ok.gif");
            this.imageList1.Images.SetKeyName(2, "warning.gif");
            this.imageList1.Images.SetKeyName(3, "error.gif");
            // 
            // wizard1
            // 
            this.wizard1.ContainingForm = this;
            this.wizard1.Controls.Add(this.pageIssues);
            this.wizard1.Controls.Add(this.pageComplete);
            this.wizard1.Controls.Add(this.pageEnvironment);
            this.wizard1.Controls.Add(this.pageWelcome);
            this.wizard1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.wizard1.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.wizard1.Location = new System.Drawing.Point(0, 0);
            this.wizard1.Name = "wizard1";
            this.wizard1.Pages.AddRange(new Gui.Wizard.WizardPage[] {
            this.pageWelcome,
            this.pageEnvironment,
            this.pageIssues,
            this.pageComplete});
            this.wizard1.Size = new System.Drawing.Size(525, 360);
            this.wizard1.TabIndex = 0;
            // 
            // pageIssues
            // 
            this.pageIssues.Controls.Add(this.panel1);
            this.pageIssues.Controls.Add(this.lstFailureDetails);
            this.pageIssues.Controls.Add(this.header1);
            this.pageIssues.Dock = System.Windows.Forms.DockStyle.Fill;
            this.pageIssues.IsFinishPage = false;
            this.pageIssues.Location = new System.Drawing.Point(0, 0);
            this.pageIssues.Name = "pageIssues";
            this.pageIssues.Size = new System.Drawing.Size(525, 312);
            this.pageIssues.TabIndex = 8;
            this.pageIssues.ShowFromBack += new System.EventHandler(this.pageIssues_ShowFromBack);
            this.pageIssues.ShowFromNext += new System.EventHandler(this.pageIssues_ShowFromNext);
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.lblFixDescription);
            this.panel1.Controls.Add(this.btnRetest);
            this.panel1.Controls.Add(this.btnFix);
            this.panel1.Controls.Add(this.lblIssueDescription);
            this.panel1.Controls.Add(this.pnlButtonBright3d);
            this.panel1.Controls.Add(this.pnlButtonDark3d);
            this.panel1.Location = new System.Drawing.Point(3, 160);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(522, 149);
            this.panel1.TabIndex = 31;
            // 
            // lblFixDescription
            // 
            this.lblFixDescription.Location = new System.Drawing.Point(165, 121);
            this.lblFixDescription.Name = "lblFixDescription";
            this.lblFixDescription.Size = new System.Drawing.Size(345, 28);
            this.lblFixDescription.TabIndex = 13;
            // 
            // btnFix
            // 
            this.btnFix.Location = new System.Drawing.Point(84, 116);
            this.btnFix.Name = "btnFix";
            this.btnFix.Size = new System.Drawing.Size(75, 23);
            this.btnFix.TabIndex = 12;
            this.btnFix.Text = "Fix Issue";
            this.btnFix.UseVisualStyleBackColor = true;
            this.btnFix.Visible = false;
            this.btnFix.Click += new System.EventHandler(this.btnFix_Click);
            // 
            // lblIssueDescription
            // 
            this.lblIssueDescription.BackColor = System.Drawing.SystemColors.Control;
            this.lblIssueDescription.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.lblIssueDescription.Cursor = System.Windows.Forms.Cursors.Arrow;
            this.lblIssueDescription.Location = new System.Drawing.Point(9, 8);
            this.lblIssueDescription.Name = "lblIssueDescription";
            this.lblIssueDescription.ReadOnly = true;
            this.lblIssueDescription.ScrollBars = System.Windows.Forms.RichTextBoxScrollBars.None;
            this.lblIssueDescription.Size = new System.Drawing.Size(501, 102);
            this.lblIssueDescription.TabIndex = 11;
            this.lblIssueDescription.Text = "";
            this.lblIssueDescription.LinkClicked += new System.Windows.Forms.LinkClickedEventHandler(this.lblIssueDescription_LinkClicked);
            // 
            // pnlButtonBright3d
            // 
            this.pnlButtonBright3d.BackColor = System.Drawing.SystemColors.ControlLightLight;
            this.pnlButtonBright3d.Dock = System.Windows.Forms.DockStyle.Top;
            this.pnlButtonBright3d.Location = new System.Drawing.Point(0, 1);
            this.pnlButtonBright3d.Name = "pnlButtonBright3d";
            this.pnlButtonBright3d.Size = new System.Drawing.Size(522, 1);
            this.pnlButtonBright3d.TabIndex = 10;
            // 
            // pnlButtonDark3d
            // 
            this.pnlButtonDark3d.BackColor = System.Drawing.SystemColors.ControlDark;
            this.pnlButtonDark3d.Dock = System.Windows.Forms.DockStyle.Top;
            this.pnlButtonDark3d.Location = new System.Drawing.Point(0, 0);
            this.pnlButtonDark3d.Name = "pnlButtonDark3d";
            this.pnlButtonDark3d.Size = new System.Drawing.Size(522, 1);
            this.pnlButtonDark3d.TabIndex = 9;
            // 
            // lstFailureDetails
            // 
            this.lstFailureDetails.AutoArrange = false;
            this.lstFailureDetails.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader3,
            this.columnHeader4});
            this.lstFailureDetails.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.None;
            this.lstFailureDetails.Location = new System.Drawing.Point(3, 71);
            this.lstFailureDetails.MultiSelect = false;
            this.lstFailureDetails.Name = "lstFailureDetails";
            this.lstFailureDetails.ShowGroups = false;
            this.lstFailureDetails.Size = new System.Drawing.Size(522, 83);
            this.lstFailureDetails.SmallImageList = this.imageList1;
            this.lstFailureDetails.TabIndex = 30;
            this.lstFailureDetails.UseCompatibleStateImageBehavior = false;
            this.lstFailureDetails.View = System.Windows.Forms.View.Details;
            this.lstFailureDetails.SelectedIndexChanged += new System.EventHandler(this.listView1_SelectedIndexChanged);
            // 
            // columnHeader3
            // 
            this.columnHeader3.Text = "Issue";
            this.columnHeader3.Width = 137;
            // 
            // columnHeader4
            // 
            this.columnHeader4.Width = 353;
            // 
            // header1
            // 
            this.header1.BackColor = System.Drawing.SystemColors.Control;
            this.header1.CausesValidation = false;
            this.header1.Description = "Issues with your system environment were found.  Please click each issue to see t" +
                "he description and possible solutions.";
            this.header1.Dock = System.Windows.Forms.DockStyle.Top;
            this.header1.Image = global::AndroMDA.VS80AddIn.Resource1.issue;
            this.header1.Location = new System.Drawing.Point(0, 0);
            this.header1.Name = "header1";
            this.header1.Size = new System.Drawing.Size(525, 65);
            this.header1.TabIndex = 27;
            this.header1.Title = "Environment Issues Encountered";
            // 
            // pageEnvironment
            // 
            this.pageEnvironment.Controls.Add(this.lstEnvironment);
            this.pageEnvironment.Controls.Add(this.header6);
            this.pageEnvironment.Dock = System.Windows.Forms.DockStyle.Fill;
            this.pageEnvironment.IsFinishPage = false;
            this.pageEnvironment.Location = new System.Drawing.Point(0, 0);
            this.pageEnvironment.Name = "pageEnvironment";
            this.pageEnvironment.Size = new System.Drawing.Size(525, 312);
            this.pageEnvironment.TabIndex = 6;
            this.pageEnvironment.ShowFromBack += new System.EventHandler(this.pageEnvironment_ShowFromNext);
            this.pageEnvironment.ShowFromNext += new System.EventHandler(this.pageEnvironment_ShowFromNext);
            // 
            // lstEnvironment
            // 
            this.lstEnvironment.AutoArrange = false;
            this.lstEnvironment.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1,
            this.columnHeader2});
            listViewGroup5.Header = "Maven 2";
            listViewGroup5.Name = "envGroupAndroMDAMaven";
            listViewGroup6.Header = "Java 5";
            listViewGroup6.Name = "envGroupJava";
            listViewGroup7.Header = "UML Tools";
            listViewGroup7.Name = "envGroupUMLTools";
            listViewGroup8.Header = "Maven 1 (optional)";
            listViewGroup8.Name = "envGroupMaven1";
            this.lstEnvironment.Groups.AddRange(new System.Windows.Forms.ListViewGroup[] {
            listViewGroup5,
            listViewGroup6,
            listViewGroup7,
            listViewGroup8});
            this.lstEnvironment.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.None;
            this.lstEnvironment.Location = new System.Drawing.Point(3, 71);
            this.lstEnvironment.MultiSelect = false;
            this.lstEnvironment.Name = "lstEnvironment";
            this.lstEnvironment.Size = new System.Drawing.Size(522, 238);
            this.lstEnvironment.SmallImageList = this.imageList1;
            this.lstEnvironment.TabIndex = 27;
            this.lstEnvironment.UseCompatibleStateImageBehavior = false;
            this.lstEnvironment.View = System.Windows.Forms.View.Details;
            // 
            // columnHeader1
            // 
            this.columnHeader1.Text = "Category";
            this.columnHeader1.Width = 137;
            // 
            // columnHeader2
            // 
            this.columnHeader2.Text = "Details";
            this.columnHeader2.Width = 353;
            // 
            // header6
            // 
            this.header6.BackColor = System.Drawing.SystemColors.Control;
            this.header6.CausesValidation = false;
            this.header6.Description = "This page will check your environment settings and ensure you have everything you" +
                " need to use AndroMDA.";
            this.header6.Dock = System.Windows.Forms.DockStyle.Top;
            this.header6.Image = ((System.Drawing.Image)(resources.GetObject("header6.Image")));
            this.header6.Location = new System.Drawing.Point(0, 0);
            this.header6.Name = "header6";
            this.header6.Size = new System.Drawing.Size(525, 65);
            this.header6.TabIndex = 26;
            this.header6.Title = "Environment Check";
            // 
            // pageWelcome
            // 
            this.pageWelcome.BackColor = System.Drawing.Color.White;
            this.pageWelcome.Controls.Add(this.label7);
            this.pageWelcome.Controls.Add(this.linkLabel3);
            this.pageWelcome.Controls.Add(this.linkLabel2);
            this.pageWelcome.Controls.Add(this.linkLabel1);
            this.pageWelcome.Controls.Add(this.groupBox1);
            this.pageWelcome.Controls.Add(this.label6);
            this.pageWelcome.Controls.Add(this.label9);
            this.pageWelcome.Controls.Add(this.label5);
            this.pageWelcome.Controls.Add(this.label2);
            this.pageWelcome.Controls.Add(this.label8);
            this.pageWelcome.Controls.Add(this.pictureBoxWelcome);
            this.pageWelcome.Dock = System.Windows.Forms.DockStyle.Fill;
            this.pageWelcome.IsFinishPage = false;
            this.pageWelcome.Location = new System.Drawing.Point(0, 0);
            this.pageWelcome.Name = "pageWelcome";
            this.pageWelcome.Size = new System.Drawing.Size(525, 312);
            this.pageWelcome.TabIndex = 5;
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(419, 243);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(85, 13);
            this.label7.TabIndex = 17;
            this.label7.Text = "(Recommended)";
            // 
            // linkLabel3
            // 
            this.linkLabel3.AutoSize = true;
            this.linkLabel3.Location = new System.Drawing.Point(276, 243);
            this.linkLabel3.Name = "linkLabel3";
            this.linkLabel3.Size = new System.Drawing.Size(145, 13);
            this.linkLabel3.TabIndex = 16;
            this.linkLabel3.TabStop = true;
            this.linkLabel3.Text = "http://www.magicdraw.com/";
            this.linkLabel3.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.linkLabel_LinkClicked);
            // 
            // linkLabel2
            // 
            this.linkLabel2.AutoSize = true;
            this.linkLabel2.Location = new System.Drawing.Point(276, 217);
            this.linkLabel2.Name = "linkLabel2";
            this.linkLabel2.Size = new System.Drawing.Size(134, 13);
            this.linkLabel2.TabIndex = 16;
            this.linkLabel2.TabStop = true;
            this.linkLabel2.Text = "http://maven.apache.org/";
            this.linkLabel2.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.linkLabel_LinkClicked);
            // 
            // linkLabel1
            // 
            this.linkLabel1.AutoSize = true;
            this.linkLabel1.Location = new System.Drawing.Point(276, 230);
            this.linkLabel1.Name = "linkLabel1";
            this.linkLabel1.Size = new System.Drawing.Size(111, 13);
            this.linkLabel1.TabIndex = 16;
            this.linkLabel1.TabStop = true;
            this.linkLabel1.Text = "http://www.java.com";
            this.linkLabel1.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.linkLabel_LinkClicked);
            // 
            // groupBox1
            // 
            this.groupBox1.Location = new System.Drawing.Point(173, 31);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(345, 4);
            this.groupBox1.TabIndex = 15;
            this.groupBox1.TabStop = false;
            // 
            // label6
            // 
            this.label6.Location = new System.Drawing.Point(180, 179);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(339, 98);
            this.label6.TabIndex = 14;
            this.label6.Text = "You need the following software installed to use this add-in and AndroMDA:\r\n\r\n- M" +
                "aven 2.x\r\n- Java 5\r\n- A UML Modeler";
            // 
            // label9
            // 
            this.label9.Location = new System.Drawing.Point(180, 60);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(342, 97);
            this.label9.TabIndex = 14;
            this.label9.Text = resources.GetString("label9.Text");
            // 
            // label5
            // 
            this.label5.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.label5.FlatStyle = System.Windows.Forms.FlatStyle.System;
            this.label5.Font = new System.Drawing.Font("Tahoma", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label5.Location = new System.Drawing.Point(175, 156);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(329, 23);
            this.label5.TabIndex = 13;
            this.label5.Text = "Prerequisites";
            // 
            // label2
            // 
            this.label2.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.label2.FlatStyle = System.Windows.Forms.FlatStyle.System;
            this.label2.Font = new System.Drawing.Font("Tahoma", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(173, 38);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(345, 23);
            this.label2.TabIndex = 13;
            this.label2.Text = "Welcome";
            // 
            // label8
            // 
            this.label8.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.label8.FlatStyle = System.Windows.Forms.FlatStyle.System;
            this.label8.Font = new System.Drawing.Font("Tahoma", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label8.Location = new System.Drawing.Point(173, 9);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(345, 23);
            this.label8.TabIndex = 12;
            this.label8.Text = "Android/VS Add-In";
            // 
            // pictureBoxWelcome
            // 
            this.pictureBoxWelcome.Dock = System.Windows.Forms.DockStyle.Left;
            this.pictureBoxWelcome.Image = global::AndroMDA.VS80AddIn.Resource1.orange;
            this.pictureBoxWelcome.Location = new System.Drawing.Point(0, 0);
            this.pictureBoxWelcome.Name = "pictureBoxWelcome";
            this.pictureBoxWelcome.Size = new System.Drawing.Size(164, 312);
            this.pictureBoxWelcome.TabIndex = 11;
            this.pictureBoxWelcome.TabStop = false;
            // 
            // pageComplete
            // 
            this.pageComplete.BackColor = System.Drawing.Color.White;
            this.pageComplete.Controls.Add(this.cbDontShowWizard);
            this.pageComplete.Controls.Add(this.groupBox2);
            this.pageComplete.Controls.Add(this.label1);
            this.pageComplete.Controls.Add(this.label3);
            this.pageComplete.Controls.Add(this.label4);
            this.pageComplete.Controls.Add(this.pictureBox1);
            this.pageComplete.Dock = System.Windows.Forms.DockStyle.Fill;
            this.pageComplete.IsFinishPage = true;
            this.pageComplete.Location = new System.Drawing.Point(0, 0);
            this.pageComplete.Name = "pageComplete";
            this.pageComplete.Size = new System.Drawing.Size(525, 312);
            this.pageComplete.TabIndex = 6;
            // 
            // cbDontShowWizard
            // 
            this.cbDontShowWizard.AutoSize = true;
            this.cbDontShowWizard.Checked = true;
            this.cbDontShowWizard.CheckState = System.Windows.Forms.CheckState.Checked;
            this.cbDontShowWizard.Location = new System.Drawing.Point(183, 125);
            this.cbDontShowWizard.Name = "cbDontShowWizard";
            this.cbDontShowWizard.Size = new System.Drawing.Size(162, 17);
            this.cbDontShowWizard.TabIndex = 16;
            this.cbDontShowWizard.Text = "Don\'t show this wizard again";
            this.cbDontShowWizard.UseVisualStyleBackColor = true;
            // 
            // groupBox2
            // 
            this.groupBox2.Location = new System.Drawing.Point(173, 31);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(345, 4);
            this.groupBox2.TabIndex = 15;
            this.groupBox2.TabStop = false;
            // 
            // label1
            // 
            this.label1.Location = new System.Drawing.Point(180, 60);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(339, 62);
            this.label1.TabIndex = 14;
            this.label1.Text = resources.GetString("label1.Text");
            // 
            // label3
            // 
            this.label3.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.label3.FlatStyle = System.Windows.Forms.FlatStyle.System;
            this.label3.Font = new System.Drawing.Font("Tahoma", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(173, 38);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(345, 23);
            this.label3.TabIndex = 13;
            this.label3.Text = "Wizard Complete";
            // 
            // label4
            // 
            this.label4.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.label4.FlatStyle = System.Windows.Forms.FlatStyle.System;
            this.label4.Font = new System.Drawing.Font("Tahoma", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label4.Location = new System.Drawing.Point(173, 9);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(345, 23);
            this.label4.TabIndex = 12;
            this.label4.Text = "Android/VS Add-In";
            // 
            // pictureBox1
            // 
            this.pictureBox1.Dock = System.Windows.Forms.DockStyle.Left;
            this.pictureBox1.Image = global::AndroMDA.VS80AddIn.Resource1.orange;
            this.pictureBox1.Location = new System.Drawing.Point(0, 0);
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.Size = new System.Drawing.Size(164, 312);
            this.pictureBox1.TabIndex = 11;
            this.pictureBox1.TabStop = false;
            // 
            // btnRetest
            // 
            this.btnRetest.Location = new System.Drawing.Point(3, 116);
            this.btnRetest.Name = "btnRetest";
            this.btnRetest.Size = new System.Drawing.Size(75, 23);
            this.btnRetest.TabIndex = 12;
            this.btnRetest.Text = "&Retest";
            this.btnRetest.UseVisualStyleBackColor = true;
            this.btnRetest.Visible = false;
            this.btnRetest.Click += new System.EventHandler(this.btnRetest_Click);
            // 
            // WelcomeWizard
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(525, 360);
            this.ControlBox = false;
            this.Controls.Add(this.wizard1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "WelcomeWizard";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Android/VS Welcome Wizard";
            this.wizard1.ResumeLayout(false);
            this.pageIssues.ResumeLayout(false);
            this.panel1.ResumeLayout(false);
            this.pageEnvironment.ResumeLayout(false);
            this.pageWelcome.ResumeLayout(false);
            this.pageWelcome.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBoxWelcome)).EndInit();
            this.pageComplete.ResumeLayout(false);
            this.pageComplete.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private Gui.Wizard.Wizard wizard1;
        private Gui.Wizard.WizardPage pageWelcome;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.PictureBox pictureBoxWelcome;
        private Gui.Wizard.WizardPage pageEnvironment;
        private Gui.Wizard.Header header6;
        private System.Windows.Forms.ListView lstEnvironment;
        private System.Windows.Forms.ImageList imageList1;
        private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.ColumnHeader columnHeader2;
        private Gui.Wizard.WizardPage pageComplete;
        private System.Windows.Forms.CheckBox cbDontShowWizard;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.LinkLabel linkLabel3;
        private System.Windows.Forms.LinkLabel linkLabel2;
        private System.Windows.Forms.LinkLabel linkLabel1;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label7;
        private Gui.Wizard.WizardPage pageIssues;
        private Gui.Wizard.Header header1;
        private System.Windows.Forms.ListView lstFailureDetails;
        private System.Windows.Forms.ColumnHeader columnHeader3;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Panel pnlButtonDark3d;
        private System.Windows.Forms.Panel pnlButtonBright3d;
        private System.Windows.Forms.ColumnHeader columnHeader4;
        private System.Windows.Forms.RichTextBox lblIssueDescription;
        private System.Windows.Forms.Label lblFixDescription;
        private System.Windows.Forms.Button btnFix;
        private System.Windows.Forms.Button btnRetest;
    }
}