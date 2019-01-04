/**
 * 
 */
package ihm;

import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import donnees.Capteur;
import donnees.Mesure;
import donnees.TypeCapteur;

/**
 * @author bruno
 *
 */
public class Graphe{
	
	private JFreeChart graph ;
	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	private TypeCapteur typeC ;
	
	public Graphe(){
		graph = ChartFactory.createLineChart("Analyse donn�es","Dates de relev�s","Capteur non d�fini", dataset);
	}
	
	public Graphe(Map<Capteur,List<Mesure>> donnees){
		for (Capteur c : donnees.keySet()){
			typeC = c.getType();
			for (Mesure m : donnees.get(c)){
				dataset.addValue(m.getValeur(), c.getNom(), m.getDate());
			}
			graph = ChartFactory.createLineChart("Analyse donn�es","Dates de relev�s",typeC.getUnitee(), dataset);
		}
		
	}
	
	public JFreeChart afficher (){
		return graph;
	}

	public void nettoyer() {
		//TODO ...
	}
	
}
