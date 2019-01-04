package donnees;

import java.util.Date;

public class Mesure {
	private Double valeur ;
	private Date date ;

	public Mesure(Double valeur,Date date) {
		this.valeur=valeur;
		this.date=date;
	}

	public Double getValeur() {
		return valeur;
	}

	public Date getDate() {
		return date;
	}

	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
