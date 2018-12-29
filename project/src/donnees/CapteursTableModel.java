/**
 * 
 */
package donnees;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * @author bruno
 *
 */
public class CapteursTableModel extends AbstractTableModel {
	private static final int COL_NUM = 6;
	
	private ArrayList<Capteur> listeCapteurs;
	
	public CapteursTableModel(ArrayList<Capteur> listeCapteurs) {
		this.listeCapteurs = listeCapteurs;
	}
	
	@Override
	public int getColumnCount() {
		return COL_NUM;
	}
	

	@Override
	public String getColumnName(int colonne) {
		String nom = null;
		
		switch (colonne) {
			case 0:
				nom = "Nom";
				break;
			case 1:
				nom = "Fluide";
				break;
			case 2:
				nom = "Bâtiment";
				break;
			case 3:
				nom = "Etage";
				break;
			case 4:
				nom = "Lieu";
				break;
			case 5:
				nom = "Dernière mesure";
		}
		
		return nom;
	}

	@Override
	public int getRowCount() {
		return listeCapteurs.size();
	}

	@Override
	public Object getValueAt(int ligne, int colonne) {
		Capteur capteur = listeCapteurs.get(ligne);
		
		switch (colonne) {
			case 0:
				return capteur.getNom();
			case 1:
				return capteur.getType().toString();
			case 2:
				return capteur.getBatiment();
			case 3:
				return capteur.getEtage();
			case 4:
				return capteur.getLieu();
			case 5:
				return capteur.getValeur();
		}
		
		return null;
	}
}
