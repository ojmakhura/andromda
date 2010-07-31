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
        // ${toDoTag} verify behavior of toUserVO
        super.toUserVO(sourceEntity, targetVO);
        // WARNING! No conversion for targetVO.id (can't convert sourceEntity.getId():java.lang.Long to java.lang.Long
        // WARNING! No conversion for targetVO.username (can't convert sourceEntity.getUsername():java.lang.String to java.lang.String
        // WARNING! No conversion for targetVO.firstName (can't convert sourceEntity.getFirstName():java.lang.String to java.lang.String
        // WARNING! No conversion for targetVO.lastName (can't convert sourceEntity.getLastName():java.lang.String to java.lang.String
    }


    /**
     * @see org.andromda.timetracker.domain.UserDao#toUserVO(org.andromda.timetracker.domain.User)
     */
    public org.andromda.timetracker.vo.UserVO toUserVO(final org.andromda.timetracker.domain.User entity)
    {
        // ${toDoTag} verify behavior of toUserVO
        return super.toUserVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store, 
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.User loadUserFromUserVO(org.andromda.timetracker.vo.UserVO userVO)
    {
        // ${toDoTag} implement loadUserFromUserVO
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
        // ${toDoTag} verify behavior of userVOToEntity
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
        // ${toDoTag} verify behavior of userVOToEntity
        super.userVOToEntity(sourceVO, targetEntity, copyIfNull);
        // No conversion for targetEntity.username (can't convert sourceVO.getUsername():java.lang.String to java.lang.String
        // No conversion for targetEntity.firstName (can't convert sourceVO.getFirstName():java.lang.String to java.lang.String
        // No conversion for targetEntity.lastName (can't convert sourceVO.getLastName():java.lang.String to java.lang.String
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
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.User loadUserFromUserDetailsVO(org.andromda.timetracker.vo.UserDetailsVO userDetailsVO)
    {
        org.andromda.timetracker.domain.User user = null;
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
     * @see org.andromda.timetracker.domain.UserDao#userDetailsVOToEntity(org.andromda.timetracker.vo.UserDetailsVO)
     */
    public User userDetailsVOToEntity(UserDetailsVO userDetailsVO)
    {
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