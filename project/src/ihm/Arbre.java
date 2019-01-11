package ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.mysql.cj.exceptions.FeatureNotAvailableException;
import com.sun.corba.se.impl.ior.GenericTaggedComponent;
import com.sun.org.glassfish.gmbal.ManagedAttribute;

import donnees.Capteur;
import donnees.ManagerDonnees;
import donnees.TypeCapteur;

public class Arbre extends JTree {
	private static final long serialVersionUID = 1L;
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("/");
	private static DefaultTreeModel modele = new DefaultTreeModel(root);
	
	private ManagerDonnees managerDonnees;
	
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
			for (TypeCapteur fluide : TypeCapteur.values()) {
				List<Capteur> listeCapteurs = managerDonnees.getCapteursBD(fluide.toString());
				for (Capteur c : listeCapteurs) 
					capteurMap.put(c.getNom(), c);
			}
			
		}
		private JPanel infoEtChangementSeuils(ManagerDonnees managerDonnees, Capteur capteur) {
			JPanel panel = new JPanel(new BorderLayout());
			Box box = new Box(BoxLayout.Y_AXIS);
			JButton changerSeuils = new JButton("Appliquer");
			JTextField min = new JTextField(new Double(capteur.getSeuilMin()).toString());
			JTextField max = new JTextField(new Double(capteur.getSeuilMax()).toString());

			box.add(new JLabel("Bâtiment: " + capteur.getBatiment()));
			box.add(new JLabel("Etage: " + capteur.getEtage()));
			box.add(new JLabel("Nom: " + capteur.getNom()));
			box.add(min);
			box.add(max);
			min.addActionListener(null);
			panel.add(box, BorderLayout.CENTER);
			panel.add(changerSeuils, BorderLayout.PAGE_END);
			changerSeuils.addActionListener(event -> {
				managerDonnees.modifierSeuilCapteur(capteur.getNom(),Double.parseDouble(min.getText()),Double.parseDouble(max.getText()));
			});
			return panel;
		}
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
	

	private void construireArbreAvecString(String str) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.modele.getRoot();
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

	public void actualiserArbre() {
		for (TypeCapteur fluide : TypeCapteur.values()) {
			List<Capteur> listeCapteurs = managerDonnees.getCapteursBD(fluide.toString());
			remplirModele(listeCapteurs);
		}
	}

}
