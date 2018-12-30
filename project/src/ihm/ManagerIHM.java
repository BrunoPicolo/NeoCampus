/**
 * 
 */
package ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import donnees.ManagerDonnees;

/**
 * @author bruno
 *
 */
public class ManagerIHM {
	private static final int DEFAULT_PORT = 8952;
	private ManagerDonnees managerDonnees;
	private static int port;
	
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
		fenetrePrincipale();
	}
	
	private static void fenetrePrincipale() {
		JFrame frame = new JFrame("NeoCampus");
		JPanel base = new JPanel();
		JPanel arborescenceCapteurs = new JPanel();
		JPanel analyseurDonnees = new JPanel();
		JPanel analyseTempsReel = new JPanel();
		BorderLayout borderlayout = new BorderLayout();
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension currentScreenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		frame.setSize(currentScreenSize);
		
		arborescenceCapteurs.add(new JButton("touch me"));
		analyseTempsReel.add(new JButton("suck me"));
		analyseurDonnees.add(new JButton("love me"));
		
		base.setLayout(borderlayout);
		base.add(arborescenceCapteurs, BorderLayout.LINE_START);
		base.add(analyseurDonnees,BorderLayout.LINE_END);
		base.add(analyseTempsReel,BorderLayout.CENTER);
		
		
		frame.getContentPane().add(base);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
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
