package ihm;

import javax.swing.DefaultListSelectionModel;

public class CapteursListSelectionModel extends DefaultListSelectionModel {
	private int nbSelectionnes() {
		int indiceMin = getMinSelectionIndex();
		int indiceMax = getMaxSelectionIndex();
		int nbCapteursSelectionnes;
		int i;
		for (i = indiceMin, nbCapteursSelectionnes = 0; i <= indiceMax; i++) {
			if (isSelectedIndex(i)) {
				nbCapteursSelectionnes++;
			}
		}
		return nbCapteursSelectionnes;
	}
	
	public CapteursListSelectionModel() {
		setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
	}
	
	@Override
	public void addSelectionInterval(int index0, int index1) {
		// System.out.println("addSelectionInterval: " + index0 + ", " + index1);
		if (nbSelectionnes() < 3) {
			super.addSelectionInterval(index0, index1);
		}
	}

	@Override
	public void setSelectionInterval(int index0, int index1) {
		// System.out.println("setSelectionInterval: " + index0 + ", " + index1);
		if (nbSelectionnes() < 3) {
			super.setSelectionInterval(index0, index1);
		}
	}
}
