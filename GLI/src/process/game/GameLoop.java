package process.game;
import java.util.ArrayList;

import GUI.components.menu.PreferencesPanel;
import data.actions.*;
import data.GameMap;
import data.Power;
import data.resource.*;
import process.management.ActionValidator;
import process.management.MapBuilder;

public class GameLoop {
	
	@SuppressWarnings("unchecked")
	private ArrayList<Action> action[]= (ArrayList<Action>[]) new ArrayList[8];
	
	private ActionValidator actionValidator;
	// constante temporaire 
	private boolean isPlaying = true;
	private Power powers[];
	GameMap map;
	
	public GameLoop( PreferencesPanel preferences ) {
		initActionArray();
		
		actionValidator = new ActionValidator(map);
		
	}
	

	public void play() {

		//the main game loop, which won't end unitl game is finished, or player wants to quit
		while(isPlaying) {
			
			//for player 1, who is the unique human
			while(canContinueTurn(powers[0].getResource(ResourceTypes.RESOURCE_ACTIONS))) {
				//window.doSomething()
				
			}
		
			//for others non-human players 
			for( int i = 1 ; i < powers.length;  i++) {
				
				//they will do all actions thay can 
				while(canContinueTurn(powers[i].getResource(ResourceTypes.RESOURCE_ACTIONS))) {
					// test des action possible 


				}
			}
		}
	}
	
	public void initActionArray() {
		for (int i = 0; i < ActionTypes.NUMBER_ACTIONS; i++) {
			action[i]= new ArrayList<Action>();
		}

	}
	public Boolean canContinueTurn(Resource actionPoints) {
		return actionPoints.getAmount() > 0;
	}
	public  void createPower(int number) {
		this.powers =new Power[number];
		for(int i = 0; i < number; i++){
			this.powers[i] = new Power("joueur " + (i+1));
		}
	}
	
}
