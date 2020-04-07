package process.management;

import data.building.special.Capital;

import java.util.ArrayList;
import java.util.Iterator;

import data.GameConstants;
import data.Power;
import data.boxes.Box;

import log.LoggerUtility;
import org.apache.log4j.Logger;

public class PowerManager {
	private static PowerManager instance = new PowerManager();
	public static PowerManager getInstance() { return instance; }
	private static Logger Logger = LoggerUtility.getLogger(PowerManager.class, GameConstants.LOG_TYPE);
	
	
	public static Power createPower(String name) {
		return new Power(name, new Capital());
	}
	
	public static Power[] createPowers (String[] names) {
		Power[] powers = new Power[names.length];
		for (int i = 0; i < names.length; i++) {
			powers[i] = createPower(names[i]);
		}
		return powers;
	}
	
	public static Power[] createDefaultPowers (int numberOfPower) {
		Power[] powers = new Power[numberOfPower];
		for (int i = 0; i < numberOfPower; i++) {
			powers[i] = createPower("Joueur "+i);
		}
		return powers;
	}
	
	/**
	 * Create an alliance with those 2 powers, making attack and conquer of territory unavaible
	 * @param power1 the power that want to launch the alliance
	 * @param power2 the power that will become allied
	 */
	public void makeAlliance(Power power1, Power power2) {
		power1.setAlly(power2);
		power2.setAlly(power1);
		Logger.info(power1.getName()+" is now allied with "+power2);
	}
	
	/**
	 * Break the alliance set by those 2 powers, making attack and conquer avaible again
	 * @param power that doesn't want to be allied anymore
	 */
	public void breakAlliance(Power power) {
		Power power2 = power.getAlly();
		power.removeAlly();
		power2.removeAlly();
		Logger.info(power.getName()+" is no longer allied with "+power2);
		
		ArrayList<Box> boxToGain = new ArrayList<Box>();
		
		for (Iterator<Box> i = power.getTerritory().iterator(); i.hasNext(); ) {
			Box visitBox = i.next();
			if (visitBox.hasUnit()) {
				if (visitBox.getUnit().getOwner() == power2) {
					boxToGain.add(visitBox);
				}
			}
		}
		for (Iterator<Box> i = boxToGain.iterator(); i.hasNext(); ) {
			Box visitBox = i.next();
			visitBox.setOwner(power2);
			power2.addBox(visitBox);
			power.removeBox(visitBox);
		}
		Logger.info(power2.getName()+" gain "+boxToGain.size()+"Box from breaking the alliance");
		
		boxToGain.clear();
		
		for (Iterator<Box> i = power2.getTerritory().iterator(); i.hasNext(); ) {
			Box visitBox = i.next();
			if (visitBox.hasUnit()) {
				if (visitBox.getUnit().getOwner() == power) {
					boxToGain.add(visitBox);
				}
			}
		}
		for (Iterator<Box> i = boxToGain.iterator(); i.hasNext(); ) {
			Box visitBox = i.next();
			visitBox.setOwner(power);
			power.addBox(visitBox);
			power2.removeBox(visitBox);
		}
		Logger.info(power.getName()+" gain "+boxToGain.size()+"Box from breaking the alliance");
	}
	
	
}
