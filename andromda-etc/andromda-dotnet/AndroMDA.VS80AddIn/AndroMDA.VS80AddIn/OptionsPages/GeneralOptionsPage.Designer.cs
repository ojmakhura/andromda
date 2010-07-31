namespace AndroMDA.VS80AddIn.Dialogs
{
    partial class GeneralOptionsPage
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
            this.lstOptions = new System.Windows.Forms.ListView();
            this.columnHeader1 = new System.Windows.Forms.ColumnHeader();
            this.SuspendLayout();
            // 
            // lstOptions
            // 
            this.lstOptions.CheckBoxes = true;
            this.lstOptions.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1});
            this.lstOptions.Dock = System.Windows.Forms.DockStyle.Fill;
            this.lstOptions.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.None;
            this.lstOptions.Location = new System.Drawing.Point(0, 0);
            this.lstOptions.MultiSelect = false;
            this.lstOptions.Name = "lstOptions";
            this.lstOptions.Size = new System.Drawing.Size(395, 289);
            this.lstOptions.TabIndex = 4;
            this.lstOptions.UseCompatibleStateImageBehavior = false;
            this.lstOptions.View = System.Windows.Forms.View.Details;
            // 
            // columnHeader1
            // 
            this.columnHeader1.Width = 370;
            // 
            // GeneralOptionsPage
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.lstOptions);
            this.Name = "GeneralOptionsPage";
            this.Size = new System.Drawing.Size(395, 289);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ListView lstOptions;
        private System.Windows.Forms.ColumnHeader columnHeader1;
    }
}
