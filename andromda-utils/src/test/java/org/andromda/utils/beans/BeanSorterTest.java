package org.andromda.utils.beans;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.andromda.utils.beans.BeanSorter;
import org.andromda.utils.beans.SortCriteria;
import org.andromda.utils.beans.SortCriteria.Ordering;
import org.junit.Test;

/**
 * Tests {@link BeanSorter}
 *
 * @author Chad Brandon
 */
public class BeanSorterTest
{
    /**
     *
     */
    @Test
    public void testSort()
    {
        final List<Person> persons = Arrays.asList(BeanSorterTest.persons);

        // - try simple property sorting
        List<Person> sorted = BeanSorter.sort(
            persons,
            new SortCriteria[] {new SortCriteria(
                    "firstName",
                    Ordering.ASCENDING)});
        Iterator<Person> iterator = sorted.iterator();
        Person billy =  iterator.next();
        assertEquals(billy.getFirstName(), "Billy");
        Person chad = iterator.next();
        assertEquals(chad.getFirstName(), "Chad");
        Person john = iterator.next();
        assertEquals(john.getFirstName(), "John");

        sorted = BeanSorter.sort(
            persons,
            new SortCriteria[] {new SortCriteria(
                    "firstName",
                    Ordering.DESCENDING)});
        iterator = sorted.iterator();
        john =  iterator.next();
        assertEquals(john.getFirstName(), "John");
        chad = iterator.next();
        assertEquals(chad.getFirstName(), "Chad");
        billy = iterator.next();
        assertEquals(billy.getFirstName(), "Billy");

        // - try nested property sorting AND in-line sorting (i.e. we are sorting
        //   "persons" without getting the returned result)
        BeanSorter.sort(
            persons,
            new SortCriteria[] {new SortCriteria(
                    "address.streetNumber",
                    Ordering.ASCENDING)});
        iterator = persons.iterator();
        john =  iterator.next();
        assertEquals(john.getFirstName(), "John");
        chad = iterator.next();
        assertEquals(chad.getFirstName(), "Chad");
        billy = iterator.next();
        assertEquals(billy.getFirstName(), "Billy");

        sorted = BeanSorter.sort(
            persons,
            new SortCriteria[] {new SortCriteria(
                    "address.streetNumber",
                    Ordering.DESCENDING)});
        iterator = sorted.iterator();
        billy =  iterator.next();
        assertEquals(billy.getFirstName(), "Billy");
        chad = iterator.next();
        assertEquals(chad.getFirstName(), "Chad");
        john = iterator.next();
        assertEquals(john.getFirstName(), "John");

    }

    private static final Person[] persons =
        new Person[]
        {
            new Person("Chad", "Brandon", new Address(
                    1234,
                    "A Street")), new Person("Billy", "Bob", new Address(
                    2323,
                    "B Street")), new Person("John", "Doe", new Address(
                    1,
                    "C Street"))
        };

    /**
     *
     */
    public static class Person
    {
        /**
         * @param firstName
         * @param lastName
         * @param address
         */
        public Person(
            final String firstName,
            final String lastName,
            final Address address)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
        }

        private String firstName;
        private String lastName;
        private Address address;

        /**
         * @return address
         */
        public Address getAddress()
        {
            return address;
        }

        /**
         * @param address
         */
        public void setAddress(Address address)
        {
            this.address = address;
        }

        /**
         * @return firstName
         */
        public String getFirstName()
        {
            return firstName;
        }

        /**
         * @param firstName
         */
        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        /**
         * @return lastName
         */
        public String getLastName()
        {
            return lastName;
        }

        /**
         * @param lastName
         */
        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }
    }

    /**
     *
     */
    public static class Address
    {
        /**
         * @param streetNumber
         * @param streetName
         */
        public Address(
            final int streetNumber,
            final String streetName)
        {
            this.streetNumber = streetNumber;
            this.streetName = streetName;
        }

        private int streetNumber;
        private String streetName;

        /**
         * @return streetName
         */
        public String getStreetName()
        {
            return streetName;
        }

        /**
         * @param streetName
         */
        public void setStreetName(String streetName)
        {
            this.streetName = streetName;
        }

        /**
         * @return streetNumber
         */
        public int getStreetNumber()
        {
            return streetNumber;
        }

        /**
         * @param streetNumber
         */
        public void setStreetNumber(int streetNumber)
        {
            this.streetNumber = streetNumber;
        }
    }
}