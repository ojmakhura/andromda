package org.andromda.cartridges.angular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.ValueObject;
import org.apache.commons.lang3.StringUtils;

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
    public static String getComponentDestination(final String _package) {
        
        String[] tmp = _package.split("\\.");
        int length = tmp.length;
        
        if(tmp[length-1].equalsIgnoreCase("vo") || tmp[length-1].equalsIgnoreCase("service")) {
            if(length > 1)
                return tmp[length-2];
            else
                return "";
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
    public static String getComponentFileName(final String className) {
        
        StringBuilder builder = new StringBuilder();
        
        String stmp = className.substring(0, className.length());
        
        for(int i = 0; i < className.length(); i++) {
            char c = className.charAt(i);
            if(Character.isUpperCase(c)) {
                c = Character.toLowerCase(c);
                if(i > 0 && !Character.isUpperCase(className.charAt(i-1))) {
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
    public static String getDatatype(final String typeName) {
                                
        String datatype = "";
        try {
            Class cls = Class.forName(typeName);
                        
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
            } else if(obj instanceof List) {
                if(typeName.contains("<")) {
                    String tmp = StringUtils.substringAfter(typeName, "<");
                    tmp = StringUtils.substringBefore(tmp, ">");
                    datatype = getDatatype(tmp) + "[]";
                } else {
                    datatype = "Object[]";
                }
            } else if(obj instanceof Map) {
                datatype = "Map";
            }      
            
        } catch (InstantiationException | IllegalAccessException e) {            
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            
            if(typeName.equalsIgnoreCase("java.lang.Boolean")) {
                datatype = "boolean";
            } else {
                String[] tmp = typeName.split("\\.");
                datatype = tmp[tmp.length-1];
            }
        }
        
        return datatype;
    }

    public static String sanitiseArguments(Collection<ParameterFacade> arguments) {
        
        return "";
    }
    
    public static HashSet<String> getArgumentImports(List<ParameterFacade> args, String applicationPackage) {
        
        HashSet<String> set = new HashSet<>();
        for(ParameterFacade arg : args) {
            if(arg.getType() instanceof ValueObject) {
                StringBuilder builder = new StringBuilder();
                builder.append("import { ");
                builder.append(arg.getType().getName());
                builder.append(" } from '../../model/");
                builder.append(getComponentDestination(arg.getType().getPackageName()));
                builder.append("/");
                //String tn = StringUtils.substringBefore(arg.getType().getName(), ".ts");
                builder.append(getComponentFileName(arg.getType().getName()));
                builder.append("';");
                set.add(builder.toString());
            }
        }
        
        return set;
    }
    
    public static String getComponentPath(String componentPackage, String applicationPackage) {
        String subPackage = StringUtils.substringAfter(componentPackage, applicationPackage );
        return StringUtils.replaceChars(subPackage, "\\.", "\\/");
    }
}
