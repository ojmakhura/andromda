// Name: MembershipService.cs
// license-header cs merge-point
//
// This is only generated once! It will never be overwritten.
// You can (and have to!) safely modify it by hand.

using System;
using System.Collections;

using NHibernate;
using AndroMDA.NHibernateSupport;

using ${wizard.solution.name}.VO;
using ${wizard.solution.name}.Domain;

namespace ${wizard.solution.name}.Service
{
    /// <summary>
    /// @see MembershipExample.Service.MembershipService
    /// </summary>
    public partial class MembershipService
    {
        /// <summary>
        /// @see MembershipExample.Service.MembershipService#CreateUser(User)
        /// </summary>
        protected void HandleCreateUser(UserVO userVo, string password)
        {
			User entity = this.UserDao.UserVOToEntity(userVo);
			entity.Password = password;
			this.UserDao.Create(entity);
        }

		/// <summary>
		/// @see MembershipExample.Service.MembershipService#GetUserNameByEmail(string email)
		/// </summary>
		protected string HandleGetUserNameByEmail(string email)
		{
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("SELECT user.Username FROM User user WHERE user.Email = :email");
			query.SetParameter("email", email);
			return (string)query.UniqueResult();
		}

		/// <summary>
		/// @see MembershipExample.Service.MembershipService#GetUserIdByUsername(string username)
		/// </summary>
		protected long HandleGetUserIdByUsername(string username)
		{
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("SELECT user.Id FROM User user WHERE user.Username = :username");
			query.SetParameter("username", username);
			return (long)query.UniqueResult();
		}
		
		/// <summary>
        /// @see MembershipExample.Service.MembershipService#GetUser(String, bool)
        /// </summary>
        protected UserVO HandleGetUser(String username)
        {
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("FROM User user WHERE user.Username = :username");
			query.SetParameter("username", username);
			IList users = query.List();
			if (users.Count == 1)
			{
				return this.UserDao.ToUserVO((User)users[0]);
			}
			else if (users.Count == 0)
			{
				return null;
			}
			else
			{
				throw new Exception("More than one user was found with the same username");
			}
		}

        /// <summary>
        /// @see MembershipExample.Service.MembershipService#GetUser(long, bool)
        /// </summary>
		protected UserVO HandleGetUser(long userId)
        {
			User u = UserDao.Load(userId);
			return this.UserDao.ToUserVO(u);
		}

        /// <summary>
        /// @see MembershipExample.Service.MembershipService#GetAllUsers(int, int)
        /// </summary>
        protected System.Collections.IList HandleGetAllUsers(int pageIndex, int pageSize)
        {
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("from User");
			query.SetFirstResult(pageIndex);
			query.SetMaxResults(pageSize);
			return this.UserDao.ToUserVOList(query.List());
		}

		private bool RoleIsInList(string[] roleList, Role roleType)
		{
			foreach (string rolestring in roleList)
			{
				if (rolestring.Equals(roleType.ToString()))
				{
					return true;
				}
			}
			return false;
		}

		private UserRole RoleIsInList(Iesi.Collections.ISet set, string rolestring)
		{
			foreach (UserRole role in set)
			{
				if (rolestring.Equals(role.Role.ToString()))
				{
					return role;
				}
			}
			return null;
		}

        /// <summary>
        /// @see MembershipExample.Service.MembershipService#UpdateUser(User)
        /// </summary>
        protected void HandleUpdateUser(UserVO membershipUser)
        {
			User entity = this.UserDao.Load(membershipUser.Id);
			entity.Comment = membershipUser.Comment;
			entity.Email = membershipUser.Email;
			entity.IsActive = membershipUser.IsActive;
			
			// Synchronize roles
			if (membershipUser.Roles != null)
			{
				ArrayList rolesToRemove = new ArrayList();
				foreach (UserRole role in entity.Roles)
				{
					if (!RoleIsInList(membershipUser.Roles, role.Role))
					{
						rolesToRemove.Add(role);
					}
				}
				foreach (UserRole role in rolesToRemove)
				{
					entity.Roles.Remove(role);
				}
				foreach (string rolestring in membershipUser.Roles)
				{
					UserRole role = RoleIsInList(entity.Roles, rolestring);
					if (role == null)
					{
						role = UserRole.Factory.NewInstance();
						role.Role = (Role)Enum.Parse(typeof(Role), rolestring);
						role.User = entity;
						entity.Roles.Add(role);
					}
				}
				
			}
			this.UserDao.Update(entity);
        }
		/// <summary>
		/// @see MembershipExample.Service.MembershipService#UpdatePassword(String, String, String)
		/// </summary>
		protected bool HandleUpdatePassword(string username, string oldPassword, string newPassword)
		{
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("FROM User user WHERE user.Username = :username AND user.Password = :password");
			query.SetParameter("username", username);
			query.SetParameter("password", oldPassword);
			IList users = query.List();
			if (users.Count != 1)
			{
				return false;
			}
			User entity = (User)users[0];
			entity.Password = newPassword;
			this.UserDao.Update(entity);
			return true;
		}

