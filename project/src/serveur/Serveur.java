/**
 * 
 */
package serveur;

import donnees.ManagerDonnees;

/**
 * @author bruno
 *
 */
public class Serveur {
	
	private ManagerDonnees managersDonnees;
	private String adresse;
	private int port;
	private boolean connexion = false;
	
	
	/**
	 * @param managersDonnes
	 * @param adresse
	 * @param port
	 */
	public Serveur(ManagerDonnees managersDonnees, String adresse, int port) {
		super();
		this.managersDonnees = managersDonnees;
		this.adresse = adresse;
		this.port = port;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean deconnexion() {
		boolean cx = false; // demander traitement de managerDonnees
		return cx;
	}




	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO server main
	}

}
