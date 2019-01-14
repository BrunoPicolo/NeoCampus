/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
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
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
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
		split.setDividerLocation(300);
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
			
			JPanel base = new JPanel(new BorderLayout());
			JButton changerSeuils = new JButton("Appliquer");
			Box boxButon = new Box(BoxLayout.X_AXIS);
			boxButon.add(Box.createHorizontalGlue());
			boxButon.add(changerSeuils);
			boxButon.add(Box.createHorizontalGlue());
			Double seuilMin = Double.valueOf(capteur.getSeuilMin());
			Double seuilMax = Double.valueOf(capteur.getSeuilMax());
			JTextField min = new JTextField(seuilMin.toString());
			min.setMaximumSize(new Dimension(150, 30));
			JTextField max = new JTextField(seuilMax.toString());
			max.setMaximumSize(new Dimension(150, 30));
			changerSeuils.addActionListener(event -> {
				managerDonnees.modifierSeuilCapteur(capteur.getNom(),Double.parseDouble(min.getText()),
						Double.parseDouble(max.getText()));
				creationListeCapteurs();
			});
			base.add(boxButon, BorderLayout.PAGE_END);
			JPanel panel = new JPanel(new BorderLayout());
			Box box1 = new Box(BoxLayout.Y_AXIS);
			box1.add(new JLabel(" Bâtiment:	" + capteur.getBatiment()));
			box1.add(Box.createVerticalStrut(10));
			box1.add(new JLabel(" Etage:	 	" + capteur.getEtage()));
			box1.add(Box.createVerticalStrut(10));
			box1.add(new JLabel(" Nom:	 	" + capteur.getNom()));
			box1.add(Box.createVerticalStrut(20));
			panel.add(box1,BorderLayout.CENTER);
			panel.setMaximumSize(new Dimension(300, 150));
			Box box2 = new Box(BoxLayout.Y_AXIS);
			box2.add(Box.createVerticalStrut(30));
			box2.add(panel);
			box1.add(Box.createVerticalGlue());
			Box box3 = new Box(BoxLayout.X_AXIS);
			box3.add(new JLabel("Seuil Min: "));
			box3.add(Box.createHorizontalStrut(10));
			box3.add(min);
			box2.add(box3);
			Box box4 = new Box(BoxLayout.X_AXIS);
			box4.add(new JLabel("Seuil Max: "));
			box4.add(Box.createHorizontalStrut(10));
			box4.add(max);
			box2.add(box4);
			box2.add(Box.createVerticalGlue());
			base.add(box2, BorderLayout.CENTER);
			return base;
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
		    		split.setDividerLocation(260);
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
