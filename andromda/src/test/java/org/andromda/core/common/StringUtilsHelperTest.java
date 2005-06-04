package org.andromda.core.common;

import junit.framework.TestCase;


/**
 * JUnit test for {@link org.andromda.core.common.StringUtilsHelper}
 *
 * @author Wouter Zoons
 */
public class StringUtilsHelperTest
    extends TestCase
{
    public StringUtilsHelperTest(String name)
    {
        super(name);
    }

    public void testReplaceSuffix()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"EntityHibernate", "Hibernate", "EJB", "EntityEJB"},
                new String[] {"EntityHibernate", "Hibernate", "Hibernate", "EntityHibernate"},
                new String[] {"EntityHibernate", "hibernate", "EJB", "EntityHibernate"},
                new String[] {"EntityHibernate", "Entity", "EJB", "EntityHibernate"},
                new String[] {"EntityHibernate", "ernate", "qwErty", "EntityHibqwErty"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.replaceSuffix(strings[0], strings[1], strings[2]),
                strings[3]);
        }
    }

    public void testUpperCamelCaseName()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"ejb", "Ejb"}, new String[] {"EJB", "EJB"}, new String[] {"an EJB class", "AnEJBClass"},
                new String[] {"an EJB Class", "AnEJBClass"}, new String[] {"HibernateEntity", "HibernateEntity"},
                new String[] {"Hibernate Entity", "HibernateEntity"},
                new String[] {"Welcome... to the jungle (Guns \'n\' Roses)", "WelcomeToTheJungleGunsNRoses"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.upperCamelCaseName(strings[0]),
                strings[1]);
        }
    }

    public void testLowerCamelCaseName()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"ejb", "ejb"}, new String[] {"EJB", "eJB"}, new String[] {"an EJB class", "anEJBClass"},
                new String[] {"an EJB Class", "anEJBClass"}, new String[] {"HibernateEntity", "hibernateEntity"},
                new String[] {"Hibernate Entity", "hibernateEntity"},
                new String[] {"Welcome... to the jungle (Guns \'n\' Roses)", "welcomeToTheJungleGunsNRoses"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.lowerCamelCaseName(strings[0]),
                strings[1]);
        }
    }

    public void testToWebFileName()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"ejb", "ejb"}, new String[] {"EJB", "ejb"}, new String[] {"an EJB class", "an-ejb-class"},
                new String[] {"an EJB Class", "an-ejb-class"}, new String[] {"HibernateEntity", "hibernate-entity"},
                new String[] {"Hibernate Entity", "hibernate-entity"},
                new String[] {"Welcome... to the jungle (Guns \'n\' Roses)", "welcome-to-the-jungle-guns-n-roses"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.toWebFileName(strings[0]),
                strings[1]);
        }
    }

    public void testToResourceMessageKey()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"ejb", "ejb"}, new String[] {"EJB", "ejb"}, new String[] {"an EJB class", "an.ejb.class"},
                new String[] {"an EJB Class", "an.ejb.class"}, new String[] {"HibernateEntity", "hibernate.entity"},
                new String[] {"Hibernate Entity", "hibernate.entity"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.toResourceMessageKey(strings[0]),
                strings[1]);
        }
    }

    public void testToPhrase()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"ejb", "Ejb"}, new String[] {"EJB", "EJB"}, new String[] {"an EJB class", "An EJB class"},
                new String[] {"an EJB Class", "An EJB Class"}, new String[] {"HibernateEntity", "Hibernate Entity"},
                new String[] {"Hibernate Entity", "Hibernate Entity"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.toPhrase(strings[0]),
                strings[1]);
        }
    }

    public void testPrefixWithAPredicate()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"a", "an a"}, new String[] {"b", "a b"}, new String[] {"c", "a c"},
                new String[] {"d", "a d"}, new String[] {"e", "an e"}, new String[] {"f", "a f"},
                new String[] {"g", "a g"}, new String[] {"h", "a h"}, new String[] {"i", "an i"},
                new String[] {"j", "a j"}, new String[] {"k", "a k"}, new String[] {"l", "a l"},
                new String[] {"m", "a m"}, new String[] {"n", "a n"}, new String[] {"o", "an o"},
                new String[] {"p", "a p"}, new String[] {"q", "a q"}, new String[] {"r", "a r"},
                new String[] {"s", "a s"}, new String[] {"t", "a t"}, new String[] {"u", "a u"},
                new String[] {"v", "a v"}, new String[] {"w", "a w"}, new String[] {"x", "a x"},
                new String[] {"y", "a y"}, new String[] {"z", "a z"}, new String[] {"elephant", "an elephant"},
                new String[] {"cat", "a cat"}, new String[] {"horse", "a horse"},
                new String[] {"building", "a building"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.prefixWithAPredicate(strings[0]),
                strings[1]);
        }
    }

    public void testToSingleLine()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"", ""}, new String[] {"", ""}, new String[] {" ", ""}, new String[] {"\n", ""},
                new String[] {" \t ", ""}, new String[] {"null", "null"},
                new String[] {"\r\ntest \nthis\n", "test this"}, new String[] {"word", "word"},
                new String[] {" horse ", "horse"}, new String[] {" clean me   up   ", "clean me up"},
                new String[] {"\n\n\r\n\n\n   ", ""},
                new String[] {"This is\na multiline\n\n?", "This is a multiline ?"},
                new String[] {"This is \na multiline\n\n?", "This is a multiline ?"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.toSingleLine(strings[0]),
                strings[1]);
        }
    }

    public void testRemoveLastOccurence()
    {
        String testString = "This is a test string ending with a comma";
        String someString = testString + ",  ";
        assertEquals(
            StringUtilsHelper.removeLastOccurrence(someString, ","),
            testString + "  ");
    }

    public void testPluralize()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {null, ""}, new String[] {"", ""}, new String[] {"               ", ""},
                new String[] {"key", "keys"}, new String[] {"word", "words"}, new String[] {"property", "properties"},
                new String[] {"bus", "busses"}, new String[] {"cross", "crosses"}, new String[] {"lackey", "lackeys"},
                new String[] {"noun", "nouns"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.pluralize(strings[0]),
                strings[1]);
        }
    }

    public void testSeparate()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"Transfer from a Critical Access Hospital", "Transfer_from_a_Critical_Access_Hospital"}
            };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(
                StringUtilsHelper.separate(strings[0], "_"),
                strings[1]);
        }
    }
}