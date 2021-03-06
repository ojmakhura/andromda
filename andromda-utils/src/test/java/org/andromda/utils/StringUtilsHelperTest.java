package org.andromda.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * JUnit test for {@link org.andromda.utils.StringUtilsHelper}
 *
 * @author Wouter Zoons
 */
public class StringUtilsHelperTest
{
    /**
     *
     */
    @Test
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

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.replaceSuffix(strings[0], strings[1], strings[2]),
                    strings[3]);
        }
    }

    /**
     *
     */
    @Test
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

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.upperCamelCaseName(strings[0]),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
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

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.lowerCamelCaseName(strings[0]),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
    public void testToResourceMessageKey()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"ejb", "ejb"}, new String[] {"EJB", "ejb"}, new String[] {"an EJB class", "an.ejb.class"},
                new String[] {"an EJB Class", "an.ejb.class"}, new String[] {"HibernateEntity", "hibernate.entity"},
                new String[] {"Hibernate Entity", "hibernate.entity"}
            };

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.toResourceMessageKey(strings[0]),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
    public void testToPhrase()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"ejb", "Ejb"}, new String[] {"EJB", "EJB"}, new String[] {"an EJB class", "An EJB class"},
                new String[] {"an EJB Class", "An EJB Class"}, new String[] {"HibernateEntity", "Hibernate Entity"},
                new String[] {"Hibernate Entity", "Hibernate Entity"}
            };

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.toPhrase(strings[0]),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
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

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.prefixWithAPredicate(strings[0]),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
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

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.toSingleLine(strings[0]),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
    public void testRemoveLastOccurence()
    {
        final String testString = "This is a test string ending with a comma";
        final String someString = testString + ",  ";
        assertEquals(
            StringUtilsHelper.removeLastOccurrence(someString, ","),
            testString + "  ");
    }

    /**
     *
     */
    @Test
    public void testPluralize()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {null, ""}, new String[] {"", ""}, new String[] {"               ", ""},
                new String[] {"key", "keys"}, new String[] {"word", "words"}, new String[] {"property", "properties"},
                new String[] {"bus", "buses"}, new String[] {"cross", "crosses"}, new String[] {"girl", "girls"},
                new String[] {"noun", "nouns"}, new String[] {"knife", "knives"}, new String[] {"child", "children"},
                new String[] {"person", "people"}, new String[] {"foot", "feet"}, new String[] {"woman", "women"},
                new String[] {"elf", "elves"}, new String[] {"series", "series"},
                new String[] {"keys", "keys"}, new String[] {"words", "words"}, new String[] {"properties", "properties"},
                new String[] {"buses", "buses"}, new String[] {"crosses", "crosses"}, new String[] {"girls", "girls"},
                new String[] {"nouns", "nouns"}, new String[] {"knives", "knives"}, new String[] {"children", "children"},
                new String[] {"people", "people"}, new String[] {"feet", "feet"}, new String[] {"women", "women"}, new String[] {"elves", "elves"},
            };

        for (String[] strings : fixture)
        {
            String plural = StringUtilsHelper.pluralize(strings[0]);
            assertEquals(plural + "!=" + strings[1],
                    plural,
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
    public void testSeparate()
    {
        final String[][] fixture =
            new String[][]
            {
                new String[] {"Transfer from a Critical Access Hospital", "Transfer_from_a_Critical_Access_Hospital"},
                new String[] {"UNDERSCORE_TEST", "UNDERSCORE_TEST"}
            };

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.separate(strings[0], "_"),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
    public void testSimpleFormat()
    {
        final String newline = StringUtilsHelper.getLineSeparator();

        final String[][] fixture =
            new String[][]
            {
                new String[] {null, ""},
                new String[] {"", ""},
                new String[] {"  ", ""},
                new String[] {"word", "word"},
                new String[] {"hottentottententoonstelling", "hottentottententoonstelling"},
                new String[] {"line1\nline2", "line1"+newline+"line2"},
                new String[] {"testing without any indentation", "testing"+newline+"without"+newline+"any"+newline+"indentation"},
                new String[] {"do you know the a b c ?", "do"+newline+"you"+newline+"know"+newline+"the a"+newline+"b c ?"},
                new String[] {"dodo do do doooo", "dodo"+newline+"do do"+newline+"doooo"}
            };

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.format(strings[0], null, 5, false),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
    public void testIndentedFormat()
    {
        final String indentation = " * ";
        final String newline = StringUtilsHelper.getLineSeparator() + indentation;

        final String[][] fixture =
            new String[][]
            {
                    new String[] {"", indentation},
                    new String[] {"one two three", indentation+"one two three"},
                    new String[] {"Sets the <code>thisArgumentIsMissingFromTheActionForm</code> field.\n",
                                  indentation+"Sets the <code>thisArgumentIsMissingFromTheActionForm</code>"+newline+"field."}
            };

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.format(strings[0], indentation, 64, false),
                    strings[1]);
        }
    }

    /**
     *
     */
    @Test
    public void testHtmlFormat()
    {
        final String newline = StringUtilsHelper.getLineSeparator();

        final String[][] fixture =
            new String[][]
            {
                new String[] {"one two three", "<p>"+newline+"one"+newline+"two"+newline+"three"+newline+"</p>"}
            };

        for (String[] strings : fixture)
        {
            assertEquals(
                    StringUtilsHelper.format(strings[0], "", 5, true),
                    strings[1]);
        }
    }
}