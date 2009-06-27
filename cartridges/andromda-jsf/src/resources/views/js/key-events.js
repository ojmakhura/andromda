//This file contains methods for cross browser scripting dealing with key events
//(ie. onKeyPress, onKeyUp, onKeyDown, etc)

/**
 * This method returns true if the enter key was pressed, false otherwise.
 */
function enterKeyPressed(event) 
{
	//13 = enter
	return keyPressed(event, 13);
}

/**
 * This method returns true if the tab key was pressed, false otherwise.
 */
function tabKeyPressed(event) 
{
	//9 = enter
	return keyPressed(event, 9);
}

/**
 * This method returns true if the key having the specified charCode was pressed.
 */ 
function keyPressed(event, charCode) 
{
	var keyPressed = false;
    if (getKeyCharCode(event) == charCode) 
    {
    	keyPressed = true;
    }

	return keyPressed;
}

/**
 * This method returns the charCode value of the key pressed.
 */
function getKeyCharCode(event) 
{
	var key = null;
	if (window.event) {
		//ie
		key = window.event.keyCode;
	} else if (event.which) {
		key = event.which;
	}
	return key;
}

/**
 * Prevents non-numeric values from being keyed into a field
 */
function isNumeric(event) 
{
	// get the ASCII key code of the onKeyPress event
	
	var isMozilla = false;
	if (!window.event && event.which) 
	{
		isMozilla = true;
	}
	var key = getKeyCharCode(event);
	var isNumeric = false;	
	if (key != null) 
	{
		if (key >= 48 && key <= 57)	 
		{
			// key is numeric
			isNumeric = true;
		} 
		else if (key == 46) 
		{
			// key is a decimal point
			isNumeric = true;
		} 
		else if (key == 13) 
		{
			//13 is a return
			isNumeric = true;
		} 
		else if (key == 8 && isMozilla) 
		{
			//8 is a backspace with mozilla
			isNumeric = true;
		}
	} 
	else 
	{
		isNumeric = true;
	}
	return isNumeric;
}

/**
 * Returns true if the entered key is the specified charCode value pressed
 * in conjunction with the ctrl key.
 */
function keyIsCtrlWithCharCode(event, charCode) 
{
	var isCharCode = false;
	//ie
	if (window.event) 
	{
		if (window.event.ctrlKey && window.event.keyCode == charCode) 
		{
			isCharCode = true;			
		}
	}
	//mozilla
	if (event.ctrlKey && event.which == charCode) 
	{
		isCharCode = true;
	}
	return isCharCode;
}

/**
 * Returns true if the entered key is the specified charCode value pressed
 * in conjunction with the shift key.
 */
function keyIsShiftWithCharCode(event, charCode) 
{
	var isCharCode = false;
	//ie
	if (window.event) 
	{
		if (window.event.shiftKey && window.event.keyCode == charCode) 
		{
			isCharCode = true;			
		}
	}
	//mozilla
	if (event.shiftKey && event.which == charCode) 
	{
		isCharCode = true;
	}
	return isCharCode;
}

/**
 * Returns true if the entered key is the specified charCode value pressed
 * in conjunction with the alt key.
 */
function keyIsAltWithCharCode(event, charCode) 
{
	var isCharCode = false;
	//ie
	if (window.event) 
	{
		if (window.event.altKey && window.event.keyCode == charCode) 
		{
			isCharCode = true;			
		}
	}
	//mozilla
	if (event.altKey && event.which == charCode) 
	{
		isCharCode = true;
	}
	return isCharCode;
}

