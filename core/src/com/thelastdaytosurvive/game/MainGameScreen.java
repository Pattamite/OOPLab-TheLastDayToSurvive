package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameScreen extends ScreenAdapter {
	
	private LastDayGame lastDayGame;
	public Player player;
	
	public MainGameScreen(LastDayGame lastDayGame)
	{
		this.lastDayGame = lastDayGame;
		player = new Player();
	}
	
	@Override
	public void render(float delta)
	{
		player.Update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		SpriteBatch batch = lastDayGame.batch;
		batch.begin();
		player.playerSprite.draw(batch);
		batch.end();
	}
}
