package org.andromda.cartridges.angular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import org.andromda.cartridges.jsf2.metafacades.JSFAttribute;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.cartridges.webservice.metafacades.WebServiceOperation;
  
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ValueObject;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.apache.log4j.Logger;

public class AngularHelper {
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(AngularHelper.class);

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
     * Find a proper lower case name for the model file.
     * 
     * If the value object name ends with vo or value name
     * 
     * @param model
     * @return 
     */
    public static String getComponentFileName(final String className) {
        
        StringBuilder builder = new StringBuilder();
        String stmp = className.trim().substring(0, className.length());
        
        for(int i = 0; i < className.length(); i++) {
            char c = className.charAt(i);
            if(Character.isUpperCase(c)) {
                c = Character.toLowerCase(c);
                if(i > 0 && !Character.isUpperCase(className.charAt(i-1)) && className.charAt(i-1) != ' ' && className.charAt(i-1) != '-') {
                    builder.append('-');
                } 
            } 
            builder.append(c);
        }

		return StringUtils.replaceChars(builder.toString().replaceAll("[^a-zA-Z0-9\\-\\ ]", "").trim(), " ",  "-");
    }
    
    /**
     * Convert java datatypes to typescript datatypes
     * @param type
     * @return 
     */
    public static String getDatatype(final String typeName) {
        
        if(typeName == null) {
            logger.error("typeName should not be null", new NullPointerException());
        }
        
        if(typeName.equalsIgnoreCase("java.lang.Boolean") || typeName.equalsIgnoreCase("Boolean") || typeName.equalsIgnoreCase("boolean")) {
            return "boolean";
        }
        
        if(typeName.equalsIgnoreCase("int") ||
                typeName.equalsIgnoreCase("short") ||
                typeName.equalsIgnoreCase("long") || 
                typeName.equalsIgnoreCase("float") ||
                typeName.equalsIgnoreCase("double")) {
            return "number";
        }
        
        // If the dataset is a java.util.Collection without type parameters
        if((typeName.equalsIgnoreCase("java.util.Collection") || typeName.equalsIgnoreCase("Collection"))) {
            return "any[]";
        }
        
        String datatype = "";
        try {
            Class cls = Class.forName(typeName);
                        
            // Anything that inherits from number
            if(cls.getSuperclass().getName().equalsIgnoreCase("java.lang.Number"))
            {
                return "number";
            }
            
            if(cls.getSuperclass().getName().equalsIgnoreCase("java.lang.Boolean"))
            {
                return "boolean";
            }
                        
            Object obj = cls.newInstance();

            if(obj instanceof String) {
                datatype = "string";
            } else if(obj instanceof Date) {
                datatype = "Date";
            } else if(obj instanceof List || obj instanceof Collection) {
                
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
            if(typeName.equalsIgnoreCase("java.lang.Boolean")) {
                datatype = "boolean";
            }
            logger.error("Could find the instance for " + typeName);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            if(typeName.equalsIgnoreCase("byte[]")) {
                datatype = "File";
            }else {
                if(typeName.contains("<")) {
                    String tmp = StringUtils.substringAfter(typeName, "<");
                    tmp = StringUtils.substringBefore(tmp, ">");
                    String[] tmp2 = tmp.split("\\.");
                    datatype = getDatatype(tmp2[tmp2.length-1]);
                    
                } else {
            
                    String[] tmp = typeName.split("\\.");
                    datatype = tmp[tmp.length-1];
                }
            }
        }
        
        return datatype;
    }
    
    /**
     * Create import statements
     * 
	 * @param args
	 * @param destPackage
	 * @param suffix
	 * @return 
	 */
    public static HashSet<String> getImports(List<ModelElementFacade> args, String destPackage, String suffix) {

		HashSet<String> set = new HashSet<String>();
        for(ModelElementFacade arg : args) {
            ModelElementFacade facade = null;
            			
            if(arg instanceof ParameterFacade) {
                ParameterFacade tmp = (ParameterFacade)arg;
                facade = tmp.getType();
            } else if(arg instanceof AttributeFacade) {
                facade = ((AttributeFacade)arg).getType();
            } else {
                facade = (ModelElementFacade)arg;
            }
            
            if(facade != null) {
                String angPath = "";
                boolean addImport = false;
                if(facade instanceof ValueObject || facade instanceof EnumerationFacade) {
                    angPath = "src/app/model/";
                    addImport = true;
                } else if(facade instanceof Service) {
                    angPath = "src/app/service/";
                    addImport = true;
                } else if(facade instanceof FrontEndController) {
                    angPath = "src/app/controller/";
                    addImport = true;
                } else if(facade instanceof FrontEndView) {
                    angPath = "src/app/view/";
                    addImport = true;
                } 

				if(addImport) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("import { ");
                    builder.append(facade.getName());
                    builder.append(suffix);
                    builder.append(" } from ");

                    builder.append("'");
                    builder.append(angPath);
                    builder.append(StringUtils.replaceChars(facade.getPackageName(), "\\.", "\\/"));
                    builder.append("/");
                    builder.append(getComponentFileName(facade.getName()+suffix));
                    builder.append("';");
                    set.add(builder.toString());
                }
            }
        }
        
        return set;
    }
    
