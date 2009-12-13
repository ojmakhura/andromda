// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import java.util.Collection;
import org.andromda.timetracker.vo.UserRoleVO;
import org.andromda.timetracker.vo.UserDetailsVO;
import org.andromda.timetracker.vo.UserVO;

/**
 * @see User
 */
public class UserDaoImpl
    extends UserDaoBase
{
    /**
     * @see org.andromda.timetracker.domain.UserDao#toUserVO(User, UserVO)
     */
    public void toUserVO(
        User sourceEntity,
        UserVO targetVO)
    {
        // @todo verify behavior of toUserVO
        super.toUserVO(sourceEntity, targetVO);
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#toUserVO(User)
     */
    public UserVO toUserVO(final User entity)
    {
        // @todo verify behavior of toUserVO
        return super.toUserVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private User loadUserFromUserVO(UserVO userVO)
    {
        // @todo implement loadUserFromUserVO
        throw new UnsupportedOperationException("org.andromda.timetracker.domain.loadUserFromUserVO(UserVO) not yet implemented.");

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
     * @see org.andromda.timetracker.domain.UserDao#userVOToEntity(UserVO)
     */
    public User userVOToEntity(UserVO userVO)
    {
        // @todo verify behavior of userVOToEntity
        User entity = this.loadUserFromUserVO(userVO);
        this.userVOToEntity(userVO, entity, true);
        return entity;
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#userVOToEntity(UserVO, User)
     */
    public void userVOToEntity(
        UserVO sourceVO,
        User targetEntity,
        boolean copyIfNull)
    {
        // @todo verify behavior of userVOToEntity
        super.userVOToEntity(sourceVO, targetEntity, copyIfNull);
    }

    /**
     * @see org.andromda.timetracker.domain.UserDao#toUserDetailsVO(User, UserDetailsVO)
     */
    public void toUserDetailsVO(
        User sourceEntity,
        UserDetailsVO targetVO)
    {
        super.toUserDetailsVO(sourceEntity, targetVO);

        // Convert roles
        Collection srcRoles = sourceEntity.getRoles();
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
     * @see org.andromda.timetracker.domain.UserDao#toUserDetailsVO(User)
     */
    public UserDetailsVO toUserDetailsVO(final User entity)
    {
        // @todo verify behavior of toUserDetailsVO
        return super.toUserDetailsVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private User loadUserFromUserDetailsVO(UserDetailsVO userDetailsVO)
    {
        // @todo implement loadUserFromUserDetailsVO
        throw new UnsupportedOperationException("org.andromda.timetracker.domain.loadUserFromUserDetailsVO(UserDetailsVO) not yet implemented.");

        /* A typical implementation looks like this:
        User user = this.load(userDetailsVO.getId());
        if (user == null)
        {
            user = User.Factory.newInstance();
        }
        return user;
        */
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#userDetailsVOToEntity(UserDetailsVO)
     */
    public User userDetailsVOToEntity(UserDetailsVO userDetailsVO)
    {
        // @todo verify behavior of userDetailsVOToEntity
        User entity = this.loadUserFromUserDetailsVO(userDetailsVO);
        this.userDetailsVOToEntity(userDetailsVO, entity, true);
        return entity;
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#userDetailsVOToEntity(UserDetailsVO, User)
     */
    public void userDetailsVOToEntity(
        UserDetailsVO sourceVO,
        User targetEntity,
        boolean copyIfNull)
    {
        // @todo verify behavior of userDetailsVOToEntity
        super.userDetailsVOToEntity(sourceVO, targetEntity, copyIfNull);
    }

    protected User handleGetUserDetails(String username) throws Exception {

        User user = (User)getSession().createQuery(
            "from User user left join fetch user.roles where user.username = :username")
            .setParameter("username", username)
            .uniqueResult();
        return user;
    }
}
