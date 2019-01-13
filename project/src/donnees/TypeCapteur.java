/*
 * Projet S5 - NeoCampus
 * OLLIVIER Denis, PICOLO-ORTIZ Bruno, POUJOL Elyan
 */

package donnees;

public enum TypeCapteur {
	EAU("m^3", 0, 10),
	ELECTRICITE("kWh", 10, 500),
	TEMPERATURE("°C", 17, 22),
	AIRCOMPRIME("m^3/h", 0, 5);

	private String unitee;
	private double seuilMin;
	private double seuilMax;
	
	/**
	 * 
	 * @param unitee
	 * @param seuilMin
	 * @param seuilMax
	 */
	private TypeCapteur(String unitee, double seuilMin, double seuilMax) {
		this.unitee = unitee;
		this.seuilMin = seuilMin;
		this.seuilMax = seuilMax;
	}

	public String getUnitee() {
		return unitee;
	}

	public double getSeuilMin() {
		return seuilMin;
	}

	public double getSeuilMax() {
		return seuilMax;
	}
}
