package ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.mysql.cj.exceptions.FeatureNotAvailableException;
import com.sun.corba.se.impl.ior.GenericTaggedComponent;

import donnees.Capteur;
import donnees.ManagerDonnees;
import donnees.TypeCapteur;

public class Arbre extends JTree {
	private static final long serialVersionUID = 1L;
	private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("/");
	private static DefaultTreeModel modele = new DefaultTreeModel(root);
	
	private ManagerDonnees managerDonnees;
	
	public Arbre(ManagerDonnees managerDonnees) {
		super(modele);
		this.managerDonnees = managerDonnees;
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addTreeSelectionListener(new selectionListener());
		
		for (TypeCapteur fluide : TypeCapteur.values()) {
			List<Capteur> listeCapteurs = managerDonnees.getCapteursBD(fluide.toString());
			remplirModele(listeCapteurs);
		}
	}
	//TODO refaire
	private static void fenetreChangementSeuils(int x, int y) {
		JFrame frame = new JFrame("SEUIL");
		JPanel base = new JPanel(new BorderLayout());
		JPanel boutons = new JPanel(new FlowLayout());
		Box options = new Box(BoxLayout.Y_AXIS);
		JPanel setMin = new JPanel(new FlowLayout());
		JPanel setMax = new JPanel(new FlowLayout());
		JTextField saisieMin = new JTextField();
		
		setMin.add(new JLabel("Seuil min"));
		setMin.add(saisieMin);
		setMax.add(new JLabel("Seuil max"));
		
		JButton accepter = new JButton("Acepter");
		JButton canceler = new JButton("Canceler");
		
		options.add(setMax);
		options.add(setMin);
		boutons.add(accepter);
		boutons.add(canceler);
		
		base.add(boutons, BorderLayout.PAGE_END);
		base.add(options, BorderLayout.PAGE_START);
		frame.add(base);
		
		frame.setMinimumSize(new Dimension(300, 200));
		frame.setLocation(x, y);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
	}
	
	private static class selectionListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent se) {
			JTree tree = (JTree) se.getSource();
		    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		    String selectedNodeName = selectedNode.toString();
		    if (selectedNode.isLeaf()) {
		    	fenetreChangementSeuils(20, 20); //TODO  trouver le bon placement
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
