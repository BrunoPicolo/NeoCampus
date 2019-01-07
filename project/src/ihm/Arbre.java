package ihm;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import donnees.Capteur;

public class Arbre extends JTree {
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Campus");
	private static DefaultTreeModel model = new DefaultTreeModel(root);
	
	public Arbre() {
		super(model);
		construireArbreAvecString(model, "Batiment 1/Etage 2/CapteurToto");
		construireArbreAvecString(model, "Batiment 1/Etage 1/CapteurLala");
		construireArbreAvecString(model, "Batiment 3/Etage 2/CapteurToto");
	}

	private void construireArbreAvecString(DefaultTreeModel modele, String str) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			String[] strings = str.split("/");
			DefaultMutableTreeNode node = root;
			for (String s : strings) {
				int index = indiceFils(node,s);
				if (index < 0) {
					DefaultMutableTreeNode nouveauFils = new DefaultMutableTreeNode(s);
					node.insert(nouveauFils, node.getChildCount());
					node = nouveauFils;
				}else {
					node = (DefaultMutableTreeNode) node.getChildAt(index);
				}
				
			}
	}
private int indiceFils(final DefaultMutableTreeNode node,final String valeurFils) {
		Enumeration<DefaultMutableTreeNode> filsNode = node.children();
		DefaultMutableTreeNode fils = null;
		int index = -1;
		
		while (filsNode.hasMoreElements() && index < 0) {
			fils = filsNode.nextElement();
			if (fils.getUserObject() != null && valeurFils.equals(fils.getUserObject())) {
				index = node.getIndex(fils);
			}
		}
		return index;
	}
	//	A modifier pour retourner un tableau de strings contenant tous les paths
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
