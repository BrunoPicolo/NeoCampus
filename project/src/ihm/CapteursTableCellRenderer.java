package ihm;

import java.awt.Color;
import java.awt.Component;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import donnees.Capteur;

public class CapteursTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	private Set<Capteur> listeCapteurs;
	
	public CapteursTableCellRenderer(Set<Capteur> listeCapteurs) {
		super();
		this.listeCapteurs = listeCapteurs;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object valeurObj,
			boolean selectionne, boolean focus, int ligne, int colonne) {
		Color couleurFond = Color.WHITE;
		Component composant = super.getTableCellRendererComponent(table, valeurObj, selectionne, focus, ligne, colonne);
		Capteur capteur = null;
		int i = 0;
		for (Capteur _capteur : listeCapteurs) {
			if (i == ligne) {
				capteur = _capteur;
				break;
			}
			i++;
		}
		if (capteur.getValeur() < capteur.getSeuilMin()
				|| capteur.getValeur() > capteur.getSeuilMax()) {
			couleurFond = Color.RED;
		}
		composant.setBackground(couleurFond);
		return composant;
	}
}
