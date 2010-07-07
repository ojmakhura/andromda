package org.andromda.ant;

import org.andromda.andromdapp.AndroMDApp;

/**
 * Executes the AndroMDA application generator from a main method.
 */
public class AndromdaAntRunner
{
    /**
     * Run andromda ant task
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            AndroMDApp andromdapp = new AndroMDApp();
            andromdapp.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
