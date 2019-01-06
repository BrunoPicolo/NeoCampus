package ihm;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import donnees.Capteur;

public class CapteursListCellRenderer extends JLabel implements ListCellRenderer<Capteur> {
	private static final long serialVersionUID = 1L;

	public CapteursListCellRenderer() {
		super();
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Capteur> liste, Capteur capteur,
			int indice, boolean selectionne, boolean focus) {
		if (selectionne) {
			setBackground(liste.getSelectionBackground());
            setForeground(liste.getSelectionForeground());
        } else {
        	setBackground(liste.getBackground());
            setForeground(liste.getForeground());
        }
		setText(capteur.getNom());
		return this;
	}
}
