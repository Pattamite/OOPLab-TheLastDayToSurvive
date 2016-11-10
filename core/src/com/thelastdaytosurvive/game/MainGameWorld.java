package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameWorld {
	public static final float MAP_X = 3200;
	public static final float MAP_Y = 1800;
	public static final float CAMERA_X = 1600;
	public static final float CAMERA_Y = 900;
	
	private MainGameScreen mainGameScreen;
	public SpriteBatch batch;
	public Player player;
	public Bullet bullet;
	public Weapon weapon;
	public Enemy enemy;
	
	public MainGameWorld(MainGameScreen mainGameScreen){
		this.mainGameScreen = mainGameScreen;
		batch = mainGameScreen.batch;
		
		enemy = new Enemy();
		bullet = new Bullet(enemy);
		weapon = new Weapon(bullet);
		player = new Player(mainGameScreen, weapon);
		
		enemy.player = player;
		
	}
	
	public void update(float delta){
		player.update();
		bullet.update(delta);
		weapon.update(Player.WEAPON_PRIMARY);
		weapon.update(Player.WEAPON_SECONDARY);
		enemy.update(delta);
	}
}
