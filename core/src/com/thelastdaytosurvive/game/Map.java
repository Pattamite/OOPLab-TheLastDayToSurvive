package com.thelastdaytosurvive.game;

public class Map {
	private MainGameWorld mainGameWorld;
	
	private int blockSize = 25;
	private char worldMap[][];
	private char aiMap[][];
	private int xMapNum;
	private int yMapNum;
	
	public Map(MainGameWorld mainGameWorld){
		this.mainGameWorld = mainGameWorld;
		
		calculateMapSize();
		setUpMap();
	}
	
	public void update(){
		
	}
	
	private void calculateMapSize(){
		xMapNum = (int) (MainGameWorld.MAP_X / blockSize);
		yMapNum = (int) (MainGameWorld.MAP_Y / blockSize);
	}
	
	private void setUpMap(){
		worldMap = new char[yMapNum][xMapNum];
		aiMap = new char[yMapNum][xMapNum];
		
		for(int i = 0 ; i < yMapNum ; i++){
			for(int j = 0 ; j < xMapNum ; j++){
				worldMap[i][j] = '.';
				aiMap[i][j] = '.';
			}
		}
	}
	
	private int xPosition(float value){
		return (int) (value / blockSize);
	}
	
	private int yPosition(float value){
		return (int) ((MainGameWorld.MAP_Y  - value) / blockSize);
	}
}
