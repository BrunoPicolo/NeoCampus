package ihm;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import donnees.Capteur;

public class Arbre extends JTree {
	private TreeSet<Capteur> capteurs = new TreeSet<>(new ComparateurCapteurs()); //ne sert à rien pour l'instant
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Campus");
	
	public Arbre() {
		super(root);
		this.setRootVisible(true);
	}
	//Ne sert à rien
	private static class ComparateurCapteurs implements Comparator<Capteur> {
		@Override
		public int compare(Capteur capteur1, Capteur capteur2) {
			String batiment1 = capteur1.getBatiment();
			String batiment2 = capteur2.getBatiment();
			int comparateur = batiment1.compareTo(batiment2);
			if (comparateur == 0) {
				int etage1 = capteur1.getEtage();
				int etage2 = capteur2.getEtage();
				comparateur = etage1 - etage2;
				if (comparateur == 0) {
					String nomCapteur1 = capteur1.getNom();
					String nomCapteur2 = capteur2.getNom();
					return nomCapteur1.compareTo(nomCapteur2);
				}
				
			}
			return comparateur;
		}
		
	}
	public void createNodes(LinkedHashSet<Capteur> listeCapteurs) {
		ArrayList<String> strPath = new ArrayList<>();
		for (Capteur capteur : listeCapteurs) {
			strPath.add(capteur.getBatiment());
			strPath.add("Etage " + capteur.getEtage());
			strPath.add(capteur.getNom());
			TreePath path = new TreePath(strPath);
			root.add((MutableTreeNode) ((DefaultMutableTreeNode)path.getLastPathComponent()).
				       getUserObject());
		}
		
	}
}
