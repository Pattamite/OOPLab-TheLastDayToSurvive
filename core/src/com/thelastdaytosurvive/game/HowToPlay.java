package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HowToPlay extends ScreenAdapter {
	
	private LastDayGame lastDayGame;
	private SpriteBatch batch;
	private BitmapFont font50;
	private Texture texture;
	
	public HowToPlay(LastDayGame lastDayGame){
		this.lastDayGame = lastDayGame;
		font50 = new BitmapFont(Gdx.files.internal("Font/Square50.fnt"));
		texture = new Texture("HowToPlay/Pic.png");
		batch = lastDayGame.batch;
	}
	
	@Override
	public void render(float delta){
		update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		batch.draw(texture, 0, 100);
		font50.setColor(Color.FOREST);
		font50.draw(batch, "Press 'P' to Play", 550, 100);
		
		batch.end();
	}

	
	private void update(){
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
			lastDayGame.goToMainGame();
		}
	}

}
