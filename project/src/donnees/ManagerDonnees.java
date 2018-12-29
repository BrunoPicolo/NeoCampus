/**
 * 
 */
package donnees;

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
}
