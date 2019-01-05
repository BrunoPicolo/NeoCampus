package ihm;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import donnees.Capteur;

public class CapteursListCellRenderer extends JLabel implements ListCellRenderer<Capteur> {
	@Override
	public Component getListCellRendererComponent(JList<? extends Capteur> liste, Capteur capteur,
			int indice, boolean selectionne, boolean focus) {
		if (selectionne) {
            setForeground(Color.BLUE);
        } else {
            setForeground(Color.BLACK);
        }
		setText(capteur.getNom());
		return this;
	}
}
