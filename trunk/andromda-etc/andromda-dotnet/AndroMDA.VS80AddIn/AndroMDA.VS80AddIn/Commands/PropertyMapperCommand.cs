
// Android/VS
// (c)2007 AndroMDA.org

#region Using statements

using System;
using System.Collections;

using Microsoft.VisualStudio.CommandBars;

using EnvDTE;
using EnvDTE80;

#endregion

namespace AndroMDA.VS80AddIn
{
    class PropertyMapperCommand : AddInCommandBase
    {
        public enum Blah { one, two, three }

        public PropertyMapperCommand()
		{
			Init("PropertyMapper", "Open Property Mapper", "Open the property mapper", 450, VisibilityType.AlwaysEnabled);
			m_isStartOfGroup = true;
			m_autoAddToCodeContextMenu = true;
			m_autoAddToToolbar = false;
        }

        public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
        {
			CodeFunction2 currentMethod = CodeModelUtils.GetCurrentMethod(m_application);
            if (IsCommandAvailable(currentMethod))
            {
                AndroMDA.VS80AddIn.Dialogs.PropertyMapperDialog propertyMapper = new AndroMDA.VS80AddIn.Dialogs.PropertyMapperDialog(currentMethod, m_addInSettings);
                if (propertyMapper.ShowDialog() == System.Windows.Forms.DialogResult.OK)
                {
                    // User clicked OK
                    AddInUtils.InsertCodeInMethod(currentMethod, propertyMapper.GeneratedCode);
                    m_application.StatusBar.Text = "Android/VS: Code inserted";
                }
            }
        }

        #region IsCommandAvailable/QueryStatus Implementation

        public override void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
        {
            if (m_solutionManager.IsEnabled && m_addInSettings.ShowPropertyMapper && m_application.ActiveDocument != null && 
                (m_application.ActiveDocument.Name.Contains("DaoImpl") || !m_addInSettings.OnlyEnableCodeGenInDaoImpl)
                
                )
            {
                status = (vsCommandStatus)vsCommandStatus.vsCommandStatusSupported;
                if (IsCommandAvailable(false))
                {
                    status = status | vsCommandStatus.vsCommandStatusEnabled;
                }
            }
            else
            {
                status = (vsCommandStatus)(vsCommandStatus.vsCommandStatusInvisible | vsCommandStatus.vsCommandStatusUnsupported);
            }
        }

        private bool IsCommandAvailable(CodeFunction2 currentMethod, bool fullCheck)
        {
            if (
                // Ensure we found the current method
                currentMethod != null &&
                // That it has one parameter
                currentMethod.Parameters.Count == 1 &&
                /*
                // That it is an override
                currentMethod.OverrideKind == vsCMOverrideKind.vsCMOverrideKindOverride &&
                */
                // That it is function (method)
                currentMethod.FunctionKind == vsCMFunction.vsCMFunctionFunction &&
                // That it exists inside a project
                currentMethod.InfoLocation == vsCMInfoLocation.vsCMInfoLocationProject &&
                // It's return type is not void
                currentMethod.Type.AsString != "void" &&
                // The return type does not equal the parameter type
                ((CodeParameter2)currentMethod.Parameters.Item(1)).Type.AsFullName != currentMethod.Type.AsFullName &&
                // And it's name is either ends with ToEntity or begins with To
                ((currentMethod.Name.EndsWith("ToEntity") || currentMethod.Name.StartsWith("To"))
                  || !m_addInSettings.OnlyEnableCodeGenOnConversionMethods)
                )
            {
                if (!fullCheck) return true;

                CodeParameter2 param = currentMethod.Parameters.Item(1) as CodeParameter2;
                CodeClass2 containingClass = currentMethod.Parent as CodeClass2;
                if (// Ensure we found the parameter and the parent class
                    param != null &&
                    containingClass != null &&

                    (!m_addInSettings.OnlyEnableCodeGenInDaoImpl ||
                    // Ensure we are in a DaoImpl class
                    containingClass.Name.Contains("DaoImpl"))

                    )
                {
                    return true;
                }

            }
            return false;
        }

        private bool IsCommandAvailable()
        {
            CodeFunction2 currentMethod = CodeModelUtils.GetCurrentMethod(m_application);
            return IsCommandAvailable(currentMethod);
        }

        private bool IsCommandAvailable(bool fullCheck)
        {
            CodeFunction2 currentMethod = CodeModelUtils.GetCurrentMethod(m_application);
            return IsCommandAvailable(currentMethod, fullCheck);
        }

        private bool IsCommandAvailable(CodeFunction2 currentMethod)
        {
            return IsCommandAvailable(currentMethod, true);
        }

        #endregion

    }
}
