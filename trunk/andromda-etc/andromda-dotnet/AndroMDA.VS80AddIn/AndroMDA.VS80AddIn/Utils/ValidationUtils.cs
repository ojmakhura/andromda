
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using System.Drawing;

#endregion

namespace AndroMDA.VS80AddIn
{
    public class ValidationUtils
    {
        public static void ValidateControl(Control c, bool passedValidation)
        {
            if (!passedValidation)
            {
                
                c.BackColor = Color.Yellow;
            }
            else
            {
                c.BackColor = Color.White;
            }
            c.Update();
        }

        public static bool ValidateRequiredTextBox(TextBox t)
        {
            bool passed = t.Text.Trim().Length > 0;
            ValidateControl(t, passed);
            return passed;
        }

    }
}
