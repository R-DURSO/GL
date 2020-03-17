package process.management;
import data.boxes.*;
import data.building.Building;
import data.building.product.BuildingProduct;
import data.resource.ResourceTypes;
import data.unit.*;
import data.Power;

/**
 * <p>Singleton class to manipulate {@link data.units.Units Units} : creation, update and deletion</p>
 * <p><b>Do not use this class without {@link process.management.ActionValidator ActionValidator}, which mades most of importants verfications</b></p>
 */
public class UnitManager {
	
	private static UnitManager instance = new UnitManager();
	public static UnitManager getInstance() { return instance; }
	
	public void addUnits(Power power, Box box, int unitType, int numberUnits) {
		//cant add negative or none Unit
		if (numberUnits > 0) {
			//we will check if there is already units of the same type on the box
			if (box.hasUnit()) {
				Units unitsOnBox = box.getUnit();
				int numberUnitsOnBox = unitsOnBox.getNumber();
				int numberUnitsNeeded = numberUnits + numberUnitsOnBox;
				//if the number of units we want to add is less than the max number, we simply add thoses new units
				if (numberUnitsNeeded <= unitsOnBox.getMaxNumber()) {
					unitsOnBox.addNumber(numberUnitsNeeded);
					//modify amount of food earned between each turn
					int foodCostToRemove = numberUnits * unitsOnBox.getFoodCost();
					power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
				}
				//TODO La gestion d'or devrait se faire ici ?
				else {
					//else, we have to add to max number
					int numberExcessUnits = numberUnitsNeeded - unitsOnBox.getMaxNumber();
					unitsOnBox.addNumber(numberUnits - numberExcessUnits);
					//reduce adapted production
					int foodCostToRemove = (numberUnits - numberExcessUnits) * unitsOnBox.getFoodCost();
					power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
					//and refound gold 
					power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(unitsOnBox.getCost() * numberExcessUnits); 
				}
			}
			else {
				Units units = createUnit(unitType, numberUnits);
				//Unit shouldn't be here if invalid type
				if (units != null) {
					if (units.getNumber() > units.getMaxNumber()) {
						//unit above max number
						int numberExcessUnits = numberUnits - units.getMaxNumber();
						units.subNumber(numberExcessUnits);
						//refund gold
						power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(units.getCost() * numberExcessUnits); 
					}
					//add those unit
					box.setUnit(units);
					//tax of food per turn
					int foodCostToRemove = units.getNumber() * units.getFoodCost();
					power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
				}
			}
		}
	}
	
	private Units createUnit(int type, int nb) {
		switch(type) {
		case UnitTypes.UNIT_INFANTRY:
			return new Infantry(nb);
		case UnitTypes.UNIT_ARCHER:
			return new Archer(nb);
		case UnitTypes.UNIT_CAVALRY:
			return new Cavalry(nb);
		case UnitTypes.UNIT_PIKEMAN:
			return new Pikeman(nb);
		case UnitTypes.UNIT_BATTERING_RAM:
			return new BatteringRam(nb);
		case UnitTypes.UNIT_TREBUCHET:
			return new Trebuchet();
		case UnitTypes.UNIT_BOAT:
			return new Boat();
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
	
	private boolean isRanged (Units unit) {
		return unit.getRange() > 1;
	}
	
	public void removeUnits(Power power, Box box, int numberUnitsRemoved) {
		Units units = box.getUnit();
		int numberUnits = units.getNumber() - numberUnitsRemoved;
		if (numberUnits <= 0) {
			deleteUnits(power, box);
		}
		else {
			units.subNumber(numberUnitsRemoved);
			power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, numberUnitsRemoved * box.getUnit().getFoodCost());
		}
	}
	
	public void deleteUnits(Power power, Box box) {
		if (power != null) {
			if (box.hasUnit()) {
				int foodProdToAdd = box.getUnit().getFoodCost() * box.getUnit().getNumber();
				power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodProdToAdd);
				box.setUnit(null);
			}
		}
	}
	
