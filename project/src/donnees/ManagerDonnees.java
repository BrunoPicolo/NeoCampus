/**
 * 
 */
package donnees;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @author bruno
 *
 */
public class ManagerDonnees {
	
	private Connection connectionBD;
	private NavigableSet<Capteur> capteurConnectes;
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
		this.capteurConnectes = new TreeSet<>();
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
	
	public List<TypeCapteur> getCapteursBD(TypeCapteur type){
		List<TypeCapteur> capteurs = new ArrayList<>();
		//TODO meme chose que methode precedente
		return capteurs;
	}
	
	public List<TypeCapteur> getCapteursConnectes(){
		List<TypeCapteur> capteurs = new ArrayList<>();
		//TODO meme chose que methode precedente
		return capteurs;
	}
	
	public void deconnecterCapteur(Capteur capteur) {
		
	}
	
}
