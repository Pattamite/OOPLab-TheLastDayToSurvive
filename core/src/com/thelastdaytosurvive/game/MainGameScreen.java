package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameScreen extends ScreenAdapter {
	
	private LastDayGame lastDayGame;
	private MainGameWorld mainGameWorld;
	private MainGameWorldRenderer mainGameWorldRenderer;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	
	public MainGameScreen(LastDayGame lastDayGame){
		this.lastDayGame = lastDayGame;
		mainGameWorld = new MainGameWorld(this);
		mainGameWorldRenderer = new MainGameWorldRenderer(this, mainGameWorld);
		
		batch = lastDayGame.batch;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MainGameWorld.CAMERA_X, MainGameWorld.CAMERA_Y);
	}
	
	@Override
	public void render(float delta){
		mainGameWorld.update(delta);
		
		cameraUpdate();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		mainGameWorldRenderer.draw(delta, batch);
		batch.end();
	}
	
	private void cameraUpdate(){
		float x = mainGameWorld.getPlayer().playerSprite.getX();
		if (x < (MainGameWorld.CAMERA_X / 2)){
			x = (MainGameWorld.CAMERA_X / 2);
		} else if (x > (MainGameWorld.MAP_X - (MainGameWorld.CAMERA_X / 2))){
			x = (MainGameWorld.MAP_X - (MainGameWorld.CAMERA_X / 2));
		}
		
		float y = mainGameWorld.getPlayer().playerSprite.getY();
		
		if (y < (MainGameWorld.CAMERA_Y / 2)){
			y = (MainGameWorld.CAMERA_Y / 2);
		} else if (y > (MainGameWorld.MAP_Y - (MainGameWorld.CAMERA_Y / 2))){
			y = (MainGameWorld.MAP_Y - (MainGameWorld.CAMERA_Y / 2));
		}
		
		camera.position.set(x, y, 0);
		camera.update();
	}
	
	public float screenPositionX (float value){
		return camera.position.x - (MainGameWorld.CAMERA_X  / 2) + value;
	}
	
	public float screenPositionY (float value){
		return camera.position.y - (MainGameWorld.CAMERA_Y  / 2) + value;
	}
	
	public float gamePositionX (float value){
		return value + camera.position.x - (MainGameWorld.CAMERA_X / 2);
	}
	
	public float gamePositionY (float value){
		return value + camera.position.y - (MainGameWorld.CAMERA_Y / 2);
	}
}
