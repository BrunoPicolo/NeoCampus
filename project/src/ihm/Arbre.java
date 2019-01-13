/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package ihm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
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
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("/");
	private static DefaultTreeModel modele = new DefaultTreeModel(root);
	
	private ManagerDonnees managerDonnees;
	
	/**
	 * 
	 * @param managerDonnees
	 * @param split
	 */
	public Arbre(ManagerDonnees managerDonnees, JSplitPane split) {
		super(modele);
		this.managerDonnees = managerDonnees;
		split.setOneTouchExpandable(true);
		split.setDividerLocation(250);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addTreeSelectionListener(new SelectionListener(managerDonnees,split));
		
		for (TypeCapteur fluide : TypeCapteur.values()) {
			List<Capteur> listeCapteurs = managerDonnees.getCapteursBD(fluide.toString());
			remplirModele(listeCapteurs);
		}
	}
	
	private static class SelectionListener implements TreeSelectionListener {
		private JSplitPane split;
		private ManagerDonnees managerDonnees;
		private HashMap<String, Capteur> capteurMap = new HashMap<>();
		
		public SelectionListener(ManagerDonnees managerDonnees,JSplitPane split) {
			this.managerDonnees = managerDonnees;
			this.split = split;
			creationListeCapteurs();	
		}
		
		/**
		 * Description: Création ou actualisation du Map capteurMap avec tous les capteurs de managerDonnees
		 */
		private void creationListeCapteurs() {
			for (TypeCapteur fluide : TypeCapteur.values()) {
				List<Capteur> listeCapteurs = managerDonnees.getCapteursBD(fluide.toString());
				for (Capteur c : listeCapteurs) 
					capteurMap.put(c.getNom(), c);
			}
		}
		
		/**
		 * Description: Créé un JPanel pour afficher des informations sur un capteur donné
		 * @param managerDonnees
		 * @param capteur
		 * @return 
		 */
		private JPanel infoEtChangementSeuils(ManagerDonnees managerDonnees, Capteur capteur) {
			Double seuilMin = Double.valueOf(capteur.getSeuilMin());
			Double seuilMax = Double.valueOf(capteur.getSeuilMax());
			JTextField min = new JTextField(seuilMin.toString());
			JTextField max = new JTextField(seuilMax.toString());
			JPanel gridPanel = new JPanel(new GridLayout(2, 2));
			gridPanel.add(new JLabel("Seuil Min: "));
			Box box2 = new Box(BoxLayout.Y_AXIS);
			box2.add(Box.createVerticalStrut(20));
			box2.add(min);
			box2.add(Box.createVerticalStrut(5));
			gridPanel.add(box2);
			gridPanel.add(new JLabel("Seuil Max: "));
			Box box3 = new Box(BoxLayout.Y_AXIS);
			box3.add(Box.createVerticalStrut(5));
			box3.add(max);
			box3.add(Box.createVerticalStrut(20));
			gridPanel.add(box3);
			Box box = new Box(BoxLayout.Y_AXIS);
			box.add(Box.createVerticalStrut(5));
			box.add(new JLabel("Bâtiment:	" + capteur.getBatiment()));
			box.add(Box.createVerticalStrut(5));
			box.add(new JLabel("Etage:	 	" + capteur.getEtage()));
			box.add(Box.createVerticalStrut(5));
			box.add(new JLabel("Nom:	 	" + capteur.getNom()));
			JButton changerSeuils = new JButton("Appliquer");
			Box boxButton = new Box(BoxLayout.X_AXIS);
			boxButton.add(Box.createHorizontalGlue());
			boxButton.add(changerSeuils);
			boxButton.add(Box.createHorizontalGlue());
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(box, BorderLayout.PAGE_START);
			panel.add(gridPanel,  BorderLayout.CENTER);
			panel.add(boxButton, BorderLayout.PAGE_END);
			changerSeuils.addActionListener(event -> {
				managerDonnees.modifierSeuilCapteur(capteur.getNom(),Double.parseDouble(min.getText()),Double.parseDouble(max.getText()));
				creationListeCapteurs();
			});
			return panel;
		}
		/**
		 * Description: Ajoute un JPanel avec des informations sur un capteur dans un JSplitPane split 
		 */
		@Override
		public void valueChanged(TreeSelectionEvent se) {
			JTree tree = (JTree) se.getSource();
		    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		    String selectedNodeName = selectedNode.toString();
		    if (selectedNode.isLeaf()) {
		    		split.setOneTouchExpandable(true);
		    		split.setDividerLocation(250);
		    		split.setRightComponent(infoEtChangementSeuils(managerDonnees,capteurMap.get(selectedNodeName)));
		    		
		    }	
		}
	}
	
	/**
	 * Description: construction d'un JTree à partir d'une String contenant le chemin vers une feuille
	 * @param str
	 */
	private void construireArbreAvecString(String str) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) modele.getRoot();
			String[] strings = str.split("/");
			DefaultMutableTreeNode node = root;
			int size = strings.length;
			for (int i=0; i < size;i++) {
				int index = indiceFils(node,strings[i]);
				if (index < 0) {
					DefaultMutableTreeNode nouveauFils = new DefaultMutableTreeNode(strings[i]);
					node.insert(nouveauFils, node.getChildCount());
					node = nouveauFils;
				}else {
					node = (DefaultMutableTreeNode) node.getChildAt(index);
				}
				
			}
	}
	
	/**
	 * 
	 * @param node
	 * @param valeurFils
	 * @return
	 */
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

	/**
	 * 
	 * @param listeCapteurs
	 */
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

	public void actualiserArbre() {
		for (TypeCapteur fluide : TypeCapteur.values()) {
			List<Capteur> listeCapteurs = managerDonnees.getCapteursBD(fluide.toString());
			remplirModele(listeCapteurs);
		}
	}

}
