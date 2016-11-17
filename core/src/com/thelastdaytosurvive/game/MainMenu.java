package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenu extends ScreenAdapter {
	
	private LastDayGame lastDayGame;
	private SpriteBatch batch;
	private BitmapFont font80;
	private BitmapFont font50;
	private BitmapFont font32;
	
	public MainMenu(LastDayGame lastDayGame){
		this.lastDayGame = lastDayGame;
		font80 = new BitmapFont(Gdx.files.internal("Font/Square80.fnt"));
		font50 = new BitmapFont(Gdx.files.internal("Font/Square50.fnt"));
		font32 = new BitmapFont(Gdx.files.internal("Font/Square32.fnt"));
		batch = lastDayGame.batch;
	}
	
	@Override
	public void render(float delta){
		update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		font80.setColor(Color.WHITE);
		font80.draw(batch, "The Last Day To Survive", 250, 800);
		
		font50.setColor(Color.SALMON);
		font50.draw(batch, "Press 'H' to go to How To Play", 380, 400);
		font50.setColor(Color.FOREST);
		font50.draw(batch, "Press 'P' to Play", 550, 300);
		
		font32.setColor(Color.GOLDENROD);
		font32.draw(batch, "Song use : \"Fields\" & \"Collapse\" by Sioum ", 430, 100);
		font32.draw(batch, "\"Digital Mk. 2\" by Tri-Tachyon ", 620, 50);
		batch.end();
	}

	
	private void update(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
			lastDayGame.goToHowToPlay();
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
			lastDayGame.goToMainGame();
		}
	}
}
