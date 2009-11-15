package org.andromda.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.andromda.utils.beans.BeanSorter;
import org.andromda.utils.beans.SortCriteria;
import org.andromda.utils.beans.SortCriteria.Ordering;


/**
 * Tests {@link BeanSorter}
 *
 * @author Chad Brandon
 */
public class BeanSorterTest
    extends TestCase
{
    public void testSort()
    {
        final List persons = Arrays.asList(BeanSorterTest.persons);

        // - try simple property sorting
        List sorted = BeanSorter.sort(
            persons,
            new SortCriteria[] {new SortCriteria(
                    "firstName",
                    Ordering.ASCENDING)});
        Iterator iterator = sorted.iterator();
        Person billy =  (Person)iterator.next();
        assertEquals(billy.getFirstName(), "Billy");
        Person chad = (Person)iterator.next();
        assertEquals(chad.getFirstName(), "Chad");
        Person john = (Person)iterator.next();
        assertEquals(john.getFirstName(), "John");

        sorted = BeanSorter.sort(
            persons,
            new SortCriteria[] {new SortCriteria(
                    "firstName",
                    Ordering.DESCENDING)});
        iterator = sorted.iterator();
        john =  (Person)iterator.next();
        assertEquals(john.getFirstName(), "John");
        chad = (Person)iterator.next();
        assertEquals(chad.getFirstName(), "Chad");
        billy = (Person)iterator.next();
        assertEquals(billy.getFirstName(), "Billy");
        
        // - try nested property sorting
        sorted = BeanSorter.sort(
            persons,
            new SortCriteria[] {new SortCriteria(
                    "address.streetNumber",
                    Ordering.ASCENDING)});      
        iterator = sorted.iterator();
        john =  (Person)iterator.next();
        assertEquals(john.getFirstName(), "John");
        chad = (Person)iterator.next();
        assertEquals(chad.getFirstName(), "Chad");
        billy = (Person)iterator.next();
        assertEquals(billy.getFirstName(), "Billy");
        
        sorted = BeanSorter.sort(
            persons,
            new SortCriteria[] {new SortCriteria(
                    "address.streetNumber",
                    Ordering.DESCENDING)});      
        iterator = sorted.iterator();
        billy =  (Person)iterator.next();
        assertEquals(billy.getFirstName(), "Billy");
        chad = (Person)iterator.next();
        assertEquals(chad.getFirstName(), "Chad");
        john = (Person)iterator.next();
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

    public static class Person
    {
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

        public Address getAddress()
        {
            return address;
        }

        public void setAddress(Address address)
        {
            this.address = address;
        }

        public String getFirstName()
        {
            return firstName;
        }

        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        public String getLastName()
        {
            return lastName;
        }

        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }
    }

    public static class Address
    {
        public Address(
            final int streetNumber,
            final String streetName)
        {
            this.streetNumber = streetNumber;
            this.streetName = streetName;
        }

        private int streetNumber;
        private String streetName;

        public String getStreetName()
        {
            return streetName;
        }

        public void setStreetName(String streetName)
        {
            this.streetName = streetName;
        }

        public int getStreetNumber()
        {
            return streetNumber;
        }

        public void setStreetNumber(int streetNumber)
        {
            this.streetNumber = streetNumber;
        }
    }
}