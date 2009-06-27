// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.service;

import java.util.Collection;

import org.andromda.timetracker.domain.Person;
import org.andromda.timetracker.domain.PersonDao;
import org.andromda.timetracker.vo.PersonVO;

/**
 * @see org.andromda.timetracker.service.PeopleService
 */
public class PeopleServiceImpl
    extends org.andromda.timetracker.service.PeopleServiceBase
{
    /**
     * @see org.andromda.timetracker.service.PeopleService#createPerson(org.andromda.timetracker.vo.PersonVO)
     */
    protected java.lang.Long handleCreatePerson(org.andromda.timetracker.vo.PersonVO personVO)
        throws java.lang.Exception
    {
        Person person = Person.Factory.newInstance();
        getPersonDao().personVOToEntity(personVO, person, true);
        getPersonDao().create(person);
        return person.getId();
    }

    /**
     * @see org.andromda.timetracker.service.PeopleService#getPerson(java.lang.Long)
     */
    protected org.andromda.timetracker.vo.PersonVO handleGetPerson(java.lang.Long id)
        throws java.lang.Exception
    {
        return (PersonVO)getPersonDao().load(PersonDao.TRANSFORM_PERSONVO, id);
    }

    /**
     * @see org.andromda.timetracker.service.PeopleService#getAllPeople()
     */
    protected org.andromda.timetracker.vo.PersonVO[] handleGetAllPeople()
        throws java.lang.Exception
    {
        Collection people = getPersonDao().loadAll(PersonDao.TRANSFORM_PERSONVO);
        return (PersonVO[])people.toArray(new PersonVO[people.size()]);
    }
}