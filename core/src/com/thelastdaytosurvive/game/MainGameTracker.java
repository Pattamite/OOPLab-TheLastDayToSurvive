package com.thelastdaytosurvive.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MainGameTracker {
	public static final int PHASE_PREP_TYPE = 0;
	public static final int PHASE_COMBAT_TYPE = 1;
	public static final long PAHSE_PREP_TIME = 60000;
	
	private MainGameWorld mainGameWorld;
	
	private int currentPhase;
	private int scoreType[] = new int[]{100, 200};
	private int score = 0;
	private int remainEnemy = 0;
	private int currentWave = 0;
	private long prepStratedTime = 0;
	private long spawnDelay = 500;
	private long lastSpawnTime = 0;
	private float positionOffset = 100f;
	
	private int dumbPerWave = 12;
	private int smartPerWave = 3;
	private int dumbNum = 0;
	private int smartNum = 0;
	
	public MainGameTracker(MainGameWorld mainGameWorld){
		this.mainGameWorld = mainGameWorld;
		
		currentPhase = PHASE_PREP_TYPE;
		prepStratedTime = TimeUtils.millis();
	}
	
	
	
	public void update(){
		if(currentPhase == PHASE_PREP_TYPE && (TimeUtils.millis() - prepStratedTime) > PAHSE_PREP_TIME){
			combatActivate();
		}
		else if(currentPhase == PHASE_COMBAT_TYPE){
			if(remainEnemy <= 0){
				prepActivate();
			} else {
				spawnEnemy();
			}
		}
	}
	
	public void enemyDown(int type){
		remainEnemy--;
		score += scoreType[type];
		//dropItem(type);
	}
	
	public void combatActivate(){
		currentPhase = PHASE_COMBAT_TYPE;
		currentWave++;
		dumbNum = (currentWave * dumbPerWave) + (int) MathUtils.random(currentWave * 1, currentWave * 2);
		smartNum = (currentWave * smartPerWave) + (int) MathUtils.random(0, currentWave * 1);
		remainEnemy = dumbNum + smartNum;
	}
	
	public void prepActivate(){
		currentPhase = PHASE_PREP_TYPE;
		prepStratedTime = TimeUtils.millis();
	}
	
	public int getPhase(){
		return currentPhase;
	}
	
	public long getPrepTime(){
		return ((PAHSE_PREP_TIME - (TimeUtils.millis() - prepStratedTime)) / 1000) + 1;
	}
	
	public int getRemainEnemy(){
		return remainEnemy;
	}
	
	public int getScore(){
		return score;
	}
	
	private void spawnEnemy(){
		if((TimeUtils.millis() - lastSpawnTime) > spawnDelay && (dumbNum > 0 || smartNum > 0)){
			int chosenType = (int) MathUtils.random(Enemy.ENEMY_DUMB, Enemy.ENEMY_SMART );
			if(chosenType == Enemy.ENEMY_DUMB && dumbNum <= 0){
				chosenType = Enemy.ENEMY_SMART;
			}
			if(chosenType == Enemy.ENEMY_SMART && smartNum <= 0){
				chosenType = Enemy.ENEMY_DUMB;
			}
			
			if(chosenType == Enemy.ENEMY_DUMB){
				dumbNum--;
			}
			if(chosenType == Enemy.ENEMY_SMART){
				smartNum--;
			}
			
			int chosenSide = (int) MathUtils.random(0, 3);
			
			switch (chosenSide){
				case 0 : spawnEnemyLeft(chosenType); break;
				case 1 : spawnEnemyRight(chosenType); break;
				case 2 : spawnEnemyUp(chosenType); break;
				case 3 : spawnEnemyDown(chosenType); break;
				default : break;
			}
			
			lastSpawnTime = TimeUtils.millis();
		}
	}
	
	private void spawnEnemyLeft(int type){
		float xPosition = (-1) * positionOffset;
		float yPosition = MathUtils.random(0, MainGameWorld.MAP_Y);
		
		mainGameWorld.getEnemy().newEnemy(type, xPosition, yPosition);
	}
	
	private void spawnEnemyRight(int type){
		float xPosition = MainGameWorld.MAP_X + positionOffset;
		float yPosition = MathUtils.random(0, MainGameWorld.MAP_Y);
		
		mainGameWorld.getEnemy().newEnemy(type, xPosition, yPosition);
	}
	
	private void spawnEnemyUp(int type){
		float xPosition = MathUtils.random(0, MainGameWorld.MAP_X);
		float yPosition = MainGameWorld.MAP_Y + positionOffset;
		
		mainGameWorld.getEnemy().newEnemy(type, xPosition, yPosition);
	}
	
	private void spawnEnemyDown(int type){
		float xPosition = MathUtils.random(0, MainGameWorld.MAP_X);
		float yPosition = (-1) * positionOffset;
		
		mainGameWorld.getEnemy().newEnemy(type, xPosition, yPosition);
	}
}
