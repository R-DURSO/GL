
package process.management;
import data.boxes.*;
import data.building.Building;
import data.building.product.BuildingProduct;
import data.resource.ResourceTypes;
import data.unit.*;
import log.LoggerUtility;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import data.GameConstants;
import data.Power;
import data.ScoreValue;

/**
 * <p>Singleton class to manipulate {@link data.units.Units Units} : creation, update and deletion</p>
 * <p><b>Do not use this class without {@link process.management.ActionValidator ActionValidator}, which mades most of importants verfications</b></p>
 */
public class UnitManager {
	
	private static UnitManager instance = new UnitManager();
	public static UnitManager getInstance() { return instance; }
	private static Logger Logger = LoggerUtility.getLogger(UnitManager.class, GameConstants.LOG_TYPE);
	/**
	 * Add {@link Units} on a {@link Box} (will always be on an {@link BuildingArmy})
	 * @param power the power who want to create units 
	 * @param box where the power want to create units
	 * @param unitType the type of unit (either Infantry, Cavalry, ...)
	 * @param numberUnits the number of units to be created
	 * @see UnitTypes
	 */
	public void addUnits(Power power, Box box, int unitType, int numberUnits) {
		//cant add negative or none Unit
		if (numberUnits > 0) {
			//we have alreay check if there was units of the same type on the box
			if (box.hasUnit() && !(box.getUnit() instanceof PhantomUnit)) {
				Units unitsOnBox = box.getUnit();
				int numberUnitsOnBox = unitsOnBox.getNumber();
				int numberUnitsNeeded = numberUnits + numberUnitsOnBox;
				//if the number of units we want to add is less than the max number, we simply add thoses new units
				if (numberUnitsNeeded <= unitsOnBox.getMaxNumber()) {
					unitsOnBox.addNumber(numberUnitsNeeded);
					//modify amount of food earned between each turn
					int foodCostToRemove = numberUnits * unitsOnBox.getFoodCost();
					Logger.info(power.getName()+" remove food : "+foodCostToRemove);
					power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
					addscore(power, unitsOnBox);
				}
				else {
					//else, we have to add to max number
					int numberExcessUnits = numberUnitsNeeded - unitsOnBox.getMaxNumber();
					unitsOnBox.addNumber(numberUnits - numberExcessUnits);
					//reduce adapted production
					int foodCostToRemove = (numberUnits - numberExcessUnits) * unitsOnBox.getFoodCost();
					Logger.info(power.getName()+" remove "+foodCostToRemove+" per turn ");
					power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
					//and refound gold 
					Logger.info(power.getName()+" remove gold "+unitsOnBox.getCost()*numberExcessUnits);
					power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(unitsOnBox.getCost() * numberExcessUnits); 
					addscore(power, unitsOnBox);
				}
			}
			else {
				Units units = createUnit(unitType, numberUnits, power);
				//Unit shouldn't be here if invalid type
				if (units != null) {
					Logger.info(power.getName()+" create units type: "+unitType+", number: "+numberUnits);
					Logger.info(power.getName()+" use "+units.getCost()*numberUnits+" gold");
					if (units.getNumber() > units.getMaxNumber()) {
						//unit above max number
						int numberExcessUnits = numberUnits - units.getMaxNumber();
						units.subNumber(numberExcessUnits);
						Logger.info(power.getName()+" create too much units, refunding "+numberExcessUnits+" units");
						//refund gold
						int refundCost = units.getCost() * numberExcessUnits;
						power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(refundCost);
						Logger.info(power.getName()+" got back "+refundCost+" gold");
						
					}
					//add those unit
					box.setUnit(units);
					//tax of food per turn
					int foodCostToRemove = units.getNumber() * units.getFoodCost();
					Logger.info(power.getName()+" has maintenance cost of "+foodCostToRemove+" food each turn");
					power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
					// add score 
					addscore(power, units);
				}
			}
		}
	}
	
