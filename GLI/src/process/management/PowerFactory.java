package process.management;

import data.Power;
import data.building.special.Capital;

public class PowerFactory {

	public static Power createPower(String name, int aiLevel) {
		return new Power(name, aiLevel, new Capital());
	}

	public static Power[] createPowers(String[] names, int[] aiLevel) {
		Power[] powers = new Power[names.length];
		for (int i = 0; i < names.length; i++) {
			powers[i] = createPower(names[i], aiLevel[i]);
		}
		return powers;
	}

	public static Power[] createDefaultPowers(int numberOfPower) {
		Power[] powers = new Power[numberOfPower];
		for (int i = 0; i < numberOfPower; i++) {
			powers[i] = createPower("Joueur " + i, 1);
		}
		return powers;
	}

}
