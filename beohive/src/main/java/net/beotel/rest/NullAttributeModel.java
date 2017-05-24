package net.beotel.rest;

/**
 * Model koji nasledjuje ovu klasu moze poslati neke atribute sa null vrednosti rest-om
 * 
 * @author nenad
 *
 */
public class NullAttributeModel {
	/**
	 * Ovde se cuva nisak atributa koje cemo zaista slati sa null vrednostima. 
	 * Obicno se null vrednost ne salju, ali kod nove IPTV platforme neke vrednosti mogu biti poslate 
	 * kao null
	 */
	private String[] exceptAttributes;

	public String[] getExceptAttributes() {
		return exceptAttributes;
	}

	public void setExceptAttributes(String[] exceptAttributes) {
		this.exceptAttributes = exceptAttributes;
	}

}
