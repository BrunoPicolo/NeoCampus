/**
 * 
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
 * @author bruno
 *
 */
public class ManagerDonnees {
	private Connection connexionBD;
	private Set<Capteur> capteursConnectes;
	private CapteursTableModel capteursTableModel;
	private Consumer<Capteur> ajoutCapteurListener = null;
	
	
	/**
	 * @param adresse
	 * @param identifiant
	 * @param motDePasse
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
					+ "/NeoCampus?serverTimezone=" + TimeZone.getDefault().getID(),
					identifiant, motDePasse);
		} catch (SQLException e) {
			throw new Error("Probleme connexion BD: " + e.getMessage());
		}
	}
	
	public Set<Capteur> getCapteursConnectes() {
		return capteursConnectes;
	}
	
	/**
	 * 
	 * @param nomCapteur
	 * @param valeur
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
	 * 
	 * @param nom
	 * @param batiment
	 * @param lieu
	 * @param etage
	 * @param type
	 */
	public synchronized void connecterCapteur(String nom, String batiment, String lieu, int etage, TypeCapteur type) {
		String requete = 
				"INSERT IGNORE INTO Capteurs (NomCapteur, NomFluide, Batiment, Etage, LieuDetails, SeuilMin, SeuilMax) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		Capteur capteur = new Capteur(nom, batiment, lieu, etage, type);
		capteursConnectes.add(capteur);
		capteur.setModele(capteursTableModel);
		capteursTableModel.fireTableDataChanged();
		if (ajoutCapteurListener != null) {
			ajoutCapteurListener.accept(capteur);
		}
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
	
	// Creation d'une classe Mesure pour le couple (Valeur,Date)
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
	
	public synchronized void deconnecterCapteur(String nomCapteur) {
		for(Iterator<Capteur> capteurIter = capteursConnectes.iterator(); capteurIter.hasNext();) {
			Capteur capteur = capteurIter.next();
			if (capteur.getNom().equals(nomCapteur)) {
				capteurIter.remove();
				capteursTableModel.fireTableDataChanged();
			}
		}
	}
	
	public void setAjoutCapteurListener(Consumer<Capteur> listener) {
		ajoutCapteurListener = listener;
	}
	
	public void modifierSeuilCapteur (String nomCapteur, Double seuilMin, Double seuilMax){
		String requete = "UPDATE Capteurs "
				+ "SET SeuilMin = ?"
				+ "SET SeuilMax = ?"
				+ "WHERE NomCapteur = ?" ;
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
