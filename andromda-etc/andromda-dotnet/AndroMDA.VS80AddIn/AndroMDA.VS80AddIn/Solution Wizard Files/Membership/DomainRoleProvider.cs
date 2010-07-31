//
// DomainRoleProvider
//

#region Using statements

using System;
using System.Web;
using System.Web.Configuration;
using System.Web.Security;
using System.Security.Principal;
using System.Security.Permissions;
using System.Globalization;
using System.Runtime.Serialization;
using System.Collections;
using System.Collections.Specialized;
using System.Text;
using System.Configuration.Provider;
using System.Configuration;
using System.Web.DataAccess;
using System.Web.Hosting;
using System.Web.Util;

#endregion

using ${wizard.solution.name}.Domain;
using ${wizard.solution.name}.VO;
using ${wizard.solution.name}.Service;

namespace ${wizard.projects.web.common.name}
{
	class DomainRoleProvider : RoleProvider
	{

		#region Member variables

		private string _AppName;
        private IMembershipService _membershipService = null;

        #endregion

		#region Properties

		public override string ApplicationName
		{
			get { return _AppName; }
			set
			{
				if (_AppName != value)
				{
					_AppName = value;
				}
			}
		}

        private IMembershipService MembershipService
        {
            get
            {
                if (_membershipService == null)
                {
                    _membershipService = new MembershipService();
                }
                return _membershipService;
            }
        }

		#endregion

		#region Initialization

		public override void Initialize(string name, NameValueCollection config)
		{
			if (config == null)
			{
				throw new ArgumentNullException("config");
			}
			if (string.IsNullOrEmpty(name))
			{
				name = "DomainRoleProvider";
			}
			if (string.IsNullOrEmpty(config["description"]))
			{
				config.Remove("description");
				config.Add("description", "$safeprojectname$ Role Provider");
			}
			base.Initialize(name, config);

			_AppName = config["applicationName"];
			if (string.IsNullOrEmpty(_AppName))
			{
				_AppName = GetDefaultAppName();
			}

			if (_AppName.Length > 255)
			{
				throw new ProviderException("Provider application name too long, max is 255.");
			}

			config.Remove("applicationName");
			config.Remove("description");
			if (config.Count > 0)
			{
				string attribUnrecognized = config.GetKey(0);
				if (!String.IsNullOrEmpty(attribUnrecognized))
				{
					throw new ProviderException("Provider unrecognized attribute: " + attribUnrecognized);
				}
			}
		}

		#endregion

		public override bool IsUserInRole(string username, string roleName)
		{
			Role role = (Role)Enum.Parse(typeof(Role), roleName);
            return MembershipService.IsUserInRole(username, role);
		}

		public override string[] GetRolesForUser(string username)
		{
            Role[] roles = MembershipService.GetRolesForUser(username);
			string[] rolestrings = new string[roles.Length];
			for (int i = 0; i < roles.Length; i++)
			{
				rolestrings[i] = roles[i].ToString();
			}
			return rolestrings;
		}

		public override void AddUsersToRoles(string[] usernames, string[] roleNames)
		{
			if (roleNames.Length > 0 && usernames.Length > 0)
			{
				foreach (string username in usernames)
				{
                    UserVO userVO = MembershipService.GetUser(username);
					string[] newRoles = new string[userVO.Roles.Length + roleNames.Length];
					userVO.Roles.CopyTo(newRoles, 0);
					int i = userVO.Roles.Length;
					for (int j = 0; j < roleNames.Length; j++)
					{
						newRoles[i++] = roleNames[j];
					}
					userVO.Roles = newRoles;
                    MembershipService.UpdateUser(userVO);
				}
			}
		}

		public override void RemoveUsersFromRoles(string[] usernames, string[] roleNames)
		{
			if (roleNames.Length > 0 && usernames.Length > 0)
			{
				foreach (string username in usernames)
				{
                    UserVO userVO = MembershipService.GetUser(username);
					ArrayList newRoles = new ArrayList();
					bool changeMade = false;
					foreach (string existingRoleName in userVO.Roles)
					{
						bool removeRole = false;

						foreach (string roleToRemove in roleNames)
						{
							if (existingRoleName.Equals(roleToRemove))
							{
								removeRole = true;
								break;
							}
						}

						if (!removeRole)
						{
							newRoles.Add(existingRoleName);
						}
						else
						{
							changeMade = true;
						}
					}

					if (changeMade)
					{
						userVO.Roles = new string[newRoles.Count];
						newRoles.CopyTo(userVO.Roles);
                        MembershipService.UpdateUser(userVO);
					}

				}
			}
		}

		public override string[] GetUsersInRole(string roleName)
		{
            return MembershipService.GetUsernamesInRole((Role)Enum.Parse(typeof(Role), roleName));
		}

		public override string[] FindUsersInRole(string roleName, string usernameToMatch)
		{
            return MembershipService.FindUsernamesInRole((Role)Enum.Parse(typeof(Role), roleName), usernameToMatch);
		}

		public override string[] GetAllRoles()
		{
			return Enum.GetNames(typeof(Role));
		}

		public override bool RoleExists(string roleName)
		{
			string[] roles = GetAllRoles();
			foreach (string role in roles)
			{
				if (role.Equals(roleName))
				{
					return true;
				}
			}
			return false;
		}

		public override void CreateRole(string roleName)
		{
			throw new System.Exception("The method or operation is not implemented.  The DomainRoleProvider does not support creating and deleting roles.  To add or remove roles please edit the model and regenerate.");
		}

		public override bool DeleteRole(string roleName, bool throwOnPopulatedRole)
		{
			throw new System.Exception("The method or operation is not implemented.  The DomainRoleProvider does not support creating and deleting roles.  To add or remove roles please edit the model and regenerate.");
		}

		#region Utility Methods

		private static string GetDefaultAppName()
		{
			try
			{
				string appName = System.Web.HttpRuntime.AppDomainAppVirtualPath;
				if (appName == null || appName.Length == 0)
				{
					return "/";
				}
				else
				{
					return appName;
				}
			}
			catch
			{
				return "/";
			}
		}

		#endregion

	}
}