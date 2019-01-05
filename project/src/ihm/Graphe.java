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
		graph = ChartFactory.createXYLineChart("Analyse donn�es","Dates de relev�s","Capteur non d�fini", dataCollection);
	}
	
	public void afficher(Map<Capteur, List<Mesure>> donnees) {
		if (donnees.isEmpty()) {
			nettoyer();
			return;
		}
		
		dataCollection.removeAllSeries();
		TypeCapteur type = TypeCapteur.EAU; // Initialisation par d�faut
		for (Capteur capteur : donnees.keySet()) {
			XYSeries series = new XYSeries(capteur.getNom());
			type = capteur.getType();
			for (Mesure mesure : donnees.get(capteur)) {
				series.add(mesure.getDate().getTime(), mesure.getValeur());
			}
			dataCollection.addSeries(series);
		}
		graph = ChartFactory.createXYLineChart("Analyse donn�es", "Dates de relev�s", type.getUnitee(), dataCollection);
		removeAll();
		revalidate();
		this.add(new ChartPanel(graph));
		this.repaint();
	}

	public void nettoyer() {
		dataCollection.removeAllSeries();
		graph = ChartFactory.createXYLineChart("Analyse donn�es", "Dates de relev�s", "", dataCollection);
		this.removeAll();
		this.add(new ChartPanel(graph));
		this.repaint();
	}
}