	/**
	 * TODO MoveUnit a modifier
	 * 	L'unite passe sur plusieurs case
	 * 		-Convertir path en Array de Box ou faire cas par cas depuis un String
	 * 		-move Unit en 2 cas,
	 * 			-1 pour le deplacement global
	 * 			*-l'autre pour la gestion de Box en Box
	 *  			*-Doit conquérir chaque Boxes
	 *  L'unite peut arriver sur un Boat
	 *  	*-Rentrer dans le bateau
	 *  	-peut passer de bateau en bateau (comme un pont ?) s'il y en a plusieurs cote à cote
	 */
	public void moveUnits(Power powerConcerned, Box fromBox, Box targetBox) {
		Units movingUnits = fromBox.getUnit();
		if (targetBox instanceof WaterBox) {
			if (targetBox.hasUnit()) {
				if (targetBox.getUnit().getTypes() == UnitTypes.UNIT_BOAT) {
					//target est sur l'eau & target possede un bateau
					((Boat) targetBox.getUnit()).setContainedUnits(movingUnits);
				}
			}
			else {
				//dans l'eau sans unite a destination, on bouge surement un bateau
				if (movingUnits.getTypes() == UnitTypes.UNIT_BOAT) {
					targetBox.setUnit(movingUnits);
				}
			}
		}
		else {
			//sur terre, les vérifications se font dans ActionValidator
			targetBox.setUnit(movingUnits);
		}
		
		//Vérification qu'il y a eu un déplacement
		if (targetBox.hasUnit()) {
			if (targetBox.getUnit().equals(movingUnits)) {
				fromBox.setUnit(null);
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
				//La case ne nous appartient pas
				if (powerConcerned.isAllied()) {
					if (powerConcerned.getAlly() != targetBoxPower) {
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
							targetBoxPower.substractResourcesProductionPerTurn(productionType, productionPerTurn);
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
	public void attackUnits (Power powerConcerned, Box fromBox, Box targetBox) {
		/*
		 * Verification de la cible
		 * S'il y a des unités, c'est un combat
		 * Sinon, une attaque de batiment
		 */
		if (targetBox.hasUnit()) {
			Units attacker = fromBox.getUnit();
			Units defender = targetBox.getUnit();
			//calcul des degats par le nombre, et chaque point de defense reduit de 10% les degats subits
			double AttackerDamageDealt = (attacker.getDamage() * attacker.getNumber()) * (((10.0 - defender.getDefense()) / 10.0));
			//Les défenseurs subissent les dégats
			int casualityDef = defender.getNumber() - (((defender.getHealth() * defender.getNumber()) - (int)AttackerDamageDealt) / defender.getHealth());
			int casualityAtt = 0;
			//Si les attaquant sont à portés, ils ne subissent pas de perte
			if (!isRanged(attacker)) {
				//Round 2, contre-attaque si pas à distance
				//int DefenderDamageDealt = (defender.getDamage() - attacker.getDefense()) * defender.getNumber();
				double DefenderDamageDealt = (defender.getDamage() * defender.getNumber()) * (((10.0 - attacker.getDefense()) / 10.0));
				//Les attaquant subissent les dégats
				casualityAtt = attacker.getNumber() - (((attacker.getHealth() * attacker.getNumber()) - (int)DefenderDamageDealt) / attacker.getHealth());
			}
			//Les 2 Units perdent en nombres
			removeUnits(targetBox.getOwner(), targetBox, casualityDef);
			removeUnits(fromBox.getOwner(), fromBox, casualityAtt);
			//s'il n'y a plus de défenseur, ils sont morts
			if (!targetBox.hasUnit()) {
				//Les defenseurs sont morts !
				//s'il reste des attaquants...
				if (fromBox.hasUnit()) {
					//La place est libre
					if (!isRanged(attacker)) {
						//Les unités qui attaquent à distance ne se déplace pas après le combat
						moveUnits(powerConcerned, fromBox, targetBox);
					}
				}
			}
		}
		else if (targetBox instanceof GroundBox) {
			GroundBox targetGBox = (GroundBox)targetBox;
			if (targetGBox.hasBuilding()) {
				//attaque du batiment
				Units attacker = fromBox.getUnit();
				Building buildDef = targetGBox.getBuilding();
				//calcul des degats par le nombre, et chaque point de defense reduit de 10% les degats subits
				double AttackerDamageDealt = (attacker.getDamage() * attacker.getNumber()) * (((10.0 - buildDef.getDefense()) / 10.0));
				//Le batiment subit les dégats
				buildDef.applyDamage((int)AttackerDamageDealt);
				if (buildDef.isDestroyed()) {
					targetGBox.setBuilding(null);
					moveUnits(powerConcerned, fromBox, targetBox);
				}
			}
			else {
				moveUnits(powerConcerned, fromBox, targetBox);
			}
		}
		else {
			//On attaque de l'eau sans unité...
		}
	}
}