	private Units createUnit(int type, int nb, Power power) {
		switch(type) {
		case UnitTypes.UNIT_INFANTRY:
			return new Infantry(nb, power);
		case UnitTypes.UNIT_ARCHER:
			return new Archer(nb, power);
		case UnitTypes.UNIT_CAVALRY:
			return new Cavalry(nb, power);
		case UnitTypes.UNIT_PIKEMAN:
			return new Pikeman(nb, power);
		case UnitTypes.UNIT_BATTERING_RAM:
			return new BatteringRam(nb, power);
		case UnitTypes.UNIT_TREBUCHET:
			return new Trebuchet(power);
		case UnitTypes.UNIT_BOAT:
			return new Boat(power);
		default:
			return null;
		}
	}
	
	public int maxNumberUnit(int type) {
		switch(type) {
		case UnitTypes.UNIT_INFANTRY:
			return Infantry.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_ARCHER:
			return Archer.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_CAVALRY:
			return Cavalry.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_PIKEMAN:
			return Pikeman.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_BATTERING_RAM:
			return BatteringRam.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_TREBUCHET:
			return Trebuchet.NUMBER_MAX_UNITS;
		case UnitTypes.UNIT_BOAT:
			return Boat.NUMBER_MAX_UNITS;
		default:
			return 1;
		}
	}
	
	/**
	 * check if Unit can attack from a distance
	 * @param unit, the Unit
	 * @return true if Unit is ranged
	 */
	private boolean isRanged (Units unit) {
		return unit.getRange() > 1;
	}
	
