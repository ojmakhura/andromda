// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import java.util.Collection;

import org.andromda.timetracker.vo.UserRoleVO;

/**
 * @see org.andromda.timetracker.domain.User
 */
public class UserDaoImpl
    extends org.andromda.timetracker.domain.UserDaoBase
{
    /**
     * @see org.andromda.timetracker.domain.UserDao#toUserVO(org.andromda.timetracker.domain.User, org.andromda.timetracker.vo.UserVO)
     */
    public void toUserVO(
        org.andromda.timetracker.domain.User sourceEntity,
        org.andromda.timetracker.vo.UserVO targetVO)
    {
        // @todo verify behavior of toUserVO
        super.toUserVO(sourceEntity, targetVO);
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#toUserVO(org.andromda.timetracker.domain.User)
     */
    public org.andromda.timetracker.vo.UserVO toUserVO(final org.andromda.timetracker.domain.User entity)
    {
        // @todo verify behavior of toUserVO
        return super.toUserVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.User loadUserFromUserVO(org.andromda.timetracker.vo.UserVO userVO)
    {
        // @todo implement loadUserFromUserVO
        throw new java.lang.UnsupportedOperationException("org.andromda.timetracker.domain.loadUserFromUserVO(org.andromda.timetracker.vo.UserVO) not yet implemented.");

        /* A typical implementation looks like this:
        org.andromda.timetracker.domain.User user = this.load(userVO.getId());
        if (user == null)
        {
            user = org.andromda.timetracker.domain.User.Factory.newInstance();
        }
        return user;
        */
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#userVOToEntity(org.andromda.timetracker.vo.UserVO)
     */
    public org.andromda.timetracker.domain.User userVOToEntity(org.andromda.timetracker.vo.UserVO userVO)
    {
        // @todo verify behavior of userVOToEntity
        org.andromda.timetracker.domain.User entity = this.loadUserFromUserVO(userVO);
        this.userVOToEntity(userVO, entity, true);
        return entity;
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#userVOToEntity(org.andromda.timetracker.vo.UserVO, org.andromda.timetracker.domain.User)
     */
    public void userVOToEntity(
        org.andromda.timetracker.vo.UserVO sourceVO,
        org.andromda.timetracker.domain.User targetEntity,
        boolean copyIfNull)
    {
        // @todo verify behavior of userVOToEntity
        super.userVOToEntity(sourceVO, targetEntity, copyIfNull);
    }

    /**
     * @see org.andromda.timetracker.domain.UserDao#toUserDetailsVO(org.andromda.timetracker.domain.User, org.andromda.timetracker.vo.UserDetailsVO)
     */
    public void toUserDetailsVO(
        org.andromda.timetracker.domain.User sourceEntity,
        org.andromda.timetracker.vo.UserDetailsVO targetVO)
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
     * @see org.andromda.timetracker.domain.UserDao#toUserDetailsVO(org.andromda.timetracker.domain.User)
     */
    public org.andromda.timetracker.vo.UserDetailsVO toUserDetailsVO(final org.andromda.timetracker.domain.User entity)
    {
        // @todo verify behavior of toUserDetailsVO
        return super.toUserDetailsVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.User loadUserFromUserDetailsVO(org.andromda.timetracker.vo.UserDetailsVO userDetailsVO)
    {
        // @todo implement loadUserFromUserDetailsVO
        throw new java.lang.UnsupportedOperationException("org.andromda.timetracker.domain.loadUserFromUserDetailsVO(org.andromda.timetracker.vo.UserDetailsVO) not yet implemented.");

        /* A typical implementation looks like this:
        org.andromda.timetracker.domain.User user = this.load(userDetailsVO.getId());
        if (user == null)
        {
            user = org.andromda.timetracker.domain.User.Factory.newInstance();
        }
        return user;
        */
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#userDetailsVOToEntity(org.andromda.timetracker.vo.UserDetailsVO)
     */
    public org.andromda.timetracker.domain.User userDetailsVOToEntity(org.andromda.timetracker.vo.UserDetailsVO userDetailsVO)
    {
        // @todo verify behavior of userDetailsVOToEntity
        org.andromda.timetracker.domain.User entity = this.loadUserFromUserDetailsVO(userDetailsVO);
        this.userDetailsVOToEntity(userDetailsVO, entity, true);
        return entity;
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#userDetailsVOToEntity(org.andromda.timetracker.vo.UserDetailsVO, org.andromda.timetracker.domain.User)
     */
    public void userDetailsVOToEntity(
        org.andromda.timetracker.vo.UserDetailsVO sourceVO,
        org.andromda.timetracker.domain.User targetEntity,
        boolean copyIfNull)
    {
        // @todo verify behavior of userDetailsVOToEntity
        super.userDetailsVOToEntity(sourceVO, targetEntity, copyIfNull);
    }

    protected User handleGetUserDetails(String username) throws Exception {

        User user = (User)getSession().createQuery(
            "from org.andromda.timetracker.domain.User user left join fetch user.roles where user.username = :username")
            .setParameter("username", username)
            .uniqueResult();
        return user;
    }
}