package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import donnees.Capteur;
import donnees.ManagerDonnees;
import donnees.Mesure;
import donnees.TypeCapteur;

public class AnalyseurDonnees extends JPanel {

	private Graphe graphe = new Graphe();
	private ManagerDonnees managerDonnees;
	public AnalyseurDonnees(ManagerDonnees managerDonnees) {
		super();
		this.managerDonnees = managerDonnees;
		this.setLayout(new BorderLayout());
		this.add(options(), BorderLayout.LINE_START);
		this.add(graphe, BorderLayout.CENTER);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	private Box options() {
		JButton appliquer = new JButton("Appliquer");
		Box boxButton = new Box(BoxLayout.X_AXIS);
		boxButton.add(Box.createHorizontalGlue());
		boxButton.add(appliquer);
		Box boxOpt = new Box(BoxLayout.X_AXIS);
		Box boxOpt2 = new Box(BoxLayout.Y_AXIS);
		boxOpt2.add(new JLabel("Analyseur de données"));
		boxOpt2.add(Box.createVerticalStrut(5));
		boxOpt2.add(choixFluide());
		boxOpt2.add(choixCapteur());
		boxOpt2.add(choixPeriode());
		boxOpt2.add(boxButton);
		boxOpt.add(Box.createHorizontalStrut(10));
		boxOpt.add(boxOpt2);
		return boxOpt;
	}
	
	private JPanel choixFluide() {
		JPanel base = new JPanel(new BorderLayout());
		JComboBox<TypeCapteur> fluides = new JComboBox<>(TypeCapteur.values());getClass();
		base.add(new JLabel("Fluide:"), BorderLayout.PAGE_START);
		base.add(fluides, BorderLayout.CENTER);
		return base;
	}
	
	private JPanel choixCapteur() {
		JPanel base = new JPanel(new BorderLayout());
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
		base.add(new JLabel("Capteurs(max 3)"),BorderLayout.PAGE_START);
		base.add(boxC2, BorderLayout.CENTER);
		return base;
	}
	
	private JDatePickerImpl date() {
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		Date today = new Date();
		model.setDate(today.getYear()+1900,today.getMonth(),today.getDate());
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		return new JDatePickerImpl(datePanel, new DateLabelFormatter());
	}
	
	private JPanel choixPeriode() {
		JPanel base = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new GridLayout(2, 1));
		JButton appliquer = new JButton("Appliquer");
		appliquer.setEnabled(false);
		panel.add(date());
		panel.add(date());
		base.add(new Label("Periode:"), BorderLayout.PAGE_START);
		base.add(panel, BorderLayout.LINE_START);

		return base;
	}
	
	// Méthode appelée lors du clic de souris sur le bouton "Appliquer"
		private void mouseClickedAppliquer(List<Capteur> capteurs,JDatePickerImpl dateMin,
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
}
