package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameScreen extends ScreenAdapter {
	
	private LastDayGame lastDayGame;
	private MainGameWorld mainGameWorld;
	private MainGameWorldRenderer mainGameWorldRenderer;
	public SpriteBatch batch;
	
	public MainGameScreen(LastDayGame lastDayGame){
		this.lastDayGame = lastDayGame;
		mainGameWorld = new MainGameWorld(this);
		mainGameWorldRenderer = new MainGameWorldRenderer(this, mainGameWorld);
		
		batch = lastDayGame.batch;
	}
	
	@Override
	public void render(float delta){
		
		mainGameWorld.update(delta);
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		mainGameWorldRenderer.draw(delta, batch);
		batch.end();
	}
}
