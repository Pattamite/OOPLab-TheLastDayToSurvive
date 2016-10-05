package com.thelastdaytosurvive.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class LastDayGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture playerTexture;
	Sprite player;
	float playerSpeed = 2f;
	float sqrt2 = 1.41421356237f;
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
	    float h = Gdx.graphics.getHeight();
	    
		batch = new SpriteBatch();
		playerTexture = new Texture("Player/Player.png");
		player = new Sprite(playerTexture);
		player.setPosition(w/2 - player.getWidth()/2, h/2 - player.getHeight()/2);
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float vertiSpeed = 0f;
		float horiSpeed = 0f;
		boolean isVertiMove = false;
		boolean isHoriMove = false;
		if(Gdx.input.isKeyPressed(Input.Keys.W))
		{
			isVertiMove = true;
			vertiSpeed = 1;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S))
		{
			isVertiMove = true;
			vertiSpeed = -1;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D))
		{
			isHoriMove = true;
			horiSpeed = 1;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A))
		{
			isHoriMove = true;
			horiSpeed = -1;
		}
		
		if(isHoriMove && isVertiMove)
		{
			player.translate(horiSpeed * playerSpeed / sqrt2, vertiSpeed * playerSpeed / sqrt2);
		}
		
		else if(isHoriMove || isVertiMove)
		{
			player.translate(horiSpeed * playerSpeed, vertiSpeed * playerSpeed);
		}
		
		
		float playerPosiX = player.getX() + player.getWidth()/2;
		float playerPosiY = ( (player.getY() + player.getHeight()/2) - Gdx.graphics.getHeight()/2) * (-1f) + Gdx.graphics.getHeight()/2;
		float playerRotate = player.getRotation() - 90f;
		float mouseX = Gdx.input.getX();
		float mouseY = Gdx.input.getY();
		float rotateTarget = (float) (Math.atan2((double)(mouseY - playerPosiY) ,(double) (playerPosiX - mouseX))* 180.0d / Math.PI);
		
		player.rotate(rotateTarget - playerRotate);
		
		
		
		batch.begin();
		player.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
