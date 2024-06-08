package org.andromda.cartridges.angular;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.andromda.cartridges.angular.metafacades.AngularAction;
import org.andromda.cartridges.angular.metafacades.AngularAttribute;
import org.andromda.cartridges.angular.metafacades.AngularComponent;
import org.andromda.cartridges.angular.metafacades.AngularControllerOperationLogic;
import org.andromda.cartridges.angular.metafacades.AngularModel;
import org.andromda.cartridges.angular.metafacades.AngularModelLogic;
import org.andromda.cartridges.angular.metafacades.AngularParameter;
import org.andromda.cartridges.angular.metafacades.AngularService;
import org.andromda.cartridges.angular.metafacades.AngularServiceOperation;
import org.andromda.cartridges.angular.metafacades.AngularServiceParameterLogic;
import org.andromda.cartridges.angular.metafacades.AngularView;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.EnumerationFacade;
import org.andromda.metafacades.uml.FrontEndAction;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndParameter;
import org.andromda.metafacades.uml.FrontEndView;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.metafacades.uml.ValueObject;
import org.andromda.metafacades.uml.web.MetafacadeWebGlobals;
import org.andromda.metafacades.uml.web.MetafacadeWebProfile;
import org.andromda.metafacades.uml.webservice.MetafacadeWebserviceGlobals;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.text.WordUtils;
import org.apache.log4j.Logger;

public class AngularUtils {
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(AngularUtils.class);

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
        
        if(StringUtils.isBlank(typeName)) {
            logger.error("typeName should not be null", new NullPointerException());
            return null;
        }

        if(typeName.contains("LocalDate")) {
            return "Date";
        }

        if(typeName.equals("String")) {
            return "string";
        }

        int i = typeName.lastIndexOf(".");

        String name = "";

        if(typeName.endsWith(">")) {
            int x = typeName.indexOf("<");
            name = typeName.substring(0, x + 1);

            int j = name.lastIndexOf(".");

            if(j >= 0) {
                name = name.substring(j+1);
            } 
        }

        if(i >= 0) {
            name = name + typeName.substring(i+1);
        } else {
            name = typeName;
        }

