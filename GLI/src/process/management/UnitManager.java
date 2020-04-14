
package process.management;
import data.boxes.*;
import data.building.Building;
import data.building.BuildingTypes;
import data.building.product.BuildingProduct;
import data.building.special.BuildingSpecial;
import data.resource.Resource;
import data.resource.ResourceTypes;
import data.unit.*;
import log.LoggerUtility;

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
			if (box.hasUnit() && !(box.getUnit().isPhantom())) {
				Units unitsOnBox = box.getUnit();
				int numberUnitsOnBox = unitsOnBox.getNumber();
				int numberUnitsNeeded = numberUnits + numberUnitsOnBox;
				//if the number of units we want to add is less than the max number, we simply add thoses new units
				if (numberUnitsNeeded <= unitsOnBox.getMaxNumber()) {
					unitsOnBox.addNumber(numberUnits);
					
					//modify amount of food earned between each turn
					int foodCostToRemove = numberUnits * unitsOnBox.getFoodCost();
					Logger.info(power.getName()+" lose "+foodCostToRemove+" Food per turn");
					power.subResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
					addScore(power, unitType, numberUnits);
				}
				else {
					//else, we have to add to max number
					int numberExcessUnits = numberUnitsNeeded - unitsOnBox.getMaxNumber();
					int numberToAdd = numberUnits - numberExcessUnits;
					unitsOnBox.addNumber(numberToAdd);
					
					//reduce adapted production
					int foodCostToRemove = numberToAdd * unitsOnBox.getFoodCost();
					Logger.info(power.getName()+" lose "+foodCostToRemove+" Food per turn ");
					power.subResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
					
					//and refound gold
					int amountRefund = unitsOnBox.getCost() * numberExcessUnits;
					Logger.info(power.getName()+" is refund "+amountRefund+" Gold");
					power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(amountRefund); 
					addScore(power, unitType, numberExcessUnits);
				}
			}
			else {
				Units units = createUnit(unitType, numberUnits, power);
				//Unit shouldn't be here if invalid type
				if (units != null) {
					Logger.info(power.getName()+" create "+numberUnits+" "+units.getClass().getSimpleName());
					Logger.info(power.getName()+" use "+units.getCost()*numberUnits+" Gold");
					if (units.getNumber() > units.getMaxNumber()) {
						//unit above max number
						int numberExcessUnits = numberUnits - units.getMaxNumber();
						units.subNumber(numberExcessUnits);
						Logger.info(power.getName()+" create too much units, refunding "+numberExcessUnits+" units");
						//refund gold
						int refundCost = units.getCost() * numberExcessUnits;
						power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(refundCost);
						Logger.info(power.getName()+" got back "+refundCost+" Gold");
						
					}
					//add those unit
					box.setUnit(units);
					//tax of food per turn
					int foodCostToRemove = units.getNumber() * units.getFoodCost();
					Logger.info(power.getName()+" lose "+foodCostToRemove+" Food per turn");
					power.subResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
					// add score 
					addScore(power, unitType, units.getNumber());
				}
			}
		}
	}
	
	/**
	 * Create a {@link data.unit.Units Unit}
	 * @param type {@link data.unit.UnitTypes UnitTypes}
	 * @param nb number of Unit created
	 * @param power {@link data.Power Power} controlling it
	 * @return created Unit
	 */
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
			return new BatteringRam(power);
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
	 * check if {@link data.unit.Units Unit} can attack from a distance
	 * @param unit {@link data.unit.Units Units}
	 * @return true if Unit is {@link data.unit.Units#getRange() ranged}
	 */
	private boolean isRanged (Units unit) {
		return unit.getRange() > 1;
	}
	
	/**
	 * Remove a defined amount of units in a {@link Box}.<p> 
	 * If there is no more units on box, will call {@link process.management.UnitManager#deleteUnits(Power, Box) deleteUnits}
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
		}
		else {
			units.subNumber(numberUnitsRemoved);
			int foodProdToAdd = box.getUnit().getFoodCost() * numberUnitsRemoved;
			power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodProdToAdd);
			Logger.info(power.getName()+" get "+foodProdToAdd+" food production back");
			subScore(power, units.getTypes(), numberUnitsRemoved);
		}
	}
	
	/**
	 * Remove all {@link Units} on a defined {@link Box}
	 * @param power {@link data.Power Power} who owned those units (and will regain production)
	 * @param box where are units to be destroyed
	 */
	public void deleteUnits(Power power, Box box) {
		if (power != null) {
			if (box.hasUnit()) {
				if (power == box.getUnit().getOwner()) {
					Units UnitsToDelete = box.getUnit();
					int foodProdToAdd = UnitsToDelete.getFoodCost() * UnitsToDelete.getNumber();
					power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodProdToAdd);
					Logger.info(power.getName()+" get "+ foodProdToAdd+" food production back");
					subScore(power, UnitsToDelete.getTypes(), UnitsToDelete.getNumber());
					box.setUnit(null);
				}
			}
		}
	}
	
	/**
	 * Move a {@link Units} depending on the Array of {@link Box} given
	 * @param powerConcerned who have the unit moving
	 * @param pathToTake Array of Box the unit will go through
	 */
	public void moveUnits(Power powerConcerned, Box[] pathToTake) {
		if (pathToTake[0].hasUnit()) {
			Box firstBox = pathToTake[0];
			Units movingUnits = firstBox.getUnit();
			firstBox.setUnit(null);
			
			if (movingUnits.getOwner() == powerConcerned) {
				//Trebuchet that move on it's own Position is changing state
				if (movingUnits.getTypes() == UnitTypes.UNIT_TREBUCHET) {
					if (pathToTake.length <= 1) {
						((Trebuchet) movingUnits).changeState();
					}
				}
				
				Box lastBox = firstBox;
				for( int i = 0; i < pathToTake.length; i++) {
					if (moveUnitsBox(movingUnits, pathToTake[i])) {
						lastBox = pathToTake[i];
					}
				}
				
					//Application du déplacement
				
				//Suppression du Phantom
				if (pathToTake[pathToTake.length-1].hasUnit()) {
					if (pathToTake[pathToTake.length-1].getUnit().isPhantom()) {
						pathToTake[pathToTake.length-1].setUnit(null);
					}
				}
				
				//Verification du chargement
				if (lastBox.hasUnit()) {
					if (lastBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
						Boat lastBoat = (Boat)lastBox.getUnit();
						if (lastBoat.hasContainedUnits()) {
							if (lastBoat.getContainedUnits().isPhantom()) {
								//It's a Phantom, move in
								lastBoat.setContainedUnits(movingUnits);
							}
							else if (lastBoat.getContainedUnitsTypes() == movingUnits.getTypes()) {
								//Same unit Types
								lastBoat.getContainedUnits().addNumber(movingUnits.getNumber());
							}
							else {
								//Nor a Phantom or same UnitTypes, dont go in
								Logger.error("Moving different units Types in the same Boat");
							}
						}
					}
					else if (lastBox.getUnit().getTypes() == movingUnits.getTypes()) {
						//Fusion d'units
						lastBox.getUnit().addNumber(movingUnits.getNumber());
					}
				}
				else {
					lastBox.setUnit(movingUnits);
					/**
					 * On peut arriver ici si:
					 * 		-on est du même type
					 * 		-un trebuchet change d'etat
					 * 		-un bateau qui doit déposer?
					 * Problème
					 * 		-déposer des units au même endroit les fait se tuer
					 * 		-monter 2 units dans un bateau fait mourir la première
					 */
				}
				movingUnits.resetIsMoving();
			}
			else {
				Logger.error(powerConcerned.getName()+" try to move another Power Units");
			}
		}
		else {
			Logger.error("No Unit was found, canceling movement");
		}
	}
	
	/**
	 * Private class made for a {@link data.unit.Units Unit} to move one {@link data.boxes.Box Box} to another
	 * @param movingUnits the {@link data.unit.Units Unit} moving
	 * @param visitBox the {@link data.boxes.Box Box} we are in
	 * @return true if the {@link data.unit.Units Unit} can go to this {@link data.boxes.Box Box}
	 */
	private boolean moveUnitsBox(Units movingUnits, Box visitBox) {
		//Initialize checker boolean
		boolean canMove = false;
		boolean checkBuilding = false;
		boolean checkAlly = false;
		boolean checkUnit = false;
		boolean conquerBox = false;
		
		//Verification du cas du bateau (pour charger/decharger les Units)
		if (visitBox instanceof WaterBox) {
			if (visitBox.hasUnit()) {
				//il y a un bateau a destination ?
				if (visitBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
					//peut-on monter dans le bateau ?
					if (movingUnits.getTypes() != UnitTypes.UNIT_BOAT) {
						canMove = true;
						checkAlly = true;
					}
				}
				//si on est un bateau
				else if (movingUnits.getTypes() == UnitTypes.UNIT_BOAT) {
					//un bateau dans l'eau
					canMove = true;
					checkAlly = true;
				}
				else {
					Logger.error("Moving a unit that isn't a Boat on WaterBox");
					return false;
				}
			}
			else {
				//dans l'eau sans unite a destination, on bouge surement un bateau
				if (movingUnits.getTypes() == UnitTypes.UNIT_BOAT) {
					canMove = true;
					checkAlly = true;
				}
				else {
					Logger.error("Moving a unit that isn't a Boat on WaterBox");
					return false;
				}
			}
		}
		else {
			//explicit GroundBox
			//sur terre, les vérifications se font dans ActionValidator
			//exception, la creation d'un bateau et movement d'un trebuchet
			if (movingUnits.getTypes() == UnitTypes.UNIT_BOAT) {
				//Si on bouge un bateau, on vide son contenu
				Boat movingUnitsBoat = (Boat)movingUnits;
				if (movingUnitsBoat.hasContainedUnits()) {
					visitBox.setUnit(movingUnitsBoat.getContainedUnits());
					visitBox.getUnit().resetIsMoving();
					movingUnitsBoat.setContainedUnits(null);
				}
				//Boat should move to water
				canMove = false;
				checkBuilding = true;
			}
			else if (movingUnits.getTypes() == UnitTypes.UNIT_TREBUCHET) {
				Trebuchet TrebUnit = (Trebuchet)movingUnits;
				if (TrebUnit.getState() == Trebuchet.STATE_MOVING) {
					canMove = true;
					checkBuilding = true;
				}
				else {
					//On devrait pas arriver ici, mais au-cas où...
					Logger.error("Moving a Trebuchet that is Installed");
					return false;
				}
			}
			else {
				//Unite sur Terre
				canMove = true;
				checkBuilding = true;
			}
		}
		
		
		if (checkBuilding) {
			checkAlly = true;
			if (visitBox instanceof GroundBox) {
				GroundBox visitGBox = (GroundBox)visitBox; 
				if (visitGBox.hasBuilding()) {
					if (visitGBox.getBuilding() instanceof BuildingSpecial) {
						//dont conquer if its a special Building
						canMove = false;
						checkAlly = false;
					}
				}
			}
		}
		
		Power powerConcerned = movingUnits.getOwner();
		//if targetBox is in ennemy's territory
		Power BoxPower = visitBox.getOwner();
		if (checkAlly) {
			if (BoxPower == null) {
				//targetBox is free, take it !
				//as there isnt a Power, dont check for Unit
				conquerBox = true;
			}
			else {
				if (powerConcerned != BoxPower) {
					//Box doesn't belong to us
					if (powerConcerned.isAllied()) {
						if (powerConcerned.getAlly() != BoxPower) {
							//Not a Ally
							checkUnit = true;
						}
					}
					else {
						//Not Allied
						checkUnit = true;
					}
				}
			/*
				else if (visitBox.getUnit().getTypes() < 0) {
					//just a PhantomUnit here, go as there is nobody here
					conquerBox = true;
				}
			*/
				//if there are Unit, we cannot conquer
			}
		}
		
		if (checkUnit) {
			if (!visitBox.hasUnit()) {
				//No Unit and not Ally, including for Phantom
				conquerBox = true;
				canMove = true;
			}
			else if (visitBox.getUnit().isPhantom()) {
				//only a Phantom here
				conquerBox = true;
				canMove = true;
			}
			//dont conquer if there is a Unit
		}
		
		if (conquerBox) {
			//powerConcerned will take this territory, and ressource gain per turn if any (inderictly, will gain the building on it too)  
			powerConcerned.addBox(visitBox);
			visitBox.setOwner(powerConcerned);
			//checking for Building
			if (visitBox instanceof GroundBox) {
				GroundBox targetGBox = (GroundBox)visitBox;
				/* Special case for buildingProducts, if takes up territory/box with a production building, 
			 	powerConcerned will 'steal' targetBoxPower's production */
				if (targetGBox.hasBuilding()) {
					if (targetGBox.getBuilding() instanceof BuildingProduct) {
						BuildingProduct buildingProduct = (BuildingProduct)targetGBox.getBuilding();
						//a building product can be on a non-compatible resource 
						//(Quarry on box which have Wood resource will do nothing for example)
						if (buildingProduct.getOnRightResource()) {
							int productionType = buildingProduct.getProductionType();
							int productionPerTurn = buildingProduct.getProductionPerTurn();
							powerConcerned.addResourcesProductionPerTurn(productionType, productionPerTurn);
							Logger.info(powerConcerned.getName()+" gain "+productionPerTurn+" of "+Resource.getResourceType(productionType)+" as production");
							BoxPower.subResourcesProductionPerTurn(productionType, productionPerTurn);
							Logger.info(BoxPower.getName()+" lose "+productionPerTurn+" of "+Resource.getResourceType(productionType)+" as production");
						}
					}
				}
			}
			if (BoxPower != null) {
				//obviously, targetBoxPower will lose what powerConcernced earned
				BoxPower.removeBox(visitBox);
			}
		}
		
		return canMove;
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
			if (powerConcerned == attacker.getOwner()) {
				if (targetBox.hasUnit()) {
					//Fight between Units
					Units defender = targetBox.getUnit();
					//we want to avoid fight between friendly units
					if (powerConcerned == defender.getOwner()) {
						Logger.warn(powerConcerned.getName()+" try to attack his own units");
					}
					else if ((powerConcerned.isAllied()) && (powerConcerned.getAlly() == defender.getOwner())) {
						Logger.warn(powerConcerned.getName()+" try to attack his Ally: "+powerConcerned.getAlly().getName());
					}
					else if (defender.getTypes() < 0) {
						Logger.warn(powerConcerned.getName()+" try to attack the ghost of "+defender.getOwner().getName());
					}
					else {
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
							if (buildDef.getType() == BuildingTypes.BUILDING_CAPITAL) {
								PowerManager.getInstance().killPower(powerConcerned, targetGBox.getOwner());
							}
							BuildingManager.getInstance().destroyBuilding(targetGBox);
							Box[] Path = {fromBox, targetBox};
							moveUnits(powerConcerned, Path);
						}
					}
					else {
						Box[] Path = {fromBox, targetBox};
						moveUnits(powerConcerned, Path);
						Logger.info("attacker attack a plain, moving unit");
					}
				}
				else {
					//Attacking just Water...
				}
				
				//End of Attack
				attacker.resetIsMoving();
			}
			//Launching an attack with units that doesn't belong to us
		}
		//no Attacker, no Attack
	}
	
	private void addScore(Power power, int unitTypes, int number) {
		int scoreGain = 0;
		switch(unitTypes) {
		case UnitTypes.UNIT_INFANTRY:
			scoreGain = number*ScoreValue.SCORE_VALUE_UNITS_INFANTRY;
			break;
		case UnitTypes.UNIT_ARCHER:
			scoreGain = number*ScoreValue.SCORE_VALUE_UNITS_ARCHER;
			break;
		case UnitTypes.UNIT_CAVALRY:
			scoreGain = number*ScoreValue.SCORE_VALUE_UNITS_CAVALRY;
			break;
		case UnitTypes.UNIT_PIKEMAN:
			scoreGain = number*ScoreValue.SCORE_VALUE_UNITS_PIKEMAN;
			break;
		case UnitTypes.UNIT_BATTERING_RAM:
			scoreGain = number*ScoreValue.SCORE_VALUE_UNITS_BATTERING_RAM;
			break;
		case UnitTypes.UNIT_TREBUCHET:
			scoreGain = number*ScoreValue.SCORE_VALUE_UNITS_TREBUCHET;
			break;
		case UnitTypes.UNIT_BOAT:
			scoreGain = number*ScoreValue.SCORE_VALUE_UNITS_BOAT;
			break;
		}
		power.addScore(scoreGain);
		Logger.info(power.getName()+" gain "+scoreGain+" score");
	}
	
	private void subScore(Power power, int unitTypes, int number) {
		int scoreLost = 0;
		switch(unitTypes) {
		case UnitTypes.UNIT_INFANTRY:
			scoreLost = number*ScoreValue.SCORE_VALUE_UNITS_INFANTRY;
			break;
		case UnitTypes.UNIT_ARCHER:
			scoreLost = number*ScoreValue.SCORE_VALUE_UNITS_ARCHER;
			break;
		case UnitTypes.UNIT_CAVALRY:
			scoreLost = number*ScoreValue.SCORE_VALUE_UNITS_CAVALRY;
			break;
		case UnitTypes.UNIT_PIKEMAN:
			scoreLost = number*ScoreValue.SCORE_VALUE_UNITS_PIKEMAN;
			break;
		case UnitTypes.UNIT_BATTERING_RAM:
			scoreLost = number*ScoreValue.SCORE_VALUE_UNITS_BATTERING_RAM;
			break;
		case UnitTypes.UNIT_TREBUCHET:
			scoreLost = number*ScoreValue.SCORE_VALUE_UNITS_TREBUCHET;
			break;
		case UnitTypes.UNIT_BOAT:
			scoreLost = number*ScoreValue.SCORE_VALUE_UNITS_BOAT;
			break;
		}
		power.subScore(scoreLost);
		Logger.info(power.getName()+" lose "+scoreLost+" score");
	}
	
}