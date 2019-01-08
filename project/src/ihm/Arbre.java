package ihm;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import donnees.Capteur;
import donnees.ManagerDonnees;
import donnees.TypeCapteur;

public class Arbre extends JTree {
	private static final long serialVersionUID = 1L;
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Campus");
	private static DefaultTreeModel modele = new DefaultTreeModel(root);
	
	
	public Arbre(ManagerDonnees managerDonnees) {
		super(modele);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addTreeSelectionListener(new selectionListener());
		
		for (TypeCapteur fluide : TypeCapteur.values()) {
			List<Capteur> listeCapteurs = managerDonnees.getCapteursBD(fluide.toString());
			remplirModele(listeCapteurs);
		}
	}
	
	private static class selectionListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent se) {
			JTree tree = (JTree) se.getSource();
		    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		    String selectedNodeName = selectedNode.toString();
		    if (selectedNode.isLeaf()) {
		    	//TODO action à ajouter
		    	System.out.println(selectedNodeName);

		    }	
		}
		
	}
	private void construireArbreAvecString(String str) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.modele.getRoot();
			String[] strings = str.split("/");
			DefaultMutableTreeNode node = root;
			int size = strings.length;
			for (int i=0; i < size;i++) {
				int index = indiceFils(node,strings[i]);
				if (index < 0) {
					DefaultMutableTreeNode nouveauFils = new DefaultMutableTreeNode(strings[i]);
					if (i == size-1) {
						///TODO ...
					}
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

	public void remplirModele(List<Capteur> listeCapteurs) {
		StringBuilder path = new StringBuilder();
		for (Capteur capteur : listeCapteurs) {
			path.append(capteur.getBatiment());
			path.append("/" + "Etage " + capteur.getEtage());
			path.append("/" + capteur.getNom());
			construireArbreAvecString(path.toString());
			path.delete(0, path.length());
		}
	}

	public void actualiserArbre(ManagerDonnees managerDonnees) {
		
		for (TypeCapteur fluide : TypeCapteur.values()) {
			List<Capteur> listeCapteurs = managerDonnees.getCapteursBD(fluide.toString());
			remplirModele(listeCapteurs);
		}
	}
}