        return name; //splits[splits.length - 1];
    }
	
	public static boolean isArray(final String typeName) {
		
		if(typeName.contains("[]") || typeName.contains("Collection") || typeName.contains("List") || typeName.contains("Set")) {
			return true;
		}
		
		return false;
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
                    angPath = "@app/model/";
                    addImport = true;
                } else if(facade instanceof Service) {
                    angPath = "@app/service/";
                    addImport = true;
                } else if(facade instanceof FrontEndController) {
                    angPath = "@app/controller/";
                    addImport = true;
                } else if(facade instanceof FrontEndView) {
                    angPath = "@app/view/";
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
	
	public String getWebServiceMethodName(AngularServiceOperation operation) {
        
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

    public static String getSelectorName(String path) {

        String[] splits = path.split("/");
        return splits[splits.length - 1];
    }

    public static String getActionName(String path) {

        return getComponentName(getSelectorName(path), "-");
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

        for(ParameterFacade arg : arguments) {
            if(builder.length() > 0) {
                
                builder.append(", ");
            }
            
            builder.append(arg.getName());
            builder.append(": ");

            if(arg instanceof AngularModel) {

                AngularModel model = (AngularModel) arg;
                builder.append(model.getAngularTypeName());
                
            } else if(arg.getType() instanceof AngularModel) {

                AngularModel model = (AngularModel) arg.getType();
                builder.append(model.getAngularTypeName());

            } else {
                String input = Objects.toString(arg.findTaggedValue(MetafacadeWebProfile.TAGGEDVALUE_INPUT_TYPE));
                
                if(input != null && input.equals("file")) {
                    builder.append("File");
                } else {
                    builder.append(getDatatype(arg.getGetterSetterTypeName()));
                }
            }
            builder.append(" | any ");
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
	
    public static Boolean isComplex(Object element) {
        
        // If this is a value object, then it's complex
        if(element instanceof ValueObject) {
            return true;
        }
        
        Boolean complex = false;
        ClassifierFacade type = null;
		
        if(element instanceof AttributeFacade) {
            type = ((AttributeFacade)element).getType();
        } else if(element instanceof ParameterFacade) {
            type = ((ParameterFacade)element).getType();
        } else if(element instanceof FrontEndParameter) {
            type = ((FrontEndParameter)element).getType();
        } 
		
        if (type != null)
        {
            if(type.isEnumeration()) { /// Enumerations are complex
                complex = true;
            } else {

                complex = !type.getAttributes().isEmpty(); // If it has more than one attribute then it's complex
                if (!complex)
                {
                    complex = !type.getAssociationEnds().isEmpty();
                }
            }
        }
		
        return complex;
    }
	
    public static String getRxwebDecorator(AngularAttribute attribute) {
		
        String decorator = "prop()";
		
        if(attribute.isMany()) {
            decorator = "propArray()";
        } else if(isComplex(attribute)) {
            decorator = "propObject()";
        }
		
        return decorator;
    }
	
    public static Collection<?> getTableColumns(AngularAttribute attribute) {
        Collection columns = new ArrayList<>();
        String identifierColumns = Objects.toString(attribute.findTaggedValue("andromda_presentation_view_field_table_identifier_columns"), "").trim();
        String viewColumns = Objects.toString(attribute.findTaggedValue("andromda_presentation_view_table_columns"), "").trim();

        if(!StringUtils.isBlank(identifierColumns)) {
            for(String col : identifierColumns.split(",")) {
                columns.add(col);
            }
        } else if(!StringUtils.isBlank(viewColumns)) {
            for(String col : viewColumns.split(",")) {
                columns.add(col);
            }
        } else {
            for(AttributeFacade attr : attribute.getType().getAttributes()) {
                columns.add(attr);
            }
        }
		
        return columns;
    }
        
    public static String getColumnName(Object column) {
        if(column instanceof String) {
            return (String)column;
        } else {
            AngularAttribute attr = (AngularAttribute)column;
                
            return attr.getName();
        }
    }
    /**
     * Constructs the signature that takes the form for this operation.
     *
     * @param isAbstract whether or not the signature is abstract.
     * @return the appropriate signature.
     */
    public static String getFormOperationSignature(AngularControllerOperationLogic operation, boolean isAbstract)
    {
        final StringBuilder signature = new StringBuilder();
        signature.append(operation.getVisibility() + ' ');
        if (isAbstract)
        {
            signature.append("abstract ");
        }
        
        signature.append(" " + operation.getName() + "(): ");
        /*if (!operation.getFormFields().isEmpty())
        {
            signature.append("form");
        }
        signature.append("): ");*/
        final ModelElementFacade returnType = operation.getReturnType();
        signature.append(returnType != null ? getDatatype(returnType.getFullyQualifiedName()) : null);
        
        return signature.toString();
    }

    public static boolean isTable(AngularAttribute attribute) {

        String columns = Objects.toString(attribute.findTaggedValue("andromda_presentation_view_field_table_identifier_columns"), "").trim();
        String viewColumns = Objects.toString(attribute.findTaggedValue("andromda_presentation_view_table_columns"), "").trim();
        String fieldType = Objects.toString(attribute.findTaggedValue("andromda_presentation_web_view_field_type"), "").trim();
        String viewTable = Objects.toString(attribute.findTaggedValue("andromda_presentation_view_table"), "").trim();

        if(Boolean.valueOf(viewTable) ||
            !StringUtils.isBlank(viewColumns) ||
            fieldType.equals("table") ||
            !StringUtils.isBlank(columns) || attribute.isInputTable()) {
            
            return true;
        }

        return false;
    }

    /**
     * @param action
     * @return
     */
    public static Collection<AngularAttribute> getStandardAttributes(Object feParameter) {

        List<AngularAttribute> standardAttributes = new ArrayList<>();

        Collection attributes = new ArrayList<>();

        if(feParameter instanceof AngularParameter) {
            final AngularParameter parameter = (AngularParameter)feParameter;
            attributes = parameter.getAttributes();
        } else if(feParameter instanceof AngularComponent) {
            final AngularComponent component = (AngularComponent)feParameter;
            attributes = component.getAttributes();
        }

        for(Object tmp : attributes) {
            AngularAttribute attribute = (AngularAttribute)tmp;
            if(!isTable(attribute)) {
                standardAttributes.add(attribute);
            }
        }

        return standardAttributes;
    }

    public static Collection<AngularAttribute> getTableAttributes(Object feParameter) {

        List<AngularAttribute> tableAttributes = new ArrayList<>();
        
        Collection attributes = new ArrayList<>();

        if(feParameter instanceof AngularParameter) {
            final AngularParameter parameter = (AngularParameter)feParameter;
            attributes = parameter.getAttributes();
            
        } else if(feParameter instanceof AngularComponent) {
            final AngularComponent component = (AngularComponent)feParameter;
            attributes = component.getAttributes();
        }

        for(Object tmp : attributes) {
            AngularAttribute attribute = (AngularAttribute)tmp;
            if(isTable(attribute)) {
                tableAttributes.add(attribute);
            }
            
        }

        return tableAttributes;
    }

    public static String getNameFromPath(final String path) {

        if(StringUtils.isBlank(path)) {
            return "";
        }

        String[] splits = path.split("/");
        return splits[splits.length - 1];
    }

    /**
     * Converts the argument into a web resource name, this means: all lowercase
     * characters and words are separated with dashes.
     *
     * @param string any string
     * @return the string converted to a value that would be well-suited for a
     *         web file name
     */
    public static String toWebResourceName(final String string)
    {
        return StringUtilsHelper.separate(
            string,
            "-").toLowerCase();
    }

    private static final Pattern VALIDATOR_TAGGEDVALUE_PATTERN =
        Pattern.compile("\\w+(\\(\\w+=[^,)]*(,\\w+=[^,)]*)*\\))?");

    private static final String ANNOTATION_VALIDATOR_PREFIX = "@";

    /**
     * Reads the validator arguments from the the given tagged value.
     * @param validatorTaggedValue
     * @return returns a list of String instances or an empty list
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List<String> parseValidatorArgs(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        final List<String> validatorArgs = new ArrayList<String>();

        //isn't it an annotation ?
        if(!StringUtils.startsWith(validatorTaggedValue,ANNOTATION_VALIDATOR_PREFIX))
        {

            // check if the input tagged value matches the required pattern
            if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
            {
                throw new IllegalArgumentException(
                    "Illegal validator tagged value (this tag is used to specify custom validators " +
                    "and might look like myValidator(myVar=myArg,myVar2=myArg2), perhaps you wanted to use " +
                    "andromda_presentation_view_field_format?): " + validatorTaggedValue);
            }

            // only keep what is between parentheses (if any)
            int left = validatorTaggedValue.indexOf('(');
            if (left > -1)
            {
                final int right = validatorTaggedValue.indexOf(')');
                validatorTaggedValue = validatorTaggedValue.substring(
                        left + 1,
                        right);

                final String[] pairs = validatorTaggedValue.split(",");
                for (int i = 0; i < pairs.length; i++)
                {
                    final String pair = pairs[i];
                    final int equalsIndex = pair.indexOf('=');

                    // it's possible the argument is the empty string
                    if (equalsIndex < pair.length() - 1)
                    {
                        validatorArgs.add(pair.substring(equalsIndex + 1));
                    }
                    else
                    {
                        validatorArgs.add("");
                    }
                }
            }
        }
        return validatorArgs;
    }

    /**
     * Reads the validator variable names from the the given tagged value.
     * @param validatorTaggedValue
     * @return never null, returns a list of String instances
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static List<String> parseValidatorVars(String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        final List<String> validatorVars = new ArrayList<String>();

        //isn't it an annotation ?
        if(!StringUtils.startsWith(validatorTaggedValue,ANNOTATION_VALIDATOR_PREFIX))
        {

            // check if the input tagged value matches the required pattern
            if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
            {
                throw new IllegalArgumentException("Illegal validator tagged value: " + validatorTaggedValue);
            }

            // only keep what is between parentheses (if any)
            int left = validatorTaggedValue.indexOf('(');
            if (left > -1)
            {
                int right = validatorTaggedValue.indexOf(')');
                validatorTaggedValue = validatorTaggedValue.substring(
                        left + 1,
                        right);

                final String[] pairs = validatorTaggedValue.split(",");
                for (int i = 0; i < pairs.length; i++)
                {
                    final String pair = pairs[i];
                    final int equalsIndex = pair.indexOf('=');
                    validatorVars.add(pair.substring(
                            0,
                            equalsIndex));
                }
            }
        }
        return validatorVars;
    }

    /**
     * Parses the validator name for a tagged value.
     * @param validatorTaggedValue
     * @return validatorTaggedValue
     * @throws IllegalArgumentException when the input string does not match the required pattern
     */
    public static String parseValidatorName(final String validatorTaggedValue)
    {
        if (validatorTaggedValue == null)
        {
            throw new IllegalArgumentException("Validator tagged value cannot be null");
        }

        //isn't it an annotation ?
        if(StringUtils.startsWith(validatorTaggedValue, ANNOTATION_VALIDATOR_PREFIX))
        {
            return validatorTaggedValue;
        }

        // check if the input tagged value matches the required pattern
        if (!VALIDATOR_TAGGEDVALUE_PATTERN.matcher(validatorTaggedValue).matches())
        {
            throw new IllegalArgumentException("Illegal validator tagged value: " + validatorTaggedValue);
        }

        final int leftParen = validatorTaggedValue.indexOf('(');
        return (leftParen == -1) ? validatorTaggedValue : validatorTaggedValue.substring(
            0,
            leftParen);
    }

    /**
     * Constructs a string representing an array initialization in Java.
     *
     * @param name the name to give the array.
     * @param count the number of items to give the array.
     * @return A String representing Java code for the initialization of an array.
     */
    public static String constructDummyArrayDeclaration(
        final String name,
        final int count)
    {
        final StringBuilder array = new StringBuilder("new Object[] {");
        for (int ctr = 1; ctr <= count; ctr++)
        {
            array.append("\"" + name + "-" + ctr + "\"");
            if (ctr != count)
            {
                array.append(", ");
            }
        }
        array.append("}");
        return array.toString();
    }

    /**
     * @param format
     * @return this field's date format
     */
    public static String getDateFormat(String format)
    {
        format = StringUtils.trimToEmpty(format);
        return format.endsWith(STRICT) ? getToken(format, 1, 2) : getToken(format, 0, 1);
    }

    private static String defaultDateFormat = "MM/dd/yyyy HH:mm:ssZ";
    private static FastDateFormat df = FastDateFormat.getInstance(defaultDateFormat);

    /**
     * Returns the current Date in the specified format.
     *
     * @param format The format for the output date
     * @return the current date in the specified format.
     */
    public static String getDate(String format)
    {
        if (df == null || !format.equals(df.getPattern()))
        {
            df = FastDateFormat.getInstance(format);
        }
        return df.format(new Date());
    }

    /**
     * Returns the current Date
     *
     * @return the current date in the default format.
     */
    public static String getDate()
    {
        return getDate(defaultDateFormat);
    }

    private static final String STRICT = "strict";

    /**
     * @param format
     * @return <code>true</code> if this field's value needs to conform to a
     * strict date format, <code>false</code> otherwise
     */
    public static boolean isStrictDateFormat(String format)
    {
        return strictDateTimeFormat ? strictDateTimeFormat : STRICT.equalsIgnoreCase(getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is an email format.
     * @param format
     * @return <code>true</code> if this field is to be formatted as an email
     * address, <code>false</code> otherwise
     */
    public static boolean isEmailFormat(String format)
    {
        return "email".equalsIgnoreCase(AngularUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is an equal format.
     * @param format
     * @return <code>true</code> if this field is to be formatted as an
     * email address, <code>false</code> otherwise
     */
    public static boolean isEqualFormat(String format)
    {
        return "equal".equalsIgnoreCase(AngularUtils.getToken(format, 0, 2));
    }

    /**
     * Indicates if the given <code>format</code> is a credit card format.
     * @param format
     * @return <code>true</code> if this field is to be formatted as a credit card, <code>false</code> otherwise
     */
    public static boolean isCreditCardFormat(final String format)
    {
        return "creditcard".equalsIgnoreCase(AngularUtils.getToken(format, 0, 2));
    }

    /**
     * Indicates if the given <code>format</code> is a pattern format.
     * @param format
     * @return <code>true</code> if this field's value needs to respect a certain pattern, <code>false</code> otherwise
     */
    public static boolean isPatternFormat(final String format)
    {
        return "pattern".equalsIgnoreCase(AngularUtils.getToken(format, 0, 2));
    }

    /**
     * Indicates if the given <code>format</code> is a minlength format.
     * @param format
     * @return <code>true</code> if this field's value needs to consist of at least a certain
     *         number of characters, <code>false</code> otherwise
     */
    public static boolean isMinLengthFormat(final String format)
    {
        return "minlength".equalsIgnoreCase(AngularUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * Indicates if the given <code>format</code> is a maxlength format.
     * @param format
     * @return <code>true</code> if this field's value needs to consist of at maximum a certain
     *         number of characters, <code>false</code> otherwise
     */
    public static boolean isMaxLengthFormat(String format)
    {
        return "maxlength".equalsIgnoreCase(AngularUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @param string
     * @param index
     * @param limit
     * @return the i-th space delimited token read from the argument String, where i does not exceed the specified limit
     */
    public static String getToken(
        String string,
        int index,
        int limit)
    {
        String token = null;
        if (string != null && string.length() > 0)
        {
            final String[] tokens = string.split(
                    "[\\s]+",
                    limit);
            token = index >= tokens.length ? null : tokens[index];
        }
        return token;
    }

    /**
     * Retrieves the input format (if one is defined), for the given
     * <code>element</code>.
     * @param element the model element for which to retrieve the input format.
     * @return the input format.
     */
    public static String getInputFormat(final ModelElementFacade element)
    {
        final Object value = element.findTaggedValue(AngularProfile.TAGGEDVALUE_INPUT_FORMAT);
        final String format = value == null ? null : String.valueOf(value);
        return format == null ? null : format.trim();
    }

    /**
     * Indicates if the given <code>format</code> is a range format.
     * @param format
     * @return <code>true</code> if this field's value needs to be in a specific range, <code>false</code> otherwise
     */
    public static boolean isRangeFormat(final String format)
    {
        return "range".equalsIgnoreCase(AngularUtils.getToken(
                format,
                0,
                2));
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a byte, <code>false</code> otherwise
     */
    public static boolean isByte(final ClassifierFacade type)
    {
        return isType(
            type,
            AngularProfile.BYTE_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a short, <code>false</code> otherwise
     */
    public static boolean isShort(final ClassifierFacade type)
    {
        return isType(
            type,
            AngularProfile.SHORT_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is an integer, <code>false</code> otherwise
     */
    public static boolean isInteger(final ClassifierFacade type)
    {
        return isType(
            type,
            AngularProfile.INTEGER_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a long integer, <code>false</code> otherwise
     */
    public static boolean isLong(final ClassifierFacade type)
    {
        return isType(
            type,
            AngularProfile.LONG_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a floating point, <code>false</code> otherwise
     */
    public static boolean isFloat(final ClassifierFacade type)
    {
        return isType(
            type,
            AngularProfile.FLOAT_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a double precision floating point,
     * <code>false</code> otherwise
     */
    public static boolean isDouble(final ClassifierFacade type)
    {
        return isType(
            type,
            AngularProfile.DOUBLE_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a date, <code>false</code> otherwise
     */
    public static boolean isDate(final ClassifierFacade type)
    {
        return type != null && type.isDateType();
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a time, <code>false</code> otherwise
     */
    public static boolean isTime(final ClassifierFacade type)
    {
        return isType(
            type,
            AngularProfile.TIME_TYPE_NAME);
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a URL, <code>false</code> otherwise
     */
    public static boolean isUrl(final ClassifierFacade type)
    {
        return isType(
            type,
            AngularProfile.URL_TYPE_NAME);
    }

    private static boolean isType(final ClassifierFacade type, String typeName)
    {
        boolean isType = UMLMetafacadeUtils.isType(
            type,
            typeName);
        if (!isType)
        {
            // - handle abstract types that are mapped to java types
            if (type.getLanguageMappings() != null)
            {
                final String javaTypeName = type.getLanguageMappings()
                    .getTo(type.getFullyQualifiedName(true));
                if (javaTypeName != null)
                {
                    isType = javaTypeName.replaceAll(".*\\.", "").equalsIgnoreCase(
                        type.getLanguageMappings().getTo(typeName));
                }
            }
        }
        return isType;
    }

    /**
     * @param type
     * @return <code>true</code> if the type of this field is a String,
     * <code>false</code> otherwise
     */
    public static boolean isString(final ClassifierFacade type)
    {
        return type != null && type.isStringType();
    }

    /**
     * Indicates if the given element is read-only or not.
     *
     * @param element the element to check.
     * @return true/false
     */
    public static boolean isReadOnly(final ModelElementFacade element)
    {
        boolean readOnly = false;
        if (element != null)
        {
            final Object value = element.findTaggedValue(AngularProfile.TAGGEDVALUE_INPUT_READONLY);
            readOnly = Boolean.valueOf(Objects.toString(value, "")).booleanValue();
        }
        return readOnly;
    }

    /**
     * Retrieves the "equal" value from the given element (if one is present).
     *
     * @param element the element from which to retrieve the equal value.
     * @return the "equal" value.
     */
    public static String getEqual(final ModelElementFacade element)
    {
        String equal = null;
        if (element != null)
        {
            final Object value = element.findTaggedValue(AngularProfile.TAGGEDVALUE_INPUT_EQUAL);
            equal = value == null ? null : value.toString();
        }
        return equal;
    }

    /**
     * Retrieves the "equal" value from the given element (if one is present).
     *
     * @param element the element from which to retrieve the equal value.
     * @param ownerParameter the optional owner parameter (specified if the element is an attribute).
     * @return the "equal" value.
     */
    public static String getEqual(final ModelElementFacade element, final ParameterFacade ownerParameter)
    {
        String equal = null;
        if (element != null)
        {
            final Object value = element.findTaggedValue(AngularProfile.TAGGEDVALUE_INPUT_EQUAL);
            equal = value == null ? null : value.toString();
            if (StringUtils.isNotBlank(equal) && ownerParameter != null)
            {
                equal = ownerParameter.getName() + StringUtilsHelper.upperCamelCaseName(equal);
            }
        }
        return equal;
    }

    /**
     * Retrieves the "validwhen" value from the given element (if one is present).
     *
     * @param element the element from which to retrieve the validwhen value.
     * @return the "validwhen" value.
     */
    public static String getValidWhen(final ModelElementFacade element)
    {
        String validWhen = null;
        if (element != null)
        {
            final Object value = element.findTaggedValue(AngularProfile.TAGGEDVALUE_INPUT_VALIDWHEN);
            validWhen = value == null ? null : '(' + value.toString() + ')';
        }
        return validWhen;
    }

    /**
     * @param format
     * @return the lower limit for this field's value's range
     */
    public static String getRangeStart(final String format)
    {
        return AngularUtils.getToken(
            format,
            1,
            3);
    }

    /**
     * @param format
     * @return the upper limit for this field's value's range
     */
    public static String getRangeEnd(final String format)
    {
        return AngularUtils.getToken(
            format,
            2,
            3);
    }

    /**
     * @param format
     * @return the minimum number of characters this field's value must consist of
     */
    public static String getMinLengthValue(final String format)
    {
        return AngularUtils.getToken(
            format,
            1,
            2);
    }

    /**
     * @param format
     * @return the maximum number of characters this field's value must consist of
     */
    public static String getMaxLengthValue(final String format)
    {
        return AngularUtils.getToken(
            format,
            1,
            2);
    }

    /**
     * @param format
     * @return the pattern this field's value must respect
     */
    public static String getPatternValue(final String format)
    {
        return '^' + AngularUtils.getToken(
            format,
            1,
            2) + '$';
    }

    //validator strings
    /** "required" */
    public static final String VT_REQUIRED="required";
    /** "url" */
    public static final String VT_URL="url";
    /** "intRange" */
    public static final String VT_INT_RANGE="intRange";
    /** "floatRange" */
    public static final String VT_FLOAT_RANGE="floatRange";
    /** "doubleRange" */
    public static final String VT_DOUBLE_RANGE="doubleRange";
    /** "email" */
    public static final String VT_EMAIL="email";
    /** "creditCard" */
    public static final String VT_CREDIT_CARD="creditCard";
    /** "minlength" */
    public static final String VT_MIN_LENGTH="minlength";
    /** "maxlength" */
    public static final String VT_MAX_LENGTH="maxlength";
    /** "mask" */
    public static final String VT_MASK="mask";
    /** "validwhen" */
    public static final String VT_VALID_WHEN="validwhen";
    /** "equal" */
    public static final String VT_EQUAL="equal";

    /**
     * Retrieves the validator types as a collection from the given
     * <code>element</code> (if any can be retrieved).
     *
     * @param element the element from which to retrieve the types.
     * @param type the type of the element.
     * @return the collection of validator types.
     */
    public static Collection<String> getValidatorTypes(
        final ModelElementFacade element,
        final ClassifierFacade type)
    {
        final Collection<String> validatorTypesList = new ArrayList<String>();
        if (element != null && type != null)
        {
            final String format = AngularUtils.getInputFormat(element);
            final boolean isRangeFormat = format != null && isRangeFormat(format);
            if (element instanceof AttributeFacade)
            {
                if (((AttributeFacade)element).isRequired())
                {
                    validatorTypesList.add(VT_REQUIRED);
                }
            }
            else if (element instanceof AngularParameter)
            {
                if (((AngularParameter)element).isRequired())
                {
                    validatorTypesList.add(VT_REQUIRED);
                }
            }
            if (AngularUtils.isByte(type))
            {
                validatorTypesList.add("byte");
            }
            else if (AngularUtils.isShort(type))
            {
                validatorTypesList.add("short");
            }
            else if (AngularUtils.isInteger(type))
            {
                validatorTypesList.add("integer");
            }
            else if (AngularUtils.isLong(type))
            {
                validatorTypesList.add("long");
            }
            else if (AngularUtils.isFloat(type))
            {
                validatorTypesList.add("float");
            }
            else if (AngularUtils.isDouble(type))
            {
                validatorTypesList.add("double");
            }
            else if (AngularUtils.isDate(type))
            {
                validatorTypesList.add("date");
            }
            else if (AngularUtils.isTime(type))
            {
                validatorTypesList.add("time");
            }
            else if (AngularUtils.isUrl(type))
            {
                validatorTypesList.add(VT_URL);
            }

            if (isRangeFormat)
            {
                if (AngularUtils.isInteger(type) || AngularUtils.isShort(type) || AngularUtils.isLong(type))
                {
                    validatorTypesList.add(VT_INT_RANGE);
                }
                if (AngularUtils.isFloat(type))
                {
                    validatorTypesList.add(VT_FLOAT_RANGE);
                }
                if (AngularUtils.isDouble(type))
                {
                    validatorTypesList.add(VT_DOUBLE_RANGE);
                }
            }

            if (format != null)
            {
                if (AngularUtils.isString(type) && AngularUtils.isEmailFormat(format))
                {
                    validatorTypesList.add(VT_EMAIL);
                }
                else if (AngularUtils.isString(type) && AngularUtils.isCreditCardFormat(format))
                {
                    validatorTypesList.add(VT_CREDIT_CARD);
                }
                else
                {
                    Collection formats = element.findTaggedValues(AngularProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        String additionalFormat = String.valueOf(formatIterator.next());
                        if (AngularUtils.isMinLengthFormat(additionalFormat))
                        {
                            validatorTypesList.add(VT_MIN_LENGTH);
                        }
                        else if (AngularUtils.isMaxLengthFormat(additionalFormat))
                        {
                            validatorTypesList.add(VT_MAX_LENGTH);
                        }
                        else if (AngularUtils.isPatternFormat(additionalFormat))
                        {
                            validatorTypesList.add(VT_MASK);
                        }
                    }
                }
            }
            if (AngularUtils.getValidWhen(element) != null)
            {
                validatorTypesList.add(VT_VALID_WHEN);
            }
            if (AngularUtils.getEqual(element) != null)
            {
                validatorTypesList.add(VT_EQUAL);
            }

            // - custom (paramterized) validators are allowed here
            final Collection taggedValues = element.findTaggedValues(AngularProfile.TAGGEDVALUE_INPUT_VALIDATORS);
            for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
            {
                String validator = String.valueOf(iterator.next());
                validatorTypesList.add(AngularUtils.parseValidatorName(validator));
            }
        }
        return validatorTypesList;
    }

    /**
     * Gets the validator variables for the given <code>element</code> (if they can
     * be retrieved).
     *
     * @param element the element from which to retrieve the variables
     * @param type the type of the element.
     * @param ownerParameter the optional owner parameter (if the element is an attribute for example).
     * @return the collection of validator variables.
     */
    public static Collection<List<String>> getValidatorVars(
        final ModelElementFacade element,
        final ClassifierFacade type,
        final ParameterFacade ownerParameter)
    {
        final Map<String, List<String>> vars = new LinkedHashMap<String, List<String>>();
        if (element != null && type != null)
        {
            final String format = AngularUtils.getInputFormat(element);
            if (format != null)
            {
                final boolean isRangeFormat = AngularUtils.isRangeFormat(format);

                if (isRangeFormat)
                {
                    final String min = "min";
                    final String max = "max";
                    vars.put(
                        min,
                        Arrays.asList(min, AngularUtils.getRangeStart(format)));
                    vars.put(
                        max,
                        Arrays.asList(max, AngularUtils.getRangeEnd(format)));
                }
                else
                {
                    final Collection formats = element.findTaggedValues(AngularProfile.TAGGEDVALUE_INPUT_FORMAT);
                    for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
                    {
                        final String additionalFormat = String.valueOf(formatIterator.next());
                        final String minlength = "minlength";
                        final String maxlength = "maxlength";
                        final String mask = "mask";
                        if (AngularUtils.isMinLengthFormat(additionalFormat))
                        {
                            vars.put(
                                minlength,
                                Arrays.asList(minlength, AngularUtils.getMinLengthValue(additionalFormat)));
                        }
                        else if (AngularUtils.isMaxLengthFormat(additionalFormat))
                        {
                            vars.put(
                                maxlength,
                                Arrays.asList(maxlength, AngularUtils.getMaxLengthValue(additionalFormat)));
                        }
                        else if (AngularUtils.isPatternFormat(additionalFormat))
                        {
                            vars.put(
                                mask,
                                Arrays.asList(mask, AngularUtils.getPatternValue(additionalFormat)));
                        }
                    }
                }
            }
            String inputFormat;
            if (element instanceof AngularAttribute)
            {
                inputFormat = ((AngularAttribute)element).getFormat();
            }
            else if (element instanceof AngularParameter)
            {
                inputFormat = ((AngularParameter)element).getFormat();
            }
            //else if (element instanceof AngularManageableEntityAttribute)
            //{
            //    inputFormat = ((AngularManageableEntityAttribute)element).getFormat();
            //}
            else
            {
                throw new RuntimeException("'element' is an invalid type, it must be either an instance of '" +
                    AngularAttribute.class.getName() + "' or '" + AngularParameter.class.getName() + "'");
            }
            if (AngularUtils.isDate(type))
            {
                final String datePatternStrict = "datePatternStrict";
                if (format != null && AngularUtils.isStrictDateFormat(format))
                {
                    vars.put(
                        datePatternStrict,
                        Arrays.asList(datePatternStrict, inputFormat));
                }
                else
                {
                    final String datePattern = "datePattern";
                    vars.put(
                        datePattern,
                        Arrays.asList(datePattern, inputFormat));
                }
            }
            if (AngularUtils.isTime(type))
            {
                final String timePattern = "timePattern";
                vars.put(
                    timePattern,
                    Arrays.asList(timePattern, inputFormat));
            }

            final String validWhen = AngularUtils.getValidWhen(element);
            if (validWhen != null)
            {
                final String test = "test";
                vars.put(
                    test,
                    Arrays.asList(test, validWhen));
            }

            final String equal = AngularUtils.getEqual(element, ownerParameter);
            if (equal != null)
            {
                final String fieldName = "fieldName";
                vars.put(
                    fieldName,
                    Arrays.asList(fieldName, equal));
            }

            // - custom (parameterized) validators are allowed here
            //   in this case we will reuse the validator arg values
            final Collection taggedValues = element.findTaggedValues(AngularProfile.TAGGEDVALUE_INPUT_VALIDATORS);
            for (final Object value : taggedValues)
            {
                final String validator = String.valueOf(value);

                // - guaranteed to be of the same length
                final List<String> validatorVars = AngularUtils.parseValidatorVars(validator);
                final List<String> validatorArgs = AngularUtils.parseValidatorArgs(validator);

                for (int ctr = 0; ctr < validatorVars.size(); ctr++)
                {
                    vars.put(validatorVars.get(ctr),
                        Arrays.asList(validatorVars.get(ctr), validatorArgs.get(ctr)));
                }
            }
        }
        return vars.values();
    }

    /**
     * Gets the validator args for the <code>element</code> and the given <code>validatorType</code>.
     *
     * @param element the element for which to retrieve the arguments.
     * @param validatorType the validator type name.
     * @return the validator args as a collection.
     */
    public static java.util.Collection getValidatorArgs(
        final ModelElementFacade element,
        final String validatorType)
    {
        final Collection<Object> args = new ArrayList<Object>();
        if ("intRange".equals(validatorType) || "floatRange".equals(validatorType) ||
            "doubleRange".equals(validatorType))
        {
            args.add("${var:min}");
            args.add("${var:max}");
        }
        else if ("minlength".equals(validatorType))
        {
            args.add("${var:minlength}");
        }
        else if ("maxlength".equals(validatorType))
        {
            args.add("${var:maxlength}");
        }
        else if ("date".equals(validatorType))
        {
            final String validatorFormat = AngularUtils.getInputFormat(element);
            if (AngularUtils.isStrictDateFormat(validatorFormat))
            {
                args.add("${var:datePatternStrict}");
            }
            else
            {
                args.add("${var:datePattern}");
            }
        }
        else if ("time".equals(validatorType))
        {
            args.add("${var:timePattern}");
        }
        else if ("equal".equals(validatorType))
        {
            ModelElementFacade equalParameter = null;
            final String equal = AngularUtils.getEqual(element);
            if (element instanceof ParameterFacade)
            {
                final FrontEndParameter parameter = (FrontEndParameter)element;
                final OperationFacade operation = parameter.getOperation();
                if (operation != null)
                {
                    equalParameter = operation.findParameter(equal);
                }
                if (equalParameter == null)
                {
                    final FrontEndAction action = parameter.getAction();
                    if (action != null)
                    {
                        equalParameter = action.findParameter(equal);
                    }
                }
            }
            else if (element instanceof AttributeFacade)
            {
                final AttributeFacade attribute = (AttributeFacade)element;
                final ClassifierFacade owner = attribute.getOwner();
                if (owner != null)
                {
                    equalParameter = owner.findAttribute(equal);
                }
            }
            args.add(equalParameter);
            args.add("${var:fieldName}");
        }

        // custom (paramterized) validators are allowed here
        final Collection taggedValues = element.findTaggedValues(AngularProfile.TAGGEDVALUE_INPUT_VALIDATORS);
        for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
        {
            final String validator = String.valueOf(iterator.next());
            if (validatorType.equals(AngularUtils.parseValidatorName(validator)))
            {
                args.addAll(AngularUtils.parseValidatorArgs(validator));
            }
        }
        return args;
    }

    /**
     * Whether or not date patterns should be treated as strict.
     */
    private static boolean strictDateTimeFormat;

    /**
     * Sets whether or not the date patterns should be treated as strict.
     *
     * @param strictDateTimeFormat
     */
    public void setStrictDateTimeFormat(final boolean strictDateTimeFormat)
    {
        AngularUtils.strictDateTimeFormat = strictDateTimeFormat;
    }

    /**
     * Indicates whether or not the format for this element is a strict date
     * format.
     * @param element
     * @return true/false
     */
    public static boolean isStrictDateFormat(final ModelElementFacade element)
    {
        final String format = AngularUtils.getInputFormat(element);
        return AngularUtils.isStrictDateFormat(format);
    }

    /**
     * Gets the format string for the given <code>element</code>.
     *
     * @param element the element for which to retrieve the format.
     * @param type the type of the element.
     * @param defaultDateFormat
     * @param defaultTimeFormat
     * @return the format string (if one is present otherwise null).
     */
    public static String getFormat(
        final ModelElementFacade element,
        final ClassifierFacade type,
        final String defaultDateFormat,
        final String defaultTimeFormat)
    {
        String format = null;
        if (element != null && type != null)
        {
            format = AngularUtils.getInputFormat(element);
            if (format == null)
            {
                if(type.isDateType() && type.isTimeType())
                {
                    format = defaultDateFormat+" "+defaultTimeFormat;
                }
                else if (type.isTimeType())
                {
                    format = defaultTimeFormat;
                }
                else if (type.isDateType())
                {
                    format = defaultDateFormat;
                }
            }
            else if (type.isDateType())
            {
                format = AngularUtils.getDateFormat(format);
            }
        }
        return format;
    }

    /**
     * The XHTML extension.
     */
    private static final String EXTENSION_XHTML = "xhtml";

    /**
     * Gets the extension for the view type.
     *
     * @return the view type extension.
     */
    public String getViewExtension()
    {
        return EXTENSION_XHTML;
    }

    //TODO remover
    private boolean isPortlet()
    {
        return false;
    }

    /**
     * @return className
     */
    public String getRequestClassName()
    {
        final String className;
        if (this.isPortlet())
        {
            className = "javax.portlet.PortletRequest";
        }
        else
        {
            className = "javax.servlet.http.HttpServletRequest";
        }
        return className;
    }

    /**
     * @return className
     */
    public String getResponseClassName()
    {
        final String className;
        if (this.isPortlet())
        {
            className = "javax.portlet.PortletResponse";
        }
        else
        {
            className = "javax.servlet.http.HttpServletResponse";
        }
        return className;
    }

    /**
     * @return className
     */
    public String getSessionClassName()
    {
        final String className;
        if (this.isPortlet())
        {
            className = "javax.portlet.PortletSession";
        }
        else
        {
            className = "javax.servlet.http.HttpSession";
        }
        return className;
    }

    /**
     * @param buffer
     * @return the calculated SerialVersionUID
     */
    public static String calcSerialVersionUID(StringBuilder buffer)
    {
        final String signature = buffer.toString();
        String serialVersionUID = String.valueOf(0L);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(signature.getBytes());

            long hash = 0;
            for (int ctr = Math.min(
                        hashBytes.length,
                        8) - 1; ctr >= 0; ctr--)
            {
                hash = (hash << 8) | (hashBytes[ctr] & 0xFF);
            }
            serialVersionUID = String.valueOf(hash);
        }
        catch (final NoSuchAlgorithmException exception)
        {
            throw new RuntimeException("Error performing AngularAction.getFormSerialVersionUID",exception);
        }

        return serialVersionUID;
    }

    /**
     * @param string
     * @return Integer.valueOf(string) * 6000
     */
    public int calculateIcefacesTimeout(String string)
    {
        return string != null ? Integer.valueOf(string) * 6000 : 0;
    }

    public static String removeFormFromParams(String formCall) {
        
        int i = formCall.indexOf('(');
        int j = formCall.indexOf(')');

        String params = formCall.substring(i + 1, j).trim();

        String[] prms = params.split(",");
        StringBuilder builder = new StringBuilder();

        builder.append(formCall.substring(0, i+1));

        if(prms.length >= 1) {
            for(int k = 0; k < prms.length; k++) {
                
                builder.append("form." + prms[k].trim());
                builder.append(", ");
            }
        }

        builder.append(")");

        return builder.toString();
    }
    
    public static boolean isServiceOnly(ModelElementFacade obj) {

        boolean service = false;
        boolean webservice = false;
        for(String stereo : obj.getStereotypeNames()) {

            if(stereo.equals("Service")) {
                service = true;
            }

            if(stereo.equals("WebService")) {
                webservice = true;
            }
        }

        if(service && !webservice) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isWebService(ModelElementFacade obj) {

        boolean webservice = false;
        for(String stereo : obj.getStereotypeNames()) {

            if(stereo.equals("WebService")) {
                webservice = true;
            }
        }

        if(webservice) {
            return true;
        }
        
        return false;
    }

    public static List<FrontEndParameter> getFormFields(List<AngularAction> actions) {

        List<FrontEndParameter> fields = new ArrayList<>();
        Set<String> tmp = new HashSet<>();

        for(AngularAction action : actions) {

            for(FrontEndParameter parameter : action.getParameters()) {
                if(tmp.add(parameter.getName())) {
                    fields.add(parameter);
                }
            }
        }

        return fields;
    }

    public static Object checkTableLink(AngularParameter parameter) {

        return parameter.findTaggedValue(AngularProfile.ANGULAR_VIEW_VIEW_TYPE);

        //return false;
    }

    /**
     * <p> Returns true if java.lang.* or java.util.* datatype and not many*
     * </p>
     *
     * @param element the ClassifierFacade instance
     * @return if type is one of the PrimitiveTypes and not an array/list
     */
    public static boolean isSimpleType(ModelElementFacade element)
    {
        boolean simple = false;
        String typeName = null;
        ClassifierFacade type = null;
        boolean many = false;
        if (element instanceof AttributeFacade)
        {
            AttributeFacade attrib = (AttributeFacade)element;
            type = attrib.getType();
            many = attrib.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof AssociationEndFacade)
        {
            AssociationEndFacade association = (AssociationEndFacade)element;
            type = association.getType();
            many = association.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof ParameterFacade)
        {
            ParameterFacade param = (ParameterFacade)element;
            type = param.getType();
            many = param.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof AngularServiceParameterLogic)
        {
            AngularServiceParameterLogic param = (AngularServiceParameterLogic)element;
            type = param.getType();
            many = param.isMany() && !type.isArrayType() && !type.isCollectionType();
        }
        else if (element instanceof ClassifierFacade)
        {
            ClassifierFacade classifier = (ClassifierFacade)element;
            type = classifier;
        }
        else
        {
            return simple;
        }
        typeName = type.getFullyQualifiedName();
        if (type.isPrimitive() || typeName.startsWith("java.lang.") || typeName.startsWith("java.util.")
            || !typeName.contains(".") || type.isEnumeration())
        {
            if (!many)
            {
                simple = true;
            }
        }
        return simple;
    }

    public static String getTableColumnMessageKey(Object column, MetafacadeBase parent)
    {

        if(column instanceof AngularAttribute) {
            AngularAttribute attribute = (AngularAttribute) column;
            return attribute.getMessageKey();
        } else if(column instanceof AngularParameter) {
            AngularParameter parameter = (AngularParameter) column;
            return parameter.getMessageKey();
        } else if(column instanceof String) {

            if(parent instanceof AngularAttribute) {

                AngularAttribute attribute = (AngularAttribute) parent;
                final StringBuilder messageKey = new StringBuilder();
                // if (!attribute.isNormalizeMessages())
                // {
                //     final ClassifierFacade owner = attribute.getOwner();
                //     if (owner != null)
                //     {
                //         messageKey.append(StringUtilsHelper.toResourceMessageKey(owner.getName()));
                //         messageKey.append('.');
                //     }
                // }
                final String name = attribute.getName();
                if (name != null && name.trim().length() > 0)
                {
                    messageKey.append(StringUtilsHelper.toResourceMessageKey(name));
                }
                return messageKey.toString();

            } else if(column instanceof AngularParameter) {
                AngularParameter parameter = (AngularParameter) parent;
                return parameter.getTableColumnMessageKey((String) column);
            } else {
                return StringUtilsHelper.toResourceMessageKey((String) column);
            }
        }

        return null;
    }

    public static String getLastProperty(String source) {
        
        String[] splits = source.split("\\.");

        return splits[splits.length - 1];
    }

    public static String getDisplayConditionDeclaration(String condition) {

        if(condition.endsWith("()")) {

            return condition.substring(0, condition.length() - 2) + " = signal(false)";
        }

        return condition + " = false";
        
    }

    public FrontEndParameter getActionMatchingParameter(AngularParameter parameter, AngularAction action) {

        for(FrontEndParameter param : action.getFormFields()) {

            if(param.getName().equals(parameter.getName()) && param.getType().equals(parameter.getType())) {
                return param;
            }
        }

        return null;

    }

    public FrontEndParameter getViewMatchingParameter(AngularParameter parameter, AngularView view) {

        for(FrontEndParameter param : view.getVariables()) {

            if(param.getName().equals(parameter.getName()) && param.getType().equals(parameter.getType())) {
                return param;
            }
        }

        return null;

    }
}
