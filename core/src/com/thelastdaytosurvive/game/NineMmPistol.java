package com.thelastdaytosurvive.game;

import com.badlogic.gdx.utils.TimeUtils;

public class NineMmPistol {
	public static int MAG_CAPACITY = 15;
	public static long RELOAD_TIME = 1500;
	public static int DAMAGE = 25;
	
	private Bullet bullet;
	
	public int currentAmmo;
	public int pocketAmmo;
	public long lastReloadTime;
	public boolean isReloading;
	public boolean isReady;
	
	public NineMmPistol(Bullet bullet){
		this.bullet = bullet;
		
		currentAmmo = MAG_CAPACITY + 1;
		pocketAmmo = -1;
		isReloading = false;
		isReady = true;
		lastReloadTime = TimeUtils.millis();
	}
	
	public void update(){
		checkReload();
	}
	
	public void pullTrigger(float x, float y, float rotation){
		if(isReady && !isReloading && (currentAmmo > 0) ){
			bullet.newBullet(Weapon.NINEMM_PISTOL, x, y, rotation);
			currentAmmo--;
			isReady = false;
		}
	}
	
	public void releaseTrigger(){
		isReady = true;
	}
	
	public void reload(){
		if(!isReloading){
			isReloading = true;
			lastReloadTime = TimeUtils.millis();
		}
	}
	
	public void cancleReload(){
		isReloading = false;
	}
	
	private void checkReload(){
		if(isReloading && ((TimeUtils.millis() - lastReloadTime) >= RELOAD_TIME) ){
			if(currentAmmo <= 0){
				currentAmmo = MAG_CAPACITY;
			}
			else{
				currentAmmo = MAG_CAPACITY + 1;
			}
			isReloading = false;
		}
	}
}
