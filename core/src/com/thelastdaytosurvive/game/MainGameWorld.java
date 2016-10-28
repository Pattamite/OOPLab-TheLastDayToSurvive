package com.thelastdaytosurvive.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGameWorld {
	private MainGameScreen mainGameScreen;
	public SpriteBatch batch;
	public Player player;
	public Bullet bullet;
	public Weapon weapon;
	
	public MainGameWorld(MainGameScreen mainGameScreen){
		this.mainGameScreen = mainGameScreen;
		batch = mainGameScreen.batch;
		
		bullet = new Bullet();
		weapon = new Weapon(bullet);
		player = new Player(this, weapon);
	}
	
	public void update(float delta){
		player.update();
		bullet.update(delta);
		weapon.update(Player.WEAPON_PRIMARY);
		weapon.update(Player.WEAPON_SECONDARY);
	}
}
