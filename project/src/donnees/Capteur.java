package donnees;

import java.util.ArrayList;

/**
 * 
 * @author bruno
 *
 */

public class Capteur {
	
	private CapteursTableModel capteursTable;
	private ArrayList<CapteursTableModel> modele = new ArrayList<>();
	protected double SEUIL_MIN; //Je ne sais pas si c'est le meme pour des capteurs de different type
	protected double SEUIL_MAX; 
	private String nom;
	private String batiment;
	private String lieu;
	private int etage;
	private TypeCapteur type;
	
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
	}
	
		
	
	
}
