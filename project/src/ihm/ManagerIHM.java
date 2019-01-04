/**
 * 
 */
package ihm;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.LinkedHashSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import donnees.Capteur;
import donnees.CapteursTableModel;
import donnees.ManagerDonnees;
import donnees.TypeCapteur;
import javafx.scene.layout.Border;
import serveur.Serveur;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;


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
	
	// TODO changer le titre en "Port d'�coute des capteurs" ?
	private void fenetreDeConnexion() {
		JFrame connexion = new JFrame("Connexion");
		String strPort = JOptionPane.showInputDialog(connexion,
				"Num�ro de port:", DEFAULT_PORT);
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
		JLabel titre = new JLabel("Analyseur Temps R�el");
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
		JLabel titre = new JLabel("Analyseur De Donn�es");
		JPanel optionPanel = new JPanel(new BorderLayout());
		JPanel choixFluide = new JPanel(new BorderLayout());
		JPanel choixCapteur = new JPanel(new BorderLayout());
		JPanel choixPeriode = new JPanel(new BorderLayout());
		JButton appliquer = new JButton("Appliquer");
		JComboBox<TypeCapteur> fluides = new JComboBox<>();// je ne sais pas tres bien l'utiliser, je crois que c'est comme �a 
		JScrollPane capteurs = new JScrollPane();
		
		Box titreBox = new Box(BoxLayout.Y_AXIS);
		Box capteurBox = new Box(BoxLayout.Y_AXIS);
		
		titreBox.add(titre);
		titreBox.add(Box.createHorizontalGlue()); //J'essaye de cr�er un espace entre le titre et les autres composants(�a ne marche pas...)
		
		//ajout de composants pour le choix des fluides
		choixFluide.add(new JLabel("Type Fluide:"), BorderLayout.PAGE_START);
		choixFluide.add(fluides, BorderLayout.CENTER);
		
		//ajout de composants pour le choix d'un capteur
		capteurBox.add(new JLabel("Capteurs(max 3):"));
		capteurBox.add(capteurs);
		choixCapteur.add(appliquer,BorderLayout.CENTER);
		choixCapteur.add(capteurBox, BorderLayout.LINE_START);
		
		//de meme pour la periode
		choixPeriode.add(new JLabel("Periode:"), BorderLayout.PAGE_START);
		
		//ajout des trois paneux de choix dans le paneu d'options
		optionPanel.add(choixFluide, BorderLayout.PAGE_START);
		optionPanel.add(choixCapteur,BorderLayout.CENTER);
		optionPanel.add(choixPeriode, BorderLayout.PAGE_END);
		
		panel.add(titre,BorderLayout.PAGE_START);
		panel.add(optionPanel,BorderLayout.LINE_START);
		
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
		JPanel arborescence = arborescenceCapteurs();
		JPanel donnees = analyseurDonnees();
		JPanel tempsReel = analyseurTempsReel();
		
		JSplitPane barre1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,arborescence,analysePanel);
		JSplitPane barre2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,donnees,tempsReel);

		analysePanel.add(barre2);
		base.add(barre1);
		
//		JFrame parametrage
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension currentScreenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		frame.setSize(currentScreenSize);
		frame.getContentPane().add(base);
		frame.pack();
		frame.setResizable(false); // l'utilisateur ne peut pas modifier la taille de la fenetre
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
		//Lancement du simulateur
		File path = new File("exec.bat");
		Runtime simulateur = Runtime.getRuntime();
		try {
			simulateur.exec("cmd.exe /k " + path);
		}catch (Exception e) {
			System.err.println(e);
		}
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
