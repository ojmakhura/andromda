// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.domain;

/**
 * @see org.andromda.timetracker.domain.UserRole
 */
public class UserRoleDaoImpl
    extends org.andromda.timetracker.domain.UserRoleDaoBase
{
    /**
     * @see org.andromda.timetracker.domain.UserRoleDao#toUserRoleVO(org.andromda.timetracker.domain.UserRole, org.andromda.timetracker.vo.UserRoleVO)
     */
    public void toUserRoleVO(
        org.andromda.timetracker.domain.UserRole sourceEntity, 
        org.andromda.timetracker.vo.UserRoleVO targetVO)
    {
        // ${toDoTag} verify behavior of toUserRoleVO
        super.toUserRoleVO(sourceEntity, targetVO);
    }


    /**
     * @see org.andromda.timetracker.domain.UserRoleDao#toUserRoleVO(org.andromda.timetracker.domain.UserRole)
     */
    public org.andromda.timetracker.vo.UserRoleVO toUserRoleVO(final org.andromda.timetracker.domain.UserRole entity)
    {
        // ${toDoTag} verify behavior of toUserRoleVO
        return super.toUserRoleVO(entity);
    }


    /**
     * Retrieves the entity object that is associated with the specified value object
     * from the object store. If no such entity object exists in the object store, 
     * a new, blank entity is created
     */
    private org.andromda.timetracker.domain.UserRole loadUserRoleFromUserRoleVO(org.andromda.timetracker.vo.UserRoleVO userRoleVO)
    {     
        org.andromda.timetracker.domain.UserRole userRole = null;
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
     * @see org.andromda.timetracker.domain.UserRoleDao#userRoleVOToEntity(org.andromda.timetracker.vo.UserRoleVO)
     */
    public org.andromda.timetracker.domain.UserRole userRoleVOToEntity(org.andromda.timetracker.vo.UserRoleVO userRoleVO)
    {
        // ${toDoTag} verify behavior of userRoleVOToEntity
        org.andromda.timetracker.domain.UserRole entity = this.loadUserRoleFromUserRoleVO(userRoleVO);
        this.userRoleVOToEntity(userRoleVO, entity, true);
        return entity;
    }


    /**
     * @see org.andromda.timetracker.domain.UserRoleDao#userRoleVOToEntity(org.andromda.timetracker.vo.UserRoleVO, org.andromda.timetracker.domain.UserRole)
     */
    public void userRoleVOToEntity(
        org.andromda.timetracker.vo.UserRoleVO sourceVO,
        org.andromda.timetracker.domain.UserRole targetEntity,
        boolean copyIfNull)
    {
        // ${toDoTag} verify behavior of userRoleVOToEntity
        super.userRoleVOToEntity(sourceVO, targetEntity, copyIfNull);
    }

}