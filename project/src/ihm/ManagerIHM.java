/**
 * 
 */
package ihm;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import donnees.ManagerDonnees;

/**
 * @author bruno
 *
 */
public class ManagerIHM {
	private static final int DEFAULT_PORT = 8952;
	private ManagerDonnees managerDonnees;
	private static int port;
	
	public ManagerIHM(ManagerDonnees managerDonnees) {
		this.managerDonnees = managerDonnees;
	}
	
	// TODO changer le titre en "Port d'écoute des capteurs" ?
	private static void fenetreDeConnexion() {
		JFrame connexion = new JFrame("Connexion");
		String strPort = JOptionPane.showInputDialog(connexion,
				"Numéro de port:", DEFAULT_PORT);
		port = Integer.parseInt(strPort);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				fenetreDeConnexion();
			}
		});
	}
}
