/**
 * 
 */
package donnees;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author bruno
 *
 */
public class ManagerDonnees {
	// TODO champ représentant la connexion BD
	private LinkedHashSet<Capteur> capteursConnectes;
	private String adresse;
	private String identifiant;
	private String motDePasse;
	
	/**
	 * @param adresse
	 * @param identifiant
	 * @param motDePasse
	 */
	public ManagerDonnees(String adresse, String identifiant, String motDePasse,
			LinkedHashSet<Capteur> capteursConnectes) {
		super();
		this.adresse = adresse;
		this.identifiant = identifiant;
		this.motDePasse = motDePasse;
		this.capteursConnectes = capteursConnectes;
	}
	
	/**
	 * 
	 * @param nomCapteur
	 * @param valeur
	 */
	public void actualiserValeurCapteur(String nomCapteur, double valeur) {
		for(Capteur capteur : capteursConnectes) {
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
		capteursConnectes.add(capteur);
	}
	
	public List<Double> mesuresPeriode(Capteur capteur, Date dateMin, Date DateMax) {
		List<Double> mesures = new ArrayList<>();
		//TODO demander a la BD
		return mesures;
	}
	
	public List<Capteur> getCapteursBD(TypeCapteur type){
		List<Capteur> capteurs = new ArrayList<>();
		// TODO demander a la BD
		return capteurs;
	}
	
	public LinkedHashSet<Capteur> getCapteursConnectes() {
		return capteursConnectes;
	}
	
	public void deconnecterCapteur(Capteur capteur) {
		capteursConnectes.remove(capteur);
	}
}
