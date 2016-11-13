package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameWorld {
	public static final float MAP_X = 3200;
	public static final float MAP_Y = 1800;
	public static final float CAMERA_X = 1600;
	public static final float CAMERA_Y = 900;
	
	private MainGameScreen mainGameScreen;
	private SpriteBatch batch;
	private Player player;
	private Bullet bullet;
	private Weapon weapon;
	private Enemy enemy;
	
	public MainGameWorld(MainGameScreen mainGameScreen){
		this.mainGameScreen = mainGameScreen;
		batch = mainGameScreen.getSpriteBatch();
		
		enemy = new Enemy();
		bullet = new Bullet(enemy);
		weapon = new Weapon(bullet);
		player = new Player(mainGameScreen, weapon);
		
		enemy.setUp(player);
		
	}
	
	public void update(float delta){
		player.update();
		bullet.update(delta);
		weapon.update(Player.WEAPON_PRIMARY);
		weapon.update(Player.WEAPON_SECONDARY);
		enemy.update(delta);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public SpriteBatch getSpriteBatch(){
		return batch;
	}
	
	public Bullet getBullet(){
		return bullet;
	}
	
	public Weapon getWeapon(){
		return weapon;
	}
	
	public Enemy getEnemy(){
		return enemy;
	}
}
