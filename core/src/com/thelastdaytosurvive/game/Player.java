package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player {
	
	private Texture playerTexture;
	public Sprite playerSprite;
	public float playerSpeed = 160f;
	private float sqrt2 = 1.41421356237f;
	
	public Player(){
		playerTexture = new Texture("Player/Player.png");
		playerSprite = new Sprite(playerTexture);
		playerSprite.setPosition(Gdx.graphics.getWidth()/2 - playerSprite.getWidth()/2, Gdx.graphics.getHeight()/2 - playerSprite.getHeight()/2);
	}
	
	public void update(){
		updatePosition();
		updateRotation();
	}
	
	private void updatePosition(){
		float vertiSpeed = 0f;
		float horiSpeed = 0f;
		boolean isVertiMove = false;
		boolean isHoriMove = false;

		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			isVertiMove = true;
			vertiSpeed = 1;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			isVertiMove = true;
			vertiSpeed = -1;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			isHoriMove = true;
			horiSpeed = 1;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A)){
			isHoriMove = true;
			horiSpeed = -1;
		}
		
		if(isHoriMove && isVertiMove){
			playerSprite.translate(horiSpeed * playerSpeed / sqrt2 * Gdx.graphics.getDeltaTime(), vertiSpeed * playerSpeed / sqrt2* Gdx.graphics.getDeltaTime());
		}
		
		else if(isHoriMove || isVertiMove){
			playerSprite.translate(horiSpeed * playerSpeed * Gdx.graphics.getDeltaTime(), vertiSpeed * playerSpeed * Gdx.graphics.getDeltaTime());
		}
	}
	
	private void updateRotation(){
		float playerPosiX = playerSprite.getX() + playerSprite.getWidth() / 2;
		float playerPosiY = ( (playerSprite.getY() + playerSprite.getHeight() / 2) - Gdx.graphics.getHeight() / 2) * (-1f) + Gdx.graphics.getHeight() / 2;
		float playerRotate = playerSprite.getRotation() - 90f;
		float mouseX = Gdx.input.getX();
		float mouseY = Gdx.input.getY();
		float rotateTarget = (float) (Math.atan2((double)(mouseY - playerPosiY) ,(double) (playerPosiX - mouseX)) * 180.0d / Math.PI);
		
		playerSprite.rotate(rotateTarget - playerRotate);
	}
	
}
