package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameWorldRenderer {
	private MainGameScreen mainGameScreen;
	private MainGameWorld mainGameWorld;
	private MainGameHud mainGameHud;
	
	private Texture backgroundTexture;
	
	public MainGameWorldRenderer(MainGameScreen mainGameScreen, MainGameWorld mainGameWorld){
		this.mainGameScreen = mainGameScreen;
		this.mainGameWorld = mainGameWorld;
		this.mainGameHud = new MainGameHud(mainGameWorld, mainGameScreen);
		
		backgroundTexture = new Texture("Background/background.png");
	}
	
	public void draw(float delta, SpriteBatch batch){
		batch.draw(backgroundTexture, 0, 0);
		mainGameWorld.getBullet().draw(batch);
		mainGameWorld.getMap().draw(batch);
		mainGameWorld.getItemdropper().draw(batch);
		mainGameWorld.getEnemy().draw(batch);
		mainGameWorld.getPlayer().getSprite().draw(batch);
		mainGameWorld.getCrafting().draw(batch);
		mainGameHud.draw(batch);
	}
}