    public static HashSet<String> getImportsFromSets(List<String> names, List<String> paths) {
		HashSet<String> set = new HashSet<String>();
        for(int i = 0; i < names.size(); i++) {
            set.add("import {" + names.get(i) + "} from '" + paths.get(i) + "';");
        }
        
        return set;
    }
	
	public static HashSet<String> getStringSet(List<String> strings) {
		HashSet<String> set = new HashSet<String>();
		set.addAll(strings);		
		return set;
	}
        
    /**
     * Get the bottom most directory name given a path
     * @param name
     * @return 
     */
    public static String getBottomLevelDir(String path, String extra){
        
        String tmp[] = path.split("\\");
       
        if(extra != null && extra.trim().length() > 0) {
            return tmp[tmp.length] + "-" + extra;
        }
        
        return tmp[tmp.length];
    }
	
	public static HashSet<ModelElementFacade> getFacadeSet(List<ModelElementFacade> facades) {
		HashSet<String> nameSet = new HashSet<String>();
		HashSet<ModelElementFacade> elementSet =  new HashSet<ModelElementFacade>();
		
		for(ModelElementFacade facade : facades) {
            
			if(facade != null && nameSet.add(facade.getName())) {
				elementSet.add(facade);
			}
		}
		
		return elementSet;
	}
	
	public String getWebServiceMethodName(WebServiceOperation operation) {
        
        if(StringUtils.isBlank(operation.getRestRequestType())) {
            return "post";
        }
        
		String[] splits = operation.getRestRequestType().split("\\.");
        
		return splits[splits.length-1].toLowerCase();
	}
	
	public String getWebServiceOperationPath(String fullPath) {
		
		String tmp = StringUtils.substringBetween(fullPath, "\"");
		return StringUtils.substringBefore(tmp, "{");
	}
	
	public static String getComponentName(String cName, String remove) {
		      
		String[] splits = WordUtils.capitalize(cName).trim().split(remove);
		StringBuilder builder = new StringBuilder();
		
		for( String s : splits) {
			builder.append(StringUtils.capitalize(s));
		}
		
		return builder.toString().replaceAll("[^a-zA-Z0-9]", "");
	}
    
    public static boolean isNative(String datatype) {
        
        if((datatype != "number" || datatype != "number[]")
                && (datatype.equals("string") || datatype.equals("string[]"))
                && (datatype.equals("Date") || datatype.equals("Date[]"))
                && (datatype.equals("boolean") || datatype.equals("boolean[]"))
                && (datatype.equals("Object") || datatype.equals("Object[]"))
                && (datatype.equals("any") || datatype.equals("any[]"))) {
            
            return true;
        }
        
        return true;
    }
    
    public static String getMethodSignatureArguments(List<ParameterFacade> arguments) {
        
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i < arguments.size(); i++) {
            if(i > 0) {
                builder.append(", ");
            }
            ParameterFacade arg = arguments.get(i);
            builder.append(arg.getName());
            builder.append(": ");
            builder.append(getDatatype(arg.getType().getFullyQualifiedName()));
        }
        
        return builder.toString();
    }
    
    public static String getMethodCallArguments(List<ParameterFacade> arguments) {
        
        if(arguments.size() == 1) {
            return arguments.get(0).getName();
        } else {
            StringBuilder builder = new StringBuilder();
            
            builder.append("{");
            
            for(int i = 0; i < arguments.size(); i++) {
                if(i > 0) {
                    builder.append(", ");
                }
                ParameterFacade arg = arguments.get(i);
                builder.append(arg.getName());
                builder.append(": ");
                builder.append(arg.getName());
            }
            
            builder.append("}");

            return builder.toString();
        }
    }
    
    public static void addDefaultRole() {
        
    }
    
    public static HashSet<String> getFacadeNameSet(List<UseCaseFacade> useCases) {
        
        HashSet<String> names = new HashSet<>();
                
        return names;
    }
	
	public static Boolean isComplex(ModelElementFacade element) {
		
		Boolean complex = false;
        ClassifierFacade type = null;
		
		if(element instanceof AttributeFacade) {
			type = ((AttributeFacade)element).getType();
		} else if(element instanceof ParameterFacade) {
			type = ((ParameterFacade)element).getType();
		}
		
        if (type != null)
        {
            complex = !type.getAttributes().isEmpty();
            if (!complex)
            {
                complex = !type.getAssociationEnds().isEmpty();
            }
        }
		
		//System.out.println("=============================================== " + complex);
        return complex;
	}
	
	public static String getRxwebDecorator(JSFAttribute attribute) {
		
		String decorator = "prop()";
		
		if(attribute.isMany()) {
			decorator = "propArray()";
		} else if(isComplex(attribute)) {
			decorator = "propObject()";
		}
		
		return decorator;
	}
}
