/**
 * 
 */
package ihm;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import donnees.Capteur;
import donnees.Mesure;
import donnees.TypeCapteur;

/**
 * @author bruno
 *
 */
public class Graphe extends JPanel {
	private JFreeChart graph;
	private XYSeriesCollection dataCollection = new XYSeriesCollection();
	
	public Graphe() {
		super(new BorderLayout());
		graph = ChartFactory.createXYLineChart("Analyse données","Dates de relevés","Capteur non défini", dataCollection);
	}
	
	public void afficher(Map<Capteur, List<Mesure>> donnees) {
		if (donnees.isEmpty()) {
			nettoyer();
			return;
		}
		
		dataCollection.removeAllSeries();
		TypeCapteur type = TypeCapteur.EAU; // Initialisation par défaut
		for (Capteur capteur : donnees.keySet()) {
			XYSeries series = new XYSeries(capteur.getNom());
			type = capteur.getType();
			for (Mesure mesure : donnees.get(capteur)) {
				series.add(mesure.getDate().getTime(), mesure.getValeur());
			}
			dataCollection.addSeries(series);
		}
		graph = ChartFactory.createXYLineChart("Analyse données", "Dates de relevés", type.getUnitee(), dataCollection);
		removeAll();
		revalidate();
		this.add(new ChartPanel(graph));
		this.repaint();
	}

	public void nettoyer() {
		dataCollection.removeAllSeries();
		graph = ChartFactory.createXYLineChart("Analyse données", "Dates de relevés", "", dataCollection);
		this.removeAll();
		this.add(new ChartPanel(graph));
		this.repaint();
	}
}
