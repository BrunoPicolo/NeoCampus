/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package ihm;

import javax.swing.table.AbstractTableModel;
import donnees.Capteur;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class CapteursTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final int COL_NUM = 6;

	private OrdreAffichage ordreAffichage;
	
	private Set<Capteur> listeCapteurs;
	
	public CapteursTableModel(Set<Capteur> listeCapteurs) {
		super();
		this.listeCapteurs = listeCapteurs;
		ordreAffichage = OrdreAffichage.AUCUN;
	}
	
	private final Comparator<Capteur> COMPARATEUR_FLUIDE = new Comparator<Capteur>() {
		@Override
		public int compare(Capteur c1, Capteur c2) {
			int comp = c1.getType().compareTo(c2.getType());
			if (comp != 0) return comp;
			return c1.compareTo(c2);
		}
	};
	
	private final Comparator<Capteur> COMPARATEUR_BATIMENT = new Comparator<Capteur>() {
		@Override
		public int compare(Capteur c1, Capteur c2) {
			int comp = c1.getBatiment().compareTo(c2.getBatiment());
			if (comp != 0) return comp;
			return c1.compareTo(c2);
		}
	};
	
	public void setOrdreAffichage(OrdreAffichage ordre) {
		ordreAffichage = ordre;
	}
	
	public OrdreAffichage getOrdreAffichage() {
		return ordreAffichage;
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
				nom = "Mesure";
		}
		
		return nom;
	}

	@Override
	public int getRowCount() {
		return listeCapteurs.size();
	}

	@Override
	public Object getValueAt(int ligne, int colonne) {
		Capteur capteur = null;
		Set<Capteur> listeCapteursOrdonne;
		switch (ordreAffichage) {
			case FLUIDE:
				listeCapteursOrdonne = new TreeSet<>(COMPARATEUR_FLUIDE);
				break;
			case BATIMENT:
				listeCapteursOrdonne = new TreeSet<>(COMPARATEUR_BATIMENT);
				break;
			default:
				listeCapteursOrdonne = new TreeSet<>();
		}
		listeCapteursOrdonne.addAll(listeCapteurs);
		
		int i = 0;
		for (Capteur c : listeCapteursOrdonne) {
			if (i == ligne) {
				capteur = c;
				break;
			}
			i++;
		}
		
		if (capteur != null) {
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
		}
		
		return null;
	}
}
