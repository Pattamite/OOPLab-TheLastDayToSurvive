package com.thelastdaytosurvive.game;

public class Weapon {
	public final static int ASSAULT_RIFLE = 0;
	public final static int NINEMM_PISTOL = 1;
	

	public NineMmPistol nineMmPistol;
	public AssaultRifle assaultRifle;
	
	public Weapon(Bullet bullet){
		nineMmPistol = new NineMmPistol(bullet);
		assaultRifle = new AssaultRifle(bullet);
	}
	
	public void update(int type){
		switch(type){
			case ASSAULT_RIFLE : assaultRifle.update(); break;
			case NINEMM_PISTOL : nineMmPistol.update(); break;
			default : break;
		}
	}
	
	public void pullTrigger(int type, float x, float y, float rotation){
		switch(type){
			case ASSAULT_RIFLE : assaultRifle.pullTrigger(x, y, rotation); break;
			case NINEMM_PISTOL : nineMmPistol.pullTrigger(x, y, rotation); break;
			default : break;
		}
		
	}
	
	public void releaseTrigger(int type){
		switch(type){
			case NINEMM_PISTOL : nineMmPistol.releaseTrigger(); break;
			default : break;
		}
	}
	
	public void reload(int type){
		switch(type){
			case ASSAULT_RIFLE : assaultRifle.reload(); break;
			case NINEMM_PISTOL : nineMmPistol.reload(); break;
			default : break;
		}
	}
	
	public void cancleReload(int type){
		switch(type){
		case ASSAULT_RIFLE : assaultRifle.cancleReload(); break;
		case NINEMM_PISTOL : nineMmPistol.cancleReload(); break;
		default : break;
	}
	}
	
	public void grabOneMag(){
		
	}
	
	public void grabFullMag(){
		
	}
	
	public int getAmmoCount(int type){
		switch(type){
			case ASSAULT_RIFLE : return assaultRifle.currentAmmo; 
			case NINEMM_PISTOL : return nineMmPistol.currentAmmo; 
			default : return -1;
		}
	}
	
	public int getPocketCount(int type){
		switch(type){
			case ASSAULT_RIFLE : return assaultRifle.pocketAmmo; 
			default : return -1;
		}
	}
}
