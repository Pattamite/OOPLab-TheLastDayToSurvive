package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameWorldRenderer {
	private MainGameScreen mainGameScreen;
	private MainGameWorld mainGameWorld;
	private MainGameHud mainGameHud;
	
	public MainGameWorldRenderer(MainGameScreen mainGameScreen, MainGameWorld mainGameWorld){
		this.mainGameScreen = mainGameScreen;
		this.mainGameWorld = mainGameWorld;
		this.mainGameHud = new MainGameHud(mainGameWorld.player);
	}
	
	public void draw(float delta, SpriteBatch batch){
		mainGameWorld.player.playerSprite.draw(batch);
		mainGameWorld.bullet.draw(batch);
		mainGameHud.draw(batch);
	}
}
