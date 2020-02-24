package process.management;
import data.boxes.*;
import data.building.product.BuildingProduct;
import data.resource.ResourceTypes;
import data.unit.*;
import data.Power;

/**
 * Singleton class to manipulate units : creation, update and deletion
 * <b>Do not use this class without {@link ActionValidator}, which mades most of importants verfications</b>
 */
public class UnitManager {
	
	private static UnitManager instance = new UnitManager();
	
	public static UnitManager getInstance() { return instance; }
	
	public void addUnits(Power power, Box box, int unitType, int numberUnits) {
		//we will check if there is already units of the same type on the box
		if(box.hasUnit()) {
			Units unitsOnBox = box.getUnit();
			int numberUnitsOnBox = unitsOnBox.getNumber();
			int numberUnitsNeeded = numberUnits + numberUnitsOnBox;
			//if the number of units we want to add is less than the max number, we simply add thoses new units
			if(numberUnitsNeeded <= unitsOnBox.getMaxNumber()) {
				unitsOnBox.addNumber(numberUnitsNeeded);
				//modify amount of food earned between each turn
				int foodCostToRemove = numberUnits * unitsOnBox.getFoodCost();
				power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
			}
			else {
				//else, we have to add to max number
				int numberExcessUnits = numberUnitsNeeded - unitsOnBox.getMaxNumber();
				unitsOnBox.addNumber(numberUnitsNeeded - numberExcessUnits);
				int foodCostToRemove = (numberUnitsNeeded - numberExcessUnits) * unitsOnBox.getFoodCost();
				power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
				//and refound gold 
				power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(unitsOnBox.getCost() * numberExcessUnits); 
			}	
		}else {
			Units units = createUnit(unitType, numberUnits);
			if(units.getNumber() > units.getMaxNumber()) {
				int numberExcessUnits = numberUnits - units.getMaxNumber();
				units.substractNumber(numberExcessUnits);
				power.getResource(ResourceTypes.RESOURCE_GOLD).addValue(units.getCost() * numberExcessUnits); 
			}
			box.setUnit(units);
			int foodCostToRemove = units.getNumber() * units.getFoodCost();
			power.substractResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodCostToRemove);
			
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
	
	public void removeUnits(Power power, Box box, int numberUnits) {
		Units units = box.getUnit();
		int numberUnitsRemoved = units.getNumber() - numberUnits;
		if(numberUnitsRemoved <= 0) {
			deleteUnits(power, box);
		}else
			units.substractNumber(numberUnitsRemoved);
	}
	
	public void deleteUnits(Power power, Box box) {
		int foodProdToAdd = box.getUnit().getFoodCost() * box.getUnit().getNumber();
		power.addResourcesProductionPerTurn(ResourceTypes.RESOURCE_FOOD, foodProdToAdd);
		box.setUnit(null);
	}
	
	public void moveUnits(Power powerConcerned, Box fromBox, Box targetBox) {
		Units movingUnits = fromBox.getUnit();
		targetBox.setUnit(movingUnits);
		fromBox.setUnit(null);
		
		//if targetBox is in ennemy's territory
		Power targetBoxPower = targetBox.getOwner();
		if (targetBoxPower != null) {
			if(targetBoxPower != powerConcerned && targetBoxPower.getAlly() != powerConcerned) {
				//powerConcerned will take this territory, and ressource gain per turn if have (inderictly, will have building on it too)  
				powerConcerned.addBox(targetBox);
				targetBox.setOwner(powerConcerned);
				
				/*Special case for buildingProducts, if takes up territory/box with a production building, 
			 	powerConcerned will 'steal' targetBoxPower's production*/
				if(targetBox instanceof GroundBox && ((GroundBox)targetBox).getBuilding() instanceof BuildingProduct) {
					BuildingProduct buildingProduct = (BuildingProduct) ((GroundBox)targetBox).getBuilding();
					//a building product can be on a non-compatible resource 
					//(Quarry on box which have Wood resource will do nothing for example)
					if (buildingProduct.isOnRightResource()) {
						int productionType = buildingProduct.getProductionType();
						int productionPerTurn = buildingProduct.getProductionPerTurn();
						powerConcerned.addResourcesProductionPerTurn(productionType, productionPerTurn);
						targetBoxPower.substractResourcesProductionPerTurn(productionType, productionPerTurn);
					}
				}
				//obviously, targetBoxPower will lose what powerConcernced earned
				targetBoxPower.removeBox(targetBox);
			}
		}
	}
	
	public void attackUnits (Power powerConcerned, Box fromBox, Box targetBox) {
		Units attacker = fromBox.getUnit();
		Units defender = targetBox.getUnit();
		
		/**
		 * place attack
		 * -if def is ranged, no counter
		 * -if def is dead, minor counter
		 * -else def counter
		 */
		
		int damageDealt = (attacker.getDamage() - defender.getDefense()) * attacker.getNumber();
		System.out.println("degat"+damageDealt);
		
		int casualityDef = defender.getNumber() - (((defender.getHealth() * defender.getNumber()) - damageDealt) / defender.getHealth());
		System.out.println("mort"+casualityDef);
		
		defender.substractNumber(casualityDef);
		if (defender.getNumber() == 0) {
			deleteUnits(targetBox.getOwner(), targetBox);
		}
		
	}
}