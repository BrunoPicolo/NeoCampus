/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package donnees;

import java.util.Date;

/**
 * Mesure relev�e par un capteur
 */
public class Mesure {
	/** Valeur relev�e */
	private Double valeur;
	/** Date du relev� */
	private Date date;

	/**
	 * Construit une mesure
	 * @param valeur Valeur relev�e
	 * @param date Date du relev�
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
