/**
 * 
 */
package donnees;

/**
 * @author bruno
 *
 */
public enum TypeCapteur {
	EAU("m^3", 0, 10),
	ELECTRICITE("kWh", 10, 500),
	TEMPERATURE("°C", 17, 22),
	AIR_COMPRIME("m^3/h", 0, 5);

	private String unitee;
	private double seuilMin;
	private double seuilMax;
	
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
