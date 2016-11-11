package com.thelastdaytosurvive.game;

import com.badlogic.gdx.utils.TimeUtils;

public class AssaultRifle {
	public static int MAG_CAPACITY = 30;
	public static int MAG_POCKET = 12;
	public static long RELOAD_TIME = 3000;
	public static int DAMAGE = 40;
	public static long FIRERATE = 86; //~700 rpm
	
	private Bullet bullet;
	
	public int currentAmmo;
	public int pocketAmmo;
	public long lastReloadTime;
	public long lastShotTime;
	public boolean isReloading;
	
	public AssaultRifle(Bullet bullet){
		this.bullet = bullet;
		
		currentAmmo = MAG_CAPACITY + 1;
		pocketAmmo = MAG_CAPACITY * MAG_POCKET;
		isReloading = false;
		lastReloadTime = TimeUtils.millis();
		lastShotTime = TimeUtils.millis();
	}
	
	public void update(){
		checkReload();
	}
	
	public void pullTrigger(float x, float y, float rotation){
		if (!isReloading && (currentAmmo > 0) && ((TimeUtils.millis() - lastShotTime) >= FIRERATE)){
			bullet.newBullet(Weapon.ASSAULT_RIFLE, x, y, rotation);
			currentAmmo--;
			lastShotTime = TimeUtils.millis();
		}
	}
	
	public void reload(){
		if (!isReloading && (pocketAmmo > 0) && (currentAmmo != (MAG_CAPACITY + 1))){
			isReloading = true;
			lastReloadTime = TimeUtils.millis();
		}
	}
	
	public void cancleReload(){
		isReloading = false;
	}
	
	public float reloadProgress(){
		return (((float)(TimeUtils.millis() - lastReloadTime) / (float)RELOAD_TIME) * 100f);
	}
	
	private void checkReload(){
		if (isReloading && ((TimeUtils.millis() - lastReloadTime) >= RELOAD_TIME) ){
			if( currentAmmo <= 0){
				emptyReload();
			} else {
				tacticalReload();
			}
			
			isReloading = false;
		}
	}
	
	private void emptyReload(){
		if (pocketAmmo > (MAG_CAPACITY - currentAmmo)){
			pocketAmmo -= (MAG_CAPACITY - currentAmmo); 
			currentAmmo = MAG_CAPACITY;
		} else {
			currentAmmo = pocketAmmo;
			pocketAmmo = 0;
		}
	}
	
	private void tacticalReload(){
		if (pocketAmmo > (MAG_CAPACITY + 1 - currentAmmo)){
			pocketAmmo -= (MAG_CAPACITY + 1 - currentAmmo); 
			currentAmmo = MAG_CAPACITY + 1;
		} else {
			currentAmmo += pocketAmmo;
			pocketAmmo = 0;
		}
	}
}
