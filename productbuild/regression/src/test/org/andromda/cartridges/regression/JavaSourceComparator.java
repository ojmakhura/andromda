package org.andromda.cartridges.regression;

import java.io.File;

import xjavadoc.codeunit.CodeTestCase;

/**
 * Compares two Java source files. It checks if both file do exist and if the
 * APIs of both files are equal.
 *
 * @author Ralf Wirdemann
 * @author Chad Brandon
 */
public class JavaSourceComparator extends CodeTestCase {
	
    private File expectedFile;
    private File actualFile;

  public JavaSourceComparator(String methodName, File expectedFile, File actualFile) {
    	super();
    	this.setName(methodName);
        setExpectedFile(expectedFile);
        setActualFile(actualFile);
  }

    public void testAPIEquals() {
        assertTrue("expected file <" + getExpectedFile().getPath()
                + "> doesn't exist", getExpectedFile().exists());
        assertTrue("actual file <" + getActualFile().getPath()
                + "> doesn't exist", getActualFile().exists());
        super.assertApiEquals(expectedFile, getActualFile());
    }

    private File getExpectedFile() {
        return expectedFile;
    }

    private void setExpectedFile(File expectedFile) {
        this.expectedFile = expectedFile;
    }

    private File getActualFile() {
        return actualFile;
    }

    private void setActualFile(File actualFile) {
        this.actualFile = actualFile;
    }
}
