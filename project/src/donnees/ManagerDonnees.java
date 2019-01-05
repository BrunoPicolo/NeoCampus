/**
 * 
 */
package donnees;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TimeZone;

import ihm.CapteursTableModel;

import java.sql.*;

/**
 * @author bruno
 *
 */
public class ManagerDonnees {
	private Connection connexionBD;
	private LinkedHashSet<Capteur> capteursConnectes;
	private String adresse;
	private String identifiant;
	private String motDePasse;
	private CapteursTableModel capteursTableModel;
	
	
	/**
	 * @param adresse
	 * @param identifiant
	 * @param motDePasse
	 */
	public ManagerDonnees(String adresse, String identifiant, String motDePasse,
			LinkedHashSet<Capteur> capteursConnectes, CapteursTableModel capteursTableModel) { 
		super();
		this.adresse = adresse;
		this.identifiant = identifiant;
		this.motDePasse = motDePasse;
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
					s.setDate(3, new java.sql.Date(cal.getTimeInMillis()));
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
	public List<Mesure> mesuresPeriode(Capteur capteur, Date dateMin, Date DateMax) {
		List<Mesure> mesures = new ArrayList<>();
		// TEMPORAIRE SIMULATION DE DONNEES SANS BDD
		for (int i = 0 ; i<5 ; i++) {
			Mesure m = new Mesure(i+0.3,new Date()) ;
			mesures.add(m);
		}
		// FIN TEMPORAIRE
		// TODO
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
	
	public LinkedHashSet<Capteur> getCapteursConnectes() {
		return capteursConnectes;
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
}
