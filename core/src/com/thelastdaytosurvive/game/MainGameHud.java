package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameHud {
	private BitmapFont font32;
	private BitmapFont font14;
	private Player player;
	
	public MainGameHud(Player player){
		font32 = new BitmapFont(Gdx.files.internal("Font/Cloud32.fnt"));
		font14 = new BitmapFont(Gdx.files.internal("Font/Cloud14.fnt"));
		this.player = player;
		font32.setColor(Color.BLACK);
	}
	
	public void draw(SpriteBatch batch){
		ammoCount(batch);
	}
	
	private void ammoCount(SpriteBatch batch){
		int currentAmmo = player.getAmmoCount();
		int pocketAmmo = player.getPocketCount();
		
		font32.draw(batch, currentAmmo + " / " + pocketAmmo, 0, 40);
	}
}
