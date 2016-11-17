package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen  extends ScreenAdapter {
	
	private LastDayGame lastDayGame;
	private SpriteBatch batch;
	private BitmapFont font80;
	private BitmapFont font50;
	private BitmapFont font32;
	private boolean isNewHighScore;
	private int score;
	
	public GameOverScreen(LastDayGame lastDayGame, int score, boolean isNewHighScore){
		this.lastDayGame = lastDayGame;
		this.isNewHighScore = isNewHighScore;
		this.score = score;
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
		font80.setColor(Color.FIREBRICK);
		font80.draw(batch, "Game Over", 570, 800);
		
		font50.setColor(Color.YELLOW);
		font50.draw(batch, "Score : " + score, 600, 600);
		font50.setColor(Color.GREEN);
		if(!isNewHighScore){
			font50.draw(batch, "High Score : " + lastDayGame.getHghScore(), 550, 500);
		} else {
			font50.draw(batch, "!!! New High Score : " + lastDayGame.getHghScore() + " !!!", 400, 500);
		}
		
		font50.setColor(Color.SALMON);
		font50.draw(batch, "Press 'M' to go to Main Menu", 400, 300);
		font50.setColor(Color.FOREST);
		font50.draw(batch, "Press 'P' to Play", 570, 200);
		
		batch.end();
	}

	
	private void update(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
			lastDayGame.goToMainMenu();
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
			lastDayGame.goToMainGame();
		}
	}
}
