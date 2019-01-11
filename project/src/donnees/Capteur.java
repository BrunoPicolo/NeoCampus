package donnees;

import ihm.CapteursTableModel;

/**
 * 
 * @author bruno
 *
 */
public class Capteur implements Comparable<Capteur> {
	private CapteursTableModel capteursTableModel;
	private double seuilMin;
	private double seuilMax; 
	private String nom;
	private String batiment;
	private String lieu;
	private int etage;
	private TypeCapteur type;
	private double valeur;
	
	/**
	 * @param nom
	 * @param batiment
	 * @param lieu
	 * @param etage
	 * @param type
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
