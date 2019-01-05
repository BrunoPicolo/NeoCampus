/**
 * 
 */
package ihm;

import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import donnees.Capteur;
import donnees.Mesure;
import donnees.TypeCapteur;

/**
 * @author bruno
 *
 */
public class Graphe extends JPanel {
	private JFreeChart graph;
	private TimeSeriesCollection dataCollection = null;
	private TypeCapteur typeC;
	
	public Graphe() {
		super();
		graph = ChartFactory.createXYLineChart("Analyse données","Dates de relevés","Capteur non défini", dataCollection);
	}
	
	public void afficher(Map<Capteur,List<Mesure>> donnees) {
		dataCollection.removeAllSeries();
		for (Capteur c : donnees.keySet()) {
			TimeSeries series = new TimeSeries(c.getNom());
			typeC = c.getType();
			for (Mesure m : donnees.get(c)) {
				series.add(new Day(m.getDate()), m.getValeur());
			}
			dataCollection.addSeries(series);
		}
		graph = ChartFactory.createXYLineChart("Analyse données", "Dates de relevés", typeC.getUnitee(), dataCollection);
		this.removeAll();
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
