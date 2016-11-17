package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameHud {
	private BitmapFont font50;
	private BitmapFont font32;
	private BitmapFont font20;
	private Texture reloadBarTexture;
	private Texture progressTexture;
	private Texture ammoBg;
	private Texture healthBg;
	private Texture phaseBg;
	private Texture resourceBg;
	private Texture scoreBg;
	private Texture fenceHealthBarTexture;
	private Texture fenceHealthTexture;
	
	private MainGameScreen mainGameScreen;
	private MainGameWorld mainGameWorld;
	
	public MainGameHud(MainGameWorld mainGameWorld, MainGameScreen mainGameScreen){
		this.mainGameWorld = mainGameWorld;
		this.mainGameScreen = mainGameScreen;
		
		setUpFont();
		setUpTexture();
	}
	
	public void draw(SpriteBatch batch){
		fenceHealth(batch);
		healthBar(batch);
		ammoCount(batch);
		reloadBar(batch);
		resourceBar(batch);
		phaseBar(batch);
		scoreBar(batch);
		//aiMap(batch);
	}
	
	private void setUpFont(){
		font50  = new BitmapFont(Gdx.files.internal("Font/Square50.fnt"));
		font32 = new BitmapFont(Gdx.files.internal("Font/Square32.fnt"));
		font20 = new BitmapFont(Gdx.files.internal("Font/Square20.fnt"));
		font50.setColor(Color.WHITE);
		font32.setColor(Color.WHITE);
	}
	
	private void setUpTexture(){
		reloadBarTexture = new Texture(Gdx.files.internal("ReloadBar/ReloadBar.png"));
		progressTexture = new Texture(Gdx.files.internal("ReloadBar/Progress.png"));
		ammoBg = new Texture(Gdx.files.internal("HUD/Ammo.png"));
		healthBg = new Texture(Gdx.files.internal("HUD/Health.png"));
		phaseBg = new Texture(Gdx.files.internal("HUD/Phase.png"));
		resourceBg = new Texture(Gdx.files.internal("HUD/Resource.png"));
		scoreBg = new Texture(Gdx.files.internal("HUD/Score.png"));
		fenceHealthBarTexture = new Texture(Gdx.files.internal("FenceHealthBar/Bar.png"));
		fenceHealthTexture = new Texture(Gdx.files.internal("FenceHealthBar/Health.png"));
	}
	
	private void fenceHealth(SpriteBatch batch){
		for(int i = 0 ; i < Map.MAP_YNUM ; i++){
			for(int j = 0 ; j < Map.MAP_XNUM ; j++){
				if (mainGameWorld.getMap().getWorldMap()[i][j] == Map.MAP_WFENCE_ORIGIN_VERTICAL
						|| mainGameWorld.getMap().getWorldMap()[i][j] == Map.MAP_WFENCE_ORIGIN_HORIZONTAL){
					batch.draw(fenceHealthBarTexture, mainGameWorld.getMap().xGamePosition(j)
							, mainGameWorld.getMap().yGamePosition(i));
					batch.draw(fenceHealthTexture, mainGameWorld.getMap().xGamePosition(j) + 2
							, mainGameWorld.getMap().yGamePosition(i) + 2
							, (float) mainGameWorld.getMap().getFenceHealth()[i][j] / (float) Crafting.CRAFTING_WFENCE_HEALTH * 50f, 4);
				} else if (mainGameWorld.getMap().getWorldMap()[i][j] == Map.MAP_MFENCE_ORIGIN_VERTICAL
						|| mainGameWorld.getMap().getWorldMap()[i][j] == Map.MAP_MFENCE_ORIGIN_HORIZONTAL){
					batch.draw(fenceHealthBarTexture, mainGameWorld.getMap().xGamePosition(j)
							, mainGameWorld.getMap().yGamePosition(i));
					batch.draw(fenceHealthTexture, mainGameWorld.getMap().xGamePosition(j) + 2
							, mainGameWorld.getMap().yGamePosition(i) + 2
							, (float) mainGameWorld.getMap().getFenceHealth()[i][j] / (float) Crafting.CRAFTING_MFENCE_HEALTH * 50f, 4);
				}
			}
		}
	}
	
	private void healthBar(SpriteBatch batch){
		batch.draw(healthBg, mainGameScreen.screenPositionX(mainGameWorld.CAMERA_X - healthBg.getWidth())
				,  mainGameScreen.screenPositionY(ammoBg.getHeight()));
		font32.draw(batch, "" + mainGameWorld.getPlayer().getHealth(), mainGameScreen.screenPositionX(1450)
				, mainGameScreen.screenPositionY(86));
	}
	
	private void ammoCount(SpriteBatch batch){
		int currentAmmo = mainGameWorld.getPlayer().getAmmoCount();
		int pocketAmmo = mainGameWorld.getPlayer().getPocketCount();
		
		batch.draw(ammoBg, mainGameScreen.screenPositionX(mainGameWorld.CAMERA_X - ammoBg.getWidth())
				,  mainGameScreen.screenPositionY(0));
		
		font50.draw(batch, currentAmmo + "", mainGameScreen.screenPositionX(1400)
					, mainGameScreen.screenPositionY(40));
		if (pocketAmmo == -1) {
			font32.draw(batch, "/ Inf", mainGameScreen.screenPositionX(1470), mainGameScreen.screenPositionY(30));
		} else {
			font32.draw(batch, "/ " + pocketAmmo, mainGameScreen.screenPositionX(1470), mainGameScreen.screenPositionY(30));
		}
	}
	
	private void reloadBar(SpriteBatch batch){
		if (mainGameWorld.getPlayer().isReloading()){
			float x = mainGameWorld.getPlayer().getSprite().getX() - 20;
			float y = mainGameWorld.getPlayer().getSprite().getY() - 20;
			float value = mainGameWorld.getPlayer().reloadProgress();
			
			batch.draw(reloadBarTexture, x, y);
			batch.draw(progressTexture, x + 2, y + 2, value, 4);
		} else if (mainGameWorld.getPlayer().getAmmoCount() <= 0){
			float x = mainGameWorld.getPlayer().getSprite().getX() - 4;
			float y = mainGameWorld.getPlayer().getSprite().getY() - 10;
			font20.draw(batch, "Reload", x, y);
		}	
	}
	
	private void resourceBar(SpriteBatch batch){
		batch.draw(resourceBg, mainGameScreen.screenPositionX(0),  mainGameScreen.screenPositionY(0));
		
		font32.draw(batch, "" + mainGameWorld.getCrafting().getCurrentWood()
				, mainGameScreen.screenPositionX(100), mainGameScreen.screenPositionY(90));
		font32.draw(batch, "" + mainGameWorld.getCrafting().getCurrentMetal()
				, mainGameScreen.screenPositionX(100), mainGameScreen.screenPositionY(45));
	}
	
	private void phaseBar(SpriteBatch batch){
		batch.draw(phaseBg, mainGameScreen.screenPositionX(0)
				,  mainGameScreen.screenPositionY(mainGameWorld.CAMERA_Y - phaseBg.getHeight()));
		
		if (mainGameWorld.getTracker().getPhase() == MainGameTracker.PHASE_PREP_TYPE){
			font32.draw(batch, "PREP PHASE"
					, mainGameScreen.screenPositionX(20), mainGameScreen.screenPositionY(880));
			font32.draw(batch, "Time : " + mainGameWorld.getTracker().getPrepTime()
					, mainGameScreen.screenPositionX(20), mainGameScreen.screenPositionY(840));
		}
		else if (mainGameWorld.getTracker().getPhase() == MainGameTracker.PHASE_COMBAT_TYPE){
			font32.draw(batch, "COMBAT PHASE"
					, mainGameScreen.screenPositionX(20), mainGameScreen.screenPositionY(880));
			font32.draw(batch, "Zombies : " + mainGameWorld.getTracker().getRemainEnemy()
					, mainGameScreen.screenPositionX(20), mainGameScreen.screenPositionY(840));
		}
	}
	
	private void scoreBar(SpriteBatch batch){
		batch.draw(scoreBg, mainGameScreen.screenPositionX(mainGameWorld.CAMERA_X - scoreBg.getWidth())
				,  mainGameScreen.screenPositionY(mainGameWorld.CAMERA_Y - scoreBg.getHeight()));
		font32.draw(batch, "Score : " + mainGameWorld.getTracker().getScore()
				, mainGameScreen.screenPositionX(1250), mainGameScreen.screenPositionY(880));
	}
	
	private void aiMap(SpriteBatch batch){
		for(int i = 0; i < Map.MAP_YNUM ; i++){
			for(int j = 0 ; j < Map.MAP_XNUM ; j++){
				font32.draw(batch, "" + mainGameWorld.getMap().getAIMap()[i][j]
						, mainGameWorld.getMap().xGamePosition(j), mainGameWorld.getMap().yGamePosition(i));
			}
		}
	}
}
