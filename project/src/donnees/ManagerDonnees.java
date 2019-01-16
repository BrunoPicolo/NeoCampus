/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package donnees;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

import ihm.CapteursTableModel;

import java.sql.*;

/**
 * Contient les capteurs connectés à l'application et permet d'accéder à la
 * base de données.
 */
public class ManagerDonnees {
	private Connection connexionBD;
	/** Liste des capteurs connectés */
	private Set<Capteur> capteursConnectes;
	/** Modèle personnalisé de JTable */
	private CapteursTableModel capteursTableModel;
	/** Listener appellé à chaque ajout de capteur */
	// private Consumer<Capteur> ajoutCapteurListener = null; // non utilisé
	
	/**
	 * Crée le manager de données de l'application
	 * @param adresse Adresse de la base de données
	 * @param identifiant Nom d'utilisateur pour se connecter à la base de données
	 * @param motDePasse Mot de passe pour se connecter à a base de données
	 */
	public ManagerDonnees(String adresse, String identifiant, String motDePasse,
			Set<Capteur> capteursConnectes, CapteursTableModel capteursTableModel) { 
		super();
		this.capteursConnectes = capteursConnectes;
		this.capteursTableModel = capteursTableModel;
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new Error("Probleme chargement driver: " + e.getMessage());
        }
		try {
			this.connexionBD = DriverManager.getConnection("jdbc:mysql://" + adresse 
					+ "/OLLIVIER_PICOLO_POUJOL_PROJETS5?serverTimezone=" + TimeZone.getDefault().getID(),
					identifiant, motDePasse);
		} catch (SQLException e) {
			throw new Error("Probleme connexion BD: " + e.getMessage());
		}
	}
	
	public Set<Capteur> getCapteursConnectes() {
		return capteursConnectes;
	}
	
	/**
	 * Actualise la valeur du capteur nommé par nomCapteur
	 * @param nomCapteur Nom du capteur dont il faut actualiser la valeur actuelle
	 * @param valeur Nouvelle valeur du capteur
	 */
	public synchronized void actualiserValeurCapteur(String nomCapteur, double valeur) {
		String requete = "INSERT INTO Releves (Id, NomCapteur, Valeur, DateReleve) VALUES (NULL, ?, ?, ?)";
		
		for(Capteur capteur : capteursConnectes) {
			if (capteur.getNom().equals(nomCapteur)) {
				capteur.actualiserValeur(valeur);
				try {
					PreparedStatement s = connexionBD.prepareStatement(requete);
					s.setString(1, nomCapteur);
					s.setDouble(2, valeur);
					Calendar cal = Calendar.getInstance();
					s.setTimestamp(3, new Timestamp(cal.getTimeInMillis()));
					s.executeUpdate();
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Crée et connecte un nouveau capteur à l'application
	 * Stocke le nouveau capteur en base de données si il ne s'y est jamais connecté
	 * Ajoute le capteur dans le tableau des capteurs de l'analyse en temps réel
	 * @param nom Nom du capteur
	 * @param batiment Batîment dans lequel se trouve le capteur
	 * @param lieu Lieu précis où se trouve le capteur
	 * @param etage Etage du batîment dans lequel se situe le capteur
	 * @param type Type de fluide du capteur
	 */
	public synchronized void connecterCapteur(String nom, String batiment, String lieu, int etage, TypeCapteur type) {
		String requete = 
				"INSERT IGNORE INTO Capteurs (NomCapteur, NomFluide, Batiment, Etage, LieuDetails, SeuilMin, SeuilMax) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		Capteur capteur = new Capteur(nom, batiment, lieu, etage, type);
		capteursConnectes.add(capteur);
		capteur.setModele(capteursTableModel);
		capteursTableModel.fireTableDataChanged();
		/* if (ajoutCapteurListener != null) {
			ajoutCapteurListener.accept(capteur);
		} */
		try {
			PreparedStatement s = connexionBD.prepareStatement(requete);
			s.setString(1, nom);
			s.setString(2, type.name());
			s.setString(3, batiment);
			s.setInt(4, etage);
			s.setString(5, lieu);
			s.setDouble(6, type.getSeuilMin());
			s.setDouble(7, type.getSeuilMax());
			s.executeUpdate();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extrait les mesures relevées par un capteur sur une période donnée
	 * @param capteur Capteur dont on veut extraire les données
	 * @param dateMin Borne inférieure de la période
	 * @param dateMax Borne supérieure de la période
	 * @return Liste des mesures relevées par le capteur sur la période
	 */
	public List<Mesure> mesuresPeriode(Capteur capteur, Date dateMin, Date dateMax) {
		String requete = 
				"SELECT NomCapteur, Valeur, DateReleve FROM Releves "
				+ "WHERE DateReleve >= ? AND DateReleve <= ? "
				+ "AND NomCapteur = ?";
		
		List<Mesure> mesures = new ArrayList<>();
		try {
			PreparedStatement s = connexionBD.prepareStatement(requete);
			s.setTimestamp(1, new Timestamp(dateMin.getTime()));
			s.setTimestamp(2, new Timestamp(dateMax.getTime()));
			s.setString(3, capteur.getNom());
			ResultSet res = s.executeQuery();
			while (res.next()) {
				mesures.add(new Mesure(res.getDouble("Valeur"), res.getTimestamp("DateReleve")));
			}
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mesures;
	}
	
	/**
	 * 
	 * Retrouve les capteur d'un certain type stockés en base de données
	 * @param nomFluide Nom du fluide associé aux capteurs voulus
	 * @return Liste des capteur du type de fluide voulu
	 */
	public List<Capteur> getCapteursBD(String nomFluide) {
		String requete = "SELECT * FROM Capteurs WHERE NomFluide = ?";
		List<Capteur> capteurs = new ArrayList<>();
		
		try {
			PreparedStatement s = connexionBD.prepareStatement(requete);
			s.setString(1, nomFluide);
			ResultSet res = s.executeQuery();
			while (res.next()) {
				Capteur capteur = new Capteur(
					res.getString("NomCapteur"),
					res.getString("Batiment"),
					res.getString("LieuDetails"),
					res.getInt("Etage"),
					TypeCapteur.valueOf(res.getString("NomFluide"))
				);
				capteur.setSeuilMin(res.getDouble("SeuilMin"));
				capteur.setSeuilMax(res.getDouble("SeuilMax"));
				capteurs.add(capteur);
			}
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return capteurs;
	}
	
	/**
	 * 
	 * Supprime le capteur de la liste des capteurs connectés
	 * Supprime le capteur du tableau de l'analyse en temps réel
	 * @param nomCapteur Nom du capteur à déconnecter
	 */
	public synchronized void deconnecterCapteur(String nomCapteur) {
		for(Iterator<Capteur> capteurIter = capteursConnectes.iterator(); capteurIter.hasNext();) {
			Capteur capteur = capteurIter.next();
			if (capteur.getNom().equals(nomCapteur)) {
				capteurIter.remove();
				capteursTableModel.fireTableDataChanged();
			}
		}
	}
	
	/**
	 * Non utilisé
	 * Fournit le callback appellé lors de la connexion d'un capteur
	 * @param listener Fonction appellée à la connexion du capteur
	 */
	/* public void setAjoutCapteurListener(Consumer<Capteur> listener) {
		ajoutCapteurListener = listener;
	} */

	/**
	 * 
	 * @param nomCapteur
	 * @param seuilMin
	 * @param seuilMax
	 */
	public void modifierSeuilCapteur(String nomCapteur, Double seuilMin, Double seuilMax){
		String requete = "UPDATE Capteurs "
				+ "SET SeuilMin = ?, "
				+ "SeuilMax = ? "
				+ "WHERE NomCapteur = ?"; 
		try {
			PreparedStatement s = connexionBD.prepareStatement(requete);
			s.setDouble(1, seuilMin);
			s.setDouble(2, seuilMax);
			s.setString(3, nomCapteur);
			s.executeUpdate();
			for ( Capteur c : capteursConnectes){
				if (c.getNom().equals(nomCapteur)){
					c.setSeuilMin(seuilMin);
					c.setSeuilMax(seuilMax);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
