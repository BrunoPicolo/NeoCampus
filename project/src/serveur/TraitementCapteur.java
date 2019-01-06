package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import donnees.ManagerDonnees;
import donnees.TypeCapteur;

public class TraitementCapteur implements Runnable {
	private final Socket socket;
	private final ManagerDonnees managerDonnees;
	
	public TraitementCapteur(Socket socket, ManagerDonnees managerDonnees) {
		this.socket = socket;
		this.managerDonnees = managerDonnees;
	}
	
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
