package org.andromda.core.common;

import junit.framework.TestCase;

public class StringUtilsHelperTest extends TestCase
{
    public StringUtilsHelperTest(String format)
    {
        super(format);
    }

    public void testUpperCaseFirstLetter()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "ejb", "Ejb" },
            new String[] { "EJB", "EJB" },
            new String[] { "an EJB class", "An EJB class" },
            new String[] { "an EJB Class", "An EJB Class" },
            new String[] { "HibernateEntity", "HibernateEntity" },
            new String[] { "Hibernate Entity", "Hibernate Entity" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.upperCaseFirstLetter(strings[0]), strings[1]);
        }
    }

    public void testLowerCaseFirstLetter()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "ejb", "ejb" },
            new String[] { "EJB", "eJB" },
            new String[] { "an EJB class", "an EJB class" },
            new String[] { "an EJB Class", "an EJB Class" },
            new String[] { "HibernateEntity", "hibernateEntity" },
            new String[] { "Hibernate Entity", "hibernate Entity" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.lowerCaseFirstLetter(strings[0]), strings[1]);
        }
    }

    public void testReplaceSuffix()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "EntityHibernate", "Hibernate", "EJB", "EntityEJB" },
            new String[] { "EntityHibernate", "Hibernate", "Hibernate", "EntityHibernate" },
            new String[] { "EntityHibernate", "hibernate", "EJB", "EntityHibernate" },
            new String[] { "EntityHibernate", "Entity", "EJB", "EntityHibernate" },
            new String[] { "EntityHibernate", "ernate", "qwErty", "EntityHibqwErty" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.replaceSuffix(strings[0], strings[1], strings[2]), strings[3]);
        }
    }


    public void testToJavaClassName()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "ejb", "Ejb" },
            new String[] { "EJB", "EJB" },
            new String[] { "an EJB class", "AnEJBClass" },
            new String[] { "an EJB Class", "AnEJBClass" },
            new String[] { "HibernateEntity", "HibernateEntity" },
            new String[] { "Hibernate Entity", "HibernateEntity" },
            new String[] { "Welcome... to the jungle (Guns \'n\' Roses)", "WelcomeToTheJungleGunsNRoses" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.toJavaClassName(strings[0]), strings[1]);
        }
    }

    public void testToJavaMethodName()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "ejb", "ejb" },
            new String[] { "EJB", "eJB" },
            new String[] { "an EJB class", "anEJBClass" },
            new String[] { "an EJB Class", "anEJBClass" },
            new String[] { "HibernateEntity", "hibernateEntity" },
            new String[] { "Hibernate Entity", "hibernateEntity" },
            new String[] { "Welcome... to the jungle (Guns \'n\' Roses)", "welcomeToTheJungleGunsNRoses" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.toJavaMethodName(strings[0]), strings[1]);
        }
    }

    public void testToWebFileName()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "ejb", "ejb" },
            new String[] { "EJB", "ejb" },
            new String[] { "an EJB class", "an-ejb-class" },
            new String[] { "an EJB Class", "an-ejb-class" },
            new String[] { "HibernateEntity", "hibernate-entity" },
            new String[] { "Hibernate Entity", "hibernate-entity" },
            new String[] { "Welcome... to the jungle (Guns \'n\' Roses)", "welcome-to-the-jungle-guns-n-roses" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.toWebFileName(strings[0]), strings[1]);
        }
    }

    public void testToResourceMessageKey()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "ejb", "ejb" },
            new String[] { "EJB", "ejb" },
            new String[] { "an EJB class", "an.ejb.class" },
            new String[] { "an EJB Class", "an.ejb.class" },
            new String[] { "HibernateEntity", "hibernate.entity" },
            new String[] { "Hibernate Entity", "hibernate.entity" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.toResourceMessageKey(strings[0]), strings[1]);
        }
    }

    public void testToPhrase()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "ejb", "Ejb" },
            new String[] { "EJB", "EJB" },
            new String[] { "an EJB class", "An EJB class" },
            new String[] { "an EJB Class", "An EJB Class" },
            new String[] { "HibernateEntity", "Hibernate Entity" },
            new String[] { "Hibernate Entity", "Hibernate Entity" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.toPhrase(strings[0]), strings[1]);
        }
    }

/*
    public void testToResourceMessage()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "This is\na multiline\nstring", "This is \\\na multiline \\\nstring" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.toResourceMessageKey(strings[0]), strings[1]);
        }
    }
*/

    public void testPrefixWithAPredicate()
    {
        final String[][] fixture = new String[][]
        {
            new String[] { "a", "an a" },
            new String[] { "b", "a b" },
            new String[] { "c", "a c" },
            new String[] { "d", "a d" },
            new String[] { "e", "an e" },
            new String[] { "f", "a f" },
            new String[] { "g", "a g" },
            new String[] { "h", "a h" },
            new String[] { "i", "an i" },
            new String[] { "j", "a j" },
            new String[] { "k", "a k" },
            new String[] { "l", "a l" },
            new String[] { "m", "a m" },
            new String[] { "n", "a n" },
            new String[] { "o", "an o" },
            new String[] { "p", "a p" },
            new String[] { "q", "a q" },
            new String[] { "r", "a r" },
            new String[] { "s", "a s" },
            new String[] { "t", "a t" },
            new String[] { "u", "a u" },
            new String[] { "v", "a v" },
            new String[] { "w", "a w" },
            new String[] { "x", "a x" },
            new String[] { "y", "a y" },
            new String[] { "z", "a z" },
            new String[] { "elephant", "an elephant" },
            new String[] { "cat", "a cat" },
            new String[] { "horse", "a horse" },
            new String[] { "building", "a building" }
        };

        for (int i = 0; i < fixture.length; i++)
        {
            String[] strings = fixture[i];
            assertEquals(StringUtilsHelper.prefixWithAPredicate(strings[0]), strings[1]);
        }
    }
}
