// Name: UserDao.cs
// license-header cs merge-point
//
// This is only generated once! It will never be overwritten.
// You can (and have to!) safely modify it by hand.

using System;

using Iesi.Collections;

using ${wizard.solution.name}.VO;

namespace ${wizard.solution.name}.Domain
{
    /// <summary>
    /// @see MembershipExample.Domain.User
    /// </summary>
    public partial class UserDao
    {
        /// <summary>
        /// @see MembershipExample.Domain.IUserDao#ToUserVO(MembershipExample.Domain.User)
        /// </summary>
        public UserVO ToUserVO(User entity)
        {
			if (entity == null) { return null; }
			string[] roles = new string[entity.Roles.Count];
			int i = 0;
			foreach (UserRole role in entity.Roles)
			{
				roles[i++] = role.Role.ToString();
			}
			UserVO userVO = new UserVO(entity.Id,
										entity.Username,
										entity.Email,
										entity.IsActive,
										entity.Comment,
										entity.CreationDate,
										roles);
            return userVO;
        }

        /// <summary>
        /// @see MembershipExample.Domain.IUserDao#UserVOToEntity(MembershipExample.VO.UserVO)
        /// </summary>
        public User UserVOToEntity(UserVO userVO)
        {
			if (userVO == null) { return null; }
			// VO to entity conversion
			User entity = User.Factory.NewInstance();

			entity.Id = userVO.Id;
			entity.Username = userVO.Username;
			entity.Email = userVO.Email;
			entity.IsActive = userVO.IsActive;
			entity.Comment = userVO.Comment;
			entity.CreationDate = userVO.CreationDate;

			return entity;

        }

    }
}
