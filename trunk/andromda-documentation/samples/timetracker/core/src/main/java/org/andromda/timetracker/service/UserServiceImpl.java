// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.service;

import java.util.Collection;
import org.andromda.timetracker.domain.UserDao;
import org.andromda.timetracker.vo.UserVO;

/**
 * @see org.andromda.timetracker.service.UserService
 */
public class UserServiceImpl
    extends UserServiceBase
{
    /**
     * @return userVOs
     * @throws Exception 
     * @see org.andromda.timetracker.service.UserService#getAllUsers()
     */
    // Use this implementation for UML2 model with returnParameter * multiplicity
    /*@SuppressWarnings("unchecked")
    protected Collection<UserVO> handleGetAllUsers()
        throws Exception
    {
        return (Collection<UserVO>) getUserDao().loadAll(UserDao.TRANSFORM_USERVO);
    */
    @Override
    protected UserVO[] handleGetAllUsers()
        throws Exception
    {
        Collection userVOs = getUserDao().loadAll(UserDao.TRANSFORM_USERVO);
        return (UserVO[]) userVOs.toArray(new UserVO[userVOs.size()]);
    }
}