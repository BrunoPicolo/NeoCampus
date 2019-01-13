/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package donnees;

import ihm.CapteursTableModel;

/**
 * Représente un capteurs en BD ou connecté à l'application
 */
public class Capteur implements Comparable<Capteur> {
	/** Modèle personnalisé de JTable */
	private CapteursTableModel capteursTableModel;
	private double seuilMin;
	private double seuilMax; 
	private String nom;
	private String batiment;
	private String lieu;
	private int etage;
	/** Type de fluide du capteur */
	private TypeCapteur type;
	/** Valeur actuelle du capteur */
	private double valeur;
	
	/**
	 * Construit un capteur
	 * @param nom Nom du capteur
	 * @param batiment Batîment où se situe le capteur
	 * @param lieu Lieu précis où se trouve le capteur
	 * @param etage Etage du batîment dans lequel se situe le capteur
	 * @param type Type de fluide du capteur
	 */
	public Capteur(String nom, String batiment, String lieu, int etage, TypeCapteur type) {
		super();
		this.nom = nom;
		this.batiment = batiment;
		this.lieu = lieu;
		this.etage = etage;
		this.type = type;
		this.seuilMin = type.getSeuilMin();
		this.seuilMax = type.getSeuilMax();
	}

	public double getSeuilMin() {
		return seuilMin;
	}

	public double getSeuilMax() {
		return seuilMax;
	}

	public void setSeuilMin(double seuilMin) {
		this.seuilMin = seuilMin;
	}

	public void setSeuilMax(double seuilMax) {
		this.seuilMax = seuilMax;
	}
	
	public String getNom() {
		return nom;
	}

	public String getBatiment() {
		return batiment;
	}

	public String getLieu() {
		return lieu;
	}

	public int getEtage() {
		return etage;
	}
	
	public double getValeur() {
		return valeur;
	}

	public TypeCapteur getType() {
		return type;
	}

	public void setModele(CapteursTableModel modele) {
		capteursTableModel = modele;
	}
	
	/**
	 * Actualise la valeur actuelle du capteur et la met à jour dans l'analyse
	 * en temps réelle
	 * @param valeur Nouvelle valeur du capteur
	 */
	public void actualiserValeur(double valeur) {
		this.valeur = valeur;
		if (capteursTableModel != null) {
			capteursTableModel.fireTableDataChanged();
		}
	}
	
	@Override
	public int hashCode() {
		return 31 * (nom.hashCode() + batiment.hashCode() + etage + lieu.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Capteur) {
			Capteur capteur = (Capteur)obj;
			return hashCode() == capteur.hashCode();
		}
		return true;
	}

	@Override
	public int compareTo(Capteur capteur) {
		return nom.compareTo(capteur.nom);
	}
}
