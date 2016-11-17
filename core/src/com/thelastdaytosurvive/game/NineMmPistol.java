package com.thelastdaytosurvive.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;

public class NineMmPistol {
	public static int MAG_CAPACITY = 15;
	public static long RELOAD_TIME = 1500;
	public static int DAMAGE = 25;
	
	private Bullet bullet;
	
	private int currentAmmo;
	private int pocketAmmo;
	private long lastReloadTime;
	private boolean isReloading;
	private boolean isReady;
	
	private Sound fireSound;
	private Sound emptySound;
	private Sound reloadSound;
	private float fireSoundVolume = 0.3f;
	private float emptySoundVolume = 1;
	private float reloadSoundVolume = 1;
	
	public NineMmPistol(Bullet bullet){
		this.bullet = bullet;
		
		currentAmmo = MAG_CAPACITY + 1;
		pocketAmmo = -1;
		isReloading = false;
		isReady = true;
		lastReloadTime = TimeUtils.millis();
		
		fireSound = Gdx.audio.newSound(Gdx.files.internal("Sound/9mm.wav"));
		emptySound = Gdx.audio.newSound(Gdx.files.internal("Sound/EmptyShot.wav"));
		reloadSound = Gdx.audio.newSound(Gdx.files.internal("Sound/9mmReload.wav"));
	}
	
	public void update(){
		checkReload();
	}
	
	public void pullTrigger(float x, float y, float rotation){
		if(isReady && !isReloading && (currentAmmo <= 0)){
			emptySound.play(emptySoundVolume);
			isReady = false;
		} else if (isReady && !isReloading && (currentAmmo > 0) ){
			bullet.newBullet(Weapon.NINEMM_PISTOL, x, y, rotation);
			currentAmmo--;
			isReady = false;
			fireSound.play(fireSoundVolume);
		}

	}
	
	public void releaseTrigger(){
		isReady = true;
	}
	
	public void reload(){
		if (!isReloading && (currentAmmo != (MAG_CAPACITY + 1))){
			isReloading = true;
			lastReloadTime = TimeUtils.millis();
			reloadSound.play(reloadSoundVolume);
		}
	}
	
	public void cancleReload(){
		isReloading = false;
		reloadSound.stop();
	}
	
	public float reloadProgress(){
		return (((float)(TimeUtils.millis() - lastReloadTime) / (float)RELOAD_TIME) * 100f);
	}
	
	public int getCurrentAmmo(){
		return currentAmmo;
	}
	
	public int getPocketAmmo(){
		return pocketAmmo;
	}
	
	public boolean isReloading(){
		return isReloading;
	}
	
	private void checkReload(){
		if (isReloading && ((TimeUtils.millis() - lastReloadTime) >= RELOAD_TIME) ){
			if(currentAmmo <= 0){
				currentAmmo = MAG_CAPACITY;
			} else {
				currentAmmo = MAG_CAPACITY + 1;
			}
			
			isReloading = false;
		}
	}
}