        /// <summary>
        /// @see MembershipExample.Service.MembershipService#DeleteUser(String)
        /// </summary>
        protected void HandleDeleteUser(String username)
        {
			long userid = HandleGetUserIdByUsername(username);
			this.UserDao.Remove(userid);
        }

		/// <summary>
        /// @see MembershipExample.Service.MembershipService#FindUsersByEmail(String, int, int)
        /// </summary>
        protected System.Collections.IList HandleFindUsersByEmail(String emailToMatch, int pageIndex, int pageSize)
        {
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("FROM User user WHERE user.Email LIKE :email");
			query.SetParameter("email", emailToMatch);
			return this.UserDao.ToUserVOList(query.List());
		}

        /// <summary>
        /// @see MembershipExample.Service.MembershipService#FindUsersByName(String, int, int)
        /// </summary>
        protected System.Collections.IList HandleFindUsersByName(String usernameToMatch, int pageIndex, int pageSize)
        {
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("FROM User user WHERE user.Username LIKE :username");
			query.SetParameter("username", usernameToMatch);
			return this.UserDao.ToUserVOList(query.List());
		}

        /// <summary>
        /// @see MembershipExample.Service.MembershipService#ValidateUser(String, String)
        /// </summary>
		protected UserVO HandleValidateUser(String username, String password)
        {
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("FROM User user WHERE user.Username = :username AND user.Password = :password");
			query.SetParameter("username", username);
			query.SetParameter("password", password);
			IList users = query.List();
			if (users.Count == 1)
			{
				return this.UserDao.ToUserVO((User)users[0]);
			}
			else if (users.Count == 0)
			{
				return null;
			}
			else
			{
				throw new Exception("More than one user was found with the same username and password combination");
			}
		}

		/// <summary>
		/// @see MembershipExample.Service.MembershipService#GetRolesForUser(long? userId)
		/// </summary>
		protected Role[] HandleGetRolesForUser(long userId)
		{
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("FROM UserRole role WHERE role.User.Id = :userid");
			query.SetParameter("userid", userId);
			IList results = query.List();
			Role[] roles = new Role[results.Count];
			for (int i = 0; i < results.Count; i++)
			{
				roles[i] = ((UserRole)results[i]).Role;
			}
			return roles;
		}

		/// <summary>
		/// @see MembershipExample.Service.MembershipService#GetRolesForUser(string username)
		/// </summary>
		protected Role[] HandleGetRolesForUser(string username)
		{
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("FROM UserRole role WHERE role.User.Username = :username");
			query.SetParameter("username", username);
			IList results = query.List();
			Role[] roles = new Role[results.Count];
			for (int i = 0; i < results.Count; i++)
			{
				roles[i] = ((UserRole)results[i]).Role;
			}
			return roles;
		}

		/// <summary>
		/// @see MembershipExample.Service.MembershipService#GetUsernamesInRole(RoleType role)
		/// </summary>
		protected string[] HandleGetUsernamesInRole(Role role)
		{
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("SELECT role.User.Username FROM UserRole role WHERE role.Role = :roletype");
			query.SetParameter("roletype", (int)role);
			IList usernameList = query.List();
			Array usernames = Array.CreateInstance(typeof(string), usernameList.Count);
			usernameList.CopyTo(usernames, 0);
			return (string[])usernames;
		}

		/// <summary>
		/// @see MembershipExample.Service.MembershipService#FindUsernamesInRole(RoleType role, string usernameToMatch)
		/// </summary>
		protected string[] HandleFindUsernamesInRole(Role role, string usernameToMatch)
		{
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("SELECT role.User.Username FROM UserRole role WHERE role.Role = :roletype AND role.User.Username LIKE :usernameToMatch");
			query.SetParameter("roletype", (int)role); 
			query.SetParameter("usernameToMatch", usernameToMatch);
			IList usernameList = query.List();
			Array usernames = Array.CreateInstance(typeof(string), usernameList.Count);
			usernameList.CopyTo(usernames, 0);
			return (string[])usernames;
		}

		/// <summary>
		/// @see MembershipExample.Service.MembershipService#IsUserInRole(User user, RoleType role)
		/// </summary>
		protected bool HandleIsUserInRole(UserVO user, Role role)
		{
			foreach (string currole in user.Roles)
			{
				if (currole.Equals(role.ToString()))
				{
					return true;
				}
			}
			return false;
		}

		/// <summary>
		/// @see MembershipExample.Service.MembershipService#IsUserInRole(string username, RoleType role)
		/// </summary>
		protected bool HandleIsUserInRole(string username, Role role)
		{
			ISession session = SessionManagerFactory.SessionManager.Session;
			IQuery query = session.CreateQuery("SELECT role.Id FROM UserRole role WHERE role.User.Username = :username AND role.Role = :roletype");
			query.SetParameter("username", username);
			query.SetParameter("roletype", (int)role);
			IList roles = query.List();
			return roles.Count > 0;	
		}

    }
}
