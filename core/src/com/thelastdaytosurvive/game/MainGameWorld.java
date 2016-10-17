package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameWorld {
	private MainGameScreen mainGameScreen;
	public SpriteBatch batch;
	public Player player;
	
	public MainGameWorld(MainGameScreen mainGameScreen){
		this.mainGameScreen = mainGameScreen;
		batch = mainGameScreen.batch;
		player = new Player();
	}
	
	public void update(float delta){
		player.update();
	}
}
