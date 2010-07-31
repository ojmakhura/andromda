
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
	public class CreateEntityVOConvertorCommand : AddInCommandBase
	{
		public CreateEntityVOConvertorCommand()
		{
			Init("CreateEntityVOConvertor", "Insert Entity/VO Conversion Code", "Insert code to convert between an entity and it's VO", 240, VisibilityType.AlwaysEnabled);
			m_isStartOfGroup = true;
			m_autoAddToCodeContextMenu = true;
			m_autoAddToToolbar = false;
		}

		public override void Exec(vsCommandExecOption executeOption, ref object varIn, ref object varOut)
		{
			CodeFunction2 currentMethod = CodeModelUtils.GetCurrentMethod(m_application);
			if (IsCommandAvailable(currentMethod))
			{
                try
                {
                    ConversionCodeGenerator codeGenerator = new ConversionCodeGenerator();

                    CodeParameter2 param = currentMethod.Parameters.Item(1) as CodeParameter2;

                    ArrayList toProperties;
                    ArrayList fromProperties;

                    try
                    {
                        toProperties = CodeModelUtils.GetPropertiesFromType(currentMethod.Type.CodeType);
                    }
                    catch (NotImplementedException)
                    {
                        throw new Exception("Method return type '" + currentMethod.Type.AsString + "' could not be resolved.");
                    }

                    try
                    {
                        fromProperties = CodeModelUtils.GetPropertiesFromType(param.Type.CodeType);
                    }
                    catch (NotImplementedException)
                    {
                        throw new Exception("Method parameter type '" + param.Type.AsString + "' could not be resolved.");
                    }

                    foreach (CodeProperty toProperty in toProperties)
                    {
                        bool mapped = false;
                        string toName = toProperty.Name;
                        string toNameNoDots = toName.Replace(".", string.Empty);
                        string toType = toProperty.Type.AsFullName;

                        foreach (CodeProperty fromProperty in fromProperties)
                        {
                            string fromName = fromProperty.Name;
                            string fromNameNoDots = fromName.Replace(".", string.Empty);
                            if (fromName == toName || fromName == toNameNoDots || fromNameNoDots == toNameNoDots)
                            {
                                string fromType = fromProperty.Type.AsFullName;
                                if (fromType.Replace("?", string.Empty) == toType.Replace("?", string.Empty))
                                {
                                    codeGenerator.AddProperty(toName, toType, fromName, fromType);
                                    mapped = true;
                                    break;
                                }
                            }
                        }

                        if (!mapped)
                        {
                            codeGenerator.AddProperty(toName, toType);
                        }
                    }

                    AddInUtils.InsertCodeInMethod(currentMethod, codeGenerator.GenerateCode(currentMethod));

                    m_application.StatusBar.Text = "Android/VS: Code inserted";
                }
                catch(Exception e)
                {
                    m_application.StatusBar.Text = "Android/VS: Unable to insert code: " + e.Message;
                    m_application.StatusBar.Highlight(true);
                }
			}
			else
			{
                m_application.StatusBar.Text = "Android/VS: Unable to insert code";
			}
        }

        #region IsCommandAvailable/QueryStatus Implementation

        public override void QueryStatus(string commandName, vsCommandStatusTextWanted neededText, ref vsCommandStatus status, ref object commandText)
        {
            if (m_solutionManager.IsEnabled && m_addInSettings.ShowInsertEntityConversion && m_application.ActiveDocument != null &&
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
