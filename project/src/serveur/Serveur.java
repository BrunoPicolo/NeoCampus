/**
 * 
 */
package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import donnees.ManagerDonnees;

/**
 * @author bruno
 *
 */
public class Serveur implements Runnable {
	private ManagerDonnees managerDonnees;
	private String adresse; // TODO supprimer l'adresse ?
	private int port;
	
	
	/**
	 * @param managerDonnes
	 * @param adresse
	 * @param port
	 */
	public Serveur(ManagerDonnees managerDonnees, String adresse, int port) {
		super();
		this.managerDonnees = managerDonnees;
		this.adresse = adresse;
		this.port = port;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean deconnexion() {
		// TODO utile ?
		return false;
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
