/**
 * 
 */
package ihm;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.PopupMenu;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.w3c.dom.events.EventException;

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
		tableau.setDefaultRenderer(Object.class, capteursTableCellRenderer);
		tableau.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int col = tableau.columnAtPoint(e.getPoint());
		        String nom = tableau.getColumnName(col);
		        switch (nom) {
		        	case "Fluide":
		        		if (capteursTableModel.getOrdreAffichage() == OrdreAffichage.FLUIDE) {
		        			capteursTableModel.setOrdreAffichage(OrdreAffichage.AUCUN);
		        		} else {
		        			capteursTableModel.setOrdreAffichage(OrdreAffichage.FLUIDE);
		        		}
		        		capteursTableModel.fireTableDataChanged();
		        		break;
		        	case "Bâtiment":
		        		if (capteursTableModel.getOrdreAffichage() == OrdreAffichage.BATIMENT) {
		        			capteursTableModel.setOrdreAffichage(OrdreAffichage.AUCUN);
		        		} else {
		        			capteursTableModel.setOrdreAffichage(OrdreAffichage.BATIMENT);
		        		}
		        		capteursTableModel.fireTableDataChanged();
		        		break;
		        }
		    }
		});
		tableau.getTableHeader().addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				Cursor defaultCursor = Cursor.getDefaultCursor();
				int col = tableau.columnAtPoint(e.getPoint());
				String nom = tableau.getColumnName(col);
				if (nom.equals("Fluide") || nom.equals("Bâtiment")) {
					tableau.getTableHeader().setCursor(handCursor);
				} else {
					tableau.getTableHeader().setCursor(defaultCursor);
				}
			}
		});
		tableau.getTableHeader().setDefaultRenderer(new CapteursTableCellHeaderRenderer());
		
		Box titreBox = new Box(BoxLayout.Y_AXIS); //set a definir la separation entre le titre et le tableau
		titreBox.add(titre);
		titreBox.add(Box.createVerticalStrut(10));
		
		panel.add(titreBox, BorderLayout.PAGE_START);
		panel.add(new JScrollPane(tableau),BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
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
		JComboBox<TypeCapteur> fluides = new JComboBox<>(TypeCapteur.values());getClass();
		Box boxF1 = new Box(BoxLayout.X_AXIS);
		Box boxF2 = new Box(BoxLayout.Y_AXIS);
		boxF1.add(Box.createHorizontalStrut(10));
		boxF1.add(fluides);
		boxF1.add(Box.createHorizontalGlue());
		boxF2.add(Box.createVerticalGlue());
		boxF2.add(boxF1);
		boxF2.add(Box.createVerticalGlue());
		choixFluide.add(new JLabel("Fluide:"), BorderLayout.PAGE_START);
		choixFluide.add(boxF2, BorderLayout.CENTER);

		JPanel choixCapteur = new JPanel(new BorderLayout());
		JList<Capteur> listeCapteurs = new JList<>();
		Box boxC1 = new Box(BoxLayout.X_AXIS);
		Box boxC2 = new Box(BoxLayout.Y_AXIS);
		listeCapteurs.setCellRenderer(new CapteursListCellRenderer());
		listeCapteurs.setSelectionModel(new CapteursListSelectionModel());
		JScrollPane capteurs = new JScrollPane(listeCapteurs,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		boxC1.add(Box.createHorizontalStrut(10));
		boxC1.add(capteurs);
		boxC1.add(Box.createHorizontalGlue());
		boxC2.add(Box.createVerticalGlue());
		boxC2.add(boxC1);
		boxC2.add(Box.createVerticalGlue());
		choixCapteur.add(new JLabel("Capteurs(max 3)"),BorderLayout.PAGE_START);
		choixCapteur.add(boxC2, BorderLayout.CENTER);
		
		// Date picker
		UtilDateModel model = new UtilDateModel();
		UtilDateModel model2 = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		Date today = new Date();
		// TODO Gerer le temps secondes (c'est le foutoir)
		model.setDate(today.getYear()+1900,today.getMonth(),today.getDate());
		model2.setDate(today.getYear()+1900,today.getMonth(),today.getDate());
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p);
		JDatePickerImpl dateMin = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		JDatePickerImpl dateMax = new JDatePickerImpl(datePanel2, new DateLabelFormatter());	
		// Fin Date Picker
		
		JPanel choixPeriode = new JPanel(new BorderLayout());
		JPanel flowPanel = new JPanel(new GridLayout(2, 1));
		JButton appliquer = new JButton("Appliquer");
		appliquer.setEnabled(false);
		flowPanel.add(dateMin);
		flowPanel.add(dateMax);
		choixPeriode.add(new Label("Periode:"), BorderLayout.PAGE_START);
		choixPeriode.add(flowPanel, BorderLayout.LINE_START);
		
		Box boxButton = new Box(BoxLayout.X_AXIS);
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(appliquer);
		Box boxGraphe1 = new Box(BoxLayout.X_AXIS);
		boxGraphe1.add(Box.createHorizontalStrut(20));
		boxGraphe1.add(graphe);

		Box titre = new Box(BoxLayout.X_AXIS);
		titre.add(new JLabel("Analyseur de données"));
		titre.add(Box.createHorizontalGlue());
		
		options.add(titre);
		options.add(Box.createVerticalStrut(5));
		options.add(choixFluide);
		options.add(choixCapteur);
		options.add(choixPeriode);
		options.add(boxButton);
		
		panel.add(options, BorderLayout.LINE_START);
		panel.add(boxGraphe1, BorderLayout.CENTER);
		
		fluides.addItemListener(event -> {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				TypeCapteur type = (TypeCapteur)event.getItem();
				itemChangedChoixFluide(type, listeCapteurs);
			}
		});
		listeCapteurs.addListSelectionListener(event -> {
			if (!event.getValueIsAdjusting()) {
				JList<Capteur> source = (JList<Capteur>)event.getSource();
				int[] selections = source.getSelectedIndices();
				appliquer.setEnabled(selections.length != 0);
			}
		});
		appliquer.addActionListener(event -> {
			 mouseClickedAppliquer(listeCapteurs.getSelectedValuesList(),dateMin,dateMax);
		});
		// valeurs par défaut de la liste de capteurs
		itemChangedChoixFluide(fluides.getItemAt(0), listeCapteurs);
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
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
		Arbre arbre = new Arbre(managerDonnees);
		JButton actualiserArbre = new JButton("Actualiser");
		Box box = new Box(BoxLayout.X_AXIS);
		
		box.add(Box.createHorizontalGlue());
		box.add(actualiserArbre);
		box.setBorder(BorderFactory.createLineBorder(Color.gray));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(arbre);
		split.setOneTouchExpandable(true);
		split.setDividerLocation(250);

		titreBox.add(titre);
		titreBox.add(Box.createVerticalStrut(10));
		panel.add(titreBox,BorderLayout.PAGE_START);
		panel.add(split,1);
		panel.add(box, BorderLayout.PAGE_END);
		
		actualiserArbre.addActionListener(event -> {
			panel.remove(1);
			split.setLeftComponent(new Arbre(managerDonnees)); 
//			split.setRightComponent(informationsCapteur); //TODO ajouter informations sur le capteur
			panel.add(split,1);
			panel.revalidate();
			panel.repaint();
		});
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		return panel;
	}
	
	private void fenetrePrincipale() {
		JFrame frame = new JFrame("NeoCampus");
		JPanel base = new JPanel(new GridLayout(2, 1));
		JPanel panel = new JPanel(new GridLayout(1,2));
		JPanel arborescence = arborescenceCapteurs();
		JPanel donnees = analyseurDonnees();
		JPanel tempsReel = analyseurTempsReel();
		
		panel.add(arborescence);
		panel.add(tempsReel);
		base.add(donnees);
		base.add(panel);
		
//		JFrame parametrage
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		Dimension currentScreenSize = new Dimension(1000,600); // s'on enleve frame.pack() alors c'est la taille de la fenetre 
//		frame.setSize(currentScreenSize);
		frame.getContentPane().add(base);
		frame.pack();
		frame.setResizable(true); // l'utilisateur ne peut pas modifier la taille de la fenetre
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	

	// Méthode appelée lors du clic de souris sur le bouton "Appliquer"
	private void mouseClickedAppliquer(List<Capteur> capteurs,
			JDatePickerImpl dateMin,
			JDatePickerImpl dateMax) { 
		Map<Capteur,List<Mesure>> donnees = new HashMap<>();
		Date dateMini = (Date) dateMin.getModel().getValue();
		Date dateMaxi = (Date) dateMax.getModel().getValue();
		for (Capteur capteur : capteurs) {
			donnees.put(capteur, this.managerDonnees.mesuresPeriode(capteur, dateMini, dateMaxi));
		}
		graphe.afficher(donnees);
	}
	
	private void itemChangedChoixFluide(TypeCapteur type, JList<Capteur> listeCapteurs) {
		List<Capteur> capteurs = managerDonnees.getCapteursBD(type.name());
		Capteur[] tableauCapteurs = capteurs.toArray(new Capteur[0]);
		listeCapteurs.setListData(tableauCapteurs);
	}
	
	public void run() {
		//fenetreDeConnexion();
		// Mise en route du serveur
		serveur = new Serveur(managerDonnees, portDEcouteCapteurs);
		Thread threadServeur = new Thread(serveur);
		threadServeur.start();
		fenetrePrincipale();
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Set<Capteur> listeCapteurs = new TreeSet<>();
		// TODO declarer a l'intérieur des méthodes créant l'IHM
		CapteursTableModel capteursTableModel = new CapteursTableModel(listeCapteurs);
		CapteursTableCellRenderer capteursTableCellRenderer = new CapteursTableCellRenderer(listeCapteurs);
		ManagerDonnees managerDonnees = new ManagerDonnees("localhost:3306", "root", "",
				listeCapteurs, capteursTableModel);
		
		javax.swing.SwingUtilities.invokeLater(new ManagerIHM(managerDonnees,
				capteursTableModel, capteursTableCellRenderer));
	}
}
