//
// DomainMembershipUser
//

#region Using statements

using System;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.Web;
using System.Web.Configuration;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Web.UI.HtmlControls;

#endregion

using ${wizard.solution.name}.VO;

namespace ${wizard.projects.web.common.name}
{
	[Serializable]
	public class DomainMembershipUser : MembershipUser
	{

		private UserVO _userVO;

		public UserVO UserVO
		{
			get { return _userVO; }
			set { this._userVO = value; }
		}

		protected DomainMembershipUser()
		{
		}

		public DomainMembershipUser(string providername,
									string username,
									object providerUserKey,
									string email,
									string passwordQuestion,
									string comment,
									bool isApproved,
									bool isLockedOut,
									DateTime creationDate,
									DateTime lastLoginDate,
									DateTime lastActivityDate,
									DateTime lastPasswordChangedDate,
									DateTime lastLockedOutDate,
									UserVO userVO)
			:
								  base(providername,
									   username,
									   providerUserKey,
									   email,
									   passwordQuestion,
									   comment,
									   isApproved,
									   isLockedOut,
									   creationDate,
									   lastLoginDate,
									   lastActivityDate,
									   lastPasswordChangedDate,
									   lastLockedOutDate)
		{
			this.UserVO = userVO;
			
		}

	}
}