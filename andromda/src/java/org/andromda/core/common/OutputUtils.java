package org.andromda.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.log4j.Logger;


/**
 * I/O Utilities for writing files.
 * 
 * @author Chad Brandon
 */
public class OutputUtils {
	
	private static Logger logger = Logger.getLogger(OutputUtils.class);
	
	/**
	 * Writes the string to the file specified by the fileLocation
	 * argument.
	 * 
	 * @param string the string to write to the file
	 * @param file the file to which to write.
	 * @param overwrite if true, replaces the file (if it exists), otherwise adds
	 *        to the contents of the file.
	 * @throws IOException
	 */
	public static void writeStringToFile(
		String string, 
		File file, 
		boolean overwrite) throws IOException {
		final String methodName = "OutputUtils.writeStringToFile";
		ExceptionUtils.checkNull(methodName, "file", file);			
		writeStringToFile(string, file.toString(), overwrite);
	}

	/**
	 * Writes the string to the file specified by the fileLocation
	 * argument.
	 * 
	 * @param string the string to write to the file
	 * @param fileLocation the location of the file which to write.
	 * @param overwrite if true, replaces the file (if it exists), otherwise adds
	 *        to the contents of the file.
	 * @throws IOException
	 */
	public static void writeStringToFile(
		String string, 
		String fileLocation, 
		boolean overwrite) throws IOException {
			
		final String methodName = "OutputUtils.writeStringToFile";

		if (string == null) {
			string = "";
		}
		
		ExceptionUtils.checkEmpty(methodName, "fileLocation", fileLocation);
		
		if (logger.isDebugEnabled())
			logger.debug("performing " + methodName + 
				" with string '" + string + "' and fileLocation '" + 
				fileLocation + "'");
		File file = new File(fileLocation);
		File parent = file.getParentFile();
		if (parent != null){
			parent.mkdirs();
		}
		
		StringBuffer contents = new StringBuffer();	
		//only try to retrieve the contents if the file wasn't just created
		if (!file.createNewFile() && !overwrite) {
			Reader fileReader = new BufferedReader(new FileReader(file));
			for(int ctr = fileReader.read(); ctr != -1; ctr = fileReader.read()){
				contents.append((char)ctr);
			}	
		}

		Reader reader = 
			new BufferedReader(
				new StringReader(contents.append(string).toString()));	
		FileOutputStream stream = new FileOutputStream(file);	
		for(int ctr = reader.read(); ctr != -1; ctr = reader.read()){
			stream.write(ctr);
		}	
		
		stream.flush();
		stream.close();
		stream = null;
	}
}