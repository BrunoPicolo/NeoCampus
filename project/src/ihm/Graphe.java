/**
 * 
 */
package ihm;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.FixedMillisecond;
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
	private static final long serialVersionUID = 1L;
	
	private JFreeChart graphe;
	private TimeSeriesCollection dataCollection = new TimeSeriesCollection();
	private DateAxis dateAxis =  new DateAxis();
	
	public Graphe() {
		super(new BorderLayout());
		dateAxis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
		nettoyer();
	}
	
	public void afficher(Map<Capteur, List<Mesure>> donnees) {
		if (donnees.isEmpty()) {
			nettoyer();
			return;
		}
		
		dataCollection.removeAllSeries();
		TypeCapteur type = TypeCapteur.EAU; // Initialisation par défaut
		for (Capteur capteur : donnees.keySet()) {
			TimeSeries series = new TimeSeries(capteur.getNom());
			type = capteur.getType();
			for (Mesure mesure : donnees.get(capteur)) {
				series.add(new FixedMillisecond(mesure.getDate()), mesure.getValeur());
			}
			dataCollection.addSeries(series);
		}
		graphe = ChartFactory.createTimeSeriesChart("Analyse données", "Dates de relevés", type.getUnitee(),
				dataCollection, false, true, false);
		XYPlot plot = graphe.getXYPlot();
		plot.setDomainAxis(dateAxis);
		
		removeAll();
		revalidate();
		this.add(new ChartPanel(graphe));
		this.repaint();
	}

	public void nettoyer() {
		dataCollection.removeAllSeries();
		graphe = ChartFactory.createTimeSeriesChart("Analyse données", "Dates de relevés", "", dataCollection);
		this.removeAll();
		this.add(new ChartPanel(graphe));
		this.repaint();
	}
}
