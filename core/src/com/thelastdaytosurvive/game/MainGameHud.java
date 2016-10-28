package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameHud {
	private BitmapFont font32;
	private BitmapFont font14;
	private Texture reloadBarTexture;
	private Texture progressTexture;
	
	private Player player;
	
	public MainGameHud(Player player){
		font32 = new BitmapFont(Gdx.files.internal("Font/Cloud32.fnt"));
		font14 = new BitmapFont(Gdx.files.internal("Font/Cloud14.fnt"));
		reloadBarTexture = new Texture(Gdx.files.internal("ReloadBar/ReloadBar.png"));
		progressTexture = new Texture(Gdx.files.internal("ReloadBar/Progress.png"));
		
		this.player = player;
		font32.setColor(Color.BLACK);
	}
	
	public void draw(SpriteBatch batch){
		ammoCount(batch);
		reloadBar(batch);
	}
	
	private void ammoCount(SpriteBatch batch){
		int currentAmmo = player.getAmmoCount();
		int pocketAmmo = player.getPocketCount();
		
		font32.draw(batch, currentAmmo + " / " + pocketAmmo, 1480, 40);
	}
	
	private void reloadBar(SpriteBatch batch){
		if(player.isReloading()){
			float x = player.playerSprite.getX() - 20;
			float y = player.playerSprite.getY() - 20;
			float value = player.reloadProgress();
			System.out.println("" + value);
			
			batch.draw(reloadBarTexture, x, y);
			batch.draw(progressTexture, x + 2, y + 2, value, 4);
		}
	}
}
