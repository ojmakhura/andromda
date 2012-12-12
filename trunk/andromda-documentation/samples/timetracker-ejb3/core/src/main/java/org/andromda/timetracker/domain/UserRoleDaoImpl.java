// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

import org.andromda.timetracker.vo.UserRoleVO;

/**
 * @see UserRole
 */
public class UserRoleDaoImpl
    extends UserRoleDaoBase
{
    /**
     * @see UserRoleDao#toUserRoleVO(UserRole, UserRoleVO)
     */
    @Override
    public void toUserRoleVO(
        UserRole sourceEntity,
        UserRoleVO targetVO)
    {
        // TODO verify behavior of toUserRoleVO
        super.toUserRoleVO(sourceEntity, targetVO);
    }

    /**
     * @see UserRoleDao#toUserRoleVO(UserRole)
     */
    @Override
    public UserRoleVO toUserRoleVO(final UserRole entity)
    {
        // TODO verify behavior of toUserRoleVO
        return super.toUserRoleVO(entity);
    }

    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store,
     * a new, blank entity is created
     */
    private UserRole loadUserRoleFromUserRoleVO(UserRoleVO userRoleVO)
    {
        UserRole userRole = null;
        if (userRoleVO != null && userRoleVO.getId() != null)
        {
            try
            {
                userRole = this.load(userRoleVO.getId());
            }
            catch (UserRoleDaoException e)
            {
               // ok to continue
            }
        }
        if (userRole == null)
        {
            userRole = new UserRole();
        }
        return userRole;
    }

    /**
     * @see UserRoleDao#userRoleVOToEntity(UserRoleVO)
     */
    @Override
    public UserRole userRoleVOToEntity(UserRoleVO userRoleVO)
    {
        // TODO verify behavior of userRoleVOToEntity
        UserRole entity = this.loadUserRoleFromUserRoleVO(userRoleVO);
        this.userRoleVOToEntity(userRoleVO, entity, true);
        return entity;
    }

    /**
     * @see UserRoleDao#userRoleVOToEntity(UserRoleVO, UserRole, boolean)
     */
    @Override
    public void userRoleVOToEntity(
        UserRoleVO sourceVO,
        UserRole targetEntity,
        boolean copyIfNull)
    {
        // TODO verify behavior of userRoleVOToEntity
        super.userRoleVOToEntity(sourceVO, targetEntity, copyIfNull);
    }
}
