/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import donnees.ManagerDonnees;

/**
 * @author bruno
 *
 */
public class Serveur implements Runnable {
	private ManagerDonnees managerDonnees;
	private int port;
	
	
	/**
	 * @param managerDonnes
	 * @param adresse
	 * @param port
	 */
	public Serveur(ManagerDonnees managerDonnees, int port) {
		super();
		this.managerDonnees = managerDonnees;
		this.port = port;
	}

	@Override
	public void run() {
		ServerSocket serveur;
		Socket client;
		try {
			serveur = new ServerSocket(port);
			do {
				client = serveur.accept();
				traiterConnexion(client);
			} while (true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void traiterConnexion(Socket client) {
		new Thread(new TraitementCapteur(client, managerDonnees)).start();
	}
}
