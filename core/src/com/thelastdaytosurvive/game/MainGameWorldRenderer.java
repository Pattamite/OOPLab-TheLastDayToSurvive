package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameWorldRenderer {
	private MainGameScreen mainGameScreen;
	private MainGameWorld mainGameWorld;
	
	public MainGameWorldRenderer(MainGameScreen mainGameScreen, MainGameWorld mainGameWorld){
		this.mainGameScreen = mainGameScreen;
		this.mainGameWorld = mainGameWorld;
	}
	
	public void draw(float delta, SpriteBatch batch){
		mainGameWorld.player.playerSprite.draw(mainGameScreen.batch);
		mainGameWorld.bullet.draw(delta, batch);
	}
}
