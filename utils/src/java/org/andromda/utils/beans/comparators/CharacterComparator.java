package org.andromda.utils.beans.comparators;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

/**
 * Used to sort by Character values
 * 
 * @author Chad Brandon
 */
class CharacterComparator implements Comparator, Serializable  {
	
	private Collator collator = null;
	
	/**
	 * Used to sort Character values, both objects are assumed to be assignable
	 * to java.util.Character
	 */
	public int compare(Object objectA, Object objectB) {
		Character aAsCharacter = (Character)objectA;
		Character bAsCharacter = (Character)objectB;
		this.initializeCollator();
		return collator.compare(
			Character.toString(aAsCharacter.charValue()), 
			Character.toString(bAsCharacter.charValue()));		
	}
	
	/**
	 * Initializes the Collator
	 */
	private void initializeCollator() {
		if (this.collator == null) {
			this.collator = Collator.getInstance();
			//set to ignore case
			collator.setStrength(Collator.PRIMARY);
		}
	}	

}
