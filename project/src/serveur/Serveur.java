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
 * Serveur écoutant les capteurs sur le réseau pour recueillir les mesures relevées
 * par ceux-ci.
 */
public class Serveur implements Runnable {
	private ManagerDonnees managerDonnees;
	private int port;
	
	/**
	 * Constructeur de la classe
	 * @param managerDonnees Instance du manager des données de l'application
	 * @param port Port sur lequel écouter les capteurs
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
	
	/**
	 * Créée un nouveau thread pour traiter la requête d'un capteur
	 * @param client Socket du capteur
	 */
	private void traiterConnexion(Socket client) {
		new Thread(new TraitementCapteur(client, managerDonnees)).start();
	}
}
