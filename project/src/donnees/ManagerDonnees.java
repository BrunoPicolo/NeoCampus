/**
 * 
 */
package donnees;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author bruno
 *
 */
public class ManagerDonnees {
	private HashSet<Capteur> capteurConnectes;
	private String adresse;
	private String identifiant;
	private String motDePasse;
	/**
	 * @param adresse
	 * @param identifiant
	 * @param motDePasse
	 */
	public ManagerDonnees(String adresse, String identifiant, String motDePasse) {
		super();
		this.adresse = adresse;
		this.identifiant = identifiant;
		this.motDePasse = motDePasse;
		this.capteurConnectes = new HashSet<>();
	}
	
	/**
	 * 
	 * @param nomCapteur
	 * @param valeur
	 */
	public void actualiserValeurCapteur(String nomCapteur, double valeur) {
		for(Capteur capteur : capteurConnectes) {
			if (capteur.getNom().equals(nomCapteur)) {
				capteur.actualiserValeur(valeur);
			}
		}
	}
	/**
	 * 
	 * @param nom
	 * @param batiment
	 * @param lieu
	 * @param etage
	 * @param type
	 */
	public void connecterCapteur(String nom, String batiment, String lieu, int etage, TypeCapteur type) {
		Capteur capteur = new Capteur(nom, batiment, lieu, etage, type);
		capteurConnectes.add(capteur);
	}
	
	public List<Double> mesuresPeriode(Capteur capteur, Date dateMin, Date DateMax) {
		List<Double> mesures = new ArrayList<>();
		//TODO demander a capteursTableModel les valeurs pendant la periode
		return mesures;
	}
	/**
	 *REVISER, je crois que ce n'est pas bon
	 */
	public List<Capteur> getCapteursBD(TypeCapteur type){
		List<Capteur> capteurs = new ArrayList<>();
		for (Capteur c : capteurConnectes) {
			if (c.getType().equals(type)) {
				capteurs.add(c);
			}
		}
		return capteurs;
	}
	
	public List<Capteur> getCapteursConnectes(){
		List<Capteur> capteurs = new ArrayList<>();
		capteurs.addAll(capteurConnectes);
		return capteurs;
	}
	
	public void deconnecterCapteur(Capteur capteur) {
		capteurConnectes.remove(capteur);
	}
	
}
