/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import donnees.ManagerDonnees;
import donnees.TypeCapteur;

/**
 * Traite la requête d'un capteur
 */
public class TraitementCapteur implements Runnable {
	/** Socket du capteur */
	private final Socket socket;
	/** Instance du manager des données de l'application */
	private final ManagerDonnees managerDonnees;
	
	/**
	 * Constructeur de la classe
	 * @param socket Socket du capteur
	 * @param managerDonnees Instance du manager des données de l'application
	 */
	public TraitementCapteur(Socket socket, ManagerDonnees managerDonnees) {
		this.socket = socket;
		this.managerDonnees = managerDonnees;
	}
	
	/**
	 * Traite la requête d'un capteur
	 * Analyse la chaîne de caractères reçue et agit en conséquence
	 * @param requete Chaîne de caractère émise par le capteur
	 */
	private void traiterRequete(String requete) {
		String[] requeteSplit = requete.split(" ");
		if (requeteSplit.length < 2) return;
		
		String nom = requeteSplit[1];
		switch (requeteSplit[0]) {
			case "Connexion":
				String[] description = requeteSplit[2].split(":");
				managerDonnees.connecterCapteur(nom,
						description[1], // batiment
						description[3], // lieu
						Integer.parseInt(description[2]), // etage
						TypeCapteur.valueOf(description[0]));
				break;
			
			case "Donnee":
				managerDonnees.actualiserValeurCapteur(nom, Double.parseDouble(requeteSplit[2]));
				break;
				
			case "Deconnexion":
				managerDonnees.deconnecterCapteur(nom);
		}
	}

	@Override
	public void run() {
		try(BufferedReader reader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()))) {
			boolean continuer = true;
			do {
				try {
					String requete = reader.readLine();
					if (requete != null) {
						traiterRequete(requete);
					}
		        } catch (SocketException e) {
		        	continuer = false;
		        }
			} while (continuer);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