	/**
	 * Remove a defined amount of units in a {@link Box}.<p> 
	 * If there is no more units on box, will call {@link UnitManager#deleteUnits}  
	 * @param power the power who have those units
	 * @param box where are units to be destroyed
	 * @param numberUnitsRemoved the number of units to be destroyed
	 */
	public void removeUnits(Power power, Box box, int numberUnitsRemoved) {
		Units units = box.getUnit();
		int numberUnits = units.getNumber() - numberUnitsRemoved;
		Logger.info(power.getName()+" lose "+numberUnitsRemoved+" unit");
		if (numberUnits <= 0) {
			deleteUnits(power, box);
			supScore(power,units);
		}
		else {
			units.subNumber(numberUnitsRemoved);
			int foodProdToAdd = box.getUnit().getFoodCost() * numberUnitsRemoved;
			power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodProdToAdd);
			Logger.info(power.getName()+" get "+foodProdToAdd+" food production back");
			supScore(power,units);
		}
	}
	
	/**
	 * Remove all {@link Units} on a defined {@link Box}
	 * @param power who have those units (gain production based on deleted unit)
	 * @param box where are units to be destroyed
	 */
	public void deleteUnits(Power power, Box box) {
		if (power != null) {
			if (box.hasUnit()) {
				int foodProdToAdd = box.getUnit().getFoodCost() * box.getUnit().getNumber();
				power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodProdToAdd);
				Logger.info(power.getName()+" get "+ foodProdToAdd+" food production back");
				supScore(power,box.getUnit());
				box.setUnit(null);
				
			}
		}
	}
	
	
	public void moveUnits(Power powerConcerned, Box[] pathToTake) {
		//Trebuchet that move on it's own Position is changing state
		if (pathToTake[0].getUnit().getTypes() == UnitTypes.UNIT_TREBUCHET) {
			if (pathToTake.length <= 1) {
				((Trebuchet) pathToTake[0].getUnit()).changeState();
			}
		}
		for (int i=0; i<pathToTake.length - 1; i++) {
			moveUnitsBox(powerConcerned, pathToTake[i], pathToTake[i+1]);
		}
		pathToTake[pathToTake.length-1].getUnit().resetIsMoving();
	}
	
	private void moveUnitsBox(Power powerConcerned, Box fromBox, Box targetBox) {
		Units movingUnits = fromBox.getUnit();
		if (targetBox instanceof WaterBox) {
			if (targetBox.hasUnit()) {
				//si il y a un bateau
				if (targetBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
					if (fromBox.getUnit().getTypes() != UnitTypes.UNIT_BOAT) {
						//target est sur l'eau & target possede un bateau
						((Boat) targetBox.getUnit()).setContainedUnits(movingUnits);
					}
				}
				//si on est un bateau
				else if (fromBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
					//un bateau qui va dans l'eau
					if (targetBox.getUnit().getTypes() != UnitTypes.UNIT_BOAT) { //note, cette verification est faites plus haut
						//c'est qu'un phantom
						targetBox.setUnit(movingUnits);
					}
				}
			}
			else {
				//dans l'eau sans unite a destination, on bouge surement un bateau
				if (movingUnits.getTypes() == UnitTypes.UNIT_BOAT) {
					targetBox.setUnit(movingUnits);
				}
				else {
					Logger.error("Moving a unit that isn't a Boat on WaterBox");
				}
			}
		}
		else {
			//GroundBox
			//sur terre, les vérifications se font dans ActionValidator
			//exception, la creation d'un bateau et movement d'un trebuchet
			if (movingUnits.getTypes() == UnitTypes.UNIT_BOAT) {
				//Si on bouge un bateau, on vide son contenu
				Boat BoatMovingUnit = (Boat)movingUnits;
				if ((fromBox instanceof GroundBox) && (targetBox instanceof WaterBox)) {
					//Boat move to water
					targetBox.setUnit(movingUnits);
				}
				else if (BoatMovingUnit.hasContainedUnits()) {
					targetBox.setUnit(BoatMovingUnit.getContainedUnits());
					BoatMovingUnit.setContainedUnits(null);
				}
				else {
					//ne vient pas de la terre et ne possède pas d'unit
					//on essaie de bouger un bateau sur un phantom
					targetBox.setUnit(null);
				}
			}
			else if (movingUnits.getTypes() == UnitTypes.UNIT_TREBUCHET) {
				Trebuchet TrebUnit = (Trebuchet)movingUnits;
				if (TrebUnit.getState() == Trebuchet.STATE_MOVING) {
					targetBox.setUnit(movingUnits);
				}
				else {
					Logger.error("Moving a Trebuchet that is Installed");
				}
			}
			else {
				targetBox.setUnit(movingUnits);
			}
		}
		
		//Vérification qu'il y a eu un déplacement
		if (targetBox.hasUnit()) {
			if (targetBox.getUnit().equals(movingUnits)) {
				fromBox.setUnit(null);
			}
			else {
				Logger.error("<"+fromBox.getUnit()+"> couldn't move to <"+targetBox+">, there is a <"+targetBox.getUnit()+"> ");
			}
		}
		
		boolean conquerBox = false;
		//if targetBox is in ennemy's territory
		Power targetBoxPower = targetBox.getOwner();
		if (targetBoxPower == null) {
			//targetBox is free, take it !
			conquerBox = true;
		}
		else {
			if (targetBoxPower != powerConcerned) {
				//Box doesn't belong to us
				if (powerConcerned.isAllied()) {
					if (powerConcerned.getAlly() != targetBoxPower) {
						//Not a Ally
						conquerBox = true;
					}
				}
				else {
					//Not Allied
					conquerBox = true;
				}
			}
		}
		if (conquerBox) {
			//powerConcerned will take this territory, and ressource gain per turn if any (inderictly, will gain the building on it too)  
			powerConcerned.addBox(targetBox);
			targetBox.setOwner(powerConcerned);
			//Are on Ground or on Water (checking for Building)
			if (targetBox instanceof GroundBox) {
				GroundBox targetGBox = (GroundBox)targetBox;
				/*Special case for buildingProducts, if takes up territory/box with a production building, 
			 	powerConcerned will 'steal' targetBoxPower's production*/
				if (targetGBox.hasBuilding()) {
					if (targetGBox.getBuilding() instanceof BuildingProduct) {
						BuildingProduct buildingProduct = (BuildingProduct)targetGBox.getBuilding();
						//a building product can be on a non-compatible resource 
						//(Quarry on box which have Wood resource will do nothing for example)
						if (buildingProduct.getOnRightResource()) {
							int productionType = buildingProduct.getProductionType();
							int productionPerTurn = buildingProduct.getProductionPerTurn();
							powerConcerned.addResourcesProductionPerTurn(productionType, productionPerTurn);
							Logger.info(powerConcerned.getName()+" gain "+productionPerTurn+" of ResourceType:"+productionType+" as production");
							targetBoxPower.substractResourcesProductionPerTurn(productionType, productionPerTurn);
							Logger.info(powerConcerned.getName()+" lose "+productionPerTurn+" of ResourceType:"+productionType+" as production");
						}
					}
				}
			}
			if (targetBoxPower != null) {
				//obviously, targetBoxPower will lose what powerConcernced earned
				targetBoxPower.removeBox(targetBox);
			}
		}
		
	}
	
	/**
	 * <p>Battle between 2 {@link data.unit.Units Units}.</p>
	 * <p>Damage are calculated by baseDamage * number minus 10% damage reduction per point of defense.</p>
	 * @param powerConcerned the {@link data.Power Power} launching the attack
	 * @param fromBox Attacker
	 * @param targetBox Defender
	 */
	public void attack (Power powerConcerned, Box fromBox, Box targetBox) {
		/*
		 * Verification de la cible
		 * S'il y a des unités, c'est un combat
		 * Sinon, une attaque de batiment
		 */
		if (fromBox.hasUnit()) {
			Units attacker = fromBox.getUnit();
			if (targetBox.hasUnit()) {
				//Fight between Units
				Units defender = targetBox.getUnit();
				Logger.info(powerConcerned.getName()+" launch an attack with "+attacker+" to "+defender+" ");
				//calculate damage done, with each defense reducing damage by 10%
				double AttackerDamageDealt = (attacker.getDamage() * attacker.getNumber()) * (((10.0 - defender.getDefense()) / 10.0));
				//defense take those damage
				int casualityDef = defender.getNumber() - (((defender.getHealth() * defender.getNumber()) - (int)AttackerDamageDealt) / defender.getHealth());
				Logger.info("attacker damage: "+AttackerDamageDealt+"\tdefender loses: "+casualityDef);
				
				double DefenderDamageDealt = 0;
				int casualityAtt = 0;
				//If attacker are range, they dont take damage
				if (!isRanged(attacker)) {
					//Round 2, counter-strike (attacker gain 10% damage reduction)
					DefenderDamageDealt = (defender.getDamage() * defender.getNumber()) * (((10.0 - attacker.getDefense() + 1) / 10.0));
					//attacker take dammage
					casualityAtt = attacker.getNumber() - (((attacker.getHealth() * attacker.getNumber()) - (int)DefenderDamageDealt) / attacker.getHealth());
				}
				Logger.info("defender damage: "+DefenderDamageDealt+"\tattacker loses:"+casualityAtt);
				//Those 2 Unit loses Units
				removeUnits(targetBox.getOwner(), targetBox, casualityDef);
				removeUnits(fromBox.getOwner(), fromBox, casualityAtt);
				//If there isn't defender, they are dead
				if (!targetBox.hasUnit()) {
					//Defender are dead !
					Logger.info("defender are dead !");
					//if there is attacker...
					if (fromBox.hasUnit()) {
						//the Box is our to take
						if (!isRanged(attacker)) {
							//But if Unit is Ranged, it doesn't move
							Box[] Path = {fromBox, targetBox};
							moveUnits(powerConcerned, Path);
							Logger.info("attacker are alive, and capture defender position");
						}
					}
					else {
						Logger.info("attacker are dead !");
					}
				}
			}
			else if (targetBox instanceof GroundBox) {
				GroundBox targetGBox = (GroundBox)targetBox;
				if (targetGBox.hasBuilding()) {
					//attacking a building
					Building buildDef = targetGBox.getBuilding();
					Logger.info(powerConcerned.getName()+" launch an attack with "+attacker+" to the building: "+buildDef.toString()+" ");
					//calculate damage done, with each defense reducing damage by 10%
					double AttackerDamageDealt = (attacker.getSiegeDamage() * attacker.getNumber()) * (((10.0 - buildDef.getDefense()) / 10.0));
					//Building take damage
					buildDef.applyDamage((int)AttackerDamageDealt);
					Logger.info("Building takes "+AttackerDamageDealt+" damage, "+buildDef.getHealth()+"HP remaining");
					if (buildDef.isDestroyed()) {
						Logger.info("attacker have destroid the building: "+buildDef.toString());
						BuildingManager.getInstance().destroyBuilding(targetGBox);
						Box[] Path = {fromBox, targetBox};
						moveUnits(powerConcerned, Path);
					}
				}
				else {
					Box[] Path = {fromBox, targetBox};
					moveUnits(powerConcerned, Path);
					Logger.info("attacker launch an attack on plain, moving unit");
				}
			}
			else {
				//On attaque de l'eau sans unité...
			}
		}
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
	private void addscore(Power power ,Units units) {
		switch(units.getTypes()) {
		case UnitTypes.UNIT_INFANTRY:
			power.addScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_INFANTRY);
			Logger.info(power.getName()+" gain "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_INFANTRY+" score");
			break;
		case UnitTypes.UNIT_ARCHER:
			power.addScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_ARCHER);
			Logger.info(power.getName()+" gain "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_ARCHER+" score");
			break;
		case UnitTypes.UNIT_CAVALRY:
			power.addScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_CAVALERY);
			Logger.info(power.getName()+" gain "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_CAVALERY+" score");
			break;
		case UnitTypes.UNIT_PIKEMAN:
			power.addScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_PIKEMAN);
			Logger.info(power.getName()+" gain "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_PIKEMAN+" score");
			break;
		case UnitTypes.UNIT_BATTERING_RAM:
			power.addScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_BATTERING_RAM);
			Logger.info(power.getName()+" gain "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_BATTERING_RAM+" score");
			break;
		case UnitTypes.UNIT_TREBUCHET:
			power.addScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_TREBUCHET);
			Logger.info(power.getName()+" gain "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_TREBUCHET+" score");
			break;
		case UnitTypes.UNIT_BOAT:
			power.addScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_BOAT);
			Logger.info(power.getName()+" gain "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_BOAT+" score");
			break;
		}
	}
	private void supScore(Power power ,Units units) {
		switch(units.getTypes()) {
		case UnitTypes.UNIT_INFANTRY:
			power.suppScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_INFANTRY);
			Logger.info(power.getName()+" lose "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_INFANTRY+" score");
			break;
		case UnitTypes.UNIT_ARCHER:
			power.suppScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_ARCHER);
			Logger.info(power.getName()+" lose "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_ARCHER+" score");
			break;
		case UnitTypes.UNIT_CAVALRY:
			power.suppScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_CAVALERY);
			Logger.info(power.getName()+" lose "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_CAVALERY+" score");
			break;
		case UnitTypes.UNIT_PIKEMAN:
			power.suppScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_PIKEMAN);
			Logger.info(power.getName()+" lose "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_PIKEMAN+" score");
			break;
		case UnitTypes.UNIT_BATTERING_RAM:
			power.suppScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_BATTERING_RAM);
			Logger.info(power.getName()+" lose "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_BATTERING_RAM+" score");
			break;
		case UnitTypes.UNIT_TREBUCHET:
			power.suppScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_TREBUCHET);
			Logger.info(power.getName()+" lose "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_TREBUCHET+" score");
			break;
		case UnitTypes.UNIT_BOAT:
			power.suppScore(units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_BOAT);
			Logger.info(power.getName()+" lose "+units.getNumber()*ScoreValue.SCORE_VALUE_UNITS_BOAT+" score");
			break;
		}
	}
}