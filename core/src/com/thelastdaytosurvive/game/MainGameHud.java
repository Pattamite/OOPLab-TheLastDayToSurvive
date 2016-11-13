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
	private MainGameScreen mainGameScreen;
	
	public MainGameHud(Player player, MainGameScreen mainGameScreen){
		font32 = new BitmapFont(Gdx.files.internal("Font/Cloud32.fnt"));
		font14 = new BitmapFont(Gdx.files.internal("Font/Cloud14.fnt"));
		reloadBarTexture = new Texture(Gdx.files.internal("ReloadBar/ReloadBar.png"));
		progressTexture = new Texture(Gdx.files.internal("ReloadBar/Progress.png"));
		
		this.player = player;
		this.mainGameScreen = mainGameScreen;
		font32.setColor(Color.BLACK);
	}
	
	public void draw(SpriteBatch batch){
		ammoCount(batch);
		reloadBar(batch);
	}
	
	private void ammoCount(SpriteBatch batch){
		int currentAmmo = player.getAmmoCount();
		int pocketAmmo = player.getPocketCount();
		
		font32.draw(batch, currentAmmo + " / " + pocketAmmo, mainGameScreen.screenPositionX(1480)
					, mainGameScreen.screenPositionY(40));
	}
	
	private void reloadBar(SpriteBatch batch){
		if (player.isReloading()){
			float x = player.getSprite().getX() - 20;
			float y = player.getSprite().getY() - 20;
			float value = player.reloadProgress();
			
			batch.draw(reloadBarTexture, x, y);
			batch.draw(progressTexture, x + 2, y + 2, value, 4);
		} else if (player.getAmmoCount() <= 0){
			float x = player.getSprite().getX() - 20;
			float y = player.getSprite().getY() - 20;
			font32.draw(batch, "Reload", x, y);
		}	
	}
}
