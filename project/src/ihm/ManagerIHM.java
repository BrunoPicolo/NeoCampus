/**
 * 
 */
package ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.LinkedHashSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import donnees.Capteur;
import donnees.CapteursTableModel;
import donnees.ManagerDonnees;
import serveur.Serveur;

/**
 * @author bruno
 *
 */
public class ManagerIHM implements Runnable {
	private static final int DEFAULT_PORT = 8952;
	
	private int portDEcouteCapteurs;
	private ManagerDonnees managerDonnees;
	private CapteursTableModel capteursTableModel;
	private int nbCapteurs = 0;
	private Serveur serveur;
	
	/**
	 * 
	 * @param managerDonnees
	 */
	public ManagerIHM(ManagerDonnees managerDonnees, CapteursTableModel capteursTableModel) {
		this.managerDonnees = managerDonnees;
		this.capteursTableModel = capteursTableModel;
	}
	
	// TODO changer le titre en "Port d'écoute des capteurs" ?
	private void fenetreDeConnexion() {
		JFrame connexion = new JFrame("Connexion");
		String strPort = JOptionPane.showInputDialog(connexion,
				"Numéro de port:", DEFAULT_PORT);
		if (strPort == null) 
			portDEcouteCapteurs = DEFAULT_PORT;
		else
			portDEcouteCapteurs = Integer.parseInt(strPort);
		connexion.dispose();
	}
	/**
	 * 
	 * @return
	 */
	private JPanel analyseurTempsReel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel titre = new JLabel("Analyseur Temps Réel");
		JTable tableau = new JTable(capteursTableModel);
		Box titreBox= new Box(BoxLayout.Y_AXIS); //set a definir la separation entre le titre et le tableau
		
		titreBox.add(titre);
		titreBox.add(Box.createVerticalStrut(10));
		
		panel.add(titreBox, BorderLayout.PAGE_START);
		panel.add(new JScrollPane(tableau),BorderLayout.CENTER);
		return panel;
	}
	/**
	 * 
	 * @return
	 */
	private JPanel analyseurDonnees() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel titre = new JLabel("Analyseur De Données");
		
		panel.add(titre,BorderLayout.PAGE_START);
		return panel;
	}
	/**
	 * 
	 * @return
	 */
	private JPanel arborescenceCapteurs() {
		JPanel panel = new JPanel(new BorderLayout());
		Box titreBox = new Box(BoxLayout.Y_AXIS);
		JLabel titre = new JLabel("Arborescence Capteurs");
		JTree arbre = new JTree();

		titreBox.add(titre);
		titreBox.add(Box.createVerticalStrut(10));
		panel.add(titreBox,BorderLayout.PAGE_START);
		panel.add(arbre);
		return panel;
	}
	
	private void fenetrePrincipale() {
		JFrame frame = new JFrame("NeoCampus");
		JPanel base = new JPanel(new BorderLayout());
		JPanel analysePanel = new JPanel(new BorderLayout());
		Box acContaier = new Box(BoxLayout.X_AXIS);
		JPanel arborescence = arborescenceCapteurs();
		JPanel donnees = analyseurDonnees();
		JPanel tempsReel = analyseurTempsReel();
		
		acContaier.add(arborescence);
		acContaier.add(Box.createHorizontalStrut(50));
		base.add(analysePanel,BorderLayout.CENTER);
		base.add(acContaier, BorderLayout.LINE_START);
		analysePanel.add(donnees,BorderLayout.PAGE_START);
		analysePanel.add(tempsReel,BorderLayout.PAGE_END);
		
//		JFrame parametrage
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension currentScreenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		frame.setSize(currentScreenSize);
		frame.getContentPane().add(base);
		frame.pack(); 
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
	}
	
	public void run() {
		fenetreDeConnexion();
		// Mise en route du serveur
		serveur = new Serveur(managerDonnees, "127.0.0.1", portDEcouteCapteurs);
		Thread threadServeur = new Thread(serveur);
		threadServeur.start();
		fenetrePrincipale();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedHashSet<Capteur> listeCapteurs = new LinkedHashSet<>();
		CapteursTableModel capteursTableModel = new CapteursTableModel(listeCapteurs);
		ManagerDonnees managerDonnees = new ManagerDonnees("localhost:3306", "root", "",
				listeCapteurs, capteursTableModel);
		
		javax.swing.SwingUtilities.invokeLater(new ManagerIHM(managerDonnees, capteursTableModel));
	}
}
