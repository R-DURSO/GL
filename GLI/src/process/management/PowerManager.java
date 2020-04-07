package process.management;

import data.building.special.Capital;
import data.resource.ResourceTypes;

import java.util.ArrayList;
import java.util.Iterator;

import data.GameConstants;
import data.Power;
import data.ScoreValue;
import data.boxes.Box;

import log.LoggerUtility;
import org.apache.log4j.Logger;

public class PowerManager {
	private static PowerManager instance = new PowerManager();
	public static PowerManager getInstance() { return instance; }
	private static Logger Logger = LoggerUtility.getLogger(PowerManager.class, GameConstants.LOG_TYPE);
	
	
	public static Power createPower(String name, int aiLevel) {
		return new Power(name, aiLevel, new Capital());
	}
	
	public static Power[] createPowers (String[] names, int[] aiLevel) {
		Power[] powers = new Power[names.length];
		for (int i = 0; i < names.length; i++) {
			powers[i] = createPower(names[i], aiLevel[i]);
		}
		return powers;
	}
	
	public static Power[] createDefaultPowers (int numberOfPower) {
		Power[] powers = new Power[numberOfPower];
		for (int i = 0; i < numberOfPower; i++) {
			powers[i] = createPower("Joueur "+i, 1);
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
	
	/**
	 * As the conquer grow, only the strongest remain
	 * @param killer, the one who will prevail
	 * @param killed, those who have failed
	 */
	public void killPower(Power killer, Power killed) {
		//get all territory of the defeated
		killer.getTerritory().addAll(killed.getTerritory());
		//receive all production
		killer.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, killed.getResourceProductionPerTurn(ResourceTypes.RESOURCE_FOOD));
		killer.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_WOOD, killed.getResourceProductionPerTurn(ResourceTypes.RESOURCE_WOOD));
		killer.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_GOLD, killed.getResourceProductionPerTurn(ResourceTypes.RESOURCE_GOLD));
		killer.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_STONE, killed.getResourceProductionPerTurn(ResourceTypes.RESOURCE_STONE));
		//reveive half of his current ressource
		killer.getResource(ResourceTypes.RESOURCE_FOOD).addValue(killed.getResource(ResourceTypes.RESOURCE_FOOD).getAmount() / 2);
		killer.getResource(ResourceTypes.RESOURCE_WOOD).addValue(killed.getResource(ResourceTypes.RESOURCE_WOOD).getAmount() / 2);
		killer.getResource(ResourceTypes.RESOURCE_GOLD).addValue(killed.getResource(ResourceTypes.RESOURCE_GOLD).getAmount() / 2);
		killer.getResource(ResourceTypes.RESOURCE_STONE).addValue(killed.getResource(ResourceTypes.RESOURCE_STONE).getAmount() / 2);
		//Receive score from the dead
		killer.addScore(killed.getResource(ResourceTypes.RESOURCE_ACTIONS).getAmount() / 4);
		killer.addScore(ScoreValue.SCORE_VALUE_POWER);
		//so, the power who died is deleted from the game
		killed = null;
	}
	
}
