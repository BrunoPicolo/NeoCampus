/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package ihm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;

import sun.swing.table.DefaultTableCellHeaderRenderer;

public class CapteursTableCellHeaderRenderer extends DefaultTableCellHeaderRenderer {
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable tableau, Object value,
    		boolean selectionne, boolean focus, int ligne, int colonne) {
        super.getTableCellRendererComponent(tableau, value, selectionne, focus, ligne, colonne);
        String nom = tableau.getColumnName(colonne);
        
        if (nom.equals("Fluide") || nom.equals("Bâtiment")) {
        	setBackground(Color.LIGHT_GRAY);
        	
        	OrdreAffichage ordre = ((CapteursTableModel)tableau.getModel()).getOrdreAffichage();
        	Font f = getFont();
        	if ((nom.equals("Fluide") && ordre == OrdreAffichage.FLUIDE)
        			|| (nom.equals("Bâtiment") && ordre == OrdreAffichage.BATIMENT)) {
        		setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        	} else {
        		setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
        	}
        }

        return this;
    }
}
