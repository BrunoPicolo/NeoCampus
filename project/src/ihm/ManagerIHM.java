/**
 * 
 */
package ihm;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.jfree.chart.ChartPanel;

import donnees.Capteur;
import donnees.ManagerDonnees;
import donnees.Mesure;
import donnees.TypeCapteur;
import serveur.Serveur;

/**
 * @author bruno
 *
 */
public class ManagerIHM implements Runnable {
	private static final int DEFAULT_PORT = 8952;
	
	private int portDEcouteCapteurs = DEFAULT_PORT;
	private ManagerDonnees managerDonnees;
	private CapteursTableModel capteursTableModel;
	private CapteursTableCellRenderer capteursTableCellRenderer;
	private int nbCapteurs = 0;
	private Serveur serveur;
	
	private Graphe graphe;

	
	/**
	 * 
	 * @param managerDonnees
	 */
	public ManagerIHM(ManagerDonnees managerDonnees, CapteursTableModel capteursTableModel,
			CapteursTableCellRenderer capteursTableCellRenderer) {
		this.managerDonnees = managerDonnees;
		this.capteursTableModel = capteursTableModel;
		this.capteursTableCellRenderer = capteursTableCellRenderer;
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
		tableau.setDefaultRenderer(Object.class, capteursTableCellRenderer);
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
		
		Box options = new Box(BoxLayout.Y_AXIS);
		graphe = new Graphe();
		
		JPanel choixFluide = new JPanel(new BorderLayout());
		JComboBox<TypeCapteur> fluides = new JComboBox<>(TypeCapteur.values());
		choixFluide.add(new JLabel("Fluide:"), BorderLayout.PAGE_START);
		choixFluide.add(fluides, BorderLayout.CENTER);
		
		JPanel choixCapteur = new JPanel(new BorderLayout());
		JList<String> listeCapteurs = new JList();
		listeCapteurs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane capteurs = new JScrollPane(listeCapteurs,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		choixCapteur.add(new JLabel("Capteurs(max 3)"),BorderLayout.PAGE_START);
		choixCapteur.add(capteurs, BorderLayout.CENTER);
		
		JPanel choixPeriode = new JPanel(new BorderLayout());
		JPanel flowPanel = new JPanel(new FlowLayout());
		Box dateMin = new Box(BoxLayout.Y_AXIS);
		JFormattedTextField min = new JFormattedTextField();
		dateMin.add(new Label("D�but:"));
		dateMin.add(min);
		Box dateMax = new Box(BoxLayout.Y_AXIS);
		dateMax.add(new Label("Fin:"));
		JFormattedTextField max = new JFormattedTextField();
		dateMax.add(max);
		JButton appliquer = new JButton("Appliquer");
		flowPanel.add(dateMin);
		flowPanel.add(dateMax);
		choixPeriode.add(new Label("Periode:"), BorderLayout.PAGE_START);
		choixPeriode.add(flowPanel, BorderLayout.LINE_START);
		
		options.add(choixFluide);
		options.add(choixCapteur);
		options.add(choixPeriode);
		options.add(appliquer);

		panel.add(new JLabel("Analyseur de donn�es"), BorderLayout.PAGE_START);
		panel.add(options, BorderLayout.LINE_START);
		panel.add(graphe, BorderLayout.LINE_END);
		
		fluides.addItemListener((ItemEvent event) -> {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				TypeCapteur type = (TypeCapteur)event.getItem();
				itemChangedChoixFluide(type, listeCapteurs);
			}
		});

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
		Arbre arbre = new Arbre();

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
		JPanel arborescence =arborescenceCapteurs();
		JPanel donnees = analyseurDonnees();
		JPanel tempsReel = analyseurTempsReel();
		
		donnees.setMinimumSize(new Dimension(800, 300));
		JSplitPane barre1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,arborescence,analysePanel);
		JSplitPane barre2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,donnees,tempsReel);

		analysePanel.add(barre2);
		base.add(barre1);
		
//		JFrame parametrage
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension currentScreenSize = new Dimension(1000,600); // s'on enleve frame.pack() alors c'est la taille de la fenetre 
		frame.setSize(currentScreenSize);
		frame.getContentPane().add(base);
//		frame.pack();
		frame.setResizable(true); // l'utilisateur ne peut pas modifier la taille de la fenetre
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
	}
	

	//M�thode appel�e lors du clic de souris
	// Creer une nouvelle classe pour le bouton appliquer et faire un bouton personnalis�
	public void mouseClickedGraphe(MouseEvent event) { 
		Map<Capteur,List<Mesure>> donnees = new TreeMap<>();
		for (Capteur c : this.managerDonnees.getCapteursConnectes()) {
			if (c.getType().equals(TypeCapteur.EAU)) {

				donnees.put(c, this.managerDonnees.mesuresPeriode(c,new Date(),new Date()));
			}
		}
		graphe.afficher(donnees);
	}
	
	public void itemChangedChoixFluide(TypeCapteur type, JList listeCapteurs) {
		List<Capteur> capteurs = managerDonnees.getCapteursBD(type.name());
		Object[] nomsCapteurs = capteurs.stream()
				.map(capteur -> capteur.getNom())
				.toArray();
		listeCapteurs.setListData(nomsCapteurs);
	}
	
	public void run() {
		//fenetreDeConnexion();
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
		CapteursTableCellRenderer capteursTableCellRenderer = new CapteursTableCellRenderer(listeCapteurs);
		ManagerDonnees managerDonnees = new ManagerDonnees("localhost:3306", "root", "",
				listeCapteurs, capteursTableModel);
		
		javax.swing.SwingUtilities.invokeLater(new ManagerIHM(managerDonnees,
				capteursTableModel, capteursTableCellRenderer));
	}
}
