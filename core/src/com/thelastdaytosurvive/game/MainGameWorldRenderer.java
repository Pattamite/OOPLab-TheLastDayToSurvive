package com.thelastdaytosurvive.game;

public class MainGameWorldRenderer {
	private MainGameScreen mainGameScreen;
	private MainGameWorld mainGameWorld;
	
	public MainGameWorldRenderer(MainGameScreen mainGameScreen, MainGameWorld mainGameWorld){
		this.mainGameScreen = mainGameScreen;
		this.mainGameWorld = mainGameWorld;
	}
	
	public void draw(){
		mainGameWorld.player.playerSprite.draw(mainGameScreen.batch);
	}
}
