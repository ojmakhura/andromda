package org.andromda.cartridges.angular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.andromda.metafacades.uml.ParameterFacade;

public class AngularHelper {

    public static Collection<String> getArgumentsAsList(final String args) {
        StringTokenizer st = new StringTokenizer(args, ",");
        Collection<String> retval = new ArrayList<String>(st.countTokens());
        while (st.hasMoreTokens()) {
            retval.add(st.nextToken().trim());
        }
        return retval;
    }


    public static String getArgumentAsString(final Collection args, String token) {
        String stringArgument = "";
        int counter = 0;
        Iterator<String> iter1 = args.iterator();
        while (iter1.hasNext()) {
            if (counter == 0) {
                stringArgument = iter1.next().toString();
            } else {
                stringArgument = stringArgument + token + iter1.next().toString();
            }
            counter++;
        }

        return stringArgument;
    }
    
    /**
     * Find the best destination for the model.
     * 
     * If the package ends with a 'vo', then we use the token before it.
     * 
     * @param _package
     * @return 
     */
    public static String getModelDestination(final String _package) {
        String[] tmp = _package.split("\\.");
        
        int length = tmp.length;
        if(tmp[length-1].equalsIgnoreCase("vo") && length > 1) {
            return tmp[length-2];
        }
        
        return _package.toLowerCase();        
    }
    
    /**
     * Find a proper lower case name for the model file.
     * 
     * If the value object name ends with vo or value name
     * 
     * @param model
     * @return 
     */
    public static String getModelFileName(final String model) {
        
        StringBuilder builder = new StringBuilder();
        
        String stmp = model.substring(0, model.length());
        
        for(int i = 0; i < model.length(); i++) {
            char c = model.charAt(i);
            if(Character.isUpperCase(c)) {
                c = Character.toLowerCase(c);
                if(i > 0 && !Character.isUpperCase(model.charAt(i-1))) {
                    builder.append('-');
                } 
            } 
            builder.append(c);
        }
        
        return builder.toString();
    }
    
    /**
     * Convert java datatypes to typescript datatypes
     * @param type
     * @return 
     */
    public static String getAttributeDatatype(final String type) {
        String datatype = "";
        try {
            Class cls = Class.forName(type);
                        
            // Anything that inherits from number
            if(cls.getSuperclass().getName().equalsIgnoreCase("java.lang.Number"))
            {
                return "number";
            }
                        
            Object obj = cls.newInstance();

            if(obj instanceof String) {
                datatype = "string";
            } else if(obj instanceof Date) {
                datatype = "Date";
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            
            e.printStackTrace();
        } 
        
        return datatype;
    }

    public static String sanitiseArguments(Collection<ParameterFacade> arguments) {
        
        return "";
    }
}
