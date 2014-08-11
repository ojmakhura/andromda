// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import org.andromda.timetracker.vo.UserDetailsVO;
import org.andromda.timetracker.vo.UserRoleVO;
import org.andromda.timetracker.vo.UserVO;

/**
 * @see User
 */
public class UserDaoImpl
    extends UserDaoBase
{
    /**
     * @see UserDao#toUserVO(User, org.andromda.timetracker.vo.UserVO)
     */
    @Override
    public void toUserVO(
        User sourceEntity,
        UserVO targetVO)
    {
        // TODO verify behavior of toUserVO
        super.toUserVO(sourceEntity, targetVO);
        // WARNING! No conversion for targetVO.id (can't convert sourceEntity.getId():java.lang.Long to java.lang.Long
        // WARNING! No conversion for targetVO.username (can't convert sourceEntity.getUsername():java.lang.String to java.lang.String
        // WARNING! No conversion for targetVO.firstName (can't convert sourceEntity.getFirstName():java.lang.String to java.lang.String
        // WARNING! No conversion for targetVO.lastName (can't convert sourceEntity.getLastName():java.lang.String to java.lang.String
    }

    /**
     * @see UserDao#toUserVO(User)
     */
    @Override
    public UserVO toUserVO(final User entity)
    {
        // TODO verify behavior of toUserVO
        return super.toUserVO(entity);
    }

    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private User loadUserFromUserVO(UserVO userVO)
    {
        // TODO implement loadUserFromUserVO
        throw new java.lang.UnsupportedOperationException("loadUserFromUserVO(UserVO) not yet implemented.");

        /* A typical implementation looks like this:
        User user = this.load(userVO.getId());
        if (user == null)
        {
            user = User.Factory.newInstance();
        }
        return user;
        */
    }

    /**
     * @see UserDao#userVOToEntity(UserVO)
     */
    @Override
    public User userVOToEntity(UserVO userVO)
    {
        // TODO verify behavior of userVOToEntity
        User entity = this.loadUserFromUserVO(userVO);
        this.userVOToEntity(userVO, entity, true);
        return entity;
    }

    /**
     * @see UserDao#userVOToEntity(UserVO, User, boolean)
     */
    @Override
    public void userVOToEntity(
        UserVO sourceVO,
        User targetEntity,
        boolean copyIfNull)
    {
        // TODO verify behavior of userVOToEntity
        super.userVOToEntity(sourceVO, targetEntity, copyIfNull);
        // No conversion for targetEntity.username (can't convert sourceVO.getUsername():java.lang.String to java.lang.String
        // No conversion for targetEntity.firstName (can't convert sourceVO.getFirstName():java.lang.String to java.lang.String
        // No conversion for targetEntity.lastName (can't convert sourceVO.getLastName():java.lang.String to java.lang.String
    }

    /**
     * @see UserDao#toUserDetailsVO(User, UserDetailsVO)
     */
    @Override
    public void toUserDetailsVO(
        User sourceEntity,
        UserDetailsVO targetVO)
    {
        super.toUserDetailsVO(sourceEntity, targetVO);

        // Convert roles
        Collection<UserRole> srcRoles = sourceEntity.getRoles();
        UserRoleVO[] targetRoles = new UserRoleVO[srcRoles.size()];
        int i=0;
        for (Object srcRole : srcRoles)
        {
            targetRoles[i] = getUserRoleDao().toUserRoleVO((UserRole)srcRole);
            i++;
        }
        targetVO.setRoles(targetRoles);
    }

    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private User loadUserFromUserDetailsVO(UserDetailsVO userDetailsVO)
    {
        User user = null;
        if (userDetailsVO != null && userDetailsVO.getId() != null)
        {
            try
            {
                user = this.load(userDetailsVO.getId());
            }
            catch (UserDaoException e)
            {
                // user id no set - OK to ignore
            }
        }
        if (user == null)
        {
            user = new User();
        }
        return user;
    }

    /**
     * @see UserDao#userDetailsVOToEntity(UserDetailsVO)
     */
    @Override
    public User userDetailsVOToEntity(UserDetailsVO userDetailsVO)
    {
        User entity = this.loadUserFromUserDetailsVO(userDetailsVO);
        this.userDetailsVOToEntity(userDetailsVO, entity, true);
        return entity;
    }

    /**
     * @see UserDao#userDetailsVOToEntity(UserDetailsVO, User, boolean)
     */
    @Override
    public void userDetailsVOToEntity(
        UserDetailsVO sourceVO,
        User targetEntity,
        boolean copyIfNull)
    {
        // @todo verify behavior of userDetailsVOToEntity
        super.userDetailsVOToEntity(sourceVO, targetEntity, copyIfNull);

        if (sourceVO.getRoles().length > 0)
        {
            Set<UserRole> roles = new TreeSet<UserRole>();

            for (UserRoleVO userRoleVO : sourceVO.getRoles())
            {
                System.out.println(" user role : " + userRoleVO.getRole());
                roles.add(this.getUserRoleDao().userRoleVOToEntity(userRoleVO));
            }
            targetEntity.setRoles(roles);
        }
    }
}
