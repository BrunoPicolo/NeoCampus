/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package donnees;

import java.util.Date;

public class Mesure {
	private Double valeur;
	private Date date;

	/**
	 * 
	 * @param valeur
	 * @param date
	 */
	public Mesure(Double valeur, Date date) {
		this.valeur = valeur;
		this.date = date;
	}

	public Double getValeur() {
		return valeur;
	}

	public Date getDate() {
		return date;
	}

	/**
	 * 
	 * @param valeur
	 */
	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}

	/**
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
