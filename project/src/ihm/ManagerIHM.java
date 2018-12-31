/**
 * 
 */
package ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import donnees.ManagerDonnees;

/**
 * @author bruno
 *
 */
public class ManagerIHM {
	private static final int DEFAULT_PORT = 8952;
	private ManagerDonnees managerDonnees;
	private static int port;
	private static Object[][] donneesCapteurs;
	private int nbCapteurs = 0;
	private static final String[] entete = {"Capteur","Fluide","Batiment","Etage","Lieu","Seuil min","Seuil max"};
	
	/**
	 * 
	 * @param managerDonnees
	 */
	public ManagerIHM(ManagerDonnees managerDonnees) {
		this.managerDonnees = managerDonnees;
	}
	
	// TODO changer le titre en "Port d'écoute des capteurs" ?
	private static void fenetreDeConnexion() {
		JFrame connexion = new JFrame("Connexion");
		String strPort = JOptionPane.showInputDialog(connexion,
				"Numéro de port:", DEFAULT_PORT);
		if (strPort == null) 
			port = DEFAULT_PORT;
		else
			port = Integer.parseInt(strPort);
		connexion.dispose();
	}
	
	private static void fenetrePrincipale() {
//		Composant principaux
		JFrame frame = new JFrame("NeoCampus");
		JPanel base = new JPanel(new BorderLayout());
		JPanel analysePanel = new JPanel(new BorderLayout());
		
		JPanel arborescenceCapteurs = new JPanel(new BorderLayout());
		JPanel analyseurDonnees = new JPanel(new BorderLayout());
		JPanel analyseTempsReel = new JPanel(new BorderLayout());
		
		base.add(arborescenceCapteurs, BorderLayout.LINE_START);
		base.add(analysePanel,BorderLayout.LINE_END);
	
		analysePanel.add(analyseurDonnees,BorderLayout.PAGE_START);
		analysePanel.add(analyseTempsReel,BorderLayout.PAGE_END);
		
//		analyseTempsReel arrangement des composants 
		JLabel titreATR = new JLabel("Analyseur Temps Réel");
		JTable tableau = new JTable(donneesCapteurs,entete);
		
		analyseTempsReel.add(titreATR, BorderLayout.PAGE_START);
		analyseTempsReel.add(tableau.getTableHeader(),BorderLayout.CENTER);
		//analyseTempsReel.add(tableau,BorderLayout.CENTER); //Pour afficher les données, Erreur si 0 capteurs
		
//		analyseur De Données arrangement des composants
		JLabel titreAD = new JLabel("Analyseur De Données");
		analyseurDonnees.add(titreAD,BorderLayout.PAGE_START);
		
//		arborescence  de capteurs arrangement des composants
		JLabel titreAC = new JLabel("Arborescence Capteurs");
		arborescenceCapteurs.add(titreAC,BorderLayout.PAGE_START);
		
//		JFrame parametrage
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension currentScreenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		frame.setSize(currentScreenSize);
		frame.getContentPane().add(base);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.pack(); // à enlever après les tests!
		frame.setVisible(true);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				fenetreDeConnexion();
				fenetrePrincipale();
			}
		});
	}
}
